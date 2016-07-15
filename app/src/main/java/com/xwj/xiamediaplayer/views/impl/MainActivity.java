package com.xwj.xiamediaplayer.views.impl;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tencent.map.geolocation.TencentLocation;
import com.xwj.xiamediaplayer.R;
import com.xwj.xiamediaplayer.presenters.MainPresenter;
import com.xwj.xiamediaplayer.presenters.impl.MainPresenterImpl;
import com.xwj.xiamediaplayer.views.MainView;


/**
 * 主界面Activity
 * Created by xiaweijia on 16/3/16.
 */
public class MainActivity extends AppCompatActivity implements MainView, View.OnClickListener {
    private ImageButton mIbVideoList, mIbMusicList;
    private CardView mCvMusicList, mCvVideoList, mCvHistory;

    private MainPresenter mMainPresenter;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private TextView mTvMainSetting, mTvQuit;
    private TextView mTvLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main);
        initViews();
        mMainPresenter = new MainPresenterImpl(this, this);
        mMainPresenter.onStart();
    }

    private void initViews() {
        Toolbar toolbar = (Toolbar) this.findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mIbMusicList = (ImageButton) this.findViewById(R.id.ib_music_list);
        mIbVideoList = (ImageButton) this.findViewById(R.id.ib_video_list);

        mCvMusicList = (CardView) this.findViewById(R.id.cv_main_music_list);
        mCvVideoList = (CardView) this.findViewById(R.id.cv_main_video_list);
        mCvHistory = (CardView) this.findViewById(R.id.cv_main_history_list);
        mDrawerLayout = (DrawerLayout) this.findViewById(R.id.dl_main);
        mTvLocation = (TextView) this.findViewById(R.id.tv_main_location);
        mTvQuit = (TextView) this.findViewById(R.id.tv_main_quit);

        mTvMainSetting = (TextView) this.findViewById(R.id.tv_main_setting);

        mActionBarDrawerToggle = new ActionBarDrawerToggle(this,
                mDrawerLayout, toolbar, 0, 0);
        mDrawerLayout.addDrawerListener(mActionBarDrawerToggle);

        mCvMusicList.setOnClickListener(this);
        mCvVideoList.setOnClickListener(this);
        mCvHistory.setOnClickListener(this);
        mTvMainSetting.setOnClickListener(this);
        mTvQuit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        mMainPresenter.onClick(v);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mActionBarDrawerToggle.syncState();
    }

    @Override
    public void startActivity(Class<?> clazz) {
        this.startActivity(new Intent(this, clazz));
    }

    @Override
    public void exit() {
        this.finish();
    }

    @Override
    public void hideDrawer() {
        mDrawerLayout.closeDrawers();
    }

    @Override
    public void showDrawer() {
        mDrawerLayout.openDrawer(Gravity.LEFT);
    }

    @Override
    public boolean isDrawerShown() {
        return mDrawerLayout.isDrawerOpen(Gravity.LEFT);
    }

    @Override
    public void bindLocation(TencentLocation tencentLocation) {
        mTvLocation.setText(tencentLocation.getCity());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mMainPresenter.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        mMainPresenter.onBackPressed();
    }
}
