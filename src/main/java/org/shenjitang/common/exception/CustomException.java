package org.shenjitang.common.exception;

import java.io.Serializable;

/**
 * User: gang.xie
 * Date: 14-4-16
 * Time: 下午1:27
 */

public class CustomException  extends Exception implements Serializable {

    private static final long serialVersionUID = 4835311323197403743L;


    private String msg;

    public CustomException() {
    }

    public CustomException(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return this.msg;
    }

}
