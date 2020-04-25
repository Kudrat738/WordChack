package com.example.wordcheck.activity;

import android.app.Activity;
import android.content.Context;
import android.example.wordcheck.R;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wordcheck.kind.Vocabulary;
import com.example.wordcheck.db.RecordSQLiteOpenHelper;
import com.example.wordcheck.single.VocabularyAction;
import com.example.wordcheck.single.WordsAction;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by 此文件打不开 on 2020/4/18.
 */

public class VocabularySerech extends Activity {
    private EditText vocabulary_search;
    private Button vocabulary_serech_bt;
    private TextView vocabulary_serech_back;
    private LinearLayout vocabulary_search_father;
    private RecordSQLiteOpenHelper helper ;
    private WordsAction wordsAction;
    private VocabularyAction vocabularyAction;
    private Vocabulary vocabulary=new Vocabulary();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vocabulary_search);
        vocabulary_search=(EditText)findViewById(R.id.vocabulary_search);
        vocabulary_serech_bt=(Button)findViewById(R.id.vocabulary_serech_bt);
        vocabulary_serech_back=(TextView) findViewById(R.id.vocabulary_tv_back);
        vocabulary_search_father=(LinearLayout)findViewById(R.id.vocabulary_search_father);
       wordsAction = WordsAction.getInstance(this);
        vocabularyAction=VocabularyAction.getInstance(this);
        helper= new RecordSQLiteOpenHelper(this);
        vocabulary_search_father.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击输入框外实现软键盘隐藏
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        });
        // 修改回车键功能
        vocabulary_search.setOnKeyListener(new View.OnKeyListener() {
            // 输入完后按键盘上的搜索键
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                   // 先隐藏键盘防止挤原有的布局
                    ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                            getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    loadWords(vocabulary_search.getText().toString().trim());
                }
                return false;           }
        });

        vocabulary_serech_bt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                loadWords(vocabulary_search.getText().toString().trim());
            }
        });
        vocabulary_serech_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wordsAction.playMP3(vocabulary_search.getText().toString().trim(), "E", VocabularySerech.this);
            }});
    }

    @Override
    protected void onStart(){
        super.onStart();
        input();
    }
    public void loadWords(String wordsKey) {
vocabulary=vocabularyAction.getVocabularyFromSQLite(wordsKey);
        if (vocabulary!=null){
        vocabulary_serech_back.setText(vocabulary.getTranslation());
    }else {
        vocabulary_serech_back.setText("生词本里没有该单词");
    }}
    public void input(){
        Timer timer=new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                InputMethodManager manager=(InputMethodManager)vocabulary_search.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                manager.showSoftInput(vocabulary_search,0);
            }
        },99);
    }
    }

