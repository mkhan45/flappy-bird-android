package com.example.fishi.flappybird;

import android.util.Log;

public class pipePair {

    private int dist;
    private int topY, bottomY;
    private int x;
    private int height, width;

    public pipePair(int screenHeight, int screenWidth){
        dist = (int) (screenHeight/1.15);
        bottomY = (int) Math.floor(Math.random() * (screenHeight/2) + .3 * screenHeight);
        topY = bottomY - dist;

        x = screenWidth;
        height = screenHeight;
        width = screenWidth;
    }


    public void moveX(){
        x -= 12;
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

    public void reset(){
        x = width;
        dist = (int) (height/1.15);
        bottomY = (int) Math.floor(Math.random() * (height/2) + .3 * height);
        topY = bottomY - dist;
    }
}
