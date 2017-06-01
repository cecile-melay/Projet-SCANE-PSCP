package com.example.denis.funculture.fragments;

import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.denis.funculture.R;
import com.example.denis.funculture.component.User;
import com.example.denis.funculture.utils.MyResources;
import com.example.denis.funculture.utils.MyServices;
import com.example.denis.funculture.utils.Util;

/**
 * Created by Cécile on 10/04/2017.
 */

public class WebFragment extends MyFragment {
    //Register Fields
    private WebView webView;

    @Override
    protected void init() {
        this.webView = (WebView) contentView.findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(Util.getMainActivity(), description, Toast.LENGTH_SHORT).show();
            }
        });
        webView.loadUrl("https://www.youtube.com/watch?v=YrHyM7jfOn4");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.web_fragment;
    }

    @Override
    protected String getTitle() {
        return MyResources.CONNEXION;
    }
}
