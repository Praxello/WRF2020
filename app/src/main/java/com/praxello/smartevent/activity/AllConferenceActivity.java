package com.praxello.smartevent.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.Manifest;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.praxello.smartevent.R;
import com.praxello.smartevent.adapter.AllConferenceAdapter;
import com.praxello.smartevent.model.allconference.AllConferenceResponse;
import com.praxello.smartevent.utility.AllKeys;
import com.praxello.smartevent.utility.CommonMethods;
import com.praxello.smartevent.utility.ConfiUrl;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

public class AllConferenceActivity extends AppCompatActivity {

    @BindView(R.id.rv_all_conference)
    RecyclerView rvAllConference;
    @BindView(R.id.ll_nodata)
    public LinearLayout llNoData;
    @BindView(R.id.ll_nointernet)
    public LinearLayout llNoInternet;
    @BindView(R.id.ll_noserver)
    public LinearLayout llNoServerFound;
    private static final String TAG = "AllConferenceActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_conference);
        ButterKnife.bind(this);

        //basic intialisation....
        initViews();

        requestPermissions();

        //load Conference data
        if (CommonMethods.isNetworkAvailable(AllConferenceActivity.this)) {
            loadData();
        } else {
            Toast.makeText(this, AllKeys.NO_INTERNET_AVAILABLE, Toast.LENGTH_SHORT).show();
            llNoInternet.setVisibility(View.VISIBLE);
            rvAllConference.setVisibility(View.GONE);
        }
    }

    private void initViews() {
        FirebaseInstanceId.getInstance().getToken();

        //Toolbar intialisation...
        Toolbar toolbar = findViewById(R.id.toolbar_allconference);
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



    private void loadData() {
        final ProgressDialog progress = new ProgressDialog(this);
        progress.setMessage("Please wait");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.show();
        progress.setCancelable(false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ConfiUrl.ALL_CONFERENCE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();

                //Log.e(TAG, "onResponse: " + response);
                AllConferenceResponse allConferenceResponse = gson.fromJson(response, AllConferenceResponse.class);

                if (allConferenceResponse.getResponsecode().equals("200")) {
                    progress.dismiss();
                    if(allConferenceResponse.getData()!=null){
                        AllConferenceAdapter allConferenceAdapter = new AllConferenceAdapter(AllConferenceActivity.this, allConferenceResponse.getData());
                        rvAllConference.setAdapter(allConferenceAdapter);
                    }else{
                        llNoData.setVisibility(View.VISIBLE);
                        rvAllConference.setVisibility(View.GONE);
                        progress.dismiss();
                    }

                } else {
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
        RequestQueue mQueue = Volley.newRequestQueue(this);
        mQueue.add(stringRequest);
    }

    private void requestPermissions() {
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            // Toast.makeText(getApplicationContext(), "All permissions are granted by user!", Toast.LENGTH_SHORT).show();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            //openSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getApplicationContext(), "Some Error! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }
}
