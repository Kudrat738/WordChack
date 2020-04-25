package com.example.wordcheck.single;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;

import com.example.wordcheck.activity.AppContext;
import com.example.wordcheck.db.WordsSQLiteOpenHelper;
import com.example.wordcheck.kind.Words;
import com.example.wordcheck.util.FileUtil;
import com.example.wordcheck.util.HttpCallBackListener;
import com.example.wordcheck.util.HttpUtil;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by 此文件打不开 on 2020/3/29.
 */
public class WordsAction {

    private static WordsAction wordsAction;
    //Words的表名
    private final String TABLE_WORDS = "Words";
    private SQLiteDatabase db;
    private MediaPlayer player = null;


    private WordsAction(Context context) {
        WordsSQLiteOpenHelper helper = new WordsSQLiteOpenHelper(context, TABLE_WORDS, null, 1);
        db = helper.getWritableDatabase();
    }


    public static WordsAction getInstance(Context context) {
        //双重效验锁，提高性能
        if (wordsAction == null) {
            synchronized (WordsAction.class) {
                if (wordsAction == null) {
                    wordsAction = new WordsAction(context);
                }
            }
        }
        return wordsAction;
    }
//保存Words数据
    public boolean saveWords(Words words) {
        //判断是否是有效对象，即有数据
        if (words.getSent().length() > 0) {
            ContentValues values = new ContentValues();
            values.put("isChinese", "" + words.getIsChinese());
            values.put("key", words.getKey());
            values.put("fy", words.getFy());
            values.put("psE", words.getPsE());
            values.put("pronE", words.getPronE());
            values.put("psA", words.getPsA());
            values.put("pronA", words.getPronA());
            values.put("posAcceptation", words.getPosAcceptation());
            values.put("sent", words.getSent());
            db.insert(TABLE_WORDS, null, values);
            values.clear();
            return true;
        }
        return false;
    }

//得到指定的Words数据
    public Words getWordsFromSQLite(String key) {
        Words words = new Words();
        Cursor cursor = db.query(TABLE_WORDS, null, "key=?", new String[]{key}, null, null, null);
        //数据库中有
        if (cursor.getCount() > 0) {
            Log.d("测试", "数据库中有");
            if (cursor.moveToFirst()) {
                do {
                    String isChinese = cursor.getString(cursor.getColumnIndex("isChinese"));
                    if ("true".equals(isChinese)) {
                        words.setIsChinese(true);
                    } else if ("false".equals(isChinese)) {
                        words.setIsChinese(false);
                    }
                    words.setKey(cursor.getString(cursor.getColumnIndex("key")));
                    words.setFy(cursor.getString(cursor.getColumnIndex("fy")));
                    words.setPsE(cursor.getString(cursor.getColumnIndex("psE")));
                    words.setPronE(cursor.getString(cursor.getColumnIndex("pronE")));
                    words.setPsA(cursor.getString(cursor.getColumnIndex("psA")));
                    words.setPronA(cursor.getString(cursor.getColumnIndex("pronA")));
                    words.setPosAcceptation(cursor.getString(cursor.getColumnIndex("posAcceptation")));
                    words.setSent(cursor.getString(cursor.getColumnIndex("sent")));
                } while (cursor.moveToNext());
            }
            cursor.close();
        } else {
            Log.d("测试", "数据库中没有");
            cursor.close();
        }

        return words;
    }

//得到网络地址
    public String getAddressForWords(final String key) {
        String address_p1 = "http://dict-co.iciba.com/api/dictionary.php?w=";
        String address_p2 = "";
        String address_p3 = "&key=087DD3B9A803895AB141F83060FA4F56";
        if (isChinese(key)) {
            try {
                //对中文的key进行重新编码，生成正确的网址
                address_p2 = "_" + URLEncoder.encode(key, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {
            address_p2 = key;
        }
        return address_p1 + address_p2 + address_p3;

    }
//判断是不是中文
    public static boolean isChinese(String strName) {
        char[] ch = strName.toCharArray();
        for (int i = 0; i < ch.length; i++) {
            char c = ch[i];
            if (isChinese(c)) {
                return true;
            }
        }
        return false;
    }
    //判断是不是中文
    private static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
            return true;
        }
        return false;
    }


    public void saveWordsMP3(final Words words) {
        final String addressE = words.getPronE();
        final String addressA = words.getPronA();
        if (addressE != "") {
           final String filePathE = words.getKey();
            //发送读音的网络地址
            HttpUtil.sentHttpRequest(addressE, new HttpCallBackListener() {
                @Override
                public void onFinish(InputStream inputStream) {
                    FileUtil.getInstance().writeToSD(filePathE, "E.mp3", inputStream);
                }


                @Override
                public void onError() {

                }
            });
        }
        if (addressA!= "") {
            final String filePathA = words.getKey();
            //发送读音的网络地址
            HttpUtil.sentHttpRequest(addressA, new HttpCallBackListener() {
                @Override
                public void onFinish(InputStream inputStream) {
                    FileUtil.getInstance().writeToSD(filePathA, "A.mp3", inputStream);
                }

                @Override
                public void onError() {

                }
            });
        }
    }

    public void playMP3(String wordsKey, String ps, Context context ) {
//得到该读音在本地保存的文件路径
      String fileName =wordsKey+"/"+ ps + ".mp3";
        String adrs = FileUtil.getInstance().getPathInSD(fileName);
        //判断是不是在播放
        if (player != null) {
            if (player.isPlaying()) {
                player.stop();
            }
            player.release();
            player = null;
        }
        if ( adrs!= "") {//有内容则播放
            try {
                player = new MediaPlayer();
                player = MediaPlayer.create(context, Uri.parse(adrs));
            }catch (Exception e){
                e.printStackTrace();
                Words words = getWordsFromSQLite(wordsKey);
                playWordsMP3(words,ps);
            }
            Log.d("测试", "播放");
            player.start();
        } else {//没有内容则重新去下载
            Words words = getWordsFromSQLite(wordsKey);
            playWordsMP3(words,ps);
       }
    }

    private void playWordsMP3(Words words,String ps) {
        final String addressE = words.getPronE();
        final String addressA = words.getPronA();
        if (ps== "A") {
            final String filePathA = words.getKey();
            //发送读音的网络地址
            HttpUtil.sentHttpRequest(addressA, new HttpCallBackListener() {
                @Override
                public void onFinish(InputStream inputStream) {
                    FileUtil.getInstance().writeToSD(filePathA, "A.mp3", inputStream);
                }
                    @Override
                    public void onError() {

                    }
                });
            }
        if (ps== "E") {
            final String filePathA = words.getKey();
            //发送读音的网络地址
            HttpUtil.sentHttpRequest(addressE, new HttpCallBackListener() {
                @Override
                public void onFinish(InputStream inputStream) {
                    FileUtil.getInstance().writeToSD(filePathA, "E.mp3", inputStream);
                }
                @Override
                public void onError() {

                }
            });
        }
        playMP3(words.getKey(),ps, AppContext.getContext());
    }
}
