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
import com.praxello.smartevent.model.agendadetails.CommentsData1;
import com.praxello.smartevent.utility.CommonMethods;
import com.praxello.smartevent.utility.AllKeys;

import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;

public class CommentsAdapter extends  RecyclerView.Adapter<CommentsAdapter.CommentsViewHolder>{

    public Context context;
    public ArrayList<CommentsData1> commentsDataArrayList;

   /* public CommentsAdapter(Context context, ArrayList<CommentsData1> commentsDataArrayList) {
        this.context = context;
        this.commentsDataArrayList = commentsDataArrayList;
    }*/

    public CommentsAdapter(Context context, ArrayList<CommentsData1> commentsDataArrayList) {
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
        if(commentsDataArrayList.get(position)!=null){
            if(CommonMethods.getPrefrence(context, AllKeys.USER_ID).equals(commentsDataArrayList.get(position).getUserId())){
                holder.llOurOwnComments.setVisibility(View.VISIBLE);
                holder.llOtherComments.setVisibility(View.GONE);
                holder.tvOurUserName.setText(commentsDataArrayList.get(position).getUserId());
                holder.tvOurCommentMessage.setText(commentsDataArrayList.get(position).getComment());
            }else{
                holder.llOurOwnComments.setVisibility(View.GONE);
                holder.llOtherComments.setVisibility(View.VISIBLE);
                holder.tvUserName.setText(commentsDataArrayList.get(position).getUserId());
                holder.tvCommentMessage.setText(commentsDataArrayList.get(position).getComment());
            }

        }
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
        @BindView(R.id.tv_our_username)
        public TextView tvOurUserName;
        @BindView(R.id.tv_our_commentmessage)
        public TextView tvOurCommentMessage;
        @BindView(R.id.ll_othercomments)
        public LinearLayout llOtherComments;
        @BindView(R.id.ll_ourowncomments)
        public LinearLayout llOurOwnComments;

        public CommentsViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
