package com.honghu.oauth2;

import cn.hutool.core.collection.CollectionUtil;

import java.util.List;

public class Const {

    /**
     * 权限参数头
     */
    public static final String AUTHORITIES = "auths";
    /**
     * 过期时间
     */
    public static final String EXP = "exp";
    /**
     * 持有者
     */
    public static final String SUB = "sub";
    /**
     * 客户端 已授权 客户端
     */
    public static final String CLIENTS = "clients";

    public static final String ADMIN_FLAG = "admin";

    public static final String TRUE_FLAG = "1";

    public static final String FLASE_FLAG = "0";

    public static final String ADMIN_ROLE= "oauth2_admin_123123";
    public static final List<String> ADMIN_ROLES = CollectionUtil.toList(ADMIN_ROLE);


    public interface  GroupInsert{}
    public interface  GroupUpdate{}
    public interface  GroupDelete{}
    public interface  GroupPage{}
}
