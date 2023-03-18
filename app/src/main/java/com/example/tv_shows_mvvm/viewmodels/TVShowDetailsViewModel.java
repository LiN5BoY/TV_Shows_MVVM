package com.example.tv_shows_mvvm.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;


import com.example.tv_shows_mvvm.database.TVShowDatabase;
import com.example.tv_shows_mvvm.models.TVShow;
import com.example.tv_shows_mvvm.repositories.TVShowDetailsRepository;
import com.example.tv_shows_mvvm.responses.TVShowsDetailsResponse;

import io.reactivex.Completable;

//AndroidViewModel类是ViewModel的子类，与之相似，它们旨在存储和管理与UI相关的数据，负责准备和提供数据UI，并自动允许数据在配置更改后继续存在。
//AndroidViewModel的唯一区别是它与应用程序上下文一起提供，如果您需要上下文来获取系统服务或有类似要求，这将很有帮助。
public class TVShowDetailsViewModel extends AndroidViewModel {

    private TVShowDetailsRepository tvShowDetailsRepository;
    private TVShowDatabase tvShowDatabase;


    public TVShowDetailsViewModel(@NonNull Application application){
        super(application);
        tvShowDetailsRepository = new TVShowDetailsRepository();
        tvShowDatabase = TVShowDatabase.getTvShowDatabase(application);
    }

    public LiveData<TVShowsDetailsResponse> getTVShowDetails(String tvShowId){
        return tvShowDetailsRepository.getTVShowDetails(tvShowId);
    }

    //Completable. 只发射一条完成通知，或者一条异常通知，不能发射数据，其中完成通知与异常通知只能发射一个
    public Completable addtoWatchlist(TVShow tvShow){
        return tvShowDatabase.tvShowDao().addToWatchlist(tvShow);
    }
}
