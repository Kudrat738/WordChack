package com.example.wordcheck.single;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.wordcheck.db.VocabularySQLiteHelper;
import com.example.wordcheck.kind.Vocabulary;
import com.example.wordcheck.kind.Words;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

/**
 * Created by 此文件打不开 on 2020/4/2.
 */
public class VocabularyAction {
    //Vocabulary的表名
    private final String TABLE_VOCABULARY = "Vocabulary";

    private static VocabularyAction vocabularyAction;
    private boolean check;
    private SQLiteDatabase db;

    private VocabularyAction(Context context) {
        VocabularySQLiteHelper vocabularySQLiteHelper = new VocabularySQLiteHelper(context, TABLE_VOCABULARY, null, 1);
        db = vocabularySQLiteHelper.getWritableDatabase();

    }



    // 单例类VocabularyAction获取实例方法
    public static VocabularyAction getInstance(Context context) {
        if (vocabularyAction == null) {
            synchronized (VocabularyAction.class) {
                if (vocabularyAction == null) {
                    vocabularyAction = new VocabularyAction(context);
                }
            }
        }
        return vocabularyAction;
    }

    //向生词本中添加单词
    public void addToVocabulary(Words words) {
        check = isExistInVocabulary(words.getKey());
        if (check) {
            Log.d("测试","重复元素");
        }else {

            Vocabulary vocabulary = new Vocabulary(words.getKey(), words.getPosAcceptation());
            ContentValues values = new ContentValues();
            values.put("wordsKey", vocabulary.getWordsKey());
            values.put("translation", vocabulary.getTranslation());
            values.put("masteryLevel", vocabulary.getMasteryLevel());
            values.put("right", vocabulary.getRight());
            values.put("wrong", vocabulary.getWrong());
            db.insert(TABLE_VOCABULARY, null, values);
            values.clear();
            Log.d("测试","元素添加成功");
        }

    }
    //判断单词是否存在于生词本中
    public boolean isExistInVocabulary(String wordsKey) {
        Cursor cursor = db.query(TABLE_VOCABULARY, null, null, null, null, null, null);
        if (cursor.moveToFirst()){
            do {
                String name=cursor.getString(cursor.getColumnIndex("wordsKey"));
                if (name.equals(wordsKey)){
                    return true;
                }
                Log.d("测试",name);
            }while (cursor.moveToNext());
        }cursor.close();
        return false;

    }


    //  删除生词本中的指定单词
    public void deleteFormVocabulary(String wordsKey) {
        check = isExistInVocabulary(wordsKey);
        if (check) {
            db.delete(TABLE_VOCABULARY, "wordsKey = ?", new String[]{wordsKey});
        }
    }

    //获取生词本中所有生词
    public List<Vocabulary> getVocabularyList() {
        List<Vocabulary> vocabularyList = new ArrayList<Vocabulary>();
        Cursor cursor = db.query(TABLE_VOCABULARY, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Vocabulary vocabulary = new Vocabulary();
                vocabulary.setWordsKey(cursor.getString(cursor.getColumnIndex("wordsKey")));
                vocabulary.setTranslation(cursor.getString(cursor.getColumnIndex("translation")));
                vocabulary.setMasteryLevel(cursor.getInt(cursor.getColumnIndex("masteryLevel")));
                vocabulary.setRight(cursor.getInt(cursor.getColumnIndex("right")));
                vocabulary.setWrong(cursor.getInt(cursor.getColumnIndex("wrong")));
                vocabularyList.add(vocabulary);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return vocabularyList;
    }
    //获取生词本里的一个单词
    public Vocabulary getVocabularyFromSQLite(String key){
        Vocabulary vocabulary=new Vocabulary();
        Cursor cursor = db.query(TABLE_VOCABULARY,  null,"wordsKey=?", new String[]{key}, null, null, null, null);
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    vocabulary.setWordsKey(cursor.getString(cursor.getColumnIndex("wordsKey")));
                    vocabulary.setTranslation(cursor.getString(cursor.getColumnIndex("translation")));
                    vocabulary.setMasteryLevel(cursor.getInt(cursor.getColumnIndex("masteryLevel")));
                    vocabulary.setRight(cursor.getInt(cursor.getColumnIndex("right")));
                    vocabulary.setWrong(cursor.getInt(cursor.getColumnIndex("wrong")));
                }   while (cursor.moveToNext());
                }
            cursor.close();
        }else {
            cursor.close();
            return null;
        }
        return vocabulary;
    }
}
