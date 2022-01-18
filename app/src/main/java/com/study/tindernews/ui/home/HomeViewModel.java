package com.study.tindernews.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.study.tindernews.model.Article;
import com.study.tindernews.model.NewsResponse;
import com.study.tindernews.repository.NewsRepository;

public class HomeViewModel extends ViewModel {

    private final NewsRepository repository;
    private final MutableLiveData<String> countryInput = new MutableLiveData<>();
    // this countryInput just store the country code in viewModel.
    // now we don't store the response in viewModel, instead we return that LiveData in getTopHeadlines.
    // if we store the response here, we can retain the data after rotate the screen.

    public HomeViewModel(NewsRepository newsRepository) {
        this.repository = newsRepository;
    }

    public void setCountryInput(String country) { // caller is View
        countryInput.setValue(country);
    }

    public LiveData<NewsResponse> getTopHeadlines() { // caller is View
        return Transformations.switchMap(countryInput, repository::getTopHeadlines);
        // return a new LiveData
    } // 获得薯片盒子（后续reposne回来后自动填充）

//    比喻：
//    土豆：country这个String,它放在一个盒子(MutableLiveData)里送入switchMap
//    生产薯片的机器：getTopHeadLines，它的input是一个String。工厂负责把country取出来给他。
//    薯片盒子：是getTopHeadlines的返回值，是一个新的盒子。
//    这个盒子先返回，等response回来了里面会突然出现薯片。
//    subscribe薯片盒子的view，会知道薯片来了。


    //  This time, we don’t need to expose the observing result.
    //  So we don’t have to do the Transformations.switchMap trick.
    public void setFavoriteArticleInput(Article article) {
        repository.favoriteArticle(article); // do not need to return a LiveData
    }
}
