package com.example.denis.funculture.component;

/**
 * Created by denis on 30/04/2017.
 */

public class User {
    private String prenom;
    private String nom;
    private String dateNaiss;
    private int lvlSport;
    private int fc;
    private String ville;
    private String mail;
    private String pass;

    public User(String prenom, String nom, String dateNaiss, int lvlSport, int fc, String ville, String mail, String pass) {
        this.prenom = prenom;
        this.nom = nom;
        this.dateNaiss = dateNaiss;
        this.lvlSport = lvlSport;
        this.fc = fc;
        this.ville = ville;
        this.mail = mail;
        this.pass = pass;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getNom() {
        return nom;
    }

    public String getDateNaiss() {
        return dateNaiss;
    }

    public int getLvlSport() {
        return lvlSport;
    }

    public int getFc() {
        return fc;
    }

    public String getVille() {
        return ville;
    }

    public String getMail() {
        return mail;
    }

    public String getPass() {
        return pass;
    }
}
