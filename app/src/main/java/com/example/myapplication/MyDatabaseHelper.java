package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "Iotshopping.db";
    private static final int DATABASE_VERSION = 1 ;
    private static final String TABLE_NAME = "Customer";
    private static final String COULMN_ID ="Cust_id";
    private static final String COULMN_NAME = "Cust_Name";
    private static final String COULMN_EMAIL = "Cust_Email";
    private static final String COULMN_PASSWORD = "Cust_Password";

    private static final String COULMN_MOBILE = "Cust_Mobile_No";
    private static final String COULMN_ADDRESS = "Cust_Address";
    private static final String COULMN_CITY = "Cust_City";
    private static final String COULMN_STATE = "Cust_State";
    private static final String COULMN_COUNTRY = "Cust_Country";

    private static final String COULMN_PINCODE = "Cust_Pincode";


    public MyDatabaseHelper(@Nullable Context context) {
        super(context,DATABASE_NAME, null, DATABASE_VERSION);

        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query= "CREATE TABLE " + TABLE_NAME +
                "(" + COULMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COULMN_NAME + " TEXT, " +
                COULMN_EMAIL + " TEXT, " +
                COULMN_PASSWORD + " TEXT, " +
                COULMN_MOBILE + " TEXT, " +
                COULMN_ADDRESS + " TEXT, " +
                COULMN_CITY + " TEXT, " +
                COULMN_STATE + " TEXT, " +
                COULMN_COUNTRY + " TEXT, " +
                COULMN_PINCODE + " INTEGER);";

        db.execSQL(query);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);

    }

    void addCustomer(String name,String email,String password,String mobile,String address,String city,String state,String country, int pincode){

        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COULMN_NAME,name);
        cv.put(COULMN_EMAIL,email);
        cv.put(COULMN_PASSWORD,password);
        cv.put(COULMN_MOBILE,mobile);
        cv.put(COULMN_ADDRESS,address);
        cv.put(COULMN_CITY,city);
        cv.put(COULMN_STATE,state);
        cv.put(COULMN_COUNTRY,country);
        cv.put(COULMN_PINCODE,pincode);

        long result= db.insert(TABLE_NAME,null,cv);

        if (result == -1){
            Toast.makeText(context, "Register Failed", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Register Successfully", Toast.LENGTH_SHORT).show();
        }

    }
        Cursor  viewCustomerDetails(){
            String query ="SELECT * FROM " + TABLE_NAME;
            SQLiteDatabase db= this.getReadableDatabase();
            Cursor cursor =null;
            if (db != null){
                cursor= db.rawQuery(query,null);
            }
            return  cursor;
        }


}
