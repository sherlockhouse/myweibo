package com.lanadelray.weibo.weiboapi;

import com.lanadelray.weibo.support.http.HttpUtility;
import com.lanadelray.weibo.support.http.WeiboParameters;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by Administrator on 2015/6/13 0013.
 *
 */
public abstract class BaseApi {
    private static final String TAG = BaseApi.class.getSimpleName();

    //HTTP  methods
    protected static final String HTTP_GET = HttpUtility.GET;
    protected static final String HTTP_POST = HttpUtility.POST;

    //Access Token
    private static String mAccessToken;

    protected static JSONObject request(String url, WeiboParameters params , String method) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, IOException {
        return  request(mAccessToken, url, params, method, JSONObject.class);
    }

    protected  static JSONArray requestArray(String url, WeiboParameters params, String method) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, IOException {
        return request(mAccessToken, url, params, method, JSONArray.class);
    }
    protected static  <T> T request(String token, String url,
                                  WeiboParameters params, String method,Class<T> jsonClass) throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        if (token == null) {
            return null;
        } else {
            params.put("access_token", token );
            String jsonData = HttpUtility.doRequest(url, params, method);

            //todo debug

            if (jsonData != null && (jsonData.contains("{") || jsonData.contains("["))) {
                return jsonClass.getConstructor(String.class).newInstance(jsonData);
            } else {
                return null;
            }
        }
    }

//    protected static JSONObject requestWithoutAccessToken (String url, WeiboParameters params,
//                                                           String method) throws IOException, JSONException {
//        String jsonData = HttpUtility.doRequest(url,params, method);
//        //todo debug
//        if (jsonData != null && jsonData.contains("{")) {
//            return  new JSONObject(jsonData);
//        }else {
//            return null;
//        }
//    }
    public static String getmAccessToken() {
        return mAccessToken;
    }

    public static void setmAccessToken(String mAccessToken) {
        BaseApi.mAccessToken = mAccessToken;
    }
}
