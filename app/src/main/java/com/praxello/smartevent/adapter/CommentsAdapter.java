package com.praxello.smartevent.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.praxello.smartevent.R;
import com.praxello.smartevent.model.agendadetails.CommentsData;
import com.praxello.smartevent.model.comments.CommentData1;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CommentsAdapter extends  RecyclerView.Adapter<CommentsAdapter.CommentsViewHolder>{

    public Context context;
    public ArrayList<CommentData1> commentsDataArrayList;

   /* public CommentsAdapter(Context context, ArrayList<CommentData1> commentsDataArrayList) {
        this.context = context;
        this.commentsDataArrayList = commentsDataArrayList;
    }*/

    public CommentsAdapter(Context context, ArrayList<CommentData1> commentsDataArrayList) {
        this.context = context;
        this.commentsDataArrayList = commentsDataArrayList;
    }

    @NonNull
    @Override
    public CommentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.layout_comments_row,parent,false);
        return new CommentsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentsViewHolder holder, int position) {
        holder.tvUserName.setText(commentsDataArrayList.get(position).getUserId());
        holder.tvCommentMessage.setText(commentsDataArrayList.get(position).getComment());
    }

    @Override
    public int getItemCount() {
        return commentsDataArrayList.size();
    }

    public class CommentsViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tv_username)
        public TextView tvUserName;
        @BindView(R.id.tv_commentmessage)
        public TextView tvCommentMessage;

        public CommentsViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
