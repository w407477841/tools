package com.honghu.oauth2.server.system.service.impl;

import com.honghu.oauth2.system.entity.Resource;
import com.honghu.oauth2.server.system.mapper.ResourceMapper;
import com.honghu.oauth2.server.system.service.IResourceService;
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
public class ResourceServiceImpl extends ServiceImpl<ResourceMapper, Resource> implements IResourceService {

}
