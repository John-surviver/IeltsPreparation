package com.devghost.ieltspreparation.Models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "scores.db";
    private static final String TABLE_NAME = "scores";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_SCORE = "score";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_NAME + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_SCORE + " INTEGER)";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String dropTableQuery = "DROP TABLE IF EXISTS " + TABLE_NAME;
        db.execSQL(dropTableQuery);
        onCreate(db);
    }

    public void saveScores(int[] scores) {
        SQLiteDatabase db = this.getWritableDatabase();

        for (int score : scores) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_SCORE, score);
            db.insert(TABLE_NAME, null, values);
        }

        db.close();
    }

    public int[] getScores() {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_NAME + " ORDER BY " + COLUMN_ID + " DESC LIMIT 5";
        Cursor cursor = db.rawQuery(selectQuery, null);

        int[] scores = new int[5];
        int index = 0;
        int scoreIndex = cursor.getColumnIndex(COLUMN_SCORE);

        if (scoreIndex >= 0) {
            while (cursor.moveToNext() && index < 5) {
                int score = cursor.getInt(scoreIndex);
                scores[index] = score;
                index++;
            }
        } else {
            // Handle the case where the column is not found
            // You can set a default value for scores or handle the error accordingly
        }

        cursor.close();
        db.close();
        return scores;
    }
}
