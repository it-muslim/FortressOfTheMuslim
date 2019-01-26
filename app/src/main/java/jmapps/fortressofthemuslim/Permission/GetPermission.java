package jmapps.fortressofthemuslim.Permission;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import java.util.Objects;

import static jmapps.fortressofthemuslim.MainActivity.PERMISSION_REQUEST_CODE;

public class GetPermission {

    private Context mContext;

    public GetPermission(Context context) {
        this.mContext = context;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public boolean checkedPermission() {
        int result = ContextCompat.checkSelfPermission(Objects.requireNonNull(mContext), android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) mContext, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            ActivityCompat.requestPermissions((Activity) mContext, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
            Toast.makeText(mContext, "Чтобы скачать аудио требуется разрешение", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions((Activity) mContext, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }
}
