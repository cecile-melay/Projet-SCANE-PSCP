package com.example.denis.funculture.component.qcm;

import java.util.ArrayList;

/**
 * Created by denis on 01/06/2017.
 */

public class QCM {
    boolean alreadyDone;
    int id;
    private String name;
    private int idTag;
    private int xp;
    private ArrayList<Question> questions;

    public QCM(int id, String name) {
        this.id = id;
        this.name = name;
        this.questions = new ArrayList<>();
        this.alreadyDone = false;
    }

    public void addQuestion(Question question) {
        this.questions.add(question);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIdTag() {
        return idTag;
    }

    public void setIdTag(int id) {
        this.idTag = id;
    }

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public ArrayList<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<Question> questions) {
        this.questions = questions;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isAlreadyDone() {
        return alreadyDone;
    }

    public void setAlreadyDone(boolean alreadyDone) {
        this.alreadyDone = alreadyDone;
    }
}
