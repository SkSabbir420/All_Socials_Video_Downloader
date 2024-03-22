package com.erickruo.allinonevideodownloader.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;


import com.erickruo.allinonevideodownloader.R;
import com.erickruo.allinonevideodownloader.databinding.ActivityLoginInstagramBinding;
import com.erickruo.allinonevideodownloader.models.instawithlogin.ModelInstagramPref;
import com.erickruo.allinonevideodownloader.utils.SharedPrefsForInstagram;
import com.erickruo.allinonevideodownloader.utils.iUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class InstagramLoginActivity extends AppCompatActivity {

    private ActivityLoginInstagramBinding binding;
    Map<String, String> extraHeaders = new HashMap<String, String>();
    int randomnumber = 0;

    //challenge url
//    https://i.instagram.com/challenge/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginInstagramBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.logintoinstatitle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        randomnumber = iUtils.getRandomNumber(iUtils.UserAgentsListLogin.length);
        extraHeaders.put("x-requested-with", "XMLHttpRequest");
        extraHeaders.put("HTTP_X-Requested-With", "com.android.chrome");

        extraHeaders.put("authority", "www.instagram.com");
        extraHeaders.put("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7");
        extraHeaders.put("accept-language", "en-GB,en-US;q=0.9,en;q=0.8");
        extraHeaders.put("cache-control", "max-age=0");
        extraHeaders.put("dnt", "1");
        extraHeaders.put("dpr", "1.5");
        extraHeaders.put("referer", "https://www.instagram.com/");
        extraHeaders.put("sec-ch-prefers-color-scheme", "light");
        extraHeaders.put("sec-ch-ua", "Not/A)Brand\";v=\"99\", \"Google Chrome\";v=\"115\", \"Chromium\";v=\"115");
        extraHeaders.put("sec-ch-ua-full-version-list", "Not/A)Brand\";v=\"99.0.0.0\", \"Google Chrome\";v=\"115.0.5790.171\", \"Chromium\";v=\"115.0.5790.171");
        extraHeaders.put("sec-ch-ua-mobile", "?1");
        extraHeaders.put("sec-ch-ua-platform", "\"Android\"");
        extraHeaders.put("sec-ch-ua-platform-version", "\"10\"");
        extraHeaders.put("sec-fetch-dest", "document");
        extraHeaders.put("sec-fetch-mode", "navigate");
        extraHeaders.put("sec-fetch-site", "same-origin");
        extraHeaders.put("sec-fetch-user", "?1");
        extraHeaders.put("upgrade-insecure-requests", "1");
        extraHeaders.put("user-agent", "Mozilla/5.0 (Linux; Android 10; SM-G981B) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.162 Mobile Safari/537.36");
        extraHeaders.put("viewport-width", "367");


        LoadPage();
        binding.swipeRefreshLayout.setOnRefreshListener(this::LoadPage);

    }

    @SuppressLint("SetJavaScriptEnabled")
    public void LoadPage() {

        SharedPrefsForInstagram sharedPrefsForInstagram = new SharedPrefsForInstagram(InstagramLoginActivity.this);

        binding.webView.getSettings().setJavaScriptEnabled(true);
        CookieManager.getInstance().setAcceptThirdPartyCookies(binding.webView, true);
        binding.webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);

        binding.webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        binding.webView.getSettings().setDatabaseEnabled(true);
        binding.webView.getSettings().setBuiltInZoomControls(false);
        binding.webView.getSettings().setSupportZoom(true);
        binding.webView.getSettings().setUseWideViewPort(true);
        binding.webView.getSettings().setDomStorageEnabled(true);
        binding.webView.getSettings().setAllowFileAccess(true);
        binding.webView.getSettings().setLoadsImagesAutomatically(true);
        binding.webView.getSettings().setBlockNetworkImage(false);
        binding.webView.getSettings().setBlockNetworkLoads(false);
        binding.webView.getSettings().setLoadWithOverviewMode(true);
        binding.webView.clearCache(true);
        binding.webView.getSettings().setGeolocationEnabled(true);
        binding.webView.getSettings().setDisplayZoomControls(false);
        binding.webView.getSettings().setUserAgentString("Mozilla/5.0 (Linux; Android 10; SM-G981B) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.162 Mobile Safari/537.36");
//        binding.webView.getSettings().setUserAgentString("Instagram 9.5.2 (iPhone7,2; iPhone OS 9_3_3; en_US; en-US; scale=2.00; 750x1334) AppleWebKit/420+");

// TODO working on fixing cookies
        if (sharedPrefsForInstagram.getPreference() != null &&
                !Objects.equals(sharedPrefsForInstagram.getPreference().getPREFERENCE_ISINSTAGRAMLOGEDIN(), "true")) {
            CookieSyncManager.createInstance(InstagramLoginActivity.this);
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.removeAllCookie();
        }

//        try {
//
//            binding.webView.getSettings().setUserAgentString(iUtils.UserAgentsListLogin[randomnumber] + "");
//
//        } catch (Exception e) {
//            System.out.println("dsakdjasdjasd " + e.getMessage());
//
//            binding.webView.getSettings().setUserAgentString("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/106.0.0.0 Safari/537.36");
//
//        }


//        binding.webView.setWebChromeClient(new WebChromeClient() {
//
//            @Override
//            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
//                Log.d("chromium-A-INSTA", consoleMessage.message());
//                return true;
//
//            }
//
//            @Override
//            public void onReceivedTitle(WebView view, String title) {
//                getWindow().setTitle(title); //Set Activity tile to page title.
//            }
//
//            public void onProgressChanged(WebView view, int progress) {
//                binding.swipeRefreshLayout.setRefreshing(progress != 100);
//            }
//        });

        binding.webView.setWebViewClient(new MyWebViewClient());
        binding.webView.setWebChromeClient(new MyWebViewChromeClient());

        if (sharedPrefsForInstagram.getPreference() != null && !Objects.equals(sharedPrefsForInstagram.getPreference().getPREFERENCE_ISINSTAGRAMLOGEDIN(), "true")) {
            binding.webView.loadUrl("http://www.instagram.com/accounts/login", extraHeaders);
//            binding.webView.loadUrl("http://www.instagram.com/accounts/login", extraHeaders);
        } else {
            binding.webView.loadUrl("https://www.instagram.com/", extraHeaders);
//            binding.webView.loadUrl("https://www.instagram.com/challenge/", extraHeaders);
        }


    }


    public String getCookie(String siteName, String CookieName) {
        String CookieValue = null;

        CookieManager cookieManager = CookieManager.getInstance();
        String cookies = cookieManager.getCookie(siteName);
        if (cookies != null && !cookies.isEmpty()) {
            String[] temp = cookies.split(";");
            for (String ar1 : temp) {
                if (ar1.contains(CookieName)) {
                    String[] temp1 = ar1.split("=");
                    CookieValue = temp1[1];
                    break;
                }
            }
        }
        return CookieValue;
    }

    private class MyWebViewChromeClient extends WebChromeClient {


        @Override
        public void onPermissionRequest(PermissionRequest request) {
            // Check if the requested resources contain any unrecognized features
            boolean hasUnrecognizedFeatures = false;
            for (String permission : request.getResources()) {
                if (permission.startsWith("ambient-light-sensor") ||
                        permission.startsWith("bluetooth") ||
                        permission.startsWith("hid") ||
                        permission.startsWith("payment") ||
                        permission.startsWith("serial") ||
                        permission.startsWith("usb")) {
                    hasUnrecognizedFeatures = true;
                    break;
                }
            }

            if (!hasUnrecognizedFeatures) {
                // Check for specific permissions
                if (Arrays.asList(request.getResources()).contains(PermissionRequest.RESOURCE_VIDEO_CAPTURE) ||
                        Arrays.asList(request.getResources()).contains(PermissionRequest.RESOURCE_AUDIO_CAPTURE)) {
                    // Handle the specific permission request here
                    request.grant(new String[]{PermissionRequest.RESOURCE_VIDEO_CAPTURE, PermissionRequest.RESOURCE_AUDIO_CAPTURE});
                } else {
                    request.grant(request.getResources());
                }
            }
        }



        @Override
        public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
            System.out.println("MyWebViewChromeClient= " + consoleMessage.message());
            return super.onConsoleMessage(consoleMessage);

        }


        @Override
        public void onProgressChanged(WebView view, int newProgress) {

            if (newProgress >= 100) {

                String str = view.getUrl();
                String cookies = CookieManager.getInstance().getCookie(str);

                try {
                    String session_id = getCookie(str, "sessionid");
                    String csrftoken = getCookie(str, "csrftoken");
                    String userid = getCookie(str, "ds_user_id");
                    if (session_id != null && csrftoken != null && userid != null) {


                        ModelInstagramPref instagramPref = new ModelInstagramPref(session_id, userid, cookies, csrftoken, "true");
                        SharedPrefsForInstagram sharedPrefsForInstagram = new SharedPrefsForInstagram(InstagramLoginActivity.this);

                        sharedPrefsForInstagram.setPreference(instagramPref);


                        view.destroy();
                        Intent intent = new Intent();
                        intent.putExtra("result", "result");
                        setResult(RESULT_OK, intent);
                        finish();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }


    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url,extraHeaders);
            return true;
        }

        @Override
        public void onLoadResource(WebView webView, String str) {
            super.onLoadResource(webView, str);
        }


        @Override
        public void onPageFinished(WebView webView, String str) {
            super.onPageFinished(webView, str);

//            binding.swipeRefreshLayout.setRefreshing(false);
//
//            String cookies = CookieManager.getInstance().getCookie(str);
//
//            System.out.println("cookiesnew are " + cookies);
//            try {
//                String session_id = getCookie(str, "sessionid");
//                String csrftoken = getCookie(str, "csrftoken");
//                String userid = getCookie(str, "ds_user_id");
//
//                System.out.println("cookiesnew val session_id " + session_id);
//                System.out.println("cookiesnew  val csrftoken " + csrftoken);
//                System.out.println("cookiesnew val  userid " + userid);
//
//
//                if (!webView.getUrl().contains("instagram.com/challenge")) {
//                    if (session_id != null && csrftoken != null && userid != null && !session_id.equals("") && !csrftoken.equals("") && !userid.equals("")) {
//
//
//                        ModelInstagramPref instagramPref = new ModelInstagramPref(session_id, userid, cookies, csrftoken, "true");
//                        SharedPrefsForInstagram sharedPrefsForInstagram = new SharedPrefsForInstagram(InstagramLoginActivity.this);
//
//                        sharedPrefsForInstagram.setPreference(instagramPref);
//
//
////                        webView.destroy();
//                        Intent intent = new Intent();
//                        intent.putExtra("result", "result");
//                        setResult(RESULT_OK, intent);
////                        finish();
//                    }
//                }
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }

        }

        @Override
        public void onReceivedError(WebView webView, int i, String str, String str2) {
            super.onReceivedError(webView, i, str, str2);
        }

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView webView, WebResourceRequest webResourceRequest) {
            return super.shouldInterceptRequest(webView, webResourceRequest);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            // Load the URL within the WebView
            view.loadUrl(request.getUrl().toString(),extraHeaders);
            return true; // Return true to indicate that the request has been handled
        }
    }
}