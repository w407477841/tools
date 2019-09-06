package com.honghu.oauth2.controller;

import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.honghu.oauth2.Const;
import com.honghu.oauth2.dto.*;
import com.honghu.oauth2.vo.PageVO;
import com.honghu.oauth2.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class BaseController<I extends IService<E> , E> {

    protected abstract String[] insertRoles();
    protected abstract String[] updateRoles();
    protected abstract String[] deleteRoles();
    protected abstract String[] queryRoles();
    @Autowired
    protected  I baseService;


    @PostMapping("insert")
    public ResultVO insert(@RequestBody @Validated(Const.GroupInsert.class) E e){
        if(noPermission(insertRoles())){
            return ResultVO.error("无权限");
        }

        if(baseService.save(e)){
            return ResultVO.success();
        }
        return ResultVO.error("失败");
    }
    @PostMapping("update")
    public ResultVO update(@RequestBody @Validated(Const.GroupUpdate.class) E e){
        if(noPermission(updateRoles())){
            return ResultVO.error("无权限");
        }
        if(baseService.updateById(e)){
            return ResultVO.success();
        }
        return ResultVO.error("失败");
    }
    @PostMapping("delete")
    public ResultVO delete(@RequestBody @Validated(Const.GroupDelete.class) DeleteDTO delete){
        if(noPermission(deleteRoles())){
            return ResultVO.error("无权限");
        }
        if(baseService.removeByIds(delete.getIds())){
            return  ResultVO.success();
        }
        return  ResultVO.error("失败");
    }

    /**
     *  根据条件查询，不分页
     * @return
     */
    @PostMapping("query")
    public ResultVO query(@RequestBody QueryDTO q){

        if(noPermission(queryRoles())){
            return ResultVO.error("无权限");
        }
        QueryWrapper<E> wrapper = new QueryWrapper<>();
        List<QueryParam> params = q.getParams();
        packageParam(params,wrapper);
        QueryOrder queryOrder  = q.getOrder();
        if(queryOrder!=null){
            wrapper.orderBy(queryOrder.isOpen(),queryOrder.isAsc(),queryOrder.getName());
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
                return   ResultVO.error("非法参数");
            }
        }
        if(selectColumns!=null&&selectColumns.size() > 0){
            wrapper.select(ArrayUtil.toArray(selectColumns,String.class));
            return   ResultVO.success(baseService.listMaps(wrapper));
        }else{
            return   ResultVO.success(baseService.list(wrapper));
        }
    }

    @PostMapping("page")
    public ResultVO page(@RequestBody @Validated(Const.GroupPage.class) QueryPageDTO q){
        if(noPermission(queryRoles())){
            return ResultVO.error("无权限");
        }
        Page<E> page  =new Page<>(q.getPageNum(),q.getPageSize());
        QueryWrapper<E> wrapper = new QueryWrapper<>();
        List<QueryParam> params = q.getParams();
        packageParam(params,wrapper);
        QueryOrder queryOrder  = q.getOrder();
        if(queryOrder!=null){
            wrapper.orderBy(queryOrder.isOpen(),queryOrder.isAsc(),queryOrder.getName());
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
                return  ResultVO.error("非法参数");
            }
        }
        if(selectColumns!=null&&selectColumns.size() > 0){
            wrapper.select(ArrayUtil.toArray(selectColumns,String.class));
            baseService.pageMaps(page,wrapper);
        }else{
            baseService.page(page,wrapper);
        }

        return ResultVO.success(new PageVO(page.getTotal(),page.getRecords()));
    }


    private boolean noPermission(String permissions []){
        //默认 ADMIN  权限
        if(permissions==null){
            permissions  = new String [] {Const.ADMIN_ROLE};
        }
        Collection<? extends GrantedAuthority> auths= SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        for(String per : permissions ){
            for(GrantedAuthority auth:  auths){
                if(auth.getAuthority().replace("ROLE_","").equals(per)){
                    return false;
                }
            }
        }
        return true;
    }


    private  void packageParam(List<QueryParam> params , QueryWrapper<E> wrapper ){
        if(params!=null){
            params.forEach(param->{
                switch(param.getCondition()){
                    case ">":
                        wrapper.gt(param.getName(),param.getValue());
                        break;
                    case ">=":
                        wrapper.ge(param.getName(),param.getValue());
                        break;
                    case "=":
                        wrapper.eq(param.getName(),param.getValue());
                        break;
                    case "<":
                        wrapper.lt(param.getName(),param.getValue());
                        break;
                    case "<=":
                        wrapper.le(param.getName(),param.getValue());
                        break;
                    case "like":
                        wrapper.like(param.getName(),param.getValue().toString());
                        break;
                    default:
                        break;
                }
            });
        }

    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResultVO validError( MethodArgumentNotValidException e){

        BindingResult bindingResult = e.getBindingResult();
        // 我只取一瓢
        FieldError fieldError = bindingResult.getFieldError();
        return ResultVO.error(fieldError.getField() + fieldError.getDefaultMessage());
    }




    @ExceptionHandler(value = {HttpRequestMethodNotSupportedException.class})
    public ResultVO methodNotSupported( HttpRequestMethodNotSupportedException e){

        return ResultVO.error("不支持");
    }

    @ExceptionHandler(value = {Exception.class})
    public ResultVO rootException( Exception e){
        e.printStackTrace();
        // 我只取一瓢
        return ResultVO.error("系统异常，请稍后再试！");
    }

}

