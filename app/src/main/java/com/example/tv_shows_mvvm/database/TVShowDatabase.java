package com.example.tv_shows_mvvm.database;

import android.content.Context;


//处理大量结构化数据的应用可极大地受益于在本地保留这些数据。最常见的使用场景是缓存相关的数据，这样一来，当设备无法访问网络时，用户仍然可以在离线状态下浏览该内容。
//Room 持久性库在 SQLite 上提供了一个抽象层，以便在充分利用 SQLite 的强大功能的同时，能够流畅地访问数据库。具体来说，Room 具有以下优势：
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.tv_shows_mvvm.dao.TVShowDao;
import com.example.tv_shows_mvvm.models.TVShow;

//@Database 注解的类必须是扩展 RoomDatabase 的抽象类；
//包含具有 0 个参数且返回使用 @Dao 注释的类的抽象方法；
//可以通过调用 Room.databaseBuilder() 或 Room.inMemoryDatabaseBuilder() 获取 Database 的实例；
//注解包含列出所有与数据库关联的数据实体的 entities 数组。
@Database(entities = TVShow.class,version = 1,exportSchema = false)
public abstract class TVShowDatabase extends RoomDatabase {

    private static TVShowDatabase tvShowDatabase;
    //synchronized
    //这时,线程获得的是成员锁,即一次只能有一个线程进入该方法
    // 其他线程要想在此时调用该方法,只能排队等候,当前线程(就是在synchronized方法内部的线程)执行完该方法后,别的线程才能进入.
    public static synchronized TVShowDatabase getTvShowDatabase(Context context){
        if(tvShowDatabase == null){
            //Database的初始化需要先使用Room.databaseBuilder方法，这个方法几个参数：
            //参数一：context，我们可以使用Application
            //参数二：Class，即我们定义的Database的class
            //参数三：name, 这个参数表示的就是数据库的名字
            tvShowDatabase = Room.databaseBuilder(
                    context,
                    TVShowDatabase.class,
                    "tv_shows_db"
            ).build();
        }
        return  tvShowDatabase;
    }

    public abstract TVShowDao tvShowDao();

}
