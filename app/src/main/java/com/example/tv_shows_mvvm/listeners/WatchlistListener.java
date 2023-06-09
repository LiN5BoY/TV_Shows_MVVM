package com.example.tv_shows_mvvm.listeners;

import com.example.tv_shows_mvvm.models.TVShow;

public interface WatchlistListener {

    void onTVShowClicked(TVShow tvShow);

    void removeTVShowFromWatchlist(TVShow tvShow,int position);


}
