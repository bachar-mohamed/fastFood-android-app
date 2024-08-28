package com.example.burgertemp;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SQLiteHelper extends SQLiteOpenHelper {

    private final Context context;
    private static final String DATABASE_NAME = "product.db";
    private static final String PRODUCT_TABLE_NAME = "product";
    private static final int VERSION = 1;
    private static final String PRODUCT_COLUMN_ID = "product_id";
    private static final String PRODUCT_COLUMN_NAME = "product_name";
    private static final String PRODUCT_COLUMN_INGREDIENTS = "ingredients";
    private static final String PRODUCT_COLUMN_PRICE = "price";
    private static final String PRODUCT_COLUMN_ORDER = "orders";
    private static final String PRODUCT_COLUMN_CALORIES = "calories";
    private static final String PRODUCT_COLUMN_WEIGHT = "weight";
    private static final String PRODUCT_COLUMN_RATING = "rating";
    private static final String PRODUCT_COLUMN_DELAY = "wait_time";
    private static final String PRODUCT_COLUMN_IMG = "image_link";
    private static final String PRODUCT_COLUMN_CATEGORY = "category";


    private static final String USER_TABLE_NAME = "app_user";
    private static final String USER_ID = "user_id";

    private static final String PRODUCT_QUANTITY="quantity";
    private static final String USER_FULL_NAME = "full_name";
    private static final String USER_EMAIL = "email";
    private static final String USER_ADDRESS = "address";
    private static final String USER_COUNTRY = "country";
    private static final String USER_PHONE_NUMBER = "phone_number";
    private static final String USER_ZIP_CODE = "zip_code";
    private static final String USER_CITY = "city";
    private static final String USER_PASSWORD = "password";

    private static final String USER_CART = "user_cart";

    private String query;
    SQLiteDatabase db;
    ContentValues cv;


    public SQLiteHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, VERSION);
        this.context = context;
        this.db = this.getWritableDatabase();
        this.cv = new ContentValues();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        query = "CREATE TABLE " + PRODUCT_TABLE_NAME + "(" +
                PRODUCT_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                PRODUCT_COLUMN_NAME + " TEXT, " +
                PRODUCT_COLUMN_IMG + " INTEGER, " +
                PRODUCT_COLUMN_INGREDIENTS + " TEXT, " +
                PRODUCT_COLUMN_PRICE + " TEXT, " +
                PRODUCT_COLUMN_ORDER + " INT, "+
                PRODUCT_COLUMN_WEIGHT + " TEXT, "+
                PRODUCT_COLUMN_CALORIES + " TEXT, "+
                PRODUCT_COLUMN_RATING + " TEXT, "+
                PRODUCT_COLUMN_DELAY + " TEXT, "+
                PRODUCT_COLUMN_CATEGORY + " INT);";
        db.execSQL(query);


        query = "CREATE TABLE " + USER_TABLE_NAME + "(" +
                USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                USER_FULL_NAME + " TEXT, " +
                USER_EMAIL + " TEXT, " +
                USER_PASSWORD + " TEXT," +
                USER_ADDRESS + " TEXT, " +
                USER_CITY + " TEXT, " +
                USER_COUNTRY + " TEXT, " +
                USER_PHONE_NUMBER + " TEXT, " +
                USER_ZIP_CODE + " TEXT " +");";
        db.execSQL(query);


        query = "CREATE TABLE "+USER_CART+"(" +
                USER_ID + " INTEGER," +
                PRODUCT_COLUMN_ID + " INTEGER," +
                PRODUCT_QUANTITY+" INTEGER,"+
                "PRIMARY KEY(" + USER_ID + "," + PRODUCT_COLUMN_ID + ")," +
                "FOREIGN KEY(" + USER_ID + ") REFERENCES " + USER_TABLE_NAME + "(" + USER_ID + ")," +
                "FOREIGN KEY(" + PRODUCT_COLUMN_ID + ") REFERENCES " + PRODUCT_TABLE_NAME + "(" + PRODUCT_COLUMN_ID + "));";
        db.execSQL(query);

       // Toast.makeText(context, "created", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + PRODUCT_TABLE_NAME + ";");
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE_NAME + ";");
        db.execSQL("DROP TABLE IF EXISTS " + USER_CART + ";");
    }


    public void addProduct(int id, String name, int img_url, String ingredients, String price, int orders,String weight,String calories,String rating,String delay,int category) {
        cv.put(PRODUCT_COLUMN_ID, id);
        cv.put(PRODUCT_COLUMN_NAME, name);
        cv.put(PRODUCT_COLUMN_IMG, img_url);
        cv.put(PRODUCT_COLUMN_INGREDIENTS, ingredients);
        cv.put(PRODUCT_COLUMN_PRICE, price);
        cv.put(PRODUCT_COLUMN_ORDER, orders);
        cv.put(PRODUCT_COLUMN_CATEGORY, category);
        cv.put(PRODUCT_COLUMN_CALORIES, calories);
        cv.put(PRODUCT_COLUMN_RATING, rating);
        cv.put(PRODUCT_COLUMN_WEIGHT, weight);
        cv.put(PRODUCT_COLUMN_DELAY, delay);
        long result = db.insert(PRODUCT_TABLE_NAME, null, cv);
        if (result == -1) {
           // Toast.makeText(context, "failed", Toast.LENGTH_SHORT).show();
        } else {
          //  Toast.makeText(context, "success", Toast.LENGTH_SHORT).show();
        }
    }

    public void addUser(String userName,String userEmail, String password) {
        this.cv = new ContentValues();
        //cv.put(USER_ID, id);
        cv.put(USER_FULL_NAME, userName);
        cv.put(USER_EMAIL, userEmail);
        cv.put(USER_PASSWORD, password);
        long result = db.insert(USER_TABLE_NAME, null, cv);

        if (result == -1) {
           // Toast.makeText(context, "failed", Toast.LENGTH_SHORT).show();
        } else {
           // Toast.makeText(context, "success", Toast.LENGTH_SHORT).show();
        }
    }

    public void addToUserCart(int userId, int prod_id,int quantity) {
        this.cv = new ContentValues();
        cv.put(USER_ID, userId);
        cv.put(PRODUCT_COLUMN_ID, prod_id);
        cv.put(PRODUCT_QUANTITY,quantity);
        long result = db.insert(USER_CART, null, cv);

        if (result == -1) {
           // Toast.makeText(context, "failed", Toast.LENGTH_SHORT).show();
        } else {
           // Toast.makeText(context, "success", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean checkUserAddress(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        query = "SELECT " + USER_ADDRESS + " FROM " + USER_TABLE_NAME + " WHERE " + USER_ID + " = " + userId;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex("address");
            if (cursor.isNull(columnIndex)) {
                Toast.makeText(context,"value is null",Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        cursor.close();
        return true;
    }

    public int setUserAddress(int userId,String address,String country,String city,String phoneNumber,String zipCode){
        SQLiteDatabase db = this.getReadableDatabase();
        this.cv = new ContentValues();
        //query = "UPDATE "+USER_TABLE_NAME+" SET "+USER_ADDRESS+" ='"+address+"',"+USER_COUNTRY+" ='"+country+"',"+USER_CITY+"='"+city+"',"+USER_PHONE_NUMBER+"='"+phoneNumber+"',"+USER_ZIP_CODE+"='"+zipCode+"' WHERE "+USER_ID+"="+userId+";";
        cv.put(USER_ADDRESS,address);
        cv.put(USER_COUNTRY,country);
        cv.put(USER_CITY,city);
        cv.put(USER_PHONE_NUMBER,phoneNumber);
        cv.put(USER_ZIP_CODE,zipCode);
        String whereClause = USER_ID+ "= ?";
        String[] whereArgs = new String[]{Integer.toString(userId)};
        return db.update(USER_TABLE_NAME,cv,whereClause,whereArgs);
    }

    public int checkEmail(String emailAddress){
        SQLiteDatabase db = this.getReadableDatabase();
        query = "SELECT COUNT("+USER_EMAIL+") FROM "+ USER_TABLE_NAME+" WHERE "+USER_EMAIL +" LIKE '"+emailAddress+"';";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int count = cursor.getInt(0);
            cursor.close();
            return count;
        }
        return -1;

    }


    @SuppressLint("Recycle")
    public Cursor getUserCart(int userId){
        SQLiteDatabase db = this.getReadableDatabase();
        query = "SELECT "+PRODUCT_COLUMN_ID+","+PRODUCT_COLUMN_NAME+","+PRODUCT_COLUMN_IMG+","+PRODUCT_COLUMN_INGREDIENTS+","+PRODUCT_COLUMN_PRICE+","+PRODUCT_COLUMN_ORDER+","+PRODUCT_COLUMN_WEIGHT+","+PRODUCT_COLUMN_CALORIES+","+PRODUCT_COLUMN_RATING+","+PRODUCT_COLUMN_DELAY+","+PRODUCT_COLUMN_CATEGORY+" FROM "+USER_TABLE_NAME+" INNER JOIN "+USER_CART+ " USING("+USER_ID+") INNER JOIN "+PRODUCT_TABLE_NAME+ " USING("+PRODUCT_COLUMN_ID+") WHERE "+USER_ID+" = "+userId+";";
        return db.rawQuery(query,null);
    }

    public HashMap<Integer, Cursor> getCategories() {
        SQLiteDatabase db = this.getReadableDatabase();
        HashMap<Integer, Cursor> entries = new HashMap<>();
        if (db != null) {
            for (int i = 0; i < 4; i++) {
                query = "SELECT *  FROM "+PRODUCT_TABLE_NAME+" WHERE " + PRODUCT_COLUMN_CATEGORY + "=" + (i + 1) + ";";
                entries.put(i + 1, db.rawQuery(query, null));
            }
        }
        return entries;
    }

    public Cursor getUsers(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor users =null;
        if (db != null) {
            query = "SELECT * FROM "+USER_TABLE_NAME+";";
            users = db.rawQuery(query,null);
        }
        return users;
    }

    public Cursor getUserAddress(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor users =null;
        if (db != null) {
            query = "SELECT address,phone_number,country,city,zip_code FROM "+USER_TABLE_NAME+" where "+USER_ID+" = "+id+";";
            users = db.rawQuery(query,null);
        }
        return users;
    }

    public int deleteFromCart(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        int status = db.delete(USER_CART, PRODUCT_COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        //Toast.makeText(context, "deletion status "+Integer.toString(status), Toast.LENGTH_SHORT).show();
        return status;
    }


    public int getCount(){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT COUNT(*) FROM " + PRODUCT_TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int count = cursor.getInt(0);
            cursor.close();
            return count;
        }
        return -1;
    }

}
