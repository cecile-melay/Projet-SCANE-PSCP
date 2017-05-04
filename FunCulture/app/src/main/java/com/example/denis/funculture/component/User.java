package com.example.denis.funculture.component;

/**
 * Created by denis on 30/04/2017.
 */

public class User {
    private int id;
    private int xp;
    private String prenom;
    private String nom;
    private String pseudo;
    private String dateNaiss;
    private int lvlSport;
    private int fc;
    private String ville;
    private String mail;
    private String pass;

    public User() {
        this.id = 0;
        this.xp = 0;
        this.prenom = "";
        this.nom = "";
        this.pseudo = "";
        this.dateNaiss = "";
        this.lvlSport = 0;
        this.fc = 0;
        this.ville = "";
        this.mail = "";
        this.pass = "";
    }

    public User(int id,String prenom, String nom, String dateNaiss, int lvlSport, int fc, String ville, String mail, String pass, String pseudo) {
        this(prenom, nom, dateNaiss, lvlSport, fc, ville, mail, pass, pseudo);
        this.id = id;
    }

    public User(String prenom, String nom, String dateNaiss, int lvlSport, int fc, String ville, String mail, String pass, String pseudo) {
        this.id = 0;
        this.xp = 0;
        this.prenom = prenom;
        this.nom = nom;
        this.pseudo = pseudo;
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

    public String getPseudo() {
        return pseudo;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public void setDateNaiss(String dateNaiss) {
        this.dateNaiss = dateNaiss;
    }

    public void setLvlSport(int lvlSport) {
        this.lvlSport = lvlSport;
    }

    public void setFc(int fc) {
        this.fc = fc;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }
}
