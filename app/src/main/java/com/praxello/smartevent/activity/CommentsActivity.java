package com.praxello.smartevent.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.praxello.smartevent.R;
import com.praxello.smartevent.adapter.CommentsAdapter;
import com.praxello.smartevent.model.agendadetails.AgendaData;
import com.praxello.smartevent.model.comments.LatestCommentData;
import com.praxello.smartevent.model.comments.CommentsResponse;
import com.praxello.smartevent.model.comments.LoadPreviousCommentResponse;
import com.praxello.smartevent.utility.CommonMethods;
import com.praxello.smartevent.utility.ConfiUrl;
import com.praxello.smartevent.utility.AllKeys;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import butterknife.BindView;
import butterknife.ButterKnife;

public class CommentsActivity extends AppCompatActivity {

    public static final String TAG="CommentsActivity";

    @BindView(R.id.et_comments)
    EditText etComments;
    @BindView(R.id.iv_submitcomment)
    ImageView ivSubmitComment;
    @BindView(R.id.rv_comments)
    RecyclerView rvComments;
    AgendaData data;
    @BindView(R.id.ll_nodata) public LinearLayout llNoData;
    @BindView(R.id.ll_nointernet) public LinearLayout llNoInternet;
    @BindView(R.id.ll_noserver) public LinearLayout llNoServerFound;
    @BindView(R.id.rl_background)
    public RelativeLayout rlBackground;
    @BindView(R.id.ivbackgroundimg)
    public ImageView ivBackgroundImage;
    private ArrayList<LatestCommentData> latestCommentDataArrayList;
    private Boolean isFirstLaunched=false;
    CommentsAdapter commentsAdapter;
    @BindView(R.id.nestedScrollView)
    public NestedScrollView nestedScrollView;
    private int totalSizeOfList=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        ButterKnife.bind(this);

        //Getting Data from intent using parcelable...
        Intent intent=getIntent();
        data=intent.getParcelableExtra("data");
        //speakersName=intent.getParcelableExtra("speaker_data");


        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        //layoutManager.setReverseLayout(false);

        rvComments.setLayoutManager(layoutManager);
        rvComments.setNestedScrollingEnabled(false);


        Toolbar toolbar=findViewById(R.id.toolbar_comments);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("Session Discussion");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);

        if(CommonMethods.isNetworkAvailable(CommentsActivity.this)){
            loadComments();
        }

        if(!CommonMethods.getPrefrence(CommentsActivity.this,AllKeys.CONFERENCE_LOGO_URL).equals(AllKeys.DNF)){
            Glide.with(CommentsActivity.this).load(CommonMethods.getPrefrence(CommentsActivity.this,AllKeys.CONFERENCE_LOGO_URL)).into(ivBackgroundImage);
        }


        final Handler ha=new Handler();
        ha.postDelayed(new Runnable() {

            @Override
            public void run() {
                //call function
                if(CommonMethods.isNetworkAvailable(CommentsActivity.this)){

                        loadComments();

                }

                ha.postDelayed(this, 3000);
            }
        }, 3000);



        ivSubmitComment.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if(isValidated()){
                   addComment();
               }
           }
       });
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

    public void loadComments(){
       StringRequest stringRequest=new StringRequest(Request.Method.POST, ConfiUrl.ALL_COMMENTS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson=new Gson();

                Log.e(TAG, "onResponse: "+response );
                LoadPreviousCommentResponse loadPreviousCommentResponse=gson.fromJson(response,LoadPreviousCommentResponse.class);

                if(loadPreviousCommentResponse.Responsecode.equals("200")){
                    //progress.dismiss();
                    if(loadPreviousCommentResponse.getData()!=null){
                        commentsAdapter=new CommentsAdapter(CommentsActivity.this,loadPreviousCommentResponse.getData(),data);
                        //commentsAdapter.notifyDataSetChanged();
                        rvComments.setAdapter(commentsAdapter);


                        if(!isFirstLaunched || totalSizeOfList != loadPreviousCommentResponse.getData().size())
                        {
                            totalSizeOfList=loadPreviousCommentResponse.getData().size();
                            isFirstLaunched = true;
                            nestedScrollView.post(new Runnable() {
                                @Override
                                public void run() {
                                    //nestedScrollView.scrollTo(0,0);
                                    nestedScrollView.fullScroll(View.FOCUS_DOWN);
                                    //rvComments.scrollToPosition(commentsAdapter.getItemCount()-1);
                                }
                            });

                        }
                    }else{
                        //llNoData.setVisibility(View.VISIBLE);
                        //rvComments.setVisibility(View.GONE);
                       // progress.dismiss();
                    }
                }else{
                    llNoData.setVisibility(View.VISIBLE);
                    rvComments.setVisibility(View.GONE);
                   // progress.dismiss();
                    Toast.makeText(CommentsActivity.this, loadPreviousCommentResponse.Message, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //progress.dismiss();
                //llNoServerFound.setVisibility(View.VISIBLE);
                //rvComments.setVisibility(View.GONE);
                Toast.makeText(CommentsActivity.this, AllKeys.SERVER_MESSAGE, Toast.LENGTH_SHORT).show();
                Log.e(TAG,"server error"+error);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params=new HashMap<>();
                params.put("sessionid",data.getSessionId());
                Log.e(TAG, "getParams: "+params );
                return params;
            }
        };
        RequestQueue mQueue= Volley.newRequestQueue(this);
        mQueue.add(stringRequest);
    }

    public void addComment(){
        StringRequest stringRequest=new StringRequest(Request.Method.POST, ConfiUrl.ADD_COMMENT_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG,"response"+response);
                Gson gson=new Gson();
                CommentsResponse commentsResponse=gson.fromJson(response,CommentsResponse.class);

                if(commentsResponse.getResponsecode().equals("200")){
                    llNoData.setVisibility(View.GONE);
                    rvComments.setVisibility(View.VISIBLE);
                    etComments.setText(null);
                    Toast.makeText(CommentsActivity.this, commentsResponse.getMessage(), Toast.LENGTH_SHORT).show();
                   // loadComments();

                    //hide keyboard when success...
                    hideKeyboard(CommentsActivity.this);
                }else{
                    Toast.makeText(CommentsActivity.this, commentsResponse.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG,"error"+error);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params=new HashMap<>();
                params.put("userid", CommonMethods.getPrefrence(CommentsActivity.this, AllKeys.USER_ID));
                params.put("sessionId",data.getSessionId());
                params.put("comment",etComments.getText().toString());
                params.put("postid",data.getSessionId());

                Log.e(TAG,"params"+params);
                return params;
            }
        };
        RequestQueue mQueue= Volley.newRequestQueue(this);
        mQueue.add(stringRequest);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Toast.makeText(this, "onResume()", Toast.LENGTH_SHORT).show();
        Log.e(TAG, "onResume: " );

    }

    private boolean isValidated(){
        if(etComments.getText().toString().isEmpty()){
            etComments.setError("Please write something!");
            etComments.setFocusable(true);
            etComments.requestFocus();
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
