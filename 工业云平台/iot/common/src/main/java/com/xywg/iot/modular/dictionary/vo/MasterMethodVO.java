package com.xywg.iot.modular.dictionary.vo;

import com.xywg.iot.modular.dictionary.model.MasterMethod;
import com.xywg.iot.modular.dictionary.model.MasterMethodParam;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 17:02 2019/1/10
 * Modified By : wangyifei
 */
@Data
public class MasterMethodVO extends MasterMethod {

    private List<MasterMethodParam> inList;
    private List<MasterMethodParam> outList;

}
