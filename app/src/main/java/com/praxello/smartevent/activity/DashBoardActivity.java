package com.praxello.smartevent.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.praxello.smartevent.R;
import com.praxello.smartevent.adapter.CustomPagerAdapter;
import com.praxello.smartevent.adapter.DashBoardAdapter;
import com.praxello.smartevent.adapter.MarqueeAdvertismentAdapter;
import com.praxello.smartevent.model.DashBoardData;
import com.praxello.smartevent.model.advertisment.AdvertismentResponse;
import com.praxello.smartevent.utility.ConfiUrl;
import com.praxello.smartevent.utility.Constants;
import com.praxello.smartevent.widget.LoopViewPager;

import java.lang.reflect.Field;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DashBoardActivity extends AppCompatActivity implements View.OnClickListener{

    @BindView(R.id.toolbar_dashboard)
    Toolbar toolbar;
    @BindView(R.id.rrBanner)
    RelativeLayout rrBanner;
    @BindView(R.id.viewpager)
    LoopViewPager viewpager;
    @BindView(R.id.rv_dashboard)
    RecyclerView rvDashBoardData;
    @BindView(R.id.rv_marqueeadvertisment)
    RecyclerView rvMarqueeAdvertisment;


    public final String TAG="DashBoardActivity";
    private static final int MESSAGE_SCROLL = 123;
    public MarqueeAdvertismentAdapter marqueeAdvertismentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        ButterKnife.bind(this);
        overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);

        //Basic intialisation....
        initViews();

        //load data to recyclerview of dashboard data..
        loadDashBoardData();

        //load Ads
        loadAdvertisment();
    }



    private void initViews(){
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //toolbar.setTitle("DashBoard");
        toolbar.setTitleTextColor(Color.WHITE);

        //Recycler View intialisation...
        rvDashBoardData.setLayoutManager(new GridLayoutManager(this,2));
        rvDashBoardData.setNestedScrollingEnabled(false);


        //rvMarqueeAdvertisment.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));

        LinearLayoutManager layoutManager = new LinearLayoutManager(DashBoardActivity.this) {
            @Override
            public void smoothScrollToPosition(RecyclerView recyclerView,RecyclerView.State state, int position) {
                LinearSmoothScroller smoothScroller = new LinearSmoothScroller(DashBoardActivity.this) {
                    private static final float SPEED = 4000f;// Change this value (default=25f)
                    @Override
                    protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                        return SPEED / displayMetrics.densityDpi;
                    }
                };
                smoothScroller.setTargetPosition(position);
                startSmoothScroll(smoothScroller);
            }

        };
        rvMarqueeAdvertisment.setLayoutManager(layoutManager);

        //rvMarqueeAdvertisment.setNestedScrollingEnabled(false);
    }

    private void loadDashBoardData() {
        ArrayList<DashBoardData> dashBoardDataArrayList=new ArrayList<>();
        dashBoardDataArrayList.add(new DashBoardData(R.string.program,R.drawable.ic_session));
        dashBoardDataArrayList.add(new DashBoardData(R.string.cases,R.drawable.ic_checklist));
        dashBoardDataArrayList.add(new DashBoardData(R.string.speaker,R.drawable.ic_lecture));
        dashBoardDataArrayList.add(new DashBoardData(R.string.booths,R.drawable.ic_booth));
        dashBoardDataArrayList.add(new DashBoardData(R.string.about_wrf,R.drawable.ic_info));
        dashBoardDataArrayList.add(new DashBoardData(R.string.quiz,R.drawable.ic_chat));
        dashBoardDataArrayList.add(new DashBoardData(R.string.sponsors,R.drawable.ic_sponsor));
        dashBoardDataArrayList.add(new DashBoardData(R.string.contact_us,R.drawable.ic_24_7));

        DashBoardAdapter dashBoardAdapter=new DashBoardAdapter(DashBoardActivity.this,dashBoardDataArrayList);
        rvDashBoardData.setAdapter(dashBoardAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            /*case R.id.ll_case_description:
                Intent intent=new Intent(DashBoardActivity.this,CaseDescriptionActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
                            break;

            case R.id.ll_agenda:
                intent=new Intent(DashBoardActivity.this,AgendaDetailsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
                break;*/

        }
    }

    public void loadAdvertisment(){
        StringRequest stringRequest=new StringRequest(Request.Method.POST, ConfiUrl.ADVERTISMENT_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson=new Gson();

                Log.e(TAG,"response"+response);
                AdvertismentResponse advertismentResponse=gson.fromJson(response,AdvertismentResponse.class);

                if(advertismentResponse.Responsecode.equals("200")){
                    if(advertismentResponse.getData().size()>0){

                            /*for(int i=0;i<advertismentResponse.getData().size();i++){
                                if(advertismentResponse.getData().get(i).getAdType().equals("1") || advertismentResponse.getData().get(i).getAdType().equals("2")){
                                    rrBanner.setVisibility(View.VISIBLE);

                                }else{
                                    rrBanner.setVisibility(View.GONE);
                                }
                            }*/
                        CustomPagerAdapter customPagerAdapter = new CustomPagerAdapter(DashBoardActivity.this,advertismentResponse.getData());
                        viewpager.setAdapter(customPagerAdapter);


                        marqueeAdvertismentAdapter=new MarqueeAdvertismentAdapter(DashBoardActivity.this,advertismentResponse.getData());
                        rvMarqueeAdvertisment.setAdapter(marqueeAdvertismentAdapter);

                        autoScroll();

                    }else{
                        rrBanner.setVisibility(View.GONE);
                    }

                    if (advertismentResponse.getData().size() > 1) {
                        set_slider_animation();
                    } else {
                        try {
                            stopAutoPlay();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }else{
                    Toast.makeText(DashBoardActivity.this, advertismentResponse.Message, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DashBoardActivity.this, Constants.SERVER_MESSAGE, Toast.LENGTH_SHORT).show();
                Log.e(TAG,"server error"+error);
            }
        });
        RequestQueue mQueue= Volley.newRequestQueue(this);
        mQueue.add(stringRequest);
    }

    public void autoScroll(){
        final int speedScroll = 0;
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            int count = 0;
            @Override
            public void run() {
                if(count == marqueeAdvertismentAdapter.getItemCount())
                    count =0;
                if(count < marqueeAdvertismentAdapter.getItemCount()){
                    rvMarqueeAdvertisment.smoothScrollToPosition(++count);
                    handler.postDelayed(this,speedScroll);
                }
            }
        };
        handler.postDelayed(runnable,speedScroll);
    }


    @SuppressLint("ClickableViewAccessibility")
    private void set_slider_animation() {
        try {
            Field mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            int animationDuration = 1000;
            FixedSpeedScroller scroller = new FixedSpeedScroller(viewpager.getContext(), new AccelerateDecelerateInterpolator(), animationDuration);
            mScroller.set(viewpager, scroller);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        if (viewpager != null && viewpager.getAdapter() != null && viewpager.getAdapter().getCount() > 1)
            startAutoPlay();
        else
            stopAutoPlay();
        viewpager.setOnTouchListener((view, motionEvent) -> {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    stopAutoPlay();
                    break;
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    if (viewpager != null && viewpager.getAdapter() != null && viewpager.getAdapter().getCount() > 1)
                        startAutoPlay();
                    else
                        stopAutoPlay();
                    break;

            }
            return false;
        });
    }


    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MESSAGE_SCROLL) {
                if (viewpager != null) {
                    viewpager.setCurrentItem(viewpager.getCurrentItem() + 1);
                    startAutoPlay();
                }
            }
        }
    };

    public void stopAutoPlay() {
        handler.removeMessages(MESSAGE_SCROLL);
    }

    public void startAutoPlay() {
        stopAutoPlay();
        int homeColumnScrollInterval = 4;
        handler.sendEmptyMessageDelayed(MESSAGE_SCROLL, homeColumnScrollInterval * 1000);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
    }

    public class FixedSpeedScroller extends Scroller {
        int duration;

        public FixedSpeedScroller(Context context, int duration) {
            super(context);
            this.duration = duration;
        }

        public FixedSpeedScroller(Context context, Interpolator interpolator, int duration) {
            super(context, interpolator);
            this.duration = duration;
        }

        public FixedSpeedScroller(Context context, Interpolator interpolator, boolean flywheel, int duration) {
            super(context, interpolator, flywheel);
            this.duration = duration;
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, this.duration);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy) {
            super.startScroll(startX, startY, dx, dy, this.duration);
        }
    }
}
