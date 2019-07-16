package com.xywg.iot.modular.dictionary.dto;

import com.xywg.iot.modular.dictionary.model.MasterEventParam;
import com.xywg.iot.modular.dictionary.model.MasterMethod;
import com.xywg.iot.modular.dictionary.model.MasterMethodParam;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 9:26 2019/1/10
 * Modified By : wangyifei
 */
@Data
public class MasterMethodDTO extends MasterMethod {

    private List<MasterMethodParam> inList;
    private List<MasterMethodParam> outList;

}
