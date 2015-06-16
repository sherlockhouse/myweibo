package com.lanadelray.weibo.weiboapi;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.lanadelray.weibo.weiboapi.user.Constants;

/**
 * Created by Administrator on 2015/6/15 0015.
 */
public class PrivateKey extends BaseApi {

    private static final String TAG = PrivateKey.class.getSimpleName();

    private static final String
            PREF = "app_key",
            PREF_ID = "id",
            PREF_SECRET = "secret",
            PREF_REDIRECT = "redirect",
            PREF_SCOPE = "scope",
            PREF_PKG = "package";

    private static String sAppId, sAppSecret,
            sRedirectUri, sScope, sPackageName;

    public static void setPrivateKey(String appId, String appSecret,
                                     String redirectUri, String scope,
                                     String packageName) {
        sAppId = appId;
        sAppSecret = appSecret;
        sRedirectUri = redirectUri;
        sScope = scope;
        sPackageName = packageName;
    }

    public static boolean readFromPref(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF, Context.MODE_APPEND);
        String id = prefs.getString("PREF_ID", null);
        String secret = prefs.getString("PREF_SECRET", null);
        String redirect = prefs.getString("PREF_REDIRECT", null);
        String scope = prefs.getString("PREF_SCOPE", null);
        String packageName = prefs.getString("PREF_PKG", null);

        if (!TextUtils.isEmpty(id) && !TextUtils.isEmpty(secret) &&
                !TextUtils.isEmpty(redirect) && !TextUtils.isEmpty(packageName)
                && !TextUtils.isEmpty(scope)) {
            setPrivateKey(id, secret, redirect, scope, packageName);
            return true;
        } else {
            return false;
        }

    }

    public static void writeToPref(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        prefs.edit()
                .putString(PREF_ID, sAppId)
                .putString(PREF_SECRET, sAppSecret)
                .putString(PREF_REDIRECT, sRedirectUri)
                .putString(PREF_SCOPE, sScope)
                .putString(PREF_PKG, sPackageName)
                .commit();
    }

    public static String[] getAll() {
        return new String[]{
                sAppId,
                sAppSecret,
                sRedirectUri,
                sScope,
                sPackageName,
        };
    }

    public static String getOauthLoginPage() {
        return Constants.OAUTH2_ACCESS_AUTHORIZE + "?" + "client_id=" + sAppId
                + "&response_type=token&redirect_uri=" + sRedirectUri
                + "&key_hash=" + sAppSecret + (TextUtils.isEmpty(sPackageName) ? "" : "&packagename=" + sPackageName)
                + "&display=mobile" + "&scope=" + sScope;
    }

    public static boolean isUrlRedirected(String url) {
        return url.startsWith(sRedirectUri);
    }

}
