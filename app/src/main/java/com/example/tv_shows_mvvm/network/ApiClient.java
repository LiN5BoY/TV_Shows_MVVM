package com.example.tv_shows_mvvm.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    //Retrofit 是一个 RESTful 的 HTTP 网络请求框架的封装，网络请求的工作本质上是 OkHttp 完成，而 Retrofit 仅负责 网络请求接口的封装

    private static Retrofit retrofit;

    public  static Retrofit getRetrofit(){
        if(retrofit==null){
            retrofit = new Retrofit.Builder()
                    .baseUrl("https://www.episodate.com/api/")//网络请求的url地址
                    .addConverterFactory(GsonConverterFactory.create())//地址解析器
                    .build();
        }
        return  retrofit;
    }
}
