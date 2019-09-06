package com.honghu.oauth2.server.security.server;

import com.honghu.oauth2.system.entity.Client;
import com.honghu.oauth2.system.entity.User;

import java.util.List;
import java.util.Map;

public interface ISecurityServer {
    /**
     *  查询客户端信息
     * @param clinetId
     * @return
     */
     Client loadClient(String clinetId);

    /**
     *
     * @param userId 登录的用户
     * @return 用户所有授权的客户端
     */
     List<String> authorizedClient(Long userId);

    /**
     *  查询用户
     * @param username
     * @return
     */
     User loadUser(String username,Long clientId);
     /**
      *  根据用户名查询 角色
      * */
     List<String> loadRoles(Long userid);

    /**
     *  加载  用户的页面
     * @param userId
     * @return 不同客户端下的页面
     */
    List<Map<String,Object>>  loadPages(Long userId);


}
