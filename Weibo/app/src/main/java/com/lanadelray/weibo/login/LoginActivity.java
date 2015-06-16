package com.lanadelray.weibo.login;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;

import com.lanadelray.weibo.MainActivity;
import com.lanadelray.weibo.R;
import com.lanadelray.weibo.cache.LoginApiCache;
import com.lanadelray.weibo.support.Utility;
import com.lanadelray.weibo.weiboapi.PrivateKey;

/**
 * Created by Administrator on 2015/6/13 0013.
 */
public class LoginActivity extends Activity {

    private static final String TAG = LoginActivity.class.getSimpleName();

    private WebView mWeb;

    private LoginApiCache mLogin;

    @Override
    public void onCreate(Bundle savedInstanceState) {

       setContentView(R.layout.web_login);
        super.onCreate(savedInstanceState);

        //initialize views
        mWeb = Utility.findViewById(this, R.id.login_web);

        //Create login instance
        mLogin = new LoginApiCache(this);

        //Login page
        WebSettings settings = mWeb.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setSaveFormData(false);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);

        mWeb.setWebViewClient(new MyWebViewClient());

        if (PrivateKey.readFromPref(this)) {
            mWeb.loadUrl(PrivateKey.getOauthLoginPage());
        } else {
            mWeb.loadUrl("about:blank");
            showAppKeyDialog();
        }

    }

    private void showAppKeyDialog() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.app_key, null);
        final EditText tvId = Utility.findViewById(v, R.id.app_id);
        final EditText tvSecret = Utility.findViewById(v, R.id.app_secret);
        final EditText tvRedirect = Utility.findViewById(v,R.id.redirect_uri);
        final EditText tvScope = Utility.findViewById(v,R.id.scope);
        final EditText tvPkg = Utility.findViewById(v,R.id.app_pkg);

        final EditText[] editTexts= {tvId, tvSecret, tvRedirect, tvScope, tvPkg};



        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.custom)
                .setView(v)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNeutralButton(R.string.app_copy, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create();

            dialog.show();

            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String id = editTexts[0].getText().toString().trim();
                    String secret = editTexts[1].getText().toString().trim();
                    String uri = editTexts[2].getText().toString().trim();
                    String scope = editTexts[3].getText().toString().trim();
                    String pkg = editTexts[4].getText().toString().trim();

                    if (!TextUtils.isEmpty(id) && !TextUtils.isEmpty(secret) &&
                            !TextUtils.isEmpty(uri) && !TextUtils.isEmpty(scope)) {
                        PrivateKey.setPrivateKey(id, secret, uri,  scope,pkg);
                        PrivateKey.writeToPref(LoginActivity.this);

                        dialog.dismiss();
                        mWeb.loadUrl(PrivateKey.getOauthLoginPage());
                    }

                }
            });


        dialog.getButton(DialogInterface.BUTTON_NEUTRAL).setTag(true);
        dialog.getButton(DialogInterface.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isEmpty = (boolean) v.getTag();

                if (!isEmpty) {
                    Utility.copyToClipboard(LoginActivity.this, encodeLoginData(
                            tvId.getText().toString(), tvSecret.getText().toString(),
                            tvRedirect.getText().toString(), tvScope.getText().toString(),
                            tvPkg.getText().toString()));
                } else {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse("http://www.baidu.com"));
                    startActivity(i);
                }
            }
        });

        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String str = s.toString().trim();
                if (isLoginData(str)){
                    String[] data = decodeLoginData(s.toString());

                    if(data == null || data.length < 5) return;

                    int tmp = 0;
                    for (EditText i: editTexts){
                        i.setText(data[tmp++].trim());
                    }
//                    editTexts[0].setText(data[0].trim());
//                    editTexts[0].setText(data[1].trim());
//                    editTexts[0].setText(data[2].trim());
//                    editTexts[0].setText(data[3].trim());
//                    editTexts[0].setText(data[4].trim());
                } else {
                    if (!str.equals("")) {
                        dialog.getButton(DialogInterface.BUTTON_NEUTRAL)
                                .setText(R.string.app_copy);
                    } else {
                        dialog.getButton(DialogInterface.BUTTON_NEUTRAL)
                                .setText(R.string.app_hint);
                    }

                    dialog.getButton(DialogInterface.BUTTON_NEUTRAL)
                            .setTag(str.equals(""));
                }

            }
        };

        for (EditText i: editTexts) {
            i.addTextChangedListener(watcher);
        }
//        tvId.addTextChangedListener(watcher);
//        tvSecret.addTextChangedListener(watcher);
//        tvRedirect.addTextChangedListener(watcher);
//        tvPkg.addTextChangedListener(watcher);
//        tvScope.addTextChangedListener(watcher);

        //initialize data
        String[] val = PrivateKey.getAll();

        int tmp = 0;
        for (EditText i: editTexts) {
            i.setText(val[tmp++]);
        }
//        tvId.setText(val[0]);
//        tvSecret.setText(val[1]);
//        tvRedirect.setText(val[2]);
//        tvPkg.setText(val[3]);
//        tvScope.setText(val[4]);

    }

    private static final String SEPERATOR = "::";
    private static final String START = "SS";
    private static final String END = "EE";
    private String encodeLoginData(String id , String secret,
                                   String uri, String scope, String pkg) {
        return START + Base64.encodeToString(
                (id + SEPERATOR + secret + SEPERATOR + uri + SEPERATOR
                + scope + SEPERATOR + pkg +  SEPERATOR   + END).getBytes(),
                Base64.URL_SAFE | Base64.NO_WRAP |
                        Base64.NO_WRAP | Base64.NO_PADDING | Base64.NO_CLOSE)
                + END;
    }

    private String[] decodeLoginData(String str) {
        if (!isLoginData(str))
            return  null;

        String data = str.substring(START.length(),
                str.length() - END.length() - 1);

        //todo debug
        return new String(Base64.decode(data, Base64.URL_SAFE | Base64.NO_WRAP
              | Base64.NO_PADDING | Base64.NO_CLOSE)).split(SEPERATOR);
    }

    private boolean isLoginData(String str) {
        return str.startsWith(START) && str.length() > START.length()
                + END.length() && str.endsWith(END);
    }



    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (PrivateKey.isUrlRedirected(url)) {
                view.stopLoading();
                handleResponse(url);
            } else {
                view.loadUrl(url);
            }

            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            if (!url.equals("about:blank") && PrivateKey.isUrlRedirected(url)) {
                view.stopLoading();
                handleResponse(url);
                return;
            }
            super.onPageStarted(view, url, favicon);
        }
    }

    private void handleResponse(String url) {
        if (!url.contains("error")) {
            int tokenIndex = url.indexOf("access_token");
            int expiresIndex = url.indexOf("expires_in");
            String token = url.substring(tokenIndex
                    + 13, url.indexOf("&", tokenIndex));
            String expiresIn = url.substring(expiresIndex + 11, url.indexOf("&", expiresIndex));
            //todo debug
            //get openUid
            new LoginTask().execute(token, expiresIn);
        }
    }

    private class LoginTask extends AsyncTask<String, Void, Long> {

        private ProgressDialog progDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progDialog = new ProgressDialog(LoginActivity.this);
            progDialog.setMessage("please wait.....");
            progDialog.setCancelable(false);
            progDialog.show();
        }

        @Override
        protected Long doInBackground(String... params) {
            //todo debug

            //
            mLogin.requestOpenUid(params[0], params[1]);
            return mLogin.getmExpireDate();
        }

        @Override
        protected void onPostExecute(Long aLong) {
            super.onPostExecute(aLong);
            progDialog.dismiss();


            //todo debug
            if (mLogin.getmAccessToken() != null) {
                mLogin.cache();
            } else {
                showLoginFail();
            }

            //Expire Date
            String msg = String.format("be invalid in %d days",
                    Utility.expireTimeInDays(aLong));
            new AlertDialog.Builder(LoginActivity.this)
                    .setMessage(msg)
                    .setCancelable(false)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Intent i = new Intent();
                            i.setAction(Intent.ACTION_MAIN);
                            i.setClass(LoginActivity.this, MainActivity.class);
                            startActivity(i);
                            finish();
                        }
                    }).create().show();
        }
    }

    private void showLoginFail() {
        new AlertDialog.Builder(LoginActivity.this)
                .setMessage("Login failed")
                .setCancelable(false)
                .create().show();

    }
}
