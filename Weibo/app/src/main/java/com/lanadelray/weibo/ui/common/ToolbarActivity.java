package com.lanadelray.weibo.ui.common;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;

import com.lanadelray.weibo.R;
import com.lanadelray.weibo.support.Utility;

/**
 * Created by Administrator on 2015/6/14 0014.
 */
public abstract class ToolbarActivity extends AppCompatActivity {

    public Toolbar mToolbar;
    public int mLayout = 0;

    @Override
    public  void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= 21) {
            requestWindowFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
            getWindow().setAllowEnterTransitionOverlap(true);
            getWindow().setAllowReturnTransitionOverlap(true);
        }
        super.onCreate(savedInstanceState);

        //子类要给mLayout赋初值,因而改成了抽象方法
        setContentView(mLayout);
        mToolbar = Utility.findViewById(this, R.id.toolbar);

        if (mToolbar != null) {
            mToolbar.bringToFront();
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            if (Build.VERSION.SDK_INT >= 21) {
                mToolbar.setElevation(getToorbarElevation());

                View shadow = Utility.findViewById(this, R.id.action_shadow);

                if (shadow != null) {
                    shadow.setVisibility(View.GONE);
                }
            }
        }


    }

    public abstract void setmLayout(int layout);

    public Toolbar getmToolbar() {
        return mToolbar;
    }

    public float getToorbarElevation() {
        if (Build.VERSION.SDK_INT >= 21) {
            return 12.8f;
        } else {
            return -1;
        }
    }
}
