package com.praxello.smartevent.activity.retrofit;

import com.praxello.smartevent.model.quiz.QuestionData;
import com.praxello.smartevent.model.quiz.UserData;

import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.QueryMap;

public interface WRFService {

    @Multipart
    @POST("sem/uploadimage.php")
    Call<ResponseBody> uploadimage(@PartMap Map<String, RequestBody> params);

    @GET("sem/user/allquestions.php")
    Call<QuestionData> allquestions(@QueryMap Map<String, String> params);

    //@POST("sem/user/savequiz.php")
    //Call<UserData> savequiz(@QueryMap Map<String, String> params);

    @FormUrlEncoded
    @POST("sem/user/savequiz.php")
    Call<UserData> savequiz(@Field("userid") String userid,@Field("score") String score);
}
