package com.multiable.library.http;

/**
 * 用于Post请求添加body
 * Created by Even on 2015/10/22.
 */
@SuppressWarnings("unused")
public class Param {
    private String key;
    private String value;

    public Param(String key, String value) {

        this.key = key;
        this.value = value;
    }

    public Param() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
