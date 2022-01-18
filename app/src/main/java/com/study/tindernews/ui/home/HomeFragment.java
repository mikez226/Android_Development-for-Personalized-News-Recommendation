package com.study.tindernews.ui.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.study.tindernews.R;
import com.study.tindernews.databinding.FragmentHomeBinding;
import com.study.tindernews.model.Article;
import com.study.tindernews.repository.NewsRepository;
import com.study.tindernews.repository.NewsViewModelFactory;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.Duration;
import com.yuyakaido.android.cardstackview.StackFrom;
import com.yuyakaido.android.cardstackview.SwipeAnimationSetting;

import java.util.List;

public class HomeFragment extends Fragment implements CardStackListener { // implements CardStackListener here, since I need to pass a listener into LayoutManager.

    private HomeViewModel viewModel;
    private FragmentHomeBinding binding; // one FragmentHome, many(5) swip_news_card held by adapter.
    private CardStackLayoutManager layoutManager;
    private List<Article> articles;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // use binding class to inflate. inner the inflate, layout will be bound;
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_home, container, false);
//        // inflate --> 打气筒    把xml的layout拿出来，变成一个Java class，把生成的view attache到需要他的地方。
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Setup CardStackView
        CardSwipeAdapter swipeAdapter = new CardSwipeAdapter(); // 将Adapter创建，它负责data -> views
        // The event listener(implements CardStackListener) is set through CardStackLayoutManager on the 2nd argument in the constructor. --> so when I press a button, an animation will show.
        layoutManager = new CardStackLayoutManager(requireContext(), this); // 1: need access Android resources, so provide a Context. 2: need a listener. HomeFragment implements the listener, so pass "this" into it
        layoutManager.setStackFrom(StackFrom.Top); // CardStackView’s documentation says default is None. We need Top. https://github.com/yuyakaido/CardStackView#stack-from
        binding.homeCardStackView.setLayoutManager(layoutManager);
        binding.homeCardStackView.setAdapter(swipeAdapter);

        // Handle like unlike button clicks (help the people who dislike swipe manually)
        binding.homeLikeButton.setOnClickListener(v -> swipeCard(Direction.Right));
        binding.homeUnlikeButton.setOnClickListener(v -> swipeCard(Direction.Left));

        // MVVM pattern : observe LiveData
        NewsRepository repository = new NewsRepository(getContext());
        viewModel = new ViewModelProvider(this, new NewsViewModelFactory(repository))
                .get(HomeViewModel.class); // Factory pattern: get里会call create() 返回class对应viewModel
        viewModel.setCountryInput("us"); // test, us has more urlImages.
        viewModel
                .getTopHeadlines()
                .observe(
                        getViewLifecycleOwner(),
                        newsResponse -> {
                            if (newsResponse != null) {
                                articles = newsResponse.articles; // Assign articles to fields
                                swipeAdapter.setArticles(articles); // feed swipeAdapter.
                            }
                        });

    }

    // Automatic swipe hleper.
    // 点button的时候call这个gesture，这样有些不喜欢滑动的人就可以去点button。
    // https://github.com/yuyakaido/CardStackView#automatic-swipe
    private void swipeCard(Direction direction) {
        SwipeAnimationSetting setting = new SwipeAnimationSetting.Builder()
                .setDirection(direction)
                .setDuration(Duration.Normal.duration)
                .build();
        layoutManager.setSwipeAnimationSetting(setting);
        binding.homeCardStackView.swipe();
    }


    @Override
    public void onCardDragging(Direction direction, float ratio) {

    }

    // we need to override this callback to save the liked articles.
    @Override
    public void onCardSwiped(Direction direction) {
        if (direction == Direction.Left) {
            Log.d("CardStackView", "Unliked " + layoutManager.getTopPosition());
        } else if (direction == Direction.Right) {
            Log.d("CardStackView", "Liked "  + layoutManager.getTopPosition());
            Article article = articles.get(layoutManager.getTopPosition() -1);
            viewModel.setFavoriteArticleInput(article); // it will finally call favorite API under NewsRepository.java
        }
    }

    @Override
    public void onCardRewound() {

    }

    @Override
    public void onCardCanceled() {

    }

    @Override
    public void onCardAppeared(View view, int position) {

    }

    @Override
    public void onCardDisappeared(View view, int position) {

    }
}