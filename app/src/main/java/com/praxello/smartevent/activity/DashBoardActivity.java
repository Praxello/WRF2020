package com.praxello.smartevent.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.praxello.smartevent.R;
import com.praxello.smartevent.activity.quiz.QuizActivity;
import com.praxello.smartevent.activity.quiz.ScoreBoardActivity;
import com.praxello.smartevent.activity.retrofit.ApiRequestHelper;
import com.praxello.smartevent.activity.retrofit.WRFApp;
import com.praxello.smartevent.adapter.CustomPagerAdapter;
import com.praxello.smartevent.adapter.MarqueeAdvertismentAdapter;
import com.praxello.smartevent.model.advertisment.AdvertismentData;
import com.praxello.smartevent.model.advertisment.AdvertismentResponse;
import com.praxello.smartevent.model.allattendee.AttendeeData;
import com.praxello.smartevent.model.allattendee.AttendeeResponse;
import com.praxello.smartevent.utility.CommonMethods;
import com.praxello.smartevent.utility.ConfiUrl;
import com.praxello.smartevent.utility.AllKeys;
import com.praxello.smartevent.widget.LoopViewPager;
import com.praxello.smartevent.widget.MarqueeView;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class DashBoardActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.toolbar_dashboard)
    Toolbar toolbar;
    @BindView(R.id.rrBanner)
    RelativeLayout rrBanner;
    @BindView(R.id.viewpager)
    LoopViewPager viewpager;
    @BindView(R.id.navView)
    NavigationView navigationView;
    @BindView(R.id.drawerLayout)
    DrawerLayout drawer;
    public final String TAG="DashBoardActivity";
    private static final int MESSAGE_SCROLL = 123;
    public MarqueeAdvertismentAdapter marqueeAdvertismentAdapter;
    @BindView(R.id.cardview_program)
    public CardView cvProgram;
    @BindView(R.id.cardview_cases)
    public CardView cvCases;
    @BindView(R.id.cardview_speakers)
    public CardView cvSpeaker;
    @BindView(R.id.cardview_about)
    public CardView cvAbout;
    @BindView(R.id.cardview_booths)
    public CardView cvBooths;
    @BindView(R.id.cardview_quiz)
    public CardView cvQuiz;
    @BindView(R.id.marquee)
    MarqueeView marqueeView;
    @BindView(R.id.iv_about)
    ImageView ivAbout;
    public static Map<Integer,AttendeeData> mapAttendeeData;
    public CircleImageView ivProfilePic;
    private static final String IMAGE_DIRECTORY = "/WRF2020";
    private int GALLERY = 1, CAMERA = 2;
    final String[] items = new String[]{"Capture photo from camera", "Select photo from gallery"};
    private static final int PICK_IMAGE = 1;
    private static final int REQUEST_CAMERA = 2;
    String imageBase64String;
    WRFApp WRFApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        ButterKnife.bind(this);
        Paper.init(this);
        WRFApp = (WRFApp) getApplication();

        //Basic intialisation....
        initViews();

        //load Ads
        loadAdvertisment();

        //loadAll Attendee...
        loadAllAttendee();
    }

    private void initViews(){
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //toolbar.setTitle("DashBoard");
        toolbar.setTitleTextColor(Color.WHITE);

        //Navigaiton intialisatio...
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(false);
        toggle.setHomeAsUpIndicator(R.drawable.ic_menu);
        toggle.syncState();
        toggle.setToolbarNavigationClickListener(view -> drawer.openDrawer(Gravity.LEFT));
        navigationView.setNavigationItemSelectedListener(this);

        //CircleImageView cvImage=navigationView.getHeaderView(0).findViewById(R.id.ivProfile);
        TextView tvName=navigationView.getHeaderView(0).findViewById(R.id.tvName);
        ivProfilePic=navigationView.getHeaderView(0).findViewById(R.id.ivProfile);

        if(!CommonMethods.getPrefrence(DashBoardActivity.this,AllKeys.FIRST_NAME).equals(AllKeys.DNF)){
            tvName.setText(CommonMethods.getPrefrence(DashBoardActivity.this, AllKeys.FIRST_NAME)+" "+CommonMethods.getPrefrence(DashBoardActivity.this, AllKeys.LAST_NAME));
        }

        if(!CommonMethods.getPrefrence(DashBoardActivity.this, AllKeys.USER_ID).equals(AllKeys.DNF)){
            Glide.with(this).load(ConfiUrl.VIEW_PROFILE_PIC_URL+CommonMethods.getPrefrence(DashBoardActivity.this, AllKeys.USER_ID)+".jpg").diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(ivProfilePic);
        }

        if(!CommonMethods.getPrefrence(DashBoardActivity.this, AllKeys.CONFERENCE_LOGO_URL).equals(AllKeys.DNF)){
            Glide.with(this).load(CommonMethods.getPrefrence(DashBoardActivity.this, AllKeys.CONFERENCE_LOGO_URL)).into(ivAbout);
        }

        //CardView object intialisation...
        cvProgram.setOnClickListener(this);
        cvCases.setOnClickListener(this);
        cvSpeaker.setOnClickListener(this);
        cvBooths.setOnClickListener(this);
        cvAbout.setOnClickListener(this);
        cvQuiz.setOnClickListener(this);

        ivProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //set_image();
                showPictureDialog();
            }
        });
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){

            case R.id.nav_home:break;

            case R.id.nav_myid:
                Intent intent=new Intent(DashBoardActivity.this,MyIdActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
                break;

            case R.id.nav_upload_profile:
                intent=new Intent(DashBoardActivity.this,UpdateProfileActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
                break;

            case R.id.nav_score_board:
                intent=new Intent(DashBoardActivity.this, ScoreBoardActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
                break;

            case R.id.nav_feedback:
                intent=new Intent(DashBoardActivity.this,FeedBackActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
                break;

            case R.id.nav_logout:
                new AlertDialog.Builder(DashBoardActivity.this)
                        .setTitle("WRF2020")
                        .setMessage("Are you sure you want to logout?")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Continue with delete operation
                                CommonMethods.setPreference(DashBoardActivity.this, AllKeys.USER_ID, AllKeys.DNF);
                                CommonMethods.setPreference(DashBoardActivity.this, AllKeys.SALUATION,AllKeys.DNF);
                                CommonMethods.setPreference(DashBoardActivity.this, AllKeys.FIRST_NAME, AllKeys.DNF);
                                CommonMethods.setPreference(DashBoardActivity.this, AllKeys.LAST_NAME,AllKeys.DNF);
                                CommonMethods.setPreference(DashBoardActivity.this, AllKeys.MOBILE, AllKeys.DNF);
                                CommonMethods.setPreference(DashBoardActivity.this, AllKeys.EMAIL,AllKeys.DNF);
                                CommonMethods.setPreference(DashBoardActivity.this, AllKeys.CITY,AllKeys.DNF);
                                CommonMethods.setPreference(DashBoardActivity.this, AllKeys.STATE,AllKeys.DNF);
                                CommonMethods.setPreference(DashBoardActivity.this, AllKeys.COUNTRY, AllKeys.DNF);
                                CommonMethods.setPreference(DashBoardActivity.this, AllKeys.PINCODE, AllKeys.DNF);
                                CommonMethods.setPreference(DashBoardActivity.this, AllKeys.DATEOFBIRTH,AllKeys.DNF);
                                CommonMethods.setPreference(DashBoardActivity.this, AllKeys.ADDRESS, AllKeys.DNF);
                                Intent intent=new Intent(DashBoardActivity.this,AllConferenceActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
                                Toast.makeText(DashBoardActivity.this, "See you again!", Toast.LENGTH_SHORT).show();
                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton("No", null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.cardview_program:
                Intent intent=new Intent(DashBoardActivity.this,AgendaDetailsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
                break;

            case R.id.cardview_cases:
                intent=new Intent(DashBoardActivity.this,CaseDescriptionActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
                break;

            case R.id.cardview_speakers:
                intent=new Intent(DashBoardActivity.this,SpeakerActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
                break;

            case R.id.cardview_booths:
                intent=new Intent(DashBoardActivity.this, PreViewActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("toolbar_title","Floor Map");
                intent.putExtra("image_url",CommonMethods.getPrefrence(DashBoardActivity.this,AllKeys.CONFERENCE_BOOTH_MAP_URL));
                startActivity(intent);
                overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
                break;

            case R.id.cardview_about:
                intent=new Intent(DashBoardActivity.this,AboutActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
                break;

            case R.id.cardview_quiz:
                intent=new Intent(DashBoardActivity.this, QuizActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
                break;

        }
    }

    public void loadAdvertisment() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ConfiUrl.ADVERTISMENT_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();

                Log.e(TAG, "response" + response);
                AdvertismentResponse advertismentResponse = gson.fromJson(response, AdvertismentResponse.class);

                if (advertismentResponse.Responsecode.equals("200")) {
                    if (advertismentResponse.getData().size() > 0) {

                        ArrayList<AdvertismentData> advertismentDataArrayList;
                        advertismentDataArrayList = new ArrayList<>();

                        ArrayList<AdvertismentData> advertismentDataArrayList1;
                        advertismentDataArrayList1 = new ArrayList<>();

                        for (int i = 0; i < advertismentResponse.getData().size(); i++) {
                            if (!advertismentResponse.getData().get(i).getAdType().equals("0")) {
                                advertismentDataArrayList.add(advertismentResponse.getData().get(i));
                            }

                            if (advertismentResponse.getData().get(i).getAdType().equals("0")) {
                                advertismentDataArrayList1.add(advertismentResponse.getData().get(i));
                            }

                            Log.e(TAG, "onResponse: advertisment size" + advertismentDataArrayList1.size());
                            Log.e(TAG, "onResponse: advertisment data" + advertismentDataArrayList1.toString());
                        }

                        String marqueeString = "";

                        for (AdvertismentData data : advertismentDataArrayList1) {
                            marqueeString += "    " + data.getAdTitle();

                        }

                        marqueeView.setText(marqueeString);
                        marqueeView.start();


                        if (advertismentDataArrayList.size() > 0) {
                            CustomPagerAdapter customPagerAdapter = new CustomPagerAdapter(DashBoardActivity.this, advertismentDataArrayList);
                            viewpager.setAdapter(customPagerAdapter);
                        } else {
                            rrBanner.setVisibility(View.GONE);
                        }

                        Log.e(TAG, "onResponse: advertisment data" + advertismentResponse.getData());

                    } else {
                        rrBanner.setVisibility(View.GONE);
                    }

                    if (advertismentResponse.getData().size() > 1) {
                        set_slider_animation();
                    } else {
                        try {
                            stopAutoPlay();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    Toast.makeText(DashBoardActivity.this, advertismentResponse.Message, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DashBoardActivity.this, AllKeys.SERVER_MESSAGE, Toast.LENGTH_SHORT).show();
                Log.e(TAG, "server error" + error);
            }
        });
        RequestQueue mQueue = Volley.newRequestQueue(this);
        mQueue.add(stringRequest);
    }


    @NonNull
    private RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(
                MultipartBody.FORM, descriptionString);
    }


    public void loadAllAttendee(){
        StringRequest stringRequest=new StringRequest(Request.Method.POST, ConfiUrl.ALL_ATTENDEE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson=new Gson();

                DashBoardActivity.mapAttendeeData = new HashMap();
                Log.e(TAG,"response"+response);
                AttendeeResponse attendeeResponse=gson.fromJson(response,AttendeeResponse.class);

                if(attendeeResponse.Responsecode.equals("200")){
                   // Paper.book().write("allattendee", attendeeResponse.getData());
                    for(AttendeeData temp : attendeeResponse.getData())
                    {
                        DashBoardActivity.mapAttendeeData.put(temp.getUserId(),temp);
                    }

                }else{
                    Toast.makeText(DashBoardActivity.this, attendeeResponse.Message, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DashBoardActivity.this, AllKeys.SERVER_MESSAGE, Toast.LENGTH_SHORT).show();
                Log.e(TAG,"server error"+error);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params=new HashMap<>();
                params.put("conferenceid",CommonMethods.getPrefrence(DashBoardActivity.this,AllKeys.CONFERENCE_ID));

                return params;
            }
        };
        RequestQueue mQueue= Volley.newRequestQueue(this);
        mQueue.add(stringRequest);
    }

    private void showPictureDialog(){
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {"Capture photo from camera",
                "Select photo from gallery"
        };
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                takePhotoFromCamera();
                                break;
                            case 1:
                                choosePhotoFromGallary();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {
            return;
        }

        if (requestCode == GALLERY && resultCode == RESULT_OK) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    //String path = saveImage(bitmap);
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG,50,out);

                    byte[] byteArray = out.toByteArray();
                    Bitmap compressedBitmap = BitmapFactory.decodeByteArray(byteArray,0,byteArray.length);
                    ivProfilePic.setImageBitmap(compressedBitmap);

                    String selectedImagePath =  getRealPathFromURIForGallery(contentURI);
                    uploadImageRetrofit(selectedImagePath);

                    //Toast.makeText(DashBoardActivity.this, "Image Saved!", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(DashBoardActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA && resultCode == RESULT_OK) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            thumbnail.compress(Bitmap.CompressFormat.PNG, 100, out);
            Bitmap decodedImage= BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));
            ivProfilePic.setImageBitmap(decodedImage);

            byte[] byteArray = out .toByteArray();

            // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
            Uri tempUri = getImageUri(DashBoardActivity.this, thumbnail);

            // CALL THIS METHOD TO GET THE ACTUAL PATH
            File finalFile = new File(getRealPathFromURI(tempUri));
            imageBase64String = Base64.encodeToString(byteArray, Base64.DEFAULT);
            uploadImageRetrofit(finalFile.getAbsolutePath());

            Log.e(TAG, "onActivityResult:decoded bitmap "+imageBase64String );

            //Toast.makeText(DashBoardActivity.this, "Image Saved!", Toast.LENGTH_SHORT).show();
        }
    }

    public String getRealPathFromURIForGallery(Uri uri) {
        if (uri == null) {
            return null;
        }
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = DashBoardActivity.this.getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        return uri.getPath();
    }

    public static Bitmap decodeSampledBitmapFromResource(String strPath, int reqWidth, int reqHeight) {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
//        if (Build.VERSION.SDK_INT < 21) {
//            options.inPurgeable = true;
//        }else {
//            options.inBitmap= true;
//        }
        BitmapFactory.decodeFile(strPath, options);
        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(strPath, options);
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = DashBoardActivity.this.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            // Calculate the largest inSampleSize value that is a power of 2 and
            // keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    public void uploadImageRetrofit(String filePath){

        Bitmap bm;
        File file = new File(filePath);
        long fileSizeInBytes = 0;
        if (file.length() > 0) {
            fileSizeInBytes = file.length();
        }
        long fileSizeInKB = 0;
        if (fileSizeInBytes > 1024) {
            fileSizeInKB = fileSizeInBytes / 1024;
        }
        if (fileSizeInKB > 500) {
            bm = decodeSampledBitmapFromResource(filePath, 400, 200);
        } else {
            bm = BitmapFactory.decodeFile(filePath);
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 40, baos); //bm is the bitmap object
        byte[] b = baos.toByteArray();
        imageBase64String= Base64.encodeToString(b, Base64.DEFAULT);

        HashMap<String, RequestBody> map = new HashMap<>();
        map.put("imageName", createPartFromString("profilepics/"+CommonMethods.getPrefrence(DashBoardActivity.this,AllKeys.USER_ID)+".jpg"));
        map.put("angle", createPartFromString("0"));
        map.put("imageData", createPartFromString(imageBase64String));

            WRFApp.getApiRequestHelper().uploadimage(map, new ApiRequestHelper.OnRequestComplete() {
                @Override
                public void onSuccess(Object object) {
                    Toast.makeText(DashBoardActivity.this, "profile updated successful", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(String apiResponse) {
                    Toast.makeText(DashBoardActivity.this, apiResponse, Toast.LENGTH_SHORT).show();
                }
            });

    }

    @SuppressLint("ClickableViewAccessibility")
    private void set_slider_animation() {
        try {
            Field mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            int animationDuration = 1000;
            FixedSpeedScroller scroller = new FixedSpeedScroller(viewpager.getContext(), new AccelerateDecelerateInterpolator(), animationDuration);
            mScroller.set(viewpager, scroller);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        if (viewpager != null && viewpager.getAdapter() != null && viewpager.getAdapter().getCount() > 1)
            startAutoPlay();
        else
            stopAutoPlay();
        viewpager.setOnTouchListener((view, motionEvent) -> {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    stopAutoPlay();
                    break;
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    if (viewpager != null && viewpager.getAdapter() != null && viewpager.getAdapter().getCount() > 1)
                        startAutoPlay();
                    else
                        stopAutoPlay();
                    break;

            }
            return false;
        });
    }


    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MESSAGE_SCROLL) {
                if (viewpager != null) {
                    viewpager.setCurrentItem(viewpager.getCurrentItem() + 1);
                    startAutoPlay();
                }
            }
        }
    };

    public void stopAutoPlay() {
        handler.removeMessages(MESSAGE_SCROLL);
    }

    public void startAutoPlay() {
        stopAutoPlay();
        int homeColumnScrollInterval = 4;
        handler.sendEmptyMessageDelayed(MESSAGE_SCROLL, homeColumnScrollInterval * 1000);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    public class FixedSpeedScroller extends Scroller {
        int duration;

        public FixedSpeedScroller(Context context, int duration) {
            super(context);
            this.duration = duration;
        }

        public FixedSpeedScroller(Context context, Interpolator interpolator, int duration) {
            super(context, interpolator);
            this.duration = duration;
        }

        public FixedSpeedScroller(Context context, Interpolator interpolator, boolean flywheel, int duration) {
            super(context, interpolator, flywheel);
            this.duration = duration;
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, this.duration);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy) {
            super.startScroll(startX, startY, dx, dy, this.duration);
        }
    }
}