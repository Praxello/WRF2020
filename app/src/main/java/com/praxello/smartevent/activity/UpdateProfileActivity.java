package com.praxello.smartevent.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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
import com.praxello.smartevent.model.login.LoginData;
import com.praxello.smartevent.model.login.LoginResponse;
import com.praxello.smartevent.utility.AllKeys;
import com.praxello.smartevent.utility.CommonMethods;
import com.praxello.smartevent.utility.ConfiUrl;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UpdateProfileActivity extends AppCompatActivity implements View.OnClickListener{

    @BindView(R.id.etFirstName)
    EditText etFirstName;
    @BindView(R.id.etLastName)
    EditText etLastName;
    @BindView(R.id.etMobileNumber)
    EditText etMobileNumber;
    @BindView(R.id.et_email_address)
    EditText etEmailAddress;
    @BindView(R.id.et_city)
    EditText etCity;
    @BindView(R.id.et_state)
    EditText etState;
    @BindView(R.id.et_country)
    EditText etCountry;
    @BindView(R.id.etPincode)
    EditText etPincode;
    @BindView(R.id.etbirthdate)
    EditText etDateOfBirth;
    @BindView(R.id.etaddress)
    EditText etAddress;
    @BindView(R.id.etpassword)
    EditText etPassword;
    @BindView(R.id.btnupdateprofile)
    AppCompatButton btnUpdateProfile;

    public static final String TAG="UpdateProfileActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        ButterKnife.bind(this);
        //Basic intialisation...
        initViews();

        //setData to editext..
        setDataToEditTexts();
    }

    private void initViews() {
        //Toolbar intialaisation..
        Toolbar toolbar=findViewById(R.id.toolbar_update_profile);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("Update Profile");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);

        //Button intialisation
        btnUpdateProfile.setOnClickListener(this);

    }

    private void setDataToEditTexts(){
        if(!CommonMethods.getPrefrence(UpdateProfileActivity.this, AllKeys.FIRST_NAME).equals(AllKeys.DNF)){
            etFirstName.setText(CommonMethods.getPrefrence(UpdateProfileActivity.this, AllKeys.SALUATION)+" "+CommonMethods.getPrefrence(UpdateProfileActivity.this, AllKeys.FIRST_NAME));
        }

        if(!CommonMethods.getPrefrence(UpdateProfileActivity.this, AllKeys.LAST_NAME).equals(AllKeys.DNF)){
            etLastName.setText(CommonMethods.getPrefrence(UpdateProfileActivity.this, AllKeys.LAST_NAME));
        }

        if(!CommonMethods.getPrefrence(UpdateProfileActivity.this, AllKeys.MOBILE).equals(AllKeys.DNF)){
            etMobileNumber.setText(CommonMethods.getPrefrence(UpdateProfileActivity.this, AllKeys.MOBILE));
        }

        if(!CommonMethods.getPrefrence(UpdateProfileActivity.this, AllKeys.EMAIL).equals(AllKeys.DNF)){
            etEmailAddress.setText(CommonMethods.getPrefrence(UpdateProfileActivity.this, AllKeys.EMAIL));
        }

        if(!CommonMethods.getPrefrence(UpdateProfileActivity.this, AllKeys.CITY).equals(AllKeys.DNF)){
            etCity.setText(CommonMethods.getPrefrence(UpdateProfileActivity.this, AllKeys.CITY));
        }

        if(!CommonMethods.getPrefrence(UpdateProfileActivity.this, AllKeys.STATE).equals(AllKeys.DNF)){
            etState.setText(CommonMethods.getPrefrence(UpdateProfileActivity.this, AllKeys.STATE));
        }

        if(!CommonMethods.getPrefrence(UpdateProfileActivity.this, AllKeys.COUNTRY).equals(AllKeys.DNF)){
            etCountry.setText(CommonMethods.getPrefrence(UpdateProfileActivity.this, AllKeys.COUNTRY));
        }

        if(!CommonMethods.getPrefrence(UpdateProfileActivity.this, AllKeys.PINCODE).equals(AllKeys.DNF)){
            etPincode.setText(CommonMethods.getPrefrence(UpdateProfileActivity.this, AllKeys.PINCODE));
        }

        if(!CommonMethods.getPrefrence(UpdateProfileActivity.this, AllKeys.DATEOFBIRTH).equals(AllKeys.DNF)){
            etDateOfBirth.setText(CommonMethods.getPrefrence(UpdateProfileActivity.this, AllKeys.DATEOFBIRTH));
        }

        if(!CommonMethods.getPrefrence(UpdateProfileActivity.this, AllKeys.ADDRESS).equals(AllKeys.DNF)){
            etAddress.setText(CommonMethods.getPrefrence(UpdateProfileActivity.this, AllKeys.ADDRESS));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnupdateprofile:
                if(CommonMethods.isNetworkAvailable(UpdateProfileActivity.this)){
                    if(isValidated()){
                        updateUserProfile();
                    }
                }
                break;
        }
    }

    private void updateUserProfile(){
        final ProgressDialog progress = new ProgressDialog(this);
        progress.setMessage("Updating please wait");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.show();
        StringRequest stringRequest=new StringRequest(Request.Method.POST, ConfiUrl.UPDATE_PROFILE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson=new Gson();

                LoginResponse loginResponse=gson.fromJson(response,LoginResponse.class);

                Log.e(TAG, "onResponse: "+response );
                if(loginResponse.getResponsecode().equals("200")){
                    progress.dismiss();
                    Toast.makeText(UpdateProfileActivity.this, loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    CommonMethods.setPreference(UpdateProfileActivity.this, AllKeys.USER_ID, loginResponse.getData().getUserId());
                    CommonMethods.setPreference(UpdateProfileActivity.this, AllKeys.SALUATION, loginResponse.getData().getSalutation());
                    CommonMethods.setPreference(UpdateProfileActivity.this, AllKeys.FIRST_NAME, loginResponse.getData().getFirstName());
                    CommonMethods.setPreference(UpdateProfileActivity.this, AllKeys.LAST_NAME, loginResponse.getData().getLastName());
                    CommonMethods.setPreference(UpdateProfileActivity.this, AllKeys.MOBILE, loginResponse.getData().getMobile());
                    CommonMethods.setPreference(UpdateProfileActivity.this, AllKeys.EMAIL, loginResponse.getData().getEmail());
                    CommonMethods.setPreference(UpdateProfileActivity.this, AllKeys.CITY, loginResponse.getData().getCity());
                    CommonMethods.setPreference(UpdateProfileActivity.this, AllKeys.STATE, loginResponse.getData().getState());
                    CommonMethods.setPreference(UpdateProfileActivity.this, AllKeys.COUNTRY, loginResponse.getData().getCountry());
                    CommonMethods.setPreference(UpdateProfileActivity.this, AllKeys.PINCODE, loginResponse.getData().getPincode());
                    CommonMethods.setPreference(UpdateProfileActivity.this, AllKeys.DATEOFBIRTH, loginResponse.getData().getBirthDate());
                    CommonMethods.setPreference(UpdateProfileActivity.this, AllKeys.ADDRESS, loginResponse.getData().getAddress());
                    Intent intent=new Intent(UpdateProfileActivity.this,DashBoardActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
                }else{
                    progress.dismiss();
                    Toast.makeText(UpdateProfileActivity.this, loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progress.dismiss();
                Log.e(TAG, "onErrorResponse: "+error );
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params=new HashMap<>();
                params.put("firstname",etFirstName.getText().toString());
                params.put("lastname",etLastName.getText().toString());
                params.put("mobile",etMobileNumber.getText().toString());
                params.put("email",etEmailAddress.getText().toString());
                params.put("address",etAddress.getText().toString());
                params.put("city",etCity.getText().toString());
                params.put("state",etState.getText().toString());
                params.put("pincode",etPincode.getText().toString());
                params.put("country",etCountry.getText().toString());
                params.put("password",etPassword.getText().toString());
                params.put("birthdate",etDateOfBirth.getText().toString());

                Log.e(TAG, "getParams: "+params);
                return params;
            }
        };
        RequestQueue mQueue= Volley.newRequestQueue(this);
        mQueue.add(stringRequest);
    }

    private Boolean isValidated(){
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if(etFirstName.getText().toString().isEmpty()){
            etFirstName.setText("Firstname required!");
            etFirstName.requestFocus();
            return false;
        }

        if(etLastName.getText().toString().isEmpty()){
            etLastName.setText("Lastname required!");
            etLastName.requestFocus();
            return false;
        }

        if(etMobileNumber.getText().toString().isEmpty()){
            etMobileNumber.setText("Mobile number required!");
            etMobileNumber.requestFocus();
            return false;
        }

        if(etMobileNumber.getText().toString().length()!=10){
            etMobileNumber.setText("Invalid mobile number!");
            etMobileNumber.requestFocus();
            return false;
        }

        if(etMobileNumber.getText().toString().startsWith("1") || etMobileNumber.getText().toString().startsWith("2") ||
                etMobileNumber.getText().toString().startsWith("3") || etMobileNumber.getText().toString().startsWith("4")
                    || etMobileNumber.getText().toString().startsWith("5")){

            etMobileNumber.setText("Invalid mobile number!");
            etMobileNumber.requestFocus();
            return false;
        }

        if(etEmailAddress.getText().toString().isEmpty()){
            etEmailAddress.setText("Email required!");
            etEmailAddress.requestFocus();
            return false;
        }

        if(!etEmailAddress.getText().toString().matches(emailPattern)){
            etEmailAddress.setError("Invalid email!");
            etEmailAddress.setFocusable(true);
            etEmailAddress.requestFocus();
            return false;
        }

        /*if(etCity.getText().toString().isEmpty()){
            etCity.setText("City required!");
            etCity.requestFocus();
            return false;
        }

        if(etState.getText().toString().isEmpty()){
            etState.setText("State required!");
            etState.requestFocus();
            return false;
        }

        if(etCountry.getText().toString().isEmpty()){
            etCountry.setText("Country required!");
            etCountry.requestFocus();
            return false;
        }*/

        if(etPincode.getText().toString().isEmpty()){
            etPincode.setText("Pincode required!");
            etPincode.requestFocus();
            return false;
        }



        return true;
    }
}
