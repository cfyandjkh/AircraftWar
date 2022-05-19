package com.example.aircraftwar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;

import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.application.Game;

public class MainActivity extends AppCompatActivity {
    public static int WINDOW_WIDTH;
    public static int WINDOW_HEIGHT;
    private Game game;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getScreenHW();
        game=new Game(this);
        setContentView(game);
    }

    public void getScreenHW(){
        DisplayMetrics dm =new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        WINDOW_WIDTH =dm.widthPixels;
        Log.i("TAG","screenWidth:"+ WINDOW_WIDTH);


        WINDOW_HEIGHT =dm.heightPixels;
        Log.i("TAG","screenHeight"+ WINDOW_HEIGHT);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN&& Game.getGameOverFlag()==false) {
            HeroAircraft.getInstance().setLocation(event.getX(),event.getY());
        }
        return  true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            this.finish();
        }
        return true;
    }
}