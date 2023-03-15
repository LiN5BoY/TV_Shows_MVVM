package com.example.tv_shows_mvvm.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

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
        activityTvshowDetailsBinding = DataBindingUtil.setContentView(this, R.layout.activity_tvshow_details);
        doInitialization();
    }

    private void doInitialization() {
        tvShowDetailsViewModel = new ViewModelProvider(this).get(TVShowDetailsViewModel.class);
        //返回按钮事件处理
        activityTvshowDetailsBinding.imageBack.setOnClickListener(v -> onBackPressed());
        getTVShowDetails();
    }

    //实现
    private void getTVShowDetails() {
        activityTvshowDetailsBinding.setIsLoading(true);
        String tvShowId = String.valueOf(getIntent().getIntExtra("id", -1));
        tvShowDetailsViewModel.getTVShowDetails(tvShowId).observe(
                this, tvShowsDetailsResponse -> {
                    activityTvshowDetailsBinding.setIsLoading(false);
                    if (tvShowsDetailsResponse.getTvShowDetails() != null) {
                        if (tvShowsDetailsResponse.getTvShowDetails().getPictures() != null) {
                            loadImageSlider(tvShowsDetailsResponse.getTvShowDetails().getPictures());
                        }
                    }
                }
        );
    }

    private void loadImageSlider(String[] sliderImages) {
        //设置的预加载数量
        //setOffscreenPageLimit() 方法设置的默认值是 1
        //（1）ViewPager 会预加载几页
        //（2）ViewPager 会缓存 2*n+1 页(n为设置的值)
        //如设置为n=1，当前在第一页，会预加载第二页，滑倒第二页，会预加载第三页，当滑倒第三页，第一页会销毁，第四页会加载。
        activityTvshowDetailsBinding.sliderViewPager.setOffscreenPageLimit(1);
        activityTvshowDetailsBinding.sliderViewPager.setAdapter(new ImageSliderAdapter(sliderImages));
        activityTvshowDetailsBinding.sliderViewPager.setVisibility(View.VISIBLE);
        activityTvshowDetailsBinding.viewFadingEdge.setVisibility(View.VISIBLE);
        setupSliderIndicators(sliderImages.length);
        //registerOnPageChangeCallback()方法——设置页面改变时的回调
        activityTvshowDetailsBinding.sliderViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setCurrentSliderIndicator(position);
            }
        });
    }

    private void setupSliderIndicators(int count) {
        //ImageView数组定义，大小为传入的length，即长度大小
        ImageView[] indicators = new ImageView[count];
        //LayoutParams翻译过来就是布局参数，子View通过LayoutParams告诉父容器（ViewGroup）应该如何放置自己。
        // 从这个定义中也可以看出来LayoutParams与ViewGroup是息息相关的，因此脱离ViewGroup谈LayoutParams是没有意义的。
        //事实上，每个ViewGroup的子类都有自己对应的LayoutParams类，
        // 典型的如LinearLayout.LayoutParams和FrameLayout.LayoutParams等，可以看出来LayoutParams都是对应ViewGroup子类的内部类。
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                //第一个参数为宽的设置，第二个参数为高的设置.
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(8, 0, 8, 0);
        for (int i = 0; i < indicators.length; i++) {
            //getApplicationContext()——获取当前应用的上下文环境。即当前应用所使用的的Application
            indicators[i] = new ImageView(getApplicationContext());
            indicators[i].setImageDrawable(ContextCompat.getDrawable(
                    getApplicationContext(),
                    R.drawable.background_slider_indicator_inactive
            ));
            indicators[i].setLayoutParams(layoutParams);
            activityTvshowDetailsBinding.layoutSliderIndicators.addView(indicators[i]);
        }
        activityTvshowDetailsBinding.layoutSliderIndicators.setVisibility(View.VISIBLE);
        setCurrentSliderIndicator(0);
    }

    private void setCurrentSliderIndicator(int position) {
        //getChildCount获得的layout布局下的子view数目。
        int childCount = activityTvshowDetailsBinding.layoutSliderIndicators.getChildCount();
        for (int i = 0; i < childCount; i++) {
            ImageView imageView = (ImageView) activityTvshowDetailsBinding.layoutSliderIndicators.getChildAt(i);
            if (i == position) {
                imageView.setImageDrawable(
                        ContextCompat.getDrawable(getApplicationContext(), R.drawable.background_slider_indicator_active)
                );
            } else {
                imageView.setImageDrawable(
                        ContextCompat.getDrawable(getApplicationContext(), R.drawable.background_slider_indicator_inactive)
                );
            }
        }
    }

}