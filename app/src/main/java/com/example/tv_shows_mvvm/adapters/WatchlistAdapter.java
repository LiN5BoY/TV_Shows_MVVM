package com.example.tv_shows_mvvm.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tv_shows_mvvm.R;

import com.example.tv_shows_mvvm.databinding.ItemConstainerTvShowBinding;
import com.example.tv_shows_mvvm.listeners.WatchlistListener;
import com.example.tv_shows_mvvm.models.TVShow;

import java.util.List;

public class WatchlistAdapter extends RecyclerView.Adapter<WatchlistAdapter.TVShowViewHolder>{

    private List<TVShow> tvShows;

    //LayoutInflater 所谓“扩展”，
    //作用类似于findViewById()，
    //不同的是LayoutInflater是用来获得View的，
    //即返回值就是View ，
    //而findViewById()是用来获得具体控件的
    private LayoutInflater layoutInflater;

    private WatchlistListener watchlistListener;



    public WatchlistAdapter(List<TVShow> tvShows, WatchlistListener watchlistListener) {
        this.tvShows = tvShows;
        this.watchlistListener = watchlistListener;
    }

    @NonNull
    @Override
    public TVShowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(layoutInflater == null){
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        ItemConstainerTvShowBinding tvShowBinding = DataBindingUtil.inflate(
                layoutInflater, R.layout.item_constainer_tv_show,parent,false
        );
        return new TVShowViewHolder(tvShowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull TVShowViewHolder holder, int position) {
        holder.bindTVshow(tvShows.get(position));

    }

    @Override
    public int getItemCount() {
        return tvShows.size();
    }

     class TVShowViewHolder extends RecyclerView.ViewHolder{

        private ItemConstainerTvShowBinding itemConstainerTvShowBinding;

        public TVShowViewHolder(ItemConstainerTvShowBinding itemConstainerTvShowBinding){
            super(itemConstainerTvShowBinding.getRoot());
            this.itemConstainerTvShowBinding = itemConstainerTvShowBinding;
        }

        public void bindTVshow(TVShow tvShow){
            itemConstainerTvShowBinding.setTvShow(tvShow);
            itemConstainerTvShowBinding.executePendingBindings();
            //getRoot,返回根视图
            itemConstainerTvShowBinding.getRoot().setOnClickListener(v -> watchlistListener.onTVShowClicked(tvShow));
            itemConstainerTvShowBinding.imageDelete.setOnClickListener(view -> watchlistListener.removeTVShowFromWatchlist(tvShow,getAdapterPosition()));
            itemConstainerTvShowBinding.imageDelete.setVisibility(View.VISIBLE);
        }
    }

}
