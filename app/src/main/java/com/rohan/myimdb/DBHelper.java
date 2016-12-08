package com.rohan.myimdb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.name;
import static android.R.attr.version;

/**
 * Created by Rohan on 08-Dec-16.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "favourites_db";
    private static final String TABLE_FAVOURITES = "favourites_table";

    private static final String _ID = "id";
    private static final String COLUMN_MOVIE_ID = "movie_id";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String SQL_CREATE_FAVOURITES_TABLE = "CREATE TABLE " + TABLE_FAVOURITES + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_MOVIE_ID + " REAL UNIQUE NOT NULL" +
                " );";

        sqLiteDatabase.execSQL(SQL_CREATE_FAVOURITES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVOURITES);
        onCreate(db);
    }

    public void addToFavourites(String id) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_MOVIE_ID, id);

        db.insert(TABLE_FAVOURITES, null, contentValues);
        db.close();
    }

    public boolean isFavourite(String id) {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(TABLE_FAVOURITES, new String[]{COLUMN_MOVIE_ID}, COLUMN_MOVIE_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null && cursor.getCount() == 1)
            return true;
        else
            return false;
    }

    public void removeFromFavourites(String id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_FAVOURITES, COLUMN_MOVIE_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public List<String> getAllFavourites() {
        List<String> favouritesList = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + TABLE_FAVOURITES;

        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                favouritesList.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }

        return favouritesList;
    }


}
