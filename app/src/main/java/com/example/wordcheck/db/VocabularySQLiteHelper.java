package com.example.wordcheck.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.example.wordcheck.R;
/**
 * Created by 此文件打不开 on 2020/3/29.
 */
public class VocabularySQLiteHelper extends SQLiteOpenHelper {

    //建表语句
    private final String CREATE_VOCABULARY = "create table Vocabulary ( " +
            "id Integer primary key autoincrement," +
            "wordsKey text," +
            "translation text," +
            "masteryLevel Integer," +
            "right Integer," +
            "wrong Integer)";

    public VocabularySQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_VOCABULARY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
