package cl.y3rk0d3.itunes_search.di;

import cl.y3rk0d3.itunes_search.ui.main.MainActivity;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
abstract class MainActivityModule {

    @ContributesAndroidInjector(modules = FragmentBuilderMainModule.class)
    abstract MainActivity contributeMainActivity();

}
