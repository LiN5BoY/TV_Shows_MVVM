package com.example.tv_shows_mvvm.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.tv_shows_mvvm.repositories.SearchTVShowRepository;
import com.example.tv_shows_mvvm.responses.TVShowsResponse;

public class SearchviewModel extends ViewModel {

    private SearchTVShowRepository searchTVShowRepository;

    public SearchviewModel(){
        searchTVShowRepository = new SearchTVShowRepository();
    }

    public LiveData<TVShowsResponse> searchTVShow(String query,int page){
        return searchTVShowRepository.searchTVShow(query,page);
    }

}
