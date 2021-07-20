package com.example.myapplication.utils;

import java.util.Iterator;
import java.util.Map;

public class StringBuild {
    public static String buildMessage(Map<String, String> msg) {

        StringBuffer stringBuffer = new StringBuffer();
        Iterator<Map.Entry<String, String>> iter = msg.entrySet().iterator();
        while (iter.hasNext()) {
            String value = iter.next().getValue();
            stringBuffer.append(value + "\r\n");
        }
        return stringBuffer.toString();
    }
}
