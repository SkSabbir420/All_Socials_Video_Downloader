package com.erickruo.allinonevideodownloader.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefsCoins {

    public final Context context;
    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;

    public PrefsCoins(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("myccprefs", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void setInt(String key, int value) {
        editor.putInt(key, value);
        editor.apply();
    }

    public void setString(String key, String value) {
        editor.putString(key, value);
        editor.apply();
    }

    public void setLong(String key, Long value) {
        editor.putLong(key, value);
        editor.apply();
    }

    public void setPremium(int value) {
        editor.putInt("Premium", value);
        editor.apply();
    }


    public void setRemoveAd(int value) {
        editor.putInt("RemoveAd", value);
        editor.apply();
    }


    public void setBoolean(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.apply();
    }

    public void removeKey(String key) {
        editor.remove(key);
        editor.apply();
    }

    public boolean getBoolean(String key, boolean def) {
        return sharedPreferences.getBoolean(key, def);
    }

    public int getInt(String key, int def) {
        return sharedPreferences.getInt(key, def);
    }


    public String getString(String key, String def) {
        return sharedPreferences.getString(key, def);
    }

    public Long getLong(String key, Long def) {
        return sharedPreferences.getLong(key, def);
    }

    public int getPremium() {
        return sharedPreferences.getInt("Premium", 0);
    }

    public int getRemoveAd() {
        return sharedPreferences.getInt("RemoveAd", 0);
    }

}
