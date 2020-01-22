package com.praxello.smartevent.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.praxello.smartevent.R;
import com.praxello.smartevent.model.DashBoardData;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class DashBoardAdapter extends RecyclerView.Adapter<DashBoardAdapter.DashBoardViewHolder>{

    public Context context;
    public ArrayList<DashBoardData> dashBoardDataArrayList;

    public DashBoardAdapter(Context context, ArrayList<DashBoardData> dashBoardDataArrayList) {
        this.context = context;
        this.dashBoardDataArrayList = dashBoardDataArrayList;
    }

    @NonNull
    @Override
    public DashBoardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.layout_dashboard_row,parent,false);
        return new DashBoardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DashBoardViewHolder holder, int position) {
        holder.tvName.setText(dashBoardDataArrayList.get(position).getName());
        Glide.with(context)
                .load(dashBoardDataArrayList.get(position).getImagePath())
                .into(holder.ivPath);
    }

    @Override
    public int getItemCount() {
        return dashBoardDataArrayList.size();
    }

    public class DashBoardViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tv_dashboardname)
        public TextView tvName;
        @BindView(R.id.iv_dashboardimg)
        public CircleImageView ivPath;

        public DashBoardViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
