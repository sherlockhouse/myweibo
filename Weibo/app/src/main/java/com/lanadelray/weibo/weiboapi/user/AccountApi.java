package com.lanadelray.weibo.weiboapi.user;

import com.lanadelray.weibo.support.http.WeiboParameters;
import com.lanadelray.weibo.weiboapi.BaseApi;

import org.json.JSONObject;

/**
 * Created by Administrator on 2015/6/14 0014.
 */
public class AccountApi extends BaseApi {
    public static String getUid() {
        JSONObject json = null;

        try {
            json = request(Constants.GET_UID, new WeiboParameters(), HTTP_GET);
        } catch (Exception e) {
            return null;
        }

        return json.optString("uid");
    }
}
