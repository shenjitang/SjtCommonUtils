/*
 * StringNumber.java
 *
 * Created on 2007年10月31日, 上午11:22
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.shenjitang.common;

/**
 *
 * @author xiaolie
 */
public class StringNumber {
    
    /** Creates a new instance of StringNumber */
    public StringNumber(String str, int num) {
        this.str = str;
        this.num = num;
    }
    
    public void inc() {
        this.num++;
    }

    /**
     * 保存属性 str 的值。
     */
    private String str;

    /**
     * 属性 str 的获取方法。
     * @return 属性 str 的值。
     */
    public String getStr() {
        return this.str;
    }

    /**
     * 属性 str 的设置方法。
     * @param str 属性 str 的新值。
     */
    public void setStr(String str) {
        this.str = str;
    }

    /**
     * 保存属性 num 的值。
     */
    private int num;

    /**
     * 属性 num 的获取方法。
     * @return 属性 num 的值。
     */
    public int getNum() {
        return this.num;
    }

    /**
     * 属性 num 的设置方法。
     * @param num 属性 num 的新值。
     */
    public void setNum(int num) {
        this.num = num;
    }
    
    public String toString() {
        return str + num;
    }
    
}
