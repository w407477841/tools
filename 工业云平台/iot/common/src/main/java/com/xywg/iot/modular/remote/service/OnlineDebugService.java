package com.xywg.iot.modular.remote.service;

import com.xywg.iot.modular.remote.model.OnlineDebug;
import com.xywg.iot.vo.ResultVO;

/**
 * @author hjy
 * @date 2019/1/21
 * 在线调试
 */
public interface OnlineDebugService {

    /**
     * 发送指令
     * @param onlineDebug
     */
    ResultVO sendOrder(OnlineDebug onlineDebug);


}
