/**
 * The SavePref class is a helper class in the package that provides methods for
 * saving and retrieving data from SharedPreferences in an Android application.
 */
package com.wowcodes.supreme;

import android.content.Context;
import android.content.SharedPreferences;

public class SavePref {

    public final String PREFS_NAME = "extraclass_prefs";
    private final Context context;
    public static final String FAVORITES2 = "Image_Favorite";
    private static final String THEME_PREF_KEY = "theme_mode";
    private static final boolean THEME_LIGHT = false;
    private static final boolean THEME_DARK = true;
    private static final String THEME_PREFS_NAME = "theme_prefs"; // Change this to your actual preferences file name
    private static final String MODE_KEY = "mode";           // Key for dark/light mode
    private static final String DEFAULT_MODE_KEY = "default_mode"; // Key for default system mode

    public final String FAVORITES = "Notification";

    public SavePref(Context context) {
        this.context = context;
    }


    public String getClasses() {
        try {
            SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
            return prefs.getString("classes", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public void setClasses(String classes) {
        try {
            SharedPreferences sharedPref = context.getSharedPreferences(PREFS_NAME, 0);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("classes", classes);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public String getClassesName() {
        try {
            SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
            return prefs.getString("classesname", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public void setClassesName(String classes) {
        try {
            SharedPreferences sharedPref = context.getSharedPreferences(PREFS_NAME, 0);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("classesname", classes);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getemail() {
        try {
            SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
            return prefs.getString("email", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public void setemail(String classes) {
        try {
            SharedPreferences sharedPref = context.getSharedPreferences(PREFS_NAME, 0);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("email", classes);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public String getUserPhone() {
        try {
            SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
            return prefs.getString("userphone", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public void setUserPhone(String classes) {
        try {
            SharedPreferences sharedPref = context.getSharedPreferences(PREFS_NAME, 0);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("userphone", classes);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public String getUserId() {
        try {
            SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
            return prefs.getString("userId", "0");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public void setUserId(String classes) {
        try {
            SharedPreferences sharedPref = context.getSharedPreferences(PREFS_NAME, 0);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("userId", classes);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        try {
            SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
            return prefs.getString("name", "0");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public void setName(String classes) {
        try {
            SharedPreferences sharedPref = context.getSharedPreferences(PREFS_NAME, 0);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("name", classes);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setCity(String classes) {
        try {
            SharedPreferences sharedPref = context.getSharedPreferences(PREFS_NAME, 0);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("city", classes);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getCity() {
        try {
            SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
            return prefs.getString("city", "0");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public void setCityId(String classes) {
        try {
            SharedPreferences sharedPref = context.getSharedPreferences(PREFS_NAME, 0);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("cityId", classes);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getCityId() {
        try {
            SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
            return prefs.getString("cityId", "1");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getLang() {
        try {
            SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
            return prefs.getString("lang", "en");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public void setLang(String classes) {
        try {
            SharedPreferences sharedPref = context.getSharedPreferences(PREFS_NAME, 0);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("lang", classes);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean getMode() {
        try {
            SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
            //return prefs.getBoolean("mode", false);
            return false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void setMode(boolean mode) {
        try {
            SharedPreferences sharedPref = context.getSharedPreferences(PREFS_NAME, 0);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean("mode" , mode);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public boolean getThemeMode() {
        SharedPreferences prefs = context.getSharedPreferences(THEME_PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getBoolean(MODE_KEY, false); // Default to light mode if not set
    }

    public void setThemeMode(boolean mode) {
        SharedPreferences sharedPref = context.getSharedPreferences(THEME_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(MODE_KEY, mode);
        editor.apply();
    }
    public boolean getDefaultThemeMode() {
        SharedPreferences prefs = context.getSharedPreferences(THEME_PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getBoolean(DEFAULT_MODE_KEY, false); // Default to false if not set
    }

    public void setDefaultThemeMode(boolean mode) {
        SharedPreferences sharedPref = context.getSharedPreferences(THEME_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(DEFAULT_MODE_KEY, mode);
        editor.apply();
    }
    public boolean getDefaultMode() {
        try {
            SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
            //return prefs.getBoolean("mode", false);
            return false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void setDefaultMode(boolean mode) {
        try {
            SharedPreferences sharedPref = context.getSharedPreferences(PREFS_NAME, 0);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean("mode" , mode);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private boolean getBoolean(String key, boolean defaultValue) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getBoolean(key, defaultValue);
    }

    private void putBoolean(String key, boolean value) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

}