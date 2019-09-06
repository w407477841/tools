package com.honghu.oauth2.server.security.server.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.honghu.oauth2.server.security.server.ISecurityServer;
import com.honghu.oauth2.server.system.service.IClientService;
import com.honghu.oauth2.server.system.service.IResourceService;
import com.honghu.oauth2.server.system.service.IRoleService;
import com.honghu.oauth2.server.system.service.IUserService;
import com.honghu.oauth2.system.entity.Client;
import com.honghu.oauth2.system.entity.Resource;
import com.honghu.oauth2.system.entity.Role;
import com.honghu.oauth2.system.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class SecurityServer implements ISecurityServer {

    private static final String [] ROLE = {"admin","base"};

    @Autowired
    private IUserService userService;
    @Autowired
    private IClientService clientService;
    @Autowired
    private IRoleService roleService;
    @Autowired
    private IResourceService resourceService;


    @Override
    public Client loadClient(String clinetId) {

        QueryWrapper<Client> queryWrapper =new QueryWrapper<>();
        queryWrapper.eq("client_code",clinetId);
        Client client = clientService.getOne(queryWrapper);
        return  client;
    }

    @Override
    public List<String> authorizedClient(Long userId) {
        List<String> clientCodes = new ArrayList<>();
        QueryWrapper<Client> queryWrapper =new QueryWrapper<>();
        queryWrapper.inSql("id","select client_id from sys_user_client where user_id = "+userId);
        List<Client> clients = clientService.list(queryWrapper);

        clients.forEach(item->{
            clientCodes.add(item.getClientCode());
        });
        return clientCodes;
    }

    @Override
    public User loadUser(String username,Long clientId) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",username);
        queryWrapper.inSql("id","select user_id from sys_user_client where client_id ="+clientId);
        User user = userService.getOne(queryWrapper);
        return user;
    }

    @Override
    public List<String> loadRoles(Long userid) {
        List<String> results = new ArrayList<>();
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        queryWrapper.inSql("id","select role_id from sys_user_role where user_id = "+userid);

        List<Role> roles =  roleService.list(queryWrapper);

        roles.forEach(role->{
            results.add(role.getRoleCode());

        });

        return results;
    }

    @Override
    public List<Map<String,Object>>  loadPages(Long userId) {
            QueryWrapper<Resource> queryWrapper = new QueryWrapper<>();
            queryWrapper.select("(select client_code from sys_client where sys_client.id =  sys_resource.client_id ) as clientCode",
                    "GROUP_CONCAT(resource_code) as resourceCode");
            queryWrapper.inSql("id","SELECT resource_id FROM sys_role_resource WHERE role_id IN ( SELECT role_id FROM sys_user_role WHERE user_id = "+userId+" )");
        queryWrapper.groupBy("client_id");

       List<Map<String,Object>>  mapList = resourceService.listMaps(queryWrapper);

       return mapList;
    }


}
