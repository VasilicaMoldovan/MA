package com.example.lab2.repository;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.lab2.database.Contract;
import com.example.lab2.model.Flower;

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
    public List<Flower> getAll()
    {
        List<Flower> data = new ArrayList<>();

        Cursor cursor = this.readableDatabase.query(Contract.FeedEntry.TABLE_NAME,null,null,null,null,null,null);

        while(cursor.moveToNext())
        {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            String type = cursor.getString(cursor.getColumnIndexOrThrow("type"));
            String color = cursor.getString(cursor.getColumnIndexOrThrow("color"));
            String number = cursor.getString(cursor.getColumnIndexOrThrow("number"));
            String price = cursor.getString(cursor.getColumnIndexOrThrow("price"));

            Flower flower = new Flower(id, name, type, color, Integer.parseInt(number), Double.parseDouble(price));

            data.add(flower);
        }

        cursor.close();
        if (data.size() > 0) {
            index = data.get(data.size() - 1).getId();
        }

        return data;
    }

    @Override
    public List<String> getAllFlowerNames() {
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
    public boolean addFlower(final Flower flower)
    {
        new Thread(new Runnable() {
            public void run(){
                ContentValues values = new ContentValues();
                index++;
                values.put(Contract.FeedEntry.COLUMN_NAME_ID, index);
                values.put(Contract.FeedEntry.COLUMN_NAME_Name, flower.getName());
                values.put(Contract.FeedEntry.COLUMN_NAME_Type, flower.getType());
                values.put(Contract.FeedEntry.COLUMN_NAME_Color, flower.getColor());
                values.put(Contract.FeedEntry.COLUMN_NAME_Number, flower.getNumber());
                values.put(Contract.FeedEntry.COLUMN_NAME_Price, flower.getPrice());

                writableDatabase.insert(Contract.FeedEntry.TABLE_NAME, null, values);
            }
        }).start();
        return true;
    }

    @Override
    public boolean deleteFlower(final Flower flower) {
        new Thread(new Runnable() {
            public void run() {
                String selection = Contract.FeedEntry.COLUMN_NAME_ID + " LIKE ?";
                String[] selectionArgs = {Integer.toString(flower.getId())};

                writableDatabase.delete(Contract.FeedEntry.TABLE_NAME, selection, selectionArgs);
            }
        }).start();
        return true;
    }

    @Override
    public Flower updateFlower(final int id, final String name, final String type, final String color, final int number, final double price) {
        new Thread(new Runnable() {
            public void run() {
                ContentValues values = new ContentValues();

                values.put(Contract.FeedEntry.COLUMN_NAME_ID, id);
                values.put(Contract.FeedEntry.COLUMN_NAME_Name, name);
                values.put(Contract.FeedEntry.COLUMN_NAME_Type, type);
                values.put(Contract.FeedEntry.COLUMN_NAME_Color, color);
                values.put(Contract.FeedEntry.COLUMN_NAME_Number, number);
                values.put(Contract.FeedEntry.COLUMN_NAME_Price, price);

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

