package com.example.wordcheck.util;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import android.example.wordcheck.R;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.xml.parsers.SAXParserFactory;
import android.example.wordcheck.R;
import android.util.Log;
/**
 * Created by 此文件打不开 on 2020/3/29.
 */
public class ParseXML {
    /**
     * 使用SAX解析XML的方法
     */
    public static void parse(DefaultHandler handler, InputStream inputStream) {
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader reader = new BufferedReader(inputStreamReader);
            SAXParserFactory factory = SAXParserFactory.newInstance();
            XMLReader xmlReader = factory.newSAXParser().getXMLReader();
            xmlReader.setContentHandler(handler);
            xmlReader.parse(new InputSource(reader));
        } catch (Exception e) {
            Log.d("测试", "得到数据失败");

            e.printStackTrace();
        }
    }
}
