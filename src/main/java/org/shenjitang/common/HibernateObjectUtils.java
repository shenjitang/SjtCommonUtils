/*
 * HibernateObjectUtils.java
 *
 * Created on 2007年11月29日, 下午4:02
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.shenjitang.common;

/**
 *
 * @author xiaolie
 */
public class HibernateObjectUtils {
    
    /** Creates a new instance of HibernateObjectUtils */
    public HibernateObjectUtils() {
    }
    
    public static boolean attrsEquals(Object obj1, Object obj2) {
        HibernateObjectDesc desc1 = HibernateObjectDesc.parseStr(obj1);
        HibernateObjectDesc desc2 = HibernateObjectDesc.parseStr(obj2);
        return desc1.getAttrBody().equals(desc2.getAttrBody());
    }
    
}
