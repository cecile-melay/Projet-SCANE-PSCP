package com.example.denis.funculture.fragments;

import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.denis.funculture.R;
import com.example.denis.funculture.component.Epreuve;
import com.example.denis.funculture.utils.MyResources;
import com.example.denis.funculture.utils.Util;

/**
 * Created by Cécile on 10/04/2017.
 */

public class EpreuveFragment extends MyFragment {
    private Epreuve epreuve;
    private WebView webView;
    private TextView tvMessage;
    private EditText etResult;
    private Button btShowTip;
    private Button btValidate;

    @Override
    protected void init() {
        this.epreuve = Util.getCurrentEpreuve();
        this.webView = (WebView) contentView.findViewById(R.id.webview);
        this.tvMessage = (TextView) contentView.findViewById(R.id.tv_message);
        this.etResult = (EditText) contentView.findViewById(R.id.et_result);
        this.btShowTip = (Button) contentView.findViewById(R.id.bt_show_tip);
        this.btValidate = (Button) contentView.findViewById(R.id.bt_validate);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(Util.getMainActivity(), description, Toast.LENGTH_SHORT).show();
            }
        });

        if(epreuve.getUrl() != null) {
            webView.loadUrl(epreuve.getUrl());
        }

        btShowTip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.setVisibility(View.VISIBLE);
                btShowTip.setVisibility(View.GONE);
            }
        });

        btValidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.createToast(String.format(MyResources.SUCCESS_EPREUVE, epreuve.getXp()));
                finish(true);
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.epreuve_fragment;
    }

    @Override
    protected String getTitle() {
        return MyResources.CONNEXION;
    }
}
