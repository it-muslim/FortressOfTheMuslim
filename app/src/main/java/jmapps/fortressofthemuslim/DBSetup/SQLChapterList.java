package jmapps.fortressofthemuslim.DBSetup;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import jmapps.fortressofthemuslim.Model.MainChaptersModel;

public class SQLChapterList extends SQLiteOpenHelper {

    private static final String DBName = "FortressOfTheMuslimDB";
    private static final int DBVersion = 2;

    private static final String TableName = "Table_of_chapters";

    private static final String mPositionId = "_id";
    private static final String mChapterName = "chapter_name";

    private static final String createTable = "CREATE TABLE " + TableName + "(" +
            mPositionId + " INTEGER PRIMARY KEY AUTOINCREMENT, " + mChapterName + " TEXT)";

    private static final String[] mColumnsContent = {mPositionId, mChapterName};

    public SQLChapterList(Context context) {
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

    public List<MainChaptersModel> getChapterList() {

        SQLiteDatabase databaseFortress = this.getReadableDatabase();

        @SuppressLint("Recycle")
        Cursor cursor = databaseFortress.query(TableName,
                mColumnsContent,
                null,
                null,
                null,
                null,
                null,
                null);

        List<MainChaptersModel> allChapters = new ArrayList<>();

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                allChapters.add(new MainChaptersModel(
                        cursor.getString(cursor.getColumnIndex(mPositionId)),
                        cursor.getString(cursor.getColumnIndex(mChapterName))));
                cursor.moveToNext();
            }
        }

        return allChapters;
    }

    public List<MainChaptersModel> getBookmarkList() {

        SQLiteDatabase databaseFortress = this.getReadableDatabase();

        @SuppressLint("Recycle")
        Cursor cursor = databaseFortress.query(TableName,
                mColumnsContent,
                "chapter_favorite = 1",
                null, null,
                null, null,
                null);

        List<MainChaptersModel> allBookmarks = new ArrayList<>();

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                allBookmarks.add(new MainChaptersModel(
                        cursor.getString(cursor.getColumnIndex(mPositionId)),
                        cursor.getString(cursor.getColumnIndex(mChapterName))));
                cursor.moveToNext();
            }
        }
        return allBookmarks;
    }
}