package com.app.fijirentalcars.SQLDB;


import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.app.fijirentalcars.Model.Manufacturer;
import com.app.fijirentalcars.listners.ManufactureListener;

import java.util.ArrayList;


public class DBHandler_Manufacture extends SQLiteOpenHelper implements ManufactureListener {
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "manufacturers.db";
    private static final String TABLE_NAME = "manufacturers_table";
    private static final String KEY_FEATURE_ID = "manufacturer_id";
    private static final String KEY_FEATURE_TITLE = "manufacturer_title";
    private static final String KEY_FEATURE_LOGO = "manufacturer_logo";

    private String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" + KEY_FEATURE_ID
            + " TEXT PRIMARY KEY," + KEY_FEATURE_TITLE + " TEXT," + KEY_FEATURE_LOGO + " TEXT)";
    private String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public DBHandler_Manufacture(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        String databasePath = context.getDatabasePath("manufacturers.db").getPath();
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
    public ArrayList<Manufacturer> getAllJobs() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Manufacturer> cityList = null;
        try {
            cityList = new ArrayList<Manufacturer>();
            String QUERY = "SELECT * FROM " + TABLE_NAME;
            @SuppressLint("Recycle") Cursor cursor = db.rawQuery(QUERY, null);
            if (!cursor.isLast()) {
                while (cursor.moveToNext()) {
                    Manufacturer govt_job = new Manufacturer();
                    govt_job.setManufacturerId(cursor.getString(0));
                    govt_job.setManufacturerTitle(cursor.getString(1));
                    govt_job.setManufacturerLogo(cursor.getString(2));
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
    public void addJobs(Manufacturer govt_job) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_FEATURE_ID, govt_job.getManufacturerId());
            values.put(KEY_FEATURE_TITLE, govt_job.getManufacturerTitle());
            values.put(KEY_FEATURE_LOGO, govt_job.getManufacturerLogo());
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
