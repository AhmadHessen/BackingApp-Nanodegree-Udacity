package com.example.coyg.bakingapp.homscreen_wedget;

import android.app.Activity;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.example.coyg.bakingapp.R;
import com.example.coyg.bakingapp.main.Main2Activity;

/**
 * Implementation of App Widget functionality.
 */
public class TheWidget extends AppWidgetProvider
{

    static void updateAppWidget(
            Context context, AppWidgetManager appWidgetManager,
            int appWidgetId)
    {

        Intent intent = new Intent (context, Main2Activity.class);

        String widgt_text = context.getSharedPreferences
                ("shared_preferences", Activity.MODE_PRIVATE)
                .getString ("recipe_name", "");

        RemoteViews views = new RemoteViews (context.getPackageName (),
                R.layout.the_widget);
        PendingIntent pendingIntent = PendingIntent.getActivity
                (context, 0, intent, 0);

        views.setTextViewText (R.id.appwidget_text, widgt_text);
        views.setOnClickPendingIntent (R.id.widget_layout, pendingIntent);

        Intent intentWidgetService = new Intent (context, WidgetService.class);
        views.setRemoteAdapter (R.id.widget_listview, intentWidgetService);
        views.setEmptyView (R.id.widget_listview, R.id.empty_list);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget (appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds)
    {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds)
        {
            updateAppWidget (context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context)
    {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context)
    {
        // Enter relevant functionality for when the last widget is disabled
    }
}

