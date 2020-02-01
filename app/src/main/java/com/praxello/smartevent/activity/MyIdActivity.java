package com.praxello.smartevent.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.praxello.smartevent.R;
import com.praxello.smartevent.utility.AllKeys;
import com.praxello.smartevent.utility.CommonMethods;
import com.praxello.smartevent.utility.ConfiUrl;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class MyIdActivity extends AppCompatActivity {

    @BindView(R.id.iv_profilepic)
    CircleImageView ivProfilePic;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_mail)
    TextView tvMail;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.tv_confenceID)
    TextView tvConferenceId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_id);
        ButterKnife.bind(this);

        //basic intialisation...
        initViews();
    }

    private void initViews(){
        //toolbar intialisation...
        Toolbar toolbar=findViewById(R.id.toolbar_myid);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("My Id");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);

        tvName.setText(CommonMethods.getPrefrence(MyIdActivity.this, AllKeys.FIRST_NAME)+" "+CommonMethods.getPrefrence(MyIdActivity.this, AllKeys.LAST_NAME)+"  ("+CommonMethods.getPrefrence(MyIdActivity.this, AllKeys.USER_TYPE)+")");
        tvAddress.setText(CommonMethods.getPrefrence(MyIdActivity.this, AllKeys.CITY)+", "+CommonMethods.getPrefrence(MyIdActivity.this, AllKeys.STATE)+", "+CommonMethods.getPrefrence(MyIdActivity.this, AllKeys.COUNTRY));
        tvMail.setText(CommonMethods.getPrefrence(MyIdActivity.this,AllKeys.EMAIL));
        tvPhone.setText(CommonMethods.getPrefrence(MyIdActivity.this,AllKeys.MOBILE));
        tvConferenceId.setText("ConferenceId: #Prax"+CommonMethods.getPrefrence(MyIdActivity.this,AllKeys.CONFERENCE_ID)+"2266");
        Glide.with(this).load(ConfiUrl.VIEW_PROFILE_PIC_URL+CommonMethods.getPrefrence(MyIdActivity.this,AllKeys.USER_ID)+".jpg").into(ivProfilePic);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
    }
}
