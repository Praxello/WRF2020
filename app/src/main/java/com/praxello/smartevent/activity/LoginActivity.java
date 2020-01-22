package com.praxello.smartevent.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.praxello.smartevent.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView (R.id.etMobileNumber) EditText etMobileNumber;
    @BindView (R.id.etPassword) EditText etPassword;
    @BindView (R.id.btnlogin) AppCompatButton btnLogIn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        btnLogIn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.btnlogin:
                            //if(isValidated()){
                                Intent mainIntent = new Intent(LoginActivity.this, DashBoardActivity.class);
                                mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(mainIntent);
                                finish();
                           // }
                            break;
        }
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
