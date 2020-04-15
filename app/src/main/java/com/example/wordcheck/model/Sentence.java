package com.example.wordcheck.model;

/**
 * Created by 此文件打不开 on 2020/4/5.
 */

public class Sentence {
    private String sid;
    private String tts;
    private String content;
    private String note;
    private String love;
    private String translation;
    private String picture;
    private String picture2;
    private String caption;

    public Sentence(String sid, String tts, String content, String note, String love, String translation, String picture, String picture2, String caption) {
        this.sid = sid;
        this.tts = tts;
        this.content = content;
        this.note = note;
        this.love = love;
        this.translation = translation;
        this.picture = picture;
        this.picture2 = picture2;
        this.caption = caption;
    }

    public Sentence(){
        this.sid="";
        this.tts = "";
        this.content ="";
        this.note = "";
        this.love = "";
        this.translation = "";
        this.picture = "";
        this.picture2 = "";
        this.caption = "";

    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getTts() {
        return tts;
    }

    public void setTts(String tts) {
        this.tts = tts;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getLove() {
        return love;
    }

    public void setLove(String love) {
        this.love = love;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getPicture2() {
        return picture2;
    }

    public void setPicture2(String picture2) {
        this.picture2 = picture2;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }
}
