package com.xwj.xiamediaplayer.views.impl;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.xwj.xiamediaplayer.R;


/**
 * Created by xwjsd on 2016-04-29.
 */
public class SettingsActivity extends AppCompatActivity {
    private FragmentManager mFragmentManager;
    private Toolbar mToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mToolBar = (Toolbar) this.findViewById(R.id.toolbar);
        setSupportActionBar(mToolBar);
        mToolBar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingsActivity.this.finish();
            }
        });

        mFragmentManager = this.getFragmentManager();

        mFragmentManager.beginTransaction()
                .add(R.id.ll_container, new SettingsFragment(), SettingsFragment.class.getSimpleName())
                .commit();

    }
}
