package com.example.wordcheck.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;

import com.example.wordcheck.db.WordsSQLiteOpenHelper;
import com.example.wordcheck.kind.Words;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by 此文件打不开 on 2020/3/29.
 */
public class WordsAction {


    /**
     * 本类的实例
     */
    private static WordsAction wordsAction;
    /**
     * Words的表名
     */
    private final String TABLE_WORDS = "Words";
    /**
     * 数据库工具，用于增、删、该、查
     */
    private SQLiteDatabase db;
    private MediaPlayer player = null;
    private MediaPlayer mediaPlayer=new MediaPlayer();
    private AudioTrack audioTrack;
    private byte[] audio;

    /**
     * 私有化的构造器
     */
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
            HttpUtil.sentHttpRequest(addressE, new HttpCallBackListener() {
                @Override
                public void onFinish(InputStream inputStream) {
                    FileUtil.getInstance().writeToSD(filePathE, "E.mp3", inputStream);
               /*   OutputStream outputStream = null;
                    try {
                        Context context=AppContext.getContext();
                        String s=context.getCacheDir().getAbsolutePath();
                     File file=new File(s+"/"+words+"/"+b);
                        outputStream=new FileOutputStream(file);
                        int length;
                        byte[] buffer = new byte[2 * 1024];
                        while ((length = inputStream.read(buffer)) != -1) {
                            //注意这里的length；
                            //利用read返回的实际成功读取的字节数，将buffer写入文件，
                            //这里的length 将避免出现错误的字节，导致保存文件与源文件不一致
                            outputStream.write(buffer,0,length);


                        }
                      //  String db=new String(buffer);
                       // Log.d("测试",db);
                       // playMP3(buffer,length);
                        outputStream.flush();
                        Log.d("测试", "写入成功");
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.d("测试", "写入失败");
                    } finally {
                       try {
                            if (outputStream != null) {
                                outputStream.close();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }*/
                }


                @Override
                public void onError() {

                }
            });
        }
        if (addressA!= "") {
            final String filePathA = words.getKey();
            HttpUtil.sentHttpRequest(addressA, new HttpCallBackListener() {
                @Override
                public void onFinish(InputStream inputStream) {
                    FileUtil.getInstance().writeToSD(filePathA, "A.mp3", inputStream);
              /*     StringBuilder stringBuilder=new StringBuilder();
                    BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
                    try {
                        String a=bufferedReader.readLine();
                        SharePreference.write(addressA,a);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //  OutputStream outputStream = null;

                     /*   Context context=AppContext.getContext();
                        String s= Environment.getDataDirectory().getAbsolutePath();

                        File dir = new File(s+"/"+filePathA);
                        if(!dir.exists()) {
                            dir.mkdirs();
                        }
                        File file= new File(dir, addressA);
                        if (!file.exists()) {
                            file.createNewFile();
                        }
                        outputStream=new FileOutputStream(file);
                        Context context=AppContext.getContext();
                        String s=context.getCacheDir().getAbsolutePath();
                        File file=new File(s+"/"+words+"/"+b);
                        outputStream=new FileOutputStream(file);
                        int length;
                        byte[] buffer = new byte[2 * 1024];
                        while ((length = inputStream.read(buffer)) != -1) {
                          outputStream.write(buffer, 0, length);
                        }
                       outputStream.flush();
                      //  String db=new String(buffer);
                        //playMP3(buffer,length);
                        Log.d("测试", "写入成功");
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.d("测试", "写入失败");
                    } finally {
                        try {
                            if (outputStream != null) {
                                outputStream.close();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
*/}

                @Override
                public void onError() {

                }
            });
        }
    }

    public void playMP3(/*byte[] buffer,int len*/String wordsKey, String ps, Context context ) {

      String fileName =wordsKey+"/"+ ps + ".mp3";
       // Context contexta=AppContext.getContext();
        //String s=contexta.getFilesDir().getAbsolutePath();
        String adrs = FileUtil.getInstance().getPathInSD(fileName);
    /*    this.release();
        this.audioTrack=new AudioTrack(AudioManager.STREAM_MUSIC,44100,AudioFormat.CHANNEL_OUT_STEREO,AudioFormat.ENCODING_PCM_16BIT,len,AudioTrack.MODE_STATIC);
        this.audioTrack.write(buffer,0,len);
        audioTrack.play();*/
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
                //try {
                player = MediaPlayer.create(context, Uri.parse(adrs));
            }catch (Exception e){
                e.printStackTrace();
                Words words = getWordsFromSQLite(wordsKey);
                playWordsMP3(words,ps);
           /*     if (!mediaPlayer.isPlaying()){
                    mediaPlayer.start();
                }else {
                    mediaPlayer.stop();
                    mediaPlayer.start();
                }*/
            }
            //} catch (IOException e) {
             //   e.printStackTrace();
            //}
            Log.d("测试", "播放");
            player.start();
        } else {//没有内容则重新去下载
            Words words = getWordsFromSQLite(wordsKey);
            playWordsMP3(words,ps);
     /*       if (!mediaPlayer.isPlaying()){
                mediaPlayer.start();
            }else {
                mediaPlayer.stop();
                mediaPlayer.start();
            }
        }*/}
    }

    private void playWordsMP3(Words words,String ps) {
        final String addressE = words.getPronE();
        final String addressA = words.getPronA();
        if (ps== "A") {
            final String filePathA = words.getKey();
            HttpUtil.sentHttpRequest(addressA, new HttpCallBackListener() {
                @Override
                public void onFinish(InputStream inputStream) {
                    FileUtil.getInstance().writeToSD(filePathA, "A.mp3", inputStream);
             /*       BufferedReader bufferedReader = null;
                    try {
                        ByteArrayOutputStream out=new ByteArrayOutputStream();
                        byte[] temp=new byte[1024];
                        for (;;){
                            int size=inputStream.read(temp);
                            if (size!=-1){
                                out.write(temp,0,size);
                            }else {
                                break;
                            }
                        }
                        String s=new String(out.toByteArray());
                            mediaPlayer.setDataSource(s);
                            mediaPlayer.prepare();
                        }catch (Exception e){
                            e.printStackTrace();
                        }*/
                }
                    @Override
                    public void onError() {

                    }
                });
            }
        if (ps== "E") {
            final String filePathA = words.getKey();
            HttpUtil.sentHttpRequest(addressE, new HttpCallBackListener() {
                @Override
                public void onFinish(InputStream inputStream) {
                    FileUtil.getInstance().writeToSD(filePathA, "E.mp3", inputStream);
              /*      BufferedReader bufferedReader = null;
                    try {
                        bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"utf-8"));
                        StringBuffer sb=new StringBuffer();
                        String s="";
                        while ((s=bufferedReader.readLine())!=null){
                            sb.append(s).append("\n");
                        }
                        mediaPlayer.setDataSource(sb.toString());
                        mediaPlayer.prepare();
                    }catch (Exception e){
                        e.printStackTrace();*/

                }
                @Override
                public void onError() {

                }
            });
        }
    }
   /* private void release(){
        if (this.audioTrack!=null){
            audioTrack.stop();
            audioTrack.release();
        }
    }*/
}
