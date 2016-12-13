package com.neo.neo.smipleandroidframe;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import com.neo.neo.smipleandroidframe.base.BaseActivity;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private Button btLoading, btLoadingFail, btLoadingSuccess, btLogin, btFirstLoadingFail;
    private Handler mHandler = new Handler();
    private Handler myHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        intiView();
    }

    private void intiView() {
        btLoadingFail = (Button) findViewById(R.id.bt_loading_fail);
        btLoading = (Button) findViewById(R.id.bt_loading);
        btLoadingSuccess = (Button) findViewById(R.id.bt_loading_success);
        btLogin = (Button) findViewById(R.id.bt_login);
        btFirstLoadingFail = (Button) findViewById(R.id.bt_loading_first_fail);

        btLoadingFail.setOnClickListener(this);
        btLoading.setOnClickListener(this);
        btLoadingSuccess.setOnClickListener(this);
        btLogin.setOnClickListener(this);
        btFirstLoadingFail.setOnClickListener(this);
    }

    /**
     * 模仿数据加载
     */
    private void myLoading() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                showContent();
                showButtomToast("加载成功");
            }
        }, 3000);
    }

    /**
     * 模仿数据加载失败
     */
    private void myLoadingFail() {
        myHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                showProgressBar(false);
                showFail("加载失败");
            }
        }, 3000);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_loading_fail:
                showProgressBar(true);
                myLoadingFail();
                break;
            case R.id.bt_loading_first_fail:
                contentFail();//吐司错误
                break;
            case R.id.bt_loading:
                showLoading();//loading页面占满整个屏幕
                myLoading();
                break;
            case R.id.bt_loading_success:
                showContent();//数据加载成功显示数据
                break;
            case R.id.bt_login:
                showLogin();//此页面需要登录
                break;
        }
    }

    @Override
    protected void onRetryLoadData() {//点击再次加载
        super.onRetryLoadData();
        showContent();
    }

    @Override
    public void loginOk() {
        super.loginOk();
        showButtomToast("登录成功");
        showContent();
    }
}
