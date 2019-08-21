package com.honghu.oauth2.server.security.server;

import com.honghu.oauth2.server.entity.Client;
import com.honghu.oauth2.server.entity.User;

import java.util.List;

public interface ISecurityServer {
    /**
     *  查询客户端信息
     * @param clinetId
     * @return
     */
     Client loadClient(String clinetId);

    /**
     *  查询用户
     * @param username
     * @return
     */
     User loadUser(String username);
     /**
      *  根据用户名查询 角色
      * */
     List<String> loadRoles(String username);


}
