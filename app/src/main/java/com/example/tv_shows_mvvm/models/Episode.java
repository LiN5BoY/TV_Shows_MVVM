package com.example.tv_shows_mvvm.models;

import com.google.gson.annotations.SerializedName;

public class Episode {
    //使用@SerializedName注解,可以将自定义的字段名与json数据里面的字段对应起来;
    @SerializedName("season")
    private String season;

    @SerializedName("episode")
    private String episode;

    @SerializedName("name")
    private String name     ;

    @SerializedName("air_date")
    private String air_date;

    public String getSeason() {
        return season;
    }

    public String getEpisode() {
        return episode;
    }

    public String getName() {
        return name;
    }

    public String getAir_date() {
        return air_date;
    }
}
