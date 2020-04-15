package com.example.wordcheck;

import android.app.Activity;
import android.example.wordcheck.R;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wordcheck.model.Sentence;
import com.example.wordcheck.util.HttpCallBackListener;
import com.example.wordcheck.util.HttpUtil;
import com.example.wordcheck.util.ParseXML;
import com.example.wordcheck.util.SentenceHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by 此文件打不开 on 2020/4/5.
 */

public class SentenceActivity extends Activity {
    private TextView caption,content,note;
    private ImageView picture;
    private Sentence sentence=new Sentence();
    private String address="http://open.iciba.com/dsapi";
    private String mp3;
    private  Bitmap bitmap;
    private   Drawable drawable;
    private MediaPlayer mediaPlayer=new MediaPlayer();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sentence);
        caption=(TextView)findViewById(R.id.caption);
        content=(TextView)findViewById(R.id.content);
        note=(TextView)findViewById(R.id.note);
        picture=(ImageView)findViewById(R.id.picture);
        drawable=null;
       download();
        picture.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (!mediaPlayer.isPlaying()){
                    mediaPlayer.start();
                }else {
                    mediaPlayer.stop();
                    mediaPlayer.start();
                }
            }
        });

    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        if (mediaPlayer!=null){
            mediaPlayer.stop();
            mediaPlayer.release();
        }

    }

    public Drawable getDrawable() {
        return drawable;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }

    public void download() {
        HttpUtil.sentHttpRequest(address, new HttpCallBackListener() {
            BufferedReader reader=null;
            @Override
            public void onFinish(InputStream inputStream) {
                try {
                    reader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null){
                     response.append(line);
                    }
                    showResponse(response.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    if (reader!=null){
                        try {
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            @Override
            public void onError() {
                Log.d("测试", "得到数据失败");
            }
        });

    }

    private void showResponse( final String s) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject json=new JSONObject(s);
                    //JSONArray jsonArray = new JSONArray(s);
                   // JSONObject json =jsonArray.getJSONObject(0);
                    caption.setText(json.getString("caption"));
                    getHttpBitmap(json.getString("picture2"));
                  // Bitmap bit=getBitmap();
                  mp3=json.getString("tts");
                    initMediaPlayer(mp3);
                    picture.setImageDrawable(drawable);
                    content.setText(json.getString("content"));
                    note.setText(json.getString("note"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }  });
    }

    private void initMediaPlayer(String mp3) {
        try {
            mediaPlayer.setDataSource(mp3);
            mediaPlayer.prepare();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public void getHttpBitmap(String uri) {
        HttpUtil.sentHttpRequest(uri, new HttpCallBackListener() {
            @Override
            public void onFinish(InputStream inputStream) {
                if (inputStream.equals(null)){
                    Log.d("测试","返回错误");
                }else{
                    Log.d("测试","返回成功");
                }
                drawable=Drawable.createFromStream(inputStream,null);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        picture.post(new Runnable() {
                            @Override
                            public void run() {


                                picture.setImageDrawable(drawable);
                            }
                        });
                    }
                }).start();
                if (drawable.equals(null)){
                    Log.d("测试","de错误");
                }else {
                    Log.d("测试","de成功");
                }
            /*    bitmap=BitmapFactory.decodeStream(inputStream);
                if (bitmap.equals(null)){
                    Log.d("测试","bitmap错误");
                }else {
                    Log.d("测试","bitmap成功");
                }
                setBitmap(bitmap); */
    }
            @Override
        public void onError() {
            Log.d("测试", "得到图片数据失败");
        }
    });
}


}
