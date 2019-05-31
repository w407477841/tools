/**
* @author: wangyifei
* Description: 稍作修改
* Date: 17:48 2019/4/22
*/
package com.baomidou.mybatisplus.generator.config;

import com.baomidou.mybatisplus.toolkit.StringUtils;

public class PackageConfig {
    private String parent = "com.baomidou";
    private String moduleName = null;
    private String entity = "entity";
    private String service = "service";
    private String serviceImpl = "service.impl";
    private String mapper = "mapper";
    private String xml = "mapper.xml";
    private String controller = "web";
    private String dto = "dto";

    public PackageConfig() {
    }

    public String getParent() {
        return StringUtils.isNotEmpty(this.moduleName) ? this.parent + "." + this.moduleName : this.parent;
    }

    public PackageConfig setParent(String parent) {
        this.parent = parent;
        return this;
    }

    public String getModuleName() {
        return this.moduleName;
    }

    public PackageConfig setModuleName(String moduleName) {
        this.moduleName = moduleName;
        return this;
    }

    public String getEntity() {
        return this.entity;
    }

    public PackageConfig setEntity(String entity) {
        this.entity = entity;
        return this;
    }

    public String getService() {
        return this.service;
    }

    public PackageConfig setService(String service) {
        this.service = service;
        return this;
    }

    public String getServiceImpl() {
        return this.serviceImpl;
    }

    public PackageConfig setServiceImpl(String serviceImpl) {
        this.serviceImpl = serviceImpl;
        return this;
    }

    public String getMapper() {
        return this.mapper;
    }

    public PackageConfig setMapper(String mapper) {
        this.mapper = mapper;
        return this;
    }

    public String getXml() {
        return this.xml;
    }



    public PackageConfig setXml(String xml) {
        this.xml = xml;
        return this;
    }

    public String getController() {
        return StringUtils.isEmpty(this.controller) ? "web" : this.controller;
    }

    public PackageConfig setController(String controller) {
        this.controller = controller;
        return this;
    }

    public String getDto() {
        return dto;
    }

    public void setDto(String dto) {
        this.dto = dto;
    }

}
