package cl.y3rk0d3.itunes_search;

import android.app.Activity;
import android.app.Application;

import javax.inject.Inject;

import cl.y3rk0d3.itunes_search.di.ApplicationInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;

public class ItunesSearchApp extends Application implements HasActivityInjector {

    @Inject
    DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;

    @Override
    public void onCreate() {
        super.onCreate();
        ApplicationInjector.initInjection(this);
    }


    @Override
    public DispatchingAndroidInjector<Activity> activityInjector() {
        return dispatchingAndroidInjector;
    }

}
