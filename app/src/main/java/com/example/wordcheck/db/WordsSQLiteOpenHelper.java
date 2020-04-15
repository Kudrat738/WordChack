package com.example.wordcheck.db;
import android.example.wordcheck.R;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
/**
 * Created by 此文件打不开 on 2020/3/29.
 */

public class WordsSQLiteOpenHelper extends SQLiteOpenHelper {
    /**
     * 建表语句
     */
    private final String CREATE_WORDS = "create table Words(" +
            "id Integer primary key autoincrement," +
            "isChinese text," +
            "key text," +
            "fy text," +
            "psE text," +
            "pronE text," +
            "psA text," +
            "pronA text," +
            "posAcceptation text," +
            "sent text)";

    public WordsSQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_WORDS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
