package com.example.fishi.flappybird;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v4.app.FragmentActivity;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewParent;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

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
    private RectF spriteRect;
    private RectF topPipeRect = new RectF();
    private RectF bottomPipeRect = new RectF();
    private boolean spriteScaled = false;
    private boolean paused;
    private boolean ai = false;
    private boolean responded = false;
    String numberFact = "";

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
            public void surfaceDestroyed(SurfaceHolder holder) {
            }

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                Canvas canvas = holder.lockCanvas();
                if (canvas != null) {
                    draw(canvas);
                    holder.unlockCanvasAndPost(canvas);
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

        });


        Thread updateThread = new Thread() {
            public void run() {
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if (!paused) {
                            try {
                                if (!spriteScaled) {
                                    sprite = Bitmap.createScaledBitmap(sprite, (int) (Math.floor(getWidth() * .15)), (int) (Math.floor(getHeight() * .08)), false); //getWidth and getHeight aren't initialized until later
                                    bottomPipe = Bitmap.createScaledBitmap(bottomPipe, (int) (Math.floor(getWidth() * .3)), (int) (Math.floor(getHeight() * .67)), false);
                                    topPipe = Bitmap.createScaledBitmap(topPipe, (int) (Math.floor(getWidth() * .3)), (int) (Math.floor(getHeight() * .67)), false);
                                    spriteScaled = true;
                                }
                                if (millis % 120 == 0 && millis >= 80) {
                                    if (pipes == null)
                                        pipes = new pipePair(getHeight(), getWidth());
                                    else
                                        pipes.reset();
                                    if (bird.isAlive()) {
                                        score++;
                                    }
                                }

                                update.run();
                            } catch (Exception e) {
                            }
                            millis++;
                        }
                    }
                }, 0, 16);
            }
        };

        updateThread.start();
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
                    dy = -25;
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
                if (millis >= 130 && millis % 2 == 0)
                    dy += 3;
                bird.update(dy);
                pipes.moveX();


                Canvas canvas = holder.lockCanvas();
                canvas.drawPaint(textPaint);
                canvas.drawColor(Color.WHITE);
                canvas.drawBitmap(bottomPipe, pipes.getX(), pipes.getBottomY(), bitmapPaint);
                canvas.drawBitmap(topPipe, pipes.getX(), pipes.getTopY(), bitmapPaint);
                canvas.drawBitmap(sprite, bird.getX(), bird.getY(), bitmapPaint);
                if (!responded)
                    canvas.drawText("Score: " + score, 5, 200, textPaint);
                else {
                    canvas.drawText("Score: " + score, 5, 200, textPaint);
                    TextPaint tp = new TextPaint();
                    tp.setTextSize(50);
                    StaticLayout sl = new StaticLayout(numberFact, tp, getWidth(), Layout.Alignment.ALIGN_LEFT, 1, 0, false);
                    sl.draw(canvas);
                }
                holder.unlockCanvasAndPost(canvas);


                spriteRect = new RectF(bird.getX(), bird.getY(), bird.getX() + sprite.getWidth(), bird.getY() + sprite.getHeight());
                topPipeRect = new RectF(pipes.getX(), pipes.getTopY(), pipes.getX() + topPipe.getWidth(), pipes.getTopY() + topPipe.getHeight());
                bottomPipeRect = new RectF(pipes.getX(), pipes.getBottomY(), pipes.getX() + bottomPipe.getWidth(), getHeight());

                if (ai) {
                    int xDist = (int) bottomPipeRect.left - bird.getX();
                    int bottomYDist = (int) bottomPipeRect.top - bird.getY() - sprite.getHeight();
                    int topYDist = (int) (bird.getY() - topPipeRect.bottom);

                    if (getHeight() - bird.getY() < 450 && dy > -23) {
                        dy -= 25;
                        Log.i("Jumping", "too low");
                    }
                    else if (bottomYDist <= 60 && topYDist > 0 && dy >= 0) {
                        dy -= 25;
                        Log.i("Jumping", "avoid pipe");
                    }
                }


                if (spriteRect.intersect(topPipeRect) || spriteRect.intersect(bottomPipeRect) || bird.getY() > getHeight()) {
                    bird.setStatus(false);
                    ai = false;
                    if (!responded)
                        numberStuff(score);
                    Log.i("Score", "" + score);
                }



            } catch (Exception e) {
            }
        }
    };

    public void togglePause() {
        paused = !paused;
    }

    public void ai() {
        ai = true;
    }

    public void numberStuff(int i){
        String url = "http://numbersapi.com/" + score;
        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("Response", response);
                responded = true;
                textPaint.setTextSize(50);
                numberFact = response;
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                Log.i("Error", error.toString());
            }
        });

        queue.add(request);
    }


}
