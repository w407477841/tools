package com.xywg.hj212.utils;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 10:15 2019/4/11
 * Modified By : wangyifei
 */
@Slf4j
public class CRC16Util {


    public static String crc(byte[] datas,int length){
        // 检验寄存器
        int crcReg   = 0xFFFF;
        int check;
        for(int i=0;i<length;i++) {
            crcReg =( (crcReg >> 8) ^ datas[i]);
            for (int j = 0; j < 8; j++) {
                check = crcReg & 0x0001;
                crcReg =  crcReg >> 1;
                if(check == 0x0001){
                    crcReg  =  crcReg^ 0xA001;
                }
            }
        }
        return StrUtil.fillBefore(Integer.toHexString(crcReg),'0',4).toUpperCase();
    }




public static void main(String args[]) throws UnsupportedEncodingException {
String data = "QN=20160801085857223;ST=32;CN=1062;PW=100000;MN=010000A8900016F000169DC0;Flag=5;CP=&&RtdInterval=30&&";
byte [] datas = data.getBytes();
System.out.println(crc(datas,datas.length));




}

}
