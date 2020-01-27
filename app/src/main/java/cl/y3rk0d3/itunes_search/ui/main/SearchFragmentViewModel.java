package cl.y3rk0d3.itunes_search.ui.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import cl.y3rk0d3.itunes_search.entity.Result;
import cl.y3rk0d3.itunes_search.repository.ResultRepository;
import cl.y3rk0d3.itunes_search.repository.common.Resource;
import cl.y3rk0d3.itunes_search.tools.livedata.AbsentLiveData;

public class SearchFragmentViewModel extends ViewModel {

    private final MutableLiveData<String> query;
    private final LiveData<Resource<List<Result>>> results;

    @Inject
    SearchFragmentViewModel(ResultRepository repository) {
        query = new MutableLiveData<>();
        results = Transformations.switchMap(query, search -> {
            if (search == null || search.trim().length() == 0) {
                return AbsentLiveData.create();
            } else {
                return repository.loadResults(search);
            }
        });
    }

    public LiveData<Resource<List<Result>>> getResults() {
        return results;
    }

    public void setQuery(String originalInput) {
        String input = new String(originalInput);
        if (Objects.equals(input, query.getValue())) {
            return;
        }
        query.setValue(input);
    }

    void refresh() {
        if (query.getValue() != null) {
            query.setValue(query.getValue());
        }
    }

}
