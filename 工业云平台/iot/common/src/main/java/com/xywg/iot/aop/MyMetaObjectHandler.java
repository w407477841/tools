package com.xywg.iot.aop;

import com.baomidou.mybatisplus.mapper.MetaObjectHandler;
import com.xywg.iot.util.UserUtil;
import org.apache.commons.lang.Validate;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.reflection.MetaObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
* @author: wangyifei
* Description: 填充
* Date: 10:07 2018/12/28
*/
@Component
public class MyMetaObjectHandler extends MetaObjectHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(MyMetaObjectHandler.class);
	private String isDelStr = "isDel";
	private String createTimeStr = "createTime";
	private String createUserStr = "createUser";
	private String createUserNameStr = "createUserName";
	private String modifyTimeStr = "modifyTime" ;
	private String modifyUserStr = "modifyUser";
	private String modifyUserNameStr = "modifyUserName";
	@Override
	public void insertFill(MetaObject metaObject) {

		final Class clazz = 	metaObject.getOriginalObject().getClass();
		getAllFieldsList(clazz).forEach(field->{
			if(field.getName().equals(isDelStr)){
				LOGGER.info("<{}>存在<{}>",clazz.getName(),field.getName());
				setInsetObject(isDelStr,0,metaObject);
			}else if(field.getName().equals(createTimeStr)){
				LOGGER.info("<{}>存在<{}>",clazz.getName(),field.getName());
				setInsetObject(createTimeStr,new Date(),metaObject);
			}else if(field.getName().equals(createUserStr)){
				LOGGER.info("<{}>存在<{}>",clazz.getName(),field.getName());
				setInsetObject(createUserStr,UserUtil.USERID.get(),metaObject);
			}else if(field.getName().equals(createUserNameStr)){
				LOGGER.info("<{}>存在<{}>",clazz.getName(),field.getName());
				setInsetObject(createUserNameStr,UserUtil.USERNAME.get(),metaObject);
			}
		});
	}

	@Override
	public void updateFill(MetaObject metaObject) {
		Class clz ;
		if(metaObject.getOriginalObject() instanceof MapperMethod.ParamMap) {
			// update(e,wrapper);
			//HashMap
			if(!((MapperMethod.ParamMap) metaObject.getOriginalObject()).containsKey("param1")) {
				clz = ((MapperMethod.ParamMap) metaObject.getOriginalObject()).get("et").getClass();
			}else {
				clz =  ((MapperMethod.ParamMap) metaObject.getOriginalObject()).get("param1").getClass();
			}
		}else{
			clz = 	metaObject.getOriginalObject().getClass();

		}
		final Class clazz = clz ;
		getAllFieldsList(clazz).forEach(field->{
			if(field.getName().equals(modifyTimeStr)){
				LOGGER.info("<{}>存在<{}>",clazz.getName(),field.getName());
					setModifyObject(modifyTimeStr,new Date(),metaObject);
			}else if(field.getName().equals(modifyUserStr)){
				LOGGER.info("<{}>存在<{}>",clazz.getName(),field.getName());
					setModifyObject(modifyUserStr,UserUtil.USERID.get(),metaObject);
			}else if(field.getName().equals(modifyUserNameStr)){
				LOGGER.info("<{}>存在<{}>",clazz.getName(),field.getName());
					setModifyObject(modifyUserNameStr,UserUtil.USERNAME.get(),metaObject);

			}
		});



	}

	private void  setInsetObject(String fieldName,Object value,MetaObject metaObject){
				if(getFieldValByName(fieldName,metaObject)==null){
					setFieldValByName(fieldName, value,metaObject);
				}
	}
	private void  setModifyObject(String fieldName,Object value,MetaObject metaObject){

		setFieldValByName(fieldName, value,metaObject);

	}


	private  Field[] getAllFields(final Class<?> cls) {
		final List<Field> allFieldsList = getAllFieldsList(cls);
		return allFieldsList.toArray(new Field[allFieldsList.size()]);
	}

	private  List<Field> getAllFieldsList(final Class<?> cls) {
		Validate.isTrue(cls != null, "class 不能为空");
		final List<Field> allFields = new ArrayList<>();
		Class<?> currentClass = cls;
		while (currentClass != null) {
			final Field[] declaredFields = currentClass.getDeclaredFields();
			for (final Field field : declaredFields) {
				allFields.add(field);
			}
			currentClass = currentClass.getSuperclass();
		}
		return allFields;
	}



}
