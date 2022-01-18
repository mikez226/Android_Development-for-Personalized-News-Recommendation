package com.study.tindernews.ui.save;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.study.tindernews.databinding.SavedNewsItemBinding;
import com.study.tindernews.model.Article;

import java.util.ArrayList;
import java.util.List;

import com.study.tindernews.R;

public class SavedNewsAdapter extends RecyclerView.Adapter<SavedNewsAdapter.SavedNewsViewHolder> {

    // interface: 好处：flexibility。不需要把saveFragment引入进来。
    //            只需要最终把private的itemCallback实现了即可。
    // delete favorite articles: provide listener & set callback for adapter
    interface ItemCallback {
        void onOpenDetails(Article article); // opening a new fragment for article details.
        void onRemoveFavorite(Article article); // to remove articles in the saved database.
    }
    private ItemCallback itemCallback; // will be implemented.
    public void setItemCallback(ItemCallback itemCallback) { // call by onViewCreated in SaveFragment.
        this.itemCallback = itemCallback;
    }


    // 1. Supporting data:
    private List<Article> articles = new ArrayList<>();

    public void setArticles(List<Article> newsList) {
        articles.clear();
        articles.addAll(newsList);
        notifyDataSetChanged(); // 提醒adapter来refresh (new data)
    }

    // 2. Adapter overrides:
    @NonNull
    @Override
    public SavedNewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.saved_news_item, parent, false);
        return new SavedNewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SavedNewsViewHolder holder, int position) {
        Article article = articles.get(position);
        holder.authorTextView.setText(article.author);
        holder.descriptionTextView.setText(article.description);
        // Use the itemCallback to inform the adapter the onRemoveFavorite event when the favoriteIcon is clicked,
        // also inform the opening for details event.
        // (SavedNewsAdapter/onViewCreated --> implement onOpen, onRemove methods)
        holder.favoriteIcon.setOnClickListener(v -> itemCallback.onRemoveFavorite(article));
        holder.itemView.setOnClickListener(v -> itemCallback.onOpenDetails(article));
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    // 3. SavedNewsViewHolder: - Holder class
    public static class SavedNewsViewHolder extends RecyclerView.ViewHolder {

        TextView authorTextView;
        TextView descriptionTextView;
        ImageView favoriteIcon;

        public SavedNewsViewHolder(@NonNull View itemView) {
            super(itemView);
            SavedNewsItemBinding binding = SavedNewsItemBinding.bind(itemView);
            authorTextView = binding.savedItemAuthorContent;
            descriptionTextView = binding.savedItemDescriptionContent;
            favoriteIcon = binding.savedItemFavoriteImageView;
        }
    }
}
