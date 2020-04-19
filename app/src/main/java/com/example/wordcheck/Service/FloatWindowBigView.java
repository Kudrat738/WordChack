package com.example.wordcheck.Service;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.example.wordcheck.R;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wordcheck.kind.Words;
import com.example.wordcheck.util.HttpCallBackListener;
import com.example.wordcheck.util.HttpUtil;
import com.example.wordcheck.util.ParseXML;
import com.example.wordcheck.util.RecordSQLiteOpenHelper;
import com.example.wordcheck.util.WordsAction;
import com.example.wordcheck.util.WordsHandler;

import java.io.InputStream;

import static android.content.Context.INPUT_METHOD_SERVICE;


/**
 * Created by 此文件打不开 on 2020/4/15.
 */
public class FloatWindowBigView extends LinearLayout {
    // 记录大悬浮窗的宽
    public int viewWidth;
    // 记录大悬浮窗的高
    public int viewHeight;
    public WindowManager.LayoutParams bigWindowParams;
    private  TextView tv_back;
    private Context context;
    private EditText search;
    private RecordSQLiteOpenHelper helper ;
    private SQLiteDatabase db;
    private WordsAction wordsAction;
    private Words words = new Words();
    private boolean ischeck;
    public FloatWindowBigView(Context context) {
        super(context);
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.float_window_big, this);
        View view = findViewById(R.id.big_window_layout);
        viewWidth = view.getLayoutParams().width;
        viewHeight = view.getLayoutParams().height;
        bigWindowParams = new WindowManager.LayoutParams();
// 设置显示的位置，默认的是屏幕中心
        bigWindowParams.x = ScreenUtils.getScreenWidth(context) / 2 - viewWidth
                / 2;
        bigWindowParams.y = ScreenUtils.getScreenHeight(context) / 2
                - viewHeight / 2;
        if (Build.VERSION.SDK_INT >= 26) {
            bigWindowParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        }else {
            bigWindowParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        }
        bigWindowParams.format = PixelFormat.RGBA_8888;
// 设置交互模式
        bigWindowParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
                //| WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        bigWindowParams.gravity = Gravity.LEFT | Gravity.TOP;
        bigWindowParams.width = viewWidth;
        bigWindowParams.height = viewHeight;
        wordsAction = WordsAction.getInstance(context);
        ischeck=true;
        helper= new RecordSQLiteOpenHelper(context);
        init(context);
        initView(context);

    }
    private void initView(final Context context) {
         tv_back = (TextView) findViewById(R.id.tv_back);
        search=(EditText)findViewById(R.id.search);
        Button search_bt=(Button) findViewById(R.id.serech_bt);
         LinearLayout searchWords_fatherLayout = (LinearLayout) findViewById(R.id.big_window_layout);
        searchWords_fatherLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击输入框外实现软键盘隐藏
                InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        });
        search.setFocusable(true);
        search_bt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                // 按完搜索键后将当前查询的关键字保存起来,如果该关键字已经存在就不执行保存
                boolean hasData = hasData(search.getText().toString().trim());
                if (!hasData) {
                    insertData(search.getText().toString().trim());
                }
            }
        });
        tv_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ischeck) {
                    wordsAction.playMP3(words.getKey(), "E", context);
                } else {
                    ischeck = true;
                }
            }
        });
        tv_back.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View view){
                FloatWindowManager.getInstance(context).removeBigWindow();
                ischeck=false;
                return ischeck;
            }
        });
    }
    private boolean hasData(String tempName) {
        Cursor cursor = helper.getReadableDatabase().rawQuery(
                "select id as _id,name from records where name =?", new String[]{tempName});
        //判断是否有下一个
        loadWords(tempName);
        return cursor.moveToNext();
    }
    private void insertData(String tempName) {
        db = helper.getWritableDatabase();
        db.execSQL("insert into records(name) values('" + tempName + "')");
        db.close();
    }
    private void init(final Context context){
        helper=new RecordSQLiteOpenHelper(context);
    }
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 111:
                    //判断网络查找不到该词的情况
                    if (words.getSent().length() > 0) {
                        upDateView();
                    } else {
                        Toast.makeText(context, "抱歉！找不到该词！", Toast.LENGTH_SHORT).show();
                    }
                    Log.d("测试", "tv保存2");
            }
        }
    };

    public Words getWords() {
        return words;
    }

    public void setWords(Words words) {
        this.words = words;
    }

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
                    wordsAction.saveWordsMP3(words);
                    wordsAction.saveWords(words);
                    //wordsAction.playMP3(words.getKey(), "A", MainActivity.this);
                    //  wordsAction.playMP3(words.getKey(), "E", MainActivity.this);
                    handler.sendEmptyMessage(111);
                    setWords(words);
                    upDateView();
                }

                @Override
                public void onError() {

                }
            });
        }else {
            setWords(words);
            upDateView();
        }
    }
    public void upDateView() {
        if (words.getIsChinese()) {
            tv_back.setText(words.getFy());
} else {
            tv_back.setText(words.getPosAcceptation());
        }}}