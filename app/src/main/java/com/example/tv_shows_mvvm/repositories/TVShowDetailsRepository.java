package com.example.tv_shows_mvvm.repositories;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.tv_shows_mvvm.network.ApiClient;
import com.example.tv_shows_mvvm.network.ApiService;
import com.example.tv_shows_mvvm.responses.TVShowsDetailsResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TVShowDetailsRepository {
    private ApiService apiService;

    public TVShowDetailsRepository(){
        apiService = ApiClient.getRetrofit().create(ApiService.class);
    }

    public LiveData<TVShowsDetailsResponse> getTVShowDetails(String tvShowId){
         /*
        1.MutableLiveData的父类是LiveData
        2.LiveData在实体类里可以通知指定某个字段的数据更新.
        3.MutableLiveData则是完全是整个实体类或者数据类型变化后才通知.不会细节到某个字段
        4.LiveData不可变,MutableLiveData是可变的 --
         */

        MutableLiveData<TVShowsDetailsResponse> data = new MutableLiveData<>();
        //发起异步请求, 调用Call的enqueue()方法（同步用execute()方法）
        apiService.getTVShowDetails(tvShowId).enqueue(new Callback<TVShowsDetailsResponse>() {
            @Override
            public void onResponse(@NonNull Call<TVShowsDetailsResponse> call,@NonNull Response<TVShowsDetailsResponse> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<TVShowsDetailsResponse> call,@NonNull Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }

}
