package com.xywg.iot.modular.enterprise.model.vo;

import com.baomidou.mybatisplus.annotations.TableField;
import com.xywg.iot.modular.enterprise.model.AccountInfoCompanyDescription;
import com.xywg.iot.modular.enterprise.model.AccountInfoCompanyProductDescription;
import com.xywg.iot.modular.enterprise.model.AccountInfoCompanyStarProduct;
import com.xywg.iot.modular.enterprise.model.AccountInfoIndividual;
import lombok.Data;

import java.util.List;

/***
 *@author:jixiaojun
 *DATE:2019/1/24
 *TIME:11:12
 */
@Data
public class AuditVo extends AccountInfoIndividual {
    /**
     * 企业全称
     */
    @TableField(exist = false)
    private String enterpriseName;

    /**
     * 企业简称
     */
    @TableField(exist = false)
    private String shortName;

    /**
     * 官网
     */
    @TableField(exist = false)
    private String website;

    /**
     * 统一设备信用代码
     */
    @TableField(exist = false)
    private String creditCode;

    /**
     * 企业营业执照
     */
    @TableField(exist = false)
    private String creditPhoto;

    /**
     * 企业描述
     */
    @TableField(exist = false)
    private String companyDescription;

    /**
     * 年营业额
     */
    @TableField(exist = false)
    private String annualTurnover;

    /**
     * 软件研发团队
     */
    @TableField(exist = false)
    private Integer team;

    /**
     * 以往明星产品
     */
    @TableField(exist = false)
    private String starProduct;

    /**
     * 产品名称
     */
    @TableField(exist = false)
    private String productName;

    /**
     * 联网方式
     */
    @TableField(exist = false)
    private Integer linkType;

    /**
     * 操作系统
     */
    @TableField(exist = false)
    private Integer operateSystem;

    /**
     * 产品描述
     */
    @TableField(exist = false)
    private String productDescription;

    /**
     * 驳回理由
     */
    @TableField(exist = false)
    private String rejectReason;

    /**
     * 个人id
     */
    @TableField(exist = false)
    private Integer individualId;

    /**
     * 企业附件
     */
    @TableField(exist = false)
    private List<AccountInfoCompanyDescription> companyFile;

    /**
     * 明星产品附件
     */
    @TableField(exist = false)
    private List<AccountInfoCompanyStarProduct> starFile;

    /**
     * 产品附件
     */
    @TableField(exist = false)
    private List<AccountInfoCompanyProductDescription> productFile;
}
