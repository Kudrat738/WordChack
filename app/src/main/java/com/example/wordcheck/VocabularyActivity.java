package com.example.wordcheck;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.example.wordcheck.R;
import android.widget.Toast;

import com.example.wordcheck.model.Vocabulary;
import com.example.wordcheck.model.Words;
import com.example.wordcheck.util.FileUtil;
import com.example.wordcheck.util.VocabularyAction;
import com.example.wordcheck.util.VocabularyAdapter;
import com.example.wordcheck.util.WordsAction;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by 此文件打不开 on 2020/3/29.
 */
public class VocabularyActivity extends Activity {
    private List<Vocabulary> listViewVocabulary=new ArrayList<>();
    private ListView listView;
    private VocabularyAction vocabularyAction;
    private WordsAction wordsAction;
    private boolean ischech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocabulary);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        ischech=true;
        vocabularyAction=VocabularyAction.getInstance(this);
        wordsAction = WordsAction.getInstance(this);
        listViewVocabulary=vocabularyAction.getVocabularyList();
        VocabularyAdapter vocabularyAdapter=new VocabularyAdapter(VocabularyActivity.this,R.layout.vocabulary_item,listViewVocabulary);
        listView = (ListView) findViewById(R.id.vocabulary_listView);
       // listView.setItemsCanFocus(true);
        listView.setAdapter(vocabularyAdapter);
     listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position,long id) {
                Vocabulary vocabulary = listViewVocabulary.get(position);
                //switch (view.getId()) {
               /*     case R.id.voice_a:
                        wordsAction.playMP3(vocabulary.getWordsKey(), "A", VocabularyActivity.this);
                        break;
                    case R.id.voice_e:
                        wordsAction.playMP3(vocabulary.getWordsKey(), "E", VocabularyActivity.this);
                        break;*/
                // case R.id.delete:
                if (ischech) {
                    wordsAction.playMP3(vocabulary.getWordsKey(), "E", VocabularyActivity.this);

                }else {
                    ischech=true;
                }
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,int position,long id){
                Log.d("测试", "主");
                Vocabulary vocabulary=listViewVocabulary.get(position);
                vocabularyAction.deleteFormVocabulary(vocabulary.getWordsKey());
                refresh();
                File file=new File(vocabulary.getWordsKey());
                FileUtil.getInstance().deleteFile(file);
                ischech=false;
                return ischech;
            }
        });


    }
    public void refresh(){
        listViewVocabulary=vocabularyAction.getVocabularyList();
        VocabularyAdapter vocabularyAdapter=new VocabularyAdapter(VocabularyActivity.this,R.layout.vocabulary_item,listViewVocabulary);
        listView.setAdapter(vocabularyAdapter);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.vocabulary_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.alphabet:
                listViewVocabulary=vocabularyAction.getVocabularyList();
                Collections.sort(listViewVocabulary, new Comparator<Vocabulary>() {
                    @Override
                    public int compare(Vocabulary a,Vocabulary b ){
                        return a.getWordsKey().compareTo(b.getWordsKey());
                    }
                });
                VocabularyAdapter vocabularyAdapter=new VocabularyAdapter(VocabularyActivity.this,R.layout.vocabulary_item,listViewVocabulary);
                listView = (ListView) findViewById(R.id.vocabulary_listView);
               listView.setAdapter(vocabularyAdapter);
                break;
            case R.id.defaults:
                listViewVocabulary=vocabularyAction.getVocabularyList();
                VocabularyAdapter vocabularyAdapterb=new VocabularyAdapter(VocabularyActivity.this,R.layout.vocabulary_item,listViewVocabulary);
                listView = (ListView) findViewById(R.id.vocabulary_listView);
                listView.setAdapter(vocabularyAdapterb);

                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
