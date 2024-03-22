/*
 * *
 *  * Created by Syed Usama Ahmad on 3/17/23, 11:37 PM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 3/17/23, 10:26 PM
 *
 */

package com.erickruo.allinonevideodownloader.tiktok_methods;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.ConsoleMessage;
import android.webkit.PermissionRequest;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.erickruo.allinonevideodownloader.databinding.ActivityTikTokDownloadWebviewBinding;
import com.erickruo.allinonevideodownloader.utils.DownloadFileMain;
import com.erickruo.allinonevideodownloader.utils.iUtils;

import java.util.ArrayList;
import java.util.Objects;

public class WebViewDownloadTesting extends AppCompatActivity {


    public static Handler handler;
    static String myvidintenturlis = "";
    private static ValueCallback<Uri[]> mUploadMessageArr;
    String TAG = "whatsapptag";
    boolean isdownloadstarted = false;
    ProgressDialog progressDialog;
    private ActivityTikTokDownloadWebviewBinding binding;
    ArrayList<String> listoflink_videos, listoflink_photos;
    private Handler handler2;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        listoflink_videos = new ArrayList<String>();
        listoflink_photos = new ArrayList<String>();
        handler2 =new  Handler();
        binding = ActivityTikTokDownloadWebviewBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        setSupportActionBar(binding.tool12);
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);

        InitHandler();

        if (getIntent().getStringExtra("myvidurl") != null && !getIntent().getStringExtra("myvidurl").equals("")) {
            myvidintenturlis = getIntent().getStringExtra("myvidurl");
        }


        binding.webViewscan.clearFormData();
        binding.webViewscan.getSettings().setSaveFormData(true);
        binding.webViewscan.setLayoutParams(new RelativeLayout.LayoutParams(-1, -1));
        binding.webViewscan.setWebViewClient(new MyBrowser());

        binding.webViewscan.getSettings().setAllowFileAccess(true);

        binding.webViewscan.getSettings().setJavaScriptEnabled(true);
        binding.webViewscan.getSettings().setDefaultTextEncodingName("UTF-8");
        binding.webViewscan.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        binding.webViewscan.getSettings().setDatabaseEnabled(true);
        binding.webViewscan.getSettings().setBuiltInZoomControls(false);
        binding.webViewscan.getSettings().setSupportZoom(false);
        binding.webViewscan.getSettings().setUseWideViewPort(true);
        binding.webViewscan.getSettings().setDomStorageEnabled(true);
        binding.webViewscan.getSettings().setAllowFileAccess(true);
        binding.webViewscan.getSettings().setLoadWithOverviewMode(true);
        binding.webViewscan.getSettings().setLoadsImagesAutomatically(true);
        binding.webViewscan.getSettings().setBlockNetworkImage(false);
        binding.webViewscan.getSettings().setBlockNetworkLoads(false);
        binding.webViewscan.getSettings().setLoadWithOverviewMode(true);
        binding.webViewscan.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        binding.webViewscan.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onPermissionRequest(final PermissionRequest request) {
                request.grant(request.getResources());
            }
        });

        binding.webViewscan.setDownloadListener((url, userAgent, contentDisposition, mimetype, contentLength) -> {

            String nametitle = "Tiktok_video_" +
                    System.currentTimeMillis();

            DownloadFileMain.startDownloading(WebViewDownloadTesting.this, url, nametitle, ".mp4");


        });

        binding.webViewscan.setWebChromeClient(new WebChromeClient() {

            public void onProgressChanged(WebView view, int progress) {
                if (progress < 100 && binding.progressBar.getVisibility() == View.GONE) {
                    binding.progressBar.setVisibility(View.VISIBLE);

                }

                binding.progressBar.setProgress(progress);
                if (progress == 100) {
                    binding.progressBar.setVisibility(View.GONE);

                }
            }
        });


        binding.webViewscan.loadUrl("https://downloadgram.org/");


    }


    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 1001) {
            mUploadMessageArr.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(i2, intent));
            mUploadMessageArr = null;
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean z = true;
        if (keyCode == 4) {
            try {
                if (binding.webViewscan.canGoBack()) {
                    binding.webViewscan.goBack();
                    return z;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        finishAndDismiss();
        z = super.onKeyDown(keyCode, event);
        return z;
    }

    @SuppressLint({"WrongConstant"})
    public void onBackPressed() {
        finishAndDismiss();

        super.onBackPressed();

    }

    public void onPause() {
        super.onPause();
        try {
            binding.webViewscan.clearCache(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onDestroy() {
        super.onDestroy();
        try {
            binding.webViewscan.clearCache(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onStop() {
        try {
            binding.webViewscan.clearCache(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onStop();
    }

    public boolean onSupportNavigateUp() {
        finishAndDismiss();
        return true;
    }

    @SuppressLint({"HandlerLeak"})
    private void InitHandler() {
        handler = new btnInitHandlerListner();
    }

    @SuppressLint("HandlerLeak")
    private class btnInitHandlerListner extends Handler {
        @SuppressLint({"SetTextI18n"})
        public void handleMessage(Message msg) {
        }
    }

    private class webChromeClients extends WebChromeClient {
        public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
            Log.e("CustomClient", consoleMessage.message());
            return super.onConsoleMessage(consoleMessage);
        }
    }

    private class MyBrowser extends WebViewClient {
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            binding.progressBar.setVisibility(View.VISIBLE);
            Log.e(TAG, "binding.progressBar");
            super.onPageStarted(view, url, favicon);
        }

        public boolean shouldOverrideUrlLoading(WebView view, String request) {
            view.loadUrl(request);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            Log.e("workkkk url", "binding!!.progressBar reciveing data0 " + url);

            try {
                String jsscript = "javascript:(function() { "
                        + "var ell = document.getElementById('url');"
                        + "ell.value = '" + myvidintenturlis + "';"
                        + "var bbb = document.getElementById('submit');"
                        + "bbb.click();"
                        + "})();";

                view.evaluateJavascript(jsscript, value -> {
                        Log.e("workkkk0", "binding!!.progressBar reciveing data1 " + value);

                        try {
                            handler2.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    runOnUiThread(() -> {
                                        view.evaluateJavascript(
                                                "(function() { " +
                                                        "var text='';" +
                                                        "var aaa = document.getElementsByTagName('a');" +
                                                        "for (var i = 0; i < aaa.length; i++) {" +
                                                        "  text += aaa[i].getAttribute('href')+'@_@';" +
                                                        "}" +
                                                        "var withoutLast3 = text.slice(0, -3);" +
                                                        "return withoutLast3+''; })();"
                                                , html -> {
                                                    Log.e("workkkk0", "binding!!.progressBar reciveing data2 " + html);

                                                    String[] sss = html.split("@_@");
                                                    for (String i : sss) {
                                                        if (i.contains("https://download.") || i.contains("scontent") || i.contains("cdninstagram")) {
                                                            if (i.contains(".jpg")) {
                                                                Log.d("HTMLimg", "" + i);
                                                                listoflink_photos.add(i);
                                                            } else if (i.contains(".mp4") || (i.startsWith("https://instagram.") && !i.contains("scontent") && !i.contains("cdninstagram"))) {
                                                                Log.d("HTML vid", "" + i);
                                                                listoflink_videos.add(i);
                                                            }
                                                        }
                                                    }

                                                    if (  (listoflink_videos.size() > 0 || listoflink_photos.size() > 0)) {
                                                        handler2.removeCallbacksAndMessages(null);

                                                        if (listoflink_videos != null || listoflink_photos != null || listoflink_videos.size() > 0 || listoflink_photos.size() > 0) {
                                                            listoflink_videos = iUtils.removeDuplicates(listoflink_videos);
                                                            listoflink_photos = iUtils.removeDuplicates(listoflink_photos);

//                                                            for (String i : listoflink_videos) {
//                                                                DownloadFileMain.startDownloading(WebViewDownloadTesting.this, i,
//                                                                        "_instagram_" + System.currentTimeMillis() + "_" + iUtils.getVideoFilenameFromURL(i), ".mp4");
//                                                            }
//
//                                                            for (String i : listoflink_photos) {
//                                                                DownloadFileMain.startDownloading(WebViewDownloadTesting.this, i,
//                                                                        "_instagram_" + System.currentTimeMillis() + "_" + iUtils.getImageFilenameFromURL(i), ".png");
//                                                            }
                                                        }
                                                    }
                                                });

                                        handler2.postDelayed(this, 3500);
                                    });
                                }
                            }, 3000);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

    void finishAndDismiss() {
        try {
            if (progressDialog != null && progressDialog.isShowing() && !isFinishing()) {
                progressDialog.dismiss();
            }
            finish();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }


}
