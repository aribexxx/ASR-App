package com.example.myapplication.views.customview.subtitle;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.text.format.Time;
import android.util.AttributeSet;
import android.view.View;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import com.example.myapplication.R;

public class SubtitleView extends View {

    private List<SubtitleBean> list;
    private Paint gPaint;
    private Paint hPaint;
    private int width = 0, height = 0;
    private int currentPosition = 0;
    private MediaPlayer player;
    private int lastPosition = 0;
    private int highLineColor;
    private int lrcColor;
    private int mode = 0;
    public final static int KARAOKE = 1;

    public void setHighLineColor(int highLineColor) {
        this.highLineColor = highLineColor;
    }

    public void setSubTitleColor(int lrcColor) {
        this.lrcColor = lrcColor;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public void setPlayer(MediaPlayer player) {
        this.player = player;
    }

    /**
     * 标准歌词字符串
     *
     * @param lrc
     */
    public void setSubtitle(String lrc) {
        list = SubtitleUtil.parseStr2List(lrc);
    }

    public SubtitleView(Context context) {
        this(context, null);
    }

    public SubtitleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SubtitleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.LrcView);
        highLineColor = ta.getColor(R.styleable.LrcView_hignLineColor, getResources().getColor(R.color.green));
        lrcColor = ta.getColor(R.styleable.LrcView_lrcColor, getResources().getColor(android.R.color.darker_gray));
        //set to karaoke mode 1
        mode = 0;
        ta.recycle();
        gPaint = new Paint();
        gPaint.setAntiAlias(true);
        gPaint.setColor(lrcColor);
        gPaint.setTextSize(36);
        gPaint.setTextAlign(Paint.Align.CENTER);
        hPaint = new Paint();
        hPaint.setAntiAlias(true);
        hPaint.setColor(highLineColor);
        hPaint.setTextSize(36);
        hPaint.setTextAlign(Paint.Align.CENTER);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (width == 0 || height == 0) {
            width = getMeasuredWidth();
            height = getMeasuredHeight();
        }
        if (list == null || list.size() == 0) {
            canvas.drawText("暂无歌词", width / 2, height / 2, gPaint);
            return;
        }

        getCurrentPosition();


        //TODO:replace mediaplayer to get the current time when the client recieve a socket response.

        //int currentMillis = player.getCurrentPosition();
        int currentMillis= (int)System.currentTimeMillis();
        drawSubtitle2(canvas, currentMillis);
        long start = list.get(currentPosition).getStart();
        float v = (currentMillis - start) > 500 ? currentPosition * 80 : lastPosition * 80 + (currentPosition - lastPosition) * 80 * ((currentMillis - start) / 500f);
        setScrollY((int) v);
        if (getScrollY() == currentPosition * 80) {
            lastPosition = currentPosition;
        }
        postInvalidateDelayed(100);
    }

    private void drawSubtitle2(Canvas canvas, int currentMillis) {
        if (mode == 0) {
            for (int i = 0; i < list.size(); i++) {
                if (i == currentPosition) {
                    canvas.drawText(list.get(i).getSubtitle(), width / 2, height / 2 + 80 * i, hPaint);
                } else {
                    canvas.drawText(list.get(i).getSubtitle(), width / 2, height / 2 + 80 * i, gPaint);
                }
            }
        } else {
            for (int i = 0; i < list.size(); i++) {
                canvas.drawText(list.get(i).getSubtitle(), width / 2, height / 2 + 80 * i, gPaint);
            }
            String highLineLrc = list.get(currentPosition).getSubtitle();
            int highLineWidth = (int) gPaint.measureText(highLineLrc);
            int leftOffset = (width - highLineWidth) / 2;
            SubtitleBean lrcBean = list.get(currentPosition);
            long start = lrcBean.getStart();
            long end = lrcBean.getEnd();
            int i = (int) ((currentMillis - start) * 1.0f / (end - start) * highLineWidth);
            if (i > 0) {
                Bitmap textBitmap = Bitmap.createBitmap(i, 80, Bitmap.Config.ARGB_8888);
                Canvas textCanvas = new Canvas(textBitmap);
                textCanvas.drawText(highLineLrc, highLineWidth / 2, 80, hPaint);
                canvas.drawBitmap(textBitmap, leftOffset, height / 2 + 80 * (currentPosition - 1), null);
            }
        }
    }

    public void init() {
        currentPosition = 0;
        lastPosition = 0;
        setScrollY(0);
        invalidate();
    }

    //TODO: treat each socket response as a bean, and set its starttime, and use draw1 method to draw
    private void drawSubtitle1(Canvas canvas) {
        String text = list.get(currentPosition).getSubtitle();
        canvas.drawText(text, width / 2, height / 2, hPaint);

        for (int i = 1; i < 10; i++) {
            int index = currentPosition - i;
            if (index > -1) {
                canvas.drawText(list.get(index).getSubtitle(), width / 2, height / 2 - 80 * i, gPaint);
            }
        }
        for (int i = 1; i < 10; i++) {
            int index = currentPosition + i;
            if (index < list.size()) {
                canvas.drawText(list.get(index).getSubtitle(), width / 2, height / 2 + 80 * i, gPaint);
            }
        }
    }

    private void getCurrentPosition() {
        try {
            //int currentMillis = player.getCurrentPosition();
            int currentMillis=(int)System.currentTimeMillis();
            if (currentMillis < list.get(0).getStart()) {
                currentPosition = 0;
                return;
            }
            if (currentMillis > list.get(list.size() - 1).getStart()) {
                currentPosition = list.size() - 1;
                return;
            }
            for (int i = 0; i < list.size(); i++) {
                if (currentMillis >= list.get(i).getStart() && currentMillis < list.get(i).getEnd()) {
                    currentPosition = i;
                    return;
                }
            }
        } catch (Exception e) {
//            e.printStackTrace();
            postInvalidateDelayed(100);
        }
    }

}
