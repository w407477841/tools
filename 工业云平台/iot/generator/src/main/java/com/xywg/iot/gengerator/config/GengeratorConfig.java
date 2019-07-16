package com.xywg.iot.gengerator.config;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 16:47 2018/12/19
 * Modified By : wangyifei
 */
public class GengeratorConfig {

    private static String projectPath = "E://代码生成";
        /**
         * <p>
         * 读取控制台内容
         * </p>
         */

        public static GlobalConfig gc(){
            GlobalConfig gc = new GlobalConfig();

            gc.setOutputDir(projectPath);
            gc.setAuthor("wyf");
            gc.setOpen(false);
            return gc;
        }
        public static DataSourceConfig dsc(){

            DataSourceConfig dsc = new DataSourceConfig();
           // dsc.setUrl("jdbc:mysql://118.31.69.25:3306/iot_cloud?useUnicode=true&useSSL=false&characterEncoding=utf8");
            dsc.setUrl("jdbc:mysql://118.31.69.25:3306/rabbitmq_admin?useUnicode=true&useSSL=false&characterEncoding=utf8");
            // dsc.setSchemaName("public");
            dsc.setDriverName("com.mysql.jdbc.Driver");
            dsc.setUsername("root");
            dsc.setPassword("Xingyun*_001");
            return dsc;
        }
        public static void main(String[] args) {
            // 代码生成器
            AutoGenerator mpg = new AutoGenerator();

            // 全局配置

            mpg.setGlobalConfig(gc());

            // 数据源配置

            mpg.setDataSource(dsc());

            // 包配置
            PackageConfig pc = new PackageConfig();

            pc.setModuleName("rabbitadmin");
            pc.setParent("com.wyf.rabbitmqtest");
            pc.setEntity("model");
            pc.setMapper("mapper");
            pc.setXml("mapper.xml");
            pc.setService("service");
            pc.setServiceImpl("service.impl");
            mpg.setPackageInfo(pc);

            // 自定义配置
            InjectionConfig cfg = new InjectionConfig() {
                @Override
                public void initMap() {
                    // to do nothing
                }
            };

            // 如果模板引擎是 freemarker
           // String templatePath = "/templates/mapper.xml.ftl";
            // 如果模板引擎是 velocity
             String templatePath = "/templates/mapper.xml.vm";

            // 自定义输出配置
            List<FileOutConfig> focList = new ArrayList<>();
            // 自定义配置会被优先输出
            focList.add(new FileOutConfig(templatePath) {
                @Override
                public String outputFile(TableInfo tableInfo) {
                    // 自定义输出文件名
                    return projectPath +"/"+pc.getParent().replaceAll("\\.","/")+"/"+pc.getXml().replaceAll("\\.","/")+"/" + tableInfo.getEntityName() + "Mapper.xml" ;
                }
            });

            cfg.setFileOutConfigList(focList);
            mpg.setCfg(cfg);

            // 配置模板
            TemplateConfig templateConfig = new TemplateConfig();
            mpg.setTemplate(templateConfig);

            // 策略配置
            StrategyConfig strategy = new StrategyConfig();
            strategy.setNaming(NamingStrategy.underline_to_camel);
            strategy.setTablePrefix("t_");
          //  strategy.setSuperEntityClass("com.xywg.iot.base.BaseModel");
          //  strategy.setSuperEntityColumns("is_del");
            strategy.setRestControllerStyle(true);
            strategy.setEntityLombokModel(true);
            strategy.setInclude(
                    "t_mq_error"
                    );
            strategy.setControllerMappingHyphenStyle(true);
            mpg.setStrategy(strategy);
            mpg.execute();

        }

    }
