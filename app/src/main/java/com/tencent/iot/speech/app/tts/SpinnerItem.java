package com.tencent.iot.speech.app.tts;

public class SpinnerItem {
    private int ID = 0;
    private String Value = "";

    public SpinnerItem() {
        ID = 0;
        Value = "";
    }

    public SpinnerItem(int iD, String value) {
        ID = iD;
        Value = value;
    }

    @Override
    public String toString() {

        // 如果传入适配器的对象不是字符串的情况下，直接就使用对象.toString()
        return Value;
    }

    public int getID() {
        return ID;
    }

    public void setID(int iD) {
        ID = iD;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        Value = value;
    }
}
