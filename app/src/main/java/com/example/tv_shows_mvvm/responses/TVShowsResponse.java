package com.example.tv_shows_mvvm.responses;

import com.example.tv_shows_mvvm.modles.TVShow;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TVShowsResponse {
    //使用@SerializedName注解,可以将自定义的字段名与json数据里面的字段对应起来;
    @SerializedName("page")
    private int page;

    @SerializedName("pages")
    private int totalpages;

    @SerializedName("tv_shows")
    private List<TVShow> tvshows;

    public int getPage() {
        return page;
    }

    public int getTotalpages() {
        return totalpages;
    }

    public List<TVShow> getTvshows() {
        return tvshows;
    }
}
