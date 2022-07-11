package com.rv882.miunlock;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.CookieManager;
import android.widget.TextView;
import android.graphics.Bitmap;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private final String LOGIN_URL = "https://account.xiaomi.com/pass/serviceLogin?sid=unlockApi&json=false&passive=true&hidden=false&_snsDefault=facebook&checkSafePhone=true&_locale=en";
    private WebView webview;
    private TextView loginData;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        getSupportActionBar().setTitle("Login");

        webview = findViewById(R.id.webview);
        webview.setWebViewClient(new MyWebViewClient());
        webview.loadUrl(LOGIN_URL);
        webview.getSettings().setJavaScriptEnabled(true);
        
        loginData = findViewById(R.id.loginData);
		loginData.setText("");
    }
    
    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url); // load the url
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url){
            String cookies = CookieManager.getInstance().getCookie(url);
            if (cookies != null) {
                StringBuilder sb = new StringBuilder();
                String[] cookiesSpl = cookies.split(";");
                for (String ar1 : cookiesSpl){
                    sb.append('\n');
                    sb.append(ar1.trim());
                }
                
                if (sb.length() > 0) {
                    loginData.append(sb.toString());
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (webview.canGoBack())
            webview.goBack();
        else
            super.onBackPressed();
	}
}
