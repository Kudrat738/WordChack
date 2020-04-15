package com.example.wordcheck.util;

import com.example.wordcheck.model.Sentence;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Created by 此文件打不开 on 2020/4/5.
 */

public class SentenceHandler extends DefaultHandler {
    private String id;
    private Sentence sentence;
    public Sentence getSentence(){
        return sentence;
    }
    @Override
    public void startDocument() throws SAXException {
        sentence=new Sentence();
    }
    @Override
    public void endDocument() throws SAXException {
super.endDocument();
    }
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        id = localName;
    }
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        String a = new String(ch, start, length);
        //去掉文本中原有的换行
        for (int i = start; i < start + length; i++) {
            if (ch[i] == '\n')
                return;
        }
        if ("sid".equals(id)) {
            sentence.setSid(a.trim());
        } else if ("tts".equals(id)) {
            sentence.setTts(a.trim());
        } else if ("content".equals(id)) {
            sentence.setContent(a.trim());
        } else if ("note".equals(id)) {
            sentence.setNote(a.trim());
        } else if ("love".equals(id)) {
            sentence.setLove(a.trim());
        } else if ("translation".equals(id)) {
            sentence.setTranslation(a.trim());
        } else if ("picture".equals(id)) {
            sentence.setPicture(a.trim());
        } else if ("picture2".equals(id)) {
            sentence.setPicture2(a.trim());
        } else if ("caption".equals(id)) {
            sentence.setCaption(a.trim());
        }
    }
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {

    }
}
