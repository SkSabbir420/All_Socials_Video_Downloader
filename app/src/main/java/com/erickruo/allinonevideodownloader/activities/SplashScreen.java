/*
 * *
 *  * Created by Syed Usama Ahmad on 3/17/23, 11:37 PM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 3/17/23, 11:29 PM
 *
 */

package com.erickruo.allinonevideodownloader.activities;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.Keep;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.ConfigurationCompat;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.gms.tasks.Task;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallState;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.erickruo.allinonevideodownloader.BuildConfig;
import com.erickruo.allinonevideodownloader.R;
import com.erickruo.allinonevideodownloader.activities.newUi.MainActivityNewUi;
import com.erickruo.allinonevideodownloader.databinding.ActivitySplashScreenBinding;
import com.erickruo.allinonevideodownloader.models.adminpanel.AdId;
import com.erickruo.allinonevideodownloader.models.adminpanel.AppSettings;
import com.erickruo.allinonevideodownloader.models.adminpanel.Setting;
import com.erickruo.allinonevideodownloader.models.adminpanel.SocialMedia;
import com.erickruo.allinonevideodownloader.utils.AdsManager;
import com.erickruo.allinonevideodownloader.utils.Constants;
import com.erickruo.allinonevideodownloader.utils.GlideApp;
import com.erickruo.allinonevideodownloader.utils.LocaleHelper;
import com.erickruo.allinonevideodownloader.utils.SharedPrefsMainApp;
import com.erickruo.allinonevideodownloader.utils.iUtils;
import com.yausername.ffmpeg.FFmpeg;
import com.yausername.youtubedl_android.YoutubeDL;
import com.yausername.youtubedl_android.YoutubeDLException;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.Executors;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.UndeliverableException;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.plugins.RxJavaPlugins;


public class SplashScreen extends AppCompatActivity {

    AppUpdateManager appUpdateManager;
    InstallStateUpdatedListener installStateUpdatedListener = new
            InstallStateUpdatedListener() {
                @Override
                public void onStateUpdate(InstallState state) {
                    if (state.installStatus() == InstallStatus.DOWNLOADED) {
                        //CHECK THIS if AppUpdateType.FLEXIBLE, otherwise you can skip
                        Log.i("mysjdjhdjkh", "InstallStateUpdatedListener: download complete");

                    } else if (state.installStatus() == InstallStatus.INSTALLED) {
                        if (appUpdateManager != null) {
                            appUpdateManager.unregisterListener(installStateUpdatedListener);
                        }
                        Log.i("mysjdjhdjkh", "InstallStateUpdatedListener: state: installed");

                    } else if (state.installStatus() == InstallStatus.FAILED) {
                        if (appUpdateManager != null) {
                            appUpdateManager.unregisterListener(installStateUpdatedListener);
                        }
                        Log.i("mysjdjhdjkh", "InstallStateUpdatedListener: state: failed " + state.installErrorCode());

                    } else {
                        Log.i("mysjdjhdjkh", "InstallStateUpdatedListener: state: " + state.installStatus());
                    }
                }
            };

    boolean updating = false;

    androidx.core.splashscreen.SplashScreen splashScreen;

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT > 31) {
            splashScreen = androidx.core.splashscreen.SplashScreen.installSplashScreen(this);
        }
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ActivitySplashScreenBinding binding = ActivitySplashScreenBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        if (Build.VERSION.SDK_INT < 31) {
            setContentView(view);
        }


        try {

            appUpdateManager = AppUpdateManagerFactory.create(SplashScreen.this);
            appUpdateManager.registerListener(installStateUpdatedListener);

            binding.version.setText(String.format("%s %s\nBy: %s", getString(R.string.version), BuildConfig.VERSION_NAME, getString(R.string.developer_name)));
            if (com.erickruo.allinonevideodownloader.BuildConfig.ISPRO) {
                GlideApp.with(SplashScreen.this)
                        .load(R.mipmap.ic_launcher)
                        .placeholder(R.mipmap.ic_launcher)
                        .into(binding.iconmain);
            }
            AdsManager.setDefaults(SplashScreen.this);


            Executors.newSingleThreadExecutor().submit(() -> {
                try {
//                    iUtils.myDLphpTempCookies = iUtils.showCookiesdlphp("http://tikdd.infusiblecoder.com/");
//                    System.out.println("mysjdjhdjkh dlphp= " + iUtils.myDLphpTempCookies);
                    iUtils.myInstagramTempCookies = iUtils.showCookies("https://www.instagram.com/");
//                    iUtils.myinstawebTempCookies = iUtils.showCookies("https://snapsave.app/");
//                    System.out.println("mysjdjhdjkh " + iUtils.myinstawebTempCookies);
                    System.out.println("mysjdjhdjkh useragent=" + iUtils.generateUserAgent());

                    //get data from admin panel
                    if (Constants.iSAdminAttached) {
                        getAppDataFromAdminPanel();
                    }

                } catch (Throwable e) {
                    e.printStackTrace();
                    goToMainAndFinish();
                }
            });

            setDefaultLanguage();


            configureRxJavaErrorHandler();
            Completable.fromAction(() -> initLibraries(SplashScreen.this))
                    .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DisposableCompletableObserver() {
                        @Override
                        public void onComplete() {
                            // it worked
                        }

                        @Override
                        public void onError(Throwable e) {
                            if (BuildConfig.DEBUG) {
                                Log.e("configureRxJavaErrorHandler", "failed to initialize youtubedl-android", e);
                                Toast.makeText(getApplicationContext(), "initialization failed: " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


        } catch (Throwable e) {
            e.printStackTrace();
            goToMainAndFinish();
        }

        if (Build.VERSION.SDK_INT > 31) {
            // Keep the splash screen visible for this Activity
            if (splashScreen != null) {
                splashScreen.setKeepOnScreenCondition(() -> true);
            }
        }
    }

    void setDefaultLanguage() {
        try {

            SharedPreferences prefs = getSharedPreferences("lang_pref", MODE_PRIVATE);
            //System.out.println("default lang = "+ Locale.getDefault().getLanguage());
            String deviceLang = Objects.requireNonNull(ConfigurationCompat.getLocales(Resources.getSystem().getConfiguration()).get(0)).getLanguage();
            String lang = prefs.getString("lang", deviceLang);

            if (!deviceLang.equals(lang)) {
                SharedPreferences sharedPreference = getSharedPreferences("lang_pref", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreference.edit();
                editor.putString("lang", deviceLang);
                editor.apply();
                lang = deviceLang;
            }
            LocaleHelper.setLocale(getApplicationContext(), lang);
            if (lang.equals("ur") || lang.equals("ar")) {
                getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void configureRxJavaErrorHandler() {
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
    }

    private void initLibraries(Context context) throws YoutubeDLException {
        try {
            initDlLibs(context,true);
            if (!BuildConfig.DEBUG) {
                checkAppUpdate();
            } else {
                goToMainAndFinish();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }


    static void initDlLibs(Context context,boolean isStart) throws YoutubeDLException {
        try {
            YoutubeDL.getInstance().init(context);
            FFmpeg.getInstance().init(context);
            if (isStart){
                updateYoutubeDL(context);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }


    static void updateYoutubeDL(Context context) {
        final CompositeDisposable compositeDisposable = new CompositeDisposable();

        Disposable disposable = Observable.fromCallable(() -> YoutubeDL.getInstance().updateYoutubeDL(context, YoutubeDL.UpdateChannel._NIGHTLY))
                .subscribeOn(io.reactivex.schedulers.Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(status -> {

                    switch (status) {
                        case DONE:
                            if (BuildConfig.DEBUG) {
                                Log.e("SplashScreen", "Api Update successful");
                                Toast.makeText(context, "Api Update successful", Toast.LENGTH_LONG).show();
                            }
                            break;
                        case ALREADY_UP_TO_DATE:
                            if (BuildConfig.DEBUG) {
                                Log.e("SplashScreen", "Already up to date " + YoutubeDL.getInstance().versionName(context));
//                            Toast.makeText(SplashScreen.this, "Already up to date " + YoutubeDL.getInstance().versionName(this), Toast.LENGTH_LONG).show();
                            }
                            break;
                        default:
                            if (BuildConfig.DEBUG) {
                                Log.e("SplashScreen", "status " + status.toString());
//                            Toast.makeText(SplashScreen.this, status.toString(), Toast.LENGTH_LONG).show();
                            }
                            break;
                    }
                }, e -> {
                    if (BuildConfig.DEBUG) {
                        Log.e("SplashScreen", "failed to update", e);
                        Toast.makeText(context, "update failed", Toast.LENGTH_LONG).show();
                    }
                });
        compositeDisposable.add(disposable);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!BuildConfig.DEBUG) {
            appUpdateManager.getAppUpdateInfo().addOnSuccessListener(appUpdateInfo -> {
                if (appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                    try {
                        appUpdateManager.startUpdateFlowForResult(
                                appUpdateInfo, AppUpdateType.IMMEDIATE, SplashScreen.this, 1009);
                    } catch (IntentSender.SendIntentException e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            goToMainAndFinish();
        }
    }


    private void goToMainAndFinish() {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (iUtils.isNewUi) {
                startActivity(new Intent(SplashScreen.this, MainActivityNewUi.class));
            } else {
                startActivity(new Intent(SplashScreen.this, MainActivity.class));
            }
            finish();
        }, 1500);
    }
    public void checkAppUpdate() {
        try {
            Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
            appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
                System.out.println("mysjdjhdjkh " + appUpdateInfo.updateAvailability());
                System.out.println("mysjdjhdjkh " + appUpdateInfo.availableVersionCode());
                if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                        && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                    try {
                        appUpdateManager.startUpdateFlowForResult(
                                appUpdateInfo, AppUpdateType.IMMEDIATE, SplashScreen.this, 1009);
                    } catch (IntentSender.SendIntentException e) {
                        e.printStackTrace();
                    }
                } else {
                    goToMainAndFinish();
                }
            }).addOnFailureListener(e -> {
                e.printStackTrace();
                goToMainAndFinish();
            });

        } catch (Exception e) {
            e.printStackTrace();
            goToMainAndFinish();
        }
    }


    @Keep
    public static void getExistingAdminPanelData(Context context) {
        if (new SharedPrefsMainApp(context) != null && new SharedPrefsMainApp(context).getPREFERENCE_adminpanelapidata() != null && Constants.iSAdminAttached) {
            AppSettings appSettings = new SharedPrefsMainApp(context).getPREFERENCE_adminpanelapidata();


            if (appSettings.getAdIds() != null) {

                AdId adId = appSettings.getAdIds().get(0);

                AdsManager.status_AdmobBanner = adId.getBannerIDStatus();
                AdsManager.status_AdmobInterstitial = adId.getIntIDStatus();
                AdsManager.status_AdmobRewardID = adId.getRewardVideoIDStatus();
                AdsManager.status_AdmobNativeID = adId.getNativeAdIDStatus();

                AdsManager.NUMBER_OF_INTERSTISHAL_ADS_PER_SESSION = adId.getIntClicks();

                if (Constants.enableTestAds) {
                    AdsManager.setDefaults((Activity) context);
                } else {
                    AdsManager.videoapp_AdmobNativeID = adId.getNativeAdID();
                    AdsManager.videoapp_AdmobRewardID = adId.getRewardVideoID();
                    AdsManager.videoapp_AdmobRewardVideoExtraFeatures = adId.getRewardVideoID();
                    AdsManager.videoapp_AdmobInterstitial = adId.getIntID();
                    AdsManager.videoapp_AdmobBanner = adId.getBannerID();
                    AdsManager.videoapp_AdmobAppId = context.getResources().getString(R.string.AdmobAppId);
                }

                Setting setting = appSettings.getSettings().get(0);
                iUtils.isNewUi = setting.getNewUi();

                iUtils.myAppSettings = setting;
            }

            if (appSettings.getSocialMedias() != null) {
                if (iUtils.socialMediaList != null) {
                    iUtils.socialMediaList.clear();
                } else {
                    iUtils.socialMediaList = new ArrayList<>();
                }
                for (SocialMedia socialMedia : appSettings.getSocialMedias()) {
                    if (socialMedia.getStatus() == 1) {
                        iUtils.socialMediaList.add(socialMedia.getSource().toLowerCase());
                    }
                }
            }

        }
    }

    @Keep
    public static void checkExistingAppDataFromAdminPanelAndSetData(Context context) {

        AndroidNetworking.get(Constants.adminApiUrl + "/all")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            System.out.println("Apidata datais  " + response.toString());
                            JSONObject jsonObject = new JSONObject(response.toString());

                            if (jsonObject.getBoolean("status")) {
                                new SharedPrefsMainApp(context).setPREFERENCE_adminpanelapidata(response.toString());
//                                    checkExistingAppDataFromAdminPanelAndSetData(context);

                                AppSettings appSettings = new SharedPrefsMainApp(context).getPREFERENCE_adminpanelapidata();


                                if (appSettings.getAdIds() != null) {

                                    AdId adId = appSettings.getAdIds().get(0);

                                    AdsManager.status_AdmobBanner = adId.getBannerIDStatus();
                                    AdsManager.status_AdmobInterstitial = adId.getIntIDStatus();
                                    AdsManager.status_AdmobRewardID = adId.getRewardVideoIDStatus();
                                    AdsManager.status_AdmobNativeID = adId.getNativeAdIDStatus();

                                    AdsManager.NUMBER_OF_INTERSTISHAL_ADS_PER_SESSION = adId.getIntClicks();

                                    if (Constants.enableTestAds) {
                                        AdsManager.setDefaults((Activity) context);
                                    } else {
                                        AdsManager.videoapp_AdmobNativeID = adId.getNativeAdID();
                                        AdsManager.videoapp_AdmobRewardID = adId.getRewardVideoID();
                                        AdsManager.videoapp_AdmobRewardVideoExtraFeatures = adId.getRewardVideoID();
                                        AdsManager.videoapp_AdmobInterstitial = adId.getIntID();
                                        AdsManager.videoapp_AdmobBanner = adId.getBannerID();
                                        AdsManager.videoapp_AdmobAppId = context.getResources().getString(R.string.AdmobAppId);
                                    }
                                    Setting setting = appSettings.getSettings().get(0);
                                    iUtils.isNewUi = setting.getNewUi();

                                    iUtils.myAppSettings = setting;

                                }

                                if (appSettings.getSocialMedias() != null) {
                                    if (iUtils.socialMediaList != null) {
                                        iUtils.socialMediaList.clear();
                                    } else {
                                        iUtils.socialMediaList = new ArrayList<>();
                                    }
                                    for (SocialMedia socialMedia : appSettings.getSocialMedias()) {
                                        if (socialMedia.getStatus() == 1) {
                                            iUtils.socialMediaList.add(socialMedia.getSource().toLowerCase());
                                        }
                                    }
                                }

                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            System.out.println("Apidata error " + e.getMessage());
                            getExistingAdminPanelData(context);
                        }

                    }

                    @Override
                    public void onError(ANError error) {
                        error.printStackTrace();
                        System.out.println("Apidata error " + error.getMessage());
                        getExistingAdminPanelData(context);
                    }
                });


    }

    @Keep
    public static void uploadDownloadedUrl(String myUrl) {

        if (Constants.iSAdminAttached) {

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("download_url", myUrl);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            AndroidNetworking.post(Constants.adminApiUrl + "/downloadedurl")
                    .addJSONObjectBody(jsonObject)
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                System.out.println("Apidata datais 44 " + response.toString());


                            } catch (Exception e) {
                                e.printStackTrace();
                                System.out.println("Apidata error 44 " + e.getMessage());
                            }

                        }

                        @Override
                        public void onError(ANError error) {
                            error.printStackTrace();
                            System.out.println("Apidata error 444" + error.getMessage());

                        }
                    });
        }
    }

    @Keep
    public void getAppDataFromAdminPanel() {


        JSONObject jsonObject = new JSONObject();
        try {
            @SuppressLint("HardwareIds") String android_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
            WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            WifiInfo wInfo = wifiManager.getConnectionInfo();
            @SuppressLint("HardwareIds") String macAddress = wInfo.getBSSID();


            jsonObject.put("macAddress", macAddress);
            jsonObject.put("deviceId", android_id);
            jsonObject.put("packageName", BuildConfig.APPLICATION_ID);
            jsonObject.put("appVersion", BuildConfig.VERSION_NAME);
            jsonObject.put("appBuildNo", BuildConfig.VERSION_CODE);
            if (new SharedPrefsMainApp(SplashScreen.this) != null && new SharedPrefsMainApp(SplashScreen.this).getPREFERENCE_adminpanelapidata() != null && Constants.iSAdminAttached) {
                jsonObject.put("isFirstBoolean", "0");
            } else {
                jsonObject.put("isFirstBoolean", "1");
            }
            System.out.println("Apidata datais storeana3= " + jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.post(Constants.adminApiUrl + "/storeanalytics")
                .addJSONObjectBody(jsonObject)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            System.out.println("Apidata datais 44 " + response.toString());


                        } catch (Exception e) {
                            e.printStackTrace();
                            System.out.println("Apidata error 44 " + e.getMessage());
                        }

                    }

                    @Override
                    public void onError(ANError error) {
                        error.printStackTrace();
                        System.out.println("Apidata error 444" + error.getMessage());

                    }
                });


        checkExistingAppDataFromAdminPanelAndSetData(SplashScreen.this);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1009) {
            if (resultCode != RESULT_OK) {
                goToMainAndFinish();
            } else {
                goToMainAndFinish();
            }
        }
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        newBase = LocaleHelper.onAttach(newBase);
        super.attachBaseContext(newBase);
    }


}



