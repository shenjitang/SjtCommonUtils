package org.shenjitang.common.exception;

import java.io.Serializable;

/**
 * User: gang.xie
 * Date: 14-4-14
 * Time: 下午3:45
 */
public class ExistingException extends Exception implements Serializable {

    private static final long serialVersionUID = 2313650704703980821L;

    private String msg;

    public ExistingException() {
    }

    public ExistingException(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return this.msg;
    }


}
