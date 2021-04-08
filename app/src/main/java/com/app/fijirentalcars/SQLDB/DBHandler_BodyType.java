package com.app.fijirentalcars.SQLDB;


import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.app.fijirentalcars.Model.BodyType;
import com.app.fijirentalcars.listners.BodyListener;

import java.util.ArrayList;


public class DBHandler_BodyType extends SQLiteOpenHelper implements BodyListener {
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "body_type.db";
    private static final String TABLE_NAME = "body_type_table";
    private static final String KEY_BODY_TYPE_ID = "body_type_id";
    private static final String KEY_BODY_TYPE_TITLE = "body_type_title";

    private String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" + KEY_BODY_TYPE_ID
            + " TEXT PRIMARY KEY," + KEY_BODY_TYPE_TITLE + " TEXT)";
    private String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public DBHandler_BodyType(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        String databasePath = context.getDatabasePath("body_type.db").getPath();
        Log.e("databasePath", "" + databasePath);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE);
        onCreate(db);
    }

    @Override
    public ArrayList<BodyType> getAllJobs() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<BodyType> cityList = null;
        try {
            cityList = new ArrayList<BodyType>();
            String QUERY = "SELECT * FROM " + TABLE_NAME;
            @SuppressLint("Recycle") Cursor cursor = db.rawQuery(QUERY, null);
            if (!cursor.isLast()) {
                while (cursor.moveToNext()) {
                    BodyType govt_job = new BodyType();
                    govt_job.setBody_type_id(cursor.getString(0));
                    govt_job.setBodyTypeTitle(cursor.getString(1));
                    cityList.add(govt_job);
                }
            }
            db.close();
        } catch (Exception e) {
            Log.e("error", e + "");
        }
        return cityList;
    }

    @Override
    public int getJobCount() {
        int num = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            String QUERY = "SELECT * FROM " + TABLE_NAME;
            @SuppressLint("Recycle") Cursor cursor = db.rawQuery(QUERY, null);
            num = cursor.getCount();
            db.close();
            return num;
        } catch (Exception e) {
            Log.e("error", e + "");
        }
        return 0;
    }

    @Override
    public void addJobs(BodyType govt_job) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_BODY_TYPE_ID, govt_job.getBody_type_id());
            values.put(KEY_BODY_TYPE_TITLE, govt_job.getBodyTypeTitle());
            //  db.insert(TABLE_NAME, null, values);
            db.insertWithOnConflict(TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);
            db.close();
        } catch (Exception e) {
            Log.e("problem", e + "");
        }
    }

    @Override
    public int removeAllJobs() {
        SQLiteDatabase db = this.getWritableDatabase();
        int status = db.delete(TABLE_NAME, null, null);
        db.close();

        return status;
    }

}
