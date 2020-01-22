package com.praxello.smartevent.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.praxello.smartevent.R;
import com.praxello.smartevent.model.allcases.AllCasesData;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CaseDescriptionAdapter extends RecyclerView.Adapter<CaseDescriptionAdapter.CaseDesciptionViewHolder> {

    public Context context;
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

        holder.tvTitle.setText(allCasesDataArrayList.get(position).getTitle());
        holder.tvSummary.setText(allCasesDataArrayList.get(position).getDetails());

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
