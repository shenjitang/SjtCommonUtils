package org.shenjitang.common;

/**
 * 该接口用于表示需要释放资源或进行清理的类，实现该接口的类告诉调用者需要显式的清理资源，
 * 清理的时机有可能是一个流程结束，触发结束事件的时候
 * User: gang.xie
 * Date: 14-4-11
 * Time: 上午9:16
 */

public interface Destroynable {

    /**
     * 实际的释放资源
     */
    void destroy();
}
