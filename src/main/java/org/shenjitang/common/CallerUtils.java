/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.shenjitang.common;

/**
 *
 * @author admin
 */
public class CallerUtils extends SecurityManager {

    public CallerUtils() {
    }
    public Class[] getClassCtt() {
        return getClassContext();
    }
    
    public static Class getCaller() {
        CallerUtils mng = new CallerUtils();
        Class[] cc = mng.getClassContext();
        if (cc.length < 3) return null;
        return cc[2];
    }

    public static String getCallerName() {
        CallerUtils mng = new CallerUtils();
        Class[] cc = mng.getClassContext();
        if (cc.length < 3) return "java vm";
        return cc[2].getName();
    }
    
    public static String getCallerNameStack() {
        CallerUtils mng = new CallerUtils();
        Class[] cc = mng.getClassContext();
        if (cc.length < 3) return "java vm";
        String s = "java vm";
        int begin = (cc.length - 1);
        if (begin > 5) {
            begin = 5;
            s += "->...";
        }
        for (int i = begin; i > 1; i--) {
            s += "->" + cc[i].getName();
        }
        return s;
    }

    public static String getAllCallerNameStack() {
        CallerUtils mng = new CallerUtils();
        Class[] cc = mng.getClassContext();
        if (cc.length < 3) return "java vm";
        String s = "java vm";
        for (int i = (cc.length - 1); i > 1; i--) {
            s += "->" + cc[i].getName();
        }
        return s;
    }
}
