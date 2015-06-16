package com.lanadelray.weibo.ui.common;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.widget.SlidingPaneLayout;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.lanadelray.weibo.R;
import com.lanadelray.weibo.support.Settings;
import com.lanadelray.weibo.support.Utility;

/**
 * Created by Administrator on 2015/6/14 0014.
 */
public abstract class AbsActivity extends ToolbarActivity{

    private Settings mSettings;
    private int mLang = -1;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        //set language
        mLang = Utility.getCurrentLanguage(this);
        if (mLang > -1) {
            Utility.changeLanguage(this, mLang);
        }

        Utility.initDarkMode(this);
        super.onCreate(savedInstanceState);
        swipeInit();

        //Common ActionBar settings
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        mSettings = Settings.getInstance(this);

    }

    @Override
    public void onPostCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
    }

    @Override
    protected void onResume() {
        super.onResume();

        int lang = Utility.getCurrentLanguage(this);
        if (lang != mLang) {
            recreate();
        }


    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void swipeInit(){
        //Replace the view
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        SlidingPaneLayout v = (SlidingPaneLayout) inflater.inflate(R.layout.swipe_decor_wrapper, null);
        final ViewGroup frame = (ViewGroup) v.findViewById(R.id.swipe_container);
        View swipeView = getSwipeView();
        ViewGroup decor = (ViewGroup) swipeView.getParent();
        ViewGroup.LayoutParams params = swipeView.getLayoutParams();
        decor.removeView(swipeView);
        frame.addView(v, params);
        decor.addView(v, params);
        decor.setBackgroundColor(0);

        //Elevation
        if(Build.VERSION.SDK_INT >= 21){
            frame.setElevation(11.8f);
        } else {
            v.setShadowResourceRight(R.drawable.panel_shadow);
        }

        //Swipe gesture configurations
        v.setSliderFadeColor(0);
        v.setPanelSlideListener(new SlidingPaneLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {

                getWindow().getDecorView().setAlpha(1.0f - slideOffset);
            }

            @Override
            public void onPanelOpened(View panel) {
                finish();
            }

            @Override
            public void onPanelClosed(View panel) {

            }
        });

        //Ajust window color
        getWindow().setBackgroundDrawable(new ColorDrawable(0));

    }

    private View getSwipeView() {
        return ((ViewGroup)getWindow().getDecorView()).getChildAt(0);
    }
}
