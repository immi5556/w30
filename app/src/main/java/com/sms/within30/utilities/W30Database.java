package com.sms.within30.utilities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.sms.within30.dataobjects.LocationDO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SR Lakhsmi on 4/24/2016.
 */
public class W30Database  extends SQLiteOpenHelper {

    // Logcat tag
    private static final String LOG = "DatabaseHelper";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "contactsManager";

    // Table Names
    private static final String TABLE_LOCATION_HISTORY = "locationhistory";


    // Common column names
    private static final String KEY_ID = "id";
    private static final String KEY_CITY = "city";
    private  static  final String KEY_LATITUDE  ="latitude";
    private  static  final String KEY_LONGITUDE  ="longitude";
    private static final String KEY_CREATED_AT = "created_at";





    // Table Create Statements

    private static final String CREATE_TABLE_TODO = "CREATE TABLE "
            + TABLE_LOCATION_HISTORY + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_CITY
            + " TEXT," + KEY_LATITUDE + " DOUBLE," + KEY_LONGITUDE + " DOUBLE," + KEY_CREATED_AT
            + " DATETIME" + ")";

    public W30Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // creating required tables
        db.execSQL(CREATE_TABLE_TODO);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCATION_HISTORY);


        // create new tables
        onCreate(db);
    }
    public void addLocationHistory(LocationDO locationDO) {

        // get reference of the BookDB database

        SQLiteDatabase db = this.getWritableDatabase();

        // make values to be inserted

        ContentValues values = new ContentValues();

        values.put(KEY_CITY, locationDO.getCity());

        values.put(KEY_LATITUDE, locationDO.getLatitude());
        values.put(KEY_LONGITUDE, locationDO.getLongitude());
        values.put(KEY_CREATED_AT, CalendarUtils.getCurrentDateTime());

        // insert location

        db.insert(TABLE_LOCATION_HISTORY, null, values);
        // close database transaction
        db.close();

    }
    public List getLocationHistory() {

        List<LocationDO> loccationHistory = new ArrayList<LocationDO>();

            // select location query

    String query = "SELECT DISTINCT "+KEY_CITY+","+KEY_LATITUDE+","+KEY_LONGITUDE+","+KEY_CREATED_AT+" FROM " + TABLE_LOCATION_HISTORY + " ORDER BY "+KEY_CREATED_AT;



            // get reference of the W30 database


    SQLiteDatabase db = this.getWritableDatabase();

    Cursor cursor = db.rawQuery(query, null);

    LocationDO locationDO = null;

            if (cursor.moveToFirst()) {

        do {

            locationDO = new LocationDO();

            locationDO.setCity(cursor.getString(0));
            locationDO.setLatitude(cursor.getDouble(1));
            locationDO.setLongitude(cursor.getDouble(2));
            Log.d("created date ",cursor.getString(3));
            loccationHistory.add(locationDO);

        } while (cursor.moveToNext());

    }

            return loccationHistory;

}


}