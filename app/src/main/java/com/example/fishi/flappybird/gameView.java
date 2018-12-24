package com.example.fishi.flappybird;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewParent;

import java.util.Timer;
import java.util.TimerTask;

public class gameView extends SurfaceView {

    private Bitmap bitmap;
    private Paint bitmapPaint;
    private Bird bird;
    private Bitmap sprite;
    private SurfaceHolder holder;
    private int dy = 0;
    private int millis = 0;
    private pipePair pipes;
    private Bitmap bottomPipe;
    private Bitmap topPipe;
    private int score = -1;
    private Paint textPaint;

    public gameView(final Context context, AttributeSet attributes) {
        super(context, attributes);
        setFocusable(true);
        setFocusableInTouchMode(true);
        bird = new Bird();
        sprite = BitmapFactory.decodeResource(getResources(), R.drawable.bird);

        bottomPipe = BitmapFactory.decodeResource(getResources(), R.drawable.pipe);
        Matrix transform = new Matrix();
        transform.preScale(1.0f, -1.0f);
        topPipe = Bitmap.createBitmap(bottomPipe, 0, 0, bottomPipe.getWidth(), bottomPipe.getHeight(), transform, true);

        bitmapPaint = new Paint(Paint.DITHER_FLAG);
        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextSize(200);



       holder = getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) { }

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                Canvas canvas = holder.lockCanvas();
                if (canvas != null) {
                    draw(canvas);
                    holder.unlockCanvasAndPost(canvas);
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) { }

        });



        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Thread updateThread = new Thread(update);
                try {
                    if(millis % 125 == 0 && millis >= 125) {
                        pipes = new pipePair(getHeight(), getWidth());
                        if(bird.isAlive()) {
                            score++;
                        }
                    }

                    if (bird.getY() <= getHeight() + 10)
                        updateThread.start();
                }catch (Exception e){}
                millis++;


            }
        }, 0, 16);


    }


//    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
//        super.onSizeChanged(w, h, oldw, oldh);
//        width = w;
//        height = h;
//        if (bitmap == null)
//            bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
//        canvas = new Canvas(bitmap);
//    }

    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (bird.isAlive())
                    dy = -15;
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




    Runnable update = new Runnable() { //draws and updates bird and pipes
        @Override
        public void run() {
            try {
                if(millis >= 125 && millis % 2 == 0)
                   dy += 1;
                bird.update(dy);
                pipes.moveX();
                pipes.moveX();


                Canvas canvas = holder.lockCanvas();
                canvas.drawPaint(textPaint);
                canvas.drawColor(Color.WHITE);
                canvas.drawBitmap(bottomPipe, pipes.getX(), pipes.getBottomY(), bitmapPaint);
                canvas.drawBitmap(topPipe, pipes.getX(), pipes.getTopY(), bitmapPaint);
                canvas.drawBitmap(sprite, bird.getX(), bird.getY(), bitmapPaint);
                canvas.drawText("Score: " + score, 200, 200, textPaint);
                holder.unlockCanvasAndPost(canvas);


                int birdNoseX = (int) Math.floor(bird.getX() + sprite.getWidth()/2);
                boolean inRange = (pipes.getBottomY() > bird.getY() + sprite.getHeight()/2) && (bird.getY() > pipes.getTopY() + topPipe.getHeight());
                if (pipes.getX() - birdNoseX < 30 && !inRange)
                    bird.setStatus(false);

            }catch (Exception e){}
        }
    };


}
