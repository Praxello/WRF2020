package com.praxello.smartevent.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.praxello.smartevent.R;
import com.praxello.smartevent.adapter.CustomPagerAdapter;
import com.praxello.smartevent.adapter.DashBoardAdapter;
import com.praxello.smartevent.adapter.MarqueeAdvertismentAdapter;
import com.praxello.smartevent.model.DashBoardData;
import com.praxello.smartevent.model.advertisment.AdvertismentData;
import com.praxello.smartevent.model.advertisment.AdvertismentResponse;
import com.praxello.smartevent.model.allattendee.AttendeeData;
import com.praxello.smartevent.model.allattendee.AttendeeResponse;
import com.praxello.smartevent.utility.CommonMethods;
import com.praxello.smartevent.utility.ConfiUrl;
import com.praxello.smartevent.utility.AllKeys;
import com.praxello.smartevent.widget.LoopViewPager;
import com.praxello.smartevent.widget.MarqueeView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class DashBoardActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.toolbar_dashboard)
    Toolbar toolbar;
    @BindView(R.id.rrBanner)
    RelativeLayout rrBanner;
    @BindView(R.id.viewpager)
    LoopViewPager viewpager;
    /*@BindView(R.id.rv_dashboard)
    RecyclerView rvDashBoardData;
    @BindView(R.id.rv_marqueeadvertisment)
    RecyclerView rvMarqueeAdvertisment;*/
    @BindView(R.id.navView)
    NavigationView navigationView;
    @BindView(R.id.drawerLayout)
    DrawerLayout drawer;
    public final String TAG="DashBoardActivity";
    private static final int MESSAGE_SCROLL = 123;
    public MarqueeAdvertismentAdapter marqueeAdvertismentAdapter;
    @BindView(R.id.cardview_program)
    public CardView cvProgram;
    @BindView(R.id.cardview_cases)
    public CardView cvCases;
    @BindView(R.id.cardview_speakers)
    public CardView cvSpeaker;
    @BindView(R.id.cardview_about)
    public CardView cvAbout;
    @BindView(R.id.cardview_booths)
    public CardView cvBooths;
    @BindView(R.id.cardview_quiz)
    public CardView cvQuiz;
    @BindView(R.id.marquee)
    MarqueeView marqueeView;
    public static Map<Integer,AttendeeData> mapAttendeeData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        ButterKnife.bind(this);
        Paper.init(this);

        overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);

        //Basic intialisation....
        initViews();

        //load data to recyclerview of dashboard data..
        //loadDashBoardData();

        //load Ads
        loadAdvertisment();

        //loadAll Attendee...
        loadAllAttendee();
    }

    private void initViews(){
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //toolbar.setTitle("DashBoard");
        toolbar.setTitleTextColor(Color.WHITE);

        //Recycler View intialisation...
        //rvDashBoardData.setLayoutManager(new GridLayoutManager(this,2));
        //rvDashBoardData.setNestedScrollingEnabled(false);

        //Navigaiton intialisatio...
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(false);
        toggle.setHomeAsUpIndicator(R.drawable.ic_menu);
        toggle.syncState();
        toggle.setToolbarNavigationClickListener(view -> drawer.openDrawer(Gravity.LEFT));
        navigationView.setNavigationItemSelectedListener(this);

        //CircleImageView cvImage=navigationView.getHeaderView(0).findViewById(R.id.ivProfile);
        TextView tvName=navigationView.getHeaderView(0).findViewById(R.id.tvName);

        if(!CommonMethods.getPrefrence(DashBoardActivity.this,AllKeys.FIRST_NAME).equals(AllKeys.DNF)){
            tvName.setText(CommonMethods.getPrefrence(DashBoardActivity.this, AllKeys.FIRST_NAME)+" "+CommonMethods.getPrefrence(DashBoardActivity.this, AllKeys.LAST_NAME));
        }

        //CardView object intialisation...
        cvProgram.setOnClickListener(this);
        cvCases.setOnClickListener(this);
        cvSpeaker.setOnClickListener(this);
        cvBooths.setOnClickListener(this);
        cvAbout.setOnClickListener(this);
        cvQuiz.setOnClickListener(this);


       // rvMarqueeAdvertisment.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));

        /*LinearLayoutManager layoutManager = new LinearLayoutManager(DashBoardActivity.this) {
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

        };*/
        //rvMarqueeAdvertisment.setLayoutManager(layoutManager);

        //rvMarqueeAdvertisment.setNestedScrollingEnabled(false);
    }

   /* private void loadDashBoardData() {
        ArrayList<DashBoardData> dashBoardDataArrayList=new ArrayList<>();
        dashBoardDataArrayList.add(new DashBoardData(R.string.program,R.drawable.ic_completed_task));
        dashBoardDataArrayList.add(new DashBoardData(R.string.cases,R.drawable.ic_checklist));
        dashBoardDataArrayList.add(new DashBoardData(R.string.speaker,R.drawable.ic_lecture));
        dashBoardDataArrayList.add(new DashBoardData(R.string.booths,R.drawable.ic_booth));
        dashBoardDataArrayList.add(new DashBoardData(R.string.about_wrf,R.drawable.ic_info));
        dashBoardDataArrayList.add(new DashBoardData(R.string.quiz,R.drawable.ic_chat));
        dashBoardDataArrayList.add(new DashBoardData(R.string.sponsors,R.drawable.ic_sponsor));
        dashBoardDataArrayList.add(new DashBoardData(R.string.contact_us,R.drawable.ic_24_7));
        DashBoardAdapter dashBoardAdapter=new DashBoardAdapter(DashBoardActivity.this,dashBoardDataArrayList);
        rvDashBoardData.setAdapter(dashBoardAdapter);
    }*/

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){



            case R.id.nav_home:break;

            case R.id.nav_myid:
                Toast.makeText(this, "My id", Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_upload_profile:
                Intent intent=new Intent(DashBoardActivity.this,UpdateProfileActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
                break;

            case R.id.nav_logout:
                new AlertDialog.Builder(DashBoardActivity.this)
                        .setTitle("WRF2020")
                        .setMessage("Are you sure you want to logout?")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Continue with delete operation
                                CommonMethods.setPreference(DashBoardActivity.this, AllKeys.USER_ID, AllKeys.DNF);
                                CommonMethods.setPreference(DashBoardActivity.this, AllKeys.SALUATION,AllKeys.DNF);
                                CommonMethods.setPreference(DashBoardActivity.this, AllKeys.FIRST_NAME, AllKeys.DNF);
                                CommonMethods.setPreference(DashBoardActivity.this, AllKeys.LAST_NAME,AllKeys.DNF);
                                CommonMethods.setPreference(DashBoardActivity.this, AllKeys.MOBILE, AllKeys.DNF);
                                CommonMethods.setPreference(DashBoardActivity.this, AllKeys.EMAIL,AllKeys.DNF);
                                CommonMethods.setPreference(DashBoardActivity.this, AllKeys.CITY,AllKeys.DNF);
                                CommonMethods.setPreference(DashBoardActivity.this, AllKeys.STATE,AllKeys.DNF);
                                CommonMethods.setPreference(DashBoardActivity.this, AllKeys.COUNTRY, AllKeys.DNF);
                                CommonMethods.setPreference(DashBoardActivity.this, AllKeys.PINCODE, AllKeys.DNF);
                                CommonMethods.setPreference(DashBoardActivity.this, AllKeys.DATEOFBIRTH,AllKeys.DNF);
                                CommonMethods.setPreference(DashBoardActivity.this, AllKeys.ADDRESS, AllKeys.DNF);
                                Intent intent=new Intent(DashBoardActivity.this,AllConferenceActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
                                Toast.makeText(DashBoardActivity.this, "See you again!", Toast.LENGTH_SHORT).show();
                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton("No", null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.cardview_program:
                Intent intent=new Intent(DashBoardActivity.this,AgendaDetailsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
                break;

            case R.id.cardview_cases:
                intent=new Intent(DashBoardActivity.this,CaseDescriptionActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
                break;

            case R.id.cardview_speakers:
                intent=new Intent(DashBoardActivity.this,SpeakerActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
                break;

            case R.id.cardview_booths:

                break;

            case R.id.cardview_about:
                intent=new Intent(DashBoardActivity.this,AboutActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
                break;

            case R.id.cardview_quiz:
                break;

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

                        ArrayList<AdvertismentData> advertismentDataArrayList;
                        advertismentDataArrayList=new ArrayList<>();

                        ArrayList<AdvertismentData> advertismentDataArrayList1;
                        advertismentDataArrayList1=new ArrayList<>();
                        for(int i=0;i<advertismentResponse.getData().size();i++){
                            if(!advertismentResponse.getData().get(i).getAdType().equals("0")){
                                advertismentDataArrayList.add(advertismentResponse.getData().get(i));
                            }
                            if(advertismentResponse.getData().get(i).getAdType().equals("0")){
                                advertismentDataArrayList1.add(advertismentResponse.getData().get(i));
                            }
                        }
                        CustomPagerAdapter customPagerAdapter = new CustomPagerAdapter(DashBoardActivity.this,advertismentDataArrayList);
                        viewpager.setAdapter(customPagerAdapter);

                        Log.e(TAG, "onResponse: advertisment data"+advertismentResponse.getData() );

                           // if(advertismentData.getAdType().equals("0")) {
                                //found it!
                               // marqueeView.setText(advertismentDataArrayList1);
                               // marqueeView.start();
                           // }


                        /*marqueeAdvertismentAdapter=new MarqueeAdvertismentAdapter(DashBoardActivity.this,advertismentResponse.getData());
                        rvMarqueeAdvertisment.setAdapter(marqueeAdvertismentAdapter);
*/
                       // autoScroll();

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
                Toast.makeText(DashBoardActivity.this, AllKeys.SERVER_MESSAGE, Toast.LENGTH_SHORT).show();
                Log.e(TAG,"server error"+error);
            }
        });
        RequestQueue mQueue= Volley.newRequestQueue(this);
        mQueue.add(stringRequest);
    }

    public void loadAllAttendee(){
        StringRequest stringRequest=new StringRequest(Request.Method.POST, ConfiUrl.ALL_ATTENDEE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson=new Gson();

                DashBoardActivity.mapAttendeeData = new HashMap();
                Log.e(TAG,"response"+response);
                AttendeeResponse attendeeResponse=gson.fromJson(response,AttendeeResponse.class);

                if(attendeeResponse.Responsecode.equals("200")){
                   // Paper.book().write("allattendee", attendeeResponse.getData());
                    for(AttendeeData temp : attendeeResponse.getData())
                    {
                        DashBoardActivity.mapAttendeeData.put(temp.getUserId(),temp);
                    }

                }else{
                    Toast.makeText(DashBoardActivity.this, attendeeResponse.Message, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DashBoardActivity.this, AllKeys.SERVER_MESSAGE, Toast.LENGTH_SHORT).show();
                Log.e(TAG,"server error"+error);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params=new HashMap<>();
                params.put("conferenceid",CommonMethods.getPrefrence(DashBoardActivity.this,AllKeys.CONFERENCE_ID));

                return params;
            }
        };
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
                    //rvMarqueeAdvertisment.smoothScrollToPosition(++count);
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

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

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
