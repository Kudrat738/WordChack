package com.example.wordcheck.util;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by 此文件打不开 on 2020/3/29.
 */

public class FileUtil {
    //SD卡的目录
    private String SDPath;
    //app存储的目录
    private String AppPath;
    private static FileUtil fileUtil;

    private FileUtil() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            //如果手机已插入SD卡，且应用程序具有读写SD卡的功能，则返回true

            SDPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
            File fileV = createSDDir(SDPath, "VocabularyBuilder");
            AppPath = fileV.getAbsolutePath() + "/";
        }else{
            Log.d("测试","没有SD卡或权限");
        }
    }

    //单例类FileUtil获取实例方法
    public static FileUtil getInstance() {
        if (fileUtil == null) {
            synchronized (FileUtil.class) {
                if (fileUtil == null) {
                    fileUtil = new FileUtil();
                }
            }
        }
        return fileUtil;
    }

    //创建目录
    public File createSDDir(String path, String dirName) {
        File dir = new File(path + dirName);
        if (dir.exists() && dir.isDirectory()) {
            return dir;
        }
        Log.d("测试", "创建目录");
        dir.mkdir();
        Log.d("测试", "创建目录成功");
        return dir;
    }

    //创建文件
    public File createSDFile(String path, String fileName) {
        File file = new File(path + fileName);
        if (file.exists() && file.isFile()) {
            return file;
        }
        try {
            file.createNewFile();
            Log.d("测试", "创建文件成功");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    //向SD卡中写入文件
    public void writeToSD(String path, String fileName, InputStream inputStream) {
        OutputStream outputStream = null;
        try {
            File dir = createSDDir(AppPath, path);
            File file = createSDFile(dir.getAbsolutePath() + "/", fileName);
            outputStream = new FileOutputStream(file);
            int length;
            byte[] buffer = new byte[2 * 1024];
            while ((length = inputStream.read(buffer)) != -1) {
                //利用read返回的实际成功读取的字节数，将buffer写入文件，
                outputStream.write(buffer, 0, length);
            }
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
        }
    }

    //获取文件在SD卡上绝对路径，如无该文件返回""
    public String getPathInSD(String fileName) {
        File file = new File(AppPath + fileName);
        if (file.exists()) {
            return file.getAbsolutePath();
        }
        return "";
    }

    //递归删除文件夹
    public void deleteFile(File file) {
        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
            } else if (file.isDirectory()) {
                //声明目录下所有的文件 files[]
                File[] files = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    //把每个文件用这个方法进行迭代
                    deleteFile(files[i]);
                }
                file.delete();
            }
        }
    }
}
