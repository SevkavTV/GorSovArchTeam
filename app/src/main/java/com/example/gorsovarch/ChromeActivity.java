package com.example.gorsovarch;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class ChromeActivity extends AppCompatActivity {
    WebView wb;
    public ChromeActivity ca;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chrome);
        getSupportActionBar().hide();
        wb = (WebView) findViewById(R.id.chromeWeb);
        wb.getSettings().setJavaScriptEnabled(true);
        wb.loadUrl("https://www.e-olymp.com/ru/");
        wb.setWebViewClient(new MyWebViewClient1());
    }
    @Override
    public void onBackPressed() {
        if(wb.canGoBack()) {
            wb.goBack();
        } else {
            super.onBackPressed();
        }
    }
    class MyWebViewClient1 extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.contains("https://www.e-olymp.com/ru/")) {
                view.loadUrl(url);
                return false;
            }else{
                Toast.makeText(ChromeActivity.this, "Сайт недоступний", Toast.LENGTH_SHORT).show();
            }
            return true;
        }
    }
}

