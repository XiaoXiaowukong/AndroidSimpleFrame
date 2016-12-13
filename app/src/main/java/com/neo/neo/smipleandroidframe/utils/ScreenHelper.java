package com.neo.neo.smipleandroidframe.utils;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;

/**
 * Created by neo on 15/5/2.
 */
public class ScreenHelper {

    /**
     * 得到屏幕高度
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Activity context) {

        DisplayMetrics displayMetrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    /**
     * 得到屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Activity context) {

        DisplayMetrics displayMetrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }


    /**
     * 将dp换算成px
     *
     * @param context
     * @param dp
     * @return
     */
    public static float dpToPx(Context context, float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    /**
     * 将sp换算成px
     *
     * @param context
     * @param sp
     * @return
     */
    public static float spToPx(Context context, float sp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());

    }


}
