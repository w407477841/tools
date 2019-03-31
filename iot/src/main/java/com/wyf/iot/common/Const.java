package com.wyf.iot.common;

import io.netty.buffer.ByteBuf;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 10:32 2019/3/25
 * Modified By : wangyifei
 */
public interface Const {

    /**
     *  byteBuf 转 字符窜
     * @param in
     * @return
     */
     static String bytebuf2ascii(ByteBuf in){
        int len = in.readableBytes();
        //  可读开始位置
        byte[] msgData = new byte[len];
        in.readBytes(msgData);
        String data = new String(msgData);
        return data;
    }

    /**
     * byte转 16进制 字符窜
     * @param in
     * @return
     */
    static String bytebuf2hex(ByteBuf in){
        int len = in.readableBytes();
        //  可读开始位置
        byte[] msgData = new byte[len];
        in.readBytes(msgData);
        String data = toHexString(msgData);
        return data;
    }

    /**
     * byte数组转16进制字符串
     * @param bytes
     * @return
     * @description
     */
     static String toHexString(byte[] bytes) {
        final String hex = "0123456789abcdef";
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            // 取出这个字节的高4位，然后与0x0f与运算，得到一个0-15之间的数据，通过HEX.charAt(0-15)即为16进制数
            sb.append(hex.charAt((b >> 4) & 0x0f));
            // 取出这个字节的低位，与0x0f与运算，得到一个0-15之间的数据，通过HEX.charAt(0-15)即为16进制数
            sb.append(hex.charAt(b & 0x0f));
        }
        return sb.toString();
    }

}
