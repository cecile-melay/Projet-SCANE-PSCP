package com.example.denis.funculture.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.denis.funculture.utils.Util;

/**
 * Created by denis on 02/04/2017.
 */

public abstract class MyFragment extends Fragment {
    protected ViewGroup contentView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        this.contentView = (ViewGroup) inflater.inflate(getLayoutId(), null, false);
        init();
        return this.contentView;
    }

    protected void init() {
        Util.getMainActivity().setNavTitle(getTitle());
    }

    protected abstract int getLayoutId();

    protected abstract String getTitle();
}
