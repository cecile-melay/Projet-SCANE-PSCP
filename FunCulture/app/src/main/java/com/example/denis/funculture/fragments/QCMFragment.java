package com.example.denis.funculture.fragments;

import android.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.denis.funculture.R;
import com.example.denis.funculture.component.qcm.Answer;
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
    private static String qcm_title;
    private static final String TAG = "QCMActivity";
    private static ArrayList<Question> questions = new ArrayList<>();

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

    private void addTestQuestions() {
        Question question1 = new Question("Actualité", "Quel animal de compagnie est le plus adoré à Nice?");
        question1.addAnswer(new Answer(1, false, "A", "C'est le chat", true));
        question1.addAnswer(new Answer(2, false, "B", "C'est le chien", false));
        question1.addAnswer(new Answer(3, false, "C", "C'est le lapin", false));
        question1.addAnswer(new Answer(4, false, "D", "C'est le hamster", false));

        Question question2 = new Question("Sport", "Quel sport est le plus médiatisé en France ?");
        question2.addAnswer(new Answer(5, false, "A", "C'est le football", true));
        question2.addAnswer(new Answer(6, false, "B", "C'est le handball", false));
        question2.addAnswer(new Answer(7, false, "C", "C'est le volleyball", false));
        question2.addAnswer(new Answer(8, false, "D", "C'est le curling", false));

        Question question3 = new Question("Spectacle", "Quel événement connait le plus de succès dans le sud ?");
        question3.addAnswer(new Answer(9, false, "A", "C'est le théâtre", true));
        question3.addAnswer(new Answer(10, false, "B", "C'est le cinéma", false));
        question3.addAnswer(new Answer(11, false, "C", "C'est le stand up", false));
        question3.addAnswer(new Answer(12, false, "D", "C'est les claquettes", false));

        questions.add(question1);
        questions.add(question2);
        questions.add(question3);
    }

    protected void init() {
        super.init();
        addTestQuestions();

        this.score = 0;
        this.currentQuestion = -1;
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
        tvTitle.setText(qcm_title);

        if(questions.size()>0) {
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
        if(currentQuestion<questions.size()) {
            Question question = questions.get(currentQuestion);
            tvCategory.setText(question.getCategory());
            tvQuestion.setText(question.getLabel());
            tvCount.setText((currentQuestion+1) + "/" + questions.size());
            this.adapter = new QCMAnswerAdapter(getActivity(), question.getAnswers());
            ll_answers.removeAllViews();
            for(int i=0; i<adapter.getCount(); i++) {
                ll_answers.addView(adapter.getView(i, null, ll_answers));
            }

            if(currentQuestion == questions.size()-1) {
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

    public static void addQuestion(Question questions) {
        QCMFragment.questions.add(questions);
    }

    public static void setQCMTitle(String QCMTitle) {
        QCMFragment.qcm_title = QCMTitle;
    }

    public static void refreshQuestions() {
        QCMFragment.questions = new ArrayList<>();
    }
}
