package com.example.denis.funculture.fragments;

import android.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.denis.funculture.R;
import com.example.denis.funculture.component.qcm.Answer;
import com.example.denis.funculture.component.qcm.QCM;
import com.example.denis.funculture.component.qcm.QCMAnswerAdapter;
import com.example.denis.funculture.component.qcm.Question;
import com.example.denis.funculture.utils.MyResources;
import com.example.denis.funculture.utils.Util;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created by remi on 18/04/2017.
 */

public class QCMFragment extends MyFragment {
    private static final String TAG = "QCMActivity";
    private QCM qcm;
    private LinearLayout ll_answers;
    private TextView tvTitle;
    private TextView tvTime;
    private TextView tvCategory;
    private TextView tvQuestion;
    private TextView tvCount;
    private Button btNext;
    private Time startTime;
    private int currentQuestion;
    private QCMAnswerAdapter adapter;
    private int score;
    private int duration;

    protected void init() {
        super.init();
        this.score = 0;
        this.currentQuestion = -1;
        this.qcm = Util.getCurrentPath().getQcm();
        if(qcm == null) {
            return;
        }

        ll_answers = (LinearLayout) this.contentView.findViewById(R.id.ll_qcm_answers);
        tvTime = (TextView) this.contentView.findViewById(R.id.tv_qcm_time);
        tvTitle = (TextView) this.contentView.findViewById(R.id.tv_qcm_title);
        tvCategory = (TextView) this.contentView.findViewById(R.id.tv_qcm_cat);
        tvQuestion = (TextView) this.contentView.findViewById(R.id.tv_qcm_question);
        tvCount = (TextView) this.contentView.findViewById(R.id.tv_qcm_count);
        btNext = (Button) this.contentView.findViewById(R.id.bt_qcm_next);

        btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextQuestion();
            }
        });
        tvTitle.setText(qcm.getName());

        if(qcm.getQuestions().size()>0) {
            launchTimer();
            nextQuestion();
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.qcm_layout;
    }

    @Override
    protected String getTitle() {
        return MyResources.QCM;
    }

    private void nextQuestion() {
        if(currentQuestion != -1) {
            if(this.adapter != null && this.adapter.answerSelected()) {
                if(this.adapter.currentAnswerIsTrue()) {
                    score ++;
                }
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(MyResources.QCM_NO_SELECTED);
                builder.setPositiveButton(MyResources.OK, null);
                builder.show();
                return;
            }
        }


        currentQuestion ++;
        if(currentQuestion<qcm.getQuestions().size()) {
            Question question = qcm.getQuestions().get(currentQuestion);
            tvCategory.setText(question.getCategory());
            tvQuestion.setText(question.getLabel());
            tvCount.setText((currentQuestion+1) + "/" + qcm.getQuestions().size());
            this.adapter = new QCMAnswerAdapter(getActivity(), question.getAnswers());
            ll_answers.removeAllViews();
            for(int i=0; i<adapter.getCount(); i++) {
                ll_answers.addView(adapter.getView(i, null, ll_answers));
            }

            if(currentQuestion == qcm.getQuestions().size()-1) {
                btNext.setText(MyResources.QCM_SEE_RESULT);
            }
        } else {
            endQcm();
        }
    }

    private void endQcm() {
        this.duration = getDuration();

        Util.createDialog(String.format(MyResources.END_QCM, this.score));
        finish(true);
        //send to server
        //display message and score
    }

    private void launchTimer() {
        long startTime;
        startTime = System.currentTimeMillis();
        this.startTime = new Time(startTime);

        Thread t = new Thread() {

            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        if(getActivity() == null) {
                            return;
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                updateTime();
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };

        t.start();
    }

    private int getDuration() {
        long diff = System.currentTimeMillis() - this.startTime.getTime();

        return (int) TimeUnit.MILLISECONDS.toSeconds(diff);
    }
    private void updateTime() {
        long diff = System.currentTimeMillis() - this.startTime.getTime();

        this.tvTime.setText(Util.formatTimeInMillis(diff));
    }
}
