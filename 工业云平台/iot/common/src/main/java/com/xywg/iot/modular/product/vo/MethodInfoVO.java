package com.xywg.iot.modular.product.vo;

import com.xywg.iot.modular.product.model.ProductMethod;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 13:15 2019/3/16
 * Modified By : wangyifei
 */
@Data
public class MethodInfoVO {

        private String pk;
        private String name;
        private String sn;
        private List<ProductMethod> productMethods;

}
