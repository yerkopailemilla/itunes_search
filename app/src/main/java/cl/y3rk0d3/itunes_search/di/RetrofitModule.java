package cl.y3rk0d3.itunes_search.di;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import cl.y3rk0d3.itunes_search.api.ApiServices;
import cl.y3rk0d3.itunes_search.tools.livedata.LiveDataCallAdapterFactory;
import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class RetrofitModule {

    @Singleton
    @Provides
    GsonConverterFactory provideGsonConverterFactory() {
        return GsonConverterFactory.create();
    }

    @Singleton
    @Provides
    LiveDataCallAdapterFactory provideLiveDataCallAdapterFactory() {
        return new LiveDataCallAdapterFactory();
    }

    @Singleton
    @Provides
    HttpLoggingInterceptor provideHttpLoggingInterceptor() {
        return new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
    }

    @Singleton
    @Provides
    OkHttpClient provideOkHttpClient(HttpLoggingInterceptor interceptor) {
        return new OkHttpClient.Builder()
                .readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .build();
    }

    @Singleton
    @Provides
    Retrofit provideRetrofit(GsonConverterFactory converterFactory, LiveDataCallAdapterFactory liveDataCallAdapterFactory, OkHttpClient client) {
        return new Retrofit.Builder()
                .baseUrl("https://itunes.apple.com/")
                .addConverterFactory(converterFactory)
                .addCallAdapterFactory(liveDataCallAdapterFactory)
                .client(client)
                .build();
    }

    @Singleton
    @Provides
    ApiServices provideMotorDeTareasService(Retrofit retrofit) {
        return retrofit.create(ApiServices.class);
    }

}
