package jmapps.fortressofthemuslim;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatDelegate;

import com.liulishuo.filedownloader.FileDownloader;

import static jmapps.fortressofthemuslim.Fragment.Settings.keyBStateArabic;
import static jmapps.fortressofthemuslim.Fragment.Settings.keyBStateRussian;
import static jmapps.fortressofthemuslim.Fragment.Settings.keyBStateTranscription;
import static jmapps.fortressofthemuslim.Fragment.Settings.keyGStateArabic;
import static jmapps.fortressofthemuslim.Fragment.Settings.keyGStateRussian;
import static jmapps.fortressofthemuslim.Fragment.Settings.keyGStateTranscription;
import static jmapps.fortressofthemuslim.Fragment.Settings.keyRStateArabic;
import static jmapps.fortressofthemuslim.Fragment.Settings.keyRStateRussian;
import static jmapps.fortressofthemuslim.Fragment.Settings.keyRStateTranscription;

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

        if (nightModeEnabled) {

            mEditor.putInt(keyRStateArabic, 255);
            mEditor.putInt(keyGStateArabic, 255);
            mEditor.putInt(keyBStateArabic, 255);

            mEditor.putInt(keyRStateTranscription, 255);
            mEditor.putInt(keyGStateTranscription, 255);
            mEditor.putInt(keyBStateTranscription, 255);

            mEditor.putInt(keyRStateRussian, 255);
            mEditor.putInt(keyGStateRussian, 255);
            mEditor.putInt(keyBStateRussian, 255);

            mEditor.apply();

        } else {

            mEditor.putInt(keyRStateArabic, 0);
            mEditor.putInt(keyGStateArabic, 0);
            mEditor.putInt(keyBStateArabic, 0);

            mEditor.putInt(keyRStateTranscription, 0);
            mEditor.putInt(keyGStateTranscription, 0);
            mEditor.putInt(keyBStateTranscription, 0);

            mEditor.putInt(keyRStateRussian, 0);
            mEditor.putInt(keyGStateRussian, 0);
            mEditor.putInt(keyBStateRussian, 0);

            mEditor.apply();
        }
    }
}