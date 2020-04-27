package com.cor.frii.persistence;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Session {

    private SharedPreferences preferences;

    public Session(Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void setToken(String token) {
        preferences.edit().putString("token", token).apply();
    }

    public String getToken() {
        return preferences.getString("token", "");
    }

    public void destroySession() {
        preferences.edit().remove("token").apply();
    }
}
