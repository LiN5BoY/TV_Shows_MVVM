package com.example.tv_shows_mvvm.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.tv_shows_mvvm.R;
import com.example.tv_shows_mvvm.adapters.WatchlistAdapter;
import com.example.tv_shows_mvvm.databinding.ActivityWatchlistBinding;
import com.example.tv_shows_mvvm.listeners.WatchlistListener;
import com.example.tv_shows_mvvm.modles.TVShow;
import com.example.tv_shows_mvvm.viewmodels.WatchlistViewModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class WatchlistActivity extends AppCompatActivity implements WatchlistListener {

    private ActivityWatchlistBinding activityWatchlistBinding;
    private WatchlistViewModel viewModel;
    private WatchlistAdapter watchlistAdapter;
    private List<TVShow> watchlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityWatchlistBinding = DataBindingUtil.setContentView(this, R.layout.activity_watchlist);
        doInitialization();
    }

    private void doInitialization() {
        viewModel = new ViewModelProvider(this).get(WatchlistViewModel.class);
        //返回事件
        activityWatchlistBinding.imageBack.setOnClickListener(v -> onBackPressed());
        watchlist = new ArrayList<>();
    }

    private void loadWatchlist() {
        activityWatchlistBinding.setIsLoading(true);
        //CompositeDisposable
        //一个disposable的容器，可以容纳多个disposable，添加和去除的复杂度为O(1)
        //add()，将disposable添加到容器中。
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        //使用RxJava的subscribeOn和observeOn可以方便地进行线程切换
        //subscribeOn只是用来决定在哪个线程订阅，如果订阅之后没有切换线程操作，数据会在当前线程（订阅时的线程）发射
        //Schedulers.computation()用于计算任务，如事件循环或和回调处理，不要用于IO操作(IO操作请使用Schedulers.io())；默认线程数等于处理器的数量
        compositeDisposable.add(viewModel.loadWatchlist().subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(tvShows -> {
                    activityWatchlistBinding.setIsLoading(false);
                    if(watchlist.size()>0){
                        watchlist.clear();
                    }
                    //addAll()方法會將所有指定集合中的元素添加到此列表的結尾，因為它們是由指定collection的迭代器返回的順序。
                    watchlist.addAll(tvShows);
                    watchlistAdapter = new WatchlistAdapter(watchlist,this);
                    activityWatchlistBinding.watchlistRecyclerView.setAdapter(watchlistAdapter);
                    activityWatchlistBinding.watchlistRecyclerView.setVisibility(View.VISIBLE);
                    compositeDisposable.dispose();
                })
        );
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadWatchlist();
    }

    @Override
    public void onTVShowClicked(TVShow tvShow) {
        Intent intent = new Intent(getApplicationContext(),TVShowDetailsActivity.class);
        intent.putExtra("tvShow",tvShow);
        startActivity(intent);
    }

    @Override
    public void removeTVShowFromWatchlist(TVShow tvShow, int position) {

    }
}