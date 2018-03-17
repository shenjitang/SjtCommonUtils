/*
 * NumberUtils.java
 *
 * Created on 2007年10月31日, 上午11:57
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.shenjitang.common.util;

import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author xiaolie
 */
public class NumberUtils {

    /**
     * Creates a new instance of NumberUtils
     */
    public NumberUtils() {
    }

    public static int max(Integer[] array) {
        return Collections.max(Arrays.asList(array));
    }

    public static int min(Integer[] array) {
        return Collections.min(Arrays.asList(array));
    }

    /**
     * 把字符串分解，转换成Integer类型，并放入list中
     * add by shw on 2011-7-26
     *
     * @param str         需要分解的字符串
     * @param regex       分隔符
     * @param integerList 存放Integer数据的list
     * @return
     */
    public static boolean splitStrToList(String str, String regex, List<Integer> integerList) {
        //如果为null，则进行初始化
        if (integerList == null) {
            integerList = new ArrayList();
        }
        //要分解的字符串不为空
        if (StringUtils.isNotEmpty(str)) {
            if (StringUtils.isNotEmpty(regex)) {//分隔符存在
                String[] strs = str.split(regex);
                for (String id : strs) {
                    if (MathUtils.isNumber(id)) {
                        integerList.add(new Integer(id));
                    }
                }
            } else {//分隔符不存在
                if (MathUtils.isNumber(str)) {
                    integerList.add(new Integer(str));
                }
            }
        }
        return integerList.size() > 0;
    }

    /**
     * 把Integer类型的list转换成带分隔符的String
     * add by shw on 2011-7-28
     *
     * @param idsList
     * @param regex
     * @return
     */
    public static String getStrByList(List<Integer> idsList, String regex) {
        StringBuffer ids = new StringBuffer(200);
        for (int i = 0; i < idsList.size(); ++i) {
            if (i > 0) {
                ids.append(regex);
            }
            ids.append(idsList.get(i).toString());
        }
        return ids.toString();
    }

    /**
     * double精确小数位
     *
     * @param value 原值
     * @param scale 小数位
     * @return double
     */
    public static Double decimal(Double value, Integer scale) {
        if (scale < 0) {
            return value;
        }
        BigDecimal bigDecimal = new BigDecimal(value);
        return bigDecimal.setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * double保留有效位数
     *
     * @param value  原值
     * @param scale  小数位
     * @param isNull 数值为空的时候返回值
     * @return String
     */
    public static String effectivePosition(Double value, Integer scale, String isNull) {
        if (value == null) {
            return isNull;
        }
        BigDecimal bigDecimal;
        if (scale < 0) {
            bigDecimal = new BigDecimal(String.valueOf(value));
            return removeZero(bigDecimal.toPlainString());
        }
        double d = value;
        int v = (int) d;
        if (v == d) {
            bigDecimal = new BigDecimal(String.valueOf(value));
            return removeZero(bigDecimal.toPlainString());
        }
        if (scale == 0) {
            bigDecimal = new BigDecimal(String.valueOf(v));
            return removeZero(bigDecimal.toPlainString());
        }
        if (value < 0) {
            return "-" + significand(-value, scale);
        } else {
            return significand(value, scale);
        }
    }

    private static String significand(Double value, int scale) {
        double d = value;
        int v = (int) d;
        BigDecimal b = new BigDecimal(String.valueOf(value - v), new MathContext(scale, RoundingMode.HALF_DOWN));
        String str = removeZero(b.toPlainString());
        if (str.equalsIgnoreCase("1")) {
            return (v + 1) + "";
        }
        return v + str.replace("0.", ".");
    }


    private static String removeZero(String value) {
        if (value.indexOf(".") > 0) {
            value = value.replaceAll("0+?$", "");//去掉后面无用的零
            value = value.replaceAll("[.]$", "");//如小数点后面全是零则去掉小数点
        }
        return value;
    }

    public static void main(String[] args) {
        List<Integer> ids = new ArrayList<>();
        splitStrToList("23,2,2323,4343,", ",", ids);
        System.out.println("~~~~~~~~~~~~~~size:" + ids.size());
        for (int id : ids) {
            System.out.println("~~~~~~~~~~~~~~~~~~:" + id);
        }
        ids = new ArrayList<>();
        splitStrToList("23,2,2323,4343,", "", ids);
        System.out.println("~~~~~~~~~~~~~~size:" + ids.size());

        ids = new ArrayList<>();
        splitStrToList("23234", "", ids);
        System.out.println("~~~~~~~~~~~~~~size:" + ids.size());
    }
}
