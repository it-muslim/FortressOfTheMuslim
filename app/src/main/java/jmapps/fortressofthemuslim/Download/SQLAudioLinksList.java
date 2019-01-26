package jmapps.fortressofthemuslim.Download;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class SQLAudioLinksList extends SQLiteOpenHelper {

    private static final String DBName = "FortressOfTheMuslimDB";
    private static final int DBVersion = 1;

    private static final String TableName = "Table_audio_links";

    private static final String mPositionId = "_id";
    private static final String mLinkAudio = "Link_audio";

    private static final String createTable = "CREATE TABLE " + TableName + "(" +
            mPositionId + " INTEGER PRIMARY KEY AUTOINCREMENT, " + mLinkAudio + " TEXT)";

    private static final String[] mColumnsContent = {mPositionId, mLinkAudio};

    SQLAudioLinksList(Context context) {
        super(context, DBName, null, DBVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TableName);
        this.onCreate(db);
    }

    List<LinkAudioModel> getAllLinks() {

        SQLiteDatabase databaseFortress = this.getReadableDatabase();

        @SuppressLint("Recycle")
        Cursor cursor = databaseFortress.query(TableName,
                mColumnsContent,
                null, null, null,
                null, null, null);

        List<LinkAudioModel> allLinks = new ArrayList<>();

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                allLinks.add(new LinkAudioModel(
                        cursor.getString(cursor.getColumnIndex(mPositionId)),
                        cursor.getString(cursor.getColumnIndex(mLinkAudio))));
                cursor.moveToNext();
            }
        }
        return allLinks;
    }

    List<LinkAudioModel> getSelectivelyLinks(int sampleBy) {

        SQLiteDatabase databaseFortress = this.getReadableDatabase();

        @SuppressLint("Recycle")
        Cursor cursor = databaseFortress.query(TableName,
                mColumnsContent,
                "Sample_by = ?",
                new String[]{String.valueOf(sampleBy)},
                null, null,
                null, null);

        List<LinkAudioModel> allLinks = new ArrayList<>();

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                allLinks.add(new LinkAudioModel(
                        cursor.getString(cursor.getColumnIndex(mPositionId)),
                        cursor.getString(cursor.getColumnIndex(mLinkAudio))));
                cursor.moveToNext();
            }
        }
        return allLinks;
    }

    List<LinkAudioModel> getOnlyLink() {

        SQLiteDatabase databaseFortress = this.getReadableDatabase();

        @SuppressLint("Recycle")
        Cursor cursor = databaseFortress.query(TableName,
                mColumnsContent,
                null, null, null,
                null, null, null);

        List<LinkAudioModel> allLinks = new ArrayList<>();

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                allLinks.add(new LinkAudioModel(
                        cursor.getString(cursor.getColumnIndex(mPositionId)),
                        cursor.getString(cursor.getColumnIndex(mLinkAudio))));
                cursor.moveToNext();
            }
        }
        return allLinks;
    }
}