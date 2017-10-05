package com.bignerdranch.ianr.geoquiz;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends AppCompatActivity {

    private static final String TAG = "CheatActivity";
    private static final String CHOSE_CHEAT = "choseCheat";
    private static final String CHEATS_REMAINING = "cheats";

    private static final String EXTRA_ANSWER_IS_TRUE = "com.bignerdranch.ianr.geoquiz.answer_is_true";
    private static final String EXTRA_ANSWER_SHOWN = "com.bignerdranch.ianr.geoquiz.answer_shown";
    private static final String EXTRA_NUMBER_CHEATS = "com.bignerdranch.ianr.geoquiz.number_cheats";


    private TextView mAnswerTextView;
    private TextView mAPILevelTextView;
    private TextView mCheatsRemainingTextView;
    private Button mShowAnswerButton;

    private boolean mAnswerIsTrue;
    private boolean mChoseCheat;
    private int mNumberOfCheats = 0;

    public static Intent newIntent(Context packageContext, boolean answerIsTrue, int numberOfCheats) {
        Intent intent = new Intent(packageContext, CheatActivity.class);
        intent.putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue);
        intent.putExtra(EXTRA_NUMBER_CHEATS, numberOfCheats);
        return intent;
    }

    public static boolean wasAnswerShown(Intent result) {
        return result.getBooleanExtra(EXTRA_ANSWER_SHOWN, false);
    }

    public static int getNumberOfCheats(Intent result) {
        return result.getIntExtra(EXTRA_NUMBER_CHEATS, 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);

        mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);
        mNumberOfCheats = getIntent().getIntExtra(EXTRA_NUMBER_CHEATS, 0);

        if (savedInstanceState != null) {
            mNumberOfCheats = savedInstanceState.getInt(CHEATS_REMAINING, 3);
            mChoseCheat = savedInstanceState.getBoolean(CHOSE_CHEAT, false);
        }

        mAnswerTextView = (TextView) findViewById(R.id.answer_text_view);
        mAPILevelTextView = (TextView) findViewById(R.id.api_text_view);
        mCheatsRemainingTextView = (TextView) findViewById(R.id.show_cheats_remaining);

        mAPILevelTextView.setText("API Level " + Build.VERSION.SDK_INT);
        mCheatsRemainingTextView.setText("Cheats Remaining: " + (3 - mNumberOfCheats));

        mShowAnswerButton = (Button) findViewById(R.id.show_answer_button);
        mShowAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAnswerIsTrue) {
                    mAnswerTextView.setText(R.string.true_button);
                }
                else {
                    mAnswerTextView.setText(R.string.false_button);
                }

                mNumberOfCheats++;
                mCheatsRemainingTextView.setText("Cheats Remaining: " + (3 - mNumberOfCheats));
                mChoseCheat = true;

                setAnswerShownResult(true, mNumberOfCheats);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    int cx = mShowAnswerButton.getWidth() / 2;
                    int cy = mShowAnswerButton.getHeight() / 2;
                    float radius = mShowAnswerButton.getWidth();
                    Animator anim = ViewAnimationUtils.createCircularReveal(mShowAnswerButton, cx, cy, radius, 0);
                    anim.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            mShowAnswerButton.setVisibility(View.INVISIBLE);
                        }
                    });
                    anim.start();
                } else {
                    mShowAnswerButton.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "OnStart() called");
    }

    @Override
    public void onResume() {
        super.onResume();
        mCheatsRemainingTextView.setText("Cheats Remaining: " + (3 - mNumberOfCheats));

        if (mChoseCheat){
            mShowAnswerButton.setVisibility(View.INVISIBLE);
        }
        else {
            mShowAnswerButton.setVisibility(View.VISIBLE);
        }

        //int question = mQuestionBank[mCurrentIndex].getTextResId();
        //mQuestionTextView.setText(question);

        //if (mAnswered == 1) {
        //    mTrueButton.setEnabled(false);
        //    mFalseButton.setEnabled(false);
        //}

        if(mChoseCheat){
            setAnswerShownResult(true, mNumberOfCheats);
        }
        Log.d(TAG, "onResume() called");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putInt(CHEATS_REMAINING, mNumberOfCheats);
        savedInstanceState.putBoolean(CHOSE_CHEAT, mChoseCheat);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }

    private void setAnswerShownResult(boolean isAnswerShown, int numberOfCheats){
        Intent data = new Intent();
        data.putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown);
        data.putExtra(EXTRA_NUMBER_CHEATS, numberOfCheats);
        setResult(RESULT_OK, data);
    }
}
