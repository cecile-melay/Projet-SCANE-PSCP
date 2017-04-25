package com.example.denis.funculture.fragments;

import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.denis.funculture.R;
import com.example.denis.funculture.utils.MyResources;
import com.example.denis.funculture.utils.MyServices;
import com.example.denis.funculture.utils.Util;

/**
 * Created by denis on 25/04/2017.
 */

public class QueriesFragment extends MyFragment {
    Button btGetZones;
    Button btPostPoint;
    EditText etId;
    EditText etLat;
    EditText etLng;
    EditText etPos;
    EditText etPath;

    @Override
    protected void init() {
        super.init();

        btGetZones = (Button) this.contentView.findViewById(R.id.bt_getZones);
        btPostPoint = (Button) this.contentView.findViewById(R.id.bt_post_point);

        etId = (EditText) this.contentView.findViewById(R.id.et_id);
        etLat = (EditText) this.contentView.findViewById(R.id.et_lat);
        etLng = (EditText) this.contentView.findViewById(R.id.et_lng);
        etPos = (EditText) this.contentView.findViewById(R.id.et_pos);
        etPath = (EditText) this.contentView.findViewById(R.id.et_path);

        btGetZones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getZones();
            }
        });

        btPostPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postPoint();
            }
        });
    }

    private void postPoint() {
        if(Util.isEmpty(etId)
                || Util.isEmpty(etLat)
                || Util.isEmpty(etLng)
                || Util.isEmpty(etPos)
                || Util.isEmpty(etPath)) {
            Util.createDialog(MyResources.MISSING_FIELD_WARNING);
        } else {
            String id = etId.getText().toString();
            String lat = etLat.getText().toString();
            String lng = etLng.getText().toString();
            String pos = etPos.getText().toString();
            String path = etPath.getText().toString();

            MyServices.getSingleton().postPoint(id, lat, lng, pos, path);
        }
    }

    private void getZones() {
        MyServices.getSingleton().getZones();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.query_test_view;
    }

    @Override
    protected String getTitle() {
        return MyResources.QUERIES;
    }
}
