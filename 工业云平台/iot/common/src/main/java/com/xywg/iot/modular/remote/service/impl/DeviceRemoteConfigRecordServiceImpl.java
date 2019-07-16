package com.xywg.iot.modular.remote.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xywg.iot.dto.QueryWhereDto;
import com.xywg.iot.dto.ResultPage;
import com.xywg.iot.enums.ResultCode;
import com.xywg.iot.modular.device.model.DeviceInfo;
import com.xywg.iot.modular.device.service.IDeviceInfoService;
import com.xywg.iot.modular.device.vo.DeviceConfigVo;
import com.xywg.iot.modular.device.vo.DeviceInfoVo;
import com.xywg.iot.modular.product.model.ProductInfo;
import com.xywg.iot.modular.product.service.IProductInfoService;
import com.xywg.iot.modular.remote.dao.DeviceRemoteConfigRecordMapper;
import com.xywg.iot.modular.remote.model.DeviceRemoteConfig;
import com.xywg.iot.modular.remote.model.DeviceRemoteConfigRecord;
import com.xywg.iot.modular.remote.model.vo.DeviceDetailInfoVo;
import com.xywg.iot.modular.remote.model.vo.DeviceRemoteConfigRecordVO;
import com.xywg.iot.modular.remote.service.DeviceRemoteConfigRecordService;
import com.xywg.iot.modular.remote.service.DeviceRemoteConfigService;
import com.xywg.iot.modular.upgrade.model.DeviceUpgradePackage;
import com.xywg.iot.util.HttpUtils;
import com.xywg.iot.util.UserUtil;
import com.xywg.iot.vo.ResultVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author hjy
 * @since 2019-01-15
 */
@Service
public class DeviceRemoteConfigRecordServiceImpl extends ServiceImpl<DeviceRemoteConfigRecordMapper, DeviceRemoteConfigRecord> implements DeviceRemoteConfigRecordService {
    @Autowired
    private IDeviceInfoService deviceInfoService;
    @Autowired
    private IProductInfoService productInfoService;
    @Autowired
    private DeviceRemoteConfigService deviceRemoteConfigService;

    @Value("${netty.iotNettyUrl:#{null}}")
    private String iotNettyUrl;
    @Value("${netty.iotNettyWebPort:#{null}}")
    private String iotNettyWebPort;

    /**
     * 获取设备列表
     *
     * @param dto
     * @return
     */
    @Override
    public ResultPage<DeviceInfoVo> selectDevicePageList(QueryWhereDto<DeviceInfo> dto) {
        Page<DeviceUpgradePackage> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        EntityWrapper<DeviceUpgradePackage> ew = new EntityWrapper<>();
        //如果不是管理员 那么数据需要过滤
        if (!UserUtil.ISADMIN.get()) {
            ew.eq("a.create_user", UserUtil.USERID.get());
        }
        if (dto.getProductId() != null) {
            ew.eq("a.product_id", dto.getProductId());
        }
        if (StringUtils.isNotBlank(dto.getKeyWord())) {
            ew.like("a.name", dto.getKeyWord());
        }
        List<DeviceInfoVo> list = baseMapper.selectDevicePageList(page, ew);
        return new ResultPage<>(page.getTotal(), list);
    }

    /**
     * 不带分页的设备列表
     *
     * @param dto
     * @return
     */
    @Override
    public ResultVO selectDeviceList(QueryWhereDto<DeviceInfo> dto) {
        EntityWrapper<DeviceInfo> ew = new EntityWrapper<>();
        //如果不是管理员 那么数据需要过滤
        if (!UserUtil.ISADMIN.get()) {
            ew.eq("create_user", UserUtil.USERID.get());
        }
        if (dto.getProductId() != null) {
            ew.eq("product_id", dto.getProductId());
        }
        if (StringUtils.isNotBlank(dto.getKeyWord())) {
            ew.like("name", dto.getKeyWord());
        }
        List<DeviceInfo> list = deviceInfoService.selectList(ew);
        return new ResultVO(ResultCode.SUCCESS, list);
    }

    @Override
    public ResultVO getDeviceInfoById(QueryWhereDto<DeviceInfo> dto) {
        if (dto.getId() == null) {
            return new ResultVO(ResultCode.SUCCESS, null);
        } else {
            DeviceInfo deviceInfo = deviceInfoService.selectById(dto.getId());
            if (deviceInfo == null) {
                return new ResultVO(ResultCode.SUCCESS, null);
            }
            ProductInfo productInfo = productInfoService.selectById(deviceInfo.getProductId());
            DeviceDetailInfoVo deviceDetailInfoVo = new DeviceDetailInfoVo();
            BeanUtils.copyProperties(deviceInfo, deviceDetailInfoVo);
            deviceDetailInfoVo.setProduct(productInfo);
            return new ResultVO(ResultCode.SUCCESS, deviceDetailInfoVo);
        }
    }

    /**
     * 设备远程配置
     *
     * @param dto
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultVO batchUpdateDeviceConfig(DeviceConfigVo dto) {
        //获取产品信息
        ProductInfo productInfo = productInfoService.selectById(dto.getProductId());

        if ("{}".equals(dto.getJsonString()) || StringUtils.isBlank(dto.getJsonString())) {
            return new ResultVO("无效数据", 400, null);
        }

        DeviceRemoteConfig deviceRemoteConfig = new DeviceRemoteConfig();
        deviceRemoteConfig.setJsonString(dto.getJsonString());
        deviceRemoteConfig.setProductId(dto.getProductId());
        //保存至配置编辑表
        deviceRemoteConfigService.insert(deviceRemoteConfig);

        List<DeviceRemoteConfigRecord> toDbList = new ArrayList<>();

        for (DeviceInfo deviceInfo : dto.getDeviceList()) {
            DeviceRemoteConfigRecord deviceRemoteConfigRecord = new DeviceRemoteConfigRecord();
            deviceRemoteConfigRecord.setProductId(dto.getProductId());
            deviceRemoteConfigRecord.setDeviceId(deviceInfo.getId());
            deviceRemoteConfigRecord.setJsonString(dto.getJsonString());
            deviceRemoteConfigRecord.setConfigId(deviceRemoteConfig.getId());
            //入库时所有的消息都设置为未发送状态0 , 当数据接收平台真正发送数据时,成功发送了的就该为1
            deviceRemoteConfigRecord.setIssuedFlag(0);
            toDbList.add(deviceRemoteConfigRecord);
        }
        //走消息中间件发送到数据消费端
        //rabbitMQService.amqpSendMessage(RabbitConfig.RABBITMQ_TOPIC_DEVICE_REMOTE_CONFIG, JSON.toJSONString(deviceRemoteConfig));
        this.insertBatch(toDbList);
        HttpUtils.sendPost("http://"+iotNettyUrl+":"+iotNettyWebPort+"/internalCommunication/config",JSONUtil.toJsonStr(deviceRemoteConfig));
        return new ResultVO("操作成功", 200, null);
    }

    /**
     * 带分页的单个设备配置履历
     *
     * @param dto
     * @return
     */
    @Override
    public ResultPage<DeviceRemoteConfigRecordVO> getDeviceConfigHistoryPage(QueryWhereDto<DeviceRemoteConfigRecord> dto) {
        Page<DeviceRemoteConfigRecordVO> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        EntityWrapper<DeviceRemoteConfigRecordVO> ew = new EntityWrapper<>();
        ew.eq("a.product_id", dto.getBody().getProductId()).eq("a.device_id", dto.getBody().getDeviceId());
        List<DeviceRemoteConfigRecordVO> list = baseMapper.getDeviceConfigHistoryPage(page, ew);
        return new ResultPage<>(page.getTotal(), list);
    }

    /**
     * 根据配置id查询配置履历
     *
     * @param dto
     * @return
     */
    @Override
    public ResultPage<DeviceRemoteConfigRecordVO> getDeviceRemoteConfigRecordList(QueryWhereDto<DeviceRemoteConfigRecord> dto) {
        Page<DeviceRemoteConfigRecordVO> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        EntityWrapper<DeviceRemoteConfigRecordVO> ew = new EntityWrapper<>();
        ew.eq("a.config_id", dto.getId());
        List<DeviceRemoteConfigRecordVO> list = baseMapper.getDeviceConfigHistoryPage(page, ew);
        return new ResultPage<>(page.getTotal(), list);
    }


    /**
     * 批量恢复远程设置
     *
     * @param dto
     * @return
     */
    @Override
    public ResultVO batchRecoverDeviceConfig(QueryWhereDto dto) {
        //获取受影响的设备
        List<DeviceRemoteConfigRecordVO> list = getDeviceConfigHistory(dto);
        DeviceRemoteConfig deviceRemoteConfig = deviceRemoteConfigService.selectById(dto.getId());

        List<DeviceInfoVo> deviceInfoVoList = new ArrayList<>();
        for (DeviceRemoteConfigRecordVO vo : list) {
            DeviceInfoVo deviceInfoVo = new DeviceInfoVo();
            deviceInfoVo.setDeviceNo(vo.getDeviceNo());
            deviceInfoVo.setProductId(vo.getProductId());
            deviceInfoVo.setId(vo.getDeviceId());
            deviceInfoVoList.add(deviceInfoVo);
        }
        //配置详细
        DeviceConfigVo deviceConfigVo = new DeviceConfigVo();
        deviceConfigVo.setProductId(deviceRemoteConfig.getProductId());
        deviceConfigVo.setJsonString(deviceRemoteConfig.getJsonString());
        deviceConfigVo.setDeviceList(deviceInfoVoList);

        return batchUpdateDeviceConfig(deviceConfigVo);
    }

    /**
     * 不带分页的查询受影响的设备
     *
     * @param dto
     * @return
     */
    @Override
    public List<DeviceRemoteConfigRecordVO> getDeviceConfigHistory(QueryWhereDto dto) {
        EntityWrapper<DeviceRemoteConfigRecordVO> ew = new EntityWrapper<>();
        ew.eq("a.config_id", dto.getId());
        //受影响的设备
        return baseMapper.getDeviceConfigHistory(ew);
    }

}
