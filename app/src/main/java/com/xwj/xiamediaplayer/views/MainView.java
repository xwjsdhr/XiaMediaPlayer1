package com.xwj.xiamediaplayer.views;

import com.tencent.map.geolocation.TencentLocation;

/**
 * Created by xiaweijia on 16/3/16.
 */
public interface MainView {

    void startActivity(Class<?> clazz);

    void exit();

    void hideDrawer();

    void showDrawer();

    boolean isDrawerShown();

    void bindLocation(TencentLocation tencentLocation);
}
