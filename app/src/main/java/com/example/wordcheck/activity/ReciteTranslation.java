package com.example.wordcheck.activity;

import android.app.Activity;
import android.content.Intent;
import android.example.wordcheck.R;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.wordcheck.kind.WordInfo;
import com.example.wordcheck.util.WordBox;
import com.example.wordcheck.util.WordsAction;

import java.util.Random;

/**
 * Created by 此文件打不开 on 2020/4/19.
 */

public class ReciteTranslation extends Activity {
    private WordBox wordBox;
    private String tableName="glossary";
    private WordInfo wordInfo;
    private WordInfo wordInfo2;
    private WordInfo wordInfo3;
    private TextView ky,translation,translation2,translation3,translation4,grasp,right,wrong,learned,unlearned;
    private Button next,delete;
    public Random rand=null;
    private boolean ischech;
    private boolean aBoolean1;
    private boolean aBoolean2;
    private boolean aBoolean3;
    private WordsAction wordsAction;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recite_translation);
        wordsAction = WordsAction.getInstance(this);
        rand=new Random();
        ischech=true;
        aBoolean1=false;
        aBoolean2=false;
        aBoolean3=false;
        ky=(TextView)findViewById(R.id.ky);
        ky.setClickable(true);
        translation3=(TextView)findViewById(R.id.translation3);
        translation3.setClickable(true);
        translation2=(TextView)findViewById(R.id.translation2);
        translation2.setClickable(true);
        translation4=(TextView)findViewById(R.id.translation4);
        translation4.setClickable(true);
        translation=(TextView)findViewById(R.id.translation);
        grasp=(TextView)findViewById(R.id.grasp);
        wrong=(TextView)findViewById(R.id.wrong);
        right=(TextView)findViewById(R.id.right);
        learned=(TextView)findViewById(R.id.learned);
        unlearned=(TextView)findViewById(R.id.unlearned);
        next=(Button)findViewById(R.id.next);
        delete=(Button)findViewById(R.id.delete);
        wordBox=new WordBox(ReciteTranslation.this,tableName);
        learned.setText(String.valueOf(wordBox.getTotalLearnProgress()));
        unlearned.setText(String.valueOf(wordBox.getWordCountOfUnlearned()));
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wordInfo=wordBox.popWord();
                ky.setText(wordInfo.getInterpret());
                translation.setVisibility(View.INVISIBLE);
                grasp.setVisibility(View.INVISIBLE);
                right.setVisibility(View.INVISIBLE);
                wrong.setVisibility(View.INVISIBLE);
                switch (rand.nextInt(2)){
                    case 1:
                        translation2.setText(wordInfo.getWord());
                        aBoolean1=true;
                        wordInfo2=wordBox.getWordByRandom();
                        if (wordInfo2.getWord().equals(wordInfo.getWord())){
                            wordInfo2=wordBox.getWordByRandom();
                            if (wordInfo2.getWord().equals(wordInfo.getWord())){
                                wordInfo2=wordBox.getWordByRandom();
                            }else {
                                translation3.setText(wordInfo2.getWord());
                            }
                        }else {
                            translation3.setText(wordInfo2.getWord());
                        }
                        aBoolean2=false;
                        wordInfo3=wordBox.getWordByRandom();
                        if (wordInfo3.getWord().equals(wordInfo.getWord())){
                            wordInfo3=wordBox.getWordByRandom();
                            if (wordInfo3.getWord().equals(wordInfo.getWord())){
                                wordInfo3=wordBox.getWordByRandom();
                            }else {
                                translation4.setText(wordInfo3.getWord());
                            }
                        }else {
                            translation4.setText(wordInfo3.getWord());
                        }
                        aBoolean3=false;
                        break;
                    case 2:
                        translation3.setText(wordInfo.getWord());
                        aBoolean2=true;
                        wordInfo2=wordBox.getWordByRandom();
                        translation2.setText(wordInfo2.getWord());
                        if (wordInfo2.getWord().equals(wordInfo.getWord())){
                            wordInfo2=wordBox.getWordByRandom();
                            if (wordInfo2.getWord().equals(wordInfo.getWord())){
                                wordInfo2=wordBox.getWordByRandom();
                            }else {
                                translation2.setText(wordInfo2.getWord());
                            }
                        }else {
                            translation2.setText(wordInfo2.getWord());
                        }
                        aBoolean1=false;
                        wordInfo3=wordBox.getWordByRandom();
                        if (wordInfo3.getWord().equals(wordInfo.getWord())){
                            wordInfo3=wordBox.getWordByRandom();
                            if (wordInfo3.getWord().equals(wordInfo.getWord())){
                                wordInfo3=wordBox.getWordByRandom();
                            }else {
                                translation4.setText(wordInfo3.getWord());
                            }
                        }else {
                            translation4.setText(wordInfo3.getWord());
                        }
                        aBoolean3=false;
                        break;
                    case 0:
                        translation4.setText(wordInfo.getWord());
                        aBoolean3=true;
                        wordInfo2=wordBox.getWordByRandom();
                        if (wordInfo2.getWord().equals(wordInfo.getWord())){
                            wordInfo2=wordBox.getWordByRandom();
                            if (wordInfo2.getWord().equals(wordInfo.getWord())){
                                wordInfo2=wordBox.getWordByRandom();
                            }else {
                                translation3.setText(wordInfo2.getWord());
                            }
                        }else {
                            translation3.setText(wordInfo2.getWord());
                        }
                        aBoolean1=false;
                        wordInfo3=wordBox.getWordByRandom();
                        if (wordInfo3.getWord().equals(wordInfo.getWord())){
                            wordInfo3=wordBox.getWordByRandom();
                            if (wordInfo3.getWord().equals(wordInfo.getWord())){
                                wordInfo3=wordBox.getWordByRandom();
                            }else {
                                translation2.setText(wordInfo3.getWord());
                            }
                        }else {
                            translation2.setText(wordInfo3.getWord());
                        }
                        aBoolean2=false;
                        break;

                }
            }
        });
        translation2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //   wordInfo=getWordInfo();
                wordBox.feedBack(wordInfo,aBoolean1);
                grasp.setVisibility(View.VISIBLE);
                right.setVisibility(View.VISIBLE);
                wrong.setVisibility(View.VISIBLE);
                grasp.setText(String.valueOf(wordInfo.getGrasp()));
                right.setText(String.valueOf(wordInfo.getRight()));
                wrong.setText(String.valueOf(wordInfo.getWrong()));
                learned.setText(String.valueOf(wordBox.getTotalLearnProgress()));
                unlearned.setText(String.valueOf(wordBox.getWordCountOfUnlearned()));
            }});
        translation3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //    wordInfo=getWordInfo();
                wordBox.feedBack(wordInfo,aBoolean2);
                grasp.setVisibility(View.VISIBLE);
                right.setVisibility(View.VISIBLE);
                wrong.setVisibility(View.VISIBLE);
                grasp.setText(String.valueOf(wordInfo.getGrasp()));
                right.setText(String.valueOf(wordInfo.getRight()));
                wrong.setText(String.valueOf(wordInfo.getWrong()));
                learned.setText(String.valueOf(wordBox.getTotalLearnProgress()));
                unlearned.setText(String.valueOf(wordBox.getWordCountOfUnlearned()));
            }});
        translation4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //wordInfo=getWordInfo();
                wordBox.feedBack(wordInfo,aBoolean3);
                grasp.setVisibility(View.VISIBLE);
                right.setVisibility(View.VISIBLE);
                wrong.setVisibility(View.VISIBLE);
                grasp.setText(String.valueOf(wordInfo.getGrasp()));
                right.setText(String.valueOf(wordInfo.getRight()));
                wrong.setText(String.valueOf(wordInfo.getWrong()));
                learned.setText(String.valueOf(wordBox.getTotalLearnProgress()));
                unlearned.setText(String.valueOf(wordBox.getWordCountOfUnlearned()));
            }});
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wordBox.removeWordFromDatabase(wordInfo.getWord());
                wordInfo=wordBox.popWord();
                //setWordInfo(wordInfo);
                ky.setText(wordInfo.getInterpret());
                learned.setText(String.valueOf(wordBox.getTotalLearnProgress()));
                unlearned.setText(String.valueOf(wordBox.getWordCountOfUnlearned()));
                grasp.setVisibility(View.INVISIBLE);
                right.setVisibility(View.INVISIBLE);
                wrong.setVisibility(View.INVISIBLE);
                switch (rand.nextInt(2)) {
                    case 1:
                        translation2.setText(wordInfo.getWord());
                        aBoolean1 = true;
                        wordInfo2 = wordBox.getWordByRandom();
                        if (wordInfo2.getWord().equals(wordInfo.getWord())) {
                            wordInfo2 = wordBox.getWordByRandom();
                            if (wordInfo2.getWord().equals(wordInfo.getWord())) {
                                wordInfo2 = wordBox.getWordByRandom();
                            } else {
                                translation3.setText(wordInfo2.getWord());
                            }
                        } else {
                            translation3.setText(wordInfo2.getWord());
                        }
                        aBoolean2 = false;
                        wordInfo3 = wordBox.getWordByRandom();
                        if (wordInfo3.getWord().equals(wordInfo.getWord())) {
                            wordInfo3 = wordBox.getWordByRandom();
                            if (wordInfo3.getWord().equals(wordInfo.getWord())) {
                                wordInfo3 = wordBox.getWordByRandom();
                            } else {
                                translation4.setText(wordInfo3.getWord());
                            }
                        } else {
                            translation4.setText(wordInfo3.getWord());
                        }
                        aBoolean3 = false;
                        break;
                    case 2:
                        translation3.setText(wordInfo.getWord());
                        aBoolean2 = true;
                        wordInfo2 = wordBox.getWordByRandom();
                        translation2.setText(wordInfo2.getWord());
                        if (wordInfo2.getWord().equals(wordInfo.getWord())) {
                            wordInfo2 = wordBox.getWordByRandom();
                            if (wordInfo2.getWord().equals(wordInfo.getWord())) {
                                wordInfo2 = wordBox.getWordByRandom();
                            } else {
                                translation2.setText(wordInfo2.getWord());
                            }
                        } else {
                            translation2.setText(wordInfo2.getWord());
                        }
                        aBoolean1 = false;
                        wordInfo3 = wordBox.getWordByRandom();
                        if (wordInfo3.getWord().equals(wordInfo.getWord())) {
                            wordInfo3 = wordBox.getWordByRandom();
                            if (wordInfo3.getWord().equals(wordInfo.getWord())) {
                                wordInfo3 = wordBox.getWordByRandom();
                            } else {
                                translation4.setText(wordInfo3.getWord());
                            }
                        } else {
                            translation4.setText(wordInfo3.getWord());
                        }
                        aBoolean3 = false;
                        break;
                    case 0:
                        translation4.setText(wordInfo.getWord());
                        aBoolean3 = true;
                        wordInfo2 = wordBox.getWordByRandom();
                        if (wordInfo2.getWord().equals(wordInfo.getWord())) {
                            wordInfo2 = wordBox.getWordByRandom();
                            if (wordInfo2.getWord().equals(wordInfo.getWord())) {
                                wordInfo2 = wordBox.getWordByRandom();
                            } else {
                                translation3.setText(wordInfo2.getWord());
                            }
                        } else {
                            translation3.setText(wordInfo2.getWord());
                        }
                        aBoolean1 = false;
                        wordInfo3 = wordBox.getWordByRandom();
                        if (wordInfo3.getWord().equals(wordInfo.getWord())) {
                            wordInfo3 = wordBox.getWordByRandom();
                            if (wordInfo3.getWord().equals(wordInfo.getWord())) {
                                wordInfo3 = wordBox.getWordByRandom();
                            } else {
                                translation2.setText(wordInfo3.getWord());
                            }
                        } else {
                            translation2.setText(wordInfo3.getWord());
                        }
                        aBoolean2 = false;
                        break;
                }
            }});
        ky.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ischech) {
                    //   wordInfo=getWordInfo();
                    translation.setVisibility(View.VISIBLE);
                    translation.setText(wordInfo.getWord());
                    right.setText(String.valueOf(wordInfo.getRight()));
                    grasp.setText(String.valueOf(wordInfo.getGrasp()));
                    wrong.setText(String.valueOf(wordInfo.getWrong()));
                    grasp.setVisibility(View.VISIBLE);
                    right.setVisibility(View.VISIBLE);
                    wrong.setVisibility(View.VISIBLE);

                }else {
                    ischech=true;
                }

            }
        });
        ky.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View v) {
                wordsAction.playMP3(wordInfo.getWord(),"E",ReciteTranslation.this);
                ischech=false;
                return ischech;
            }
        });

    }

}
