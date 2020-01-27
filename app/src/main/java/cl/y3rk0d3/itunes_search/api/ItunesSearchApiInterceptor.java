package cl.y3rk0d3.itunes_search.api;

import android.util.Log;

import java.io.IOException;

import retrofit2.Response;

public class ItunesSearchApiInterceptor<T> {

    public final int code;
    public final T body;
    public final String errorMessage;

    public ItunesSearchApiInterceptor(Throwable error) {
        code = 500;
        body = null;
        errorMessage = error.getMessage();
    }

    public ItunesSearchApiInterceptor(Response<T> response) {
        code = response.code();

        if (response.isSuccessful()) {
            body = response.body();
            errorMessage = null;
        } else {
            String message = null;
            if (response.errorBody() != null) {
                try {
                    message = response.errorBody().string();
                } catch (IOException ignored) {
                    Log.d("ANDROID_DEBUG: " + ignored.getMessage(), "Se ha producido un error mientras la respuesta era parseada.");
                }

            }
            if (message == null || message.trim().length() == 0) {
                message = response.message();
            }
            errorMessage = message;
            body = null;
        }
    }

    public boolean isSuccessful() {
        return code >= 200 && code < 300;
    }

}
