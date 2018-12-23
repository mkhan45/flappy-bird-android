package com.example.fishi.flappybird;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;

public class gameView extends View {

    private Paint paint;
    private android.graphics.Path path;
    private Canvas canvas;
    private Bitmap bitmap;
    private Paint bitmapPaint;
    private int width;
    private int height;
    private Bird bird;
    private boolean blur;
    private Bitmap sprite;
    private int x = 0;
    private int millis = 0;

    public gameView(Context context, AttributeSet attributes) {
        super(context, attributes);
        setFocusable(true);
        setFocusableInTouchMode(true);
        path = new Path();
        bird = new Bird();
        sprite = BitmapFactory.decodeResource(getResources(), R.drawable.bird);
        bitmapPaint = new Paint(Paint.DITHER_FLAG);
        if (paint == null)
            setPaint();

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                update.run();
                try {
                    postInvalidate();
                }catch (Exception e){}
                millis++;
            }
        }, 0, 1);

        Thread renderThread = new Thread(update);
        renderThread.start();

    }


    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
        if (bitmap == null)
            bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
    }

    public boolean onTouchEvent(MotionEvent event) {
        float pointX = event.getX();
        float pointY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                path.reset();
                path.moveTo(pointX, pointY);
                bird.jump();
                millis = 0;
                x = 0;
                break;
        }
        postInvalidate();
        return true;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bmp) {
        bitmap = bmp.copy(Bitmap.Config.ARGB_8888, true);
    }

    public void changeBitmap(Bitmap bmp) {
        int w = getWidth();
        int h = getHeight();
        Rect src = new Rect(0, 0, bmp.getWidth() - 1, bmp.getHeight() - 1);
        Rect dest = new Rect(0, 0, width - 1, height - 1);
        canvas.drawBitmap(bmp, src, dest, bitmapPaint);
    }


    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(bitmap, 0, 0, bitmapPaint);
    }

    private void setPaint() {
        Log.i("setting paint", "");

        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(5);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStyle(Paint.Style.STROKE);
    }

    void setPaintColor(int c) {
        paint.setColor(c);
    }

    public void reset() {
        canvas.drawColor(Color.WHITE);
    }


    Runnable update = new Runnable() {
        @Override
        public void run() {
            try {
                if (millis % 10 == 0){
                    x ++;
                }
                bird.update(x);
                reset();
                canvas.drawBitmap(sprite, bird.getX(), bird.getY(), bitmapPaint);
                onDraw(canvas);
            }catch (Exception e){}
        }
    };



}
