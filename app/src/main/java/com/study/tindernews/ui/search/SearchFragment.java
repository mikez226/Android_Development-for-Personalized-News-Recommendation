package com.study.tindernews.ui.search;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.study.tindernews.R;
import com.study.tindernews.databinding.FragmentSearchBinding;
import com.study.tindernews.repository.NewsRepository;
import com.study.tindernews.repository.NewsViewModelFactory;

public class SearchFragment extends Fragment {
    private SearchViewModel viewModel; // viewModel
    private FragmentSearchBinding binding; // --> 将 XML 文件的名称转换为驼峰式大小写，并在末尾添加“Binding”一词。
    // Binding如何知道和哪个绑定？
    // this Fragment + Search + Binding, can help binding
    // to find fragment_search.xml.
    // annotation preprocessor, compile的时候做的，不是代码运行的时候。

    public SearchFragment() {
    } // Required empty public constructor

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Old method:
        // Inflate the layout for this fragment --> layout xml->view class
        // return inflater.inflate(R.layout.fragment_search, container, false);

        // New method
        // I will data binding to do the inflate
        // notice, no id provided. since FragmentSearchBinding know which one we need to inflate.
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // binding is FragmentSearchBinding not SearchNewsItemBinding
        SearchNewsAdapter newsAdapter = new SearchNewsAdapter();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(requireContext(), 2);
        binding.newsResultsRecyclerView.setLayoutManager(gridLayoutManager);
        binding.newsResultsRecyclerView.setAdapter(newsAdapter);
        binding.newsSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // 操作SearchView里面的newsSearchView （搜索栏）
            @Override
            public boolean onQueryTextSubmit(String query) { // submit的时候search
                if (!query.isEmpty()) {
                    viewModel.setSearchInput(query); // replace the hardcoded setSearchInput bellow.
                }
                binding.newsSearchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) { // textChange的时候不search
                return false;
            }
        });


        NewsRepository repository = new NewsRepository(getContext());
        viewModel = new ViewModelProvider(this, new NewsViewModelFactory(repository))
                .get(SearchViewModel.class);
        // viewModel.setSearchInput("Covid-19"); // will get input from searchView in future.
        viewModel
                .searchNews() // return a LiveData
                .observe( // Observe LiveData data stream, input the callback functions
                        getViewLifecycleOwner(),
                        newsResponse -> {
                            if (newsResponse != null) {
                                Log.d("SearchFragment", newsResponse.toString());
                                newsAdapter.setArticles(newsResponse.articles); // reset list and refresh
                                // 注意：我不是只渲染新加入的那个，而是将整个列表都重新渲染。
                                // 但是Picasso、Glide、Fresco都可以实现loading和caching，
                                // 这样尽管我们refresh整个list，但是不会重新下载所有图片。
                            }
                        });

        // implement the methods in the interface.
        newsAdapter.setItemCallback(article -> {
            SearchFragmentDirections.ActionNavigationSearchToNavigationDetails direction = SearchFragmentDirections.actionNavigationSearchToNavigationDetails(article);
            NavHostFragment.findNavController(SearchFragment.this).navigate(direction);
        });
    }
}