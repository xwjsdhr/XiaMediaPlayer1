package com.xwj.xiamediaplayer.dao;


import com.xwj.xiamediaplayer.entitys.HistoryVideo;

import java.util.List;

/**
 * Created by xwjsd on 2016-05-05.
 */
public interface HistoryDao {

    List<HistoryVideo> getAllHistory();

    boolean isExist(int id);

    long insert(HistoryVideo video);

    long update(HistoryVideo video);

    void clearAll();

}
