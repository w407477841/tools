package com.xywg.iot.netty.handler;


import cn.hutool.json.JSONUtil;
import com.xywg.iot.common.utils.DataUtil;
import com.xywg.iot.common.utils.FtpUtils;
import com.xywg.iot.dto.QueryWhereDto;
import com.xywg.iot.modular.device.dto.DeviceConfigDelDTO;
import com.xywg.iot.modular.device.dto.DeviceConfigFileRecordDto;
import com.xywg.iot.modular.device.model.DeviceConfigFile;
import com.xywg.iot.modular.device.model.DeviceConfigFileRecord;
import com.xywg.iot.modular.device.service.DeviceConfigFileRecordService;
import com.xywg.iot.modular.device.service.IDeviceConfigFileService;
import com.xywg.iot.modular.device.vo.DeviceInfoVo;
import com.xywg.iot.modular.netty.dto.CallMethodDTO;
import com.xywg.iot.modular.netty.dto.NettyCtrlDTO;
import com.xywg.iot.modular.upgrade.model.DeviceUpgradePackage;
import com.xywg.iot.modular.upgrade.model.DeviceUpgradeRecord;
import com.xywg.iot.modular.upgrade.model.DeviceUpgradeRecordData;
import com.xywg.iot.modular.upgrade.service.IDeviceUpgradeRecordService;
import com.xywg.iot.util.RedisUtil;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.*;

import static com.xywg.iot.common.global.GlobalStaticConstant.REDIS_SEND_CONFIG_HEAD;
import static com.xywg.iot.common.global.GlobalStaticConstant.REDIS_UPGRADE_HEAD;
import static com.xywg.iot.netty.handler.CommonStaticMethod.stringToHexString;


/**
 * @author hjy
 * @date 2018/10/8
 * 设备主动下发操作
 */
@RestController
@RequestMapping("/internalCommunication")
public class ActiveIssuedController {
    @Value("${xywg.upgrade-file-base-path}")
    private String upgradeFileBasePath;
    @Value("${xywg.redisHead}")
    private String redisHead;

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private CommonMethod commonMethod;
    @Autowired
    private BusinessHandle deviceServer;
    @Autowired
    private IDeviceUpgradeRecordService deviceUpgradeRecordService;
    @Autowired
    private DeviceConfigFileRecordService configFileRecordService;
    @Autowired
    private IDeviceConfigFileService configFileService;

    private Logger logger = LoggerFactory.getLogger(ActiveIssuedController.class);

    /**
     * 发送升级指令 功能码0006
     */
    @PostMapping("/upgrade")
    public void upgradePackage(@RequestBody DeviceUpgradePackage deviceUpgradePackage) {
        if (deviceUpgradePackage == null || deviceUpgradePackage.getId() == null) {
            return;
        }
        DeviceUpgradeRecord deviceUpgradeRecord = new DeviceUpgradeRecord();
        deviceUpgradeRecord.setPackageId(deviceUpgradePackage.getId());
        QueryWhereDto<DeviceUpgradeRecord> dto = new QueryWhereDto<>();
        dto.setBody(deviceUpgradeRecord);
        //根据升级包id 获取设备升级履历信息
        List<DeviceUpgradeRecordData> recordDataList = deviceUpgradeRecordService.getDeviceUpgradeRecordList(dto);
        try {
            FtpUtils ftpUtils = new FtpUtils();
            String path = recordDataList.get(0).getPath();
            String ftpPath = path.substring(0, path.lastIndexOf("/"));
            String fileName = path.substring(path.lastIndexOf("/") + 1);
            boolean b = ftpUtils.downloadFile(ftpPath, fileName, upgradeFileBasePath + ftpPath);
            if (!b) {
                logger.info("<<从ftp下载文件失败,path:" + path + ">>");
            }
            long fileLength = new File(upgradeFileBasePath + path).length();
            //文件长度转16进制
            String fileLength16 = Long.toHexString(fileLength).length() % 2 == 0 ?
                    Long.toHexString(fileLength) : ("0" + Long.toHexString(fileLength));

            for (DeviceUpgradeRecordData recordData : recordDataList) {
                String sn = recordData.getDeviceNo();
                //序列号转16进制字节码
                String snCode = stringToHexString(sn);
                Channel channel = NettyChannelManage.getChannel(sn);
                if (channel != null) {
                    LinkedHashMap<String, String> linkedMap = new LinkedHashMap<>();
                    linkedMap.put("02", fileLength16);
                    commonMethod.resMessageJoint(channel, snCode, linkedMap, "0006", "01", "00");
                    redisUtil.set(redisHead + ":" + REDIS_UPGRADE_HEAD + sn, path);
                    redisUtil.set(redisHead + ":packageRecord:" + REDIS_UPGRADE_HEAD + sn, recordData.getId() + "");
            }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设备重启指令下发  功能码0009
     *
     * @return
     */
    @PostMapping("/rebootDevice")
    public String rebootDevice(@RequestBody DeviceUpgradePackage deviceUpgradePackage) {
        try {
            List<DeviceInfoVo> deviceNoList = deviceUpgradePackage.getDeviceList();
            StringBuilder res = new StringBuilder();
            for (DeviceInfoVo deviceNo : deviceNoList) {
                Channel channel = NettyChannelManage.getChannel(deviceNo.getDeviceNo());
                if (channel != null) {
                    //序列号转16进制字节码
                    String snCode = stringToHexString(deviceNo.getDeviceNo()).replaceAll(" ", "");
                    System.out.println("发送重启");
                    commonMethod.resMessageJoint(channel, snCode, null, "0009", "01", "00");
                    res.append(deviceNo).append(",");
                }
            }
            return res.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }


    /**
     * 配置参数下发  功能码000B
     *
     * @return
     */
    @PostMapping("/sendConfig")
    public String deployDevice(@RequestBody DeviceConfigFileRecordDto configFileRecordDto) {
        if (configFileRecordDto.getDeviceList() == null || configFileRecordDto.getDeviceList().size() == 0) {
            return "ok";
        }
        try {
            //FTP上文件路径
            String path = configFileRecordDto.getPath();

            FtpUtils ftpUtils = new FtpUtils();
            String ftpPath = path.substring(0, path.lastIndexOf("/"));
            String fileName = path.substring(path.lastIndexOf("/") + 1);
            boolean b = ftpUtils.downloadFile(ftpPath, fileName, upgradeFileBasePath + ftpPath);
            if (!b) {
                logger.info("<<从ftp下载文件失败,path:" + path + ">>");
                return "error";
            }
            long fileLength = new File(upgradeFileBasePath + path).length();
            //文件长度转16进制
            String fileLength16 = Long.toHexString(fileLength).length() % 2 == 0 ?
                    Long.toHexString(fileLength) : ("0" + Long.toHexString(fileLength));
           DeviceConfigFile configFile =  configFileService.selectById(configFileRecordDto.getPackageId());

            String fileName16 = DataUtil.bytesToHexString(configFile.getFileName().getBytes("utf-8"));

            for (DeviceInfoVo device : configFileRecordDto.getDeviceList()) {
                DeviceConfigFileRecord configRecord = new DeviceConfigFileRecord();
                configRecord.setPackageId(configFileRecordDto.getPackageId());
                configRecord.setDeviceId(device.getId());
                configRecord.setProductId(configFileRecordDto.getProductId());
                configRecord.setIsDel(0);
                configRecord.setPath(configFileRecordDto.getPath());
                configRecord.setIssuedFlag(0);


                //序列号转16进制字节码
                String snCode = stringToHexString(device.getDeviceNo());
                Channel channel = NettyChannelManage.getChannel(device.getDeviceNo());
                if (channel != null) {
                    //已经下发了的设置为1
                    configRecord.setIssuedFlag(1);

                    LinkedHashMap<String, String> linkedMap = new LinkedHashMap<>();
                    linkedMap.put("02", fileLength16);
                    linkedMap.put("03", fileName16);
                    commonMethod.resMessageJoint(channel, snCode, linkedMap, "000B", "01", "00");
                    configFileRecordService.insert(configRecord);
                    redisUtil.set(redisHead + ":" + REDIS_SEND_CONFIG_HEAD + device.getDeviceNo(), path);
                    redisUtil.set(redisHead + ":" + REDIS_SEND_CONFIG_HEAD+"_record_" + device.getDeviceNo(), configRecord.getId());

                }
            }
            return "ok";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }


    @PostMapping("/{pk}/{dn}/method/{method}")
    public Object callMethod(@PathVariable(value = "pk") String pk, @PathVariable(value = "dn") String dn, @PathVariable(value = "method") String method, @RequestBody Map<String, String> params) {

        Map<String, Object> result = new HashMap<>(10);

        CallMethodDTO callMethodDTO = new CallMethodDTO();
        callMethodDTO.setDn(dn);
        callMethodDTO.setParams(params);
        callMethodDTO.setPk(pk);
        callMethodDTO.setMethod(method);


        NettyCtrlDTO nettyCtrlDTO = new NettyCtrlDTO();
        nettyCtrlDTO.setCtrl(1);
        nettyCtrlDTO.setData(JSONUtil.toJsonStr(callMethodDTO));

        deviceServer.callMethod(pk, dn, method, params);
        result.put("msg", "操作成功");
        return result;
    }


    @PostMapping("/delConfig")
    public Object delConfig(@RequestBody DeviceConfigDelDTO delDTO) {

        Map<String, Object> result = new HashMap<>(10);

        if (delDTO.getDeviceList() == null || delDTO.getDeviceList().size() == 0) {
            result.put("msg", "操作成功");
            return result;
        }

        try {
            String fileName16 = DataUtil.bytesToHexString(delDTO.getFileName().getBytes("utf-8"));
            LinkedHashMap<String, String> linkedMap = new LinkedHashMap<>();
            linkedMap.put("02", fileName16);

            for(DeviceInfoVo deviceInfoVo:delDTO.getDeviceList()){
                //序列号转16进制字节码
                String snCode = stringToHexString(deviceInfoVo.getDeviceNo());
                Channel channel = NettyChannelManage.getChannel(deviceInfoVo.getDeviceNo());
                if(channel!=null){
                    commonMethod.resMessageJoint(channel, snCode, linkedMap, "0010", "01", "00");
                }

            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        result.put("msg", "操作成功");
        return result;
    }

}



