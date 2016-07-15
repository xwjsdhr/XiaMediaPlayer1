package com.xwj.xiamediaplayer.interator;

/**
 * Created by xiaweijia on 16/3/15.
 */
public interface QueryVideoInteractor {
    public void query(QueryListener listener);

    public void stop();

    public void queryNormal(QueryListener listener);

    public void queryByName(QueryListener listener, String text);

    public void queryHistoryFromDb(QueryListener listener);

}
