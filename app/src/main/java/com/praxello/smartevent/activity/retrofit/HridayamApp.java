package com.praxello.smartevent.activity.retrofit;

import android.app.Application;
import android.content.Context;

public class HridayamApp extends Application {

    private ApiRequestHelper apiRequestHelper;
    Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        doInit();

    }

    private void doInit() {

        this.apiRequestHelper = ApiRequestHelper.init(this);
    }

    public synchronized ApiRequestHelper getApiRequestHelper() {
        return apiRequestHelper;
    }
}

