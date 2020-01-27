package com.praxello.smartevent.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.praxello.smartevent.R;
import com.praxello.smartevent.model.NotificationData;
import com.praxello.smartevent.utility.ConfiUrl;
import com.praxello.smartevent.utility.Constants;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView (R.id.etMobileNumber) EditText etMobileNumber;
    @BindView (R.id.etPassword) EditText etPassword;
    @BindView (R.id.btnlogin) AppCompatButton btnLogIn;
    String token;
    public static final String TAG="LoginActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);
        btnLogIn.setOnClickListener(this);


        token= FirebaseInstanceId.getInstance().getToken();
        Toast.makeText(this, "token" +token, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.btnlogin:

                            //if(isValidated()){
                                //save firebase data to server...
                                saveFirebaseCredential();

                                Intent mainIntent = new Intent(LoginActivity.this, DashBoardActivity.class);
                                mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(mainIntent);
                                finish();
                           // }
                            break;
        }
    }

    private void saveFirebaseCredential() {
        StringRequest stringRequest=new StringRequest(Request.Method.POST, ConfiUrl.REG_PUSH_NOTIFICATION, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson=new Gson();

                NotificationData notificationData=gson.fromJson(response,NotificationData.class);

                if(notificationData.getResponsecode().equals("200")){
                    Toast.makeText(LoginActivity.this, notificationData.getMessage(), Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(LoginActivity.this, notificationData.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginActivity.this, Constants.SERVER_MESSAGE, Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params=new HashMap<>();
                params.put("userid","1");
                params.put("deviceid",token);
                params.put("ostype", Build.MODEL);
                params.put("appversion",Build.VERSION.RELEASE);

                Log.e(TAG,"params"+params);
                return params;
            }
        };
        RequestQueue mQueue= Volley.newRequestQueue(this);
        mQueue.add(stringRequest);
    }

    private Boolean isValidated(){

        String mobile_number=etMobileNumber.getText().toString();
        if(etMobileNumber.getText().toString().isEmpty()){
            etMobileNumber.setError("Mobile number required!");
            etMobileNumber.setFocusable(true);
            etMobileNumber.requestFocus();
            return false;
        }

        if(etPassword.getText().toString().isEmpty()){
            etPassword.setError("Password required!");
            etPassword.setFocusable(true);
            etPassword.requestFocus();
            return false;
        }

        if(etMobileNumber.length()!=10 || mobile_number.startsWith("1") || mobile_number.startsWith("2") ||
                mobile_number.startsWith("3") || mobile_number.startsWith("4") || mobile_number.startsWith("5") ){
            etMobileNumber.setError("Invalid mobile number!");
            etMobileNumber.setFocusable(true);
            etMobileNumber.requestFocus();
            return false;
        }

        if(etPassword.length()!=6){
            etPassword.setError("Password must be 6 chracter!");
            etPassword.setFocusable(true);
            etPassword.requestFocus();
            return false;
        }

        return true;
    }
}
