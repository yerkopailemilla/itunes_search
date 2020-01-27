package cl.y3rk0d3.itunes_search.di;

import android.app.Application;

import javax.inject.Singleton;

import cl.y3rk0d3.itunes_search.ItunesSearchApp;
import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;

@Singleton
@Component(modules = {AndroidInjectionModule.class,
        ApplicationModule.class,
        RetrofitModule.class,
        MainActivityModule.class})
interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);

        AppComponent build();
    }

    void inject(ItunesSearchApp itunesSearchApp);

}

