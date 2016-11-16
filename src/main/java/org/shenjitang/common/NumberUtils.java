/*
 * NumberUtils.java
 *
 * Created on 2007年10月31日, 上午11:57
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.shenjitang.common;

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
    
}
