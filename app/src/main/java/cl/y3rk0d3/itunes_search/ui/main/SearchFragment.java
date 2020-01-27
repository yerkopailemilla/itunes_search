package cl.y3rk0d3.itunes_search.ui.main;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import javax.inject.Inject;

import cl.y3rk0d3.itunes_search.R;
import cl.y3rk0d3.itunes_search.binding.fragment.DataBindingFragmentComponent;
import cl.y3rk0d3.itunes_search.databinding.FragmentSearchBinding;
import cl.y3rk0d3.itunes_search.di.Injectable;
import cl.y3rk0d3.itunes_search.entity.Result;
import cl.y3rk0d3.itunes_search.repository.common.Resource;
import cl.y3rk0d3.itunes_search.tools.AutoClearedValue;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment implements Injectable {

    private static final String ARTIST_QUERY = "artist_query";

    @Inject
    ViewModelProvider.Factory viewModelProvider;
    private androidx.databinding.DataBindingComponent dataBindingComponent = new DataBindingFragmentComponent(this);
    private AutoClearedValue<FragmentSearchBinding> binding;
    private AutoClearedValue<ResultListAdapter> adapter;

    private SearchFragmentViewModel searchFragmentViewModel;
    private clickEventListener clickEventListener;

    public static SearchFragment create(String query) {
        SearchFragment searchFragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString(ARTIST_QUERY, query);
        searchFragment.setArguments(args);
        return searchFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentSearchBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_search,
                container, false, dataBindingComponent);
        binding = new AutoClearedValue<>(this, dataBinding);
        return dataBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        searchFragmentViewModel = ViewModelProviders.of(this, viewModelProvider).get(SearchFragmentViewModel.class);
        Bundle args = getArguments();
        if (args != null && args.containsKey(ARTIST_QUERY)) {
            searchFragmentViewModel.setQuery(args.getString(ARTIST_QUERY));
        } else {
            searchFragmentViewModel.setQuery(null);
        }

        initRecyclerView();

        ResultListAdapter rvAdapter = new ResultListAdapter(dataBindingComponent, new ResultListAdapter.ResultClickCallback() {

            @Override
            public void display(Result result) {
                clickEventListener.clickEvent(result);
            }

            @Override
            public void addFavorite() {
                Toast.makeText(getContext(), "Agregado a favorito", Toast.LENGTH_LONG).show();
            }
        });

        binding.get().recyclerViewResult.setAdapter(rvAdapter);
        adapter = new AutoClearedValue<>(this, rvAdapter);

        binding.get().setCallback(() -> searchFragmentViewModel.refresh());
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            clickEventListener = (clickEventListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement onSomeEventListener");
        }
    }

    private void initRecyclerView() {
        searchFragmentViewModel.getResults().observe(this, result -> {
            binding.get().setSearchResource(result);
            adapter.get().replace(result == null ? null : result.data);
            binding.get().executePendingBindings();
        });
    }

    public interface clickEventListener {
        void clickEvent(Result result);
    }

}
