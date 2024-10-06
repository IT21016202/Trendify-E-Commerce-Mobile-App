package com.example.e_commerce;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.style.IconMarginSpan;

import java.net.NetworkInterface;

public class Session {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    final String SHARED_PREF_NAME = "session";
    final String SESSION_KEY = "user_session";

    public Session(Context context){
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }


}
