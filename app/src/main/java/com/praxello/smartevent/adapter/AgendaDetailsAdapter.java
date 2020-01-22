package com.praxello.smartevent.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
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

    @Override
    public void onBindViewHolder(@NonNull AgendaDetailsAdapter.AgendaDetailsViewHolder holder, int position) {
        holder.tvTitle.setText(agendaDataArrayList.get(position).getTitle());
        try{
            if(agendaDataArrayList.get(position).getSpeakers()!=null ||agendaDataArrayList.get(position).getSpeakers().size()!=0){
                holder.tvSpeakerName.setText(agendaDataArrayList.get(position).getSpeakers().get(0).getFirstname()+" "+agendaDataArrayList.get(position).getSpeakers().get(0).getLastname());
            }
        }catch (NullPointerException e){
            e.printStackTrace();
        }

        holder.tvSummary.setText(agendaDataArrayList.get(position).getDetails());
    }

    @Override
    public int getItemCount() {
        return agendaDataArrayList.size();
    }

    public class AgendaDetailsViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tv_title)
        public TextView tvTitle;
        @BindView(R.id.tv_speakername)
        public TextView tvSpeakerName;
        @BindView(R.id.tv_summary)
        public TextView tvSummary;
        @BindView(R.id.ll_comment)
        public LinearLayout llComments;
        @BindView(R.id.ll_like)
        public LinearLayout llLikes;

        public AgendaDetailsViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
