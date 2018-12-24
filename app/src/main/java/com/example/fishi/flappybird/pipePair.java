package com.example.fishi.flappybird;

import android.util.Log;

public class pipePair {

    private int dist;
    private int topY, bottomY;
    private int x;

    public pipePair(int screenHeight, int screenWidth){
        dist = (int) (screenHeight/1.15);
        bottomY = (int) Math.floor(Math.random() * (screenHeight/2) + .3 * screenHeight);
        topY = bottomY - dist;
        x = screenWidth;
    }


    public void moveX(){
        x -= 8;
    }

    public int getX(){
        return x;
    }

    public int getTopY() {
        return topY;
    }

    public int getBottomY() {
        return bottomY;
    }
}
