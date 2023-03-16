package com.example.tv_shows_mvvm.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.tv_shows_mvvm.modles.TVShow;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

//Data Access Object，数据访问对象。使用DAO设计模式来封装数据库持久层所有操作(CRUD)
//使低级的数据逻辑和高级的业务分离，达到解耦合的目的。
@Dao

//使用Rxjava访问数据库的优点：
//
//1.随意的线程控制，数据库操作在一个线程，返回数据处理在ui线程
//2.随时订阅和取消订阅，而不必再使用回调函数
//3.对读取的数据用rxjava进行过滤，流式处理
//4.使用sqlbrite可以原生返回rxjava的格式，同时是响应式数据库框架
//（有数据添加和更新时自动调用之前订阅了的读取函数，达到有数据添加自动更新ui的效果，
//同时这个特性没有禁止的方法，只能通过取消订阅停止这个功能，对于有的框架这反而是一种累赘）
public interface TVShowDao {
    //定义接口：（对于update，delete，insert，可以选择void类型，来简化调用代码，但缺少了执行结果判断）
    @Query("SELECT * FROM tvshows")
    Flowable<List<TVShow>> getWatchlist();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    //Completable. 只发射一条完成通知，或者一条异常通知，不能发射数据，其中完成通知与异常通知只能发射一个
    Completable addToWatchlist(TVShow tvShow);

    @Delete
    void removeFromWatchlist(TVShow tvShow);

}
