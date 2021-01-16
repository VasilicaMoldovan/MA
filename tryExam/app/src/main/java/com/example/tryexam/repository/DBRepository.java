package com.example.tryexam.repository;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.tryexam.database.Contract;
import com.example.tryexam.model.Recipe;

import java.util.ArrayList;
import java.util.List;

public class DBRepository implements Repository {
    static int index = 0;
    private SQLiteDatabase readableDatabase;
    private SQLiteDatabase writableDatabase;

    public  DBRepository(SQLiteDatabase rdb,SQLiteDatabase wdb) {
        this.readableDatabase = rdb;
        this.writableDatabase = wdb;
    }

    @Override
    public List<Recipe> getAllRecipes() {
        List<Recipe> data = new ArrayList<>();

        Cursor cursor = this.readableDatabase.query(Contract.FeedEntry.TABLE_NAME,null,null,null,null,null,null);

        while(cursor.moveToNext())
        {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            String details = cursor.getString(cursor.getColumnIndexOrThrow("details"));
            String time = cursor.getString(cursor.getColumnIndexOrThrow("time"));
            String type = cursor.getString(cursor.getColumnIndexOrThrow("type"));
            String rating = cursor.getString(cursor.getColumnIndexOrThrow("rating"));

            Recipe recipe = new Recipe(id, name, details, Integer.parseInt(time), type, Integer.parseInt(rating));

            data.add(recipe);
        }

        cursor.close();
        if (data.size() > 0) {
            index = data.get(data.size() - 1).getId();
        }

        return data;
    }

    @Override
    public List<String> getAll() {
        List<String> data = new ArrayList<>();

        Cursor cursor = this.readableDatabase.query(Contract.FeedEntry.TABLE_NAME,null,null,null,null,null,null);

        while(cursor.moveToNext())
        {
            String name = cursor.getString(cursor.getColumnIndexOrThrow("type"));
            data.add(name);
        }

        cursor.close();

        return data;
    }

    @Override
    public boolean addRecipe(final Recipe recipe) {
        new Thread(new Runnable() {
            public void run(){
                ContentValues values = new ContentValues();
                index++;
                values.put(Contract.FeedEntry.COLUMN_NAME_ID, index);
                values.put(Contract.FeedEntry.COLUMN_NAME_Name, recipe.getName());
                values.put(Contract.FeedEntry.COLUMN_NAME_Details, recipe.getDetails());
                values.put(Contract.FeedEntry.COLUMN_NAME_Time, recipe.getTime());
                values.put(Contract.FeedEntry.COLUMN_NAME_Type, recipe.getType());
                values.put(Contract.FeedEntry.COLUMN_NAME_Rating, recipe.getRating());

                writableDatabase.insert(Contract.FeedEntry.TABLE_NAME, null, values);
            }
        }).start();
        return true;
    }

    @Override
    public boolean deleteRecipe(final Recipe recipe) {
        new Thread(new Runnable() {
            public void run() {
                String selection = Contract.FeedEntry.COLUMN_NAME_ID + " LIKE ?";
                String[] selectionArgs = {Integer.toString(recipe.getId())};

                writableDatabase.delete(Contract.FeedEntry.TABLE_NAME, selection, selectionArgs);
            }
        }).start();
        return true;
    }

    @Override
    public Recipe updateRecipe(final int id, final String name, final String details, final int time, final String type, final int rating) {
        new Thread(new Runnable() {
            public void run() {
                ContentValues values = new ContentValues();

                values.put(Contract.FeedEntry.COLUMN_NAME_ID, id);
                values.put(Contract.FeedEntry.COLUMN_NAME_Name, name);
                values.put(Contract.FeedEntry.COLUMN_NAME_Details, type);
                values.put(Contract.FeedEntry.COLUMN_NAME_Time, time);
                values.put(Contract.FeedEntry.COLUMN_NAME_Type, type);
                values.put(Contract.FeedEntry.COLUMN_NAME_Rating, rating);

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
