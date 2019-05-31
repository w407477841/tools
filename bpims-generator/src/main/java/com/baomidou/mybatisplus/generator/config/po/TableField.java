//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.baomidou.mybatisplus.generator.config.po;

import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.toolkit.StringUtils;

public class TableField {
    private boolean convert;
    private boolean keyFlag;
    private boolean keyIdentityFlag;
    private String name;
    private Integer length;
    private String type;
    private String propertyName;
    private DbColumnType columnType;
    private String comment;
    private String fill;

    public TableField() {
    }

    public boolean isConvert() {
        return this.convert;
    }

    protected void setConvert(StrategyConfig strategyConfig) {
        if (strategyConfig.isCapitalModeNaming(this.name)) {
            this.convert = false;
        } else if (StrategyConfig.DB_COLUMN_UNDERLINE) {
            if (StringUtils.containsUpperCase(this.name)) {
                this.convert = true;
            }
        } else if (!this.name.equals(this.propertyName)) {
            this.convert = true;
        }

    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public void setConvert(boolean convert) {
        this.convert = convert;
    }

    public boolean isKeyFlag() {
        return this.keyFlag;
    }

    public void setKeyFlag(boolean keyFlag) {
        this.keyFlag = keyFlag;
    }

    public boolean isKeyIdentityFlag() {
        return this.keyIdentityFlag;
    }

    public void setKeyIdentityFlag(boolean keyIdentityFlag) {
        this.keyIdentityFlag = keyIdentityFlag;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPropertyName() {
        return this.propertyName;
    }

    public void setPropertyName(StrategyConfig strategyConfig, String propertyName) {
        this.propertyName = propertyName;
        this.setConvert(strategyConfig);
    }

    public DbColumnType getColumnType() {
        return this.columnType;
    }

    public void setColumnType(DbColumnType columnType) {
        this.columnType = columnType;
    }

    public String getPropertyType() {
        return null != this.columnType ? this.columnType.getType() : null;
    }

    public String getComment() {
        return this.comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCapitalName() {
        if (this.propertyName.length() <= 1) {
            return this.propertyName.toUpperCase();
        } else {
            String setGetName = this.propertyName;
            if (DbColumnType.BASE_BOOLEAN.getType().equalsIgnoreCase(this.columnType.getType())) {
                setGetName = StringUtils.removeIsPrefixIfBoolean(setGetName, Boolean.class);
            }

            String firstChar = setGetName.substring(0, 1);
            return Character.isLowerCase(firstChar.toCharArray()[0]) && Character.isUpperCase(setGetName.substring(1, 2).toCharArray()[0]) ? firstChar.toLowerCase() + setGetName.substring(1) : firstChar.toUpperCase() + setGetName.substring(1);
        }
    }

    public String getFill() {
        return this.fill;
    }

    public void setFill(String fill) {
        this.fill = fill;
    }
}
