package com.praxello.smartevent.activity.quiz;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.praxello.smartevent.R;
import com.praxello.smartevent.activity.retrofit.ApiRequestHelper;
import com.praxello.smartevent.activity.retrofit.WRFApp;
import com.praxello.smartevent.model.quiz.QuestionData;
import com.praxello.smartevent.utility.AllKeys;
import com.praxello.smartevent.utility.CommonMethods;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;


public class QuizActivity extends AppCompatActivity {

   // @BindView(R.id.progressBar)
    ProgressBar progressBar;
    //@BindView(R.id.tv_error)
    TextView tvError;
    public QuestionData questionData;
    public int totalScore = 0;
    public int currentQuesPos = 0;
    WRFApp WRFApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        ButterKnife.bind(this);
        WRFApp = (WRFApp) getApplication();
        //basic intialisation...
        initViews();

        allquestions();
    }

    private void initViews(){
        //Toolbar intialisation...
        Toolbar toolbar=findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("Quiz");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);

        progressBar=findViewById(R.id.progressBar);
        tvError=findViewById(R.id.tv_error);
    }

    public void allquestions() {

        if (CommonMethods.isNetworkAvailable(QuizActivity.this)) {
            Map<String, String> map = new HashMap<>();
            map.put("userid", CommonMethods.getPrefrence(QuizActivity.this, AllKeys.USER_ID));
            progressBar.setVisibility(View.VISIBLE);

            WRFApp.getApiRequestHelper().allquestions(map, new ApiRequestHelper.OnRequestComplete() {
                @Override
                public void onSuccess(Object object) {
                    if (progressBar != null) progressBar.setVisibility(View.GONE);
                    questionData = (QuestionData) object;
                    if (questionData != null) {
                        if (questionData.getResponsecode() == 200) {
                            if (questionData.getQuestions() != null && questionData.getQuestions().size() > 0) {
                                loadQuizFragment();
                            }
                        } else if (!TextUtils.isEmpty(questionData.getMessage())) {
                            tvError.setVisibility(View.VISIBLE);
                            tvError.setText(questionData.getMessage());
                        }
                    } else {
                        Toast.makeText(QuizActivity.this, AllKeys.SERVER_MESSAGE, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(String apiResponse) {
                    if (progressBar != null) progressBar.setVisibility(View.GONE);
                    Log.e("in", "error " + apiResponse);
                    Toast.makeText(QuizActivity.this, apiResponse, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(QuizActivity.this, AllKeys.NO_INTERNET_AVAILABLE, Toast.LENGTH_SHORT).show();
        }

    }

    public void loadQuizFragment() {
        QuizFragment quizFragment = new QuizFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("question", questionData.getQuestions().get(currentQuesPos));
        quizFragment.setArguments(bundle);
        loadFragment(quizFragment);
    }

    public void loadFragment(Fragment fragment) {
        if (fragment != null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frameLayout, fragment);
            fragmentTransaction.commitAllowingStateLoss();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       // getMenuInflater().inflate(R.menu.menu_my_score, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

           /* case R.id.action_my_score: {
                startActivity(new Intent(mContext, MyScoreActivity.class));
                return true;
            }*/

            /*case android.R.id.home: {
                Utils.hideSoftKeyboard(this);
                finish();
                return true;
            }*/
        }
        return super.onOptionsItemSelected(item);
    }
}
