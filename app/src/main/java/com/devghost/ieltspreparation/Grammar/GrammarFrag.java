package com.devghost.ieltspreparation.Grammar;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.devghost.ieltspreparation.Models.DatabaseHelper;
import com.devghost.ieltspreparation.R;
public class GrammarFrag extends Fragment {

    WebView webView;
    public static String webLink = "";
    View view;
    private DatabaseHelper databaseHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_grammar, container, false);

        webView=view.findViewById(R.id.loadWebId);
        databaseHelper = new DatabaseHelper(requireContext());
        loadWebSettings();
        // Save the scores to the database
        int[] Scores = databaseHelper.getScores();

        Scores[4]+=Scores[4]+2;

        saveScores(Scores);


        webView.loadUrl(webLink);

        webView.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
                    webView.goBack();
                    return true;
                }
            }
            return false;
        });

        return view;
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void loadWebSettings(){
        WebSettings webSettings=webView.getSettings();
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webSettings.setAllowContentAccess(true);
        // webSettings.setAppCacheEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setUseWideViewPort(true);

        webView.setWebChromeClient(new WebChromeClient());
        webView.getSettings().setUserAgentString("Mozilla/5.0 (iPhone; CPU iPhone OS 8_3 like Mac OS X) AppleWebKit/600.14 (KHTML, like Gecko) Mobile/12F70");
    }

    private void saveScores(int[] scores) {
        databaseHelper.saveScores(scores);
    }
}