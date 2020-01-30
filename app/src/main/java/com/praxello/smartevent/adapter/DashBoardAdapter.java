package com.praxello.smartevent.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.praxello.smartevent.R;
import com.praxello.smartevent.activity.AboutActivity;
import com.praxello.smartevent.activity.AgendaDetailsActivity;
import com.praxello.smartevent.activity.CaseDescriptionActivity;
import com.praxello.smartevent.activity.SpeakerActivity;
import com.praxello.smartevent.activity.WebviewActivity;
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

        holder.cvDashboardClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position==0){
                    Activity activity = (Activity) context;
                    Intent intent=new Intent(context,AgendaDetailsActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent);
                    activity.overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
                }

                if(position==1){
                    Activity activity = (Activity) context;
                    Intent intent=new Intent(context,CaseDescriptionActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent);
                    activity.overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
                }

                if(position==2){
                    Activity activity = (Activity) context;
                    Intent intent=new Intent(context, SpeakerActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent);
                    activity.overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
                }
                if(position==3){
                    Toast.makeText(context, "Comming Soon", Toast.LENGTH_SHORT).show();
                }
                if(position==4){
                    /*Activity activity = (Activity) context;
                    Intent intent=new Intent(context, WebviewActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("title","About");
                    intent.putExtra("type","ABOUT");
                    intent.putExtra("url","file:///android_asset/aboutus.html");
                    context.startActivity(intent);
                    activity.overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);*/

                    Activity activity = (Activity) context;
                    Intent intent=new Intent(context, AboutActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent);
                    activity.overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
                }
                if(position==5){
                    Toast.makeText(context, "Comming Soon", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dashBoardDataArrayList.size();
    }

    public class DashBoardViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tv_dashboardname)
        public TextView tvName;
        @BindView(R.id.iv_dashboardimg)
        public ImageView ivPath;
        @BindView(R.id.cardview_dashboard)
        public CardView cvDashboardClick;
        //public CircleImageView ivPath;

        public DashBoardViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
