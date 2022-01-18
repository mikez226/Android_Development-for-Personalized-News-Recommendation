package com.study.tindernews.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.study.tindernews.ui.home.HomeViewModel;
import com.study.tindernews.ui.save.SaveViewModel;
import com.study.tindernews.ui.search.SearchViewModel;

// 通过工厂模式来创建ViewModel, 来自ViewModelProvider.Factory --> 存储ViewModel信息
// 这里就是Factory，输入对应的class，就可以获得HomeViewMode或者SearchViewModel
// 使用工厂模式的原因：
//      所有创建的ViewModel都在一个ViewModelStoreOwner store，这个store和app进程的生命周期不一样。
//      - 在手机切换app的时候，system可能会kill某些进程，如果不用ViewModelProvider，那么数据丢失然后restore的时候new一个新的。
//      - 如果使用ViewModelProvider。则ViewModel会被存起来，被kill后restore的时候，就会从里面拿而不是new一个新的。
//      这样ViewModel里面的数据就没有丢失。
public class NewsViewModelFactory implements ViewModelProvider.Factory {

    private final NewsRepository repository;

    public NewsViewModelFactory(NewsRepository repository) {
        this.repository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(HomeViewModel.class)) {
            return (T) new HomeViewModel(repository);
        } else if (modelClass.isAssignableFrom(SearchViewModel.class)) {
            return (T) new SearchViewModel(repository);
        } else if (modelClass.isAssignableFrom(SaveViewModel.class)) {
            return (T) new SaveViewModel(repository);
        }else {
            throw new IllegalStateException("Unknown ViewModel");
        }
    }
}