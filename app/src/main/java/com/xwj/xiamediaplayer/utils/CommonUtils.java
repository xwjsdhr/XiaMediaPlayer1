package com.xwj.xiamediaplayer.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;
import android.widget.Toast;

/**
 * 工具类
 * Created by xiaweijia on 16/3/25.
 */
public class CommonUtils {
    //整型转时间字符串
    public static String getTimeString(long duration) {
        int minutes = (int) Math.floor(duration / 1000 / 60);
        int seconds = (int) ((duration / 1000) - (minutes * 60));
        return minutes + ":" + String.format("%02d", seconds);
    }

    //单位转化
    public static float dp2px(Context context, int dp) {
        Resources r = context.getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
        return px;
    }

    public static void toast(Context context, String msg) {
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
    }
}
