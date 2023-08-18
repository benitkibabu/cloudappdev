package com.cloudappdev.ben.virtualkitchen.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.cloudappdev.ben.virtualkitchen.models.MyRecipes;

import java.util.ArrayList;
import java.util.List;

public class FavouriteSQLiteHandler extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "fav_db";
    private final String TB_NAME = "favourite";


    private final String id = "id";
    private final String uri = "uri";
    private final String label = "label";
    private final String image = "image";
    private final String source = "source";
    private final String url = "url";
    private final String shareAs = "shareAs";
    private final String yield = "yield";
    private final String dietLabels = "dietLabels";
    private final String healthLabels = "healthLabels";
    private final String cautions = "cautions";
    private final String ingredientLines = "ingredientLines";
    private final String calories = "calories";
    private final String totalWeight = "totalWeight";
    private final String user_id = "userid";
    private final String ingredientCount = "ingredientCount";

    public FavouriteSQLiteHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create = "CREATE TABLE " + TB_NAME + " ("
                +id + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                +uri + " TEXT, "
                +label + " TEXT, "
                +image + " TEXT, "
                +source + " TEXT, "
                +shareAs + " TEXT, "
                +yield + " DOUBLE, "
                +dietLabels + " TEXT, "
                +healthLabels + " TEXT, "
                +cautions + " TEXT, "
                +ingredientLines + " TEXT, "
                +calories + " DOUBLE, "
                +totalWeight + " DOUBLE, "
                +user_id + " INTEGER, "
                +ingredientCount + " INTEGER, "
                +url + " TEXT )";

        db.execSQL(create);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TB_NAME);
        onCreate(db);
    }

    public long addItem(MyRecipes r){
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put(uri, r.getUri());
            values.put(label, r.getLabel());
            values.put(image, r.getImage());
            values.put(source, r.getSource());
            values.put(shareAs, r.getShareAs());
            values.put(yield, r.getYield());
            values.put(dietLabels, r.getDietLabels());
            values.put(healthLabels, r.getHealthLabels());
            values.put(cautions, r.getCautions());
            values.put(ingredientLines, r.getIngredientLines());
            values.put(calories, r.getCalories());
            values.put(totalWeight, r.getTotalWeight());
            values.put(user_id, r.getUser_id());
            values.put(ingredientCount, r.getIngredientCount());
            values.put(url, r.getUrl());

            long id = db.insert(TB_NAME, null, values);
            db.close();
            return id;
        }catch (Exception ex){
            return -1;
        }
    }

    public List<MyRecipes> getMyFavourites(){

        List<MyRecipes> items = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TB_NAME, null);

        while(c!=null && c.getCount() > 0 && c.moveToNext()){
            MyRecipes mr = new MyRecipes();
            mr.setId(c.getInt(c.getColumnIndex(id)));
            mr.setUser_id(c.getInt(c.getColumnIndex(user_id)));
            mr.setIngredientCount(c.getInt(c.getColumnIndex(ingredientCount)));

            mr.setCalories(c.getDouble(c.getColumnIndex(calories)));
            mr.setYield(c.getDouble(c.getColumnIndex(yield)));
            mr.setTotalWeight(c.getDouble(c.getColumnIndex(totalWeight)));

            mr.setUri(c.getString(c.getColumnIndex(uri)));
            mr.setLabel(c.getString(c.getColumnIndex(label)));
            mr.setImage(c.getString(c.getColumnIndex(image)));
            mr.setSource(c.getString(c.getColumnIndex(source)));
            mr.setShareAs(c.getString(c.getColumnIndex(shareAs)));
            mr.setDietLabels(c.getString(c.getColumnIndex(dietLabels)));
            mr.setHealthLabels(c.getString(c.getColumnIndex(healthLabels)));
            mr.setCautions(c.getString(c.getColumnIndex(cautions)));
            mr.setIngredientLines(c.getString(c.getColumnIndex(ingredientLines)));
            mr.setUrl(c.getString(c.getColumnIndex(url)));
            items.add(mr);
        }

        if (c != null) {
            c.close();
        }

        db.close();
        return items;
    }
}
