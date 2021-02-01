package com.example.exam.repository;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.exam.database.Contract;
import com.example.exam.model.Item;

import java.util.ArrayList;
import java.util.List;

public class DBRepository implements Repository {
    static int index = 0;
    private SQLiteDatabase readableDatabase;
    private SQLiteDatabase writableDatabase;

    public  DBRepository(SQLiteDatabase rdb,SQLiteDatabase wdb) {
        this.readableDatabase = rdb;
        this.writableDatabase = wdb;
        getAll();
    }

    @Override
    public List<Item> getAll() {
        List<Item> data = new ArrayList<>();

        Cursor cursor = this.readableDatabase.query(Contract.FeedEntry.TABLE_NAME,null,null,null,null,null,null);

        while(cursor.moveToNext())
        {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            String model = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            String level = cursor.getString(cursor.getColumnIndexOrThrow("level"));
            String status = cursor.getString(cursor.getColumnIndexOrThrow("status"));
            String from1 = cursor.getString(cursor.getColumnIndexOrThrow("from1"));
            String to1 = cursor.getString(cursor.getColumnIndexOrThrow("to1"));

            Item item = new Item(id, model, Integer.parseInt(level), status, Integer.parseInt(from1), Integer.parseInt(to1));

            data.add(item);
        }

        cursor.close();
        if (data.size() > 0) {
            index = data.get(data.size() - 1).getId();
        }

        return data;
    }

    @Override
    public boolean add(final Item item) {
        new Thread(new Runnable() {
            public void run(){
                ContentValues values = new ContentValues();
                index++;
                values.put(Contract.FeedEntry.COLUMN_NAME_ID, index);
                values.put(Contract.FeedEntry.COLUMN_NAME_Name, item.getName());
                values.put(Contract.FeedEntry.COLUMN_NAME_Level, item.getLevel());
                values.put(Contract.FeedEntry.COLUMN_NAME_Status, item.getStatus());
                values.put(Contract.FeedEntry.COLUMN_NAME_From1, item.getFrom());
                values.put(Contract.FeedEntry.COLUMN_NAME_To, item.getTo());

                writableDatabase.insert(Contract.FeedEntry.TABLE_NAME, null, values);
            }
        }).start();
        return true;
    }

    @Override
    public boolean delete(Item item) {
        return false;
    }

    @Override
    public Item update(final int id, final String name, final int level, final String status, final int from, final int to) {
        new Thread(new Runnable() {
            public void run() {
                ContentValues values = new ContentValues();

                values.put(Contract.FeedEntry.COLUMN_NAME_ID, id);
                values.put(Contract.FeedEntry.COLUMN_NAME_Name, name);
                values.put(Contract.FeedEntry.COLUMN_NAME_Level, level);
                values.put(Contract.FeedEntry.COLUMN_NAME_Status, status);
                values.put(Contract.FeedEntry.COLUMN_NAME_From1, from);
                values.put(Contract.FeedEntry.COLUMN_NAME_To, to);

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

