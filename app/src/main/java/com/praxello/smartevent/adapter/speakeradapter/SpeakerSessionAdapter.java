package com.praxello.smartevent.adapter.speakeradapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.praxello.smartevent.R;
import com.praxello.smartevent.model.speaker.SessionsData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SpeakerSessionAdapter extends RecyclerView.Adapter<SpeakerSessionAdapter.SpeakerSessionViewHolder>{

    public Context context;
    public ArrayList<SessionsData> sessionsDataArrayList;
    public static String TAG="SpeakerSessionAdapter";

    public SpeakerSessionAdapter(Context context, ArrayList<SessionsData> sessionsDataArrayList) {
        this.context = context;
        this.sessionsDataArrayList = sessionsDataArrayList;
    }

    @NonNull
    @Override
    public SpeakerSessionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.layout_speaker_session_row,parent,false);
        return new SpeakerSessionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SpeakerSessionViewHolder holder, int position) {
        holder.tvSessionTitle.setText(sessionsDataArrayList.get(position).getTitle());
        //Log.e(TAG, "onBindViewHolder: "+sessionsDataArrayList.get(position).getSessionDate());

        String date=sessionsDataArrayList.get(position).getSessionDate();
        SimpleDateFormat spf=new SimpleDateFormat("yyyy-MM-dd");
       // Log.e(TAG, "simple date format: "+spf.toString());
        Date newDate= null;
        try {
            newDate = spf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        spf= new SimpleDateFormat(" EEE,dd MMM yy");
        date = spf.format(newDate);
        //Log.e(TAG, "onBindViewHolder: "+date.toString() );
        holder.tvDateTime.setText(date+"  -  "+sessionsDataArrayList.get(position).getSlotTitle());
    }

    @Override
    public int getItemCount() {
        return sessionsDataArrayList.size();
    }

    public class SpeakerSessionViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tv_session_title)
        public TextView tvSessionTitle;
        @BindView(R.id.tv_date_time)
        public TextView tvDateTime;

        public SpeakerSessionViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
