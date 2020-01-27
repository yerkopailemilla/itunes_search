package cl.y3rk0d3.itunes_search.api;

import androidx.lifecycle.LiveData;

import cl.y3rk0d3.itunes_search.entity.WrapperResult;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiServices {

    @GET("search")
    LiveData<ItunesSearchApiInterceptor<WrapperResult>> searchResults(@Query("term") String artist);

}
