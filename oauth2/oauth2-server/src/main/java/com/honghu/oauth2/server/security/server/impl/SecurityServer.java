package com.honghu.oauth2.server.security.server.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.honghu.oauth2.server.entity.Client;
import com.honghu.oauth2.server.entity.User;
import com.honghu.oauth2.server.security.server.ISecurityServer;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SecurityServer implements ISecurityServer {

    private static final String [] ROLE = {"admin","base"};

    @Override
    public Client loadClient(String clinetId) {
        if("123456".equals(clinetId)){
            Client client =new Client();
            client.setClinetId("123456");
            client.setId(1);
            client.setClientName("测试");
            client.setSecret("1231231");
            client.setExpiration(2*60*60*1000L);
            client.setMainUrl("http://www.baidu.com");
            return client;
        }
        return null;

    }

    @Override
    public User loadUser(String username) {
        User user = new User();
        user.setUsername("admin").setPassword("$2a$04$jc8bgmJy6x.glOFjOSSBkO0kvepS6wlFrwiZG.fQZyaQ.mRi457fe");
        return user;
    }

    @Override
    public List<String> loadRoles(String username) {

        return CollectionUtil.toList(ROLE);
    }
}
