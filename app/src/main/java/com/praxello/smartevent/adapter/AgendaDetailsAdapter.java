package com.praxello.smartevent.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.praxello.smartevent.R;
import com.praxello.smartevent.model.agendadetails.AgendaData;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;

public class AgendaDetailsAdapter extends RecyclerView.Adapter<AgendaDetailsAdapter.AgendaDetailsViewHolder> {

    public Context context;
    public ArrayList<AgendaData> agendaDataArrayList;

    public AgendaDetailsAdapter(Context context, ArrayList<AgendaData> agendaDataArrayList) {
        this.context = context;
        this.agendaDataArrayList = agendaDataArrayList;
    }

    @NonNull
    @Override
    public AgendaDetailsAdapter.AgendaDetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.agenda_details_row,parent,false);
        return new AgendaDetailsAdapter.AgendaDetailsViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull AgendaDetailsAdapter.AgendaDetailsViewHolder holder, int position) {
        if(agendaDataArrayList.get(position).getSessionType().equals("Session")){
            holder.cardView.setCardBackgroundColor(Color.parseColor("#fffde7"));
            holder.tvTitle.setText(agendaDataArrayList.get(position).getTitle());
            holder.tvSubject.setText(agendaDataArrayList.get(position).getSubject());
            holder.tvSlotTime.setText(agendaDataArrayList.get(position).getSlotTitle());
            holder.tvLocation.setText(agendaDataArrayList.get(position).getSessionLocation());
            try{
                if(agendaDataArrayList.get(position).getSpeakers()==null ||agendaDataArrayList.get(position).getSpeakers().size()==0){
                    holder.tvSpeakerName.setVisibility(View.GONE);
                }else{
                    holder.tvSpeakerName.setText(agendaDataArrayList.get(position).getSpeakers().get(0).getFirstname()+" "+agendaDataArrayList.get(position).getSpeakers().get(0).getLastname());
                }
            }catch (NullPointerException e){
                e.printStackTrace();
            }

            holder.tvSummary.setText(agendaDataArrayList.get(position).getDetails());
        }else if(agendaDataArrayList.get(position).getSessionType().equals("Break")){
            holder.cardView.setCardBackgroundColor(Color.parseColor("#f1f8e9"));
            holder.tvTitle.setText(agendaDataArrayList.get(position).getTitle());
            //holder.tvSubject.setText(agendaDataArrayList.get(position).getSubject());
            holder.tvSlotTime.setText(agendaDataArrayList.get(position).getSlotTitle());
            holder.tvLocation.setText(agendaDataArrayList.get(position).getSessionLocation());
            holder.llButtons.setVisibility(View.GONE);
            holder.tvSummary.setVisibility(View.GONE);
            holder.tvSubject.setVisibility(View.GONE);
            holder.tvSpeakerName.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return agendaDataArrayList.size();
    }

    public class AgendaDetailsViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tv_title)
        public TextView tvTitle;
        @BindView(R.id.tv_subject)
        public TextView tvSubject;
        @BindView(R.id.tv_speakername)
        public TextView tvSpeakerName;
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

        public AgendaDetailsViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
