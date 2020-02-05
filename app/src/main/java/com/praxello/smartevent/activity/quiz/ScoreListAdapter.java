package com.praxello.smartevent.activity.quiz;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.praxello.smartevent.R;
import com.praxello.smartevent.model.score.ScoresData;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;


public class ScoreListAdapter extends RecyclerView.Adapter<ScoreListAdapter.RecyclerViewHolder> {
    private Context mContext;
    private ArrayList<ScoresData> list;

    public ScoreListAdapter(Context mContext, ArrayList<ScoresData> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_score, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        ScoresData score = list.get(position);
        holder.tvScore.setText(score.getScore());
        //holder.tvDate.setText((CharSequence) CommonMethods.getYmdDate(score.getQuizDate()));
        holder.tvDate.setText(score.getQuizDate());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvScore)
        TextView tvScore;
        @BindView(R.id.tvDate)
        TextView tvDate;

        RecyclerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}