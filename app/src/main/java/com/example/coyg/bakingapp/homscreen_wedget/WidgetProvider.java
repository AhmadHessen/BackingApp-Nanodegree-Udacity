package com.example.coyg.bakingapp.homscreen_wedget;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

public class WidgetProvider extends ContentProvider
{
    public static final String AUTHORITY = "com.example.coyg.bakingapp.main";
    public static final String INGS_TABLE ="ing_Table";
    public static final String ID_COLUMN = "id";
    public static final String QUANTITY_COLUMN = "quantity";
    public static final String MEASURE_COLUMN = "measure";
    public static final String INGREDIENT_COLUMN = "ingredient";
    public static final int INGREDIENTS = 100;
    public static final Uri URI = Uri.parse ("content://" + AUTHORITY + "/" + INGS_TABLE);


    public static final String CREATE_TABLE = "CREATE TABLE "+INGS_TABLE +" ("+
            ID_COLUMN+" INTEGER PRIMARY KEY AUTOINCREMENT ,"+
            QUANTITY_COLUMN+" TEXT ,"+
            MEASURE_COLUMN+" TEXT ,"+
            INGREDIENT_COLUMN+" TEXT )";

    UriMatcher uriMatcher = theMatcher();
    TheDB theDB;

    private UriMatcher theMatcher()
    {
        UriMatcher uriMatcher = new UriMatcher (UriMatcher.NO_MATCH);
        uriMatcher.addURI (AUTHORITY, INGS_TABLE, INGREDIENTS);
        return uriMatcher;
    }

    public WidgetProvider()
    {}

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs)
    {
        SQLiteDatabase sqLiteDatabase = theDB.getWritableDatabase ();

        int match = uriMatcher.match (uri);
        int del=0;

        switch (match)
        {
            case INGREDIENTS:
                del = sqLiteDatabase.delete (INGS_TABLE, selection, selectionArgs);
                getContext ().getContentResolver ().notifyChange (uri, null);
                break;
        }
        return del;
    }

    @Override
    public String getType(Uri uri)
    {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values)
    {
        SQLiteDatabase sqLiteDatabase = theDB.getWritableDatabase ();

        int match = uriMatcher.match (uri);
        Uri uri1 = null;

        switch (match)
        {
            case INGREDIENTS:
                long id = sqLiteDatabase.insert (INGS_TABLE, null, values);

                if(id>0)
                {
                    uri1 = ContentUris.withAppendedId (URI, id);
                    getContext ().getContentResolver ().notifyChange (uri, null);
                }
                break;
        }
        return uri1;
    }

    @Override
    public boolean onCreate()
    {
        theDB = new TheDB(getContext ());
        return true;
    }

    @Override
    public Cursor query(
            Uri uri, String[] projection, String selection,
            String[] selectionArgs, String sortOrder)
    {
        SQLiteDatabase sqLiteDatabase = theDB.getReadableDatabase ();

        int matcher = uriMatcher.match (uri);
        Cursor cursor = null;

        switch (matcher)
        {
            case INGREDIENTS:
                cursor = sqLiteDatabase.query (INGS_TABLE,projection, selection,
                        selectionArgs, null, null, sortOrder);

                cursor.setNotificationUri (getContext ().getContentResolver (), uri);
                break;
        }
        return cursor;
    }

    @Override
    public int update(
            Uri uri, ContentValues values, String selection,
            String[] selectionArgs)
    {
        return 0;
    }

    class TheDB extends SQLiteOpenHelper
    {

        TheDB(Context context)
        {
            super (context, INGS_TABLE, null, 1);

        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase)
        {
            sqLiteDatabase.execSQL (CREATE_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1)
        {
            sqLiteDatabase.execSQL ("DROP TABLE IF EXISTS "+INGS_TABLE);
            onCreate (sqLiteDatabase);
        }
    }
}
