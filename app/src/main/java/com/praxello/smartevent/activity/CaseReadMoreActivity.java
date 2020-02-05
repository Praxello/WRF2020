package com.praxello.smartevent.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
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
import com.praxello.smartevent.model.NotificationData;
import com.praxello.smartevent.model.allcases.AllCasesData;
import com.praxello.smartevent.utility.AllKeys;
import com.praxello.smartevent.utility.CommonMethods;
import com.praxello.smartevent.utility.ConfiUrl;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CaseReadMoreActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.tv_title)
    public TextView tvTitle;
    @BindView(R.id.tv_summary)
    public TextView tvSummary;
    @BindView(R.id.webview)
    public WebView webView;
    @BindView(R.id.btndiagnosis)
    public AppCompatButton btnDiagnosis;
    public AllCasesData allCasesData;
    AlertDialog alertDialog;
    public static final String TAG="CaseReadMoreActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_case_read_more);
        ButterKnife.bind(this);

        if(getIntent().getParcelableExtra("data")!=null){
            allCasesData=getIntent().getParcelableExtra("data");
        }

        //basic intialisation...
        initViews();
    }

    public void initViews(){
        Toolbar toolbar=findViewById(R.id.toolbar_readmore);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("Case Description");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);

        //button intialisaition...
        btnDiagnosis.setOnClickListener(this);

        tvTitle.setText(allCasesData.getCaseTitle());
        tvSummary.setText(allCasesData.getCaseDetails());


        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        if(allCasesData.getPdflink()!=null){
            if(allCasesData.getPdflink().contains("pdf")){
                webView.loadUrl("https://docs.google.com/viewer?url="+allCasesData.getPdflink());
            }else{
                webView.loadUrl(allCasesData.getPdflink());
            }
        }

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                //progressBar.setVisibility(ProgressBar.VISIBLE);
                //webview.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onPageCommitVisible(WebView view, String url) {
                super.onPageCommitVisible(view, url);

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                //progressBar.setVisibility(ProgressBar.GONE);
                //webview.setVisibility(View.VISIBLE);
            }
        });

    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btndiagnosis:
                AlertDialog.Builder builder = new AlertDialog.Builder(CaseReadMoreActivity.this);
                //builder.setCancelable(false);
                ViewGroup viewGroup = findViewById(android.R.id.content);
                View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.bottom_sheet_row, viewGroup, false);

                EditText etSuggesstion=dialogView.findViewById(R.id.et_suggestion);
                AppCompatButton btnSubmitSuggesstion=dialogView.findViewById(R.id.btn_submit);

                if(allCasesData.getSubmission()==null || allCasesData.getSubmission().isEmpty() ){
                    etSuggesstion.setText(null);

                }else{
                    etSuggesstion.setText(allCasesData.getSubmission());
                }

                btnSubmitSuggesstion.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(etSuggesstion.getText().toString().isEmpty()){
                            Toast.makeText(CaseReadMoreActivity.this, "Suggesstion required!", Toast.LENGTH_LONG).show();
                        }else{
                            submitSuggesstion(etSuggesstion.getText().toString(),allCasesData.getCaseId());
                        }
                    }
                });

                builder.setView(dialogView);
                alertDialog = builder.create();
                alertDialog.show();
                break;
        }
    }

    public void submitSuggesstion(String suggesstion,String caseId){
        final ProgressDialog progress = new ProgressDialog(CaseReadMoreActivity.this);
        progress.setMessage("Please wait");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.show();
        progress.setCancelable(false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ConfiUrl.SAVE_CASE_DIAGNOSIS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();

                NotificationData notificationData = gson.fromJson(response, NotificationData.class);

                Log.e(TAG, "onResponse: "+response );
                if (notificationData.getResponsecode().equals("200")) {
                    progress.dismiss();
                    Toast.makeText(CaseReadMoreActivity.this, notificationData.getMessage(), Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                } else {
                    progress.dismiss();
                    Toast.makeText(CaseReadMoreActivity.this, notificationData.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progress.dismiss();
                Log.e(TAG, "onErrorResponse: " + error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("userid", CommonMethods.getPrefrence(CaseReadMoreActivity.this, AllKeys.USER_ID));
                params.put("caseid", caseId);
                params.put("details", suggesstion);
                Log.e(TAG, "getParams: " + params);
                return params;
            }
        };
        RequestQueue mQueue = Volley.newRequestQueue(CaseReadMoreActivity.this);
        mQueue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
    }
}
