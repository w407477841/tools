package com.xywg.hj212.coder;

import cn.hutool.core.util.StrUtil;
import com.xywg.hj212.pojo.Hj212Request;
import com.xywg.hj212.utils.CRC16Util;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import lombok.extern.slf4j.Slf4j;
import static com.xywg.hj212.common.Const.*;
import java.util.List;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 13:52 2019/4/9
 * Modified By : wangyifei
 */
@Slf4j

public class Encoder extends MessageToMessageEncoder<Hj212Request> {


    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Hj212Request request, List<Object> list) throws Exception {

                   String encodeStr = encode0(request);
                   list.add(encodeStr);

    }

    /**
     * 将业务对象转成string
     * @param request
     * @return
     */
    private String encode0( Hj212Request request){
        StringBuilder sb = new StringBuilder();
        String contents= request.getContents();
        int length;
        if(StrUtil.isBlank(contents)){
            length  = 0;
        }else{
            length  =contents.length();
        }
        String lengthStr = StrUtil.fillBefore(""+(length+10),'0',4);
        String calcCrc = CRC16Util.crc(StrUtil.utf8Bytes(contents),length);
        sb.append(HEAD).append(lengthStr).append(contents).append(calcCrc).append("\r\n");

        return sb.toString();

    }

}
