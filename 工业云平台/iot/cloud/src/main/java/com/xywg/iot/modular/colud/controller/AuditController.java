package com.xywg.iot.modular.colud.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.xywg.log.annotations.OpenLog;
import com.xywg.iot.common.RoleConstant;
import com.xywg.iot.dto.QueryPageDTO;
import com.xywg.iot.enums.ResultCode;
import com.xywg.iot.modular.colud.service.AuditService;
import com.xywg.iot.modular.colud.service.UserRoleService;
import com.xywg.iot.modular.enterprise.model.vo.AuditVo;
import com.xywg.iot.vo.ResultVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/***
 *@author:jixiaojun
 *DATE:2019/1/24
 *TIME:10:56
 */
@RestController
@RequestMapping("/business/audit")
public class AuditController {
    @Autowired
    private AuditService auditService;
    @Autowired
    private UserRoleService userRoleService;

    @PostMapping("/getAuditList")
    @OpenLog
    @ApiOperation("获取待审核认证列表")
    @RolesAllowed(RoleConstant.ADMIN)
    public ResultVO getAuditList(@RequestBody QueryPageDTO queryPageDTO) {
        try {
            Page<AuditVo> page = new Page<>(queryPageDTO.getPageNum(),queryPageDTO.getPageSize());
            List<AuditVo> list = auditService.getAuditList(page,queryPageDTO);
            Map<String,Object> map = new HashMap<>(10);
            map.put("list",list);
            map.put("total",page.getTotal());
            return new ResultVO(ResultCode.SUCCESS,map);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return new ResultVO(ResultCode.SYS_ERROR,null);
    }

    @PostMapping("/audit")
    @OpenLog
    @ApiOperation("审核")
    @RolesAllowed(RoleConstant.ADMIN)
    public ResultVO audit(@RequestBody AuditVo auditVo) {
        try {
            if(userRoleService.updateRole(auditVo)) {
                if(new Integer(2).equals(auditVo.getAuditStatus())) {
                    return new ResultVO(ResultCode.AGREE_SUCCESS,null);
                }else {
                    return new ResultVO(ResultCode.DISAGREE_SUCCESS,null);
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        if(new Integer(2).equals(auditVo.getAuditStatus())) {
            return new ResultVO(ResultCode.AGREE_ERROR,null);
        }else {
            return new ResultVO(ResultCode.DISAGREE_ERROR,null);
        }
    }
}
