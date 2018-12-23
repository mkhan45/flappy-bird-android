package com.example.fishi.flappybird;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

import java.util.Timer;
import java.util.TimerTask;

public class Bird {
    private int x, y;
    private boolean status; //dead or alive
    private double rotation;
    int millis = 0;


    public Bird() {
            x = 50;
            y = 500;
            status = true;
            rotation = 0;
        }


        //basically the y velocity decreases by a bit every millisecond but clicking increases it
        public void update (int dy) {
            y += dy;
        }

        public int getX () {
            return x;
        }

        public void setX ( int x){
            this.x = x;
        }

        public int getY () {
            return y;
        }

        public void setY ( int y){
            this.y = y;
        }

        public boolean isAlive () {
            return status;
        }

        public void setStatus ( boolean status){
            this.status = status;
        }

        public double getRotation () {
            return rotation;
        }

        public void setRotation ( double rotation){
            this.rotation = rotation;
        }
    }
