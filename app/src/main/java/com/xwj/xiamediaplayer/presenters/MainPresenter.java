package com.xwj.xiamediaplayer.presenters;

import android.view.MenuItem;
import android.view.View;

/**
 * Created by xiaweijia on 16/3/16.
 */
public interface MainPresenter {
    void onClick(View view);

    void onDestroy();

    boolean onOptionsItemSelected(MenuItem item);

    void onBackPressed();

    void onStart();

}
