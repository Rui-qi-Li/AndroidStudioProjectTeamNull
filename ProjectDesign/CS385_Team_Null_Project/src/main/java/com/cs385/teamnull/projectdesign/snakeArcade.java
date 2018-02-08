package com.cs385.teamnull.projectdesign;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import com.cs385.teamnull.projectdesign.Labyrinth.LabLevelManager;

import static com.cs385.teamnull.projectdesign.Constants.musicSetting;


/**
 * A modified version of the snake activity for the arcade mode
 * Very similar to snake.java except an altered version of the SnakeView is created, SnakeViewArcade
 * Also it is designed to be called directly from the arcade menu instead of as a level in the adventure mode
 *
 *
 * @author Ruiqi Li
 * @author student ID :17251911
 * @author Katie Brugha
 * @author student ID : 17186293
 * @version 18-1-2018
 */

public class snakeArcade extends AppCompatActivity {

    private SnakeViewArcade mSnakeViewArcade;
    private static final int REFRESH = 1;
    public static int REFRESHINTERVAL = 200;
   
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.arg1 == REFRESH) {
                if (mSnakeViewArcade != null)
                    mSnakeViewArcade.invalidate();
            }
        }
    };

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DisplayMetrics dm = new DisplayMetrics();

        getWindowManager().getDefaultDisplay().getMetrics(dm);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mSnakeViewArcade = new SnakeViewArcade(this);
        setContentView(mSnakeViewArcade);
        new Thread(new GameThread()).start();

    }

    class GameThread implements Runnable {

        @Override
        public void run() {
            while(true ){
                Message msg = mHandler.obtainMessage();
                msg.arg1 = REFRESH;
                mHandler.sendMessage(msg);
                try {
                    Thread.sleep(REFRESHINTERVAL);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
