package com.example.tv_shows_mvvm.responses;

import com.example.tv_shows_mvvm.modles.TVShowDetails;
import com.google.gson.annotations.SerializedName;

public class TVShowsDetailsResponse {

    @SerializedName("tvShow")
    private TVShowDetails tvShowDetails;

    public TVShowDetails getTvShowDetails() {
        return tvShowDetails;
    }
}
