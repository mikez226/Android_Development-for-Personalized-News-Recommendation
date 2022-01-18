package com.study.tindernews.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.study.tindernews.model.Article;

import java.util.List;

// ArticleDao specifies the operations needed to access the article table.
// annotation processor will auto generate code for these operations.
@Dao
public interface ArticleDao {
    @Insert
    void saveArticle(Article article);

    @Query("SELECT * FROM article")
    LiveData<List<Article>> getAllArticles();

    @Delete
    void deleteArticle(Article article);
}
