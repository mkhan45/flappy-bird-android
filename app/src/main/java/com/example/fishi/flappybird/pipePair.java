package com.example.fishi.flappybird;

public class pipePair {

    private int dist;
    private int topY, bottomY;
    private int x;

    public pipePair(int screenHeight, int screenWidth){
        dist = (int) Math.floor(screenHeight/1.5);
        bottomY = (int) Math.floor(Math.random() * (screenHeight/2) + .25 * screenHeight);
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
