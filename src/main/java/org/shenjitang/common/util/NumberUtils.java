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

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author xiaolie
 */
public class NumberUtils {
    
    /** Creates a new instance of NumberUtils */
    public NumberUtils() {
    }
    
    public static int max(int[] array) {
        int result = array[0];
        for (int i = 1; i < array.length; i++) {
            result = result > array[i] ? result : array[i];
        }
        return result;
    }
    
    public static int min(int[] array) {
        int result = array[0];
        for (int i = 1; i < array.length; i++) {
            result = result < array[i] ? result : array[i];
        }
        return result;
    }
    
    /**
     * 把字符串分解，转换成Integer类型，并放入list中
     * add by shw on 2011-7-26
     * @param str    需要分解的字符串
     * @param regex  分隔符
     * @param integerList  存放Integer数据的list
     * @return
     */
    public static boolean splitStrToList(String str, String regex, List<Integer> integerList){
    	//如果为null，则进行初始化
    	if(integerList == null){
    		integerList = new ArrayList();
}
    	//要分解的字符串不为空
    	if(StringUtils.isNotEmpty(str)){
    		if(StringUtils.isNotEmpty(regex)){//分隔符存在
    			String[] strs = str.split(regex);
    			for(String id : strs){
    				if(MathUtils.isNumber(id)){
    					integerList.add(new Integer(id));
    				}
    			}
    		}else{//分隔符不存在
    			if(MathUtils.isNumber(str)){
    				integerList.add(new Integer(str));
    			}
    		}
    	}
    	if(integerList != null && integerList.size() > 0){
    		return true;
    	}else{
    		return false;
    	}
    }
    /**
     * 把Integer类型的list转换成带分隔符的String
     * add by shw on 2011-7-28
     * @param idsList
     * @param regex
     * @return
     */
    public static String getStrByList(List<Integer> idsList, String regex){
    	StringBuffer ids = new StringBuffer(200);
    	for(int i = 0; i < idsList.size(); ++i){
    		if(i > 0){
    			ids.append(regex);
    		}
    		ids.append(idsList.get(i).toString());
    	}
    	return ids.toString();
    }
    public static void main(String[] args){
    	List<Integer> ids = new ArrayList();
    	splitStrToList("23,2,2323,4343,", ",", ids);
    	System.out.println("~~~~~~~~~~~~~~size:"+ids.size());
    	for(int id : ids){
    		System.out.println("~~~~~~~~~~~~~~~~~~:"+id);
    	}
    	ids = new ArrayList();
    	splitStrToList("23,2,2323,4343,", "", ids);
    	System.out.println("~~~~~~~~~~~~~~size:"+ids.size());
    	
    	ids = new ArrayList();
    	splitStrToList("23234", "", ids);
    	System.out.println("~~~~~~~~~~~~~~size:"+ids.size());
    }
}
