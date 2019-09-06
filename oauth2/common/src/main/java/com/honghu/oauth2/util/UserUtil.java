package com.honghu.oauth2.util;

import com.honghu.oauth2.system.entity.User;

public class UserUtil {


    public static ThreadLocal<String> USER_PAGES = new ThreadLocal<>();
    public static ThreadLocal<User> USER_INFO = new ThreadLocal<>();



}
