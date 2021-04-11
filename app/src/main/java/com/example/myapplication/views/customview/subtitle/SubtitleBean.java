package com.example.myapplication.views.customview.subtitle;

public class SubtitleBean {
    private String subtitle;
    private long start;
    private long end;
    public SubtitleBean() {
    }

    public SubtitleBean(String text, long start, long end) {
        this.subtitle = text;
        this.start = start;
        this.end = end;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }
}
