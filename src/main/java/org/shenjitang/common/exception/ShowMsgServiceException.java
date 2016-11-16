/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */

package org.shenjitang.common.exception;

/**
 * 当程序中出现异常，需要向前台显示信息时，可以抛出此异常，由前端显示此异常的 message
 *
 * @author admin
 */
@SuppressWarnings("serial")
public class ShowMsgServiceException extends RuntimeException {

    private String debugMsg;

    private boolean normal = false;

    public String getDebugMsg() {
        return debugMsg;
    }

    public void setDebugMsg(String debugMsg) {
        this.debugMsg = debugMsg;
    }

    /**
     * Creates a new instance of <code>ServiceException</code> without detail
     * message.
     */
    public ShowMsgServiceException() {
        super();
    }

    /**
     * Constructs an instance of <code>ServiceException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public ShowMsgServiceException(String msg) {
        super(msg);
    }

    public ShowMsgServiceException(String msg, boolean normal) {
        super(msg);
        this.normal = normal;
    }

    public ShowMsgServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ShowMsgServiceException(Throwable cause) {
        super(cause);
    }

    @Override
    public String toString() {
        return getMessage();
    }

    public boolean isNormal() {
        return normal;
    }

    public void setNormal(boolean normal) {
        this.normal = normal;
    }

}
