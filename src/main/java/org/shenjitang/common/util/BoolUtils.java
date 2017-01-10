/*
 * BoolUtils.java
 *
 * Created on 2007年11月7日, 上午9:56
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.shenjitang.common.util;

/**
 *
 * @author xiaolie
 */
public class BoolUtils {
    
    /** Creates a new instance of BoolUtils */
    public BoolUtils() {
    }
    
    public static boolean getValue(Object value) {
        if (value == null) return false;
        if ("true".equalsIgnoreCase(value.toString())) return true;
        if ("t".equalsIgnoreCase(value.toString())) return true;
        if ("1".equals(value.toString())) return true;
        return false;
    }
    
}
