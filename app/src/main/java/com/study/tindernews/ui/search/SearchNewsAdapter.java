package com.study.tindernews.ui.search;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.study.tindernews.R;
import com.study.tindernews.databinding.SearchNewsItemBinding;
import com.study.tindernews.model.Article;

import java.util.ArrayList;
import java.util.List;

public class SearchNewsAdapter extends RecyclerView.Adapter<SearchNewsAdapter.SearchNewsViewHolder> {
    // I create a new interface, since we don't need remove method here.
    interface ItemCallback {
        void onOpenDetails(Article article);
    }

    private ItemCallback itemCallback;

    public void setItemCallback(ItemCallback itemCallback) {
        this.itemCallback = itemCallback;
    }



    // 1. Supporting data:---------------------------------------
    private List<Article> articles = new ArrayList<>();


    // every time a new list is set, refresh and re-render the data.
    public void setArticles(List<Article> newsList) {
        articles.clear();
        articles.addAll(newsList);
        notifyDataSetChanged(); // 提醒adapter来refresh（有新数据了）- 相当于暴力reload整个内容。
    }


    // 2. Adapter overrides - three methods:---------------------------------------

    // onCreateViewHolder is for providing the generated item views;
    // 创建一个View，把它放到ViewHolder中去bind。bind好了返回对象，未来就不需要再bind了。
    @NonNull
    @Override
    public SearchNewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext()); // parent's context (search fragment)
        View view = inflater.inflate(R.layout.search_news_item, parent, false);
        return new SearchNewsViewHolder(view);  // bind in advance, update bound fields when "onBindViewHolder"
    }

    // onBindViewHolder is for binding the data with a view. (data)
    // 这个bind是绑定的数据和我们已经拿出来的view fields；而不是类似ViewHolder的构造函数那样，把layout和view绑定起来。
    // data bind fields (not bind layout with view class)
    @Override
    public void onBindViewHolder(@NonNull SearchNewsViewHolder holder, int position) {
        // get data
        Article article = articles.get(position);
        // fill "favorite" image into corresponding view
        holder.favoriteImageView.setImageResource(R.drawable.ic_favorite_border_24dp);
        // fill text into corresponding view
        holder.itemTitleTextView.setText(article.title);
        // set image view using url
        Picasso.get().load(article.urlToImage).into(holder.itemImageView); // loading and caching library.
        // listener:
        holder.itemView.setOnClickListener(v -> itemCallback.onOpenDetails(article));
    }

    // getItemCount is for providing the current data collection size
    @Override
    public int getItemCount() {
        return articles.size();
    }

    // 3. SearchNewsViewHolder:---------------------------------------
    public static class SearchNewsViewHolder extends RecyclerView.ViewHolder { // 创建了一个class，以后hold view的reference

        ImageView favoriteImageView;
        ImageView itemImageView;
        TextView itemTitleTextView;


        // 构造函数负责：1. view binding 2. get all subviews
        public SearchNewsViewHolder(@NonNull View itemView) {
            super(itemView);
            SearchNewsItemBinding binding = SearchNewsItemBinding.bind(itemView);
            favoriteImageView = binding.searchItemFavorite;
            itemImageView = binding.searchItemImage;
            itemTitleTextView = binding.searchItemTitle;
        }
    }

}
