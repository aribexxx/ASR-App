package com.example.myapplication.models;

import com.google.gson.annotations.SerializedName;

/*    status: String 0成功    非0失败
    seq: String 分片序号
    src: String 原文
    tranRes: String 譯文 或者是服务出问题后的message*/

public class TranslationResponse {

    @SerializedName("status")
    private String status;
    @SerializedName("seq")
    private String seq;
    @SerializedName("src")
    private String src;
    @SerializedName("tranRes")
    private String tranRes;

    public TranslationResponse(String status, String seq,String src,String tranRes) {
        this.status=status;
        this.seq=seq;
        this.src=src;
        this.tranRes=tranRes;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSeq() {
        return seq;
    }

    public void setSeq(String seq) {
        this.seq = seq;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getTranRes() {
        return tranRes;
    }

    public void setTranRes(String tranRes) {
        this.tranRes = tranRes;
    }



}
