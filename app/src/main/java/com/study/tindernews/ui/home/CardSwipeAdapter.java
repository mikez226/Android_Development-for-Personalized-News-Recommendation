package com.study.tindernews.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.study.tindernews.databinding.SwipeNewsCardBinding;
import com.study.tindernews.model.Article;
import com.study.tindernews.R;

import java.util.ArrayList;
import java.util.List;

// Adapter: data -> views

public class CardSwipeAdapter extends RecyclerView.Adapter<CardSwipeAdapter.CardSwipeViewHolder> {
    // 1. Supporting data:
    private List<Article> articles = new ArrayList<>();

    public void setArticles(List<Article> newsList) {
        articles.clear();
        articles.addAll(newsList);
        notifyDataSetChanged();
    }

    // 2. Adapter overrides (implement abstract methods):
    // onCreateViewHolder is for providing the generated item views;
    // in debugging, we found 5 views are created and reused.
    @NonNull
    @Override
    public CardSwipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.swipe_news_card, parent, false);
        return new CardSwipeViewHolder(view); // call the binder
    }

    // onBindViewHolder is for binding the data with a view. (data)
    @Override
    public void onBindViewHolder(@NonNull CardSwipeViewHolder holder, int position) {
        // change the field when update data.
        Article article = articles.get(position);
        holder.titleTextView.setText(article.title);
        holder.descriptionTextView.setText(article.description);
        Picasso.get().load(article.urlToImage).into(holder.imageView);
    }

    // getItemCount is for providing the current data collection size
    @Override
    public int getItemCount() {
        return articles.size();
    }

    // 3. CardSwipeViewHolder:
    public static class CardSwipeViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView titleTextView;
        TextView descriptionTextView;

        public CardSwipeViewHolder(@NonNull View itemView) { // binder
            super(itemView);
            // Bind once. On updating data, only change the field.
            SwipeNewsCardBinding binding = SwipeNewsCardBinding.bind(itemView);
            imageView = binding.swipeCardImageView;
            titleTextView = binding.swipeCardTitle;
            descriptionTextView = binding.swipeCardDescription;
        }
    }
}
