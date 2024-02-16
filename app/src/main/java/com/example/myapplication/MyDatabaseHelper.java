package com.example.myapplication;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "Iotshopping.db";
    private static final String EXTERNAL_STORAGE_DIRECTORY = category.EXTERNAL_STORAGE_DIRECTORY;

    private static final int DATABASE_VERSION = 16;
    private static final String TABLE_NAME = "Customer";

    private static final String COULMN_ID ="Cust_id";
    public static final String COULMN_NAME = "Cust_Name";
    public static final String COULMN_EMAIL = "Cust_Email";
    public static final String COULMN_PASSWORD = "Cust_Password";

    public static final String COULMN_MOBILE = "Cust_Mobile_No";
    public static final String COULMN_ADDRESS = "Cust_Address";
    public static final String COULMN_CITY = "Cust_City";
    public static final String COULMN_STATE = "Cust_State";
    public static final String COULMN_COUNTRY = "Cust_Country";

    public static final String COULMN_PINCODE = "Cust_Pincode";

    private static final String TABLE_NAME_PRODUCT = "Product";
    private static final String PRODUCT_ID = "Product_id";
    private static final String PRODUCT_NAME = "Product_name";
    private static final String PRODUCT_DESC = "Product_desc";

    private static final String PRODUCT_MRP = "Product_mrp";
    private static final String PRODUCT_SP = "Product_sp";
    private static final String PRODUCT_CATEGORY = "Product_category";
    // Modify the columns in MyDatabaseHelper
    public static final String PRODUCT_COVER_IMAGE = "Product_cover_image";
    public static final String PRODUCT_IMAGE = "Product_IMAGE_path";
    public static final int MAX_PRODUCT_IMAGES = 3;


    private static final String TABLE_NAME_SLIDER = "slider";
    public static final String SLIDER_ID = "Slide_id";
    public static final String SLIDER_IMAGE = "SLIDER_IMAGE_path";
    public static final int MAX_SLIDER_IMAGES = 3;




    private static final String TABLE_NAME_CATEGORY = "Category";
    private static final String CATEGORY_ID = "Category_id";
    private static final String CATEGORY_NAME = "Category_name";
    public static final String CATEGORY_IMG_PATH = "Category_img_path";


    private static final String TABLE_NAME_CART = "Cart";
    public static final String CART_ID = "Cart_id";
    private static final String CART_PRODUCT_ID = "Product_id";
    public static final String CART_PRODUCT_NAME = "Product_name";
    public static final String CART_COVER_IMAGE = "Product_cover_image";
    public static final String CART_PRODUCT_PRICE = "Product_price";
    public static final String CART_PRODUCT_QUANTITY = "Product_quantity";






    public MyDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_CUSTOMER = "CREATE TABLE " + TABLE_NAME + "("
                + COULMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COULMN_NAME + " TEXT,"
                + COULMN_EMAIL + " TEXT,"
                + COULMN_PASSWORD + " TEXT,"
                + COULMN_MOBILE + " TEXT,"
                + COULMN_ADDRESS + " TEXT,"
                + COULMN_CITY + " TEXT,"
                + COULMN_STATE + " TEXT,"
                + COULMN_COUNTRY + " TEXT,"
                + COULMN_PINCODE + " INTEGER"
                + ")";



        String CREATE_TABLE_PRODUCT = "CREATE TABLE " + TABLE_NAME_PRODUCT + "("
                + PRODUCT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + PRODUCT_NAME + " TEXT,"
                + PRODUCT_DESC + " TEXT,"
                + PRODUCT_MRP + " TEXT,"
                + PRODUCT_SP + " TEXT,"
                + PRODUCT_CATEGORY + " TEXT,"
                + PRODUCT_COVER_IMAGE + " TEXT,";

// Add columns for product images dynamically
        for (int i = 0; i < MAX_PRODUCT_IMAGES; i++) {
            CREATE_TABLE_PRODUCT += PRODUCT_IMAGE + "_" + i + " TEXT,";
        }

// Remove the trailing comma
        CREATE_TABLE_PRODUCT = CREATE_TABLE_PRODUCT.substring(0, CREATE_TABLE_PRODUCT.length() - 1);

// Close the CREATE TABLE statement
        CREATE_TABLE_PRODUCT += ")";





        String CREATE_TABLE_SLIDER = "CREATE TABLE " + TABLE_NAME_SLIDER + "("
                + SLIDER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + SLIDER_IMAGE + " TEXT "
        +")";





        String CREATE_TABLE_CATEGORY = "CREATE TABLE " + TABLE_NAME_CATEGORY + "("
                + CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + CATEGORY_NAME + " TEXT,"
                + CATEGORY_IMG_PATH + " TEXT"  // New column for image path
                + ")";



        String CREATE_TABLE_CART = "CREATE TABLE " + TABLE_NAME_CART + "("
                + CART_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + CART_PRODUCT_ID + " INTEGER,"
                + CART_PRODUCT_NAME + " TEXT,"
                + CART_PRODUCT_PRICE + " REAL,"
                + CART_COVER_IMAGE + " TEXT," // Add the missing column here
                + CART_PRODUCT_QUANTITY + " INTEGER,"
                + " FOREIGN KEY (" + CART_PRODUCT_ID + ") REFERENCES " + TABLE_NAME_PRODUCT + "(" + PRODUCT_ID + ")"
                + ")";






        db.execSQL(CREATE_TABLE_CART);
        db.execSQL(CREATE_TABLE_CUSTOMER);
        db.execSQL(CREATE_TABLE_PRODUCT);
        db.execSQL(CREATE_TABLE_CATEGORY);
        db.execSQL(CREATE_TABLE_SLIDER);


        // Ensure the directory exists
        File directory = new File(EXTERNAL_STORAGE_DIRECTORY);
        if (!directory.exists()) {
            directory.mkdirs();
        }


    }




    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_PRODUCT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_CATEGORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_CART);
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME_SLIDER);

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

    Cursor  viewProductDetails(){
        String query ="SELECT * FROM " + TABLE_NAME_PRODUCT;
        Log.d("SQLQuery", "Executing query: " + query);

        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery(query, null);
    }


    Cursor  viewSliderDetails(){
        String query ="SELECT * FROM " + TABLE_NAME_SLIDER;
        Log.d("SQLQuery", "Executing query: " + query);

        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery(query, null);
    }

    public Cursor viewProductDetailsbycategory(String category) {
        String query = "SELECT * FROM " + TABLE_NAME_PRODUCT + " WHERE " + MyDatabaseHelper.PRODUCT_CATEGORY + " = ?";
        Log.d("SQLQuery", "Executing query: " + query);

        SQLiteDatabase db = this.getReadableDatabase();
        // Pass the category value as the second argument
        return db.rawQuery(query, new String[]{category});
    }


    public Cursor  viewProductDetailswithdescription(String Id) {
        String query = "SELECT * FROM " + TABLE_NAME_PRODUCT + " WHERE " + MyDatabaseHelper.PRODUCT_ID + " = ?";
        Log.d("SQLQuery", "Executing query: " + query);

        SQLiteDatabase db = this.getReadableDatabase();
        // Pass the category value as the second argument
        return db.rawQuery(query, new String[]{Id});
    }


    Cursor viewCategoryDetails() {
        String query = "SELECT * FROM " + TABLE_NAME_CATEGORY;

        Log.d("SQLQuery", "Executing query: " + query);

        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery(query, null);
    }


    // Add method to add cart item to the database
    public boolean addToCart(int productId, String productName, double productPrice, int quantity, byte[] productCoverImage) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(CART_PRODUCT_ID, productId);
        cv.put(CART_PRODUCT_NAME, productName);
        cv.put(CART_PRODUCT_PRICE, productPrice);
        cv.put(CART_PRODUCT_QUANTITY, quantity);
        cv.put(CART_COVER_IMAGE, productCoverImage); // Fix typo

        // Save cover image to external storage and get the file path
        String coverImagePath = saveImageToExternalStorage("cover_" + productName,productCoverImage);
        cv.put(PRODUCT_COVER_IMAGE, coverImagePath);

        long result = db.insert(TABLE_NAME_CART, null, cv);
        db.close(); // Close the database connection

        return result != -1; // Return true if insertion was successful, false otherwise
    }

    public Cursor viewCartItems() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME_CART;
        return db.rawQuery(query, null);
    }

    public boolean removeFromCart(int cartItemId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete(TABLE_NAME_CART, CART_ID + "=?", new String[]{String.valueOf(cartItemId)});
        db.close(); // Close the database connection

        return rowsAffected > 0; // Return true if rows were affected (item was removed), false otherwise
    }

    public boolean checkIfProductInCart(String productId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            // Query the cart table to check if the product ID exists
            String query = "SELECT * FROM " + MyDatabaseHelper.TABLE_NAME_CART + " WHERE " + MyDatabaseHelper.CART_PRODUCT_ID + " = ?";
            cursor = db.rawQuery(query, new String[]{productId});

            // Check if the cursor has any rows (product exists in cart)
            return cursor != null && cursor.getCount() > 0;
        } catch (SQLException e) {
            Log.e("ProductInCartError", "Error checking if product is in cart: " + e.getMessage());
            return false; // Return false in case of an exception or error
        } finally {
            // Close the cursor and database connection
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
    }


    // Method in MyDatabaseHelper to check if a product is in the cart





    public boolean checkEmailExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + MyDatabaseHelper.COULMN_EMAIL + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public boolean checkEmailPassword(String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] selectionArgs = {email, password};
        Cursor cursor = db.query(TABLE_NAME, null, COULMN_EMAIL + "=? AND " + COULMN_PASSWORD + "=?", selectionArgs, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count > 0;
    }


    public boolean addProduct(String productName, String productDesc, String productMrp, String productSp, String selectedCategory, byte[] coverImageData, List<byte[]> imageListData) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(PRODUCT_NAME, productName);
        cv.put(PRODUCT_DESC, productDesc);
        cv.put(PRODUCT_MRP, productMrp);
        cv.put(PRODUCT_SP, productSp);
        cv.put(PRODUCT_CATEGORY, selectedCategory);
        cv.put(PRODUCT_COVER_IMAGE, coverImageData);

        // Save cover image to external storage and get the file path
        String coverImagePath = saveImageToExternalStorage("cover_" + productName, coverImageData);
        cv.put(PRODUCT_COVER_IMAGE, coverImagePath);

        List<String> imagePaths = new ArrayList<>();
        for (int i = 0; i < imageListData.size(); i++) {
            String imagePath = saveImageToExternalStorage("image_" + productName + "_" + i, imageListData.get(i));
            imagePaths.add(imagePath);
        }
        // Add file paths to the ContentValues
        for (int i = 0; i < imagePaths.size(); i++) {
            cv.put(PRODUCT_IMAGE + "_" + i, imagePaths.get(i));
        }

        long result = db.insert(TABLE_NAME_PRODUCT, null, cv);
        Log.d("DatabaseInsertion", "Result: " + result);


        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }


    public boolean addSlider(byte[] sliderImageData) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(SLIDER_IMAGE, sliderImageData); // Corrected column name

        // Save slider image to external storage and get the file path
        String sliderImagePath = saveImageToExternalStorage("slider_" + System.currentTimeMillis(), sliderImageData); // Use a unique timestamp as the image name
        cv.put(SLIDER_IMAGE, sliderImagePath); // Corrected column name

        long result = db.insert(TABLE_NAME_SLIDER, null, cv); // Corrected table name
        db.close();

        return result != -1;
    }




    public boolean addCategory(String Category_name, String Category_imgPath) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(CATEGORY_NAME, Category_name);

        try {
            // Use the provided image path directly
            cv.put(CATEGORY_IMG_PATH, Category_imgPath);

            long result = db.insert(TABLE_NAME_CATEGORY, null, cv);

            if (result == -1) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            Log.e("AddCategoryException", "Error adding category: " + e.getMessage());
            return false;
        } finally {
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
    }


    private String saveImageToExternalStorage(String imageName, byte[] imageData) {
        try {
            // Save the image to external storage
            File directory = new File(category.EXTERNAL_STORAGE_DIRECTORY); // Use category.EXTERNAL_STORAGE_DIRECTORY
            if (!directory.exists()) {
                directory.mkdirs(); // Create the directory if it doesn't exist
            }

            if (imageData == null) {
                Log.e("SaveImage", "Image data is null for image: " + imageName);
                return null;
            }


            SimpleDateFormat edf =new SimpleDateFormat("yyyyMMdd_HHmmss");
            Date now=new Date();
            String timestamp =edf.format(now);
            // Ensure a valid file name with a specific extension (e.g., .jpg)
            String cleanedImageName = imageName.replaceAll("[^a-zA-Z0-9]", "");
            String filePath = directory.getAbsolutePath() + File.separator + cleanedImageName + timestamp+".jpg";
            Log.d("SaveImage", "File Path: " + filePath);  // Add this line to log the file path


            OutputStream outputStream = new FileOutputStream(filePath);
            outputStream.write(imageData);
            outputStream.close();

            // Return the full file path
            return filePath;
        } catch (Exception e) {
            // Log the exception for debugging
            Log.e("SaveImageException", "Error saving image: " + e.getMessage());
            return null;
        }
    }

    public boolean updateCustomerDetails(String name, String email, String mobile, String address, String city, String state, String pincode, String country, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COULMN_NAME, name);
        cv.put(COULMN_EMAIL, email);
        cv.put(COULMN_MOBILE, mobile);
        cv.put(COULMN_ADDRESS, address);
        cv.put(COULMN_CITY, city);
        cv.put(COULMN_STATE, state);
        cv.put(COULMN_PINCODE, pincode);
        cv.put(COULMN_COUNTRY, country);
        cv.put(COULMN_PASSWORD, password);

        String whereClause = COULMN_EMAIL + "=?";
        String[] whereArgs = new String[]{email};

        int rowsUpdated = db.update(TABLE_NAME, cv, whereClause, whereArgs);
        db.close();

        return rowsUpdated > 0;
    }

    Cursor viewCustomerDetailsforprofile(String userEmail) {
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COULMN_EMAIL + " = ?";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, new String[]{userEmail});
        }
        return cursor;
    }



    public String getCategoryImagePathColumnName() {
        return CATEGORY_IMG_PATH;
    }
    public static String getExternalStorageDirectoryPath() {
        return EXTERNAL_STORAGE_DIRECTORY;
    }


}
