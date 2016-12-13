package com.neo.neo.smipleandroidframe.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Set;


/**
 * 检测手机网络状态的帮助类
 * Created by kevin on 2015/4/20.
 */
public class NetworkHelper {


    /**
     * 得到手机是否连接到Wi-Fi网络
     *
     * @return
     */
    public static boolean isWifiConnect(Context context) {

        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return networkInfo.isConnected();

    }

    /**
     * 得到手机是否连接到移动网络
     *
     * @return
     */
    public static boolean isMobileConnect(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        return networkInfo.isConnected();

    }

    /**
     * 判断手机是否连接到网络
     *
     * @return
     */
    public static boolean isNetworkConnect(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    /**
     * 格式化请求参数
     *
     * @param param
     * @return
     */
    public static HashMap<String, String> formatRequestParams(HashMap<String, String> param) {

        JSONObject jsonObject = new JSONObject();
        HashMap<String, String> forms = new HashMap<String, String>();
        if (param != null && param.size() != 0) {
            try {
                Set<String> set = param.keySet();
                for (String key : set) {
                    jsonObject.put(key, param.get(key));
//                    forms.put(key, param.get(key));
                }
                //设置要post的参数
//                HashMap<String, String> forms = new HashMap<String, String>();
//                forms.put("sign", DataEncrypt.getEncryptStr(jsonObject.toString()));
                return forms;
            } catch (JSONException e) {

                return null;
            }

        }
        return null;
    }

    /**
     * 格式化请求参数
     *
     * @param param
     * @return
     */
    public static HashMap<String, String> formatRequestParams2(String param) {

        StringBuffer stringBuffer = new StringBuffer();
        HashMap<String, String> forms = new HashMap<String, String>();
        //设置要post的参数
//        forms.put("data", DataEncrypt.getEncryptStr(param));
        return forms;
    }
    /**
     * 格式化url
     *
     * @param api
     * @param param
     * @return
     */
    public static String formatUrl(String api, HashMap<String, String> param) {

        StringBuffer sb = new StringBuffer();
        sb.append(api);

        if (param != null) {
            Set<String> set = param.keySet();
            sb.append("?");
            for (String key : set) {
                sb
                        .append(key)
                        .append("=")
                        .append(param.get(key))
                        .append("&");
            }

            sb.deleteCharAt(sb.length() - 1);

        }
        return sb.toString();

    }


    public static String formatResponse(String data) {
        return "";
    }


}
