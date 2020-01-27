package com.praxello.smartevent.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.praxello.smartevent.adapter.CommentsAdapter;
import com.praxello.smartevent.model.agendadetails.AgendaData;
import com.praxello.smartevent.model.comments.CommentsResponse;
import com.praxello.smartevent.utility.CommonMethods;
import com.praxello.smartevent.utility.ConfiUrl;
import com.praxello.smartevent.utility.Constants;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        ButterKnife.bind(this);

        //Getting Data from intent using parcelable...
        Intent intent=getIntent();
        data=intent.getParcelableExtra("data");

        Log.e(TAG,"parcelable data"+data.getSessionType());


        rvComments.setLayoutManager(new LinearLayoutManager(this));

        Toolbar toolbar=findViewById(R.id.toolbar_comments);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("Comments");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);

        ivSubmitComment.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if(isValidated()){
                   addComment();
               }
           }
       });
    }

    public void addComment(){
        StringRequest stringRequest=new StringRequest(Request.Method.POST, ConfiUrl.ADD_COMMENT_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG,"response"+response);
                Gson gson=new Gson();
                CommentsResponse commentsResponse=gson.fromJson(response,CommentsResponse.class);

                if(commentsResponse.getResponsecode().equals("200")){
                    etComments.setText(null);
                    Toast.makeText(CommentsActivity.this, commentsResponse.getMessage(), Toast.LENGTH_SHORT).show();

                    CommentsAdapter commentsAdapter=new CommentsAdapter(CommentsActivity.this,commentsResponse.getCommentsData());
                    rvComments.setAdapter(commentsAdapter);
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
                params.put("userid", CommonMethods.getPrefrence(CommentsActivity.this, Constants.USER_ID));
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
}
