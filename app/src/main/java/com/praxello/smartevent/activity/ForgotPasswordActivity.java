package com.praxello.smartevent.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.praxello.smartevent.R;
import com.praxello.smartevent.model.NotificationData;
import com.praxello.smartevent.utility.CommonMethods;
import com.praxello.smartevent.utility.ConfiUrl;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.et_email_address)
    public EditText etEmailAddress;
    @BindView(R.id.btn_send)
    public AppCompatButton btnSend;
    @BindView(R.id.etPassword)
    public EditText etPassword;
   /* @BindView(R.id.etConfirmPassword)
    public EditText etConfirmPassword;
    @BindView(R.id.btn_reset_password)
    public AppCompatButton btnResetPassword;
    @BindView(R.id.ll_forgot_password)
    public LinearLayout llForgotPassword;
    @BindView(R.id.ll_create_new_password)
    public LinearLayout llCreateNewPassword;*/

    public static final String TAG = "ForgotPasswordActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        ButterKnife.bind(this);

        //Basic intialisation...
        btnSend.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send:
                if (CommonMethods.isNetworkAvailable(ForgotPasswordActivity.this)) {
                    if (isEmailValidated()) {
                        resetPassword(etEmailAddress.getText().toString());
                    }
                }
                break;

            /*case R.id.btn_reset_password:
                if(CommonMethods.isNetworkAvailable(ForgotPasswordActivity.this)){
                    if(isValidPassword()){
                        Intent intent=new Intent(ForgotPasswordActivity.this,LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
                    }
                }
                break;*/

        }
    }

    private void resetPassword(String email) {
        final ProgressDialog progress = new ProgressDialog(this);
        progress.setMessage("Please wait");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.show();
        progress.setCancelable(false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ConfiUrl.FORGOT_PASSWORD_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();

                NotificationData notificationData = gson.fromJson(response, NotificationData.class);

                Log.e(TAG, "onResponse: "+response );
                if (notificationData.getResponsecode().equals("200")) {
                    progress.dismiss();
                    Toast.makeText(ForgotPasswordActivity.this, notificationData.getMessage(), Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(ForgotPasswordActivity.this,LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
                   /* llForgotPassword.setVisibility(View.GONE);
                    llCreateNewPassword.setVisibility(View.VISIBLE);*/
                } else {
                    progress.dismiss();
                    Toast.makeText(ForgotPasswordActivity.this, notificationData.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progress.dismiss();
                Log.e(TAG, "onErrorResponse: " + error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("mobile", email);
                Log.e(TAG, "getParams: " + params);
                return params;
            }
        };
        RequestQueue mQueue = Volley.newRequestQueue(this);
        mQueue.add(stringRequest);
    }

    private boolean isEmailValidated() {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (etEmailAddress.getText().toString().isEmpty()) {
            etEmailAddress.setError("Email required!");
            etEmailAddress.setFocusable(true);
            etEmailAddress.requestFocus();
            return false;
        }

        if (!etEmailAddress.getText().toString().matches(emailPattern)) {
            etEmailAddress.setError("Invalid email!");
            etEmailAddress.setFocusable(true);
            etEmailAddress.requestFocus();
            return false;
        }
        return true;
    }
/*
    private boolean isValidPassword(){

        if (etPassword.getText().toString().isEmpty()) {
            etPassword.setError("Password required!");
            etPassword.setFocusable(true);
            etPassword.requestFocus();
            return false;
        }

        if (etConfirmPassword.getText().toString().isEmpty()) {
            etConfirmPassword.setError("Confirm password required!");
            etConfirmPassword.setFocusable(true);
            etConfirmPassword.requestFocus();
            return false;
        }

        if(!etPassword.getText().toString(). equals(etConfirmPassword.getText().toString())){
            etConfirmPassword.setError("Password does'nt match!");
            etConfirmPassword.setFocusable(true);
            etConfirmPassword.requestFocus();
            return false;
        }
        return true;
    }*/
}
