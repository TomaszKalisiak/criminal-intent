package com.ngworks.criminal.intent.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.Date;
import java.util.UUID;

/**
 * Created by TKALISIAK on 2015-08-04.
 */
public class Crime {

    private static final String JSON_ID = "id";
    private static final String JSON_TITLE = "title";
    private static final String JSON_SOLVED = "solved";
    private static final String JSON_DATE = "date";

    private UUID id;
    private String title;
    private Date date;
    private boolean solved;

    public Crime(){
        id = UUID.randomUUID();
        date = new Date();
    }
    public Crime(JSONObject jsonObject) throws JSONException, ParseException{
        id = UUID.fromString(jsonObject.getString(JSON_ID));
        if (jsonObject.has(JSON_TITLE)) {
            title = jsonObject.getString(JSON_TITLE);
        }
        date = new Date(jsonObject.getLong(JSON_DATE));
    }

    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isSolved() {
        return solved;
    }

    public void setSolved(boolean solved) {
        this.solved = solved;
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        json.put(JSON_ID, id.toString());
        json.put(JSON_TITLE, title);
        json.put(JSON_SOLVED, solved);
        json.put(JSON_DATE, date.getTime());
        return json;
    }
    @Override
    public String toString() {
        return title;
    }
}
