package com.study.tindernews;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.util.Log;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.study.tindernews.model.NewsResponse;
import com.study.tindernews.network.NewsApi;
import com.study.tindernews.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navView = findViewById(R.id.nav_view); // find view for bottom in XML layout files.
        // 找到fragment host     host fragment --> activity_main/host_fragment
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        // 把fragment的controller取出来，它可以告诉我们当前显示的是谁、也可以控制跳转
        navController = navHostFragment.getNavController();
        // 1. link navView and controller
        NavigationUI.setupWithNavController(navView, navController);
//        // 2. link action bar and controller：action bar会显示名字、负责返回。Now we don't want it anymore.
//        NavigationUI.setupActionBarWithNavController(this, navController);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Retrofit retrofit = RetrofitClient.newInstance(this);


        // TEST code: test the getTopHeadlines function.
        //     1. moved to NewsRepository
        //     2. use LiveData to store response
//        NewsApi newsApi = retrofit.create(NewsApi.class);        // reflection
//        // one call is one task, enqueue the task to let executor do it.
//        // give a callback to handle response
//        Call<NewsResponse> task = newsApi.getTopHeadlines("CN");
//        task.enqueue(new Callback<NewsResponse>() {
//            @Override
//            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
//                // if success print body, else print response(message, code)
//                if (response.isSuccessful()) {
//                    Log.d("getTopHeadlines", response.body().toString());
//                } else {
//                    Log.d("getTopHeadlines", response.toString());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<NewsResponse> call, Throwable t) {
//                Log.d("getTopHeadlines", t.toString());
//            }
//        });

        // NetworkOnMainThreadException: if send sync requests in main thread, then you will get this Exception
        // network为何不能在主线程：阻塞主线程 --> block UI

        // Note1: sync的方法(execute)要在其他线程才能用，可以试试手动new Thread(new Runnable(){ ... sync request(execute) ...}) ，可执行。
        // Note2: async方法(enqueue)，帮我们创建新线程，并主动把数据搬回主线程(防止data race)。

    }

    // 支持action bar的回退到home的操作
    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp();
    }
}
