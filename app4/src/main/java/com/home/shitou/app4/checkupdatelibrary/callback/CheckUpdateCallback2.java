package com.home.shitou.app4.checkupdatelibrary.callback;

/**
 * 请求回调
 */
public interface CheckUpdateCallback2 {
    /**
     * 检查更新成功
     *
     * @param result 服务端返回的json信息
     */
    void onCheckUpdateSuccess(String result);

    /**
     * 检查更新失败
     *
     * @param failureMessage 失败信息
     */
    void onCheckUpdateFailure(String failureMessage);
}