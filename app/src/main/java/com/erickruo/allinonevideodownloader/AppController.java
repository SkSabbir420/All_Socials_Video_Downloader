package com.erickruo.allinonevideodownloader;

import android.content.Context;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.util.Log;

import androidx.multidex.MultiDexApplication;

import com.facebook.ads.AudienceNetworkAds;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.erickruo.allinonevideodownloader.receiver.DownloadBroadcastReceiver;
import com.erickruo.allinonevideodownloader.utils.AdsManager;
import com.erickruo.allinonevideodownloader.utils.AppOpenManager;
import com.erickruo.allinonevideodownloader.utils.Constants;
import com.erickruo.allinonevideodownloader.utils.LocaleHelper;
import com.erickruo.allinonevideodownloader.utils.PrefsCoins;
import com.erickruo.allinonevideodownloader.utils.SharedPrefsMainApp;
import com.onesignal.OneSignal;

import java.util.concurrent.Executor;

import io.reactivex.exceptions.UndeliverableException;
import io.reactivex.plugins.RxJavaPlugins;

public class AppController extends MultiDexApplication {

    AppOpenManager appOpenManager;
    private FirebaseRemoteConfig firebaseRemoteConfig;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }

    @Override
    public void onCreate() {
        super.onCreate();

        new InitializationTask().execute();

        try {
            registerReceiver(new DownloadBroadcastReceiver(), new IntentFilter("android.intent.action.DOWNLOAD_COMPLETE"));
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private void initializeApplication() {
        try {
            RxJavaPlugins.setErrorHandler(e -> {

                if (e instanceof UndeliverableException) {
                    // As UndeliverableException is a wrapper, get the cause of it to get the "real" exception
                    e = e.getCause();
                }

                if (e instanceof InterruptedException) {
                    // fine, some blocking code was interrupted by a dispose call
                    return;
                }

                Log.e("configureRxJavaErrorHandler", "Undeliverable exception received, not sure what to do", e);
            });

            if (!BuildConfig.DEBUG) {
                if (!Constants.iSAdminAttached) {
//                    fetchRemoteTitle();
                        //TODO don't change these ids
                        AdsManager.videoapp_AdmobNativeID = "ca-app-pub-3933249691387172/8066316338";
                        AdsManager.videoapp_AdmobRewardVideoExtraFeatures = "ca-app-pub-3933249691387172/9972749788";
                        AdsManager.videoapp_AdmobRewardID = "ca-app-pub-3933249691387172/9972749788";
                        AdsManager.videoapp_AdmobInterstitial = "ca-app-pub-3933249691387172/4911994790";
                        AdsManager.videoapp_AdmobBanner = "ca-app-pub-3933249691387172/2086343293";
                        AdsManager.videoapp_AdmobAppId = "ca-app-pub-3933249691387172~3930796059";
                }
            } else {
                if (Constants.enableTestAds) {
                    //TODO don't change these ids
                    AdsManager.videoapp_AdmobNativeID = "ca-app-pub-3940256099942544/2247696110";
                    AdsManager.videoapp_AdmobRewardVideoExtraFeatures = "ca-app-pub-3940256099942544/5224354917";
                    AdsManager.videoapp_AdmobRewardID = "ca-app-pub-3940256099942544/5224354917";
                    AdsManager.videoapp_AdmobInterstitial = "ca-app-pub-3940256099942544/1033173712";
                    AdsManager.videoapp_AdmobBanner = "ca-app-pub-3940256099942544/6300978111";
                    AdsManager.videoapp_AdmobAppId = "ca-app-pub-3940256099942544~3347511713";
                }
            }

            // Firebase
            FirebaseMessaging.getInstance().subscribeToTopic("all");

            // OneSignal Initialization
            OneSignal.initWithContext(this);
            OneSignal.setAppId(getString(R.string.onsignalappid));

        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private void fetchRemoteTitle() {
        AdsManager.videoapp_AdmobNativeID = firebaseRemoteConfig.getString("videoapp_AdmobNativeID");
        AdsManager.videoapp_AdmobRewardID = firebaseRemoteConfig.getString("videoapp_AdmobRewardID");
        AdsManager.videoapp_AdmobRewardVideoExtraFeatures = firebaseRemoteConfig.getString("videoapp_AdmobRewardVideoExtraFeatures");
        AdsManager.videoapp_AdmobInterstitial = firebaseRemoteConfig.getString("videoapp_AdmobInterstitial");
        AdsManager.videoapp_AdmobBanner = firebaseRemoteConfig.getString("videoapp_AdmobBanner");
        AdsManager.videoapp_AdmobAppId = firebaseRemoteConfig.getString("videoapp_AdmobAppId");

        firebaseRemoteConfig.fetchAndActivate()
                .addOnCompleteListener((Executor) this, task -> {
                    if (task.isSuccessful()) {
                        boolean updated = task.getResult();
                        // System.err.println("failedloadidss done = "+AdsManager.videoapp_AdmobInterstitial);
                    } else {
                        // System.err.println("failedloadidss = "+AdsManager.videoapp_AdmobInterstitial);
                        // Toast.makeText(SplashScreen.this, "Fetch failed",Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    e.printStackTrace();
                    System.err.println("failedloadidss falier = " + AdsManager.videoapp_AdmobInterstitial);
                });
    }

    private class InitializationTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                PrefsCoins prefs = new PrefsCoins(getApplicationContext());

                if (prefs.getPremium() == 1) {
                    if (System.currentTimeMillis() < prefs.getLong("time", System.currentTimeMillis())) {
                        new SharedPrefsMainApp(getApplicationContext()).setPREFERENCE_inappads("ppp");
                        prefs.setPremium(1);
                    } else {
                        new SharedPrefsMainApp(getApplicationContext()).setPREFERENCE_inappads("nnn");
                        prefs.setPremium(0);
                    }
                }

                AudienceNetworkAds.initialize(AppController.this.getApplicationContext());
                MobileAds.initialize(
                        AppController.this.getApplicationContext(),
                        initializationStatus -> {
                            appOpenManager = new AppOpenManager(AppController.this);
                        });

                firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
                firebaseRemoteConfig.setDefaultsAsync(R.xml.remote_config_defaults);

                FirebaseRemoteConfigSettings.Builder configBuilder = new FirebaseRemoteConfigSettings.Builder();
                if (BuildConfig.DEBUG) {
                    long cacheInterval = 0;
                    configBuilder.setMinimumFetchIntervalInSeconds(cacheInterval);
                }
                firebaseRemoteConfig.setConfigSettingsAsync(configBuilder.build());


                AppController appController = (AppController) AppController.this.getApplicationContext();
                appController.initializeApplication();
            }catch (Throwable e){
                e.printStackTrace();
                return null;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            // Run on the main thread after initialization is complete
//            new Handler(Looper.getMainLooper()).post(() -> {
//                AppController appController = (AppController) AppController.this.getApplicationContext();
//                appController.initializeApplication();
//            });
        }
    }
}
