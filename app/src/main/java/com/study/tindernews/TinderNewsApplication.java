package com.study.tindernews;

import android.app.Application;

import androidx.room.Room;

import com.ashokvarma.gander.Gander;
import com.ashokvarma.gander.imdb.GanderIMDB;
import com.facebook.stetho.Stetho;
import com.study.tindernews.database.TinderNewsDatabase;

public class TinderNewsApplication extends Application {
    // 区分activity与application：
    // 离开当前应用但后台不关闭：
    //      activity状态：onStop
    //      application状态：不需要变化，它是个singleton，一个App只有一个Application的实例。

    private TinderNewsDatabase database;

    @Override
    public void onCreate() {
        super.onCreate();
        // Gander lets you see each HTTP request from an app,
        // it’s a very convenient network debugging tool.
        Gander.setGanderStorage(GanderIMDB.getInstance());
        Stetho.initializeWithDefaults(this);
        database = Room.databaseBuilder(this, TinderNewsDatabase.class, "tindernews_db").build(); // name is tindernews_db
    }

    public TinderNewsDatabase getDatabase() {
        return database;
    }
}
