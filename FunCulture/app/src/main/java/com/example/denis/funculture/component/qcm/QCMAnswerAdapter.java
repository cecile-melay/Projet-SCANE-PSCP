package com.example.denis.funculture.component.qcm;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.denis.funculture.component.qcm.Answer;

import com.example.denis.funculture.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by remi on 18/04/2017.
 */

public class QCMAnswerAdapter  extends BaseAdapter{
    private List<Answer> answers;
    private Activity activity;

    public QCMAnswerAdapter(Activity activity, List<Answer> answers) {
        this.activity = activity;
        this.answers = answers;
    }

    @Override
    public int getCount() {
        if(answers == null) {
            return 0;
        }

        return answers.size();
    }

    @Override
    public Answer getItem(int position) {
        return answers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return answers.get(position).getProfileId();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Answer answer = getItem(position);

        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = activity.getLayoutInflater().inflate(R.layout.qcm_answer, parent, false);
            holder.ivCheck = (ImageView) convertView.findViewById(R.id.pv_check);
            holder.tvAnswer = (TextView) convertView.findViewById(R.id.tv_qcm_answer);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        answer.setContentView(convertView, holder.ivCheck);
        holder.tvAnswer.setText(answer.getLabel());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(Answer item : answers) {
                    item.setSelected(false);
                }
                answer.setSelected(true);
            }
        });

        return convertView;
    }

    public boolean answerSelected() {
        for(Answer answer : this.answers) {
            if(answer.isSelected()) {
                return true;
            }
        }

        return false;
    }

    private final static class ViewHolder {
        private TextView tvAnswer;
        private ImageView ivCheck;
    }

    public boolean currentAnswerIsTrue() {
        for(Answer answer : this.answers) {
            if(answer.isSelected()) {
                return answer.isTrue();
            }
        }

        return false;
    }
}
