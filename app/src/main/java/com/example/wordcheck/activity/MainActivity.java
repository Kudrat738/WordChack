package com.example.wordcheck.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.example.wordcheck.R;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.example.wordcheck.Service.FloatWindowService;
import com.example.wordcheck.db.DataBaseHelper;
import com.example.wordcheck.kind.Words;
import com.example.wordcheck.util.HttpCallBackListener;
import com.example.wordcheck.util.HttpUtil;
import com.example.wordcheck.list.MyListView;
import com.example.wordcheck.util.ParseXML;
import com.example.wordcheck.db.RecordSQLiteOpenHelper;
import com.example.wordcheck.single.VocabularyAction;
import com.example.wordcheck.single.WordsAction;
import com.example.wordcheck.util.WordsHandler;

import java.io.InputStream;
import java.util.Random;

/**
 * Created by 此文件打不开 on 2020/3/29.
 */
public class MainActivity extends Activity {
    private Context context;
    private EditText et_search;
    private TextView tv_tip;
    private MyListView listView;
    private TextView tv_clear;
    private RecordSQLiteOpenHelper helper ;
    private SQLiteDatabase db;
    private BaseAdapter adapter;
    private VocabularyAction vocabularyAction;
    private DataBaseHelper dataBaseHelper;
    private boolean ischech;
    private View view;
    private static final int REQUEST_EXTERNAL_STORAGE=1;
    private static String[] PERMISSIONS_STORAGE={
            "Manifest.permission.READ_EXTERNAL_STORAGE","Manifest.permission.WRITE_EXTERNAL_STORAGE"
    };
    private TextView searchWords_key, searchWords_psE, searchWords_psA, searchWords_posAcceptation, searchWords_sent;
    private ImageButton searchWords_voiceE, searchWords_voiceA;
    private LinearLayout searchWords_posA_layout,searchWords_posE_layout, searchWords_linerLayout, searchWords_fatherLayout;
    private WordsAction wordsAction;
    private Words words = new Words();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化普通数据;
        initType();
        //得到权限
        primission();
        //初始化查词控件
        initWordView();
        //初始化搜索框
        initView();
        //点击输入框外实现软键盘隐藏
        searchWords_fatherLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        });
        //创建小悬浮窗口
        show(view);
        searchWords_voiceE = (ImageButton) findViewById(R.id.searchWords_voiceE);
        searchWords_voiceE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wordsAction.playMP3(words.getKey(), "E", MainActivity.this);


            }
        });
        searchWords_voiceA = (ImageButton) findViewById(R.id.searchWords_voiceA);
        searchWords_voiceA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wordsAction.playMP3(words.getKey(), "A", MainActivity.this);
            }
        });

        // 清空搜索历史
        tv_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //删除记录
                deleteData();
                //加载数据
                queryData("");
            }
        });
        // 修改回车键功能
        et_search.setOnKeyListener(new View.OnKeyListener() {// 输入完后按键盘上的搜索键

            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    // 先隐藏键盘防止挤原有的布局
                    ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                            getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    // 按完搜索键后将当前查询的关键字保存起来,如果该关键字已经存在就不执行保存
                    boolean hasData = hasData(et_search.getText().toString().trim());
                    if (!hasData) {
                        insertData(et_search.getText().toString().trim());
                        queryData("");
                    }
                }
                return false;
            }
        });
        // 搜索框的文本变化实时监听
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!et_search.getText().toString().trim().equals(null))
                loadWords(et_search.getText().toString().trim());

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().length() == 0) {
                    tv_tip.setText("搜索历史");
                } else {
                    tv_tip.setText("搜索结果");

                }
                String tempName = et_search.getText().toString();
                // 根据tempName去模糊查询数据库中有没有数据
                queryData(tempName);


            }
        });
//搜索点击的搜查记录item
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (ischech) {
                TextView textView = (TextView) view.findViewById(android.R.id.text1);
                String name = textView.getText().toString();
                et_search.setText(name);
                loadWords(name);

            }else {
                    ischech=true;
                }
        }});
        //删除长按的搜查记录item
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id){
                TextView textView = (TextView) view.findViewById(android.R.id.text1);
                String name = textView.getText().toString();
                db = helper.getWritableDatabase();
                db.execSQL("delete from records where name =?", new String[]{name});
                db.close();
                queryData("");
                ischech=false;
                return ischech;
        }


        });}
    //创建数据表或者打开数据表
    private void init(){
        helper=new RecordSQLiteOpenHelper(MainActivity.this);
        //加载数据
        queryData("");
    }

//添加数据
    private void insertData(String tempName) {
        db = helper.getWritableDatabase();
        db.execSQL("insert into records(name) values('" + tempName + "')");
        db.close();
    }

    //加载数据
    private void queryData(String tempName) {
        Cursor cursor = helper.getReadableDatabase().rawQuery(
                //查询records表中的id字段起个别名为“_id”（这是为了下面的创建适配器）,条件是name段包含“”
                "select id as _id,name from records where name like '%" + tempName + "%' order by id desc ", null);
        // 创建adapter适配器对象，这里使用系统定义好的布局文件只显示一行文字
        adapter=new SimpleCursorAdapter(this,android.R.layout.simple_list_item_1,cursor,new String[]{"name"},new int[]{ android.R.id.text1 },CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        // 设置适配器
        listView.setAdapter(adapter);
        //记住你划到的位置,重新加载数据的时候不会改变位置,只是改变了数据
        adapter.notifyDataSetChanged();
    }
    //检查数据库中是否已经有该条记录
    private boolean hasData(String tempName) {
        Cursor cursor = helper.getReadableDatabase().rawQuery(
                "select id as _id,name from records where name =?", new String[]{tempName});
        //判断是否有下一个
        loadWords(tempName);
        return cursor.moveToNext();
    }

    //清空数据
    private void deleteData() {
        db = helper.getWritableDatabase();
        db.execSQL("delete from records");
        db.close();
    }
public void primission(){
    if(Build.VERSION.SDK_INT >= 23){   //悬浮窗口权限
        if (!Settings.canDrawOverlays(context)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }
    //内存读取权限
    if (ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
        ActivityCompat.requestPermissions(MainActivity.this,PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);
    }
}
private void initType(){
    context = this;
    ischech=true;
    helper= new RecordSQLiteOpenHelper(this);
    dataBaseHelper=new DataBaseHelper(this,"glossary");
    vocabularyAction=VocabularyAction.getInstance(this);
    wordsAction = WordsAction.getInstance(this);
}
    private void initWordView(){
        searchWords_linerLayout = (LinearLayout) findViewById(R.id.searchWords_linerLayout);
        searchWords_posA_layout = (LinearLayout) findViewById(R.id.searchWords_posA_layout);
        searchWords_posE_layout = (LinearLayout) findViewById(R.id.searchWords_posE_layout);
        searchWords_fatherLayout = (LinearLayout) findViewById(R.id.searchWords_fatherLayout);
        searchWords_key = (TextView) findViewById(R.id.searchWords_key);
        searchWords_psE = (TextView) findViewById(R.id.searchWords_psE);
        searchWords_psA = (TextView) findViewById(R.id.searchWords_psA);
        searchWords_posAcceptation = (TextView) findViewById(R.id.searchWords_posAcceptation);
        searchWords_sent = (TextView) findViewById(R.id.searchWords_sent);
    }
    private void initView() {
        et_search = (EditText) findViewById(R.id.et_search);
        tv_tip = (TextView) findViewById(R.id.tv_tip);
        listView = (MyListView) findViewById(R.id.listView);
        tv_clear = (TextView) findViewById(R.id.tv_clear);
        //创建数据表或者打开数据表
         init();
        // 调整EditText左边的搜索按钮的大小
        Drawable drawable = getResources().getDrawable(R.drawable.search);
        drawable.setBounds(0, 0, 60, 60);// 第一0是距左边距离，第二0是距上边距离，60分别是长宽
        et_search.setCompoundDrawables(drawable, null, null, null);// 只放左边
    }
    //网络查词完成后回调handleMessage方法
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 111:
                    //判断网络查找不到该词的情况
                    if (words.getSent().length() > 0) {
                        upDateView();
                    } else {
                        searchWords_linerLayout.setVisibility(View.GONE);
                        Toast.makeText(MainActivity.this, "抱歉！找不到该词！", Toast.LENGTH_SHORT).show();
                    }
                    Log.d("测试", "保存成功");
            }
        }
    };
    // 读取words的方法，优先从数据中搜索，没有在通过网络搜索
    public void loadWords(String key) {
        words = wordsAction.getWordsFromSQLite(key);
        if ("" == words.getKey()) {
            String address = wordsAction.getAddressForWords(key);
            HttpUtil.sentHttpRequest(address, new HttpCallBackListener() {
                @Override
                public void onFinish(InputStream inputStream) {
                    WordsHandler wordsHandler = new WordsHandler();
                    ParseXML.parse(wordsHandler, inputStream);
                    words = wordsHandler.getWords();
                    setWords(words);
                   wordsAction.saveWordsMP3(words);
                    wordsAction.saveWords(words);
                    handler.sendEmptyMessage(111);
                }

                @Override
                public void onError() {

                }
            });
        } else {
            setWords(words);
            //加载数据到界面
            upDateView();
        }
    }

    //加载数据到界面
    public void upDateView() {
        if (words.getIsChinese()) {
            searchWords_posAcceptation.setText(words.getFy());
            searchWords_posA_layout.setVisibility(View.GONE);
            searchWords_posE_layout.setVisibility(View.GONE);
        } else {
            searchWords_posAcceptation.setText(words.getPosAcceptation());
            if(words.getPsE()!="") {
                //字符串常规类型格式化
                searchWords_psE.setText(String.format(getResources().getString(R.string.psE), words.getPsE()));
                searchWords_posE_layout.setVisibility(View.VISIBLE);
            }else {
                searchWords_posE_layout.setVisibility(View.GONE);
            }
            if(words.getPsA()!="") {
                //字符串常规类型格式化
                searchWords_psA.setText(String.format(getResources().getString(R.string.psA), words.getPsA()));
                searchWords_posA_layout.setVisibility(View.VISIBLE);
            }else {
                searchWords_posA_layout.setVisibility(View.GONE);
            }
        }
        searchWords_key.setText(words.getKey());
        searchWords_sent.setText(words.getSent());
        searchWords_linerLayout.setVisibility(View.VISIBLE);
    }

    public Words getWords() {
        return words;
    }

    public void setWords(Words words) {
        this.words = words;
    }

    //加载actionbar的菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbar_layout_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.newWords:
                Intent newWords=new Intent(MainActivity.this,VocabularyActivity.class);
                startActivity(newWords);
                break;
            case R.id.add:
                words=getWords();
                //添加给生词本
                vocabularyAction.addToVocabulary(words);
                //添加给操练本
                dataBaseHelper.insertWordInfoToDataBase(words.getKey(),words.getPosAcceptation(),true);
                break;
            case R.id.sentence:
                Intent sentence=new Intent(MainActivity.this,SentenceActivity.class);
                startActivity(sentence);
                break;
            case R.id.recite_translation:
                Intent recite_translation=new Intent(MainActivity.this,ReciteTranslation.class);
                startActivity(recite_translation);
                break;
            case R.id.recite:
                Intent recite=new Intent(MainActivity.this,ReciteWord.class);
                startActivity(recite);
        }
        return super.onOptionsItemSelected(item);
    }

    //显示小窗口
    public void show(View view) {
        // 需要传递小悬浮窗布局，以及根布局的id，启动后台服务
        Intent intent = new Intent(context, FloatWindowService.class);
        intent.putExtra(FloatWindowService.LAYOUT_RES_ID,
                R.layout.float_window_small);
        intent.putExtra(FloatWindowService.ROOT_LAYOUT_ID,
                R.id.small_window_layout);
        startService(intent);
    }
}
