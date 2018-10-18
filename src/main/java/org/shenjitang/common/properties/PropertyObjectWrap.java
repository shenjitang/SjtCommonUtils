/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.shenjitang.common.properties;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author xiaolie33
 */
public class PropertyObjectWrap <T> {
    private T bean;
    private Map props = new HashMap();

    public PropertyObjectWrap(T bean) {
        this.bean = bean;
    }

    public T getBean() {
        return bean;
    }

    public void setBean(T bean) {
        this.bean = bean;
    }

    public Map getProps() {
        return props;
    }

    public void setProps(Map props) {
        this.props = props;
    }
    
}
