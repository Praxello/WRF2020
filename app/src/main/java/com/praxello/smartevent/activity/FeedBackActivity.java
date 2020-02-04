package com.praxello.smartevent.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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
import com.praxello.smartevent.model.CommentDeleteResponse;
import com.praxello.smartevent.utility.AllKeys;
import com.praxello.smartevent.utility.CommonMethods;
import com.praxello.smartevent.utility.ConfiUrl;
import com.willy.ratingbar.ScaleRatingBar;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FeedBackActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.crb1)
    public ScaleRatingBar scaleRatingBar1;
    @BindView(R.id.crb2)
    public ScaleRatingBar scaleRatingBar2;
    @BindView(R.id.crb3)
    public ScaleRatingBar scaleRatingBar3;
    @BindView(R.id.crb4)
    public ScaleRatingBar scaleRatingBar4;
    @BindView(R.id.etFeedback)
    public EditText etFeedback;
    @BindView(R.id.btn_submit_feedback)
    public AppCompatButton btnSubmitFeedBack;
    private static final String TAG="FeedBackActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);
        ButterKnife.bind(FeedBackActivity.this);
        //basic intialisation....
        initViews();

    }

    private void initViews(){
        //Toolbar intialisation...
        Toolbar toolbar=findViewById(R.id.ll_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("Feedback");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);

        //button intialisation..
        btnSubmitFeedBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_submit_feedback:
                submitFeedback();
                break;
        }
    }

    private void submitFeedback(){
        StringRequest stringRequest=new StringRequest(Request.Method.POST, ConfiUrl.SAVE_FEEDBACK_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Gson gson=new Gson();
                CommentDeleteResponse feedbackResponse=gson.fromJson(response,CommentDeleteResponse.class);

                Log.e(TAG, "onResponse: "+response );

                if(feedbackResponse.getResponsecode().equals("200")){

                    Toast.makeText(FeedBackActivity.this, feedbackResponse.Message, Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(FeedBackActivity.this,DashBoardActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(FeedBackActivity.this, feedbackResponse.Message, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "onErrorResponse: "+error );
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params=new HashMap<>();
                params.put("customerid", CommonMethods.getPrefrence(FeedBackActivity.this, AllKeys.USER_ID));
                params.put("rate1", String.valueOf(scaleRatingBar1.getRating()));
                params.put("rate2",String.valueOf(scaleRatingBar2.getRating()));
                params.put("rate3",String.valueOf(scaleRatingBar3.getRating()));
                params.put("rate4",String.valueOf(scaleRatingBar4.getRating()));
                params.put("feedback",etFeedback.getText().toString());

                Log.e(TAG, "getParams: "+params );

                return params;
            }
        };
        RequestQueue mQueue= Volley.newRequestQueue(FeedBackActivity.this);
        mQueue.add(stringRequest);
    }
}
