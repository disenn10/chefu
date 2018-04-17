package com.example.disen.chefu;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.example.disen.chefu.service.GridviewService;

/**
 * Implementation of App Widget functionality.
 */
public class FavoriteRecipeWidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        appWidgetManager.getAppWidgetInfo(appWidgetId);
        RemoteViews views = getSavedRecipes(context);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }
    public static void updateWidgets(Context context, AppWidgetManager appWidgetManager,
                                     int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    public static RemoteViews getSavedRecipes(Context context){
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(),R.layout.favorite_recipe_widget_provider);
        Intent intent = new Intent(context, GridviewService.class);
        remoteViews.setRemoteAdapter(R.id.appwidget_id,intent);
        remoteViews.setEmptyView(R.id.appwidget_id,R.id.empty);

        Intent intent1 = new Intent(context, DetailActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent1,PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setPendingIntentTemplate(R.id.appwidget_id,pendingIntent);
        return  remoteViews;
    }
    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

