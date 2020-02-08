package com.praxello.smartevent.activity.quiz;

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
import com.praxello.smartevent.R;
import com.praxello.smartevent.model.score.ScoreResponse;
import com.praxello.smartevent.utility.AllKeys;
import com.praxello.smartevent.utility.CommonMethods;
import com.praxello.smartevent.utility.ConfiUrl;
import java.util.HashMap;
import java.util.Map;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ScoreBoardActivity extends AppCompatActivity {

    @BindView(R.id.rv_scoreboard)
    RecyclerView rvScoreBoard;
    @BindView(R.id.ll_nodata)
    public LinearLayout llNoData;
    @BindView(R.id.ll_nointernet)
    public LinearLayout llNoInternet;
    @BindView(R.id.ll_noserver)
    public LinearLayout llNoServerFound;
    private static final String TAG = "ScoreBoardActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_board);
        ButterKnife.bind(this);

        //basic intialisation...
        initViews();

        //load score details..
        if(CommonMethods.isNetworkAvailable(ScoreBoardActivity.this)){
            loadData();

        }else{
            Toast.makeText(this, AllKeys.NO_INTERNET_AVAILABLE, Toast.LENGTH_SHORT).show();
            llNoInternet.setVisibility(View.VISIBLE);
            rvScoreBoard.setVisibility(View.GONE);
        }
    }

    private void initViews(){

        //Toolbar intialisation...
        Toolbar toolbar = findViewById(R.id.toolbar_scoreboard);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("Score Board");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvScoreBoard.setLayoutManager(layoutManager);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadData() {
        final ProgressDialog progress = new ProgressDialog(this);
        progress.setMessage("Please wait");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.show();
        progress.setCancelable(false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ConfiUrl.SCORES_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();

               // Log.e(TAG, "onResponse: " + response);
                ScoreResponse scoreResponse= gson.fromJson(response, ScoreResponse.class);

                if (scoreResponse.getResponsecode().equals("200")) {
                    progress.dismiss();
                    ScoreListAdapter scoreListAdapter = new ScoreListAdapter(ScoreBoardActivity.this, scoreResponse.getScores());
                    rvScoreBoard.setAdapter(scoreListAdapter);
                } else {
                    llNoData.setVisibility(View.VISIBLE);
                    rvScoreBoard.setVisibility(View.GONE);
                    progress.dismiss();
                    Toast.makeText(ScoreBoardActivity.this, scoreResponse.Message, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progress.dismiss();
                llNoServerFound.setVisibility(View.VISIBLE);
                rvScoreBoard.setVisibility(View.GONE);
                Toast.makeText(ScoreBoardActivity.this, AllKeys.SERVER_MESSAGE, Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params=new HashMap<>();
                params.put("userid", CommonMethods.getPrefrence(ScoreBoardActivity.this,AllKeys.USER_ID));

                return params;
            }
        };
        RequestQueue mQueue = Volley.newRequestQueue(this);
        mQueue.add(stringRequest);
    }

}
