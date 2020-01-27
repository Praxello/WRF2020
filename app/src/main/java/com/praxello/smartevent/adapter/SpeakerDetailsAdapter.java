package com.praxello.smartevent.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.praxello.smartevent.R;
import com.praxello.smartevent.model.speaker.SpeakerData;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SpeakerDetailsAdapter extends RecyclerView.Adapter<SpeakerDetailsAdapter.SpeakerViewHolder>{

    public Context context;
    public ArrayList<SpeakerData> speakerDataArrayList;

    public SpeakerDetailsAdapter(Context context, ArrayList<SpeakerData> speakerDataArrayList) {
        this.context = context;
        this.speakerDataArrayList = speakerDataArrayList;
    }

    @NonNull
    @Override
    public SpeakerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.layouts_speaker_row,parent,false);
        return new SpeakerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SpeakerViewHolder holder, int position) {
            holder.tvSpeakerName.setText(speakerDataArrayList.get(position).getFirstname()+ " "+speakerDataArrayList.get(position).getLastname());
            holder.tvPhone.setText(speakerDataArrayList.get(position).getMobile());
            holder.tvGmail.setText(speakerDataArrayList.get(position).getEmail());
            holder.tvAddress.setText(speakerDataArrayList.get(position).getCity()+" "+speakerDataArrayList.get(position).getState()+" "+speakerDataArrayList.get(position).getCountry());
            holder.tvSummary.setText(speakerDataArrayList.get(position).getSummary());
    }

    @Override
    public int getItemCount() {
        return speakerDataArrayList.size();
    }

    public class SpeakerViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tv_speakername)
        public TextView tvSpeakerName;
        @BindView(R.id.tv_phone)
        public TextView tvPhone;
        @BindView(R.id.tv_gmail)
        public TextView tvGmail;
        @BindView(R.id.tv_location)
        public TextView tvAddress;
        @BindView(R.id.tv_summary)
        public TextView tvSummary;

        public SpeakerViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
