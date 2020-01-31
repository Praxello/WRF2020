package com.praxello.smartevent.adapter.agendaadapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
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
import androidx.recyclerview.widget.LinearLayoutManager;
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
import com.praxello.smartevent.activity.CommentsActivity;
import com.praxello.smartevent.model.agendadetails.SpeakersName;
import com.praxello.smartevent.model.likes.LikesResponse;
import com.praxello.smartevent.model.agendadetails.AgendaData;
import com.praxello.smartevent.utility.CommonMethods;
import com.praxello.smartevent.utility.ConfiUrl;
import com.praxello.smartevent.utility.AllKeys;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.paperdb.Paper;

public class AgendaDetailsAdapter extends RecyclerView.Adapter<AgendaDetailsAdapter.AgendaDetailsViewHolder> {

    public Context context;
    public ArrayList<AgendaData> agendaDataArrayList;
    public static String TAG = "AgendaDetailsAdapter";
    public int dateValue;


   /* public AgendaDetailsAdapter(Context context, ArrayList<AgendaData> agendaDataArrayList) {
        this.context = context;
        this.agendaDataArrayList = agendaDataArrayList;
    }*/

    public AgendaDetailsAdapter(Context context, ArrayList<AgendaData> agendaDataArrayList, int dateValue) {
        this.context = context;
        this.agendaDataArrayList = agendaDataArrayList;
        this.dateValue = dateValue;
    }




    @NonNull
    @Override
    public AgendaDetailsAdapter.AgendaDetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.agenda_details_row, parent, false);
        return new AgendaDetailsAdapter.AgendaDetailsViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull AgendaDetailsAdapter.AgendaDetailsViewHolder holder, int position) {

        final AgendaData data = agendaDataArrayList.get(position);


        if(dateValue==0) {
            if (agendaDataArrayList.get(position).getSessionDate().equals("2020-03-28")) {
                if (agendaDataArrayList.get(position).getSessionType().equals("Session")) {
                    holder.cardView.setVisibility(View.VISIBLE);
                    holder.cvTeaCarView.setVisibility(View.GONE);
                    holder.cardView.setCardBackgroundColor(Color.parseColor("#fffde7"));
                    holder.tvTitle.setText(agendaDataArrayList.get(position).getTitle());
                    holder.tvSubject.setText(agendaDataArrayList.get(position).getSubject());
                    holder.tvSlotTime.setText(agendaDataArrayList.get(position).getSlotTitle());
                    holder.tvLocation.setText(agendaDataArrayList.get(position).getSessionLocation());
                    //holder.tvDate.setText(agendaDataArrayList.get(position).getSessionDate());
            /*try{
                if(agendaDataArrayList.get(position).getSpeakers()==null ||agendaDataArrayList.get(position).getSpeakers().size()==0){
                    holder.tvSpeakerName.setVisibility(View.GONE);
                }else{
                    holder.tvSpeakerName.setText(agendaDataArrayList.get(position).getSpeakers().get(0).getFirstname()+" "+agendaDataArrayList.get(position).getSpeakers().get(0).getLastname());
                }
            }catch (NullPointerException e){
                e.printStackTrace();
            }

            Log.e(TAG,"Speaker Data"+agendaDataArrayList.get(position).getSpeakers());
          */
                    try {
                        if (agendaDataArrayList.get(position).getSpeakers() != null) {
                            AgendaSpeakerAdapter agendaSpeakerAdapter = new AgendaSpeakerAdapter(context, agendaDataArrayList.get(position).getSpeakers());
                            holder.rvSpeakerData.setAdapter(agendaSpeakerAdapter);
                            //Log.e(TAG, "onBindViewHolder: speaker data response "+agendaDataArrayList.get(position).getSpeakers().size());
                            //Paper.book().write("speaker_data",agendaDataArrayList.get(position).getSpeakers());




                        }
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }

                    holder.tvSummary.setText(agendaDataArrayList.get(position).getDetails());
                    holder.tvLike.setText("Like (" + agendaDataArrayList.get(position).getLikes() + ")");
                    Log.e(TAG, "is Liked" + agendaDataArrayList.get(position).isLiked);

                    if (agendaDataArrayList.get(position).isLiked.equals("true") || agendaDataArrayList.get(position).isLiked.equals(true)) {
                        //holder.tvLike.setTextColor(R.color.blue500);
                        holder.tvLike.setTextColor(Color.parseColor("#1a237e"));
                        holder.tvLike.setTypeface(null, Typeface.BOLD);
                    }

                } else if (agendaDataArrayList.get(position).getSessionType().equals("Tea Break")) {
                    holder.cardView.setVisibility(View.GONE);
                    holder.cvTeaCarView.setVisibility(View.VISIBLE);

                    // holder.cvTeaCarView.setCardBackgroundColor(Color.parseColor("#f1f8e9"));
                    holder.tvTeaTitle.setText(agendaDataArrayList.get(position).getTitle());
                    holder.tvTeaSlotTime.setText(agendaDataArrayList.get(position).getSlotTitle());
                    holder.tvTeaLocation.setText(agendaDataArrayList.get(position).getSessionLocation());
                }

                holder.llLikes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        hitLikes(agendaDataArrayList.get(position).getSessionId(),holder,position);
                    }
                });

                holder.llComments.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Activity activity = (Activity) context;
                        Intent intent = new Intent(context, CommentsActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("data", agendaDataArrayList.get(position));

                        context.startActivity(intent);
                        activity.overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);

                    }
                });
            }
            else {
            }
        }else{
            if (agendaDataArrayList.get(position).getSessionDate().equals("2020-03-29")) {
                if (agendaDataArrayList.get(position).getSessionType().equals("Session")) {
                    holder.cardView.setVisibility(View.VISIBLE);
                    holder.cvTeaCarView.setVisibility(View.GONE);
                    holder.cardView.setCardBackgroundColor(Color.parseColor("#fffde7"));
                    holder.tvTitle.setText(agendaDataArrayList.get(position).getTitle());
                    holder.tvSubject.setText(agendaDataArrayList.get(position).getSubject());
                    holder.tvSlotTime.setText(agendaDataArrayList.get(position).getSlotTitle());
                    holder.tvLocation.setText(agendaDataArrayList.get(position).getSessionLocation());
                    //holder.tvDate.setText(agendaDataArrayList.get(position).getSessionDate());
            /*try{
                if(agendaDataArrayList.get(position).getSpeakers()==null ||agendaDataArrayList.get(position).getSpeakers().size()==0){
                    holder.tvSpeakerName.setVisibility(View.GONE);
                }else{
                    holder.tvSpeakerName.setText(agendaDataArrayList.get(position).getSpeakers().get(0).getFirstname()+" "+agendaDataArrayList.get(position).getSpeakers().get(0).getLastname());
                }
            }catch (NullPointerException e){
                e.printStackTrace();
            }

            Log.e(TAG,"Speaker Data"+agendaDataArrayList.get(position).getSpeakers());
          */
                    try {
                        if (agendaDataArrayList.get(position).getSpeakers() != null) {
                            AgendaSpeakerAdapter agendaSpeakerAdapter = new AgendaSpeakerAdapter(context, agendaDataArrayList.get(position).getSpeakers());
                            holder.rvSpeakerData.setAdapter(agendaSpeakerAdapter);
                          //      Paper.book().write("speaker_data",agendaDataArrayList.get(position).getSpeakers());

                        }
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }

                    holder.tvSummary.setText(agendaDataArrayList.get(position).getDetails());
                    holder.tvLike.setText("Like (" + agendaDataArrayList.get(position).getLikes() + ")");
                    Log.e(TAG, "is Liked" + agendaDataArrayList.get(position).isLiked);

                    if (agendaDataArrayList.get(position).isLiked.equals("true") || agendaDataArrayList.get(position).isLiked.equals(true)) {
                        //holder.tvLike.setTextColor(R.color.blue500);
                        holder.tvLike.setTextColor(Color.parseColor("#1a237e"));
                        holder.tvLike.setTypeface(null, Typeface.BOLD);
                    }

                } else if (agendaDataArrayList.get(position).getSessionType().equals("Tea Break")) {
                    holder.cardView.setVisibility(View.GONE);
                    holder.cvTeaCarView.setVisibility(View.VISIBLE);

                    // holder.cvTeaCarView.setCardBackgroundColor(Color.parseColor("#f1f8e9"));
                    holder.tvTeaTitle.setText(agendaDataArrayList.get(position).getTitle());
                    holder.tvTeaSlotTime.setText(agendaDataArrayList.get(position).getSlotTitle());
                    holder.tvTeaLocation.setText(agendaDataArrayList.get(position).getSessionLocation());
                }

                holder.llLikes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        hitLikes(agendaDataArrayList.get(position).getSessionId(),holder,position);
                    }
                });

                holder.llComments.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Activity activity = (Activity) context;
                        Intent intent = new Intent(context, CommentsActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("data", agendaDataArrayList.get(position));
                        context.startActivity(intent);
                        activity.overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);

                    }
                });
            } else {
            }
        }

    }

    @Override
    public int getItemCount() {
        return agendaDataArrayList.size();
    }

    private void hitLikes(String sessionId,AgendaDetailsViewHolder holder,int position) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ConfiUrl.LIKE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e(TAG, "response" + response);

                Gson gson = new Gson();
                LikesResponse likesResponse = gson.fromJson(response, LikesResponse.class);

                if (likesResponse.getResponsecode().equals("200")) {
                    holder.tvLike.setText("Like (" + likesResponse.getLikes() + ")");
                    Log.e(TAG, "is Liked" + likesResponse.isLiked);

                    if (likesResponse.isLiked.equals("true") || likesResponse.isLiked.equals(true)) {
                        //holder.tvLike.setTextColor(R.color.blue500);
                        holder.tvLike.setTextColor(Color.parseColor("#1a237e"));
                        holder.tvLike.setTypeface(null, Typeface.BOLD);
                    }else{
                        holder.tvLike.setTextColor(Color.parseColor("#9e9e9e"));
                        holder.tvLike.setTypeface(null, Typeface.NORMAL);
                    }
                } else {
                    Toast.makeText(context, likesResponse.getMessage(), Toast.LENGTH_SHORT).show();
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
                params.put("userid", CommonMethods.getPrefrence(context, AllKeys.USER_ID));
                params.put("sessionid", sessionId);

                Log.e(TAG, "params" + params);

                return params;
            }
        };
        RequestQueue mQueue = Volley.newRequestQueue(context);
        mQueue.add(stringRequest);
    }

    public class AgendaDetailsViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_title)
        public TextView tvTitle;
        @BindView(R.id.tv_subject)
        public TextView tvSubject;
        // @BindView(R.id.tv_speakername)
        // public TextView tvSpeakerName;
        @BindView(R.id.tv_summary)
        public TextView tvSummary;
        @BindView(R.id.ll_comment)
        public LinearLayout llComments;
        @BindView(R.id.ll_like)
        public LinearLayout llLikes;
        @BindView(R.id.tv_location)
        public TextView tvLocation;
        @BindView(R.id.tv_slottime)
        public TextView tvSlotTime;
        @BindView(R.id.ll_buttons)
        public LinearLayout llButtons;
        @BindView(R.id.cardview)
        public CardView cardView;
        @BindView(R.id.cardview_tea)
        public CardView cvTeaCarView;
        @BindView(R.id.tv_like)
        public TextView tvLike;
        //@BindView(R.id.tv_datetime)
        //public TextView tvDate;
        @BindView(R.id.tv_tea_title)
        public TextView tvTeaTitle;
        @BindView(R.id.tv_tea_slottime)
        public TextView tvTeaSlotTime;
        @BindView(R.id.tv_tea_location)
        public TextView tvTeaLocation;
        @BindView(R.id.rv_speaker_data)
        public RecyclerView rvSpeakerData;


        public AgendaDetailsViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            rvSpeakerData.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, true));
        }
    }
}
