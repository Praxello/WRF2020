package com.praxello.smartevent.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
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
import com.praxello.smartevent.model.agendadetails.AgendaData;
import com.praxello.smartevent.model.agendadetails.SpeakersName;
import com.praxello.smartevent.model.allattendee.AttendeeData;
import com.praxello.smartevent.model.comments.CommentsResponse;
import com.praxello.smartevent.model.comments.LatestCommentData;
import com.praxello.smartevent.utility.CommonMethods;
import com.praxello.smartevent.utility.AllKeys;
import com.praxello.smartevent.utility.ConfiUrl;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.internal.Constants;


public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentsViewHolder> {

    public Context context;
    public ArrayList<LatestCommentData> commentsDataArrayList;
    ArrayList<AttendeeData> attendeeDataArrayList;
    AgendaData agendaData;


    public static final String TAG = "CommentsAdapter";

    public CommentsAdapter(Context context, ArrayList<LatestCommentData> commentsDataArrayList, AgendaData agendaData) {
        this.context = context;
        this.commentsDataArrayList = commentsDataArrayList;
        this.agendaData = agendaData;
    }

    @NonNull
    @Override
    public CommentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layout_comments_row, parent, false);
        return new CommentsViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull CommentsViewHolder holder, int position) {
        if (commentsDataArrayList.get(position) != null) {
            String date = commentsDataArrayList.get(position).getCommentDateTime();
            SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date startDate = null;

            try {
                startDate = spf.parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String startDate1="";
            startDate1=spf.format(startDate);

            String endDate1="";
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date endDate = new Date();
            endDate1=formatter.format(endDate);
            //String currentDate=getCurrentTime(newDate);
            String currentDate="";

            int diffInDays=calculateDifferenceBetweenDatesDays(startDate1,endDate1);
           // Log.e(TAG, "onBindViewHolder: diffindays"+diffInDays );

            if (diffInDays == 0 || diffInDays < 0) {
                spf = new SimpleDateFormat("hh:mm a");
                currentDate = spf.format(startDate);
            } else if (diffInDays > 0) {
                spf = new SimpleDateFormat("EEE hh:mm a");
                currentDate = spf.format(startDate);
            }

          //  spf = new SimpleDateFormat("EEE hh:mm a");
           // date = spf.format(newDate);

            int id = Integer.parseInt(CommonMethods.getPrefrence(context, AllKeys.USER_ID));
            if (id == commentsDataArrayList.get(position).getUserId()) {
                //Log.e(TAG,"parcelable data"+data.getSessionType());
                holder.llOurOwnComments.setVisibility(View.VISIBLE);
                holder.llOtherComments.setVisibility(View.GONE);
                // holder.tvUserName.setText(commentsDataArrayList.get(position).getUserId());
                holder.tvOurCommentMessage.setText(commentsDataArrayList.get(position).getComment());
                holder.tvOurCommentTime.setText(currentDate);
                //holder.cvDelete.setCardBackgroundColor(Color.TRANSPARENT);
                holder.cvDelete.setCardBackgroundColor(Color.parseColor("#70ffffff"));

            } else {
                holder.llOurOwnComments.setVisibility(View.GONE);
                holder.llOtherComments.setVisibility(View.VISIBLE);
                //holder.cvOther.setCardBackgroundColor(Color.TRANSPARENT);
                holder.cvOther.setCardBackgroundColor(Color.parseColor("#70ffffff"));
                holder.tvCommentTime.setText(currentDate);
                String attendeeName = "";
                //Log.e(TAG, "onBindViewHolder:HashMapData "+DashBoardActivity.mapAttendeeData.size() );
                // Log.e(TAG, "onBindViewHolder: UserId"+commentsDataArrayList.get(position).getUserId() );
                if (DashBoardActivity.mapAttendeeData.containsKey(commentsDataArrayList.get(position).getUserId())) {
                    AttendeeData attendeeData;
                    attendeeData = DashBoardActivity.mapAttendeeData.get(commentsDataArrayList.get(position).getUserId());
                    attendeeName = attendeeData.getFirstName() + " " + attendeeData.getLastName();
                }

                // Log.e(TAG, "onBindViewHolder: attendeeName" +attendeeName );

                try{
                    for (SpeakersName speakersName : agendaData.getSpeakers()) {
                        if (speakersName.getUserid().equals(commentsDataArrayList.get(position).getUserId())) {
                            //found it!
                            holder.tvUserName.setText(attendeeName + " (Host)");

                            break;
                        } else {
                            holder.tvUserName.setText(attendeeName);

                        }
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }

                holder.tvCommentMessage.setText(commentsDataArrayList.get(position).getComment());

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
                                    deleteComment(commentsDataArrayList.get(position).getSessionId(), commentsDataArrayList.get(position).getCommentId(), position);
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

    public static int calculateDifferenceBetweenDatesDays(String dateStart, String dateStop) {
        //   String dateStart = "11/03/14 09:29:58";
        // String dateStop = "11/03/14 09:33:43";

        // Custom date format
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        Date d1 = null;
        Date d2 = null;
        try {
            d1 = format.parse(dateStart);
            d2 = format.parse(dateStop);
            //Log.e(TAG, "calculateDifferenceBetweenDatesDays: dateStart" +d1.toString() );
            //Log.e(TAG, "calculateDifferenceBetweenDatesDays: dateStop" +d2.toString() );
            // Get msec from each, and subtract.
            long diff = d2.getTime() - d1.getTime();
            long diffSeconds = diff / 1000 % 60;
            long diffMinutes = diff / (60 * 1000) % 60;
            long diffHours = diff / (60 * 60 * 1000);
            int days = (int) diffHours / 24;
//            System.out.println("Time in seconds: " + diffSeconds + " seconds.");
//            System.out.println("Time in minutes: " + diffMinutes + " minutes.");
//            System.out.println("Time in hours: " + diffHours + " hours.");
            return days;
        } catch (java.text.ParseException ex) {
            // JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
            Logger.getLogger(Constants.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }


    private void deleteComment(String sessionId, String commentId, Integer position) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ConfiUrl.DELETE_COMMENT_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                // Log.e(TAG, "response" + response);

                Gson gson = new Gson();
                CommentsResponse commentsResponse = gson.fromJson(response, CommentsResponse.class);

                if (commentsResponse.getResponsecode().equals("200")) {
                    Toast.makeText(context, commentsResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    commentsDataArrayList.remove(position);
                    //notifyDataSetChanged();
                    notifyItemRemoved(position);
                    notifyDataSetChanged();
                } else {
                    Toast.makeText(context, commentsResponse.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.e(TAG, "volley error" + error);
                Toast.makeText(context, AllKeys.SERVER_MESSAGE, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("commentid", commentId);
                params.put("sessionid", sessionId);

                //  Log.e(TAG, "params" + params);

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

    public class CommentsViewHolder extends RecyclerView.ViewHolder {

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
        @BindView(R.id.cvother)
        public CardView cvOther;


        public CommentsViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
