package com.honghu.oauth2.server.system.service.impl;

import com.honghu.oauth2.system.entity.Client;
import com.honghu.oauth2.server.system.mapper.ClientMapper;
import com.honghu.oauth2.server.system.service.IClientService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wyf
 * @since 2019-08-28
 */
@Service
public class ClientServiceImpl extends ServiceImpl<ClientMapper, Client> implements IClientService {

}
