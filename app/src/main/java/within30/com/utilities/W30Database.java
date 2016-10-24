package within30.com.utilities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import within30.com.dataobjects.CustomerDO;
import within30.com.dataobjects.LocationDO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SR Lakhsmi on 4/24/2016.
 */
public class W30Database  extends SQLiteOpenHelper {


    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "contactsManager";

    // Table Names
    private static final String TABLE_LOCATION_HISTORY = "locationhistory";
    private static final String TABLE_BOOKED_SLOTS_INFO = "bookedslotsinfo";


    // Common column names
    private static final String KEY_ID = "id";
    private static final String KEY_CITY = "city";
    private  static  final String KEY_LATITUDE  ="latitude";
    private  static  final String KEY_LONGITUDE  ="longitude";
    private static final String KEY_CREATED_AT = "created_at";

    private static final String KEY_CUSTMER_ID = "customerid";
    private static final String KEY_DEFAULT_DURATION = "defaultduration";
    private static final String KEY_CUSTOMER_NAME = "customername";
    private static final String KEY_SLOT_BOOKED_DATE = "slotbookeddate";
    private static final String KEY_SLOT_BOOKED_DATE_AND_DURSTION = "slotbookeddateandduration";

    // Table Create Statements

    private static final String CREATE_TABLE_TODO = "CREATE TABLE "
            + TABLE_LOCATION_HISTORY + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_CITY
            + " TEXT," + KEY_LATITUDE + " DOUBLE," + KEY_LONGITUDE + " DOUBLE," + KEY_CREATED_AT
            + " DATETIME" + ")";

    private static  final String CREATE_TABLE_BOOK_SLOTS_INFO= "CREATE TABLE "+ TABLE_BOOKED_SLOTS_INFO +"("+KEY_ID+
            " INTEGER PRIMARY KEY,"+ KEY_CUSTMER_ID +" TEXT, "+KEY_DEFAULT_DURATION+" INTEGER, "+KEY_CUSTOMER_NAME+" TEXT, "
            + KEY_SLOT_BOOKED_DATE+" TEXT, "+KEY_SLOT_BOOKED_DATE_AND_DURSTION+" TEXT, "+ KEY_CREATED_AT
            + " DATETIME" + ")";




    public W30Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // creating required tables
        db.execSQL(CREATE_TABLE_TODO);
        db.execSQL(CREATE_TABLE_BOOK_SLOTS_INFO);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCATION_HISTORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKED_SLOTS_INFO);


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

    String query = "SELECT "+KEY_CITY+","+KEY_LATITUDE+","+KEY_LONGITUDE+","+KEY_CREATED_AT+" FROM " + TABLE_LOCATION_HISTORY
            +" "+"WHERE LENGTH("+KEY_CITY +")>0 "+"GROUP BY"+" "+KEY_CITY
            + " ORDER BY "+KEY_CREATED_AT;

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
    public void addBookedSlotsInfo(CustomerDO customerDO) {

        // get reference of the BookDB database

        SQLiteDatabase db = this.getWritableDatabase();
        // make values to be inserted
        ContentValues values = new ContentValues();
        values.put(KEY_CUSTMER_ID, customerDO.get_id());
        values.put(KEY_DEFAULT_DURATION, customerDO.getDefaultDuration());
        values.put(KEY_CUSTOMER_NAME,customerDO.getFullName());
        String strDate = CalendarUtils.getCurrentPostDate("MM/dd/yyyy");
        Log.d("slot booked date",strDate);
        values.put(KEY_SLOT_BOOKED_DATE,strDate);
        values.put(KEY_CREATED_AT, CalendarUtils.getCurrentDateTime());
        values.put(KEY_SLOT_BOOKED_DATE_AND_DURSTION,customerDO.getSlotBookedDate());
        db.insert(TABLE_BOOKED_SLOTS_INFO, null, values);
        // close database transaction
        db.close();

    }
    public List<CustomerDO> getUnRatedCustomer() {

        String query = "SELECT  "+KEY_CUSTOMER_NAME+","+KEY_CUSTMER_ID+","+KEY_SLOT_BOOKED_DATE+","+KEY_SLOT_BOOKED_DATE_AND_DURSTION+","+
                KEY_ID+" FROM " + TABLE_BOOKED_SLOTS_INFO;

        Log.d("query", query);
        // get reference of the W30 database
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        CustomerDO customerDO  = null;
        List<CustomerDO> unratedCustomerList = new ArrayList<CustomerDO>();

        if (cursor.moveToFirst()) {

            do {
               if( CalendarUtils.getComparisionSlotBookedDateAndPresentDate(cursor.getString(3))){
                   customerDO = new CustomerDO();
                   customerDO.setCompanyName(cursor.getString(cursor.getColumnIndex(KEY_CUSTOMER_NAME)));
                   customerDO.set_id(cursor.getString(cursor.getColumnIndex(KEY_CUSTMER_ID)));
                   customerDO.setSlotBookedDate(cursor.getString(cursor.getColumnIndex(KEY_SLOT_BOOKED_DATE)));
                   customerDO.setRatingId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                   unratedCustomerList.add(customerDO);
               }

            } while (cursor.moveToNext());

        }
        return unratedCustomerList;
    }


    public void removeRatedCustomer(int ratingId) {
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(TABLE_BOOKED_SLOTS_INFO, KEY_ID + "=" + ratingId, null) ;

        }catch(Exception e){
            e.printStackTrace();
        }
    }
}