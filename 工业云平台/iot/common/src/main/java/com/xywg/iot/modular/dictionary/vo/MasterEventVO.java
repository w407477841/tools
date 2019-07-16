package com.xywg.iot.modular.dictionary.vo;

import com.xywg.iot.modular.dictionary.model.MasterEvent;
import com.xywg.iot.modular.dictionary.model.MasterEventParam;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 15:11 2019/1/10
 * Modified By : wangyifei
 */
@Data
public class MasterEventVO extends MasterEvent {

    private List<MasterEventParam> list;

}
