package com.xywg.iot.dto;

import com.xywg.iot.base.BaseEntity;
import com.xywg.iot.modular.enterprise.model.vo.FilePropertyVo;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author hjy
 * @date 2019/1/9
 */
@Data
public class EnterpriseCertificationDTO extends BaseEntity<EnterpriseCertificationDTO> {
    /**
     * 企业id
     */
    private Integer enterpriseId;

    /**
     * 企业名称
     */
    private String enterpriseName;
    /**
     * 企业简介
     */
    private String shortName;

    /**
     * 企业官网
     */
    private String website;

    /**
     * 省市区
     */
    private Integer province;
    private Integer city;
    private Integer district;
    private String provinceName;
    private String cityName;
    private String districtName;
    /**
     * 街道
     */
    private String street;
    /**
     * 社会信用代码
     */
    private String creditCode;
    /**
     * 营业执照
     */
    private String creditPhoto;

    /**
     * 企业描述
     */
    private String companyDescription;

    /**
     * 企业描述附件
     */
    private List<FilePropertyVo> companyDescriptionFile;
//-------------------------------------------------------------------------------
    /**
     * 年营业额
     */
    private String annualTurnover;

    /**
     * 研发团队
     */
    private Integer team;
    /**
     * 以往明星产品
     */
    private String starProduct;
    /**
     * 以往明星产品文件地址
     */
    private List<FilePropertyVo> starProductFile;
//---------------------------------------------------------------------------------------

    /**
     * 产品名称
     */
    private String productName;

    /**
     * 产品类型
     */
    private Integer productType;

    /**
     * 联网方式
     */
    private Integer linkType;

    /**
     * 操作系统
     */
    private Integer operateSystem;
    /**
     * 产品描述
     */
    private String productDescription;
    /**
     * 产品描述文件
     */
    private List<FilePropertyVo> productDescriptionFile;
//----------------------------------------------------------------------------------------------
    /**
     * 申请人Id
     */
    private Integer individualId;
    /**
     * 姓名
     */
    private String individualName;
    /**
     * 职务
     */
    private String staff;
    /**
     * 电话
     */
    private String tel;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 来源
     */
    private Integer source;

    /**
     * 身份证号码
     */
    private String identityNo;
    /**
     * 身份证图片地址  正面地址在前,反面地址在后,以逗号分隔
     */
    private String identityPhoto;
    @Override
    protected Serializable pkVal() {
        return null;
    }
}
