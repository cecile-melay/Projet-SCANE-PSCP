package com.example.denis.funculture.component;

import com.example.denis.funculture.fragments.Accueil;
import com.example.denis.funculture.fragments.EpreuveFragment;
import com.example.denis.funculture.fragments.SeConnecter;
import com.example.denis.funculture.utils.Util;

/**
 * Created by denis on 01/06/2017.
 */

public class Epreuve {
    int id;
    String name;
    String url;
    int xp;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public void launch() {
        Util.setCurrentEpreuve(this);
        Util.getMainActivity().startFragment(EpreuveFragment.class);
    }
}
