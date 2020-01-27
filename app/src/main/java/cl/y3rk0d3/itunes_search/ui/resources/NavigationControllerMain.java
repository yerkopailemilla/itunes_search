package cl.y3rk0d3.itunes_search.ui.resources;

import androidx.fragment.app.FragmentManager;

import javax.inject.Inject;

import cl.y3rk0d3.itunes_search.R;
import cl.y3rk0d3.itunes_search.ui.main.MainActivity;
import cl.y3rk0d3.itunes_search.ui.main.SearchFragment;

public class NavigationControllerMain {

    private final int containerId;
    private final FragmentManager fragmentManager;

    @Inject
    public NavigationControllerMain(MainActivity mainActivity) {
        this.containerId = R.id.container_main_activity;
        this.fragmentManager = mainActivity.getSupportFragmentManager();
    }

    public void navigateToSearchFragment(String query) {
        SearchFragment fragment = SearchFragment.create(query);
        String tag = "artist" + "/" + query;
        fragmentManager.beginTransaction()
                .replace(containerId, fragment, tag)
                .commitAllowingStateLoss();
    }

}
