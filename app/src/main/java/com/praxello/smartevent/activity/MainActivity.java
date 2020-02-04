package com.praxello.smartevent.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.praxello.smartevent.R;
import com.praxello.smartevent.utility.CommonMethods;
import com.praxello.smartevent.utility.AllKeys;

public class MainActivity extends AppCompatActivity {


    private final int SPLASH_DISPLAY_DURATION=2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_main);

        String token = FirebaseInstanceId.getInstance().getToken();
        //Toast.makeText(this, "token" + token, Toast.LENGTH_SHORT).show();

        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                if(CommonMethods.getPrefrence(MainActivity.this, AllKeys.USER_ID).equals(AllKeys.DNF)){
                    Intent mainIntent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(mainIntent);
                    finish();
                }else{
                    Intent mainIntent = new Intent(MainActivity.this, AllConferenceActivity.class);
                    startActivity(mainIntent);
                    finish();
                }

            }
        }, SPLASH_DISPLAY_DURATION);
    }
}
