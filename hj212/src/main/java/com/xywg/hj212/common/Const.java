package com.xywg.hj212.common;

import io.netty.channel.Channel;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 15:23 2019/7/2
 * Modified By : wangyifei
 */
public class Const {

        public static final AttributeKey<NettySession> NETTY_SESSION  = AttributeKey.valueOf("hj212.session");

        public static final Map<String,Channel> CHANNEL_MAP = new ConcurrentHashMap<>(2048);

        /**
         *  数据长度位 下标开始-下标结束
         */
        public static final Integer[] ITEM_LENGTH_INDEX = {2,6};

        /**
         * 长度位 长度
         */
        public static final Integer ITEM_LENGTH_LENGTH = 4;
        /**
         * 校验位 长度
         */
        public static final Integer ITEM_CRC_LENGTH = 4;
        /**
         * 消息头
         */
        public static final String HEAD = "##";
        /**
         * 头部 长度
         */
        public static final Integer ITEM_HEAD_LENGTH = HEAD.length();
        /**
         *  内容体 属性头
         */
        public static final String[] CONTENT_PREFIXS = {"QN","ST","CN","PW","MN","Flag","PNUM","PNO","CP"};

        public static final Integer  CONTENT_PREFIXS_SIZE  = CONTENT_PREFIXS.length;

}
