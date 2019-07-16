package com.xywg.iot.base;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.IService;
import com.xywg.log.annotations.OpenLog;
import com.xywg.iot.common.RoleConstant;
import com.xywg.iot.dto.QueryWhereDto;
import com.xywg.iot.enums.ResultCode;
import com.xywg.iot.util.UserUtil;
import com.xywg.iot.vo.ResultVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.security.RolesAllowed;

/**
 * @author hjy
 */
public class BasalController<T extends BaseEntity<T>, S extends IService<T>> {
    @Autowired
    public S service;

    @PostMapping("insert")
    @ApiOperation("插入")
    @OpenLog
    @RolesAllowed({RoleConstant.ADMIN,RoleConstant.AUTH,RoleConstant.THIRD,RoleConstant.BASE})
    public ResultVO insert(@RequestBody T e) {
        if (service.insert(e)) {
            return new ResultVO(ResultCode.INSERT_SUCCESS, null);
        } else {
            return new ResultVO(ResultCode.INSERT_ERROR, null);
        }
    }

    @PostMapping("update")
    @ApiOperation("更新")
    @OpenLog
    @RolesAllowed({RoleConstant.ADMIN,RoleConstant.AUTH,RoleConstant.THIRD,RoleConstant.BASE})
    public ResultVO update(@RequestBody T e) {
        if (service.updateById(e)) {
            return new ResultVO(ResultCode.UPDATE_SUCCESS, null);
        } else {
            return new ResultVO(ResultCode.UPDATE_ERROR, null);
        }
    }

    @PostMapping("delete")
    @ApiOperation("删除")
    @OpenLog
    @RolesAllowed({RoleConstant.ADMIN,RoleConstant.AUTH,RoleConstant.THIRD,RoleConstant.BASE})
    public ResultVO delete(@RequestBody QueryWhereDto<T> m) {
        if (service.deleteBatchIds(m.getIds())) {
            return new ResultVO(ResultCode.DELETE_SUCCESS, null);
        } else {
            return new ResultVO(ResultCode.DELETE_ERROR, null);
        }
    }

    @GetMapping("getById")
    @ApiOperation("根据id查询")
    @OpenLog
    @RolesAllowed({RoleConstant.ADMIN,RoleConstant.AUTH,RoleConstant.THIRD,RoleConstant.BASE})
    public ResultVO selectById(@RequestParam Integer id) {
        return new ResultVO(ResultCode.SUCCESS, service.selectById(id));
    }

    /**
     * 不带任何条件和分页的查询list
     */
    @ApiOperation("不带分页和条件的列表")
    @GetMapping("getListNoPageAndWhere")
    @OpenLog
    @RolesAllowed({RoleConstant.ADMIN,RoleConstant.AUTH,RoleConstant.THIRD,RoleConstant.BASE})
    public ResultVO selectList() {
        EntityWrapper<T> ew = new EntityWrapper<>();
        EntityWrapper<T> wrapper = dataFilter(ew);
        return new ResultVO(ResultCode.SUCCESS, service.selectList(wrapper));
    }

    public EntityWrapper<T> dataFilter(EntityWrapper<T> wrapper) {
        //如果不是管理员 那么数据需要过滤
        if (!UserUtil.ISADMIN.get()) {
            wrapper.eq("create_user", UserUtil.USERID.get());
        }
        return wrapper;
    }
}
