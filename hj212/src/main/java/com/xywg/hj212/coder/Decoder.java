package com.xywg.hj212.coder;

import cn.hutool.core.util.StrUtil;
import com.xywg.hj212.common.enums.LogEnum;
import com.xywg.hj212.common.exceptions.CloseChannelException;
import com.xywg.hj212.pojo.Hj212Request;
import com.xywg.hj212.utils.CRC16Util;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

import static com.xywg.hj212.common.Const.*;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 14:08 2019/4/9
 * Modified By : wangyifei
 */
@Slf4j
@ChannelHandler.Sharable
public class Decoder extends MessageToMessageDecoder<String> {



    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, String data, List<Object> list) throws Exception {
        Hj212Request hj212Request = new Hj212Request();
        //校验长度
        int contentLength  =  validLength(data, hj212Request);
        //头部校验
        validHeader(data,hj212Request);
        // 校验位检验
        validCRC(contentLength,data,hj212Request);
        list.add(hj212Request);
    }

    private void validHeader(String data,Hj212Request hj212Request){
        if(!data.startsWith(HEAD)){
            String log = LogEnum.COMMON.format("数据异常","缺少头部##");
            throw new CloseChannelException(log);
        }
        hj212Request.setHeader(HEAD);
    }

    /**
     *  数据长度 = 头（2）+ 长度(4)+数据(N)+crc(4)
     * @param data 原始数据
     * @return  内容长度
     */
    private int validLength(String data,Hj212Request hj212Request){
        // 原始数据长度
        int dataLength =  data.length();
        // 解析到的 内容体长度
        String cutContentLength = data.substring(ITEM_LENGTH_INDEX[0],ITEM_LENGTH_INDEX[1]);
        int contentLength = Integer.parseInt(cutContentLength);
        // 数据长度 = 头（2）+ 长度(4)+数据(N)+crc(4)
        if((dataLength-ITEM_HEAD_LENGTH-ITEM_LENGTH_LENGTH-ITEM_CRC_LENGTH)!=contentLength){
            String log = LogEnum.COMMON.format("数据异常","长度校验失败");
            throw new CloseChannelException(log);
        }
        hj212Request.setLength(cutContentLength);
        return contentLength;
    }

    /**
     *
     * @param contentLength 内容长度
     * @param data  原始数据
     * @return 内容
     */
    private String validCRC(int contentLength ,String data,Hj212Request hj212Request){

        String content = StrUtil.sub(data,ITEM_HEAD_LENGTH+ITEM_LENGTH_LENGTH,ITEM_HEAD_LENGTH+ITEM_LENGTH_LENGTH+contentLength);
        //计算校验位
        String calcCRC = CRC16Util.crc(content.getBytes(),content.length());
        String cutCRC = StrUtil.sub(data,ITEM_HEAD_LENGTH+ITEM_LENGTH_LENGTH+contentLength,ITEM_HEAD_LENGTH+ITEM_LENGTH_LENGTH+contentLength+ITEM_CRC_LENGTH);
        if(!cutCRC.equals(calcCRC)){
            String log = LogEnum.COMMON.format("数据异常","校验错误截取为:"+cutCRC+",计算为:"+calcCRC);
            throw new CloseChannelException(log);
        }
        hj212Request.setCrc(cutCRC);
        hj212Request.setContents(content);
        return content;
    }

}
