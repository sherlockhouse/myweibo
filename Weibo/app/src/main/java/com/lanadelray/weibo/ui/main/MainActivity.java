package com.lanadelray.weibo.ui.main;

import android.os.Bundle;

import com.lanadelray.weibo.R;
import com.lanadelray.weibo.support.Utility;
import com.lanadelray.weibo.ui.common.ToolbarActivity;

/**
 * Created by Administrator on 2015/6/16 0016.
 */
public class MainActivity extends ToolbarActivity {

    private int mLang = -1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setmLayout(R.layout.main);
        mLang = Utility.getCurrentLanguage(this);
        if (mLang > -1 ){
            Utility.changeLanguage(this, mLang);
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setmLayout(int layout) {
        mLayout = layout;
    }


}
