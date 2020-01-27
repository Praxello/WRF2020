package com.praxello.smartevent.adapter.agendaadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.praxello.smartevent.R;
import com.praxello.smartevent.model.agendadetails.SpeakersName;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AgendaSpeakerAdapter extends  RecyclerView.Adapter<AgendaSpeakerAdapter.AgendaSpeakerViewHolder>{

    public Context context;
    public ArrayList<SpeakersName> speakersNameArrayList;

    public AgendaSpeakerAdapter(Context context, ArrayList<SpeakersName> speakersNameArrayList) {
        this.context = context;
        this.speakersNameArrayList = speakersNameArrayList;
    }

    @NonNull
    @Override
    public AgendaSpeakerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.layout_agendaspeaker_row,parent,false);
        return new AgendaSpeakerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AgendaSpeakerViewHolder holder, int position) {
        holder.tvSpeakerName.setText(speakersNameArrayList.get(position).getFirstname()+" "+speakersNameArrayList.get(position).getLastname());
    }

    @Override
    public int getItemCount() {
        return speakersNameArrayList.size();
    }

    public class AgendaSpeakerViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tv_speakername)
        public TextView tvSpeakerName;
        //@BindView(R.id.cardview)
       // public CardView cardView;

        public AgendaSpeakerViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
