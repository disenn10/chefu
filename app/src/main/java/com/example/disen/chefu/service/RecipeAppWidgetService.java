package com.example.disen.chefu.service;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.Nullable;

import com.example.disen.chefu.FavoriteRecipeWidgetProvider;
import com.example.disen.chefu.R;
import com.example.disen.chefu.data.RecipeContract;

/**
 * Created by disen on 3/29/2018.
 */

public class RecipeAppWidgetService extends IntentService {
    static String UpdateWidgets = "updateWidgets";
    public RecipeAppWidgetService() {
        super("widgetservice");
    }
    public static void StartupdateWidgets(Context context){
        Intent intent = new Intent(context,RecipeAppWidgetService.class);
        intent.setAction(UpdateWidgets);
        context.startService(intent);
    }
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if(intent != null) {
            String action = intent.getAction();
            if (action.equals(UpdateWidgets)) {
                updateWidgets();
            }
        }
    }

    private void updateWidgets() {
        //Cursor cursor = getContentResolver().query(RecipeContract.contentRecipe,null,null,null,null);
        AppWidgetManager widgetmanager = AppWidgetManager.getInstance(this);
        int [] appWigetIds = widgetmanager.getAppWidgetIds(new ComponentName(this, FavoriteRecipeWidgetProvider.class));
        widgetmanager.notifyAppWidgetViewDataChanged(appWigetIds, R.layout.favorite_recipe_widget_provider);
        FavoriteRecipeWidgetProvider.updateWidgets(this,widgetmanager,appWigetIds);
    }
}
