package com.xywg.hj212.utils;

import cn.hutool.core.util.StrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import static com.xywg.hj212.common.Const.CONTENT_PREFIXS;
import static com.xywg.hj212.common.Const.CONTENT_PREFIXS_SIZE;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 10:38 2019/7/30
 * Modified By : wangyifei
 */
public class CommonMethod {


    /**
     * 包装内容
     * @param content
     * @return
     */
    public static Map<String,String> packageContent(String content){
        Map<String,String> result = new HashMap<>(32);
        String[] kvs =  content.split(";");
        int index;
        for(String kv : kvs){
            for(int i=0;i<CONTENT_PREFIXS_SIZE;i++){
                index =  kv.indexOf(CONTENT_PREFIXS[i]+"=");
                if(-1!=index){
                    result.put(CONTENT_PREFIXS[i], StrUtil.subAfter(kv,CONTENT_PREFIXS[i]+"=",false));
                }
            }
        }
        return result;
    }


}
