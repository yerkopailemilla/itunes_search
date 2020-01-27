package cl.y3rk0d3.itunes_search.di;

import android.app.Application;

import androidx.room.Room;

import javax.inject.Singleton;

import cl.y3rk0d3.itunes_search.db.ItunesSearchDB;
import cl.y3rk0d3.itunes_search.db.ResultDAO;
import dagger.Module;
import dagger.Provides;

@Module(includes = ViewModelModule.class)
public class ApplicationModule {

    @Singleton
    @Provides
    ItunesSearchDB provideDb(Application app){
        return Room.databaseBuilder(app, ItunesSearchDB.class, "itunes_search.db")
                .fallbackToDestructiveMigration()
                .build();
    }

    @Singleton
    @Provides
    ResultDAO provideResultDao(ItunesSearchDB db){
        return db.resultDAO();
    }

}
