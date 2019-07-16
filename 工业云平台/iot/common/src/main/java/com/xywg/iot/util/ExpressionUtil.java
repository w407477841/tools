package com.xywg.iot.util;

import com.xywg.iot.exceptions.DataTypeException;
import com.xywg.iot.exceptions.ExpressionException;
import com.xywg.iot.exceptions.MissingParamException;
import com.xywg.iot.exceptions.NotBooleanException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author : wangyifei
 * Description 表达式工具类
 * 其中$ 和 self 为保留字符不能使用
 * self 表示当前字段值
 * $propertyName 表示该字段值
 * 例如：
 * "$str == 'mamapi' && $shidu>20 && $temp>20"
 * $str 表示 str字段
 * $shidu 表示 shidu字段
 * $temp 表示 temp字段
 *
 *
 *
 *
 * Date: Created in 11:08 2018/12/25
 * Modified By : wangyifei
 */
public class ExpressionUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExpressionUtil.class);

    private static final String  PATTERT_STR = "\\$[A-Za-z0-9]+";

    /**
     *  单属性 报警判断
     * @param expression
     * @param value
     * @return
     */
    public  static  boolean  test(String expression,Object value) throws ExpressionException, NotBooleanException {
        System.out.println(expression);
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("js");
        engine.put("self",value );

        return eval(engine,expression);

    }

    private static boolean eval(ScriptEngine engine ,  String expression) throws  NotBooleanException,ExpressionException{
        Object result1 ;
        try {
            result1 = engine.eval("("+expression+")?true:false");
        } catch (ScriptException e) {
            throw new ExpressionException("表达式类型有误");
        }
        if("java.lang.Boolean".equals(result1.getClass().getName())){
            return (Boolean)result1;
        }else{
            throw new NotBooleanException("表达式解析后非boolean");
        }
    }


    /**
     *  多属性关联 报警判断
     * @param params  参数值
     * @param expression  表达式
     * @param value  值
     * @return
     *
     */
    public  static  boolean  test(Map<String,Object> params,String expression,Object value) throws MissingParamException, ExpressionException, NotBooleanException {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("js");
        engine.put("self",value );
        if(!expression.contains("$")){
            //不存在$ ,为简单表达式
                return test(expression,value);
        }
        // 看看参数中有没有
        Pattern pattern = Pattern.compile(PATTERT_STR);
        Matcher matcher  = pattern.matcher(expression);
        while(matcher.find()){
            String key  = expression.substring(matcher.start()+1,matcher.end());
            if(!params.containsKey(key)){
                throw new MissingParamException(key);
            }
        }


        for (Map.Entry<String,Object> item: params.entrySet()){
            engine.put("$"+item.getKey(),item.getValue());
        }


        return eval(engine,expression);
    }


    public static Object parseObject( int type , String value ) throws DataTypeException{

        switch ( type){
            case 1 :
                return  Integer.parseInt(value);
            case 2 :
                return Float.parseFloat(value);
            case 3 :
                return Double.parseDouble(value);
            case 4 :
                return "1".equals(value)?true:false;
            case 5 :
                return value;
            case 6 :
                return new Date(Long.parseLong(value));
            default:
                throw new DataTypeException("数据类型异常");
        }

    }


    public static void main (String args[]) throws ScriptException {

//        String expression  = "$str == 'mamapi' && $shidu>20 && $temp>20" ;
//
//
//        Pattern pattern = Pattern.compile(PATTERT_STR);
//        Matcher matcher  = pattern.matcher(expression);
//        while(matcher.find()){
//            System.out.println("start(): "+matcher.start());
//            System.out.println("end(): "+matcher.end());
//            System.out.println(expression.substring(matcher.start(),matcher.end()));
//        }
//
//
//        ScriptEngineManager manager = new ScriptEngineManager();
//        ScriptEngine engine = manager.getEngineByName("js");
//        engine.put("$str","mamapi" );
//        engine.put("$shidu",60.0 );
//        engine.put("$temp",40.0 );
//        System.out.println(engine.eval(expression));
       // System.out.println(test(expression,4,"haha"));

        Map<String,Object> params = new HashMap<>();
        params.put("Humi",1);

        try {
           System.out.println(test(params,"self<0 | self>100",1)); ;
        } catch (MissingParamException e) {
            e.printStackTrace();
        } catch (ExpressionException e) {
            e.printStackTrace();
        } catch (NotBooleanException e) {
            e.printStackTrace();
        }


    }


}
