/*
 * Log4jCon.java
 *
 * Created on 2007年11月13日, 下午12:42
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.shenjitang.common;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedOperationParameter;
import org.springframework.jmx.export.annotation.ManagedOperationParameters;
import org.springframework.jmx.export.annotation.ManagedResource;

import java.util.ArrayList;
import java.util.Enumeration;

@ManagedResource(objectName="bean:name=log4jCon", description="log4j Bean", log=true,
logFile="jmx.log", currencyTimeLimit=15, persistPolicy="OnUpdate", persistPeriod=200,
persistLocation="foo", persistName="log4j")
public class Log4jCon implements Log4jConMBean {

    /** Creates a new instance of Log4jCon */
    public Log4jCon() {
        
    }

    @ManagedOperation(description="set log lever")
    @ManagedOperationParameters({
        @ManagedOperationParameter(name = "categoryName", description = "category name"),
        @ManagedOperationParameter(name = "level", description = "level")})
    public String setLevel(String categoryName, String level) {
        Logger.getLogger(categoryName).setLevel(Level.toLevel(level));
        return getLevel(categoryName);
    }

    public String getLevel(String categoryName) {
        Logger log = Logger.getLogger(categoryName);
        if (log.getLevel() != null) {
            return log.getLevel().toString();
        } else {
            return "";
        }
    }
    
    public String getRootName() {
        return Logger.getRootLogger().getName();
    }
    
    public String getName(String categoryName) {
        return Logger.getLogger(categoryName).getName();
    }
    
    public String getRootLevel() {
        return Logger.getRootLogger().getLevel().toString();
    }

    public String setRootLevel(String level) {
        Logger.getRootLogger().setLevel(Level.toLevel(level));
        return getRootLevel();
    }

    public String[] getAllCategoryLevel() {
        ArrayList list = new ArrayList();
        try {
            StringBuilder res = new StringBuilder();
            Enumeration enu = Logger.getRootLogger().getLoggerRepository().getCurrentLoggers();
            while (enu.hasMoreElements()) {
                Logger log = (Logger)enu.nextElement();
                if (log.getLevel() != null) {
                    res.append('[').append(log.getName()).append(':').append(log.getLevel()).append("] ");
                    list.add("[" + log.getName() + ":" + log.getLevel() + "]");
                }
            }
            //return res.toString();
            String[] resArray = new String[list.size()];
            list.toArray(resArray);
            return resArray;
        } catch (Exception e) {
            return new String[] {e.toString()};
        }
    }
    
}
