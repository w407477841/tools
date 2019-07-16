package com.xywg.iot.modular.enterprise.model.vo;

import lombok.Data;

import java.util.List;

/**
 * <p>
 * 公司基本信息
 * </p>
 *
 * @author hjy
 * @since 2019-01-08
 */
@Data
public class AccountInfoReturnVo {

    private AccountInfoCompanyVo company;
    private AccountInfoIndividualVo individual;
    private List<FilePropertyVo> companyDescriptionFile;
    private List<FilePropertyVo> starProductFile;
    private List<FilePropertyVo> productDescriptionFile;


}
