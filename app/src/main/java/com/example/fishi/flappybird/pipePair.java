package com.example.fishi.flappybird;

public class pipePair {

    private int dist;
    private int topY, bottomY;
    private int x;

    public pipePair(int screenHeight, int screenWidth){
        dist = (int) 425 * 4 + 250; //300 is pipe height
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
