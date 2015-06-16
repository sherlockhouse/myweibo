package com.lanadelray.weibo.ui.entry;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.lanadelray.weibo.MainActivity;
import com.lanadelray.weibo.cache.LoginApiCache;
import com.lanadelray.weibo.login.LoginActivity;
import com.lanadelray.weibo.support.Utility;

/**
 * Created by Administrator on 2015/6/15 0015.
 */
public class EntryActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //login
        LoginApiCache login = new LoginApiCache(this);
        if (needsLogin(login)) {
            login.resetAccessToken();
            Intent i = new Intent();
            Log.d("www","entry ");
            i.setAction(Intent.ACTION_MAIN);
            i.setClass(this, LoginActivity.class);
            startActivity(i);
            finish();
        } else {
            Intent i = new Intent();
            i.setAction(Intent.ACTION_MAIN);
            i.setClass(this, MainActivity.class);
            i.putExtra(Intent.EXTRA_INTENT,
                    getIntent().getIntExtra(Intent.EXTRA_INTENT, 0));
            startActivity(i);
            finish();
        }
    }

    private boolean needsLogin(LoginApiCache login) {
        return login.getmAccessToken() == null ||
                Utility.isTokenExpired(login.getmExpireDate());
    }
}
