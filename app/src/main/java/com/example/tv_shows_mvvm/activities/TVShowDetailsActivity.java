package com.example.tv_shows_mvvm.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.text.HtmlCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.tv_shows_mvvm.R;
import com.example.tv_shows_mvvm.adapters.EpisodesAdapter;
import com.example.tv_shows_mvvm.adapters.ImageSliderAdapter;
import com.example.tv_shows_mvvm.databinding.ActivityTvshowDetailsBinding;
import com.example.tv_shows_mvvm.databinding.LayoutEpisodesBottomSheetBinding;
import com.example.tv_shows_mvvm.models.TVShow;
import com.example.tv_shows_mvvm.utilities.TempDataHolder;
import com.example.tv_shows_mvvm.viewmodels.TVShowDetailsViewModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class TVShowDetailsActivity extends AppCompatActivity {

    private ActivityTvshowDetailsBinding activityTvshowDetailsBinding;
    private TVShowDetailsViewModel tvShowDetailsViewModel;

    //BottomSheet的效果是指从屏幕底部向上滑的效果，是MaterialDesign风格的一种,使用起来也很简单。
    private BottomSheetDialog episodesBottomSheetDialog;
    private LayoutEpisodesBottomSheetBinding layoutEpisodesBottomSheetBinding;
    private TVShow tvShow;
    private Boolean isTVShowAvailableInWatchlist = false;

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
        //Android中的Activity传递数据时，为了方便往往将很多数据封装成对象，然后将整个对象传递过去。
        //传对象的时候有两种情况，一种是实现Parcelable接口，一种是实现Serializable接口。
        //可以用bundle putSerializable（Key，Object）传递数据或者直接用intent putExtrr（Key，Object）传递数据。
        tvShow = (TVShow) getIntent().getSerializableExtra("tvShow");
        checkTVShowInWatchlist();
        getTVShowDetails();
    }

    private void checkTVShowInWatchlist() {
        //CompositeDisposable
        //一个disposable的容器，可以容纳多个disposable，添加和去除的复杂度为O(1)
        //add()，将disposable添加到容器中。
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(tvShowDetailsViewModel.getTVShowFromWatchlist(String.valueOf(tvShow.getId()))
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(tvShow -> {
                    isTVShowAvailableInWatchlist = true;
                    activityTvshowDetailsBinding.imageWatchlist.setImageResource(R.drawable.ic_added);
                    compositeDisposable.dispose();
                })
        );
    }


    //实现
    private void getTVShowDetails() {
        activityTvshowDetailsBinding.setIsLoading(true);
        String tvShowId = String.valueOf(tvShow.getId());
        tvShowDetailsViewModel.getTVShowDetails(tvShowId).observe(
                this, tvShowsDetailsResponse -> {
                    activityTvshowDetailsBinding.setIsLoading(false);
                    if (tvShowsDetailsResponse.getTvShowDetails() != null) {
                        if (tvShowsDetailsResponse.getTvShowDetails().getPictures() != null) {
                            loadImageSlider(tvShowsDetailsResponse.getTvShowDetails().getPictures());
                        }
                        activityTvshowDetailsBinding.setTvShowImageURL(
                                tvShowsDetailsResponse.getTvShowDetails().getImage_path()
                        );
                        activityTvshowDetailsBinding.imageTVShow.setVisibility(View.VISIBLE);
                        activityTvshowDetailsBinding.setDescription(
                                String.valueOf(
                                        HtmlCompat.fromHtml(
                                                tvShowsDetailsResponse.getTvShowDetails().getDescription(),
                                                HtmlCompat.FROM_HTML_MODE_LEGACY
                                        )
                                )
                        );
                        activityTvshowDetailsBinding.textDescription.setVisibility(View.VISIBLE);
                        activityTvshowDetailsBinding.textReadMore.setVisibility(View.VISIBLE);
                        //ReadMore触发事件，点击后增加当前TextView的最大显示行数，否则返回至一开始设定的4条
                        //Ellipsize————textview中有个内容过长加省略号的属性
                        activityTvshowDetailsBinding.textReadMore.setOnClickListener(v -> {
                            if (activityTvshowDetailsBinding.textReadMore.getText().toString().equals("Read More")) {
                                activityTvshowDetailsBinding.textDescription.setMaxLines(Integer.MAX_VALUE);
                                activityTvshowDetailsBinding.textDescription.setEllipsize(null);
                                activityTvshowDetailsBinding.textReadMore.setText(R.string.read_less);
                            } else {
                                activityTvshowDetailsBinding.textDescription.setMaxLines(4);
                                //(TextUtils.TruncateAt.END) 设置省略号在结尾
                                activityTvshowDetailsBinding.textDescription.setEllipsize(TextUtils.TruncateAt.END);
                                activityTvshowDetailsBinding.textReadMore.setText(R.string.read_more);
                            }
                        });
                        //绑定对应的信息处理
                        activityTvshowDetailsBinding.setRating(
                                String.format(
                                        //返回默认语言环境
                                        Locale.getDefault(),
                                        "%.2f",
                                        Double.parseDouble(tvShowsDetailsResponse.getTvShowDetails().getRating())
                                )
                        );
                        if (tvShowsDetailsResponse.getTvShowDetails().getGenres() != null) {
                            activityTvshowDetailsBinding.setGenre(tvShowsDetailsResponse.getTvShowDetails().getGenres()[0]);
                        } else {
                            activityTvshowDetailsBinding.setGenre("N/A");
                        }
                        activityTvshowDetailsBinding.setRuntime(tvShowsDetailsResponse.getTvShowDetails().getRuntime() + " Min");
                        activityTvshowDetailsBinding.viewDivider1.setVisibility(View.VISIBLE);
                        activityTvshowDetailsBinding.layoutMisc.setVisibility(View.VISIBLE);
                        activityTvshowDetailsBinding.viewDivider2.setVisibility(View.VISIBLE);

                        //设置点击事件，跳转到对应的url链接
                        activityTvshowDetailsBinding.buttonWebsite.setOnClickListener(v -> {
                            //ACTION_VIEW打开浏览器
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            //通过这个URI可以访问一个网络上或者是本地的资源
                            //Uri.parse()就是将一个网址字符串解析成为一个Uri对象，就是我们所要操作的数据。
                            intent.setData(Uri.parse(tvShowsDetailsResponse.getTvShowDetails().getUrl()));
                            startActivity(intent);
                        });
                        activityTvshowDetailsBinding.buttonWebsite.setVisibility(View.VISIBLE);
                        activityTvshowDetailsBinding.buttonEpisodes.setVisibility(View.VISIBLE);
                        activityTvshowDetailsBinding.buttonEpisodes.setOnClickListener(v -> {
                            if (episodesBottomSheetDialog == null) {
                                episodesBottomSheetDialog = new BottomSheetDialog(TVShowDetailsActivity.this);
                                //我们通常通过 DataBindingUtil.inflate(inflater, R.layout.demo, container, false) 来实例化的 DemoBinding 对象
                                //即 ViewDataBinding。
                                layoutEpisodesBottomSheetBinding = DataBindingUtil.inflate(
                                        LayoutInflater.from(TVShowDetailsActivity.this),
                                        R.layout.layout_episodes_bottom_sheet,
                                        findViewById(R.id.episodesContainer),
                                        false
                                );
                                //设置根视图
                                episodesBottomSheetDialog.setContentView(layoutEpisodesBottomSheetBinding.getRoot());
                                layoutEpisodesBottomSheetBinding.episodesRecylerView.setAdapter(
                                        new EpisodesAdapter(tvShowsDetailsResponse.getTvShowDetails().getEpisodes())
                                );
                                layoutEpisodesBottomSheetBinding.textTitle.setText(
                                        String.format("Episodes | %s", tvShow.getName())
                                );
                                layoutEpisodesBottomSheetBinding.imageClose.setOnClickListener(v1 -> episodesBottomSheetDialog.dismiss());
                            }

                            //FrameLayout是最简单的布局了。所有放在布局里的控件，都按照层次堆叠在屏幕的左上角。后加进来的控件覆盖前面的控件。
                            // ---- Optional section start ---- //
                            FrameLayout frameLayout = episodesBottomSheetDialog.findViewById(
                                    com.google.android.material.R.id.design_bottom_sheet
                            );
                            if (frameLayout != null) {
                                //BottomSheetBehavior使用(底部导航抽屉的实现)
                                BottomSheetBehavior<View> bottomSheetBehavior = BottomSheetBehavior.from(frameLayout);
                                //setPeekHeight() 为其参数获取像素值
                                //esources.getSystem().getDisplayMetrics().heightPixels 获取屏幕高度
                                bottomSheetBehavior.setPeekHeight(Resources.getSystem().getDisplayMetrics().heightPixels);
                                //使用setState()方法从本机方法调用中更新UI
                                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                            }
                            // ---- Optional section end ---- //

                            episodesBottomSheetDialog.show();
                        });


                        //CompositeDisposable
                        //一个disposable的容器，可以容纳多个disposable，添加和去除的复杂度为O(1)
                        //add()，将disposable添加到容器中。
                        activityTvshowDetailsBinding.imageWatchlist.setOnClickListener(v -> {
                            CompositeDisposable compositeDisposable = new CompositeDisposable();
                            //如果当前页面已经在watchlist中，改变图片（已添加）
                            if (isTVShowAvailableInWatchlist) {
                                compositeDisposable.add(tvShowDetailsViewModel.removeTVShowFromWatchlist(tvShow)
                                        .subscribeOn(Schedulers.computation())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(()->{
                                            isTVShowAvailableInWatchlist = false;
                                            TempDataHolder.IS_WATCHLIST_UPDATED = true;
                                            activityTvshowDetailsBinding.imageWatchlist.setImageResource(R.drawable.ic_watchlist);
                                            Toast.makeText(getApplicationContext(),"Removed from watchlist",Toast.LENGTH_SHORT).show();
                                            compositeDisposable.dispose();
                                        })
                                );
                            } else {
                                compositeDisposable.add(tvShowDetailsViewModel.addtoWatchlist(tvShow)
                                        //使用RxJava的subscribeOn和observeOn可以方便地进行线程切换
                                        //subscribeOn只是用来决定在哪个线程订阅，如果订阅之后没有切换线程操作，数据会在当前线程（订阅时的线程）发射
                                        //Schedulers.io()调度器主要用于I/O操作
                                        //它基于根据需要，增长或缩减来自适应的线程池。大量的I/O调度操作将创建许多个线程并占用内存。
                                        //一如既往的是，我们需要在性能和简捷两者之间找到一个有效的平衡点。
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        //Observable发送消息，而Subscriber则用于消费消息。
                                        .subscribe(() -> {
                                            TempDataHolder.IS_WATCHLIST_UPDATED = true;
                                            activityTvshowDetailsBinding.imageWatchlist.setImageResource(R.drawable.ic_added);
                                            Toast.makeText(getApplicationContext(), "Added to watchlist", Toast.LENGTH_SHORT).show();
                                            compositeDisposable.dispose();
                                        })
                                );
                            }


                        });
                        activityTvshowDetailsBinding.imageWatchlist.setVisibility(View.VISIBLE);
                        loadBasicTVShowDetails();
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

    private void loadBasicTVShowDetails() {
        activityTvshowDetailsBinding.setTvShowName(tvShow.getName());
        activityTvshowDetailsBinding.setNetworkCountry(
                tvShow.getNetwork() + "(" +
                        tvShow.getCountry() + ")"
        );
        activityTvshowDetailsBinding.setStatus(tvShow.getStatus());
        activityTvshowDetailsBinding.setStartedDate(tvShow.getStartDate());
        activityTvshowDetailsBinding.textName.setVisibility(View.VISIBLE);
        activityTvshowDetailsBinding.textNetworkCountry.setVisibility(View.VISIBLE);
        activityTvshowDetailsBinding.textStatus.setVisibility(View.VISIBLE);
        activityTvshowDetailsBinding.textStarted.setVisibility(View.VISIBLE);
    }


}