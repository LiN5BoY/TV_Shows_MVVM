package com.example.tv_shows_mvvm.network;

import com.example.tv_shows_mvvm.responses.TVShowsDetailsResponse;
import com.example.tv_shows_mvvm.responses.TVShowsResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    // 注解里传入 网络请求 的部分URL地址
    // Retrofit把网络请求的URL分成了两部分：一部分放在Retrofit对象里，另一部分放在网络请求接口里

    //获取列表
    @GET("most-popular")
    Call<TVShowsResponse> getMostPopularTVShows(@Query("page")int page);

    @GET("show-details")
    Call<TVShowsDetailsResponse> getTVShowDetails(@Query("q")String tvShowId);
}
