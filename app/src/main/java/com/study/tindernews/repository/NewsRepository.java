package com.study.tindernews.repository;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.study.tindernews.TinderNewsApplication;
import com.study.tindernews.database.TinderNewsDatabase;
import com.study.tindernews.model.Article;
import com.study.tindernews.model.NewsResponse;
import com.study.tindernews.network.NewsApi;
import com.study.tindernews.network.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsRepository {
    // MVVM --> Model part (Repository in Android)

    private final NewsApi newsApi;
    private final TinderNewsDatabase database;

    public NewsRepository(Context context) {
        newsApi = RetrofitClient.newInstance(context).create(NewsApi.class);
        database = ((TinderNewsApplication) context.getApplicationContext()).getDatabase(); // get the singleton database instance from context
        // In TinderNewsApplication, I create a getter to access the singleton database
        // You can also use Dependency Injection (Dagger2) to get the database.
    }

    //  理解执行顺序：
    //  1. task enqueue
    //  2. return 这个LiveData（空的）
    //  3. 等消息回来了，执行callback里面的setValue（给里面填数据）
    //  4. View subscribe了LiveData，那么它会知道里面有数据了。

    // Part 1 implement getTopHeadlines API in NewsRepository;
    public LiveData<NewsResponse> getTopHeadlines(String country) {
        MutableLiveData<NewsResponse> topHeadlinesLiveData = new MutableLiveData<>();
        Call<NewsResponse> task = newsApi.getTopHeadlines(country);
        task.enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                if (response.isSuccessful()) {
                    topHeadlinesLiveData.setValue(response.body());
                } else {
                    topHeadlinesLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<NewsResponse> call, Throwable t) {
                topHeadlinesLiveData.setValue(null);
            }
        });
        return topHeadlinesLiveData; // the LiveData container.
    }

    // Part 2 implement searchNews API in NewsRepository:
    public LiveData<NewsResponse> searchNews(String query) {
        MutableLiveData<NewsResponse> everyThingLiveData = new MutableLiveData<>();
        newsApi.getEverything(query, 40)
                .enqueue(
                        new Callback<NewsResponse>() {
                            @Override
                            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                                if (response.isSuccessful()) {
                                    everyThingLiveData.setValue(response.body());
                                } else {
                                    everyThingLiveData.setValue(null);
                                }
                            }

                            @Override
                            public void onFailure(Call<NewsResponse> call, Throwable t) {
                                everyThingLiveData.setValue(null);
                            }
                        });
        return everyThingLiveData;
    }

    // Part 3 favorite API --> run database query in background thread
    // API - 1    I write an AsyncTask class to implement set favorite (just for practicing)
    public LiveData<Boolean> favoriteArticle(Article article) {
        MutableLiveData<Boolean> resultLiveData = new MutableLiveData<>();
        new FavoriteAsyncTask(database, resultLiveData).execute(article); // execute -> doInBackground -> onPostExecute
        return resultLiveData; // the LiveData is returned immediately.
        // The database operation runs in the background and notifies the result through the resultLiveData at a later time.
    }

    // API - 2    read operation don't need async task 读操作不需要async task
    public LiveData<List<Article>> getAllSavedArticles() {
        return database.articleDao().getAllArticles();
    }

    // API - 3    write operation need async task 写操作需要async task.
    // I don't need result, so I can use lambda.
    public void deleteSavedArticle(Article article) {
        AsyncTask.execute(() -> database.articleDao().deleteArticle(article));
        // data racing handled by SQLite
    }

    // We use a simple AsyncTask to access database.
    private static class FavoriteAsyncTask extends AsyncTask<Article, Void, Boolean> {

        private final TinderNewsDatabase database;
        private final MutableLiveData<Boolean> liveData;

        private FavoriteAsyncTask(TinderNewsDatabase database, MutableLiveData<Boolean> liveData) {
            this.database = database;
            this.liveData = liveData;
        }

        // Everything inside doInBackground would be executed on a separate background thread.
        @Override
        protected Boolean doInBackground(Article... articles) {
            Article article = articles[0];
            try {
                database.articleDao().saveArticle(article);
            } catch (Exception e) {
                return false;
            }
            return true;
        }

        // After doInBackground finishes, onPostExecute would be executed back on the main UI thread.
        @Override
        protected void onPostExecute(Boolean success) {
            liveData.setValue(success);
        }
    }
}
