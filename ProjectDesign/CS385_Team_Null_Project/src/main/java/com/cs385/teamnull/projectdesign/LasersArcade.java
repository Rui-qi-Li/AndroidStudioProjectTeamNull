package com.cs385.teamnull.projectdesign;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Window;

import com.cs385.teamnull.projectdesign.Labyrinth.*;
import static android.view.WindowManager.*;
import static com.cs385.teamnull.projectdesign.Constants.*;

/**
 * This activity starts the Labyrinth for the Laser Game
 * @author Katie Brugha
 * @author student ID : 17186293
 * @version 18-1-2018
 */
public class LasersArcade extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(LayoutParams.FLAG_FULLSCREEN, LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Level 4 is the arcade lasers game in the labyrinth
        setContentView(new LabyrinthManager(this,4));
    }

    /**
     * If the back button is called anywhere in the Labyrinth this is called to end the Labyrinth
     */
    @Override
    public void onBackPressed() {
        LabLevelManager.terminate();
        finish();
    }

    /**
     * In addition to the super onPause, calls the pause function in the LabLevelManager
     */
    @Override
    protected void onPause(){
        super.onPause();
        LabLevelManager.pause();
    }

    /**
     * In addition to the super onResume, calls the resume function in the LabLevelManager
     */
    @Override
    public void onResume(){
        super.onResume();
        LabLevelManager.resume();
    }
}
