package jmapps.fortressofthemuslim;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatDelegate;

import com.liulishuo.filedownloader.FileDownloader;

public class MainApplication extends Application {

    public static final String keyNightMode = "key_night_mode";
    private boolean isNightModeEnabled = false;
    private SharedPreferences.Editor mEditor;
    private static MainApplication singleton;

    public static MainApplication getSingleton() {
        if (singleton == null) {
            singleton = new MainApplication();
        }
        return singleton;
    }

    @SuppressLint("CommitPrefEdits")
    @Override
    public void onCreate() {
        super.onCreate();

        FileDownloader.setup(this);

        SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mPreferences.edit();

        singleton = this;

        this.isNightModeEnabled = mPreferences.getBoolean(keyNightMode, false);

        if (isNightModeEnabled) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    public boolean isNightModeEnabled() {
        return isNightModeEnabled;
    }

    public void setNightModeEnabled(boolean nightModeEnabled) {
        this.isNightModeEnabled = nightModeEnabled;
        mEditor.putBoolean(keyNightMode, nightModeEnabled).apply();
    }
}