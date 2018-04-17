package com.example.disen.chefu.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by disen on 3/27/2018.
 */

public class RecipeContract  {

    public static String recipeAuthority = "com.example.disen.chefu";
    public static Uri baseRecipe = Uri.parse("content://"+recipeAuthority);
    public static String path = "recipe";
    public static Uri contentRecipe = Uri.withAppendedPath(baseRecipe,path);

    public static class recipeEntry implements BaseColumns{
        public static String db_name = "recipes";
        public static String recipe_id = BaseColumns._ID;
        public static String image = "image";
        public static String data = "data";
        public static String uri = "uri";
        public static String recipe_title = "title";

    }
}
