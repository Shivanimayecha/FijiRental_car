package com.app.fijirentalcars.SQLDB;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.app.fijirentalcars.Model.FutureModel;
import com.app.fijirentalcars.Model.Manufacturer;
import com.app.fijirentalcars.listners.FetureDbListner;

import java.util.ArrayList;

public class DBHandler_Feature extends SQLiteOpenHelper implements FetureDbListner {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "feature.db";
    private static final String TABLE_NAME = "feature_table";
    private static final String KEY_FEATURE_ID = "feature_id";
    private static final String KEY_FEATURE_TITLE = "feature_title";
    private static final String KEY_FEATURE_ICON = "feature_icon";


    private String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" + KEY_FEATURE_ID
            + " TEXT PRIMARY KEY," + KEY_FEATURE_TITLE + " TEXT,"+KEY_FEATURE_ICON+" TEXT)";
    private String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public DBHandler_Feature(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        String databasePath = context.getDatabasePath("feature.db").getPath();
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
    public void addJobs(FutureModel vehicle) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_FEATURE_ID, vehicle.getFeatureId());
            values.put(KEY_FEATURE_TITLE, vehicle.getFeatureTitle());
            values.put(KEY_FEATURE_ICON, vehicle.getFeatureIcon());
            //  db.insert(TABLE_NAME, null, values);
            db.insertWithOnConflict(TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);
            db.close();
        } catch (Exception e) {
            Log.e("problem", e + "");
        }
    }

    @Override
    public ArrayList<FutureModel> getAllJobs() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<FutureModel> cityList = null;
        try {
            cityList = new ArrayList<FutureModel>();
            String QUERY = "SELECT * FROM " + TABLE_NAME;
            @SuppressLint("Recycle") Cursor cursor = db.rawQuery(QUERY, null);
            if (!cursor.isLast()) {
                while (cursor.moveToNext()) {
                    FutureModel govt_job = new FutureModel();
                    govt_job.setFeatureId(cursor.getString(0));
                    govt_job.setFeatureTitle(cursor.getString(1));
                    govt_job.setFeatureIcon(cursor.getString(2));
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
    public int removeAllJobs() {
        SQLiteDatabase db = this.getWritableDatabase();
        int status = db.delete(TABLE_NAME, null, null);
        db.close();

        return status;
    }
}
