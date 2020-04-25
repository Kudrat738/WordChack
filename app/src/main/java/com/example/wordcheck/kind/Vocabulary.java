package com.example.wordcheck.kind;
import android.example.wordcheck.R;

/**
 * Created by 此文件打不开 on 2020/3/29.
 */

public class Vocabulary{
    //生词的掌握程度
    public static final int MASTERY_LEVEL_1 = 1;
    private String wordsKey;
    /**
     * 生词的基本释义，对应Words类的posAcceptation属性
     */
    private String translation;
    //生词的掌握程度
    private int masteryLevel;
    //练习中答对的次数
    private int right;
    //练习中答错的次数
    private int wrong;

    public Vocabulary() {
        wordsKey = "";
        translation = "";
        masteryLevel = MASTERY_LEVEL_1;
        right = 0;
        wrong = 0;
    }

    public Vocabulary(String wordsKey, String translation) {
        this.wordsKey = wordsKey;
        this.translation = translation;
        masteryLevel = MASTERY_LEVEL_1;
        right = 0;
        wrong = 0;
    }

    public String getWordsKey() {
        return wordsKey;
    }


    public void setWordsKey(String wordsKey) {
        this.wordsKey = wordsKey;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public int getMasteryLevel() {
        return masteryLevel;
    }

    public int getRight() {
        return right;
    }

    public int getWrong() {
        return wrong;
    }

    public void setMasteryLevel(int masteryLevel) {
        this.masteryLevel = masteryLevel;
    }

    public void setRight(int right) {
        this.right = right;
    }

    public void setWrong(int wrong) {
        this.wrong = wrong;
    }
}
