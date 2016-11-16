/*
 * Log4jConMBean.java
 *
 * Created on 2007年11月13日, 下午12:41
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.shenjitang.common;

/**
 *
 * @author xiaolie
 */
public interface Log4jConMBean {
    public String getLevel(String categoryName);
    public String setLevel(String categoryName, String level);
    public String getRootLevel();
    public String setRootLevel(String level);
    public String[] getAllCategoryLevel();
    public String getRootName();
    public String getName(String categoryName);
    
}
