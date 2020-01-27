package cl.y3rk0d3.itunes_search.tools.livedata;

import androidx.lifecycle.LiveData;

import java.lang.reflect.Type;
import java.util.concurrent.atomic.AtomicBoolean;

import cl.y3rk0d3.itunes_search.api.ItunesSearchApiInterceptor;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Callback;
import retrofit2.Response;

public class LiveDataCallAdapter<R> implements CallAdapter<R, LiveData<ItunesSearchApiInterceptor<R>>> {

    private final Type responseType;

    public LiveDataCallAdapter(Type responseType) {
        this.responseType = responseType;
    }

    @Override
    public Type responseType() {
        return responseType;
    }

    @Override
    public LiveData<ItunesSearchApiInterceptor<R>> adapt(Call<R> call) {
        return new LiveData<ItunesSearchApiInterceptor<R>>() {
            AtomicBoolean started = new AtomicBoolean(false);

            @Override
            protected void onActive() {
                super.onActive();
                if (started.compareAndSet(false, true)) {
                    call.enqueue(new Callback<R>() {
                        @Override
                        public void onResponse(Call<R> call, Response<R> response) {
                            postValue(new ItunesSearchApiInterceptor<R>(response));
                        }

                        @Override
                        public void onFailure(Call<R> call, Throwable t) {
                            postValue(new ItunesSearchApiInterceptor<R>(t));
                        }
                    });
                }
            }
        };
    }

}
