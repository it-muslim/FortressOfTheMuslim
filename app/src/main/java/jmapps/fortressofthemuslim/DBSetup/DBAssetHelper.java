package jmapps.fortressofthemuslim.DBSetup;

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class DBAssetHelper extends SQLiteAssetHelper {

    private static final String DBName = "FortressOfTheMuslimDB";
    private static final int DBVersion = 2;

    public DBAssetHelper(Context context) {
        super(context, DBName, null, DBVersion);

        setForcedUpgrade(DBVersion);
    }
}