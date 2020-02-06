package com.praxello.smartevent.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.praxello.smartevent.R;
import com.praxello.smartevent.utility.AllKeys;
import com.praxello.smartevent.utility.CommonMethods;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AboutActivity extends AppCompatActivity {

    @BindView(R.id.iv_first_image)
    public ImageView ivFirstImage;
    @BindView(R.id.iv_second_image)
    public ImageView ivSecondImage;
    @BindView(R.id.tv_conference_title)
    public TextView tvConferenceTitle;
    @BindView(R.id.tv_address)
    public TextView tvAddress;
    @BindView(R.id.btn_getdirections)
    public AppCompatButton btnGetDirections;
    @BindView(R.id.tv_summary)
    public TextView tvSummary;

    private static final String TAG = "AboutActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);
        Toolbar toolbar = findViewById(R.id.toolbar_about);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("About");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);


        if (!CommonMethods.getPrefrence(AboutActivity.this, AllKeys.CONFERENCE_TITLE).equals(AllKeys.DNF)) {
            tvConferenceTitle.setText(CommonMethods.getPrefrence(AboutActivity.this, AllKeys.CONFERENCE_TITLE));
        }

        if (!CommonMethods.getPrefrence(AboutActivity.this, AllKeys.CONFERENCE_ADDRESS).equals(AllKeys.DNF)) {
            tvAddress.setText(CommonMethods.getPrefrence(AboutActivity.this, AllKeys.CONFERENCE_ADDRESS));
        }

        if (!CommonMethods.getPrefrence(AboutActivity.this, AllKeys.CONFERENCE_SUMMARY).equals(AllKeys.DNF)) {
            tvSummary.setText(CommonMethods.getPrefrence(AboutActivity.this, AllKeys.CONFERENCE_SUMMARY));
        }

        if (!CommonMethods.getPrefrence(AboutActivity.this, AllKeys.CONFERENCE_SPLASH_URL).equals(AllKeys.DNF)) {
            Glide.with(this).load(CommonMethods.getPrefrence(AboutActivity.this, AllKeys.CONFERENCE_SPLASH_URL)).into(ivFirstImage);
        }

        if (!CommonMethods.getPrefrence(AboutActivity.this, AllKeys.CONFERENCE_LOGO_URL).equals(AllKeys.DNF)) {
            Glide.with(this).load(CommonMethods.getPrefrence(AboutActivity.this, AllKeys.CONFERENCE_LOGO_URL)).into(ivSecondImage);
        }

        btnGetDirections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?daddr=%f,%f (%s)", Integer.parseInt(CommonMethods.getPrefrence(AboutActivity.this, AllKeys.CONFERENCE_LATITUDE)),Integer.parseInt(CommonMethods.getPrefrence(AboutActivity.this, AllKeys.CONFERENCE_LONGITUE)), "Where the party is at");
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                intent.setPackage("com.google.android.apps.maps");
                startActivity(intent);*/
                try {


                    Float destinationLatitude = Float.valueOf(CommonMethods.getPrefrence(AboutActivity.this, AllKeys.CONFERENCE_LATITUDE));
                    Float destinationLongitude = Float.valueOf(CommonMethods.getPrefrence(AboutActivity.this, AllKeys.CONFERENCE_LONGITUE));

                    //Log.e(TAG, "onClick: latitude" + destinationLatitude + "longitude" + destinationLongitude);

                    String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?daddr=%f,%f (%s)", destinationLatitude, destinationLongitude, "Where the conference is at");
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    intent.setPackage("com.google.android.apps.maps");
                    startActivity(intent);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
    }
}
