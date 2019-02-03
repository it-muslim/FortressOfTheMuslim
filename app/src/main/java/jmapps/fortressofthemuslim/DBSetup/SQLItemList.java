package jmapps.fortressofthemuslim.DBSetup;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import jmapps.fortressofthemuslim.Model.MainItemsModel;

public class SQLItemList extends SQLiteOpenHelper {

    private static final String DBName = "FortressOfTheMuslimDB";
    private static final int DBVersion = 2;

    private static final String TableName = "Table_of_dua";

    private static final String mPositionId = "_id";
    private static final String mContentArabic = "content_arabic";
    private static final String mContentTranscription = "content_transcription";
    private static final String mContentRussian = "content_russian";
    private static final String mContentSource = "content_source";

    private static final String createTable = "CREATE TABLE " + TableName + "(" +
            mPositionId + " INTEGER PRIMARY KEY AUTOINCREMENT, " + mContentArabic +
            " TEXT, " + mContentTranscription + " TEXT, " + mContentRussian + " TEXT, " + mContentSource + " TEXT)";

    private static final String[] mColumnsContent = {mPositionId, mContentArabic, mContentTranscription,
            mContentRussian, mContentSource};

    public SQLItemList(Context context) {
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

    public List<MainItemsModel> getItemList() {

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

        List<MainItemsModel> allItems = new ArrayList<>();

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                allItems.add(new MainItemsModel(
                        cursor.getString(cursor.getColumnIndex(mPositionId)),
                        cursor.getString(cursor.getColumnIndex(mContentArabic)),
                        cursor.getString(cursor.getColumnIndex(mContentTranscription)),
                        cursor.getString(cursor.getColumnIndex(mContentRussian)),
                        cursor.getString(cursor.getColumnIndex(mContentSource))));
                cursor.moveToNext();
            }
        }

        return allItems;
    }

    public List<MainItemsModel> getBookmarkList() {

        SQLiteDatabase databaseFortress = this.getReadableDatabase();

        @SuppressLint("Recycle")
        Cursor cursor = databaseFortress.query(TableName,
                mColumnsContent,
                "item_favorite = 1",
                null, null,
                null, null,
                null);

        List<MainItemsModel> allBookmarks = new ArrayList<>();

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                allBookmarks.add(new MainItemsModel(
                        cursor.getString(cursor.getColumnIndex(mPositionId)),
                        cursor.getString(cursor.getColumnIndex(mContentArabic)),
                        cursor.getString(cursor.getColumnIndex(mContentTranscription)),
                        cursor.getString(cursor.getColumnIndex(mContentRussian)),
                        cursor.getString(cursor.getColumnIndex(mContentSource))));
                cursor.moveToNext();
            }
        }
        return allBookmarks;
    }
}