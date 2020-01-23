package com.praxello.smartevent.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.praxello.smartevent.R;
import com.praxello.smartevent.model.advertisment.AdvertismentData;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MarqueeAdvertismentAdapter extends RecyclerView.Adapter<MarqueeAdvertismentAdapter.MarqueeAdvertismentViewHolder> {

    public Context context;
    public ArrayList<AdvertismentData> advertismentDataArrayList;

    public MarqueeAdvertismentAdapter(Context context, ArrayList<AdvertismentData> advertismentDataArrayList) {
        this.context = context;
        this.advertismentDataArrayList = advertismentDataArrayList;
    }

    @NonNull
    @Override
    public MarqueeAdvertismentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.layout_marquee_row,parent,false);
        return new MarqueeAdvertismentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MarqueeAdvertismentViewHolder holder, int position) {

        if(advertismentDataArrayList.size()!=0){
            if(advertismentDataArrayList.get(position).adType.equals("0")){
                holder.tvMarquee.setSelected(true);
                holder.tvMarquee.setText(advertismentDataArrayList.get(position).getAdDetails());
            }
        }
    }

    @Override
    public int getItemCount() {
        return advertismentDataArrayList.size();
    }

    public class MarqueeAdvertismentViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tv_marquee)
        TextView tvMarquee;

        public MarqueeAdvertismentViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
