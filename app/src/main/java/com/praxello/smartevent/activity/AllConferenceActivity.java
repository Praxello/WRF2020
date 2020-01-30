package com.praxello.smartevent.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.praxello.smartevent.R;
import com.praxello.smartevent.adapter.AllConferenceAdapter;
import com.praxello.smartevent.model.allconference.AllConferenceResponse;
import com.praxello.smartevent.utility.AllKeys;
import com.praxello.smartevent.utility.CommonMethods;
import com.praxello.smartevent.utility.ConfiUrl;

import java.util.ArrayList;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AllConferenceActivity extends AppCompatActivity {

    @BindView(R.id.rv_all_conference)
    RecyclerView rvAllConference;
    @BindView(R.id.ll_nodata) public LinearLayout llNoData;
    @BindView(R.id.ll_nointernet) public LinearLayout llNoInternet;
    @BindView(R.id.ll_noserver) public LinearLayout llNoServerFound;

    private static final String TAG="AllConferenceActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_conference);
        ButterKnife.bind(this);

        //basic intialisation....
        initViews();

        //load Conference data
        if(CommonMethods.isNetworkAvailable(AllConferenceActivity.this)){
            loadData();
        }else{
            Toast.makeText(this, AllKeys.NO_INTERNET_AVAILABLE, Toast.LENGTH_SHORT).show();
            llNoInternet.setVisibility(View.VISIBLE);
            rvAllConference.setVisibility(View.GONE);
        }
    }

    private void initViews(){
        FirebaseInstanceId.getInstance().getToken();

        //Toolbar intialisation...
        Toolbar toolbar=findViewById(R.id.toolbar_allconference);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("Events");
        toolbar.setTitleTextColor(Color.WHITE);
        //toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvAllConference.setLayoutManager(layoutManager);
    }

    private void loadData(){
        final ProgressDialog progress=new ProgressDialog(this);
        progress.setMessage("Please wait");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.show();
        StringRequest stringRequest=new StringRequest(Request.Method.POST, ConfiUrl.ALL_CONFERENCE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson=new Gson();

                Log.e(TAG, "onResponse: "+response );
                AllConferenceResponse allConferenceResponse=gson.fromJson(response,AllConferenceResponse.class);

                if(allConferenceResponse.getResponsecode().equals("200")){
                    progress.dismiss();
                    AllConferenceAdapter allConferenceAdapter=new AllConferenceAdapter(AllConferenceActivity.this,allConferenceResponse.getData());
                    rvAllConference.setAdapter(allConferenceAdapter);
                }else{
                    llNoData.setVisibility(View.VISIBLE);
                    rvAllConference.setVisibility(View.GONE);
                    progress.dismiss();
                    Toast.makeText(AllConferenceActivity.this, allConferenceResponse.Message, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progress.dismiss();
                llNoServerFound.setVisibility(View.VISIBLE);
                rvAllConference.setVisibility(View.GONE);
                Toast.makeText(AllConferenceActivity.this, AllKeys.SERVER_MESSAGE, Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue mQueue= Volley.newRequestQueue(this);
        mQueue.add(stringRequest);
    }
}
