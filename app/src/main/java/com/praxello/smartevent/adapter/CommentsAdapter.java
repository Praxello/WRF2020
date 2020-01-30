package com.praxello.smartevent.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.praxello.smartevent.R;
import com.praxello.smartevent.activity.DashBoardActivity;
import com.praxello.smartevent.activity.LoginActivity;
import com.praxello.smartevent.model.agendadetails.CommentsData1;
import com.praxello.smartevent.model.allattendee.AttendeeData;
import com.praxello.smartevent.model.comments.CommentsResponse;
import com.praxello.smartevent.model.likes.LikesResponse;
import com.praxello.smartevent.utility.CommonMethods;
import com.praxello.smartevent.utility.AllKeys;
import com.praxello.smartevent.utility.ConfiUrl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.paperdb.Paper;

public class CommentsAdapter extends  RecyclerView.Adapter<CommentsAdapter.CommentsViewHolder>{

    public Context context;
    public ArrayList<CommentsData1> commentsDataArrayList;
    ArrayList<AttendeeData> attendeeDataArrayList;
   /* public CommentsAdapter(Context context, ArrayList<CommentsData1> commentsDataArrayList) {
        this.context = context;
        this.commentsDataArrayList = commentsDataArrayList;
    }*/
    public static final String TAG="CommentsAdapter";

    public CommentsAdapter(Context context, ArrayList<CommentsData1> commentsDataArrayList) {
        this.context = context;
        this.commentsDataArrayList = commentsDataArrayList;
    }
    
    @NonNull
    @Override
    public CommentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.layout_comments_row,parent,false);
        return new CommentsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentsViewHolder holder, int position) {
        if(commentsDataArrayList.get(position)!=null){

            String date=commentsDataArrayList.get(position).getCommentDateTime();
            SimpleDateFormat spf=new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
            Date newDate= null;
            try {
                newDate = spf.parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            spf= new SimpleDateFormat("MMM dd,yy hh:mm ");
            date = spf.format(newDate);
            System.out.println(date);

            attendeeDataArrayList=  Paper.book().read("allattendee");
            if(CommonMethods.getPrefrence(context, AllKeys.USER_ID).equals(commentsDataArrayList.get(position).getUserId())){
                holder.llOurOwnComments.setVisibility(View.VISIBLE);
                holder.llOtherComments.setVisibility(View.GONE);
                for(AttendeeData attendeeData : attendeeDataArrayList) {
                    if(attendeeData.getUserId().equals(commentsDataArrayList.get(position).getUserId())) {
                        //found it!
                        if(attendeeData.getUserType().equals("Speaker")){
                            holder.tvOurUserName.setText(attendeeData.getFirstName()+" "+attendeeData.getLastName()+" (Host)");
                        }else{
                            holder.tvOurUserName.setText(attendeeData.getFirstName()+" "+attendeeData.getLastName());
                        }
                    }
                }
                holder.tvOurCommentMessage.setText(commentsDataArrayList.get(position).getComment());
                holder.tvOurCommentTime.setText(date);
            }else{

                //Log.e(TAG,"parcelable data"+data.getSessionType());
                for(AttendeeData attendeeData : attendeeDataArrayList) {
                    if(attendeeData.getUserId().equals(commentsDataArrayList.get(position).getUserId())) {
                        //found it!
                        if(attendeeData.getUserType().equals("Speaker")){
                            holder.tvUserName.setText(attendeeData.getFirstName()+" "+attendeeData.getLastName()+" (Host)");
                        }else{
                            holder.tvUserName.setText(attendeeData.getFirstName()+" "+attendeeData.getLastName());
                        }
                    }
                }
                holder.llOurOwnComments.setVisibility(View.GONE);
                holder.llOtherComments.setVisibility(View.VISIBLE);
               // holder.tvUserName.setText(commentsDataArrayList.get(position).getUserId());
                holder.tvCommentMessage.setText(commentsDataArrayList.get(position).getComment());
                holder.tvCommentTime.setText(date);
            }

            holder.cvDelete.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    new AlertDialog.Builder(context)
                            .setTitle("WRF2020")
                            .setMessage("Are you sure you want to delete?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Continue with delete operation
                                    deleteComment(commentsDataArrayList.get(position).getSessionId(),commentsDataArrayList.get(position).getCommentId());
                                }
                            })

                            // A null listener allows the button to dismiss the dialog and take no further action.
                            .setNegativeButton("No", null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                    return true;
                }
            });
        }
    }

    private void deleteComment(String sessionId,String commentId) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ConfiUrl.DELETE_COMMENT_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e(TAG, "response" + response);

                Gson gson = new Gson();
                CommentsResponse commentsResponse = gson.fromJson(response, CommentsResponse.class);

                if (commentsResponse.getResponsecode().equals("200")) {
                    Toast.makeText(context, commentsResponse.getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, commentsResponse.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "volley error" + error);
                Toast.makeText(context, AllKeys.SERVER_MESSAGE, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("commentid",commentId);
                params.put("sessionid", sessionId);

                Log.e(TAG, "params" + params);

                return params;
            }
        };
        RequestQueue mQueue = Volley.newRequestQueue(context);
        mQueue.add(stringRequest);

    }

    @Override
    public int getItemCount() {
        return commentsDataArrayList.size();
    }


    public class CommentsViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tv_username)
        public TextView tvUserName;
        @BindView(R.id.tv_commentmessage)
        public TextView tvCommentMessage;
        @BindView(R.id.tv_our_username)
        public TextView tvOurUserName;
        @BindView(R.id.tv_our_commentmessage)
        public TextView tvOurCommentMessage;
        @BindView(R.id.tv_our_commenttime)
        public TextView tvOurCommentTime;
        @BindView(R.id.tv_commenttime)
        public TextView tvCommentTime;
        @BindView(R.id.ll_othercomments)
        public LinearLayout llOtherComments;
        @BindView(R.id.ll_ourowncomments)
        public LinearLayout llOurOwnComments;
        @BindView(R.id.cv_delete)
        public CardView cvDelete;


        public CommentsViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
