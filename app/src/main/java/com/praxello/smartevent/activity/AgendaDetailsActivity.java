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
import com.github.jhonnyx2012.horizontalpicker.DatePickerListener;
import com.google.gson.Gson;
import com.praxello.smartevent.adapter.agendaadapter.AgendaDetailsAdapter;
import com.praxello.smartevent.model.agendadetails.AgendaData;
import com.praxello.smartevent.model.agendadetails.AgendaDetailsRespose;
import com.praxello.smartevent.utility.CommonMethods;
import com.praxello.smartevent.utility.AllKeys;
import com.praxello.smartevent.R;
import com.praxello.smartevent.utility.ConfiUrl;
import com.praxello.smartevent.widget.numberpicker.NumberPicker;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.paperdb.Paper;

public class AgendaDetailsActivity extends AppCompatActivity implements DatePickerListener {

    @BindView (R.id.rv_load_agenda_details) RecyclerView rvAgendaDetails;
    public static String TAG="AgendaDetailsActivity";
    public AgendaDetailsAdapter agendaDetailsAdapter;
    @BindView(R.id.ll_nodata) public LinearLayout llNoData;
    @BindView(R.id.ll_nointernet) public LinearLayout llNoInternet;
    @BindView(R.id.ll_noserver) public LinearLayout llNoServerFound;
    //@BindView(R.id.np_picker)
    public NumberPicker npPicker;
    private String[] volumes;
    ArrayList<AgendaData> agendaDataArrayList;

    //@BindView(R.id.datePicker)
    //public HorizontalPicker horizontalPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda_details);
        Paper.init(this);
        ButterKnife.bind(this);

        npPicker= findViewById(R.id.np_picker);
        //basic intialisa1tion...
        initViews();

        //Load data...
        if(CommonMethods.isNetworkAvailable(AgendaDetailsActivity.this)){
            if(agendaDataArrayList!=null){
                agendaDetailsAdapter=new AgendaDetailsAdapter(AgendaDetailsActivity.this,agendaDataArrayList,0);
                rvAgendaDetails.setAdapter(agendaDetailsAdapter);
            }else{
                loadData();
            }
        }else{
            Toast.makeText(this, AllKeys.NO_INTERNET_AVAILABLE, Toast.LENGTH_SHORT).show();
            llNoInternet.setVisibility(View.VISIBLE);
            rvAgendaDetails.setVisibility(View.GONE);
        }

        try{
            volumes = new String[]{"Sat 28 Mar 20", "Sun 29 Mar 20"};
            npPicker.setDisplayedValues(volumes);
            npPicker.setMinValue(0);
            npPicker.setMaxValue(1);
            npPicker.setValue(0);

        }catch(NumberFormatException e){
            e.printStackTrace();
        }

        npPicker.setOnValueChangedListener((picker1, oldVal, newVal) -> {
            //Toast.makeText(this,String.valueOf(picker1), Toast.LENGTH_SHORT).show();
            Log.e(TAG, "onCreate: date picker "+picker1.toString() );
            Log.e(TAG, "onCreate: old value"+oldVal);
            Log.e(TAG, "onCreate: new value "+newVal );
            
            int value=npPicker.getValue();
            if(agendaDataArrayList!=null){
                agendaDetailsAdapter=new AgendaDetailsAdapter(AgendaDetailsActivity.this,agendaDataArrayList,value);
                rvAgendaDetails.setAdapter(agendaDetailsAdapter);
            }
            // Toast.makeText(this,String.valueOf(value), Toast.LENGTH_SHORT).show();
        });
    }



    private void initViews() {
        //Toolbar intialisation...
        Toolbar toolbar=findViewById(R.id.toolbar_agendadetails);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("Agenda");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        // initialize it and attach a listener
        //horizontalPicker.setListener(this).init();
       // horizontalPicker.setDate(new DateTime().withDate(2020,03,28));

        //Recyclerview declaration...
        rvAgendaDetails.setLayoutManager(new LinearLayoutManager(AgendaDetailsActivity.this));

    }

    @Override
    public void onDateSelected(DateTime dateSelected) {
        // log it for demo
       // Log.e("HorizontalPicker", "Selected date is " + dateSelected.toString());
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

        StringRequest stringRequest=new StringRequest(Request.Method.POST, ConfiUrl.AGENDA_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson=new Gson();

                Log.e(TAG,"response"+response);
                AgendaDetailsRespose agendaDetailsRespose=gson.fromJson(response,AgendaDetailsRespose.class);

                if(agendaDetailsRespose.Responsecode.equals("200")){
                    progress.dismiss();
                    if (agendaDetailsRespose.getData() != null || agendaDetailsRespose.getData().size() != 0) {
                        Paper.book().write("agenda", agendaDetailsRespose.getData());
                        agendaDataArrayList = Paper.book().read("agenda");
                        if(agendaDataArrayList!=null){
                            agendaDetailsAdapter=new AgendaDetailsAdapter(AgendaDetailsActivity.this,agendaDataArrayList,0);
                            rvAgendaDetails.setAdapter(agendaDetailsAdapter);
                        }
                    }
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
                Toast.makeText(AgendaDetailsActivity.this, AllKeys.SERVER_MESSAGE, Toast.LENGTH_SHORT).show();
                Log.e(TAG,"server error"+error);
            }
        }) {
                protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                params.put("userid",CommonMethods.getPrefrence(AgendaDetailsActivity.this, AllKeys.USER_ID));
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
