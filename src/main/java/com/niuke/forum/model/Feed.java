package com.niuke.forum.model;

import com.alibaba.fastjson.JSONObject;

import java.util.Date;

public class Feed {
    private int id;
    private int type;
    private int user_id;
    private Date created_date;
    private String data;
    private JSONObject dataJSON = null;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
        dataJSON = JSONObject.parseObject(data);
    }

    public String get(String key) {
        return dataJSON == null ? null : dataJSON.getString(key);
    }

    public JSONObject getDataJSON() {
        return dataJSON;
    }

    public void setDataJSON(JSONObject dataJSON) {
        this.dataJSON = dataJSON;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getUserId() {
        return user_id;
    }

    public void setUserDd(int user_id) {
        this.user_id = user_id;
    }

    public Date getCreatedDate() {
        return created_date;
    }

    public void setCreateDate(Date create_date) {
        this.created_date = create_date;
    }
}
