package com.example.denis.funculture.fragments;

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
    Button btLoadPath;
    Button btPathInfos;
    EditText etLat;
    EditText etLng;
    EditText etPos;
    EditText etPath;
    EditText etPathId;

    @Override
    protected void init() {
        super.init();

        btGetZones = (Button) this.contentView.findViewById(R.id.bt_getZones);
        btPostPoint = (Button) this.contentView.findViewById(R.id.bt_post_point);
        btLoadPath = (Button) this.contentView.findViewById(R.id.bt_load_path);
        btPathInfos = (Button) this.contentView.findViewById(R.id.bt_show_path);

        etLat = (EditText) this.contentView.findViewById(R.id.et_lat);
        etLng = (EditText) this.contentView.findViewById(R.id.et_lng);
        etPos = (EditText) this.contentView.findViewById(R.id.et_pos);
        etPath = (EditText) this.contentView.findViewById(R.id.et_path);
        etPathId = (EditText) this.contentView.findViewById(R.id.et_path_id);

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

        btLoadPath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadPath();
            }
        });

        btPathInfos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Util.getCurrentPath() != null) {
                    Util.createDialog(Util.getCurrentPath().ToString());
                }
            }
        });
    }

    private void loadPath() {
        if(Util.isEmpty(etPathId)) {
            Util.createDialog(MyResources.MISSING_FIELD_WARNING);
        } else {
            int id = Integer.parseInt(etPathId.getText().toString());

            MyServices.getSingleton().loadPath(id, true);
        }
    }

    private void postPoint() {
        if(Util.isEmpty(etLat)
                || Util.isEmpty(etLng)
                || Util.isEmpty(etPos)
                || Util.isEmpty(etPath)) {
            Util.createDialog(MyResources.MISSING_FIELD_WARNING);
        } else {
            String lat = etLat.getText().toString();
            String lng = etLng.getText().toString();
            String pos = etPos.getText().toString();
            String path = etPath.getText().toString();

            MyServices.getSingleton().postPoint(
                    Double.parseDouble(lat),
                    Double.parseDouble(lng),
                    Integer.parseInt(pos),
                    Integer.parseInt(path));
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
