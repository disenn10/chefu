package com.example.disen.chefu.Ui;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.disen.chefu.R;
import com.example.disen.chefu.data.RecipeContract;
import com.squareup.picasso.Picasso;

/**
 * Created by disen on 3/27/2018.
 */

public class FavoritesCursorLoaderAdapter extends android.support.v4.widget.CursorAdapter {
    public FavoritesCursorLoaderAdapter(Context context, Cursor c) {
        super(context, c);
    }
    public static class viewHolder{
        ImageView image;
        TextView textView_label;
        public viewHolder(View view){
            image = view.findViewById(R.id.recipe_Image);
            textView_label = view.findViewById(R.id.recipe_label);
        }

    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.recipe_list,parent,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        viewHolder viewHolder = new viewHolder(view);
        int image_col = cursor.getColumnIndex(RecipeContract.recipeEntry.image);
        int label = cursor.getColumnIndex(RecipeContract.recipeEntry.recipe_title);
        String image = cursor.getString(image_col);
        String title = cursor.getString(label);
        viewHolder.textView_label.setText(title);
        Picasso.with(context).load(image).into(viewHolder.image);
    }
}
