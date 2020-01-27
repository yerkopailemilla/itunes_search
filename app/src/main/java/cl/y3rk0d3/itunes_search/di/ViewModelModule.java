package cl.y3rk0d3.itunes_search.di;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import cl.y3rk0d3.itunes_search.ui.main.SearchFragmentViewModel;
import cl.y3rk0d3.itunes_search.viewmodel.ItunesViewModelFactory;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(SearchFragmentViewModel.class)
    abstract ViewModel bindSearchFragmentViewModel(SearchFragmentViewModel searchFragmentViewModel);

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(ItunesViewModelFactory factory);

}
