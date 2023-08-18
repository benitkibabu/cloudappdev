package com.cloudappdev.ben.virtualkitchen.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.cloudappdev.ben.virtualkitchen.models.User;

/**
 * Created by Benit Kibabu on 26/05/2017.
 */

public class SQLiteHandler extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "login_db";
    private final String TB_NAME = "user";

    private final String ID = "id";
    private final String NAME = "name";
    private final String USERNAME = "username";
    private final String IMAGE_URL = "image_url";


    public SQLiteHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create = "CREATE TABLE " + TB_NAME + " ("
                +ID + " INTEGER PRIMARY KEY, "
                +NAME + " TEXT, "
                +USERNAME + " TEXT, "
                +IMAGE_URL + " TEXT )";

        db.execSQL(create);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TB_NAME);
        onCreate(db);
    }

    public long creatUser(User user){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ID, user.getId());
        values.put(NAME, user.getName());
        values.put(USERNAME, user.getUsername());
        values.put(IMAGE_URL, user.getImage_url());

        long id = db.insert(TB_NAME, null, values);
        db.close();
        return id;
    }
    public int deleteUser(){
        SQLiteDatabase db = this.getWritableDatabase();
        int i = db.delete(TB_NAME, null, null);
        db.close();
        return i;
    }

    public User getUser(){
        SQLiteDatabase db = this.getReadableDatabase();
        User u = null;
        Cursor c = db.rawQuery("SELECT * FROM " + TB_NAME, null);

        if(c!=null && c.getCount() > 0 && c.moveToFirst()){
            u = new User();
            u.setId(c.getInt(c.getColumnIndex(ID)));
            u.setName(c.getString(c.getColumnIndex(NAME)));
            u.setUsername(c.getString(c.getColumnIndex(USERNAME)));
            u.setImage_url(c.getString(c.getColumnIndex(IMAGE_URL)));

            c.close();
        }

        db.close();
        return u;
    }
}
