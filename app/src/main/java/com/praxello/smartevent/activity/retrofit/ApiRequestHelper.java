package com.praxello.smartevent.activity.retrofit;

import android.text.Html;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.praxello.smartevent.utility.AllKeys;
import com.praxello.smartevent.utility.ConfiUrl;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiRequestHelper {

    public interface OnRequestComplete {
        void onSuccess(Object object);

        void onFailure(String apiResponse);
    }

    private static ApiRequestHelper instance;
    private WRFApp application;
    private WRFService WRFService;
    static Gson gson;

    public static synchronized ApiRequestHelper init(WRFApp application) {
        if (null == instance) {
            instance = new ApiRequestHelper();
            instance.setApplication(application);
            gson = new GsonBuilder()
                    .setLenient()
                    .registerTypeAdapterFactory(new NullStringToEmptyAdapterFactory())
//                    .setExclusionStrategies(new ExclusionStrategy() {
//                        @Override
//                        public boolean shouldSkipField(FieldAttributes f) {
//                            return f.getDeclaringClass().equals(RealmObject.class);
//                        }
//
//                        @Override
//                        public boolean shouldSkipClass(Class<?> clazz) {
//                            return false;
//                        }
//                    })
                    .create();
            instance.createRestAdapter();
        }
        return instance;
    }

    public void uploadimage(Map<String, RequestBody> params, final OnRequestComplete onRequestComplete) {
        Call<ResponseBody> call = WRFService.uploadimage(params);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
//                    Log.e("res", "" + response.code() + "||" + response.message() + "||" + response.body() + "||" + response.headers() + "||" + response.isSuccessful() + "||" + response.raw());
                    if (response.code() == 200 || response.isSuccessful()) {
                        onRequestComplete.onSuccess(null);
                    } else {
                        try {
                            if (response.errorBody() != null && !TextUtils.isEmpty(response.errorBody().toString()))
                                onRequestComplete.onFailure(Html.fromHtml(response.errorBody().string()) + "");
                            else
                                onRequestComplete.onFailure(AllKeys.UNPROPER_RESPONSE);
                        } catch (IOException e) {
                            onRequestComplete.onFailure(AllKeys.UNPROPER_RESPONSE);
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                handle_fail_response(t, onRequestComplete);
            }
        });
    }

    private void handle_fail_response(Throwable t, OnRequestComplete onRequestComplete) {
        if (t.getMessage() != null) {
            if (t.getMessage().contains("Unable to resolve host")) {
                onRequestComplete.onFailure(AllKeys.NO_INTERNET_AVAILABLE);
            } else {
                onRequestComplete.onFailure(Html.fromHtml(t.getLocalizedMessage()) + "");
            }
        } else
            onRequestComplete.onFailure(AllKeys.UNPROPER_RESPONSE);
    }

    private static class NullStringToEmptyAdapterFactory<T> implements TypeAdapterFactory {
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {

            Class<T> rawType = (Class<T>) type.getRawType();
            if (rawType != String.class) {
                return null;
            }
            return (TypeAdapter<T>) new StringAdapter();
        }
    }

    public static class StringAdapter extends TypeAdapter<String> {
        public String read(JsonReader reader) throws IOException {
            if (reader.peek() == JsonToken.NULL) {
                reader.nextNull();
                return "";
            }
            return reader.nextString();
        }

        public void write(JsonWriter writer, String value) throws IOException {
            if (value == null) {
                writer.nullValue();
                return;
            }
            writer.value(value);
        }
    }

    /**
     * REST Adapter Configuration
     */
    private void createRestAdapter() {
//        ObjectMapper objectMapper = new ObjectMapper();
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
// set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.readTimeout(60, TimeUnit.SECONDS);
        httpClient.connectTimeout(60, TimeUnit.SECONDS);
// add your other interceptors …

// add logging as last interceptor
        httpClient.interceptors().add(logging);
        Retrofit.Builder builder =
                new Retrofit.Builder()
                        .baseUrl(ConfiUrl.BASE_URL1)
                        .addConverterFactory(GsonConverterFactory.create(gson));
        Retrofit retrofit = builder.client(httpClient.build()).build();
        WRFService = retrofit.create(WRFService.class);
    }

    /**
     * End REST Adapter Configuration
     */

    public WRFApp getApplication() {
        return application;
    }

    public void setApplication(WRFApp application) {
        this.application = application;
    }

}
