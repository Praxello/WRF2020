package com.praxello.smartevent.retrofit;

import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;

public interface WRFService {

    @Multipart
    @POST("uploadimage.php")
    Call<ResponseBody> uploadimage(@PartMap Map<String, RequestBody> params);
}
