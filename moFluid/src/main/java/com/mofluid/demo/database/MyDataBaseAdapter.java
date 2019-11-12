package com.mofluid.magento2.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.ebizon.fluid.Utils.CartSynckManager;
import com.ebizon.fluid.Utils.ConvertValues;
import com.ebizon.fluid.Utils.MySharedPreferences;
import com.ebizon.fluid.model.ShippingMethodItem;
import com.ebizon.fluid.model.ShoppingCart;
import com.ebizon.fluid.model.ShoppingCartItem;
import com.ebizon.fluid.model.ShoppingItem;
import com.ebizon.fluid.model.SimpleShoppingItem;
import com.ebizon.fluid.model.UserManager;
import com.ebizon.fluid.model.UserProfileItem;
import com.mofluid.magento2.EncodeString;
import com.mofluid.magento2.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by ebizon on 24/11/15.
 */
public class MyDataBaseAdapter extends SQLiteOpenHelper {
    // Database Name
    private static final String DATABASE_NAME = "cart_list_db";

    // Current version of database
    private static final int DATABASE_VERSION = 1;

    /* User profile table*/
    //table name
    private static final String TABLE_USER_PROFILE = "user_profile_table";
    private static final String KEY_USER_NAME = "username";//this is email id
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_FIRST_NAME = "firstname";
    private static final String KEY_LAST_NAME = "lastname";
    private static final String KEY_USER_ID = "id";//this id provide by server
    private static final String KEY_LOGIN_STATUS = "login_status";

    // cart

    private static final String TABLE_USER_CART = "user_cart_table";
    private static final String KEY_PRODUCT_ID = "product_id";//this is email id
    private static final String KEY_PRODUCT_SKU = "product_sku";
    private static final String KEY_PRODUCT_NAME = "poduct_name";
    private static final String KEY_PRODUCT_PRICE = "product_price";
    private static final String KEY_PRODUCT_SPCLPRICE = "product_specialprice";
    private static final String KEY_PRODUCT_QUANTITY = "product_quantity";//this id provide by server
    private static final String KEY_PRODUCT_STOCKQTY = "stock_quantity";
    private static final String KEY_PRODUCT_IMAGE = "product_image";
    private static final String KEY_PRODUCT_THUMBNAIL = "product_thumbnail";
    private static final String KEY_PARENT_ID = "product_parent_id";
    private static final String KEY_PRODUCT_TYPE = "product_type";
    private static final String KEY_PRODUCT_USER = "product_user";
    private static final String KEY_PRODUCT_IS_IN_STOCK = "product_is_in_stock";


    //create table
    private static final String CREATE_TABLE_USER = "CREATE TABLE "
            + TABLE_USER_PROFILE + "("
            + KEY_USER_NAME + " TEXT  PRIMARY KEY,"
            + KEY_PASSWORD + " TEXT,"
            + KEY_FIRST_NAME + " TEXT,"
            + KEY_LAST_NAME + " TEXT,"
            + KEY_USER_ID + " TEXT,"
            + KEY_LOGIN_STATUS + " TEXT );";


    private static final String CREATE_TABLE_CART = "CREATE TABLE "
            + TABLE_USER_CART + " ( "
            + KEY_PRODUCT_ID + " TEXT PRIMARY KEY,"
            + KEY_PRODUCT_SKU + " TEXT,"
            + KEY_PRODUCT_NAME + " TEXT,"
            + KEY_PRODUCT_PRICE + " TEXT,"
            + KEY_PRODUCT_SPCLPRICE + " TEXT,"
            + KEY_PRODUCT_QUANTITY + " TEXT,"
            + KEY_PRODUCT_STOCKQTY + " TEXT,"
            + KEY_PRODUCT_TYPE + " TEXT,"
            + KEY_PRODUCT_IS_IN_STOCK + " TEXT,"
            + KEY_PRODUCT_THUMBNAIL + " TEXT,"
            + KEY_PARENT_ID + " TEXT,"
            + KEY_PRODUCT_USER + " TEXT,"
            + KEY_PRODUCT_IMAGE + " TEXT );";

    private static final String TAG = "MyDataBaseAdapter";
    private static ShippingMethodItem shippingMethod = null;
    private static String coupon = null;
    private static float couponValue = 0.0f;
    private Context context = null;

    public MyDataBaseAdapter(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        TAG.getClass().getSimpleName();
    }

    public MyDataBaseAdapter(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    public static void SetCoupon(String coupon, float couponValue) {
        MyDataBaseAdapter.coupon = coupon;
        MyDataBaseAdapter.couponValue = couponValue;
    }

    public static String getCoupon() {
        return MyDataBaseAdapter.coupon;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_USER);
        Log.d("CARTDB","create query is : "+ CREATE_TABLE_CART);
        sqLiteDatabase.execSQL(CREATE_TABLE_CART);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_USER);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_CART);
        onCreate(sqLiteDatabase);

    }

    public void SetShippingMethod(ShippingMethodItem shipMethod) {
        shippingMethod = shipMethod;
    }

    public ShippingMethodItem getShippingMethod() {
        return shippingMethod;
    }

    public double getTotalPrice(){
        double totalPrice = ShoppingCart.getInstance().getSubTotal();

        if (shippingMethod != null){
            totalPrice += shippingMethod.getPrice();
        }

        if (coupon != null){
            totalPrice += couponValue;
        }
        return totalPrice;
    }

    /*insert data into user profile table*/
    public long inserUserProfiletData(UserProfileItem item) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Creating content values
        Log.d(TAG, "inserUserProfiletData() called with: " + "primary key  = [" +  item.getUsername() + "]");
        ContentValues values = new ContentValues();

        values.put(KEY_FIRST_NAME, item.getFirstname());
        values.put(KEY_LAST_NAME, item.getLastname());
        values.put(KEY_USER_ID, item.getId());
        values.put(KEY_LOGIN_STATUS, item.getLogin_status());
        values.put(KEY_PASSWORD, item.getPassword());
        values.put(KEY_USER_NAME, item.getUsername());
        MySharedPreferences ob=MySharedPreferences.getInstance();
        ob.set(ob.CUSTOMER_ID,item.getId());

        return db.insert(TABLE_USER_PROFILE, null, values);
    }

    public long insertItemtoCart(ShoppingCartItem item) {
        if(item.getCount()==0)
            return 0L;
        MyDataBaseAdapter dbadapter = new MyDataBaseAdapter(MainActivity.INSTANCE);
        String userID;
        UserProfileItem currentUser = UserManager.getInstance().getUser();
        if (currentUser == null)
            userID = "0";
        else
            userID = currentUser.getId();

        boolean exists = checkCartforSimgleItem(item.getShoppingItem().getId());
        Log.d("CartDB","Already exists item in cart, need to update is" +exists);
        Log.d(TAG, "inserUserProfiletData() called with: " + "primary key  = [" + item.getShoppingItem().getId() + "]");
            SQLiteDatabase db = dbadapter.getWritableDatabase();
            ShoppingItem shopitem = item.getShoppingItem();
            ContentValues values = new ContentValues();
            values.put(KEY_PRODUCT_ID, shopitem.getId());
            values.put(KEY_PRODUCT_SKU, shopitem.getSku());
            values.put(KEY_PRODUCT_NAME, shopitem.getName());
            values.put(KEY_PRODUCT_PRICE, shopitem.getPriceStr());
            values.put(KEY_PRODUCT_SPCLPRICE, shopitem.getSpecialPriceStr());
            values.put(KEY_PRODUCT_QUANTITY, item.getCount());
            values.put(KEY_PRODUCT_STOCKQTY, shopitem.getStockQuantity());
            values.put(KEY_PRODUCT_IMAGE, shopitem.getImage());
            values.put(KEY_PRODUCT_THUMBNAIL, shopitem.getThumbnail());
            values.put(KEY_PARENT_ID, shopitem.getParentID());
            values.put(KEY_PRODUCT_TYPE, shopitem.getType());
            values.put(KEY_PRODUCT_IS_IN_STOCK, shopitem.getInStock());
            values.put(KEY_PRODUCT_USER, userID);
            try {
                JSONObject obj = new JSONObject();
                obj.put("qty", item.getCount());
                obj.put("sku",shopitem.getSku());
                obj.put("quote_id",1);
                CartSynckManager.getInstance().insertItemtoCart(Integer.parseInt(userID), EncodeString.encodeStrBase64Bit(obj.toString()));
            }catch (JSONException e){}
        if(!exists) {
            // Creating content values
            values.put(KEY_PRODUCT_ID, shopitem.getId());
            values.put(KEY_PRODUCT_SKU, shopitem.getSku());
            values.put(KEY_PRODUCT_NAME, shopitem.getName());
            values.put(KEY_PRODUCT_PRICE, shopitem.getPriceStr());
            values.put(KEY_PRODUCT_SPCLPRICE, shopitem.getSpecialPriceStr());
            values.put(KEY_PRODUCT_QUANTITY, item.getCount());
            values.put(KEY_PRODUCT_STOCKQTY, shopitem.getStockQuantity());
            values.put(KEY_PRODUCT_IMAGE, shopitem.getImage());
            values.put(KEY_PARENT_ID, shopitem.getParentID());
            values.put(KEY_PRODUCT_THUMBNAIL, shopitem.getThumbnail());
            values.put(KEY_PRODUCT_TYPE, shopitem.getType());
            values.put(KEY_PRODUCT_IS_IN_STOCK, shopitem.getInStock());
            values.put(KEY_PRODUCT_USER, userID);
            return db.insert(TABLE_USER_CART, null, values);
        }
        else
        {
            // update quantity only of this product
            Log.d("CartDB","Updating quantity of"+ item.getShoppingItem().getName()+" with " + item.getCount());
            values.put(KEY_PRODUCT_QUANTITY, item.getCount());
            //CartSynckManager.getInstance().updateCartItem(Integer.parseInt(userID),Integer.parseInt(shopitem.getId()),item.getCount());
            return db.update(TABLE_USER_CART,values,KEY_PRODUCT_ID +" = ?",
                    new String[]{item.getShoppingItem().getId()});
        }
    }



    public int deleteSingleItemFromCart(ShoppingCartItem item){
        String userID;
        UserProfileItem currentUser = UserManager.getInstance().getUser();
        if (currentUser == null)
            userID = "0";
        else
            userID = currentUser.getId();
        SQLiteDatabase db = this.getWritableDatabase();
        int res = db.delete(TABLE_USER_CART,KEY_PRODUCT_ID +" = ?", new String[]{item.getShoppingItem().getId()});
        db.close();
       CartSynckManager.getInstance().removeItemFromCart(Integer.parseInt(userID),Integer.parseInt(item.getShoppingItem().getId()));
        return res;
    }
    //created by saddam
   public ArrayList<ShoppingCartItem> getCartItems(){
       String userID;
       UserProfileItem currentUser = UserManager.getInstance().getUser();
       if (currentUser == null)
           userID = "0";
       else
           userID = currentUser.getId();
       ArrayList<ShoppingCartItem> res=new ArrayList<ShoppingCartItem>();
       ShoppingCart.getInstance().clearCart();
       String selectQuery = "SELECT  * FROM " + TABLE_USER_CART;
       Log.d(TAG, selectQuery);

       SQLiteDatabase db = this.getReadableDatabase();
       Cursor c = db.rawQuery(selectQuery, null);
       // looping through all rows and adding to list
       if (c.moveToFirst()) {

           do {
               String id = c.getString(c.getColumnIndex(KEY_PRODUCT_ID));
               String name = c.getString(c.getColumnIndex(KEY_PRODUCT_NAME));
               String sku = c.getString(c.getColumnIndex(KEY_PRODUCT_SKU));
               String image = c.getString(c.getColumnIndex(KEY_PRODUCT_IMAGE));
               String price = c.getString(c.getColumnIndex(KEY_PRODUCT_PRICE));
               String spclprice = c.getString(c.getColumnIndex(KEY_PRODUCT_SPCLPRICE));
               String is_in_stock = c.getString(c.getColumnIndex(KEY_PRODUCT_IS_IN_STOCK));
               String stockqty = c.getString(c.getColumnIndex(KEY_PRODUCT_STOCKQTY));
               String type = c.getString(c.getColumnIndex(KEY_PRODUCT_TYPE));
               String thumbnail = c.getString(c.getColumnIndex(KEY_PRODUCT_THUMBNAIL));
               String parentID = c.getString(c.getColumnIndex(KEY_PARENT_ID));
               String productCount = c.getString(c.getColumnIndex(KEY_PRODUCT_QUANTITY));
               ShoppingItem simpleItem = new ShoppingItem(id, name, sku, image, price,spclprice, is_in_stock, stockqty, type, image, null, null);
               ShoppingCartItem shopItem = new ShoppingCartItem(simpleItem,Integer.parseInt(productCount));
               ShoppingCart.getInstance().addItem(shopItem);
               res.add(shopItem);
           } while (c.moveToNext());
       }

       db.close();
      // CartSynckManager.getInstance().getCartItems(Integer.parseInt(userID));
       Log.d("Saddam","items count in cart="+res.size());
       return res;

   }
    public HashMap<String,ShoppingCartItem> getallCartItems()
    {
        HashMap<String,ShoppingCartItem> cartList = new HashMap<>();

        String selectQuery = "SELECT  * FROM " + TABLE_USER_CART;
        Log.d(TAG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        Log.d(TAG, "getUserProfile() called with:number of record  " + c.getCount() + "");


        // looping through all rows and adding to list
        if (c.moveToFirst()) {

            do {
                String id = c.getString(c.getColumnIndex(KEY_PRODUCT_ID));
                String name = c.getString(c.getColumnIndex(KEY_PRODUCT_NAME));
                String sku = c.getString(c.getColumnIndex(KEY_PRODUCT_SKU));
                String image = c.getString(c.getColumnIndex(KEY_PRODUCT_IMAGE));
                String price = c.getString(c.getColumnIndex(KEY_PRODUCT_PRICE));
                String spclprice = c.getString(c.getColumnIndex(KEY_PRODUCT_SPCLPRICE));
                String is_in_stock = c.getString(c.getColumnIndex(KEY_PRODUCT_IS_IN_STOCK));
                String stockqty = c.getString(c.getColumnIndex(KEY_PRODUCT_STOCKQTY));
                String type = c.getString(c.getColumnIndex(KEY_PRODUCT_TYPE));
                String thumbnail = c.getString(c.getColumnIndex(KEY_PRODUCT_THUMBNAIL));
                String parentID = c.getString(c.getColumnIndex(KEY_PARENT_ID));

                SimpleShoppingItem item = new SimpleShoppingItem(parentID,name,sku,image,price,spclprice,is_in_stock,stockqty,type,thumbnail);
                ShoppingCartItem singleItem = new ShoppingCartItem(item,c.getShort(c.getColumnIndex(KEY_PRODUCT_QUANTITY)));
                cartList.put(singleItem.getShoppingItem().getParentID(),singleItem);
            } while (c.moveToNext());
        }

        db.close();
        return cartList;
    }

    public int clearCart()
    {   String userID;
        UserProfileItem currentUser = UserManager.getInstance().getUser();
        if (currentUser == null)
            userID = "0";
        else
            userID = currentUser.getId();
        SQLiteDatabase db = this.getWritableDatabase();
         int row_affected = db.delete(TABLE_USER_CART,null,null);
        db.close();
        //CartSynckManager.getInstance().clearCart(Integer.parseInt(userID));
        return row_affected;
    }


    public int updateUserProfileData(UserProfileItem item) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Creating content values
        ContentValues values = new ContentValues();
        values.put(KEY_FIRST_NAME, item.getFirstname());
        values.put(KEY_LAST_NAME, item.getLastname());
        values.put(KEY_USER_ID, item.getId());
        values.put(KEY_LOGIN_STATUS, item.getLogin_status());
        values.put(KEY_PASSWORD, item.getPassword());
        //values.put(KEY_USER_NAME, item.getUsername());
        // update row in students table base on students.is value
        MySharedPreferences ob=MySharedPreferences.getInstance();
        ob.set(ob.CUSTOMER_ID,item.getId());
        return db.update(TABLE_USER_PROFILE, values, KEY_USER_NAME + " = ?",
                new String[]{String.valueOf(item.getUsername())});
    }

    public int updateOnlyUserProfileStatus(UserProfileItem item) {
       if(!item.getLogin_status().equals("0"))
       MySharedPreferences.getInstance().set(MySharedPreferences.CUSTOMER_ID,item.getId()+"");
       else
           MySharedPreferences.getInstance().clear();
        SQLiteDatabase db = this.getWritableDatabase();

        // Creating content values
        ContentValues values = new ContentValues();
        values.put(KEY_LOGIN_STATUS, item.getLogin_status());
        //values.put(KEY_USER_NAME, item.getUsername());
        // update row in students table base on students.is value
        Log.d(TAG, "updateOnlyUserProfileStatus() called with: " + "status = [" + item.getLogin_status() + "]");
        Log.d(TAG, "updateOnlyUserProfileStatus() called with: " + "user name = [" + item.getUsername() + "]");
        return db.update(TABLE_USER_PROFILE, values, KEY_USER_NAME + " = ?",
                new String[] { String.valueOf(item.getUsername()) });
    }
    public int updateOnlyUserProfilePssord(UserProfileItem item) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Creating content values
        ContentValues values = new ContentValues();
        values.put(KEY_LOGIN_STATUS, item.getLogin_status());
        //values.put(KEY_USER_NAME, item.getUsername());
        // update row in students table base on students.is value
        Log.d(TAG, "updateOnlyUserProfilePssord() called with: " + "status = [" + item.getLogin_status() + "]");
        Log.d(TAG, "updateOnlyUserProfilePssord() called with: " + "user password = [" + item.getPassword() + "]");
        return db.update(TABLE_USER_PROFILE, values, KEY_PASSWORD + " = ?",
                new String[] { String.valueOf(item.getPassword()) });
    }
    public ArrayList<UserProfileItem> getUserProfile() {
        ArrayList<UserProfileItem> userProfile = new ArrayList<UserProfileItem>();

        String selectQuery = "SELECT  * FROM " + TABLE_USER_PROFILE;
        Log.d(TAG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        Log.d(TAG, "getUserProfile() called with:number of record  " +c.getCount()+ "");


        // looping through all rows and adding to list
        if (c.moveToFirst()) {

            Log.d(TAG, "getUserProfile() called with:number of record  " +c.getString(0)+ "");
            Log.d(TAG, "getUserProfile() called with:number of record  " +c.getString(1)+ "");
            Log.d(TAG, "getUserProfile() called with:number of record  " +c.getString(2)+ "");
            Log.d(TAG, "getUserProfile() called with:number of record  " +c.getString(3)+ "");
            Log.d(TAG, "getUserProfile() called with:number of record  " +c.getString(4)+ "");
            Log.d(TAG, "getUserProfile() called with:number of record  " +c.getString(5)+ "");
            do {

                //String firstname, String id, String lastname, String login_status, String password, String username
                UserProfileItem item = new UserProfileItem(c.getString(c.getColumnIndex(KEY_FIRST_NAME)),c.getString(c.getColumnIndex(KEY_USER_ID)),c.getString(c.getColumnIndex(KEY_LAST_NAME)),c.getString(c.getColumnIndex(KEY_LOGIN_STATUS)),c.getString(c.getColumnIndex(KEY_PASSWORD)),c.getString(c.getColumnIndex(KEY_USER_NAME)));
                userProfile.add(item);
            } while (c.moveToNext());
        }

        db.close();
        return userProfile;
    }

    public boolean checkCartforSimgleItem(String itemId) {
        ShoppingCartItem item;
        MyDataBaseAdapter dba = new MyDataBaseAdapter(MainActivity.INSTANCE);

        String selectQuery = "SELECT  * FROM " + TABLE_USER_CART + " WHERE " + KEY_PRODUCT_ID + " = ? ";
        try {
            SQLiteDatabase db = dba.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, new String[]{itemId});
            Log.d(TAG, "getUserProfile() called with:number of record  " + c.getCount() + "");
            if (c.getCount() != 0) { // already exists need to update
                db.close();
                return true;
            } else {
                db.close();
                return false;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }


    public boolean isUserAvailable(String id) {
        SQLiteDatabase db = this.getReadableDatabase();

        // SELECT * FROM students WHERE id = ?;
        String selectQuery = "SELECT  * FROM " + TABLE_USER_PROFILE + " WHERE " + KEY_USER_NAME + " = ? ";
        Log.d(TAG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, new String[]{id});



        if ( c.getCount()>0) {
            db.close();
            return true;
        }
        db.close();
        return false;
    }

}
