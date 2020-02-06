package com.praxello.smartevent.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.praxello.smartevent.R;
import com.praxello.smartevent.utility.CommonMethods;
import butterknife.BindView;
import butterknife.ButterKnife;

public class WebviewActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    Context mContext;

    @BindView(R.id.webview)
    WebView webview;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    public static String TAG="WebViewActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
        setContentView(R.layout.activity_webview);
        ButterKnife.bind(this);

        //Log.e(TAG,"Url" +getIntent().getStringExtra("url"));
       // Log.e(TAG,"Type" +getIntent().getStringExtra("type"));

        try {
            mContext = WebviewActivity.this;
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            toolbar.setTitle(getIntent().getStringExtra("title"));
            toolbar.setTitleTextColor(Color.WHITE);
            toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
//        webview.setWebViewClient(new MyBrowser());
            String url = getIntent().getStringExtra("url");
            webview.getSettings().setLoadsImagesAutomatically(true);
            webview.getSettings().setJavaScriptEnabled(true);
            webview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
            if(getIntent().getStringExtra("type").equals("PDF")){
                webview.loadUrl("https://docs.google.com/viewer?url="+url);
            }else if(getIntent().getStringExtra("type").equals("ABOUT")){
                webview.loadUrl(url);
            }

            webview.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
                    progressBar.setVisibility(ProgressBar.VISIBLE);
                    webview.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onPageCommitVisible(WebView view, String url) {
                    super.onPageCommitVisible(view, url);

                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    progressBar.setVisibility(ProgressBar.GONE);
                    webview.setVisibility(View.VISIBLE);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                    CommonMethods.hideSoftKeyboard(WebviewActivity.this);
                finish();
                overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
    }
}