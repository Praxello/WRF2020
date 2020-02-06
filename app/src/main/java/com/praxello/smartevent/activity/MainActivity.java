package com.praxello.smartevent.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.praxello.smartevent.R;


public class MainActivity extends AppCompatActivity {


    private final int SPLASH_DISPLAY_DURATION = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_main);

        FirebaseInstanceId.getInstance().getToken();
        TextView tvTitle = findViewById(R.id.tv_app_title);
        Typeface face = Typeface.createFromAsset(getAssets(),
                "fonts/markerfeltthin.ttf");
        tvTitle.setTypeface(face);
        //Toast.makeText(this, "token" + token, Toast.LENGTH_SHORT).show();

        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent mainIntent = new Intent(MainActivity.this, AllConferenceActivity.class);
                startActivity(mainIntent);
                finish();

            }
        }, SPLASH_DISPLAY_DURATION);
    }
}
