package com.praxello.smartevent.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.praxello.smartevent.R;
import com.praxello.smartevent.activity.CaseReadMoreActivity;
import com.praxello.smartevent.model.allcases.AllCasesData;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;

public class CaseDescriptionAdapter extends RecyclerView.Adapter<CaseDescriptionAdapter.CaseDesciptionViewHolder> {

    public Context context;
    public ArrayList<AllCasesData> allCasesDataArrayList;
    AlertDialog alertDialog;
    public static final String TAG="CaseDescriptionAdapter";

    public CaseDescriptionAdapter(Context context, ArrayList<AllCasesData> allCasesDataArrayList) {
        this.context = context;
        this.allCasesDataArrayList = allCasesDataArrayList;
    }

    @NonNull
    @Override
    public CaseDesciptionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.case_description_row, parent, false);
        return new CaseDesciptionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CaseDesciptionViewHolder holder, int position) {

        holder.tvTitle.setText(allCasesDataArrayList.get(position).getCaseTitle());
        holder.tvSummary.setText(allCasesDataArrayList.get(position).getCaseDetails());

       // Log.e(TAG, "onBindViewHolder: "+allCasesDataArrayList.get(position).getSubmission());

        holder.btnReadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = (Activity) context;
                Intent intent = new Intent(context, CaseReadMoreActivity.class);
                intent.putExtra("data",allCasesDataArrayList.get(position));
                intent.putExtra("position",position);
                context.startActivity(intent);
                activity.overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
            }
        });
    }

    @Override
    public int getItemCount() {
        return allCasesDataArrayList.size();
    }


    public class CaseDesciptionViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_title)
        public TextView tvTitle;
        @BindView(R.id.tv_summary)
        public TextView tvSummary;
        @BindView(R.id.cvcasedescription)
        public CardView cvCaseDescription;
        @BindView(R.id.btnreadmore)
        public AppCompatButton btnReadMore;

        public CaseDesciptionViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

    }


}
