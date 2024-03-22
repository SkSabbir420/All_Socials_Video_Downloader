/*
 * *
 *  * Created by Syed Usama Ahmad on 3/14/23, 5:04 PM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 3/14/23, 4:56 PM
 *
 */

package com.erickruo.allinonevideodownloader.facebookstorysaver.fbutils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatDelegate;

import com.google.gson.Gson;
import com.erickruo.allinonevideodownloader.facebookstorysaver.fbmodels.ModelFacebookpref;
import com.erickruo.allinonevideodownloader.utils.iUtils;

public class Facebookprefloader {

    public static String DataFileName = "fb_prefvals";
    public static String PREFERENCE_itemFB = "fb_prefvals_item";

    public Activity context;
    SharedPreferences sharedPreference;
    SharedPreferences.Editor editor;

    public Facebookprefloader(Activity context) {
        this.context = context;
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        sharedPreference = context.getSharedPreferences(DataFileName, Context.MODE_PRIVATE);
        editor = sharedPreference.edit();
    }


    public ModelFacebookpref LoadPrefString() {
        if (sharedPreference == null) {
            sharedPreference = context.getSharedPreferences(DataFileName, Context.MODE_PRIVATE);
            editor = sharedPreference.edit();
        }
        try {
            Gson gson = new Gson();
            String storedHashMapString = sharedPreference.getString(PREFERENCE_itemFB, null);

            boolean isValid = iUtils.isValidJson(storedHashMapString);

            if (isValid) {
                return gson.fromJson(storedHashMapString, ModelFacebookpref.class);
            } else {
                return new ModelFacebookpref("", "", "false");
            }
        } catch (Exception exception) {
//            exception.printStackTrace();
            return new ModelFacebookpref("", "", "false");
        }
    }

    public void SavePref(String str2, String str3) {
        if (editor == null) {
            sharedPreference = context.getSharedPreferences(DataFileName, Context.MODE_PRIVATE);
            editor = sharedPreference.edit();
        }

        ModelFacebookpref modelFacebookpref = new ModelFacebookpref(str2, str2, str3);


        Gson gson = new Gson();
        String hashMapString = gson.toJson(modelFacebookpref);

        editor = sharedPreference.edit();
        editor.putString(PREFERENCE_itemFB, hashMapString);
        editor.apply();


    }

    public void MakePrefEmpty() {
        if (editor == null) {
            sharedPreference = context.getSharedPreferences(DataFileName, Context.MODE_PRIVATE);
            editor = sharedPreference.edit();
        }

        ModelFacebookpref modelFacebookpref = new ModelFacebookpref("", "", "false");


        Gson gson = new Gson();
        String hashMapString = gson.toJson(modelFacebookpref);

        editor = sharedPreference.edit();
        editor.putString(PREFERENCE_itemFB, hashMapString);
        editor.apply();
    }


}
