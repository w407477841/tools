package com.xywg.iot.enums;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 16:07 2018/12/25
 * Modified By : wangyifei
 */
public enum PropertyType {

    INT32(1,Integer.class,"int32"),
    FLOAT(2,Float.class,"float"),
    DOUBLE(3,Double.class,"double"),
    BOOLEAN(4,Boolean.class,"boolean"),
    TEXT(5,String.class,"text")

    ;

    private Integer value;

    private Class clazz ;

    private String commons;

    PropertyType(Integer value, Class clazz,String commons) {
        this.value = value;
        this.clazz = clazz;
        this.commons = commons ;
    }

    public Integer getValue() {
        return value;
    }

    public Class getClazz() {
        return clazz;
    }

    public static String getCommons(Integer vlaue) {

         for(PropertyType type : PropertyType.values()){
             if(type.value .equals(vlaue)){
                 return type.commons;
             }
         }

        return null;
    }
}
