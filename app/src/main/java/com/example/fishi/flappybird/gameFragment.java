package com.example.fishi.flappybird;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

public class gameFragment extends Fragment{

    View rootView;
    Boolean ai = false;

    public gameFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.game_layout, container, false);
        FloatingActionButton pauseButton = rootView.findViewById(R.id.pauseButton);
        pauseButton.setOnClickListener(pause);

        if (ai){
            ((gameView) rootView.findViewById(R.id.gameView)).ai();
        }
        return rootView;
    }

    public View.OnClickListener pause = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
           gameView game = (gameView) rootView.findViewById(R.id.gameView);
            Log.i("Paused", "paused");
           game.togglePause();
        }
    };

    public void ai(){
        ai = true;
    }

}
