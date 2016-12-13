package com.neo.neo.smipleandroidframe.base;


/**
 * Created by Neo on 2016/8/26.
 */
public interface BaseContract {

    void contentFail();//网络错误

    void dataFail();//数据解析错误

    void showFail(String message);//展示错误

    void timeOutFail();//超时错误

    void showNetError(String msg);//展示错误

    void tokenOld();//toke过期

    void showCode();//验证码

    void loginOk();//登录成功

    void loginFail(String msg);//登录失败


}
