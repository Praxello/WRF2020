package com.praxello.smartevent.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.praxello.smartevent.R;
import com.praxello.smartevent.utility.AllKeys;
import com.praxello.smartevent.utility.CommonMethods;
import com.praxello.smartevent.utility.ConfiUrl;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executors;

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
    @BindView(R.id.iv_qr_code)
    ImageView ivQrCode;
    public static String TAG = "MyIdActivity";
    Thread thread;
    public final static int QRcodeWidth = 500;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_id);
        ButterKnife.bind(this);

        //basic intialisation...
        initViews();

        //set text to textview
        appendDataToText();


        byte[] data = new byte[0];
        data = CommonMethods.getPrefrence(MyIdActivity.this, AllKeys.USER_ID).getBytes(StandardCharsets.UTF_8);
        String base64 = Base64.encodeToString(data, Base64.DEFAULT);

        String genearteQrString = base64 + ":" + base64 + CommonMethods.getPrefrence(MyIdActivity.this, AllKeys.USER_ID) + ":" + CommonMethods.getPrefrence(MyIdActivity.this, AllKeys.CONFERENCE_ID);

        Log.e(TAG, "onCreate: " + genearteQrString);
/*
        Executors.newCachedThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    bitmap = TextToImageEncode(genearteQrString);
                } catch (WriterException e) {
                    e.printStackTrace();
                }
            }
        });

        Log.e(TAG, "onCreate: "+bitmap );
        if(bitmap!=null){
            ivQrCode.setImageBitmap(bitmap);
        }*/

    }

    private void initViews() {
        //toolbar intialisation...
        Toolbar toolbar = findViewById(R.id.toolbar_myid);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("My Id");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);

        tvPhone.setOnClickListener(this);
        tvMail.setOnClickListener(this);
    }

    private void appendDataToText() {
        if (!CommonMethods.getPrefrence(MyIdActivity.this, AllKeys.FIRST_NAME).equals(AllKeys.DNF)) {
            tvName.setText(CommonMethods.getPrefrence(MyIdActivity.this, AllKeys.FIRST_NAME) + " " + CommonMethods.getPrefrence(MyIdActivity.this, AllKeys.LAST_NAME) + "  (" + CommonMethods.getPrefrence(MyIdActivity.this, AllKeys.USER_TYPE) + ")");
        }

        if (!CommonMethods.getPrefrence(MyIdActivity.this, AllKeys.CITY).equals(AllKeys.DNF)) {
            tvAddress.setText(CommonMethods.getPrefrence(MyIdActivity.this, AllKeys.CITY) + ", " + CommonMethods.getPrefrence(MyIdActivity.this, AllKeys.STATE) + ", " + CommonMethods.getPrefrence(MyIdActivity.this, AllKeys.COUNTRY));
        }

        if (!CommonMethods.getPrefrence(MyIdActivity.this, AllKeys.EMAIL).equals(AllKeys.DNF)) {
            tvMail.setText(CommonMethods.getPrefrence(MyIdActivity.this, AllKeys.EMAIL));
        }

        if (!CommonMethods.getPrefrence(MyIdActivity.this, AllKeys.MOBILE).equals(AllKeys.DNF)) {
            tvPhone.setText(CommonMethods.getPrefrence(MyIdActivity.this, AllKeys.MOBILE));
        }

        if (!CommonMethods.getPrefrence(MyIdActivity.this, AllKeys.CONFERENCE_ID).equals(AllKeys.DNF)) {
            tvConferenceId.setText("ConferenceId: #Prax" + CommonMethods.getPrefrence(MyIdActivity.this, AllKeys.CONFERENCE_ID) + "2266");
        }

        //Log.e(TAG, "appendDataToText: "+ConfiUrl.VIEW_PROFILE_PIC_URL+CommonMethods.getPrefrence(MyIdActivity.this, AllKeys.USER_ID)+".jpg");
        if (!CommonMethods.getPrefrence(MyIdActivity.this, AllKeys.USER_ID).equals(AllKeys.DNF)) {
            Glide.with(this).load(ConfiUrl.VIEW_PROFILE_PIC_URL + CommonMethods.getPrefrence(MyIdActivity.this, AllKeys.USER_ID) + ".jpg").diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(ivProfilePic);
        }

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_phone:
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + CommonMethods.getPrefrence(MyIdActivity.this, AllKeys.MOBILE)));//change the number
                startActivity(callIntent);
                break;

            case R.id.tv_mail:
                if (!CommonMethods.getPrefrence(MyIdActivity.this, AllKeys.EMAIL).equals(AllKeys.DNF)) {
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

    Bitmap TextToImageEncode(String Value) throws WriterException {
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(
                    Value,
                    BarcodeFormat.QR_CODE,
                    QRcodeWidth, QRcodeWidth, null
            );

        } catch (IllegalArgumentException Illegalargumentexception) {

            return null;
        }
        int bitMatrixWidth = bitMatrix.getWidth();

        int bitMatrixHeight = bitMatrix.getHeight();

        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

        for (int y = 0; y < bitMatrixHeight; y++) {
            int offset = y * bitMatrixWidth;

            for (int x = 0; x < bitMatrixWidth; x++) {

                pixels[offset + x] = bitMatrix.get(x, y) ?
                        getResources().getColor(R.color.black) : getResources().getColor(R.color.white);
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);

        bitmap.setPixels(pixels, 0, 500, 0, 0, bitMatrixWidth, bitMatrixHeight);

        return bitmap;
    }

}
