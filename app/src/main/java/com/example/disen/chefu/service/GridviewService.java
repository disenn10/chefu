package com.example.disen.chefu.service;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.disen.chefu.DetailActivity;
import com.example.disen.chefu.R;
import com.example.disen.chefu.data.RecipeContract;
import com.squareup.picasso.Picasso;

import java.io.IOException;

/**
 * Created by disen on 3/28/2018.
 */

public class GridviewService extends RemoteViewsService{

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new GridRemoteViewsFactory(this.getApplicationContext());
    }
}

class GridRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    Context mContext;
    Cursor mCursor;

    public GridRemoteViewsFactory(Context context){
        this.mContext = context;
    }
    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        if(mCursor!=null)mCursor.close();
        String [] projection = new String[]{RecipeContract.recipeEntry.recipe_id,
                RecipeContract.recipeEntry.recipe_title,
                RecipeContract.recipeEntry.uri,
                RecipeContract.recipeEntry.image};
        mCursor = mContext.getContentResolver().query(RecipeContract.contentRecipe,projection,null,null,null);
    }

    @Override
    public void onDestroy() {
        mCursor.close();
    }

    @Override
    public int getCount() {
        if(mCursor == null){
            return 0;
        }
        return mCursor.getCount();
    }

    @Override
    public RemoteViews getViewAt(int i) {
        if(mCursor == null || mCursor.getCount() == 0){
            return null;
        }
        Bitmap bitmapImage = null;
        mCursor.moveToPosition(i);
        int imageIndex = mCursor.getColumnIndex(RecipeContract.recipeEntry.image);
        int titleIndex = mCursor.getColumnIndex(RecipeContract.recipeEntry.recipe_title);
        String image = mCursor.getString(imageIndex);
        String title = mCursor.getString(titleIndex);
        int uriColumnName = mCursor.getColumnIndex(RecipeContract.recipeEntry.uri);
        String uri = mCursor.getString(uriColumnName);
        String activity = "widget";
        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.widget_list);
        remoteViews.setTextViewText(R.id.text_widget,title);
        try {
            bitmapImage = Picasso.with(mContext).load(image).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        remoteViews.setImageViewBitmap(R.id.image_widget, bitmapImage);

        Intent intent = new Intent();
        intent.putExtra("uri",uri);
        intent.putExtra("title",title);
        intent.putExtra("activity",activity);
        remoteViews.setOnClickFillInIntent(R.id.image_widget,intent);
        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
