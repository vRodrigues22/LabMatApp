package com.example.ipet.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.ipet.R;

public class LabirintoActivity extends AppCompatActivity {


        @SuppressLint("SetJavaScriptEnabled")
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_labirinto);

            WebView myWebView = findViewById(R.id.webview);
            myWebView.getSettings().setJavaScriptEnabled(true);
            myWebView.loadUrl("https://scratch.mit.edu/projects/1083440544");

        }
    }

