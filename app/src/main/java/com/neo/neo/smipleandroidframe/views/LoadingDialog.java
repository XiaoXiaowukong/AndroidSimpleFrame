package com.neo.neo.smipleandroidframe.views;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.neo.neo.smipleandroidframe.R;

/**
 * Created by neo on 2016/12/1.
 */

public class LoadingDialog extends Dialog {
    private LayoutInflater inflater;
    private Context context;
    private View baseView;
    private ImageView ivLoading;

    public LoadingDialog(Context context) {
        super(context, R.style.Theme_loading_dialog);
        setCancelable(true);
        setCanceledOnTouchOutside(true);
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        initView();
    }

    private void initView() {
        baseView = inflater.inflate(R.layout.view_loading_dialog, null);
        ivLoading = (ImageView) baseView.findViewById(R.id.iv_loading);
        Glide.with(context).load(R.mipmap.ic_loading_mid).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(ivLoading);
        setContentView(baseView);
    }

    public void setMessage(String msg) {

    }
}
