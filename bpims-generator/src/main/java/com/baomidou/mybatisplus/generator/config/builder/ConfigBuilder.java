/**
* @author: wangyifei
* Description: 稍作修改
* Date: 17:55 2019/4/22
*/
package com.baomidou.mybatisplus.generator.config.builder;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.PatternPool;
import cn.hutool.core.util.ReUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.TemplateConfig;
import com.baomidou.mybatisplus.generator.config.po.TableField;
import com.baomidou.mybatisplus.generator.config.po.TableFill;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.config.rules.DbType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.config.rules.QuerySQL;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public class ConfigBuilder {
    private final TemplateConfig template;
    private final DataSourceConfig dataSourceConfig;
    private Connection connection;
    private QuerySQL querySQL;
    private String superEntityClass;
    private String superMapperClass;
    private String superServiceClass;
    private String superServiceImplClass;
    private String superControllerClass;
    private String superDtoClass;
    private List<TableInfo> tableInfoList;
    private Map<String, String> packageInfo;
    private Map<String, String> pathInfo;
    private StrategyConfig strategyConfig;
    private GlobalConfig globalConfig;

    public ConfigBuilder(PackageConfig packageConfig, DataSourceConfig dataSourceConfig, StrategyConfig strategyConfig, TemplateConfig template, GlobalConfig globalConfig) {
        if (null == globalConfig) {
            this.globalConfig = new GlobalConfig();
        } else {
            this.globalConfig = globalConfig;
        }

        if (null == template) {
            this.template = new TemplateConfig();
        } else {
            this.template = template;
        }

        if (null == packageConfig) {
            this.handlerPackage(this.template, this.globalConfig.getOutputDir(), new PackageConfig());
        } else {
            this.handlerPackage(this.template, this.globalConfig.getOutputDir(), packageConfig);
        }

        this.dataSourceConfig = dataSourceConfig;
        this.handlerDataSource(dataSourceConfig);
        if (null == strategyConfig) {
            this.strategyConfig = new StrategyConfig();
        } else {
            this.strategyConfig = strategyConfig;
        }

        this.handlerStrategy(this.strategyConfig);
    }

    public Map<String, String> getPackageInfo() {
        return this.packageInfo;
    }

    public Map<String, String> getPathInfo() {
        return this.pathInfo;
    }

    public String getSuperEntityClass() {
        return this.superEntityClass;
    }

    public String getSuperMapperClass() {
        return this.superMapperClass;
    }

    public String getSuperServiceClass() {
        return this.superServiceClass;
    }

    public String getSuperServiceImplClass() {
        return this.superServiceImplClass;
    }

    public String getSuperControllerClass() {
        return this.superControllerClass;
    }

    public String getSuperDtoClass() {
        return this.superDtoClass;
    }

    public List<TableInfo> getTableInfoList() {
        return this.tableInfoList;
    }

    public TemplateConfig getTemplate() {
        return this.template == null ? new TemplateConfig() : this.template;
    }

    private void handlerPackage(TemplateConfig template, String outputDir, PackageConfig config) {
        this.packageInfo = new HashMap();
        this.packageInfo.put("ModuleName", config.getModuleName());
        this.packageInfo.put("Entity", this.joinPackage(config.getParent(), config.getEntity()));
        this.packageInfo.put("Mapper", this.joinPackage(config.getParent(), config.getMapper()));
        this.packageInfo.put("Xml", this.joinPackage(config.getParent(), config.getXml()));
        this.packageInfo.put("Service", this.joinPackage(config.getParent(), config.getService()));
        this.packageInfo.put("ServiceImpl", this.joinPackage(config.getParent(), config.getServiceImpl()));
        this.packageInfo.put("Controller", this.joinPackage(config.getParent(), config.getController()));
        this.packageInfo.put("Dto",this.joinPackage(config.getParent(),config.getDto()));
        this.pathInfo = new HashMap();
        if (StringUtils.isNotEmpty(template.getEntity(this.getGlobalConfig().isKotlin()))) {
            this.pathInfo.put("entity_path", this.joinPath(outputDir, (String)this.packageInfo.get("Entity")));
        }

        if (StringUtils.isNotEmpty(template.getMapper())) {
            this.pathInfo.put("mapper_path", this.joinPath(outputDir, (String)this.packageInfo.get("Mapper")));
        }

        if (StringUtils.isNotEmpty(template.getXml())) {
            this.pathInfo.put("xml_path", this.joinPath(outputDir, (String)this.packageInfo.get("Xml")));
        }

        if (StringUtils.isNotEmpty(template.getService())) {
            this.pathInfo.put("serivce_path", this.joinPath(outputDir, (String)this.packageInfo.get("Service")));
        }

        if (StringUtils.isNotEmpty(template.getServiceImpl())) {
            this.pathInfo.put("serviceimpl_path", this.joinPath(outputDir, (String)this.packageInfo.get("ServiceImpl")));
        }

        if (StringUtils.isNotEmpty(template.getController())) {
            this.pathInfo.put("controller_path", this.joinPath(outputDir, (String)this.packageInfo.get("Controller")));
        }
        if(StringUtils.isNotEmpty(template.getDto())){
            this.pathInfo.put("dto_path", this.joinPath(outputDir, (String)this.packageInfo.get("Dto")));
        }



    }

    private void handlerDataSource(DataSourceConfig config) {
        this.connection = config.getConn();
        this.querySQL = this.getQuerySQL(config.getDbType());
    }

    private void handlerStrategy(StrategyConfig config) {
        this.processTypes(config);
        this.tableInfoList = this.getTablesInfo(config);
    }

    private void processTypes(StrategyConfig config) {
        if (StringUtils.isEmpty(config.getSuperServiceClass())) {
            this.superServiceClass = "com.baomidou.mybatisplus.service.IService";
        } else {
            this.superServiceClass = config.getSuperServiceClass();
        }

        if (StringUtils.isEmpty(config.getSuperServiceImplClass())) {
            this.superServiceImplClass = "com.baomidou.mybatisplus.service.impl.ServiceImpl";
        } else {
            this.superServiceImplClass = config.getSuperServiceImplClass();
        }

        if (StringUtils.isEmpty(config.getSuperMapperClass())) {
            this.superMapperClass = "com.baomidou.mybatisplus.mapper.BaseMapper";
        } else {
            this.superMapperClass = config.getSuperMapperClass();
        }

        this.superEntityClass = config.getSuperEntityClass();
        this.superControllerClass = config.getSuperControllerClass();

    }

    private List<TableInfo> processTable(List<TableInfo> tableList, NamingStrategy strategy, String[] tablePrefix) {
        Iterator i$ = tableList.iterator();

        while(i$.hasNext()) {
            TableInfo tableInfo = (TableInfo)i$.next();
            tableInfo.setEntityName(this.strategyConfig, NamingStrategy.capitalFirst(this.processName(tableInfo.getName(), strategy, tablePrefix)));
            if (StringUtils.isNotEmpty(this.globalConfig.getMapperName())) {
                tableInfo.setMapperName(String.format(this.globalConfig.getMapperName(), tableInfo.getEntityName()));
            } else {
                tableInfo.setMapperName(tableInfo.getEntityName() + "Mapper");
            }

            if (StringUtils.isNotEmpty(this.globalConfig.getXmlName())) {
                tableInfo.setXmlName(String.format(this.globalConfig.getXmlName(), tableInfo.getEntityName()));
            } else {
                tableInfo.setXmlName(tableInfo.getEntityName() + "Mapper");
            }

            if (StringUtils.isNotEmpty(this.globalConfig.getServiceName())) {
                tableInfo.setServiceName(String.format(this.globalConfig.getServiceName(), tableInfo.getEntityName()));
            } else {
                tableInfo.setServiceName("I" + tableInfo.getEntityName() + "Service");
            }

            if (StringUtils.isNotEmpty(this.globalConfig.getServiceImplName())) {
                tableInfo.setServiceImplName(String.format(this.globalConfig.getServiceImplName(), tableInfo.getEntityName()));
            } else {
                tableInfo.setServiceImplName(tableInfo.getEntityName() + "ServiceImpl");
            }

            if (StringUtils.isNotEmpty(this.globalConfig.getControllerName())) {
                tableInfo.setControllerName(String.format(this.globalConfig.getControllerName(), tableInfo.getEntityName()));
            } else {
                tableInfo.setControllerName(tableInfo.getEntityName() + "Controller");
            }
        }

        return tableList;
    }

    private List<TableInfo> getTablesInfo(StrategyConfig config) {
        boolean isInclude = null != config.getInclude() && config.getInclude().length > 0;
        boolean isExclude = null != config.getExclude() && config.getExclude().length > 0;
        if (isInclude && isExclude) {
            throw new RuntimeException("<strategy> 标签中 <include> 与 <exclude> 只能配置一项！");
        } else {
            List<TableInfo> tableList = new ArrayList();
            List<TableInfo> includeTableList = new ArrayList();
            List<TableInfo> excludeTableList = new ArrayList();
            Set<String> notExistTables = new HashSet();
            NamingStrategy strategy = config.getNaming();
            PreparedStatement preparedStatement = null;

            try {
                String tableCommentsSql = this.querySQL.getTableCommentsSql();
                if (QuerySQL.POSTGRE_SQL == this.querySQL) {
                    tableCommentsSql = String.format(tableCommentsSql, this.dataSourceConfig.getSchemaname());
                }

                preparedStatement = this.connection.prepareStatement(tableCommentsSql);
                System.out.println(tableCommentsSql);
                ResultSet results = preparedStatement.executeQuery();

                while(true) {
                    while(results.next()) {
                        String tableName = results.getString(this.querySQL.getTableName());
                        if (StringUtils.isNotEmpty(tableName)) {
                            String tableComment = results.getString(this.querySQL.getTableComment());
                            TableInfo tableInfo = new TableInfo();
                            tableInfo.setName(tableName);
                            tableInfo.setComment(tableComment);
                            String[] arr$;
                            int len$;
                            int i$;
                            String excludeTab;
                            if (isInclude) {
                                arr$ = config.getInclude();
                                len$ = arr$.length;

                                for(i$ = 0; i$ < len$; ++i$) {
                                    excludeTab = arr$[i$];
                                    if (excludeTab.equalsIgnoreCase(tableName)) {
                                        includeTableList.add(tableInfo);
                                    } else {
                                        notExistTables.add(excludeTab);
                                    }
                                }
                            } else if (isExclude) {
                                arr$ = config.getExclude();
                                len$ = arr$.length;

                                for(i$ = 0; i$ < len$; ++i$) {
                                    excludeTab = arr$[i$];
                                    if (excludeTab.equalsIgnoreCase(tableName)) {
                                        excludeTableList.add(tableInfo);
                                    } else {
                                        notExistTables.add(excludeTab);
                                    }
                                }
                            }

                            tableList.add(this.convertTableFields(tableInfo, strategy));
                        } else {
                            System.err.println("当前数据库为空！！！");
                        }
                    }

                    Iterator i$ = tableList.iterator();

                    while(i$.hasNext()) {
                        TableInfo tabInfo = (TableInfo)i$.next();
                        notExistTables.remove(tabInfo.getName());
                    }

                    if (notExistTables.size() > 0) {
                        System.err.println("表 " + notExistTables + " 在数据库中不存在！！！");
                    }

                    if (isExclude) {
                        tableList.removeAll(excludeTableList);
                        includeTableList = tableList;
                    }

                    if (!isInclude && !isExclude) {
                        includeTableList = tableList;
                    }
                    break;
                }
            } catch (SQLException var27) {
                var27.printStackTrace();
            } finally {
                try {
                    if (preparedStatement != null) {
                        preparedStatement.close();
                    }

                    if (this.connection != null) {
                        this.connection.close();
                    }
                } catch (SQLException var26) {
                    var26.printStackTrace();
                }

            }

            return this.processTable(includeTableList, strategy, config.getTablePrefix());
        }
    }

    private boolean isKeyIdentity(ResultSet results) throws SQLException {
        if (QuerySQL.MYSQL == this.querySQL) {
            String extra = results.getString("Extra");
            if ("auto_increment".equals(extra)) {
                return true;
            }
        } else if (QuerySQL.SQL_SERVER == this.querySQL) {
            int isIdentity = results.getInt("isIdentity");
            return 1 == isIdentity;
        }

        return false;
    }

    private TableInfo convertTableFields(TableInfo tableInfo, NamingStrategy strategy) {
        boolean haveId = false;
        List<TableField> fieldList = new ArrayList();
        ArrayList commonFieldList = new ArrayList();

        try {
            String tableFieldsSql = this.querySQL.getTableFieldsSql();
            if (QuerySQL.POSTGRE_SQL == this.querySQL) {
                tableFieldsSql = String.format(tableFieldsSql, this.dataSourceConfig.getSchemaname(), tableInfo.getName());
            } else {
                tableFieldsSql = String.format(tableFieldsSql, tableInfo.getName());
            }
            System.out.println(tableFieldsSql);
            PreparedStatement preparedStatement = this.connection.prepareStatement(tableFieldsSql);
            ResultSet results = preparedStatement.executeQuery();

            label64:
            while(true) {
                while(true) {
                    if (!results.next()) {
                        break label64;
                    }

                    TableField field = new TableField();
                    String key = results.getString(this.querySQL.getFieldKey());
                    boolean isId = StringUtils.isNotEmpty(key) && key.toUpperCase().equals("PRI");
                    if (isId && !haveId) {
                        field.setKeyFlag(true);
                        if (this.isKeyIdentity(results)) {
                            field.setKeyIdentityFlag(true);
                        }

                        haveId = true;
                    } else {
                        field.setKeyFlag(false);
                    }

                    field.setName(results.getString(this.querySQL.getFieldName()));
                    field.setType(results.getString(this.querySQL.getFieldType()));
                    field.setPropertyName(this.strategyConfig, this.processName(field.getName(), strategy));
                    field.setColumnType(this.dataSourceConfig.getTypeConvert().processTypeConvert(field.getType()));
                    field.setComment(results.getString(this.querySQL.getFieldComment()));
                    if(field.getColumnType().getType().equals(DbColumnType.STRING.getType())){
                        // 设置长度
                        field.setLength(Convert.toInt( ReUtil.get( PatternPool.NUMBERS,field.getType(),0)) );
                    }
                    System.out.println(JSONUtil.toJsonStr(field));
                    if (this.strategyConfig.includeSuperEntityColumns(field.getName())) {
                        commonFieldList.add(field);
                    } else {
                        List<TableFill> tableFillList = this.getStrategyConfig().getTableFillList();
                        if (null != tableFillList) {
                            Iterator i$ = tableFillList.iterator();

                            while(i$.hasNext()) {
                                TableFill tableFill = (TableFill)i$.next();
                                if (tableFill.getFieldName().equals(field.getName())) {
                                    field.setFill(tableFill.getFieldFill().name());
                                    break;
                                }
                            }
                        }

                        fieldList.add(field);
                    }
                }
            }
        } catch (SQLException var15) {
            System.err.println("SQL Exception：" + var15.getMessage());
        }

        tableInfo.setFields(fieldList);
        tableInfo.setCommonFields(commonFieldList);
        return tableInfo;
    }

    private String joinPath(String parentDir, String packageName) {
        if (StringUtils.isEmpty(parentDir)) {
            parentDir = System.getProperty("java.io.tmpdir");
        }

        if (!StringUtils.endsWith(parentDir, File.separator)) {
            parentDir = parentDir + File.separator;
        }

        packageName = packageName.replaceAll("\\.", "\\" + File.separator);
        return parentDir + packageName;
    }

    private String joinPackage(String parent, String subPackage) {
        return StringUtils.isEmpty(parent) ? subPackage : parent + "." + subPackage;
    }

    private String processName(String name, NamingStrategy strategy) {
        return this.processName(name, strategy, (String[])null);
    }

    private String processName(String name, NamingStrategy strategy, String[] tablePrefix) {
        boolean removePrefix = false;
        if (tablePrefix != null && tablePrefix.length >= 1) {
            removePrefix = true;
        }

        String propertyName;
        if (removePrefix) {
            if (strategy == NamingStrategy.underline_to_camel) {
                propertyName = NamingStrategy.removePrefixAndCamel(name, tablePrefix);
            } else {
                propertyName = NamingStrategy.removePrefix(name, tablePrefix);
            }
        } else if (strategy == NamingStrategy.underline_to_camel) {
            propertyName = NamingStrategy.underlineToCamel(name);
        } else {
            propertyName = name;
        }

        return propertyName;
    }

    private QuerySQL getQuerySQL(DbType dbType) {
        QuerySQL[] arr$ = QuerySQL.values();
        int len$ = arr$.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            QuerySQL qs = arr$[i$];
            if (qs.getDbType().equals(dbType.getValue())) {
                return qs;
            }
        }

        return QuerySQL.MYSQL;
    }

    public StrategyConfig getStrategyConfig() {
        return this.strategyConfig;
    }

    public ConfigBuilder setStrategyConfig(StrategyConfig strategyConfig) {
        this.strategyConfig = strategyConfig;
        return this;
    }

    public GlobalConfig getGlobalConfig() {
        return this.globalConfig;
    }

    public ConfigBuilder setGlobalConfig(GlobalConfig globalConfig) {
        this.globalConfig = globalConfig;
        return this;
    }
}
