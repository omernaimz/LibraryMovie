package com.example.omernaim.librarymovie;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by OmerNaim on 07/02/2017.
 */

public class SQLhelper extends SQLiteOpenHelper {
    Context context;

    public SQLhelper(Context context) {
        super(context, "mymovieslist.db" , null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+DBconstant.tablename_db+"(_id INTEGER PRIMARY KEY AUTOINCREMENT, "+DBconstant.moviename_db+" TEXT ,"+DBconstant.description_db+" TEXT, "+DBconstant.url_db+" TEXT , "+DBconstant.bitmap64+ " TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
