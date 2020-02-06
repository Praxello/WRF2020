package com.praxello.smartevent.activity.quiz;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.afollestad.materialdialogs.MaterialDialog;
import com.praxello.smartevent.R;
import com.praxello.smartevent.activity.retrofit.ApiRequestHelper;
import com.praxello.smartevent.activity.retrofit.WRFApp;
import com.praxello.smartevent.model.quiz.Question;
import com.praxello.smartevent.model.quiz.UserData;
import com.praxello.smartevent.utility.AllKeys;
import com.praxello.smartevent.utility.CommonMethods;
import butterknife.ButterKnife;

public class QuizFragment extends Fragment {

    //@BindView(R.id.tvQuestion)
    TextView tvQuestion;
    //@BindView(R.id.ivOption1)
    ImageView ivOption1;
   // @BindView(R.id.tvOption1)
    TextView tvOption1;
   // @BindView(R.id.rlOption1)
    RelativeLayout rlOption1;
    //@BindView(R.id.ivOption2)
    ImageView ivOption2;
    //@BindView(R.id.tvOption2)
    TextView tvOption2;
    //@BindView(R.id.rlOption2)
    RelativeLayout rlOption2;
    //@BindView(R.id.ivOption3)
    ImageView ivOption3;
    //@BindView(R.id.tvOption3)
    TextView tvOption3;
    //@BindView(R.id.rlOption3)
    RelativeLayout rlOption3;
    //@BindView(R.id.ivOption4)
    ImageView ivOption4;
    //@BindView(R.id.tvOption4)
    TextView tvOption4;
    //@BindView(R.id.rlOption4)
    RelativeLayout rlOption4;
    //@BindView(R.id.tvTimer)
    TextView tvTimer;
    //@BindView(R.id.tvNext)
    TextView tvNext;
    //@BindView(R.id.ivAnswer)
    ImageView ivAnswer;
    //@BindView(R.id.tvAnswer)
    TextView tvAnswer;
    public Question question;
    CountDownTimer cFirstTimer = null;
    boolean isQuestionAnswered = false;
    WRFApp WRFApp;
    private final static String TAG="QuizFragment";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = getArguments();
        if (arguments != null) {
            question = arguments.getParcelable("question");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_quiz,container,false);
        ButterKnife.bind(getContext(),view);
        WRFApp = (WRFApp) getActivity().getApplication();


        //basic intilaisation...
        initView(view);

        return view;
    }

    private void initView(View view){
        tvQuestion=view.findViewById(R.id.tvQuestion);
        tvTimer=view.findViewById(R.id.tvTimer);
        tvNext=view.findViewById(R.id.tvNext);
        tvAnswer=view.findViewById(R.id.tvAnswer);

        ivOption1=view.findViewById(R.id.ivOption1);
        ivOption2=view.findViewById(R.id.ivOption2);
        ivOption3=view.findViewById(R.id.ivOption3);
        ivOption4=view.findViewById(R.id.ivOption4);
        ivAnswer=view.findViewById(R.id.ivAnswer);

        tvOption1=view.findViewById(R.id.tvOption1);
        tvOption2=view.findViewById(R.id.tvOption2);
        tvOption3=view.findViewById(R.id.tvOption3);
        tvOption4=view.findViewById(R.id.tvOption4);

        rlOption1=view.findViewById(R.id.rlOption1);
        rlOption2=view.findViewById(R.id.rlOption2);
        rlOption3=view.findViewById(R.id.rlOption3);
        rlOption4=view.findViewById(R.id.rlOption4);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        tvQuestion.setText(question.getQuestion());
        tvOption1.setText(question.getOption1());
        tvOption2.setText(question.getOption2());
        tvOption3.setText(question.getOption3());
        tvOption4.setText(question.getOption4());
        tvAnswer.setText(question.getAnsdes());
        if (((QuizActivity) getContext()).questionData.getQuestions().size() - 1 == ((QuizActivity) getContext()).currentQuesPos) {
            tvNext.setText("Submit");
        }
        rlOption1.setOnClickListener(view1 -> {
            isQuestionAnswered = true;
            if (question.getCorrectoption().equals("1")) {
                ivOption1.setVisibility(View.VISIBLE);
                ((QuizActivity) getContext()).totalScore++;
                playAudio(true);
            } else {
                playAudio(false);
                showCorrectAnswer();
            }
            disableClicks();
        });
        rlOption2.setOnClickListener(view1 -> {
            isQuestionAnswered = true;
            if (question.getCorrectoption().equals("2")) {
                ivOption2.setVisibility(View.VISIBLE);
                ((QuizActivity) getContext()).totalScore++;
                playAudio(true);
            } else {
                playAudio(false);
                showCorrectAnswer();
            }
            disableClicks();
        });
        rlOption3.setOnClickListener(view1 -> {
            isQuestionAnswered = true;
            if (question.getCorrectoption().equals("3")) {
                ivOption3.setVisibility(View.VISIBLE);
                ((QuizActivity) getContext()).totalScore++;
                playAudio(true);
            } else {
                playAudio(false);
                showCorrectAnswer();
            }
            disableClicks();
        });
        rlOption4.setOnClickListener(view1 -> {
            isQuestionAnswered = true;
            if (question.getCorrectoption().equals("4")) {
                ivOption4.setVisibility(View.VISIBLE);
                ((QuizActivity) getContext()).totalScore++;
                playAudio(true);
            } else {
                playAudio(false);
                showCorrectAnswer();
            }
            disableClicks();
        });
        startFirstTimer(10);

        tvNext.setOnClickListener(view1 -> {
            if (!isQuestionAnswered) {
                Toast.makeText(getContext(), "Please answer the question", Toast.LENGTH_SHORT).show();
                return;
            }
            if (((QuizActivity) getContext()).questionData.getQuestions().size() - 1 == ((QuizActivity) getContext()).currentQuesPos) {
               /* Map<String, String> map = new HashMap<>();
                map.put("userid", CommonMethods.getPrefrence(getContext(), AllKeys.USER_ID));
                map.put("score", ""+((QuizActivity) getContext()).totalScore);

                Log.e(TAG, "onViewCreated: "+map );
                savequiz(map);*/
               savequiz(CommonMethods.getPrefrence(getContext(), AllKeys.USER_ID),""+((QuizActivity) getContext()).totalScore);
            } else {
                ((QuizActivity) getContext()).currentQuesPos++;
                ((QuizActivity) getContext()).loadQuizFragment();
            }
        });
    }

    private void savequiz(String userid,String score) {
        if (CommonMethods.isNetworkAvailable(getContext())) {
            //CustomProgressDialog pd = new CustomProgressDialog(mContext);
            //pd.show();
            WRFApp.getApiRequestHelper().savequiz(userid,score, new ApiRequestHelper.OnRequestComplete() {
                @Override
                public void onSuccess(Object object) {
                    //if (pd.isShowing()) pd.dismiss();/
                    UserData userData = (UserData) object;
                    //Log.e("in", "success");

                   // Log.e(TAG, "onSuccess: "+userData.getMessage() );
                    //Log.e(TAG, "onSuccess: "+userData.getResponsecode() );
                    if (userData != null) {
                        if (userData.getResponsecode() == 200) {
                            Toast.makeText(getContext(), "Answers submitted", Toast.LENGTH_SHORT).show();
                            new MaterialDialog.Builder(getContext())
                                    .title("Your score")
                                    .content(((QuizActivity) getContext()).totalScore + "/" + ((QuizActivity) getContext()).questionData.getQuestions().size())
                                    .positiveText("Ok")
                                    .cancelable(false)
                                    .onPositive((dialog, which) -> ((QuizActivity) getContext()).finish())
                                    .show();
                        } else {
                            if (userData.getMessage() != null && !TextUtils.isEmpty(userData.getMessage()))
                            Toast.makeText(getContext(), userData.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getContext(),AllKeys.SERVER_MESSAGE, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(String apiResponse) {
                   // Log.e("in", "error " + apiResponse);

                    Toast.makeText(getContext(), apiResponse, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getContext(), AllKeys.NO_INTERNET_AVAILABLE, Toast.LENGTH_SHORT).show();
        }
    }

    private void playAudio(boolean isCorrect) {
        MediaPlayer mp = MediaPlayer.create(getContext(), isCorrect ? R.raw.correct : R.raw.wrong);
        mp.start();
    }

    private void startFirstTimer(long sec) {
        long calculatedSec = sec * 1000 + 1000;
        cFirstTimer = new CountDownTimer(calculatedSec, 1000) {
            @Override
            public void onTick(long l) {
                //Log.e("l", "" + l);
                long seconds = l / 1000;
                //Log.e("seconds", "" + seconds);
                long minutes = seconds / 60;
                seconds %= 60;
                tvTimer.setText(String.format("%02d", seconds));
            }

            @Override
            public void onFinish() {
                isQuestionAnswered = true;
                showCorrectAnswer();
                playAudio(false);
                disableClicks();
            }
        };
        cFirstTimer.start();
    }

    private void showCorrectAnswer() {
        if (question.getCorrectoption().equals("1")) {
            ivOption1.setVisibility(View.VISIBLE);
        } else if (question.getCorrectoption().equals("2")) {
            ivOption2.setVisibility(View.VISIBLE);
        } else if (question.getCorrectoption().equals("3")) {
            ivOption3.setVisibility(View.VISIBLE);
        } else if (question.getCorrectoption().equals("4")) {
            ivOption4.setVisibility(View.VISIBLE);
        }
    }

    private void disableClicks() {
        cFirstTimer.cancel();
        rlOption1.setFocusable(false);
        rlOption2.setFocusable(false);
        rlOption3.setFocusable(false);
        rlOption4.setFocusable(false);
        rlOption1.setClickable(false);
        rlOption2.setClickable(false);
        rlOption3.setClickable(false);
        rlOption4.setClickable(false);
        ivAnswer.setVisibility(View.VISIBLE);
        tvAnswer.setVisibility(View.VISIBLE);
        tvTimer.setVisibility(View.GONE);
    }

    @Override
    public void onDestroy() {
        if (cFirstTimer != null) {
            cFirstTimer.cancel();
        }
        super.onDestroy();
    }
}