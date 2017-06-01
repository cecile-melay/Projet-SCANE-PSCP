package com.example.denis.funculture.component.qcm;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by remi on 18/04/2017.
 */

public class Question {
    int id;
    private String category;
    private String label;
    private List<Answer> answers;

    public Question(String category, String label) {
        this.category = category;
        this.label = label;
        this.answers = new ArrayList<>();
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void addAnswer(Answer answers) {
        this.answers.add(answers);
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }
}
