package com.praxello.smartevent.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
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
import com.praxello.smartevent.utility.CommonMethods;
import com.praxello.smartevent.utility.AllKeys;
import com.praxello.smartevent.R;
import com.praxello.smartevent.adapter.CaseDescriptionAdapter;
import com.praxello.smartevent.model.allcases.AllCases;
import com.praxello.smartevent.utility.ConfiUrl;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CaseDescriptionActivity extends AppCompatActivity {

    @BindView (R.id.rv_load_case_description) RecyclerView rvCaseDescription;
    public CaseDescriptionAdapter caseDescriptionAdapter;
    public static String TAG="CaseDescriptionActivity";
    @BindView(R.id.ll_nodata) public LinearLayout llNoData;
    @BindView(R.id.ll_nointernet) public LinearLayout llNoInternet;
    @BindView(R.id.ll_noserver) public LinearLayout llNoServerFound;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_case_description);
        ButterKnife.bind(this);

        //basic intialisation...
        initViews();

        //Load data...
        if(CommonMethods.isNetworkAvailable(CaseDescriptionActivity.this)){
            loadData();

        }else{
            Toast.makeText(this, AllKeys.NO_INTERNET_AVAILABLE, Toast.LENGTH_SHORT).show();
            llNoInternet.setVisibility(View.VISIBLE);
            rvCaseDescription.setVisibility(View.GONE);
        }

    }

    private void initViews() {
        toolbar=findViewById(R.id.toolbar_casedescription);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("Case Description");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);

        //Recyclerview declaration...
        rvCaseDescription=findViewById(R.id.rv_load_case_description);

        /*SnapHelper mSnapHelper = new PagerSnapHelper();
        mSnapHelper.attachToRecyclerView(rvCaseDescription);*/

        //rvCaseDescription.setLayoutManager(new LinearLayoutManager(CaseDescriptionActivity.this,LinearLayoutManager.HORIZONTAL,true));
        LinearLayoutManager layoutManager = new LinearLayoutManager(CaseDescriptionActivity.this);
        rvCaseDescription.setLayoutManager(layoutManager);

        //Linear Layout intialisation...
        llNoData=findViewById(R.id.ll_nodata);
        llNoServerFound=findViewById(R.id.ll_noserver);
        llNoInternet=findViewById(R.id.ll_nointernet);


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadData() {
        final ProgressDialog progress=new ProgressDialog(this);
        progress.setMessage("Please wait");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.show();
        progress.setCancelable(false);
        StringRequest stringRequest=new StringRequest(Request.Method.POST, ConfiUrl.ALL_CASES_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson=new Gson();

                AllCases allCasesResponse=gson.fromJson(response,AllCases.class);

                //Log.e(TAG, "onResponse: "+response );
                if(allCasesResponse.Responsecode.equals("200")){
                    progress.dismiss();
                    caseDescriptionAdapter=new CaseDescriptionAdapter(CaseDescriptionActivity.this,allCasesResponse.Data);
                    rvCaseDescription.setAdapter(caseDescriptionAdapter);

                }else{
                    llNoData.setVisibility(View.VISIBLE);
                    rvCaseDescription.setVisibility(View.GONE);
                    progress.dismiss();
                    Toast.makeText(CaseDescriptionActivity.this, allCasesResponse.Message, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progress.dismiss();
                llNoServerFound.setVisibility(View.VISIBLE);
                rvCaseDescription.setVisibility(View.GONE);
                Toast.makeText(CaseDescriptionActivity.this, AllKeys.SERVER_MESSAGE, Toast.LENGTH_SHORT).show();
                //Log.e(TAG,"server error"+error);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params=new HashMap<>();
                params.put("userid",CommonMethods.getPrefrence(CaseDescriptionActivity.this, AllKeys.USER_ID));
                //Log.e(TAG, "getParams: "+params );
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
