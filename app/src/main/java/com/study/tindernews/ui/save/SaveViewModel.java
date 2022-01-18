package com.study.tindernews.ui.save;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.study.tindernews.model.Article;
import com.study.tindernews.repository.NewsRepository;

import java.util.List;

public class SaveViewModel extends ViewModel {
    private final NewsRepository repository;

    public SaveViewModel(NewsRepository repository) {
        this.repository = repository;
    }

    // API-1 get all saved articles
    public LiveData<List<Article>> getAllSavedArticles() {
        return repository.getAllSavedArticles();
    }

    // API-2 delete saved articles
    public void deleteSavedArticle(Article article) {
        repository.deleteSavedArticle(article);
    }
}
