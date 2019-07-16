package com.xywg.iot.modular.enterprise.model.vo;

import com.xywg.iot.modular.enterprise.model.AccountInfoCompany;
import lombok.Data;

/**
 * <p>
 * 公司基本信息
 * </p>
 *
 * @author hjy
 * @since 2019-01-08
 */
@Data
public class AccountInfoCompanyVo extends AccountInfoCompany {

		private String operateSystemName;

		private String linkTypeName;

		private String productTypeName;


}
