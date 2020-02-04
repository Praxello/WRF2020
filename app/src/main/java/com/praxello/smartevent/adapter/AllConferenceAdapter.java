package com.praxello.smartevent.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.praxello.smartevent.R;
import com.praxello.smartevent.activity.DashBoardActivity;
import com.praxello.smartevent.activity.LoginActivity;
import com.praxello.smartevent.model.allconference.AllConferenceData;
import com.praxello.smartevent.utility.AllKeys;
import com.praxello.smartevent.utility.CommonMethods;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AllConferenceAdapter extends RecyclerView.Adapter<AllConferenceAdapter.AllConferenceViewHolder> {

    public Context context;
    public ArrayList<AllConferenceData> conferenceDataArrayList;
    public static final String TAG="AllConferenceAdapter";

    public AllConferenceAdapter(Context context, ArrayList<AllConferenceData> conferenceDataArrayList) {
        this.context = context;
        this.conferenceDataArrayList = conferenceDataArrayList;
    }

    @NonNull
    @Override
    public AllConferenceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.layout_all_conference_row,parent,false);
        return new AllConferenceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AllConferenceViewHolder holder, int position) {
        Log.e(TAG, "onBindViewHolder: "+conferenceDataArrayList.get(position).getSplashUrl() );
        if (conferenceDataArrayList.get(position).getSplashUrl() != null) {
            Glide.with(context).load(conferenceDataArrayList.get(position).getSplashUrl()).into(holder.imageView);
            //Glide.with(context).load(R.drawable.no_data).into(holder.imageView);

            holder.imageView.setOnClickListener(v -> {
                /*if (advertisement.getWebsitelink() != null && !TextUtils.isEmpty(advertisement.getWebsitelink())) {
                    Utils.openBrowser(mContext, advertisement.getWebsitelink());
                }*/
                /*Log.e(TAG, "onBindViewHolder:link_ logourl"+conferenceDataArrayList.get(position).getLogoUrl());
                Log.e(TAG, "onBindViewHolder: link_splashurl"+CommonMethods.getPrefrence(context,AllKeys.CONFERENCE_SPLASH_URL) );
                Log.e(TAG, "onBindViewHolder: link_Boot map url"+CommonMethods.getPrefrence(context,AllKeys.CONFERENCE_BOOTH_MAP_URL) );*/
                CommonMethods.setPreference(context, AllKeys.CONFERENCE_ID,conferenceDataArrayList.get(position).getConferenceId());
                CommonMethods.setPreference(context, AllKeys.CONFERENCE_TITLE,conferenceDataArrayList.get(position).getConferenceTitle());
                CommonMethods.setPreference(context, AllKeys.CONFERENCE_LOGO_URL,conferenceDataArrayList.get(position).getLogoUrl());
                CommonMethods.setPreference(context, AllKeys.CONFERENCE_SPLASH_URL,conferenceDataArrayList.get(position).getSplashUrl());
                CommonMethods.setPreference(context, AllKeys.CONFERENCE_BOOTH_MAP_URL,conferenceDataArrayList.get(position).getBoothMapUrl());
                CommonMethods.setPreference(context, AllKeys.CONFERENCE_SUMMARY,conferenceDataArrayList.get(position).getSummary());
                CommonMethods.setPreference(context, AllKeys.CONFERENCE_ADDRESS,conferenceDataArrayList.get(position).getAddress());
                CommonMethods.setPreference(context, AllKeys.CONFERENCE_LONGITUE,conferenceDataArrayList.get(position).getLongitude());
                CommonMethods.setPreference(context, AllKeys.CONFERENCE_LATITUDE,conferenceDataArrayList.get(position).getLatitude());

               /* Log.e(TAG, "onBindViewHolder: logourl"+CommonMethods.getPrefrence(context,AllKeys.CONFERENCE_LOGO_URL) );
                Log.e(TAG, "onBindViewHolder: splashurl"+CommonMethods.getPrefrence(context,AllKeys.CONFERENCE_SPLASH_URL) );
                Log.e(TAG, "onBindViewHolder: Boot map url"+CommonMethods.getPrefrence(context,AllKeys.CONFERENCE_BOOTH_MAP_URL) );*/

                if(CommonMethods.getPrefrence(context, AllKeys.USER_ID).equals(AllKeys.DNF)){
                    Activity activity = (Activity) context;
                    Intent intent=new Intent(context, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent);
                    activity.overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
                    activity.finish();
                }else{
                    Activity activity = (Activity) context;
                    Intent intent=new Intent(context, DashBoardActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent);
                    activity.overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
                    activity.finish();
                }
            });
        }

        holder.tvAddName.setText(conferenceDataArrayList.get(position).getConferenceTitle());
    }

    @Override
    public int getItemCount() {
        return conferenceDataArrayList.size();
    }

    public class AllConferenceViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.iv_banner)
        ImageView imageView;
        @BindView(R.id.tvAdName)
        TextView tvAddName;

        public AllConferenceViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
