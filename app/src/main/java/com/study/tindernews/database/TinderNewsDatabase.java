package com.study.tindernews.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.study.tindernews.model.Article;

//  The Room annotation processor will implements these abstract methods.
// So this class is an abstract class

@Database(entities = {Article.class}, version = 1, exportSchema = false)
// no migration strategy. if a db already exists, I will drop it. (this is a simple app)
public abstract class TinderNewsDatabase extends RoomDatabase {
    public abstract ArticleDao articleDao();
}
