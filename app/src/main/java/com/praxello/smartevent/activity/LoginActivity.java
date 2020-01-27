package com.praxello.smartevent.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
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
import com.praxello.smartevent.model.login.LoginResponse;
import com.praxello.smartevent.utility.CommonMethods;
import com.praxello.smartevent.utility.ConfiUrl;
import com.praxello.smartevent.utility.Constants;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.etMobileNumber)
    EditText etMobileNumber;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.btnlogin)
    AppCompatButton btnLogIn;
    @BindView(R.id.tv_forgotpassword)
    TextView tvForgotPassword;
    String token;
    public static final String TAG = "LoginActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);
        btnLogIn.setOnClickListener(this);
        tvForgotPassword.setOnClickListener(this);

        token = FirebaseInstanceId.getInstance().getToken();
        Toast.makeText(this, "token" + token, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnlogin:
                if(CommonMethods.isNetworkAvailable(LoginActivity.this)){
                    if(isValidated()) {
                        userAuthentication();
                    }
                }else{
                    Toast.makeText(this, Constants.NO_INTERNET_AVAILABLE, Toast.LENGTH_LONG).show();
                }

                break;

            case R.id.tv_forgotpassword:
                Intent intent=new Intent(LoginActivity.this,ForgotPasswordActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
        }
    }

    private void userAuthentication() {
        final ProgressDialog progress = new ProgressDialog(this);
        progress.setMessage("Please wait");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ConfiUrl.USER_LOGIN_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                //Log.e(TAG, "response" + response);
                LoginResponse loginResponse = gson.fromJson(response, LoginResponse.class);

                if (loginResponse.getResponsecode().equals("200")) {
                    progress.dismiss();
                    Toast.makeText(LoginActivity.this, loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    //save firebase data to server...
                    saveFirebaseCredential();
                    CommonMethods.setPreference(LoginActivity.this, Constants.USER_ID, loginResponse.getData().getUserId());
                    Intent mainIntent = new Intent(LoginActivity.this, DashBoardActivity.class);
                    mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(mainIntent);
                    finish();
                } else {
                    progress.dismiss();
                    Toast.makeText(LoginActivity.this, loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progress.dismiss();
                Log.e(TAG, "error" + error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("usrname", etMobileNumber.getText().toString());
                params.put("uuid", Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID));
                params.put("passwrd", etPassword.getText().toString());
                params.put("conferenceid", "1");

                Log.e(TAG, "params of user authentication" + params);
                return params;
            }
        };
        RequestQueue mQueue = Volley.newRequestQueue(this);
        mQueue.add(stringRequest);
    }

    private void saveFirebaseCredential() {
        final ProgressDialog progress = new ProgressDialog(this);
        progress.setMessage("Please wait");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ConfiUrl.REG_PUSH_NOTIFICATION, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();

                NotificationData notificationData = gson.fromJson(response, NotificationData.class);

                if (notificationData.getResponsecode().equals("200")) {
                    progress.dismiss();


                    Toast.makeText(LoginActivity.this, notificationData.getMessage(), Toast.LENGTH_SHORT).show();

                } else {
                    progress.dismiss();
                    Toast.makeText(LoginActivity.this, notificationData.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progress.dismiss();
                Toast.makeText(LoginActivity.this, Constants.SERVER_MESSAGE, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("userid", CommonMethods.getPrefrence(LoginActivity.this,Constants.USER_ID));
                params.put("deviceid", token);
                params.put("ostype", Build.MODEL);
                params.put("appversion", Build.VERSION.RELEASE);

                Log.e(TAG, "params" + params);
                return params;
            }
        };
        RequestQueue mQueue = Volley.newRequestQueue(this);
        mQueue.add(stringRequest);
    }

    private Boolean isValidated() {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (etMobileNumber.getText().toString().isEmpty()) {
            etMobileNumber.setError("Email required!");
            etMobileNumber.setFocusable(true);
            etMobileNumber.requestFocus();
            return false;
        }

        if (etPassword.getText().toString().isEmpty()) {
            etPassword.setError("Password required!");
            etPassword.setFocusable(true);
            etPassword.requestFocus();
            return false;
        }

        if(!etMobileNumber.getText().toString().matches(emailPattern)){
            etMobileNumber.setError("Invalid email!");
            etMobileNumber.setFocusable(true);
            etMobileNumber.requestFocus();
            return false;
        }

        return true;
    }
}
