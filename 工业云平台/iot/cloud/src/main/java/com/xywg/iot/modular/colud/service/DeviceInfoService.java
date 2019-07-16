package com.xywg.iot.modular.colud.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.xywg.iot.dto.QueryPageDTO;
import com.xywg.iot.modular.device.model.DeviceInfo;
import com.xywg.iot.modular.device.vo.DeviceInfoVo;
import com.xywg.iot.modular.product.vo.MethodInfoVO;
import com.xywg.iot.modular.product.vo.ParamInfoVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/***
 *@author:jixiaojun
 *DATE:2019/1/4
 *TIME:10:11
 */
public interface DeviceInfoService {
    /**
     * 批量导入
     * @param productId
     * @param file
     * @return
     * @throws Exception
     */
    boolean inputBatch(Integer productId, MultipartFile file) throws Exception;

    /**
     * 编辑
     * @param deviceInfo
     * @return
     * @throws Exception
     */
    boolean updateDevice(DeviceInfo deviceInfo) throws Exception;

    /**
     * 删除
     * @param ids
     * @return
     * @throws Exception
     */
    boolean deleteDevice(List<Integer> ids) throws Exception;

    /**
     * 获取列表
     * @param page
     * @param queryPageDTO
     * @return
     * @throws Exception
     */
    List<DeviceInfoVo> selectDevice(Page<DeviceInfoVo> page, QueryPageDTO queryPageDTO) throws Exception;

    /**
     * 单条查询
     * @param id
     * @return
     * @throws Exception
     */
    DeviceInfo selectDeviceById(Integer id) throws Exception;

    /**
     *  根据设备id 查询产品信息
     * @param id
     * @return
     */
    MethodInfoVO selectMethodInfoByDeviceId(Integer id) throws Exception;

    /**
     *  根据 方法id查询 入参列表
     * @param id
     * @return
     * @throws Exception
     */
    List<ParamInfoVO> selectParamInfoByMethodId(Integer id) throws Exception;

}
