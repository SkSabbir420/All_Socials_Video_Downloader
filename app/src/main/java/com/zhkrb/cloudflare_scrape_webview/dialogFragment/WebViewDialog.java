/*
 * *
 *  * Created by Syed Usama Ahmad on 9/23/22, 7:54 PM
 *  * Copyright (c) 2022 . All rights reserved.
 *  * Last modified 9/19/22, 2:25 PM
 *
 */

package com.zhkrb.cloudflare_scrape_webview.dialogFragment;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.erickruo.allinonevideodownloader.R;
import com.zhkrb.cloudflare_scrape_webview.CfCallback;
import com.zhkrb.cloudflare_scrape_webview.Cloudflare;
import com.zhkrb.cloudflare_scrape_webview.util.CheckUtil;
import com.zhkrb.cloudflare_scrape_webview.util.ConvertUtil;
import com.zhkrb.cloudflare_scrape_webview.util.LogUtil;
import com.zhkrb.cloudflare_scrape_webview.util.WordUtil;
import com.zhkrb.cloudflare_scrape_webview.webClient.AdvanceWebClient;

import java.lang.ref.WeakReference;
import java.net.HttpCookie;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class WebViewDialog extends AbsDialogFragment {

    private CfCallback mListener;
    private WebView mWebView;
    private AdvanceWebClient mAdvanceWebClient;
    private ProgressBar mProgressBar;
    private ConstraintLayout mLayout;
    private MyHandler mHandler;
    private Animation mShowAnim;
    private Animation mHideAnim;

    private String mOriginUrl;
    private URL mUrl;
    private String mUser_agent;
    private boolean isOnBackPress = true;
    private int mRetry_count;
    private static final int MAX_COUNT = 2;
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36";
    private boolean hasNewUrl = false;  //when cf return 301 you need to change old url to new url;

    private CheckUtil mCheckUtil;


    @Override
    protected void setWindowAttributes(Window window) {
        window.setWindowAnimations(R.style.bottomToTopAnim);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        params.gravity = Gravity.CENTER;
        window.setAttributes(params);
    }

    @Override
    protected boolean canCancel() {
        return false;
    }

    @Override
    protected int getDialogStyle() {
        return R.style.dialog;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_view;
    }

    @Override
    protected void main() {
        Bundle bundle = getArguments();
        if (mListener == null) {
            LogUtil.e("Must set listener before dialog show");
            this.dismissAllowingStateLoss();
            return;
        }
        if (bundle == null) {
            isOnBackPress = false;
            failed(Cloudflare.ERR_BUNDLE_PARAM, WordUtil.getString(mContext, R.string.nobundle));
            this.dismissAllowingStateLoss();
            return;
        }
        mOriginUrl = bundle.getString("url", "");
        mUser_agent = bundle.getString("ua", "");
        if (TextUtils.isEmpty(mUser_agent)) {
            mUser_agent = USER_AGENT;
        }
        mProgressBar = mRootView.findViewById(R.id.progress);
        mLayout = mRootView.findViewById(R.id.parent);
        mHandler = new MyHandler(this);
        mShowAnim = AnimationUtils.loadAnimation(mContext, R.anim.right_to_left_enter);
        mHideAnim = AnimationUtils.loadAnimation(mContext, R.anim.left_to_right_exit);
        try {
            mUrl = new URL(mOriginUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            LogUtil.e("Error url : " + mOriginUrl);
            cancelAll();
            isOnBackPress = false;
            failed(Cloudflare.ERR_URL, WordUtil.getString(mContext, R.string.urlerr));
            dismissAllowingStateLoss();
            return;
        }

        mCheckUtil = new CheckUtil();
        mCheckUtil.setCheckListener(mCheckListener);
        mCheckUtil.checkVisit(mOriginUrl, mUser_agent);
    }

    private void initWebView() {
        mWebView = new WebView(mContext);
        ConstraintLayout.LayoutParams layoutParams =
                new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
        layoutParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;
        layoutParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
        layoutParams.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;
        mWebView.setLayoutParams(layoutParams);
        mWebView.setId(R.id.webview);
        mWebView.setVisibility(View.INVISIBLE);
        mLayout.addView(mWebView, -1);
        mAdvanceWebClient = new AdvanceWebClient(getContext(), mWebView, mUser_agent);
        mAdvanceWebClient.setListener(mLoginSuccessListener);
        mAdvanceWebClient.initWebView(mOriginUrl, mUrl.getHost());
    }

    private void reload() {
        mAdvanceWebClient.getCookieManager().removeAllCookies(null);
        mAdvanceWebClient.setUrl(mUrl.getHost());
        mAdvanceWebClient.setOriginUrl(mOriginUrl);
        mWebView.stopLoading();
        mWebView.clearCache(true);
        mAdvanceWebClient.reset();
        mWebView.loadUrl(mOriginUrl);
    }

    private void showWebView() {
        setViewVisibility(mWebView, VISIBLE);
        setViewVisibility(mProgressBar, INVISIBLE);
    }

    private void hideWebView() {
        setViewVisibility(mWebView, INVISIBLE);
        setViewVisibility(mProgressBar, VISIBLE);
    }

    private void setViewVisibility(View view, int vis) {
        if (view != null && view.getVisibility() != vis) {
            Animation animation;
            animation = vis == VISIBLE ? mShowAnim : mHideAnim;
            view.setVisibility(vis);
            view.startAnimation(animation);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mCheckUtil != null) {
            mCheckUtil.cancel();
        }
        if (mWebView != null) {
            mWebView.stopLoading();
            mWebView.getSettings().setJavaScriptEnabled(false);
            mWebView.clearHistory();
            mWebView.removeAllViews();
            mWebView.destroy();
        }

        if (isOnBackPress) {
            failed(Cloudflare.ERR_CANCEL, "getCookie cancel");
        }
    }


    public void setListener(CfCallback callback) {
        mListener = callback;
    }

    private final CheckUtil.CheckListener mCheckListener = new CheckUtil.CheckListener() {
        @Override
        public void onSuccess(List<HttpCookie> cookieList) {
            Message msg = Message.obtain();
            msg.what = 0x05;
            msg.obj = cookieList;
            mHandler.sendMessage(msg);
        }

        @Override
        public void onFail(String msg) {
            if (mRetry_count < MAX_COUNT) {
                if (!mCheckUtil.isCancel()) {
                    mRetry_count++;
                    LogUtil.e("Retry count : " + mRetry_count);
                    mHandler.sendEmptyMessage(mWebView == null ? 0x03 : 0x04);
                } else {
                    Message message = Message.obtain(mHandler);
                    message.what = 0x06;
                    message.obj = msg;
                    message.sendToTarget();
                }
            } else {
                Message message = Message.obtain(mHandler);
                message.what = 0x06;
                message.obj = msg;
                message.sendToTarget();
            }
        }

        @Override
        public void onChangeNewUrl(String url) {
            hasNewUrl = true;
            try {
                mOriginUrl = url;
                mUrl = new URL(url);
                mCheckUtil.checkVisit(mOriginUrl, mUser_agent);
            } catch (MalformedURLException e) {
                e.printStackTrace();
                try {
                    LogUtil.e("Error url : " + url);
                    if (!mContext.isFinishing()) {
                        cancelAll();
                        isOnBackPress = false;
                        failed(Cloudflare.ERR_URL, e.getMessage());
                        dismissAllowingStateLoss();
                    }
                } catch (Throwable e1) {
                    e1.printStackTrace();
                }
            }
        }
    };

    private void getCookieSuccess(List<HttpCookie> obj) {
        isOnBackPress = false;
        success(obj, hasNewUrl, mUrl.getHost());
        dismissAllowingStateLoss();
    }

    private void getCookieFail(String cause) {
        try {
            isOnBackPress = false;
            if (TextUtils.isEmpty(cause)) {
                failed(Cloudflare.ERR_EXCEED_LIMIT, WordUtil.getString(mContext, R.string.exceedlimit));
            } else {
                failed(Cloudflare.ERR_CAUSE, cause);
            }

            dismissAllowingStateLoss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void failed(final int code, final String msg) {
        mContext.runOnUiThread(() -> {
            try {
                mListener.onFail(code, msg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void success(final List<HttpCookie> cookieList, final boolean hasNewUrl, final String newUrl) {
        mContext.runOnUiThread(() -> {
            try {
                mListener.onSuccess(cookieList, hasNewUrl, newUrl);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private final AdvanceWebClient.LoginSuccessListener mLoginSuccessListener = new AdvanceWebClient.LoginSuccessListener() {
        @Override
        public void onSuccess(String cookie) {
            mHandler.sendEmptyMessage(0x01);
            mCheckUtil.setCookieList(ConvertUtil.String2List(cookie));
            mCheckUtil.checkVisit(mOriginUrl, mUser_agent);
        }

        @Override
        public void onCaptchaChallenge() {
            mHandler.sendEmptyMessage(0x02);
        }

        @Override
        public void onFail() {
            mHandler.sendEmptyMessage(0x01);
            mCheckUtil.checkVisit(mOriginUrl, mUser_agent);
        }
    };

    public void cancelAll() {
        if (mCheckUtil != null) {
            mCheckUtil.cancel();
        }
    }


    private static class MyHandler extends Handler {

        private final WeakReference<WebViewDialog> mDialogWeakReference;

        @SuppressWarnings("deprecation")
        MyHandler(WebViewDialog dialog) {
            mDialogWeakReference = new WeakReference<>(dialog);
        }

        @SuppressWarnings("unchecked")
        @Override
        public void handleMessage(@NonNull Message msg) {
            WebViewDialog dialog = mDialogWeakReference.get();
            super.handleMessage(msg);
            if (dialog != null) {
                switch (msg.what) {
                    case 0x01:
                        dialog.hideWebView();
                        break;
                    case 0x02:
                        dialog.showWebView();
                        break;
                    case 0x03:
                        dialog.initWebView();
                        break;
                    case 0x04:
                        dialog.reload();
                        break;
                    case 0x05:
                        dialog.getCookieSuccess((List<HttpCookie>) msg.obj);
                        break;
                    case 0x06:
                        String cause = (String) msg.obj;
                        dialog.getCookieFail(cause);
                        break;
                    default:
                }
            }
        }
    }

}
