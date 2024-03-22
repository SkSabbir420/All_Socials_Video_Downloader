/*
 * *
 *  * Created by Syed Usama Ahmad on 2/27/23, 1:22 AM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 2/26/23, 11:10 PM
 *
 */

package com.erickruo.allinonevideodownloader.facebookstorysaver.fbutils;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.erickruo.allinonevideodownloader.R;
import com.erickruo.allinonevideodownloader.utils.iUtils;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class LoginWithFB extends AppCompatActivity {
    public WebView mWebview;
    public ProgressBar progressBar;
    String TAG = "LoginWithFB";
    private final WebCrome WebCromeClass = new WebCrome(this);
    Map<String, String> extraHeaders = new HashMap<String, String>();
    int randomnumber = 0;


    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_login_with_fb);
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.logintofbtitle);

        randomnumber = iUtils.getRandomNumber(iUtils.UserAgentsListLogin.length);

        extraHeaders.put("x-requested-with", "XMLHttpRequest");

        initView();
        startWebview();
    }

    private void initView() {
        this.mWebview = findViewById(R.id.webView);
        this.progressBar = findViewById(R.id.progressBar);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void startWebview() {
        this.mWebview.getSettings().setJavaScriptEnabled(true);
        this.mWebview.getSettings().setDomStorageEnabled(true);
        this.mWebview.getSettings().setBuiltInZoomControls(true);
        this.mWebview.getSettings().setDisplayZoomControls(true);
        this.mWebview.getSettings().setUseWideViewPort(true);
        this.mWebview.getSettings().setLoadWithOverviewMode(true);
        this.mWebview.addJavascriptInterface(this, "FB");
        this.mWebview.getSettings().setPluginState(WebSettings.PluginState.ON);
        CookieManager.getInstance().setAcceptThirdPartyCookies(this.mWebview, true);
        this.mWebview.getSettings().setMixedContentMode(2);
        this.mWebview.setWebViewClient(new WebViewClass(this));
        this.mWebview.setWebChromeClient(this.WebCromeClass);
        CookieSyncManager.createInstance(this);
        CookieManager.getInstance().setAcceptCookie(true);
        CookieSyncManager.getInstance().startSync();
        try {

            this.mWebview.getSettings().setUserAgentString(iUtils.UserAgentsListLogin[randomnumber] + "");

        } catch (Exception e) {
            System.out.println("dsakdjasdjasd " + e.getMessage());

            this.mWebview.getSettings().setUserAgentString("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/104.0.0.0 Safari/537.36");

        }
        this.mWebview.loadUrl("https://www.facebook.com/", extraHeaders);


    }


    public void onDestroy() {
        try {
            this.mWebview.destroy();
            super.onDestroy();
        } catch (Throwable e) {
            e.printStackTrace();
        }

    }

    @JavascriptInterface
    public void keyFound(String str) {
        String sr = StringUtils.substringBetween(str, "token", "async_get_token");
        str = StringUtils.substringBetween(sr, "\":\"", "\"},");

        Log.e("tag2webSWmall", "cookie str " + str);

        Log.e("tag2", "cookie found " + CookieManager.getInstance().getCookie("https://www.facebook.com"));


        if (str != null && str.length() < 15) {
            Log.e("tag2", "key found but too small " + str);
            return;
        }
        Log.e("tag2", "key found " + str);
        if (FBhelper.valadateCooki(CookieManager.getInstance().getCookie("https://www.facebook.com"))) {
            Log.e("tag2", "cookie is not valid");
            Facebookprefloader sharedPrefsFor = new Facebookprefloader(LoginWithFB.this);

            sharedPrefsFor.SavePref(str, "true");
            Intent intent = new Intent();
            intent.putExtra("resultfb", "result");
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    private class WebCrome extends WebChromeClient {
        LoginWithFB authWithFacebookActivity;

        private WebCrome(LoginWithFB loginWithFB) {
            this.authWithFacebookActivity = null;
            this.authWithFacebookActivity = loginWithFB;
        }

        public void onProgressChanged(WebView webView, int i) {
            super.onProgressChanged(webView, i);
            if (i < 100) {
                this.authWithFacebookActivity.progressBar.setVisibility(View.VISIBLE);
                this.authWithFacebookActivity.mWebview.setVisibility(View.GONE);
                return;
            }
            this.authWithFacebookActivity.progressBar.setVisibility(View.GONE);
            this.authWithFacebookActivity.mWebview.setVisibility(View.VISIBLE);
        }
    }

    private class WebViewClass extends WebViewClient {
        LoginWithFB authWithFacebookActivity;

        private WebViewClass(LoginWithFB loginWithFB) {
            this.authWithFacebookActivity = loginWithFB;
        }

//         "token": "AQF6RVI6d25ao30:47:1646064254",
//                 "async_get_token": "AQwnRPDt_YxK059w9b77vGsrqXfg3Jt17Qe_0-WQtFYmU31e:47:1646064254"

        public void onPageFinished(WebView webView, String str) {
            super.onPageFinished(webView, str);
            Log.e("tag3", "pageFinished");
            Log.e("tag3.5", "pageURL " + str);

            try {
                webView.getSettings().setUserAgentString(iUtils.UserAgentsListLogin[randomnumber] + "");
            } catch (Exception e) {
                System.out.println("dsakdjasdjasd " + e.getMessage());
                webView.getSettings().setUserAgentString("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/104.0.0.0 Safari/537.36");
            }

            webView.loadUrl("javascript:FB.keyFound();");
            webView.loadUrl("javascript:var el = document.querySelector('body');" +
                    "FB.keyFound(el.innerHTML);");
        }

        public void onReceivedError(WebView webView, int i, String str, String str2) {
            super.onReceivedError(webView, i, str, str2);
            Log.e("tag3", "webview error " + i + " / " + str + " / " + str2);
        }

        private boolean loginActivity(WebView webView, String str) {
            if (str.contains("play.google.com/store/apps/details?id=com.instagram.android")) {
                Log.e(LoginWithFB.this.TAG, "first_if");
                Intent intent = new Intent("android.intent.action.VIEW");
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setData(Uri.parse("market://details?id=com.instagram.android"));
                LoginWithFB.this.startActivity(intent);
                Log.e(LoginWithFB.this.TAG, "play.google.com/store/apps/details?id=com.instagram.android");
                return true;
            } else if (str.contains("https://m.facebook.com/v2.2/dialog/oauth?channel")) {
                try {

                    webView.getSettings().setUserAgentString(iUtils.UserAgentsListLogin[randomnumber] + "");
                } catch (Exception e) {
                    System.out.println("dsakdjasdjasd " + e.getMessage());
                    webView.getSettings().setUserAgentString("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/104.0.0.0 Safari/537.36");
                }
                webView.loadUrl("https://www.instagram.com/accounts/login/", extraHeaders);
                return false;
            } else {
                try {

                    webView.getSettings().setUserAgentString(iUtils.UserAgentsListLogin[randomnumber] + "");
                } catch (Exception e) {
                    System.out.println("dsakdjasdjasd " + e.getMessage());
                    webView.getSettings().setUserAgentString("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/104.0.0.0 Safari/537.36");
                }
                webView.loadUrl(str, extraHeaders);
                String cookie = CookieManager.getInstance().getCookie(str);
                if (!FBhelper.valadateCooki(cookie)) {
                    return true;
                }
                String str2 = LoginWithFB.this.TAG;
                Log.e(str2, "login done cookie:" + cookie);
                Facebookprefloader sharedPrefsFor = new Facebookprefloader(LoginWithFB.this);
                sharedPrefsFor.SavePref(cookie, "true");
                return true;
            }
        }

        public void onReceivedError(WebView webView, WebResourceRequest webResourceRequest, WebResourceError webResourceError) {
            Log.e(LoginWithFB.this.TAG, "onReceivedError");
        }

        public boolean shouldOverrideUrlLoading(WebView webView, WebResourceRequest webResourceRequest) {
            return loginActivity(webView, webResourceRequest.getUrl().toString());
        }

        @Deprecated
        public boolean shouldOverrideUrlLoading(WebView webView, String str) {
            return loginActivity(webView, str);
        }

        public WebResourceResponse shouldInterceptRequest(WebView webView, WebResourceRequest webResourceRequest) {
            String uri = webResourceRequest.getUrl().toString();
            WebResourceResponse shouldInterceptRequest = super.shouldInterceptRequest(webView, webResourceRequest);
            if (uri.contains("https://www.facebook.com/api/graphqlbatch")) {
                Log.e("tag1", "found graph url " + uri + "   " + webResourceRequest.getRequestHeaders().get("cookie"));
            }
            return shouldInterceptRequest;
        }
    }
}
