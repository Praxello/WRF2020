package com.praxello.smartevent.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.praxello.smartevent.R;
import com.praxello.smartevent.utility.AllKeys;
import com.praxello.smartevent.utility.CommonMethods;
import com.praxello.smartevent.utility.ConfiUrl;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class MyIdActivity extends AppCompatActivity implements View.OnClickListener {

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
    public static String TAG="MyIdActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_id);
        ButterKnife.bind(this);

        //basic intialisation...
        initViews();

        //set text to textview
        appendDataToText();
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

        tvPhone.setOnClickListener(this);
        tvMail.setOnClickListener(this);
    }

    private void appendDataToText(){
        if(!CommonMethods.getPrefrence(MyIdActivity.this, AllKeys.FIRST_NAME).equals(AllKeys.DNF)){
            tvName.setText(CommonMethods.getPrefrence(MyIdActivity.this, AllKeys.FIRST_NAME)+" "+CommonMethods.getPrefrence(MyIdActivity.this, AllKeys.LAST_NAME)+"  ("+CommonMethods.getPrefrence(MyIdActivity.this, AllKeys.USER_TYPE)+")");
        }

        if(!CommonMethods.getPrefrence(MyIdActivity.this, AllKeys.CITY).equals(AllKeys.DNF)){
            tvAddress.setText(CommonMethods.getPrefrence(MyIdActivity.this, AllKeys.CITY)+", "+CommonMethods.getPrefrence(MyIdActivity.this, AllKeys.STATE)+", "+CommonMethods.getPrefrence(MyIdActivity.this, AllKeys.COUNTRY));
        }

        if(!CommonMethods.getPrefrence(MyIdActivity.this,AllKeys.EMAIL).equals(AllKeys.DNF)){
            tvMail.setText(CommonMethods.getPrefrence(MyIdActivity.this,AllKeys.EMAIL));
        }

        if(!CommonMethods.getPrefrence(MyIdActivity.this,AllKeys.MOBILE).equals(AllKeys.DNF)){
            tvPhone.setText(CommonMethods.getPrefrence(MyIdActivity.this,AllKeys.MOBILE));
        }

        if(!CommonMethods.getPrefrence(MyIdActivity.this,AllKeys.CONFERENCE_ID).equals(AllKeys.DNF)){
            tvConferenceId.setText("ConferenceId: #Prax"+CommonMethods.getPrefrence(MyIdActivity.this,AllKeys.CONFERENCE_ID)+"2266");
        }

        Log.e(TAG, "appendDataToText: "+ConfiUrl.VIEW_PROFILE_PIC_URL+CommonMethods.getPrefrence(MyIdActivity.this, AllKeys.USER_ID)+".jpg");
        if(!CommonMethods.getPrefrence(MyIdActivity.this, AllKeys.USER_ID).equals(AllKeys.DNF)){
            Glide.with(this).load(ConfiUrl.VIEW_PROFILE_PIC_URL+CommonMethods.getPrefrence(MyIdActivity.this, AllKeys.USER_ID)+".jpg").diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(ivProfilePic);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.tv_phone:
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:"+CommonMethods.getPrefrence(MyIdActivity.this,AllKeys.MOBILE)));//change the number
                startActivity(callIntent);
                break;

            case R.id.tv_mail:
                if(!CommonMethods.getPrefrence(MyIdActivity.this,AllKeys.EMAIL).equals(AllKeys.DNF)) {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    //String[] recipients = {CommonMethods.getPrefrence(MyIdActivity.this, AllKeys.EMAIL)};
                    //intent.putExtra(Intent.EXTRA_EMAIL, recipients);
                    intent.setType("text/html");
                    intent.setPackage("com.google.android.gm");
                    startActivity(Intent.createChooser(intent, "Send mail"));
                }
                break;
        }
    }
}
