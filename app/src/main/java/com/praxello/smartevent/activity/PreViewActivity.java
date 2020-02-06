package com.praxello.smartevent.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.praxello.smartevent.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.senab.photoview.PhotoViewAttacher;

public class PreViewActivity extends AppCompatActivity {

    @BindView(R.id.ivImageView)
    public ImageView ivPreviewImage;
    PhotoViewAttacher mAttacher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_view);
        ButterKnife.bind(this);

        Toolbar toolbar=findViewById(R.id.toolbar_preview);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        if(getIntent().getStringExtra("toolbar_title").equals("Preview")){
            toolbar.setTitle("Preview");
        }else{
            toolbar.setTitle("Floor Map");
        }

        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);


        if(!getIntent().getStringExtra("image_url").isEmpty()){
            Glide.with(this).load(getIntent().getStringExtra("image_url")).into(ivPreviewImage);
        }

        // Attach a PhotoViewAttacher, which takes care of all of the zooming functionality.
        mAttacher = new PhotoViewAttacher(ivPreviewImage);
        mAttacher.update();
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
