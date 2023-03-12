package com.example.tv_shows_mvvm.repositories;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.tv_shows_mvvm.network.ApiClient;
import com.example.tv_shows_mvvm.network.ApiService;
import com.example.tv_shows_mvvm.responses.TVShowsResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MostPopularTVShowsRepository {

    private ApiService apiService;

    public MostPopularTVShowsRepository(){
        //创建网络请求接口实例
        apiService = ApiClient.getRetrofit().create(ApiService.class);
    }
    /*
    LiveData 是一种可观察的数据存储器类。
    与常规的可观察类不同，LiveData 具有生命周期感知能力，即它遵循其他应用组件（如 Activity/Fragment）的生命周期。
    这种感知能力可确保 LiveData仅更新处于活跃生命周期状态的应用组件观察者。
    实时数据刷新：当组件处于活跃状态或者从不活跃状态到活跃状态时总是能收到最新的数据；
    不会发生内存泄漏：observer会在LifecycleOwner状态变为DESTROYED后自动remove；
    不会因 Activity 处于STOP等状态而导致崩溃：如果LifecycleOwner生命周期处于非活跃状态，则它不会接收任何 LiveData事件；
    不需要手动解除观察：开发者不需要在onPause或onDestroy方法中解除对LiveData的观察，因为LiveData能感知生命周期状态变化，所以会自动管理所有这些操作；
    数据始终保持最新状态：数据更新时，若LifecycleOwner为非活跃状态，那么会在变为活跃时接收最新数据。例如，曾经在后台的 Activity 会在返回前台后，observer立即接收最新的数据等；
     */
    public LiveData<TVShowsResponse> getMostPopularTVShows(int page){
        /*
        1.MutableLiveData的父类是LiveData
        2.LiveData在实体类里可以通知指定某个字段的数据更新.
        3.MutableLiveData则是完全是整个实体类或者数据类型变化后才通知.不会细节到某个字段
        4.LiveData不可变,MutableLiveData是可变的 --
         */
        MutableLiveData<TVShowsResponse>data = new MutableLiveData<>();
        //发起异步请求, 调用Call的enqueue()方法（同步用execute()方法）
        apiService.getMostPopularTVShows(page).enqueue(new Callback<TVShowsResponse>() {
            @Override
            public void onResponse(@NonNull Call<TVShowsResponse> call,@NonNull Response<TVShowsResponse> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<TVShowsResponse> call,@NonNull Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }
}
