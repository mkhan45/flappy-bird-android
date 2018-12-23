package com.example.fishi.flappybird;

public class pipePair {

    final int dist = 1400;
    private int topY, bottomY;
    private int x;

    public pipePair(int screenHeight, int screenWidth){
        bottomY = (int) Math.floor(Math.random() * (screenHeight/2) + .25 * screenHeight);
        topY = bottomY - dist;
        x = screenWidth;
    }


    public void moveX(){
        x -= 10;
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
