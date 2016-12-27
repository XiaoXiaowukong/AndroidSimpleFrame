package com.neo.neo.smipleandroidframe.base;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.neo.neo.smipleandroidframe.R;
import com.neo.neo.smipleandroidframe.application.SimpleFrameApplication;
import com.neo.neo.smipleandroidframe.views.LoadingDialog;
import com.neo.neo.smipleandroidframe.views.MessageDialog;

import java.net.URISyntaxException;
import java.util.List;


/**
 * Created by neo on 16/11/29.此框架仅作为参考，里卖面图片可以自行修改。自建回退栈，管理方便。满足现在大多数App的需求。
 * 邮箱：18310187026@163.com
 */
public abstract class BaseActivity extends LifecycleActivity implements BaseContract {
    private LoadingDialog mProgressDialog;
    private Toast mToast;
    private BaseActivity.PullToRefreshCompleteListener pullToRefreshCompleteListener;
    //网络接收
    private IntentFilter filter;
    //基类布局
    private LinearLayout parentLinearLayout;//把父类activity和子类activity的view都add到这里
    protected View baseView;
    private ViewGroup viewGroup;
    private ImageView baseErrorIV, baseLoadIV;
    private TextView baseErrorTitle, tvLoadGetcode;
    private TextView baseErrorSubTitle;
    private LinearLayout ltLoading, ltError, ltLogin;
    private EditText etloadPhonenumber, etLoadCode;
    protected Button btStartUse;
    private Handler handler;
    private int time = 60;
    //
    BasePresenter basePresenter = new BasePresenter();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SimpleFrameApplication.getInstance().addActivity(this);
        initContentAndBaseView(R.layout.activity_base);
        initProgressAndToast();
    }

    /**
     * 初始化contentview和基类
     */
    private void initContentAndBaseView(int layoutResID) {
        viewGroup = (ViewGroup) findViewById(android.R.id.content);
        viewGroup.removeAllViews();
        parentLinearLayout = new LinearLayout(this);
        parentLinearLayout.setOrientation(LinearLayout.VERTICAL);
        viewGroup.addView(parentLinearLayout);
        LayoutInflater.from(this).inflate(layoutResID, parentLinearLayout, true);

        baseView = viewGroup.findViewById(R.id.base_view);
        ltLoading = (LinearLayout) baseView.findViewById(R.id.lt_laoding);
        baseLoadIV = (ImageView) baseView.findViewById(R.id.tv_laoding_gif);
        ltError = (LinearLayout) baseView.findViewById(R.id.error_and_empty_layout);
        baseErrorIV = (ImageView) baseView.findViewById(R.id.error_image);
        baseErrorTitle = (TextView) baseView.findViewById(R.id.error_title);
        baseErrorSubTitle = (TextView) baseView.findViewById(R.id.error_subtitle);
        ltLogin = (LinearLayout) baseView.findViewById(R.id.base_login);

        tvLoadGetcode = (TextView) baseView.findViewById(R.id.tv_load_getcode);
        etloadPhonenumber = (EditText) baseView.findViewById(R.id.et_load_phonenumber);
        etLoadCode = (EditText) baseView.findViewById(R.id.et_load_code);
        btStartUse = (Button) baseView.findViewById(R.id.bt_start_use);

        Glide.with(this)
                .load(R.mipmap.ic_loading_mid)
                .asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(baseLoadIV);

        handler = new Handler();
        ltError.setOnClickListener(myOnClickListenr);
        btStartUse.setOnClickListener(myOnClickListenr);
        tvLoadGetcode.setOnClickListener(myOnClickListenr);

        etloadPhonenumber.addTextChangedListener(new BaseActivity.PhoneTextwatcher());
        etLoadCode.addTextChangedListener(new BaseActivity.CodeTextwatcher());
    }

    @Override
    public void setContentView(int layoutResID) {

        LayoutInflater.from(this).inflate(layoutResID, parentLinearLayout, true);

    }

    @Override
    public void setContentView(View view) {

        parentLinearLayout.addView(view);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {

        parentLinearLayout.addView(view, params);

    }

    //
    private void initProgressAndToast() {
        mProgressDialog = new LoadingDialog(this);
//        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(true);
        mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
    }

    /**
     * 得到指定id的view
     *
     * @param id  所要获取view的id
     * @param <T> 所要获取view的类型
     * @return
     */
    protected <T> T getViewById(int id) {
        return (T) findViewById(id);
    }

    //显示BaseView
    protected void showBaseView() {
        if (baseView != null) {
            baseView.setVisibility(View.VISIBLE);
        }
    }

    //隐藏
    protected void hideBaseView() {
        if (baseView != null) {
            baseView.setVisibility(View.GONE);
        }
    }

    /**
     * 显示内容视图
     */
    protected void showContent() {
        if (baseView != null) {
            baseView.setVisibility(View.GONE);
        }
    }

    /**
     *
     */
    protected void showLoading() {

        if (baseView == null) {
            return;
        }
        if (baseView.getVisibility() != View.VISIBLE) {
            baseView.setVisibility(View.VISIBLE);
        }
        if (ltError != null) {
            if (ltError.getVisibility() != View.INVISIBLE) {
                ltError.setVisibility(View.INVISIBLE);
            }
        }
        if (ltLogin != null) {
            if (ltLogin.getVisibility() != View.INVISIBLE) {
                ltLogin.setVisibility(View.INVISIBLE);
            }
        }
        if (ltLoading != null) {
            if (ltLoading.getVisibility() != View.VISIBLE) {
                ltLoading.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * 显示加载用户数据出错时的提示信息
     *
     * @param title    主标题
     * @param subTitle 子标题
     */
    protected void showErrorMessage(String title, String subTitle) {
        if (baseView == null) {
            return;
        }
        if (baseView.getVisibility() != View.VISIBLE) {
            baseView.setVisibility(View.VISIBLE);
        }
        if (ltError != null) {
            if (ltError.getVisibility() != View.VISIBLE) {
                ltError.setVisibility(View.VISIBLE);
                baseErrorIV.setImageResource(R.mipmap.error_view_cloud);
            }
        }
        if (ltLoading != null) {
            if (ltLoading.getVisibility() != View.INVISIBLE) {
                ltLoading.setVisibility(View.INVISIBLE);
            }
        }
        if (ltLogin != null) {
            if (ltLogin.getVisibility() != View.INVISIBLE) {
                ltLogin.setVisibility(View.INVISIBLE);
            }
        }
        if (title != null && !"".equals(title.trim())) {
            baseErrorTitle.setVisibility(View.VISIBLE);
            baseErrorTitle.setText(title);
        } else {
            baseErrorTitle.setVisibility(View.GONE);
        }

        if (subTitle != null && !"".equals(subTitle.trim())) {
            baseErrorSubTitle.setVisibility(View.VISIBLE);
            baseErrorSubTitle.setText(subTitle);
        } else {
            baseErrorSubTitle.setVisibility(View.GONE);
        }
    }

    //显示登录框
    protected void showLogin() {
        if (baseView == null) {
            return;
        }
        if (baseView.getVisibility() != View.VISIBLE) {
            baseView.setVisibility(View.VISIBLE);
        }
        if (ltError != null) {
            if (ltError.getVisibility() != View.INVISIBLE) {
                ltError.setVisibility(View.INVISIBLE);
            }
        }
        if (ltLoading != null) {
            if (ltLoading.getVisibility() != View.INVISIBLE) {
                ltLoading.setVisibility(View.INVISIBLE);
            }
        }
        if (ltLogin != null) {
            if (ltLogin.getVisibility() != View.VISIBLE) {
                ltLogin.setVisibility(View.VISIBLE);
            }
        }
    }

    private View.OnClickListener myOnClickListenr = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.bt_start_use:
                    if (checkPhoneNumber(2)) {
                        showProgressBar(true);
                        basePresenter.login(BaseActivity.this, BaseActivity.this, etloadPhonenumber.getText().toString().trim(), etLoadCode.getText().toString().trim());
                    }
                    break;

                case R.id.tv_load_getcode:
                    if (checkPhoneNumber(1)) {
                        time = 120;
                        showProgressBar(true);
                        basePresenter.sendCode(BaseActivity.this, BaseActivity.this, etloadPhonenumber.getText().toString().trim());
                    }
                    break;

                case R.id.error_and_empty_layout:
                    onRetryLoadData();
                    break;
            }
        }
    };

    boolean checkPhoneNumber(int type) {
        boolean b = true;
        switch (type) {
            case 1:
                if (etloadPhonenumber.getText().toString().trim().equals("")) {
                    showButtomToast("请输入电话号码");
                    b = false;
                }
                break;
            case 2:
                if (etloadPhonenumber.getText().toString().trim().equals("")) {
                    showButtomToast("请输入电话号码");
                    b = false;
                } else {
                    if (etLoadCode.getText().toString().trim().equals("")) {
                        showButtomToast("请输入验证码");
                        b = false;
                    }
                }
                break;
        }

        return b;
    }


    class PhoneTextwatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.toString().trim().length() == 11) {
                tvLoadGetcode.setClickable(true);
                tvLoadGetcode.setTextColor(ContextCompat.getColor(BaseActivity.this, R.color.main));
            } else {
                tvLoadGetcode.setClickable(false);
                tvLoadGetcode.setTextColor(ContextCompat.getColor(BaseActivity.this, R.color.gray));
            }
            if (etLoadCode.getText().toString().length() == 4 && etloadPhonenumber.getText().toString().trim().length() == 11) {
                btStartUse.setEnabled(true);
            } else {
                btStartUse.setEnabled(false);
            }


        }
    }

    class CodeTextwatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (etLoadCode.getText().toString().length() == 4 && etloadPhonenumber.getText().toString().trim().length() == 11) {
                btStartUse.setEnabled(true);
            } else {
                btStartUse.setEnabled(false);
            }
        }
    }

    /**
     * 显示用户数据为空时的提示信息
     *
     * @param title    提示标题
     * @param subTitle 提示子标题
     */

    protected void showBlankMessage(String title, String subTitle) {

//        if (mErrorView == null) {
//            return;
//        }
//
//        mErrorView.setImageResource(R.mipmap.ic_exception_blank_task);
//        mErrorView.showEmptyView();
//
//        if (mErrorView.getVisibility() != View.VISIBLE) {
//            mErrorView.setVisibility(View.VISIBLE);
//        }
//
//        if (title != null && !"".equals(title.trim())) {
//            mErrorView.showTitle(true);
//            mErrorView.setTitle(title);
//        } else {
//            mErrorView.showTitle(false);
//        }
//
//        if (subTitle != null && !"".equals(subTitle.trim())) {
//            mErrorView.showSubtitle(true);
//            mErrorView.setSubtitle(subTitle);
//        } else {
//            mErrorView.showTitle(false);
//        }
//
//        if (getContentView() != null) {
//            getContentView().setVisibility(View.GONE);
//        }


    }


    /**
     * 显示用户数据为空时的提示信息
     *
     * @param title    提示标题
     * @param subTitle 提示子标题
     */
    protected void showBlankMessage(String title, String subTitle, int resId) {

//        if (mErrorView == null) {
//            return;
//        }
//
//        mErrorView.setImageResource(resId);
//        mErrorView.showEmptyView();
//
//        if (mErrorView.getVisibility() != View.VISIBLE) {
//            mErrorView.setVisibility(View.VISIBLE);
//        }
//
//        if (title != null && !"".equals(title.trim())) {
//            mErrorView.showTitle(true);
//            mErrorView.setTitle(title);
//        } else {
//            mErrorView.showTitle(false);
//        }
//
//        if (subTitle != null && !"".equals(subTitle.trim())) {
//            mErrorView.showSubtitle(true);
//            mErrorView.setSubtitle(subTitle);
//        } else {
//            mErrorView.showTitle(false);
//        }
//
//        if (getContentView() != null) {
//            getContentView().setVisibility(View.GONE);
//        }


    }


    /**
     * 重新加载数据，当点击重试按钮时会调用此方法
     */
    protected void onRetryLoadData() {

    }

    /**
     * 如果show为true则显示进度条对话框，否则关闭对话框
     *
     * @param show 为true则显示对话框，否则关闭对话框
     */
    protected void showProgressBar(boolean show) {
        showProgressBar(show, "");
    }


    /**
     * 如果show为true则显示进度条对话框，否则关闭对话框.
     *
     * @param show    为true则显示对话框，否则关闭对话框
     * @param message 对话所要显示的提示消息
     */
    protected void showProgressBar(boolean show, String message) {
        if (show) {
            mProgressDialog.setMessage(message);
            mProgressDialog.show();
        } else {
            mProgressDialog.dismiss();
        }
    }


    /**
     * 显示带消息的进度条对话框
     *
     * @param messageId 所要显示的文字的资源id
     */
    protected void showProgressBar(int messageId) {
        String message = getString(messageId);
        showProgressBar(true, message);
    }

    /**
     * 显示吐司
     *
     * @param msg 吐司中所要显示的提示信息
     */
    protected void showButtomToast(String msg) {
        mToast.setText(msg);
        mToast.setGravity(Gravity.NO_GRAVITY, 0, 0);
        mToast.show();
    }

    /**
     * 显示吐司
     *
     * @param messageId 吐司中所要显示的提示信息的id
     */
    protected void showButtomToast(int messageId) {
        mToast.setText(messageId);
        mToast.setGravity(Gravity.NO_GRAVITY, 0, 0);
        mToast.show();
    }

    /**
     * 居中显示吐司
     *
     * @param msg 吐司中所要显示的提示信息
     */

    protected void showMiddleToast(String msg) {
        mToast.setText(msg);
        mToast.setGravity(Gravity.CENTER, 0, 0);
        mToast.show();
    }


    /**
     * 显示提示对话框
     *
     * @param title   对话框的title
     * @param msg     对话框要显示的提示信息
     * @param clickOk 确定按钮的点击监听器
     */
    protected void showDialog(String title, String msg, DialogInterface.OnClickListener clickOk) {
        showDialog(title, msg, clickOk, null, null);
    }


    /**
     * 显示提示对话框
     *
     * @param title       对话框的title
     * @param msg         对话框要显示的提示信息
     * @param clickOk
     * @param clickCancel
     * @param onListener
     */
    protected void showDialog(String title, String msg, DialogInterface.OnClickListener clickOk, DialogInterface.OnClickListener clickCancel, DialogInterface.OnCancelListener onListener) {
        MessageDialog dialog = new MessageDialog(this);
        dialog.setTitle(title);
        dialog.setBtnYes("确定");
        dialog.setBtnNo("取消");
        dialog.setMessage(msg);
        dialog.setOkListener(clickOk);
        dialog.setCancelListener(clickCancel);
        dialog.setOnCancelListener(onListener);
        dialog.show();

    }

    /**
     * 显示提示对话框
     *
     * @param title
     * @param msg
     * @param ok
     * @param cancel
     * @param clickOk
     * @param clickCancel
     * @param onListener
     */
    protected void showDialog(String title, String msg, String ok, String cancel, DialogInterface.OnClickListener clickOk, DialogInterface.OnClickListener clickCancel, DialogInterface.OnCancelListener onListener) {
        MessageDialog dialog = new MessageDialog(this);
        dialog.setTitle(title);
        dialog.setBtnYes(ok);
        dialog.setBtnNo(cancel);
        dialog.setMessage(msg);
        dialog.setOkListener(clickOk);
        dialog.setCancelListener(clickCancel);
        dialog.setOnCancelListener(onListener);
        dialog.show();
    }

    /**
     * 显示提示对话框
     *
     * @param title
     * @param msg
     * @param ok
     * @param cancel
     * @param canCancel
     * @param clickOk
     * @param clickCancel
     * @param onListener
     */
    protected void showDialog(String title, String msg, String ok, String cancel, boolean canCancel, DialogInterface.OnClickListener clickOk, DialogInterface.OnClickListener clickCancel, DialogInterface.OnCancelListener onListener) {
        MessageDialog dialog = new MessageDialog(this);
        dialog.setTitle(title);
        if (title.equals("")) {
            dialog.hideTitle();
        } else {
            dialog.showTitle();
        }
        dialog.setBtnYes(ok);
        dialog.setBtnNo(cancel);
        dialog.setMessage(msg);
        dialog.setCancelable(canCancel);
        dialog.setOkListener(clickOk);
        dialog.setCancelListener(clickCancel);
        dialog.setOnCancelListener(onListener);
        dialog.show();
    }


    /**
     * 显示提示对话框
     *
     * @param title
     * @param msg
     */
    protected void showOkDialog(String title, String msg) {
        showOkDialog(title, msg, null);
    }


    /***
     * 显示提示对话框
     *
     * @param title
     * @param msg
     * @param clickOk
     */
    protected void showOkDialog(String title, String msg, DialogInterface.OnClickListener clickOk) {
        showOkDialog(title, msg, false, clickOk);
    }

    /***
     * 显示提示对话框
     *
     * @param title
     * @param msg
     * @param clickOk
     */
    protected void showOkDialog(String title, String msg, boolean cancel, DialogInterface.OnClickListener clickOk) {

        MessageDialog dialog = new MessageDialog(this);
        dialog.setTitle(title);
        dialog.setBtnYes("确定");
        dialog.setMessage(msg);
        dialog.setCancelable(cancel);
        dialog.setOkListener(clickOk);
        dialog.show();
    }


    public void setPullToRefreshCompleteListener(BaseActivity.PullToRefreshCompleteListener pullToRefreshCompleteListener) {
        this.pullToRefreshCompleteListener = pullToRefreshCompleteListener;
    }

    public interface PullToRefreshCompleteListener {
        void pullToRefreshComplete();
    }

    /**
     * 停止刷新
     */
    public class GetDataTask extends AsyncTask<Void, Void, String> {

        //后台处理部分
        @Override
        protected String doInBackground(Void... params) {
            // Simulates a background job.
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
            }
            String str = "Added after refresh...I add";
            return str;
        }

        //这里是对刷新的响应，可以利用addFirst（）和addLast()函数将新加的内容加到LISTView中
        //根据AsyncTask的原理，onPostExecute里的result的值就是doInBackground()的返回值
        @Override
        protected void onPostExecute(String result) {
            //在头部增加新添内容
//            mListItems.addFirst(result);

            //通知程序数据集已经改变，如果不做通知，那么将不会刷新mListItems的集合
//            adapter.notifyDataSetChanged();
            // Call onRefreshComplete when the list has been refreshed.
            if (pullToRefreshCompleteListener != null) {
                pullToRefreshCompleteListener.pullToRefreshComplete();
            }
            super.onPostExecute(result);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        SimpleFrameApplication.getInstance().finishActivity(this);

    }


    @Override
    public void contentFail() {
        showErrorMessage(getResources().getString(R.string.net_error), getResources().getString(R.string.relaod));
        showProgressBar(false);
    }

    @Override
    public void dataFail() {
        showProgressBar(false);
    }

    @Override
    public void showFail(String message) {
        showProgressBar(false);
        showButtomToast(message);
    }

    @Override
    public void timeOutFail() {
        showButtomToast(getString(R.string.time_out));
        showProgressBar(false);
    }

    @Override
    public void showNetError(String msg) {
        showButtomToast(getString(R.string.time_out));
        showProgressBar(false);
    }

    @Override
    public void tokenOld() {
        showProgressBar(false);
        showLogin();
    }

    @Override
    public void loginOk() {
        showProgressBar(false);
    }

    @Override
    public void loginFail(String msg) {
        showProgressBar(false);
        showButtomToast(msg);
    }

    @Override
    public void showCode() {
        showProgressBar(false);
        showButtomToast("获取验证码请求已发送");
        handler.postDelayed(runnable, 1000);
        tvLoadGetcode.setClickable(false);
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            time--;
            tvLoadGetcode.setText("" + time + "s");
            if (time == 0) {
                handler.removeCallbacks(this);
                tvLoadGetcode.setClickable(true);
                tvLoadGetcode.setText("重新获取");
            } else {
                handler.postDelayed(this, 1000);
            }
        }
    };

    /**
     * 使用外部应用导航
     *
     * @param startLat
     * @param startLng
     * @param startName
     * @param endLat
     * @param endLng
     * @param endName
     * @param navMode
     */
    protected void navigationLocationApp(double startLat, double startLng, String startName, double endLat, double endLng, String endName, int navMode) {
        if (isInstalled(this, "com.baidu.BaiduMap")) {
            try {
                String mode = "driving";
                switch (navMode) {
                    case 1:
                        mode = "transit";
                        break;
                    case 2:
                        mode = "driving";
                        break;
                    case 3:
                        mode = "walking";
                        break;
                }
                Intent intent7 = Intent.getIntent("intent://map/direction?origin=latlng:" + startLat + "," + startLng + "| name:" + startName + "&destination=latlng:" + endLat + "," + endLng + "|name:" + endName + "&mode=" + mode + "&src=yourCompanyName|letravel#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
                startActivity(intent7);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        } else {
            showButtomToast("未安装应用");
        }
    }

    /**
     * 检查手机上是否安装了指定的软件
     *
     * @param context
     * @param packageName：应用包名
     * @return
     */
    public static boolean isInstalled(Context context, String packageName) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        if (packageInfos != null) {
            for (int i = 0; i < packageInfos.size(); i++) {
                String pkName = packageInfos.get(i).packageName;
                if (pkName.equals(packageName))
                    return true;
            }
        }
        return false;
    }
}