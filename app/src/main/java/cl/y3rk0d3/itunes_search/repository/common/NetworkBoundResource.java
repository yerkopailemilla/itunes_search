package cl.y3rk0d3.itunes_search.repository.common;

import androidx.annotation.MainThread;
import androidx.annotation.WorkerThread;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import java.util.Objects;

import cl.y3rk0d3.itunes_search.AppThreadExecutor;
import cl.y3rk0d3.itunes_search.api.ItunesSearchApiInterceptor;

public abstract class NetworkBoundResource<ResultType, RequestType> {

    private final AppThreadExecutor appExecutors;

    private final MediatorLiveData<Resource<ResultType>> result = new MediatorLiveData<>();

    @MainThread
    public NetworkBoundResource(AppThreadExecutor appExecutors) {
        this.appExecutors = appExecutors;
        result.setValue(Resource.loading(null));
        LiveData<ResultType> dbSource = loadFromDb();
        result.addSource(dbSource, data -> {
            result.removeSource(dbSource);
            if (NetworkBoundResource.this.shouldFetch(data)) {
                NetworkBoundResource.this.fetchFromNetwork(dbSource);
            } else {
                result.addSource(dbSource, (ResultType newData) -> {
                    NetworkBoundResource.this.setValue(Resource.success(newData));
                });
            }
        });
    }

    @MainThread
    private void setValue(Resource<ResultType> newValue) {
        if (!Objects.equals(result.getValue(), newValue)) {
            result.setValue(newValue);
        }
    }


    private void fetchFromNetwork(final LiveData<ResultType> dbSource) {
        LiveData<ItunesSearchApiInterceptor<RequestType>> apiResponse = createCall();
        result.addSource(dbSource, newData -> NetworkBoundResource.this.setValue(Resource.loading(newData)));
        result.addSource(apiResponse, response -> {
            result.removeSource(apiResponse);
            result.removeSource(dbSource);
            if (response.isSuccessful()) {
                appExecutors.diskIO().execute(() -> {
                    NetworkBoundResource.this.saveCallResult(NetworkBoundResource.this.processResponse(response));
                    appExecutors.mainThread().execute(() -> {
                        result.addSource(NetworkBoundResource.this.loadFromDb(), newData ->
                                NetworkBoundResource.this.setValue(Resource.success(newData)));
                    });
                });
            } else {
                onFetchFailed();
                result.addSource(dbSource, newData ->
                        setValue(Resource.error(response.errorMessage, newData)));
            }
        });
    }

    @MainThread
    protected abstract boolean shouldFetch(ResultType data);

    @MainThread
    protected abstract LiveData<ResultType> loadFromDb();

    protected void onFetchFailed() {
    }

    public LiveData<Resource<ResultType>> asLiveData() {
        return result;
    }

    @WorkerThread
    protected RequestType processResponse(ItunesSearchApiInterceptor<RequestType> response) {
        return response.body;
    }

    @WorkerThread
    protected abstract void saveCallResult(RequestType item);

    @MainThread
    protected abstract LiveData<ItunesSearchApiInterceptor<RequestType>> createCall();

}
