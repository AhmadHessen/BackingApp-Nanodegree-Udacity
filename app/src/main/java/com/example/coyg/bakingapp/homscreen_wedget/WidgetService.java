package com.example.coyg.bakingapp.homscreen_wedget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.coyg.bakingapp.R;

public class WidgetService extends RemoteViewsService
{
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent)
    {
        return new WidgetRemoteViewsFactory (getApplicationContext (), intent);
    }

    class WidgetRemoteViewsFactory implements RemoteViewsFactory
    {
        Context context;
        Cursor cursor;

        public WidgetRemoteViewsFactory(Context context, Intent intent)
        {
            this.context = context;
        }

        @Override
        public void onCreate()
        {
            getDB();
        }

        private void getDB()
        {
            cursor = context.getContentResolver ().query
                    (WidgetProvider.URI,null,
                    null, null, null);
        }

        @Override
        public void onDataSetChanged()
        {
            getDB();
        }

        @Override
        public void onDestroy()
        {
            cursor.close ();
        }

        @Override
        public int getCount()
        {
            return cursor.getCount ();
        }

        @Override
        public RemoteViews getViewAt(int i)
        {
            if (cursor.getCount() == 0) return null;
            RemoteViews remoteViews = new RemoteViews
                    (context.getPackageName(), R.layout.widget_list_item);

            cursor.moveToPosition(i);

            String ingredient = cursor.getString
                    (cursor.getColumnIndex(WidgetProvider.INGREDIENT_COLUMN));

            String measure = cursor.getString
                    (cursor.getColumnIndex(WidgetProvider.MEASURE_COLUMN));

            String quantity = cursor.getString
                    (cursor.getColumnIndex(WidgetProvider.QUANTITY_COLUMN));

            remoteViews.setTextViewText(R.id.widget_ing, ingredient);
            remoteViews.setTextViewText(R.id.widget_measure, measure);
            remoteViews.setTextViewText(R.id.widget_quantity, quantity);

            return remoteViews;
        }

        @Override
        public RemoteViews getLoadingView()
        {
            return null;
        }

        @Override
        public int getViewTypeCount()
        {
            return 1;
        }

        @Override
        public long getItemId(int i)
        {
            return i;
        }

        @Override
        public boolean hasStableIds()
        {
            return false;
        }
    }
}
