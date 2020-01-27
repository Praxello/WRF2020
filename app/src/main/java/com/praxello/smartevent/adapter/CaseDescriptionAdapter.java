package com.praxello.smartevent.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.praxello.smartevent.R;
import com.praxello.smartevent.activity.WebviewActivity;
import com.praxello.smartevent.model.allcases.AllCasesData;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;

public class CaseDescriptionAdapter extends RecyclerView.Adapter<CaseDescriptionAdapter.CaseDesciptionViewHolder> {

    public Context  context;
    public ArrayList<AllCasesData> allCasesDataArrayList;

    public CaseDescriptionAdapter(Context context, ArrayList<AllCasesData> allCasesDataArrayList) {
        this.context = context;
        this.allCasesDataArrayList = allCasesDataArrayList;
    }


    @NonNull
    @Override
    public CaseDesciptionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.case_description_row,parent,false);
        return new CaseDesciptionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CaseDesciptionViewHolder holder, int position) {

        holder.tvTitle.setText(allCasesDataArrayList.get(position).getCaseTitle());
        holder.tvSummary.setText(allCasesDataArrayList.get(position).getCaseDetails());

        holder.ivPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(allCasesDataArrayList.get(position).getPdflink()==null || allCasesDataArrayList.get(position).getPdflink().isEmpty()
                || allCasesDataArrayList.get(position).getPdflink().equals("null")){
                    Toast.makeText(context, "Pdf not available", Toast.LENGTH_SHORT).show();
                }else{
                    Activity activity = (Activity) context;
                    Intent intent=new Intent(context, WebviewActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("title","PDF Viewer");
                    intent.putExtra("type","PDF");
                    intent.putExtra("url",allCasesDataArrayList.get(position).getPdflink());
                    context.startActivity(intent);
                    activity.overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
                }
            }
        });

        holder.ivVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        holder.ivPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(allCasesDataArrayList.get(position).getPhotoUrl()==null || allCasesDataArrayList.get(position).getPhotoUrl().isEmpty()
                        || allCasesDataArrayList.get(position).getPhotoUrl().equals("null")){
                    Toast.makeText(context, "Photo not available", Toast.LENGTH_SHORT).show();
                }else{
                    Activity activity = (Activity) context;
                    Intent intent=new Intent(context, WebviewActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("title","Photo Viewer");
                    //intent.putExtra("type","PDF");
                    intent.putExtra("url",allCasesDataArrayList.get(position).getPhotoUrl());
                    context.startActivity(intent);
                    activity.overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
                }
            }
        });

        holder.ivLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return allCasesDataArrayList.size();
    }


    public class CaseDesciptionViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tv_title)
        public TextView tvTitle;
        @BindView(R.id.tv_summary)
        public TextView tvSummary;
        @BindView(R.id.iv_pdf)
        public ImageView ivPdf;
        @BindView(R.id.iv_video)
        public ImageView ivVideo;
        @BindView(R.id.iv_photo)
        public ImageView ivPhoto;
        @BindView(R.id.iv_link)
        public ImageView ivLink;

        public CaseDesciptionViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this,itemView);
        }

    }
}
