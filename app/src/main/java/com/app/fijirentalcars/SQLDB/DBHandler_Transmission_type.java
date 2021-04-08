package com.app.fijirentalcars.SQLDB;


import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.app.fijirentalcars.Model.TransmissionType;
import com.app.fijirentalcars.listners.TransmissionListener;

import java.util.ArrayList;


public class DBHandler_Transmission_type extends SQLiteOpenHelper implements TransmissionListener {
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "transmission_types.db";
    private static final String TABLE_NAME = "transmission_types_table";
    private static final String KEY_TRANSMISSION_TYPE_ID = "transmission_type_id";
    private static final String KEY_TRANSMISSION_TYPE_TITLE = "transmission_type_title";

    private String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" + KEY_TRANSMISSION_TYPE_ID
            + " TEXT PRIMARY KEY," + KEY_TRANSMISSION_TYPE_TITLE + " TEXT)";
    private String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public DBHandler_Transmission_type(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        String databasePath = context.getDatabasePath("transmission_types.db").getPath();
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
    public ArrayList<TransmissionType> getAllJobs() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<TransmissionType> cityList = null;
        try {
            cityList = new ArrayList<TransmissionType>();
            String QUERY = "SELECT * FROM " + TABLE_NAME;
            @SuppressLint("Recycle") Cursor cursor = db.rawQuery(QUERY, null);
            if (!cursor.isLast()) {
                while (cursor.moveToNext()) {
                    TransmissionType govt_job = new TransmissionType();
                    govt_job.setTransmissionTypeId(cursor.getString(0));
                    govt_job.setTransmissionTypeTitle(cursor.getString(1));
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
    public void addJobs(TransmissionType govt_job) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_TRANSMISSION_TYPE_ID, govt_job.getTransmissionTypeId());
            values.put(KEY_TRANSMISSION_TYPE_TITLE, govt_job.getTransmissionTypeTitle());
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
