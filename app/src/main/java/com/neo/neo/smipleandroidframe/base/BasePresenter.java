package com.neo.neo.smipleandroidframe.base;/**
 * Created by jyt on 2016/9/26.
 */


import android.content.Context;


/**
 * created by neo on 2016/9/26 10:34
 */
public class BasePresenter {

    public BasePresenter() {
    }


    //登录
    protected <T extends BaseContract> void login(final T baseContract, final Context context, String phoneNumber, String code) {
        baseContract.loginOk();
    }

    //发送验证码
    public <T extends BaseContract> void sendCode(final T baseContract, final Context context, String phoneNumber) {
        baseContract.showCode();
    }
}
