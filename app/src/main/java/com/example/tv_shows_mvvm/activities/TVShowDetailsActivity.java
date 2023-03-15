package com.example.tv_shows_mvvm.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.tv_shows_mvvm.R;
import com.example.tv_shows_mvvm.adapters.ImageSliderAdapter;
import com.example.tv_shows_mvvm.databinding.ActivityTvshowDetailsBinding;
import com.example.tv_shows_mvvm.viewmodels.TVShowDetailsViewModel;

public class TVShowDetailsActivity extends AppCompatActivity {

    private ActivityTvshowDetailsBinding activityTvshowDetailsBinding;
    private TVShowDetailsViewModel tvShowDetailsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityTvshowDetailsBinding = DataBindingUtil.setContentView(this,R.layout.activity_tvshow_details);
        doInitialization();
    }

    private void doInitialization(){
        tvShowDetailsViewModel = new ViewModelProvider(this).get(TVShowDetailsViewModel.class);
        getTVShowDetails();
    }

    private void getTVShowDetails(){
        activityTvshowDetailsBinding.setIsLoading(true);
        String tvShowId = String.valueOf(getIntent().getIntExtra("id",-1));
        tvShowDetailsViewModel.getTVShowDetails(tvShowId).observe(
                this,tvShowsDetailsResponse -> {
                    activityTvshowDetailsBinding.setIsLoading(false);
                    if(tvShowsDetailsResponse.getTvShowDetails() != null){
                        if(tvShowsDetailsResponse.getTvShowDetails().getPictures()!=null){
                            loadImageSlider(tvShowsDetailsResponse.getTvShowDetails().getPictures());
                        }
                    }
                }
        );
    }

    private void loadImageSlider(String[] sliderImages){
        //设置的预加载数量
        //setOffscreenPageLimit() 方法设置的默认值是 1
        //（1）ViewPager 会预加载几页
        //（2）ViewPager 会缓存 2*n+1 页(n为设置的值)
        //如设置为n=1，当前在第一页，会预加载第二页，滑倒第二页，会预加载第三页，当滑倒第三页，第一页会销毁，第四页会加载。
        activityTvshowDetailsBinding.sliderViewPager.setOffscreenPageLimit(1);
        activityTvshowDetailsBinding.sliderViewPager.setAdapter(new ImageSliderAdapter(sliderImages));
        activityTvshowDetailsBinding.sliderViewPager.setVisibility(View.VISIBLE);
        activityTvshowDetailsBinding.viewFadingEdge.setVisibility(View.VISIBLE);
    }

}