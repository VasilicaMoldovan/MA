package com.example.tryexamorders.repository;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.tryexamorders.database.Contract;
import com.example.tryexamorders.model.Order;

import java.util.ArrayList;
import java.util.List;

public class DBRepository implements Repository{
    static int index = 0;
    private SQLiteDatabase readableDatabase;
    private SQLiteDatabase writableDatabase;

    public  DBRepository(SQLiteDatabase rdb,SQLiteDatabase wdb) {
        this.readableDatabase = rdb;
        this.writableDatabase = wdb;
    }

    @Override
    public List<Order> getAll()
    {
        List<Order> data = new ArrayList<>();

        Cursor cursor = this.readableDatabase.query(Contract.FeedEntry.TABLE_NAME,null,null,null,null,null,null);

        while(cursor.moveToNext())
        {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            String table = cursor.getString(cursor.getColumnIndexOrThrow("tableName"));
            String details = cursor.getString(cursor.getColumnIndexOrThrow("details"));
            String status = cursor.getString(cursor.getColumnIndexOrThrow("status"));
            String time = cursor.getString(cursor.getColumnIndexOrThrow("time"));
            String type = cursor.getString(cursor.getColumnIndexOrThrow("type"));

            Order order = new Order(id, table, details, status, Integer.parseInt(time), type);

            data.add(order);
        }

        cursor.close();
        if (data.size() > 0) {
            index = data.get(data.size() - 1).getId();
        }

        return data;
    }

    @Override
    public List<String> getAll2() {
        List<String> data = new ArrayList<>();

        Cursor cursor = this.readableDatabase.query(Contract.FeedEntry.TABLE_NAME,null,null,null,null,null,null);

        while(cursor.moveToNext())
        {
            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            data.add(name);
        }

        cursor.close();

        return data;
    }

    @Override
    public boolean add(final Order order)
    {
        new Thread(new Runnable() {
            public void run(){
                ContentValues values = new ContentValues();
                index++;
                values.put(Contract.FeedEntry.COLUMN_NAME_ID, index);
                values.put(Contract.FeedEntry.COLUMN_NAME_TableName, order.getTable());
                values.put(Contract.FeedEntry.COLUMN_NAME_Details, order.getDetails());
                values.put(Contract.FeedEntry.COLUMN_NAME_Status, order.getStatus());
                values.put(Contract.FeedEntry.COLUMN_NAME_Time, order.getTime());
                values.put(Contract.FeedEntry.COLUMN_NAME_Type, order.getType());

                writableDatabase.insert(Contract.FeedEntry.TABLE_NAME, null, values);
            }
        }).start();
        return true;
    }

    @Override
    public boolean delete(final Order order) {
        new Thread(new Runnable() {
            public void run() {
                String selection = Contract.FeedEntry.COLUMN_NAME_ID + " LIKE ?";
                String[] selectionArgs = {Integer.toString(order.getId())};

                writableDatabase.delete(Contract.FeedEntry.TABLE_NAME, selection, selectionArgs);
            }
        }).start();
        return true;
    }

    @Override
    public Order update(final int id, final String table, final String details, final String status, final int time, final String type) {
        new Thread(new Runnable() {
            public void run() {
                ContentValues values = new ContentValues();

                values.put(Contract.FeedEntry.COLUMN_NAME_ID, id);
                values.put(Contract.FeedEntry.COLUMN_NAME_TableName, table);
                values.put(Contract.FeedEntry.COLUMN_NAME_Details, details);
                values.put(Contract.FeedEntry.COLUMN_NAME_Status, status);
                values.put(Contract.FeedEntry.COLUMN_NAME_Time, time);
                values.put(Contract.FeedEntry.COLUMN_NAME_Type, type);

                String selection = Contract.FeedEntry.COLUMN_NAME_ID + " LIKE ?";
                String[] selectionArgs = {Integer.toString(id)};

                writableDatabase.update(
                        Contract.FeedEntry.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);
            }
        }).start();
        return null;

    }
}

