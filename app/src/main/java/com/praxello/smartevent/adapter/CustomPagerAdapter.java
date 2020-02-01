package com.praxello.smartevent.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.praxello.smartevent.R;
import com.praxello.smartevent.activity.LoginActivity;
import com.praxello.smartevent.activity.PreViewActivity;
import com.praxello.smartevent.model.advertisment.AdvertismentData;
import com.praxello.smartevent.utility.CommonMethods;

import java.util.ArrayList;

import butterknife.internal.Utils;

public class CustomPagerAdapter extends PagerAdapter {
    private final int widthPixels;
    private final float scale;
    Context mContext;
    LayoutInflater mLayoutInflater;
    ArrayList<AdvertismentData> advertisements;
    public static final String TAG="CustomPagerAdapter";
    int flag = 0;

    public CustomPagerAdapter(Context mContext, ArrayList<AdvertismentData> advertisements) {
        this.mContext = mContext;
        this.advertisements = advertisements;
        DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
        widthPixels = displayMetrics.widthPixels;
        scale = displayMetrics.density;
    }

    @Override
    public int getCount() {
        return advertisements.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = mLayoutInflater.inflate(R.layout.viewpager_image, container, false);
        ImageView iv_banner = itemView.findViewById(R.id.iv_banner);
        TextView tvAdName = itemView.findViewById(R.id.tvAdName);
        RelativeLayout rlCustomeAdapter = itemView.findViewById(R.id.rl_customeadapter);
        AdvertismentData advertisement = advertisements.get(position);
//        if (advertisement.getMediaType() != null) {
//            if (advertisement.getMediaType().equals("0")) {


        /*if(flag==1){

            Glide.with ( mContext ).load ( R.drawable.no_data ).into (iv_banner);
            iv_banner.setOnClickListener ( v->{
               *//* if (advertisement.getWebsitelink() != null && !TextUtils.isEmpty(advertisement.getWebsitelink())) {
                    Utils.openBrowser(mContext, advertisement.getWebsitelink());
                }*//*
            } );
            if( ! TextUtils.isEmpty ( advertisement.getAdTitle ( ) ) ){
                tvAdName.setText ( R.string.no_ads_available );
                tvAdName.setVisibility ( View.VISIBLE );
            }else{
                tvAdName.setVisibility ( View.GONE );
            }
        }*/


        // Log.e(TAG,"Inside if");
        if (advertisement.getAdType().equals("1") || advertisement.getAdType().equals("2")){
            if (advertisement.getAdImageUrl() != null && !TextUtils.isEmpty(advertisement.getAdImageUrl())) {
                rlCustomeAdapter.setVisibility(View.VISIBLE);
                if(advertisements.get(position).getAdType().equals("1")){
                    Glide.with(mContext).load(advertisement.getAdImageUrl()).into(iv_banner);

                }else{
                    Glide.with(mContext).load("https://img.youtube.com/vi/"+(advertisement.getAdVideourl())+"/0.jpg").into(iv_banner);
                }


                iv_banner.setOnClickListener(v -> {
                /*if (advertisement.getWebsitelink() != null && !TextUtils.isEmpty(advertisement.getWebsitelink())) {
                    Utils.openBrowser(mContext, advertisement.getWebsitelink());
                }*/
                    if (advertisements.get(position).getAdType().equals("2")) {
                        if (advertisements.get(position).getAdVideourl() != null && !TextUtils.isEmpty(advertisements.get(position).getAdVideourl())) {
                           // CommonMethods.openBrowser(mContext, advertisements.get(position).getAdVideourl());
                            mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v="+advertisements.get(position).getAdVideourl())));

                        }
                    }

                    if (advertisements.get(position).getAdType().equals("1")) {
                        if (advertisements.get(position).getAdVideourl() != null && !TextUtils.isEmpty(advertisements.get(position).getAdVideourl())) {
                            //CommonMethods.openBrowser(mContext, advertisements.get(position).getAdImageUrl());
                            Activity activity = (Activity) mContext;
                            Intent intent=new Intent(mContext, PreViewActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtra("image_url",advertisement.getAdImageUrl());
                            intent.putExtra("toolbar_title","Preview");
                            mContext.startActivity(intent);
                            activity.overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
                        }
                    }
                });
            }
        if (!TextUtils.isEmpty(advertisement.getAdTitle())) {
            tvAdName.setText(advertisement.getAdTitle());
            tvAdName.setVisibility(View.VISIBLE);
        } else {
            tvAdName.setVisibility(View.GONE);
        }
    }
       /*else{

         Log.e(TAG,"Inside else");
            Glide.with(mContext).load(R.drawable.no_data).into(iv_banner);
            iv_banner.setOnClickListener(v -> {
               if (advertisement.getWebsitelink() != null && !TextUtils.isEmpty(advertisement.getWebsitelink())) {
                    Utils.openBrowser(mContext, advertisement.getWebsitelink());
                }
            });
            if (!TextUtils.isEmpty(advertisement.getAdTitle())) {
                tvAdName.setText(R.string.no_ads_available);
                tvAdName.setVisibility(View.VISIBLE);
            } else {
                tvAdName.setVisibility(View.GONE);
            }
        }*/

//            } else {
//                Glide.with(mContext).load("http://img.youtube.com/vi/" + banner.getWebsitelink() + "/0.jpg").into(iv_banner);
//                iv_banner.setOnClickListener(v -> {
//                    String url = banner.getWebsitelink();
//                    try {
//                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube://" + url));
//                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        mContext.startActivity(intent);
//                    } catch (ActivityNotFoundException e) {
//                        // youtube is not installed.Will be opened in other available apps
//                        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://youtube.com/watch?v=" + url));
//                        mContext.startActivity(i);
//                    }
//                });
//            }
//        }
        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }
}