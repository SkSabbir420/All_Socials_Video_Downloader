/*
 * *
 *  * Created by Syed Usama Ahmad on 3/15/23, 2:27 PM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 3/15/23, 2:24 PM
 *
 */

package com.erickruo.allinonevideodownloader.utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatDelegate;

import com.google.gson.Gson;
import com.erickruo.allinonevideodownloader.models.adminpanel.AppSettings;

public class SharedPrefsMainApp {
    public static String PREFERENCE_NAME = "whatsapp_pref";
    private final String PREFERENCE_inappads = "inappads";
    private final String PREFERENCE_istutshownlong = "istutshownlong";
    private final String PREFERENCE_isBonusAlertShown = "isbonusalert";
    private final String PREFERENCE_isMetaRemovalShown = "metaremoval";
    private final String PREFERENCE_isFirstTime = "isFirstTime";
    private final String PREFERENCE_android13permissions = "android13permissions";
    private final String PREFERENCE_whatsapp = "whatsapp";
    private final String PREFERENCE_whatsappbusiness = "whatsappbusiness";
    private final String PREFERENCE_adminpanelapidata = "adminpanelapidata";
    private final String PREFERENCE_ytdlp = "ytdlp";
    private final String PREFERENCE_ffmpeg = "ffmpeg";
    private Context context;

    SharedPreferences sharedPreference;
    SharedPreferences.Editor editor;


    private static SharedPrefsMainApp instance;

    public SharedPrefsMainApp(Context context) {
        this.context = context;
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        sharedPreference = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        editor = sharedPreference.edit();
    }


    public static SharedPrefsMainApp getInstance() {
        return instance;
    }


    public String getPREFERENCE_inappads() {
        return sharedPreference.getString(PREFERENCE_inappads, "nnn");
    }

    public String getPREFERENCE_ytdlp() {
        return sharedPreference.getString(PREFERENCE_ytdlp, "");
    }

    public String getPREFERENCE_ffmpeg() {
        return sharedPreference.getString(PREFERENCE_ffmpeg, "");
    }

    public Boolean getPREFERENCE_istutshownlong() {
        return sharedPreference.getBoolean(PREFERENCE_istutshownlong, false);
    }


    public Boolean getPREFERENCE_isBonusAlertShown() {
        return sharedPreference.getBoolean(PREFERENCE_isBonusAlertShown, false);
    }


    public void setPREFERENCE_isMetaRemovalShown(boolean PREFERENCE_inappads) {
        editor = sharedPreference.edit();
        editor.putBoolean(this.PREFERENCE_isMetaRemovalShown, PREFERENCE_inappads);
        editor.apply();

    }

    public Boolean getPREFERENCE_isMetaRemovalShown() {
        return sharedPreference.getBoolean(PREFERENCE_isMetaRemovalShown, false);
    }

    public Boolean getPREFERENCE_isFirstTime() {
        return sharedPreference.getBoolean(PREFERENCE_isFirstTime, false);
    }


    public Boolean getPREFERENCE_android13permissions() {
        return sharedPreference.getBoolean(PREFERENCE_android13permissions, false);
    }

    public String getPREFERENCE_whatsapp() {
        return sharedPreference.getString(PREFERENCE_whatsapp, "");
    }

    public String getPREFERENCE_whatsappbusiness() {
        return sharedPreference.getString(PREFERENCE_whatsappbusiness, "");
    }

    public void setPREFERENCE_inappads(String PREFERENCE_inappads) {
        editor = sharedPreference.edit();
        editor.putString(this.PREFERENCE_inappads, PREFERENCE_inappads);
        editor.apply();
    }


    public void setPREFERENCE_ytdlp(String PREFERENCE_ytdlp) {
        editor = sharedPreference.edit();
        editor.putString(this.PREFERENCE_ytdlp, PREFERENCE_ytdlp);
        editor.apply();
    }

    public void setPREFERENCE_ffmpeg(String PREFERENCE_ffmpeg) {
        editor = sharedPreference.edit();
        editor.putString(this.PREFERENCE_ffmpeg, PREFERENCE_ffmpeg);
        editor.apply();
    }

    public void setPREFERENCE_istutshownlong(boolean PREFERENCE_istutshownlong) {
        editor = sharedPreference.edit();
        editor.putBoolean(this.PREFERENCE_istutshownlong, PREFERENCE_istutshownlong);
        editor.apply();
    }


    public void setPREFERENCE_isBonusAlertShown(boolean PREFERENCE_isBonusAlertShown) {
        editor = sharedPreference.edit();
        editor.putBoolean(this.PREFERENCE_isBonusAlertShown, PREFERENCE_isBonusAlertShown);
        editor.apply();
    }

    public void setPREFERENCE_isFirstTime(boolean PREFERENCE_isFirstTime) {
        editor = sharedPreference.edit();
        editor.putBoolean(this.PREFERENCE_isFirstTime, PREFERENCE_isFirstTime);
        editor.apply();
    }


    public void setPREFERENCE_android13permissions(boolean PREFERENCE_android13permissions) {
        editor = sharedPreference.edit();
        editor.putBoolean(this.PREFERENCE_android13permissions, PREFERENCE_android13permissions);
        editor.apply();
    }

    public void setPREFERENCE_whatsapp(String PREFERENCE_whatsapp) {
        editor = sharedPreference.edit();
        editor.putString(this.PREFERENCE_whatsapp, PREFERENCE_whatsapp);
        editor.apply();
    }

    public void setPREFERENCE_whatsappbusiness(String PREFERENCE_whatsappbusiness) {
        editor = sharedPreference.edit();
        editor.putString(this.PREFERENCE_whatsappbusiness, PREFERENCE_whatsappbusiness);
        editor.apply();
    }


    public void setPREFERENCE_adminpanelapidata(String map) {
        editor = sharedPreference.edit();
        editor.putString(PREFERENCE_adminpanelapidata, map);
        editor.apply();
    }

    public AppSettings getPREFERENCE_adminpanelapidata() {
        try {
            Gson gson = new Gson();
            String storedHashMapString = sharedPreference.getString(PREFERENCE_adminpanelapidata, "oopsDintWork");

            return gson.fromJson(storedHashMapString, AppSettings.class);
        } catch (Exception e) {
//            e.printStackTrace();
            return null;
        }
    }


    public void clearSharePrefs() {
        sharedPreference.edit().clear().apply();
    }


}
