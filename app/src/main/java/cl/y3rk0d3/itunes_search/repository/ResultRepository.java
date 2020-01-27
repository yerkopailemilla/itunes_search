package cl.y3rk0d3.itunes_search.repository;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import cl.y3rk0d3.itunes_search.AppThreadExecutor;
import cl.y3rk0d3.itunes_search.api.ApiServices;
import cl.y3rk0d3.itunes_search.api.ItunesSearchApiInterceptor;
import cl.y3rk0d3.itunes_search.db.ItunesSearchDB;
import cl.y3rk0d3.itunes_search.db.ResultDAO;
import cl.y3rk0d3.itunes_search.entity.Result;
import cl.y3rk0d3.itunes_search.entity.WrapperResult;
import cl.y3rk0d3.itunes_search.repository.common.NetworkBoundResource;
import cl.y3rk0d3.itunes_search.repository.common.Resource;
import cl.y3rk0d3.itunes_search.tools.RateLimiter;

@Singleton
public class ResultRepository {

    private final ItunesSearchDB db;
    private final ResultDAO resultDAO;
    private final ApiServices apiServices;
    private final AppThreadExecutor appExecutors;

    private RateLimiter<String> resultListRateLimit = new RateLimiter<>(10, TimeUnit.MINUTES);

    @Inject
    public ResultRepository(AppThreadExecutor appExecutors, ItunesSearchDB db, ResultDAO resultDAO, ApiServices apiServices) {
        this.db = db;
        this.resultDAO = resultDAO;
        this.apiServices = apiServices;
        this.appExecutors = appExecutors;
    }

    public LiveData<Resource<List<Result>>> loadResults(String artist) {
        return new NetworkBoundResource<List<Result>, WrapperResult>(appExecutors) {

            @Override
            protected boolean shouldFetch(List<Result> data) {
                return data == null || data.isEmpty() || resultListRateLimit.shouldFetch(artist);
            }

            @Override
            protected LiveData<List<Result>> loadFromDb() {
                return resultDAO.loadResults(artist);
            }

            @Override
            protected void saveCallResult(WrapperResult item) {
                resultDAO.insertList(item.results);
            }

            @Override
            protected LiveData<ItunesSearchApiInterceptor<WrapperResult>> createCall() {
                return apiServices.searchResults(artist);
            }

            @Override
            protected void onFetchFailed() {
                resultListRateLimit.reset(artist);
            }

        }.asLiveData();
    }

}
