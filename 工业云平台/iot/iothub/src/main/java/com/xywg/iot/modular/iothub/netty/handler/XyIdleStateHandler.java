package com.xywg.iot.modular.iothub.netty.handler;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.xywg.iot.dto.DataDTO;
import com.xywg.iot.enums.Method;
import com.xywg.iot.modular.product.model.ProductInfo;
import com.xywg.iot.modular.product.service.IProductInfoService;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author : wangyifei
 * Description  继承ChannelDuplexHandler,
 * 连接后，默认闲置时间10秒
 * 登录后 闲置时间为产品上配置的时间
 * 超过闲置踢出
 * Date: Created in 10:09 2018/12/18
 * Modified By : wangyifei
 */
@Component
@Scope("prototype")
public class XyIdleStateHandler extends ChannelDuplexHandler {
    private boolean first =true;
    private static final long MIN_TIMEOUT_NANOS;
    private final ChannelFutureListener writeListener;
    private final boolean observeOutput;
    private  long readerIdleTimeNanos;
    private final long writerIdleTimeNanos;
    private final long allIdleTimeNanos;
    private ScheduledFuture<?> readerIdleTimeout;
    private long lastReadTime;
    private boolean firstReaderIdleEvent;
    private ScheduledFuture<?> writerIdleTimeout;
    private long lastWriteTime;
    private boolean firstWriterIdleEvent;
    private ScheduledFuture<?> allIdleTimeout;
    private boolean firstAllIdleEvent;
    private byte state;
    private boolean reading;
    private long lastChangeCheckTimeStamp;
    private int lastMessageHashCode;
    private long lastPendingWriteBytes;
    /**
     *  产品
     */
    @Autowired
    private IProductInfoService productService;


    public XyIdleStateHandler() {
        this(0,0,0);
    }

    public XyIdleStateHandler(int readerIdleTimeSeconds, int writerIdleTimeSeconds, int allIdleTimeSeconds) {
        this((long)readerIdleTimeSeconds, (long)writerIdleTimeSeconds, (long)allIdleTimeSeconds, TimeUnit.SECONDS);
    }

    public XyIdleStateHandler(long readerIdleTime, long writerIdleTime, long allIdleTime, TimeUnit unit) {
        this(false, readerIdleTime, writerIdleTime, allIdleTime, unit);
    }

    public XyIdleStateHandler(boolean observeOutput, long readerIdleTime, long writerIdleTime, long allIdleTime, TimeUnit unit) {
        this.writeListener = new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                XyIdleStateHandler.this.lastWriteTime = XyIdleStateHandler.this.ticksInNanos();
                XyIdleStateHandler.this.firstWriterIdleEvent = XyIdleStateHandler.this.firstAllIdleEvent = true;
            }
        };
        this.firstReaderIdleEvent = true;
        this.firstWriterIdleEvent = true;
        this.firstAllIdleEvent = true;
        if (unit == null) {
            throw new NullPointerException("unit");
        } else {
            this.observeOutput = observeOutput;
            if (readerIdleTime <= 0L) {
                this.readerIdleTimeNanos = 0L;
            } else {
                this.readerIdleTimeNanos = Math.max(unit.toNanos(readerIdleTime), MIN_TIMEOUT_NANOS);
            }

            if (writerIdleTime <= 0L) {
                this.writerIdleTimeNanos = 0L;
            } else {
                this.writerIdleTimeNanos = Math.max(unit.toNanos(writerIdleTime), MIN_TIMEOUT_NANOS);
            }

            if (allIdleTime <= 0L) {
                this.allIdleTimeNanos = 0L;
            } else {
                this.allIdleTimeNanos = Math.max(unit.toNanos(allIdleTime), MIN_TIMEOUT_NANOS);
            }

        }
    }

    public long getReaderIdleTimeInMillis() {
        return TimeUnit.NANOSECONDS.toMillis(this.readerIdleTimeNanos);
    }

    public long getWriterIdleTimeInMillis() {
        return TimeUnit.NANOSECONDS.toMillis(this.writerIdleTimeNanos);
    }

    public long getAllIdleTimeInMillis() {
        return TimeUnit.NANOSECONDS.toMillis(this.allIdleTimeNanos);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        this.destroy();
    }
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {


        super.channelRegistered(ctx);
    }
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        this.initialize(ctx);
        super.channelActive(ctx);
    }
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        this.destroy();
        super.channelInactive(ctx);
    }
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(first){
            ByteBuf buf  = (ByteBuf) msg;
            int len = buf.readableBytes();

            int start  =  buf.readerIndex();
            byte[] msgData = new byte[len];
            buf.readBytes(msgData);
            String data  = new String(msgData);
            DataDTO dataDTO = JSONUtil.toBean(data,DataDTO.class);
            if(Method.LOGIN.getValue().equals(dataDTO.getMethod())){
             String productKey =   dataDTO.getParams().get("productKey");
                Wrapper<ProductInfo> wrapper = new EntityWrapper<>();
                wrapper.eq("product_key",productKey);
                ProductInfo product =     productService.selectOne(wrapper);
                this.first = false;
                this.destroy();
                this.state= 0 ;
                this.readerIdleTimeNanos = TimeUnit.SECONDS.toNanos(product.getReaderIdleTime());
                //this.readerIdleTimeNanos = product.getReaderIdleTime() *1000000000L;
                initialize(ctx);
            }
            //可读位置 回到开始位置
            buf.readerIndex(start);

        }else{
            if (this.readerIdleTimeNanos > 0L ) {
                this.reading = true;
                this.firstReaderIdleEvent = this.firstAllIdleEvent = true;
            }
        }


        ctx.fireChannelRead(msg);
    }
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        if ((this.readerIdleTimeNanos > 0L ) && this.reading) {
            this.lastReadTime = this.ticksInNanos();
            this.reading = false;
        }

        ctx.fireChannelReadComplete();
    }
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (this.writerIdleTimeNanos <= 0L && this.allIdleTimeNanos <= 0L) {
            ctx.write(msg, promise);
        } else {
            ctx.write(msg, promise.unvoid()).addListener(this.writeListener);
        }

    }

    private void initialize(ChannelHandlerContext ctx) {
        if(this.first){
             this.readerIdleTimeNanos = TimeUnit.SECONDS.toNanos(100L);
        }
        switch(this.state) {
            case 1:
            case 2:
                return;
            default:
                this.state = 1;
                this.initOutputChanged(ctx);
                this.lastReadTime = this.lastWriteTime = this.ticksInNanos();
                if (this.readerIdleTimeNanos > 0L) {
                    System.out.println(this.getReaderIdleTimeInMillis());
                    this.readerIdleTimeout = this.schedule(ctx, new XyIdleStateHandler.ReaderIdleTimeoutTask(ctx), this.readerIdleTimeNanos, TimeUnit.NANOSECONDS);
                }
        }
    }

    long ticksInNanos() {
        return System.nanoTime();
    }

    ScheduledFuture<?> schedule(ChannelHandlerContext ctx, Runnable task, long delay, TimeUnit unit) {
        return ctx.executor().schedule(task, delay, unit);
    }

    private void destroy() {
        this.state = 2;
        if (this.readerIdleTimeout != null) {
            this.readerIdleTimeout.cancel(false);
            this.readerIdleTimeout = null;
        }

        if (this.writerIdleTimeout != null) {
            this.writerIdleTimeout.cancel(false);
            this.writerIdleTimeout = null;
        }

        if (this.allIdleTimeout != null) {
            this.allIdleTimeout.cancel(false);
            this.allIdleTimeout = null;
        }

    }

    protected void channelIdle(ChannelHandlerContext ctx, IdleStateEvent evt) throws Exception {
        ctx.fireUserEventTriggered(evt);
    }

    protected IdleStateEvent newIdleStateEvent(IdleState state, boolean first) {
        switch(state) {
            case ALL_IDLE:
                return first ? IdleStateEvent.FIRST_ALL_IDLE_STATE_EVENT : IdleStateEvent.ALL_IDLE_STATE_EVENT;
            case READER_IDLE:
                return first ? IdleStateEvent.FIRST_READER_IDLE_STATE_EVENT : IdleStateEvent.READER_IDLE_STATE_EVENT;
            case WRITER_IDLE:
                return first ? IdleStateEvent.FIRST_WRITER_IDLE_STATE_EVENT : IdleStateEvent.WRITER_IDLE_STATE_EVENT;
            default:
                throw new IllegalArgumentException("Unhandled: state=" + state + ", first=" + first);
        }
    }

    private void initOutputChanged(ChannelHandlerContext ctx) {
        if (this.observeOutput) {
            Channel channel = ctx.channel();
            Channel.Unsafe unsafe = channel.unsafe();
            ChannelOutboundBuffer buf = unsafe.outboundBuffer();
            if (buf != null) {
                this.lastMessageHashCode = System.identityHashCode(buf.current());
                this.lastPendingWriteBytes = buf.totalPendingWriteBytes();
            }
        }

    }

    private boolean hasOutputChanged(ChannelHandlerContext ctx, boolean first) {
        if (this.observeOutput) {
            if (this.lastChangeCheckTimeStamp != this.lastWriteTime) {
                this.lastChangeCheckTimeStamp = this.lastWriteTime;
                if (!first) {
                    return true;
                }
            }

            Channel channel = ctx.channel();
            Channel.Unsafe unsafe = channel.unsafe();
            ChannelOutboundBuffer buf = unsafe.outboundBuffer();
            if (buf != null) {
                int messageHashCode = System.identityHashCode(buf.current());
                long pendingWriteBytes = buf.totalPendingWriteBytes();
                if (messageHashCode != this.lastMessageHashCode || pendingWriteBytes != this.lastPendingWriteBytes) {
                    this.lastMessageHashCode = messageHashCode;
                    this.lastPendingWriteBytes = pendingWriteBytes;
                    if (!first) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    static {
        MIN_TIMEOUT_NANOS = TimeUnit.MILLISECONDS.toNanos(1L);
    }





    private final class ReaderIdleTimeoutTask extends XyIdleStateHandler.AbstractIdleTask {
        ReaderIdleTimeoutTask(ChannelHandlerContext ctx) {
            super(ctx);
        }
        @Override
        protected void run(ChannelHandlerContext ctx) {
            long nextDelay = XyIdleStateHandler.this.readerIdleTimeNanos;
            if (!XyIdleStateHandler.this.reading) {
                nextDelay -= XyIdleStateHandler.this.ticksInNanos() - XyIdleStateHandler.this.lastReadTime;
            }

            if (nextDelay <= 0L) {
                XyIdleStateHandler.this.readerIdleTimeout = XyIdleStateHandler.this.schedule(ctx, this, XyIdleStateHandler.this.readerIdleTimeNanos, TimeUnit.NANOSECONDS);
                boolean first = XyIdleStateHandler.this.firstReaderIdleEvent;
                XyIdleStateHandler.this.firstReaderIdleEvent = false;

                try {
                    IdleStateEvent event = XyIdleStateHandler.this.newIdleStateEvent(IdleState.READER_IDLE, first);
                    XyIdleStateHandler.this.channelIdle(ctx, event);
                } catch (Throwable var6) {
                    ctx.fireExceptionCaught(var6);
                }
            } else {
                XyIdleStateHandler.this.readerIdleTimeout = XyIdleStateHandler.this.schedule(ctx, this, nextDelay, TimeUnit.NANOSECONDS);
            }

        }
    }

    private abstract static class AbstractIdleTask implements Runnable {
        private final ChannelHandlerContext ctx;

        AbstractIdleTask(ChannelHandlerContext ctx) {
            this.ctx = ctx;
        }
        @Override
        public void run() {
            if (this.ctx.channel().isOpen()) {
                this.run(this.ctx);
            }
        }

        protected abstract void run(ChannelHandlerContext var1);
    }
}
