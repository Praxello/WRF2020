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
import com.praxello.smartevent.adapter.agendaadapter.AgendaDetailsAdapter;
import com.praxello.smartevent.model.agendadetails.AgendaDetailsRespose;
import com.praxello.smartevent.utility.CommonMethods;
import com.praxello.smartevent.utility.Constants;
import com.praxello.smartevent.R;
import com.praxello.smartevent.utility.ConfiUrl;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AgendaDetailsActivity extends AppCompatActivity {

    @BindView (R.id.rv_load_agenda_details) RecyclerView rvAgendaDetails;
    public static String TAG="AgendaDetailsActivity";
    public AgendaDetailsAdapter agendaDetailsAdapter;
    @BindView(R.id.ll_nodata) public LinearLayout llNoData;
    @BindView(R.id.ll_nointernet) public LinearLayout llNoInternet;
    @BindView(R.id.ll_noserver) public LinearLayout llNoServerFound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda_details);
        ButterKnife.bind(this);
        //basic intialisation...
        initViews();

        //Load data...
        if(CommonMethods.isNetworkAvailable(AgendaDetailsActivity.this)){
            loadData();
        }else{
            Toast.makeText(this, Constants.NO_INTERNET_AVAILABLE, Toast.LENGTH_SHORT).show();
            llNoInternet.setVisibility(View.VISIBLE);
            rvAgendaDetails.setVisibility(View.GONE);
        }
    }



    private void initViews() {
        Toolbar toolbar=findViewById(R.id.toolbar_agendadetails);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("Agenda Details");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);

        //Recyclerview declaration...
        rvAgendaDetails.setLayoutManager(new LinearLayoutManager(AgendaDetailsActivity.this));
    }

    private void loadData() {
        final ProgressDialog progress=new ProgressDialog(this);
        progress.setMessage("Please wait");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.show();
        StringRequest stringRequest=new StringRequest(Request.Method.POST, ConfiUrl.AGENDA_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson=new Gson();

                Log.e(TAG,"response"+response);
                AgendaDetailsRespose agendaDetailsRespose=gson.fromJson(response,AgendaDetailsRespose.class);

                if(agendaDetailsRespose.Responsecode.equals("200")){
                    progress.dismiss();
                    agendaDetailsAdapter=new AgendaDetailsAdapter(AgendaDetailsActivity.this,agendaDetailsRespose.Data);
                    rvAgendaDetails.setAdapter(agendaDetailsAdapter);
                }else{
                    llNoData.setVisibility(View.VISIBLE);
                    rvAgendaDetails.setVisibility(View.GONE);
                    progress.dismiss();
                    Toast.makeText(AgendaDetailsActivity.this, agendaDetailsRespose.Message, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progress.dismiss();
                llNoServerFound.setVisibility(View.VISIBLE);
                rvAgendaDetails.setVisibility(View.GONE);
                Toast.makeText(AgendaDetailsActivity.this, Constants.SERVER_MESSAGE, Toast.LENGTH_SHORT).show();
                Log.e(TAG,"server error"+error);
            }
        }) {
                protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                params.put("userid",CommonMethods.getPrefrence(AgendaDetailsActivity.this,Constants.USER_ID));
                Log.e(TAG, "getParams: " + params);
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
