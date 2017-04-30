package com.systems.adr_.studio8_48_mobile;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by luis_ on 29/04/2017.
 */

public class LocalDBHelper extends SQLiteOpenHelper {
    public LocalDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table auth (id integer primary key)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP DATABASE IF EXIST auth");
        onCreate(db);
    }
}
