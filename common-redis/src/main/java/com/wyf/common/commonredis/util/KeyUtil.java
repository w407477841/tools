package com.wyf.common.commonredis.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 10:47 2019/4/8
 * Modified By : wangyifei
 */
public class KeyUtil {

    /**
     *  数据库名:表名:主键名:主键值
     * @param db
     * @param tn
     * @param pk
     * @param pkV
     * @return
     */
    public static String getKey( String db,String tn,String pk,String pkV){
        return db+":"+tn+":"+pk+":"+pkV;
    }

}
