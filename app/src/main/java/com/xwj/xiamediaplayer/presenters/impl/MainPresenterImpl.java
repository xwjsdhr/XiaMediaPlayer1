package com.xwj.xiamediaplayer.presenters.impl;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;
import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.map.geolocation.TencentLocationRequest;
import com.xwj.xiamediaplayer.R;
import com.xwj.xiamediaplayer.presenters.MainPresenter;
import com.xwj.xiamediaplayer.utils.CommonUtils;
import com.xwj.xiamediaplayer.views.MainView;
import com.xwj.xiamediaplayer.views.impl.HistoryListActivity;
import com.xwj.xiamediaplayer.views.impl.MusicListActivity;
import com.xwj.xiamediaplayer.views.impl.SettingsActivity;
import com.xwj.xiamediaplayer.views.impl.VideoListActivity;


/**
 * Created by xiaweijia on 16/3/16.
 */
public class MainPresenterImpl implements MainPresenter, TencentLocationListener {
    private static final String TAG = MainPresenterImpl.class.getSimpleName();
    private MainView mMainView;
    private Context mContext;
    private boolean isPressed = false;

    private Handler handler = new Handler();

    public MainPresenterImpl(Context context, MainView mainView) {
        this.mMainView = mainView;
        this.mContext = context;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.cv_main_video_list:
                mMainView.startActivity(VideoListActivity.class);
                break;
            case R.id.cv_main_music_list:
                mMainView.startActivity(MusicListActivity.class);
                break;
            case R.id.cv_main_history_list:
                mMainView.startActivity(HistoryListActivity.class);
                break;
            case R.id.tv_main_setting:
                mMainView.hideDrawer();
                mMainView.startActivity(SettingsActivity.class);
                break;
            case R.id.tv_main_quit:
                mMainView.exit();
                break;
        }
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_setting:
                mMainView.startActivity(SettingsActivity.class);
                break;
        }
        return false;
    }

    /**
     * 双击退出
     */
    @Override
    public void onBackPressed() {
        if (!isPressed) {
            CommonUtils.toast(mContext, "再次点击退出");
            isPressed = true;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    isPressed = false;
                }
            }, 3000);
        } else {
            isPressed = false;
            mMainView.exit();
        }
    }

    @Override
    public void onStart() {
        TencentLocationRequest tencentLocationRequest = TencentLocationRequest.create();
        tencentLocationRequest.setInterval(1000 * 60 * 60);
        tencentLocationRequest.setRequestLevel(TencentLocationRequest.REQUEST_LEVEL_ADMIN_AREA);
        int error = TencentLocationManager.getInstance(mContext).requestLocationUpdates(tencentLocationRequest, this);
        Log.e(TAG, "onStart: " + error);
    }

    @Override
    public void onLocationChanged(TencentLocation tencentLocation, int i, String s) {
        Toast.makeText(mContext, tencentLocation.getCity(), Toast.LENGTH_SHORT).show();
        if (TencentLocation.ERROR_OK == i) {
            mMainView.bindLocation(tencentLocation);
        } else {
            Toast.makeText(mContext, "定位失败", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onStatusUpdate(String s, int i, String s1) {
    }
}
