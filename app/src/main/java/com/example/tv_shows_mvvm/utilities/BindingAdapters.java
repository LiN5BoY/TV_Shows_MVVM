package com.example.tv_shows_mvvm.utilities;

import android.media.Image;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class BindingAdapters {
    //BindingAdapter 与 LiveData双向绑定
    @BindingAdapter("android:imageURL")
    public static void setImageURL(ImageView imageView,String URL){
        try{
            imageView.setAlpha(0f);
            //加载网络图片是一个常见的需求，而网上也有很多加载图片的框架，
            //使用Picasso框架加载图片
            Picasso.get().load(URL).noFade().into(imageView, new Callback() {
                @Override
                public void onSuccess() {
                    imageView.animate().setDuration(300).alpha(1f).start();
                }

                @Override
                public void onError(Exception e) {

                }
            });
        }catch(Exception ignored){

        }
    }
}
