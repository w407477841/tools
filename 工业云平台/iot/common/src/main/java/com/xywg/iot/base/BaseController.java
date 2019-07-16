package com.xywg.iot.base;

import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.xywg.log.annotations.OpenLog;
import com.xywg.iot.common.RoleConstant;
import com.xywg.iot.dto.*;
import com.xywg.iot.enums.ResultCode;
import com.xywg.iot.util.BaseControllerUtil;
import com.xywg.iot.util.UserUtil;
import com.xywg.iot.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * @author : wangyifei
 * Description model类未继承BaseModel
 * Date: Created in 16:32 2018/12/27
 * Modified By : wangyifei
 */
public  class BaseController<T  extends Model<T>, S extends IService<T>> {

    /**
     * 新增权限
     */
    private String [] insertRoles = {RoleConstant.ADMIN} ;
    /**
     * 修改权限
     */
    private String [] updateRoles = {RoleConstant.ADMIN} ;
    /**
     * 查询权限
     */
    private String [] queryRoles =  {RoleConstant.ADMIN} ;
    /**
     *  删除权限
     */
    private String [] deleteRoles = {RoleConstant.ADMIN} ;

    public void setDeleteRoles(String[] deleteRoles) {
        this.deleteRoles = deleteRoles;
    }

    public void setInsertRoles(String[] insertRoles) {
        this.insertRoles = insertRoles;
    }

    public void setUpdateRoles(String[] updateRoles) {
        this.updateRoles = updateRoles;
    }

    public void setQueryRoles(String[] queryRoles) {
        this.queryRoles = queryRoles;
    }

    @Autowired
    public S service;

    @PostMapping("insert")
    @OpenLog
    public ResultVO insert(@RequestBody T e) {
        if(!hasPermission(insertRoles)){
            return new ResultVO(ResultCode.NO_PERMISSION,null);
        }
        if (service.insert(e)) {
            return new ResultVO(ResultCode.INSERT_SUCCESS, null);
        } else {
            return new ResultVO(ResultCode.INSERT_ERROR, null);
        }
    }

    /**
     * hujingyun
     * @param dto
     * @param <T>
     * @return
     */
    @PostMapping("selectById")
    @OpenLog
    public <T> ResultVO selectById(@RequestBody SelectByIdDTO dto) {
        return new ResultVO(ResultCode.SUCCESS, service.selectById(dto.getId()));
    }


    @PostMapping("update")
    @OpenLog
    public ResultVO update(@RequestBody T e) {
        if(!hasPermission(updateRoles)){
            return new ResultVO(ResultCode.NO_PERMISSION,null);
        }
        if (service.updateById(e)) {
            return new ResultVO(ResultCode.UPDATE_SUCCESS, null);
        } else {
            return new ResultVO(ResultCode.UPDATE_ERROR, null);
        }
    }

    @PostMapping("delete")
    @OpenLog
    public ResultVO delete(@RequestBody DeleteDTO m) {
        if(!hasPermission(deleteRoles)){
            return new ResultVO(ResultCode.NO_PERMISSION,null);
        }
        if (service.deleteBatchIds(m.getIds())) {
            return new ResultVO(ResultCode.DELETE_SUCCESS, null);
        } else {
            return new ResultVO(ResultCode.DELETE_ERROR, null);
        }
    }

    /**
     *  不过滤 ，一般用于数据字典
     * @param q
     * @return
     */
    @PostMapping("selectList")
    @OpenLog
    public ResultVO selectList(@RequestBody QueryDTO q){
        if(!hasPermission(queryRoles)){
            return new ResultVO(ResultCode.NO_PERMISSION,null);
        }
        return baseList(q,true);
    }

    /**
     *  根据用户过滤， 管理员不过滤
     * @param q
     * @return
     */
    @PostMapping("selectFilterList")
    @OpenLog
    public ResultVO selectFilterList(@RequestBody QueryDTO q){
        if(!hasPermission(queryRoles)){
            return new ResultVO(ResultCode.NO_PERMISSION,null);
        }
        return baseList(q,UserUtil.ISADMIN.get());
    }

    /**
     *  不过滤，一般用于数据字典
     * @param q
     * @return
     */
    @PostMapping("selectPage")
    @OpenLog
    public ResultVO selectList(@RequestBody QueryPageDTO q){
        if(!hasPermission(queryRoles)){
            return new ResultVO(ResultCode.NO_PERMISSION,null);
        }
        return basePage(q,true);
    }

    /**
     *  根据用户过滤， 管理员不过滤
     * @param q
     * @return
     */
    @PostMapping("selectFilterPage")
    @OpenLog
    public ResultVO selectFilterList(@RequestBody QueryPageDTO q){
        if(!hasPermission(queryRoles)){
            return new ResultVO(ResultCode.NO_PERMISSION,null);
        }
        return basePage(q,UserUtil.ISADMIN.get());
    }

    public ResultVO basePage(QueryPageDTO q,boolean b){
        Page<T> page  =new Page<>(q.getPageNum(),q.getPageSize());
        Wrapper<T> wrapper = new EntityWrapper<>();
        List<QueryParam> params = q.getParams();
        BaseControllerUtil.packageParam(params,wrapper);
        if(!b){
            wrapper.eq("create_user",UserUtil.USERID.get());
        }
        QueryOrder queryOrder  = q.getOrder();
        if(queryOrder!=null){
            wrapper.orderBy(queryOrder.isOpen(),queryOrder.getName(),queryOrder.isAsc());
        }
        List<String> selectColumns = q.getSelectColumns();
        if(selectColumns!=null&&selectColumns.size() > 0){
            AtomicBoolean illegal = new AtomicBoolean(false);
            selectColumns.forEach(item->{
                if(item.indexOf(";")>0||item.toUpperCase().indexOf("SELECT")>0){
                    illegal.set(true);
                    return;
                }
            });
            if(illegal.get()){
                return  new ResultVO(ResultCode.ILLEGAL,null);
            }
        }
        if(selectColumns!=null&&selectColumns.size() > 0){
            wrapper.setSqlSelect(ArrayUtil.toArray(selectColumns,String.class));
            service.selectMapsPage(page,wrapper);
        }else{
            service.selectPage(page,wrapper);
        }
        ResultPage<T> resultPage  = new ResultPage<>(page.getTotal(),page.getRecords());
        return  new ResultVO(ResultCode.SUCCESS,resultPage);
    }
    public ResultVO baseList( QueryDTO q,boolean b){
        Wrapper<T> wrapper = new EntityWrapper<>();
        List<QueryParam> params = q.getParams();
        BaseControllerUtil.packageParam(params,wrapper);
        if(!b){
            wrapper.eq("create_user",UserUtil.USERID.get());
        }
        QueryOrder queryOrder  = q.getOrder();
        if(queryOrder!=null){
            wrapper.orderBy(queryOrder.isOpen(),queryOrder.getName(),queryOrder.isAsc());
        }
        List<String> selectColumns = q.getSelectColumns();
        if(selectColumns!=null&&selectColumns.size() > 0){
            AtomicBoolean illegal = new AtomicBoolean(false);
            selectColumns.forEach(item->{
                if(item.indexOf(";")>0||item.toUpperCase().indexOf("SELECT")>0){
                    illegal.set(true);
                    return;
                }
            });
            if(illegal.get()){
                return  new ResultVO(ResultCode.ILLEGAL,null);
            }
        }
        if(selectColumns!=null&&selectColumns.size() > 0){
            wrapper.setSqlSelect(ArrayUtil.toArray(selectColumns,String.class));
            return  new ResultVO(ResultCode.SUCCESS,service.selectMaps(wrapper));
        }else{
            return  new ResultVO(ResultCode.SUCCESS,service.selectList(wrapper));
        }
    }


    private boolean hasPermission(String permissions []){
        //默认 ADMIN  权限
        if(permissions==null){
            permissions  = new String [] {RoleConstant.ADMIN};
        }
        Collection<? extends GrantedAuthority> auths= SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        for(String per : permissions ){
            for(GrantedAuthority auth:  auths){
                if(auth.getAuthority().replace("ROLE_","").equals(per)){
                    return true;
                }
            }
        }
        return false;
    }

}
