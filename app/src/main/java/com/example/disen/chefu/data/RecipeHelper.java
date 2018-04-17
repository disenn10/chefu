package com.example.disen.chefu.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by disen on 3/27/2018.
 */

public class RecipeHelper extends SQLiteOpenHelper {
    public static String db_name = RecipeContract.recipeEntry.db_name;
    public static int version = 6;
    public RecipeHelper(Context context, SQLiteDatabase.CursorFactory factory) {
        super(context, db_name, factory, version);
    }

    public static String CreateEntries = "CREATE TABLE " + RecipeContract.recipeEntry.db_name+"("
            +RecipeContract.recipeEntry.recipe_id+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
            +RecipeContract.recipeEntry.recipe_title+ " TEXT, "
            +RecipeContract.recipeEntry.image+ " TEXT, "
            +RecipeContract.recipeEntry.uri+ " TEXT, "
            +RecipeContract.recipeEntry.data+ " TEXT) ";
    public static String deleteTable = "DROP TABLE IF EXISTS "+ db_name;

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CreateEntries);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(deleteTable);
        onCreate(sqLiteDatabase);
    }
}
