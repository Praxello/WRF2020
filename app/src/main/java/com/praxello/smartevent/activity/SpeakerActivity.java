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
import com.google.gson.Gson;
import com.praxello.smartevent.R;
import com.praxello.smartevent.adapter.speakeradapter.SpeakerDetailsAdapter;
import com.praxello.smartevent.model.speaker.SpeakerResponse;
import com.praxello.smartevent.utility.CommonMethods;
import com.praxello.smartevent.utility.ConfiUrl;
import com.praxello.smartevent.utility.Constants;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SpeakerActivity extends AppCompatActivity {

    @BindView(R.id.rv_speaker)
    RecyclerView rvSpeaker;

    @BindView(R.id.ll_nodata) public LinearLayout llNoData;
    @BindView(R.id.ll_nointernet) public LinearLayout llNoInternet;
    @BindView(R.id.ll_noserver) public LinearLayout llNoServerFound;

    public static final String TAG="SpeakerActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speaker);
        ButterKnife.bind(this);

        //Basic intialisation...
        initViews();

        //Load data...
        if(CommonMethods.isNetworkAvailable(SpeakerActivity.this)){
            loadSpeaker();
        }else{
            Toast.makeText(this, Constants.NO_INTERNET_AVAILABLE, Toast.LENGTH_SHORT).show();
            llNoInternet.setVisibility(View.VISIBLE);
            rvSpeaker.setVisibility(View.GONE);
        }
    }

    private void initViews(){
        //Toolbar declarations...
        Toolbar toolbar=findViewById(R.id.toolbar_speaker);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("Speakers");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);

        //Recycler view intialisation...
        rvSpeaker.setLayoutManager(new LinearLayoutManager(SpeakerActivity.this));

    }

    public void loadSpeaker(){
        final ProgressDialog progress=new ProgressDialog(this);
        progress.setMessage("Please wait");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.show();
        StringRequest stringRequest=new StringRequest(Request.Method.POST, ConfiUrl.ALL_SPEAKER_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson=new Gson();

                Log.e(TAG,"response"+response);
                SpeakerResponse speakerResponse=gson.fromJson(response,SpeakerResponse.class);

                if(speakerResponse.Responsecode.equals("200")){
                    progress.dismiss();
                    SpeakerDetailsAdapter speakerDetailsAdapter =new SpeakerDetailsAdapter(SpeakerActivity.this,speakerResponse.Data);
                    rvSpeaker.setAdapter(speakerDetailsAdapter);
                }else{
                    llNoData.setVisibility(View.VISIBLE);
                    rvSpeaker.setVisibility(View.GONE);
                    progress.dismiss();
                    Toast.makeText(SpeakerActivity.this, speakerResponse.Message, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progress.dismiss();
                llNoServerFound.setVisibility(View.VISIBLE);
                rvSpeaker.setVisibility(View.GONE);
                Toast.makeText(SpeakerActivity.this, Constants.SERVER_MESSAGE, Toast.LENGTH_SHORT).show();
                Log.e(TAG,"server error"+error);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params=new HashMap<>();
                params.put("conferenceid","1");
                Log.e(TAG, "getParams: "+params );
                return params;
            }
        };
        RequestQueue mQueue= Volley.newRequestQueue(this);
        mQueue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
    }
}
