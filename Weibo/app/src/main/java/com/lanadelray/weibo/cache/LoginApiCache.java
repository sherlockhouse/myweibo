package com.lanadelray.weibo.cache;

import android.content.Context;
import android.content.SharedPreferences;

import com.lanadelray.weibo.weiboapi.BaseApi;
import com.lanadelray.weibo.weiboapi.user.AccountApi;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/6/13 0013.
 */
public class LoginApiCache {

    private static final String TAG = LoginApiCache.class.getSimpleName();

    private Context mContext;
    private SharedPreferences mPrefs;
    private String mAccessToken;
    private String mUid;
    private long mExpireDate;

    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mTokens = new ArrayList<>();
    private ArrayList<String> mExpireDates = new ArrayList<>();

    public LoginApiCache(Context context) {
        mContext = context;
        mPrefs = context.getSharedPreferences("access_token", Context.MODE_PRIVATE);
        mAccessToken = mPrefs.getString("access_token", null);
        mUid = mPrefs.getString("uid", "");
        mExpireDate = mPrefs.getLong("expires_in", Long.MIN_VALUE);

        if (mAccessToken != null) {
            BaseApi.setmAccessToken(mAccessToken);
        }
    }

    public void requestOpenUid(String token, String expire){
        mAccessToken = token;
        BaseApi.setmAccessToken(mAccessToken);
        mExpireDate = System.currentTimeMillis() + Long.valueOf(expire) * 1000;
        mUid = AccountApi.getUid();
    }

    public  void resetAccessToken() {
        mAccessToken = null;
        mExpireDate = Long.MIN_VALUE;
        mPrefs.edit().remove("access_token").remove("expries_in").remove("uid").commit();
    }

    public  void cache() {
        mPrefs.edit().putString("access_token", mAccessToken)
                .putLong("expires_in", mExpireDate)
                .putString("uid", mUid)
                .commit();
    }

    public String getmAccessToken() {
        return mAccessToken;
    }

    public String getmUid() {
        return mUid;
    }

    public long getmExpireDate() {
        return mExpireDate;
    }

    public ArrayList<String> getmNames() {
        return mNames;
    }
}
