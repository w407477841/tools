//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.baomidou.mybatisplus.generator.config;

public class TemplateConfig {
    private String entity = "/templates/entity.java.vm";
    private String service = "/templates/service.java.vm";
    private String serviceImpl = "/templates/serviceImpl.java.vm";
    private String mapper = "/templates/mapper.java.vm";
    private String xml = "/templates/mapper.xml.vm";
    private String controller = "/templates/controller.java.vm";
    private String dto = "/templates/dto.java.vm";

    public TemplateConfig() {
    }

    public String getEntity(boolean kotlin) {
        return kotlin ? "/templates/entity.kt.vm" : this.entity;
    }

    public TemplateConfig setEntity(String entity) {
        this.entity = entity;
        return this;
    }

    public String getService() {
        return this.service;
    }

    public TemplateConfig setService(String service) {
        this.service = service;
        return this;
    }

    public String getServiceImpl() {
        return this.serviceImpl;
    }

    public TemplateConfig setServiceImpl(String serviceImpl) {
        this.serviceImpl = serviceImpl;
        return this;
    }

    public String getMapper() {
        return this.mapper;
    }

    public TemplateConfig setMapper(String mapper) {
        this.mapper = mapper;
        return this;
    }

    public String getXml() {
        return this.xml;
    }

    public TemplateConfig setXml(String xml) {
        this.xml = xml;
        return this;
    }

    public String getController() {
        return this.controller;
    }

    public TemplateConfig setController(String controller) {
        this.controller = controller;
        return this;
    }

    public String getDto() {
        return dto;
    }

    public TemplateConfig setDto(String dto){
        this.dto = dto;
        return this;
    }



}
