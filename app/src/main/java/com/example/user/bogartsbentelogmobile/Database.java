package com.example.user.bogartsbentelogmobile;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.example.user.bogartsbentelogmobile.Model.Order;
import com.google.firebase.firestore.FieldValue;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by user on 2/13/2019.
 */

public class Database extends SQLiteAssetHelper {

    private static final String Database_Name = "BogartsDB.db";
    private static final int DatabaseVersion = 1;


    public Database(Context context) {
        super(context, Database_Name, null,  DatabaseVersion);
    }

//    public List<Order> getCart(){
//        SQLiteDatabase db = getReadableDatabase();
//        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
//
//        String [] sqlSelect = {"ProductID", "productName", "quantity", "price"};
//        String table = "OrderDetail";
//
//        qb.setTables(table);
//
//        Cursor cursor = qb.query(db,sqlSelect,null,null,null,null,null);
//
//        final List<Order> res = new ArrayList<>();
//
//        if (cursor.moveToFirst()){
//            do {
//                res.add(new Order(cursor.getString(cursor.getColumnIndex("ProductID")),
//                        cursor.getString(cursor.getColumnIndex("productName")),
//                        cursor.getString(cursor.getColumnIndex("quantity")),
//                        cursor.getString(cursor.getColumnIndex("price"))
//                        ));
//            }while (cursor.moveToNext());
//        }
//        return res;
//    }

    public void addOrderToCart(Order order){
        SQLiteDatabase db = getWritableDatabase();

        Map<String, Object> values = new HashMap<>();
        values.put("ProductID", order.getProductID());
        values.put("productName", order.getProductName());
        values.put("quantity", order.getQuantity());
        values.put("price", order.getPrice());
        values.put("latestUpdateTimestamp", FieldValue.serverTimestamp());


//        long newRowId = db.insert("OrderDetail",null,values);

    }

    public void cleanOrderFromCart(){
        SQLiteDatabase db = getReadableDatabase();
        String q = String.format("DELETE FROM OrderDetail ");
        db.execSQL(q);
    }
}
