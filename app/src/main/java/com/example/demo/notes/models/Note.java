package com.example.demo.notes.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "notes")
public class Note {

    public Note() {
    }

    public final static String NAME_TABLEFIELD_ID = "noteID";
    public final static String NAME_TABLEFIELD_TITLE = "title";
    private final static String NAME_TABLEFIELD_BODY = "body";
    private final static String NAME_TABLEFIELD_PHOTOURL = "url";
    private final static String NAME_TABLEFIELD_DATE = "date";
    public final static String NAME_TABLEFIELD_ISFAVARITE = "isFav";


    @DatabaseField(generatedId = true, columnName = NAME_TABLEFIELD_ID)
    private int id;

    @DatabaseField(canBeNull = false, columnName = NAME_TABLEFIELD_TITLE)
    private String title;

    @DatabaseField(columnName = NAME_TABLEFIELD_BODY)
    private String body;

    @DatabaseField(columnName = NAME_TABLEFIELD_PHOTOURL)
    private String url;

    @DatabaseField(columnName = NAME_TABLEFIELD_DATE)
    private long date;

    @DatabaseField(columnName = NAME_TABLEFIELD_ISFAVARITE)
    private int isFav;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Note(String title, String body, long date) {
        this.title = title;
        this.body = body;
        this.date = date;
        this.isFav = 0;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public int getIsFav() {
        return isFav;
    }

    public void setIsFav(int isFav) {
        this.isFav = isFav;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
