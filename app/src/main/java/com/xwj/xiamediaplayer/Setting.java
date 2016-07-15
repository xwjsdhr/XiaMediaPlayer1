package com.xwj.xiamediaplayer;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.xwj.xiamediaplayer.utils.Constant;

/**
 * Created by xiaweijia on 16/7/14.
 */
public class Setting implements com.xwj.xiamediaplayer.widget.ISetting {
    private Context mContext;
    private SharedPreferences mSharedPreferences;

    public Setting(Context context) {
        this.mContext = context;
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Override
    public boolean isAutoPlayNext() {
        return mSharedPreferences.getBoolean(Constant.SETTING_AUOT_NEXT, false);
    }

    @Override
    public String getShowVideoType() {
        return mSharedPreferences.getString(Constant.SETTING_SHOW_WAYS, "si");
    }

    @Override
    public String getVideoScreenOrientation() {
        return mSharedPreferences.getString(Constant.SETTING_PLAY_VIDEO_ORIETATION, "por");
    }
}
