package cl.y3rk0d3.itunes_search.di;

import cl.y3rk0d3.itunes_search.ui.main.SearchFragment;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
abstract class FragmentBuilderMainModule {

    @ContributesAndroidInjector
    abstract SearchFragment contributeSearchFragment();

}
