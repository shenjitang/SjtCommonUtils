/*
 * HibernateObjectDesc.java
 *
 * Created on 2007年11月29日, 下午4:03
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.shenjitang.common;

/**
 *
 * @author xiaolie
 */
public class HibernateObjectDesc {
    /** Creates a new instance of HibernateObjectDesc */
    public HibernateObjectDesc() {
    }
    
    private String descStr;
    
    public static HibernateObjectDesc parseStr(Object obj) {
        HibernateObjectDesc instance = new HibernateObjectDesc();
        instance.descStr = obj.toString();
        instance.parseStr();
        return instance;
    }
    
    private void parseStr() {
        int idx = descStr.indexOf('@');
        className = descStr.substring(0, idx).trim();
        String tempStr = descStr.substring(idx + 1);
        idx = tempStr.indexOf('[');
        hashCode = tempStr.substring(0, idx).trim();
        attrBody = tempStr.substring(idx).trim();
    }
    
    /**
     * 保存属性 className 的值。
     */
    private String className;

    /**
     * 属性 className 的获取方法。
     * @return 属性 className 的值。
     */
    public String getClassName() {
        return this.className;
    }

    /**
     * 属性 className 的设置方法。
     * @param className 属性 className 的新值。
     */
    public void setClassName(String className) {
        this.className = className;
    }

    /**
     * 保存属性 hashCode 的值。
     */
    private String hashCode;

    /**
     * 属性 hashCode 的获取方法。
     * @return 属性 hashCode 的值。
     */
    public String getHashCode() {
        return this.hashCode;
    }

    /**
     * 属性 hashCode 的设置方法。
     * @param hashCode 属性 hashCode 的新值。
     */
    public void setHashCode(String hashCode) {
        this.hashCode = hashCode;
    }

    /**
     * 保存属性 attrBody 的值。
     */
    private String attrBody;

    /**
     * 属性 attrBody 的获取方法。
     * @return 属性 attrBody 的值。
     */
    public String getAttrBody() {
        return this.attrBody;
    }

    /**
     * 属性 attrBody 的设置方法。
     * @param attrBody 属性 attrBody 的新值。
     */
    public void setAttrBody(String attrBody) {
        this.attrBody = attrBody;
    }
    
}
