package com.example.fishi.flappybird;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class gameView extends View{
    private Canvas canvas;
    private Bird bird;
    private Bitmap birdSprite;

    public gameView(Context context, AttributeSet attributes){
        super(context, attributes);
        setFocusable(true);
        setFocusableInTouchMode(true);

        birdSprite = BitmapFactory.decodeResource(getResources(), R.drawable.bird);
    }

    public boolean onTouchEvent(MotionEvent event){
        bird.jump();
        return false;
    }

    Runnable render = new Runnable() {
        @Override
        public void run() {
            canvas.drawColor(Color.WHITE);
            canvas.drawBitmap(birdSprite, bird.getX(), bird.getY(), null);
            bird.update();
        }
    };

}
