package com.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 11:30 2019/6/24
 * Modified By : wangyifei
 */
public class Maptest {

    public static void test(){



    }

    public static void main(String args[]){

        float a = 1.0f-.9f;
        float b = 0.9f-0.8f;
        System.out.println(a==b);
        BigDecimal decimalA = BigDecimal.valueOf(1.0f);
        BigDecimal decimalB = BigDecimal.valueOf(0.9f);
        BigDecimal decimalC =  BigDecimal.valueOf(0.8f);
        BigDecimal x = decimalA.subtract(decimalB);
        BigDecimal y = decimalB.subtract(decimalC);
        System.out.println(x.equals(y));

    }

}
