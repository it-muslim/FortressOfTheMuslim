package jmapps.fortressofthemuslim;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent toMainActivity = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(toMainActivity);
                finish();
            }
        }, 1500);
    }
}