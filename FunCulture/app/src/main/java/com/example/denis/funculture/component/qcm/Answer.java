package com.example.denis.funculture.component.qcm;

import android.view.View;
import android.widget.ImageView;

import com.example.denis.funculture.R;
import com.example.denis.funculture.utils.Util;

/**
 * Created by remi on 18/04/2017.
 */

public class Answer {
    private View contentView;
    private ImageView ivCheck;
    private String header;
    private String label;
    private long profileId;
    private boolean selected;
    private boolean isTrue;

    public Answer(long profileId, boolean selected, String header, String label, boolean isTrue) {
        this.profileId = profileId;
        this.selected = selected;
        this.header = header;
        this.label = label;
        this.isTrue = isTrue;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        if(this.contentView != null) {
            if(selected) {
                contentView.setBackgroundColor(Util.getMainActivity().getResources().getColor(R.color.gray));
                ivCheck.setVisibility(View.VISIBLE);
            } else {
                contentView.setBackgroundColor(Util.getMainActivity().getResources().getColor(R.color.gray_light));
                ivCheck.setVisibility(View.INVISIBLE);
            }
        }
        this.selected = selected;
    }

    public long getProfileId() {
        return profileId;
    }

    public void setProfileId(long profileId) {
        this.profileId = profileId;
    }

    public void setContentView(View contentView, ImageView ivCheck) {
        this.contentView = contentView;
        this.ivCheck = ivCheck;
        this.setSelected(this.selected);
    }

    public boolean isTrue() {
        return isTrue;
    }
}
