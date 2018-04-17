package com.example.disen.chefu.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.net.URI;

/**
 * Created by disen on 3/27/2018.
 */

public class RecipeProvider extends ContentProvider {
    UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    final int recipe = 100;
    final int recipe_id = 101;
    SQLiteDatabase sqLiteDatabase;
    RecipeHelper recipeHelper;
    @Override
    public boolean onCreate() {
        recipeHelper = new RecipeHelper(getContext(),null);
        uriMatcher.addURI(RecipeContract.recipeAuthority,RecipeContract.path,recipe);
        uriMatcher.addURI(RecipeContract.recipeAuthority,RecipeContract.path+"#",recipe_id);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        int recipe_path = uriMatcher.match(uri);
        sqLiteDatabase = recipeHelper.getReadableDatabase();
        Cursor cursor;
        switch (recipe_path){
            case 100:
                cursor = sqLiteDatabase.query(RecipeContract.recipeEntry.db_name,strings,s,strings1,null,null,null);
                break;
            case 101:
                s = RecipeContract.recipeEntry.recipe_id+"=?";
                strings1 = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = sqLiteDatabase.query(RecipeContract.recipeEntry.db_name,strings,s,strings1,null,null,null);
                break;
            default:
                throw new IllegalArgumentException("Can't perform request");
        }
        cursor.setNotificationUri(getContext().getContentResolver(),uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        sqLiteDatabase = recipeHelper.getWritableDatabase();
        int path = uriMatcher.match(uri);
        Long ID = null;
        switch (path){
            case recipe:
                ID = sqLiteDatabase.insert(RecipeContract.recipeEntry.db_name,null,contentValues);
                break;
            default:
                throw new IllegalArgumentException("Couldn't perform insert");
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return ContentUris.withAppendedId(uri,ID);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        int path = uriMatcher.match(uri);
        int deletion_id = 0;
        sqLiteDatabase = recipeHelper.getWritableDatabase();
        switch (path){
            case recipe:
                deletion_id = sqLiteDatabase.delete(RecipeContract.recipeEntry.db_name,s,strings);
                break;
            case recipe_id:
                s = RecipeContract.recipeEntry.recipe_id+"=?";
                strings = new String[]{String.valueOf(ContentUris.parseId(uri))};
                deletion_id = sqLiteDatabase.delete(RecipeContract.recipeEntry.db_name,s,strings);
                break;
            default:
                throw new IllegalArgumentException("Couldn't perform deletion");
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return deletion_id;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
