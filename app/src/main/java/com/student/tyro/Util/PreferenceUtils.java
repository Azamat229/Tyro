package com.student.tyro.Util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferenceUtils {

    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    Context context;

    // Shared pref mode
    int PRIVATE_MODE = 0;
    // Sharedpref file name
    private static final String PREF_NAME = "closet";
    public static final String KEY_TYPE = "type";
    public static final String LANGUAGE = "language";




    public PreferenceUtils(Context context) {
        pref = PreferenceManager.getDefaultSharedPreferences(context);
        this.pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        this.editor = pref.edit();
        this.context = context;
    }

    public void setType(String type) {

        // Storing name in pref
        editor.putString(KEY_TYPE, type);

        editor.commit();
    }

    public String getType() {
        return pref.getString(KEY_TYPE, "");
    }

    public String getLanguage() {

        return pref.getString(LANGUAGE, "");
    }


    public void setLanguage(String language) {
        editor.putString(LANGUAGE, language);
        editor.commit();
    }
}


