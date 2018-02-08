package com.cs385.teamnull.projectdesign;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.cs385.teamnull.projectdesign.Labyrinth.LabyrinthManager;

/**
 * This activity contains links to the arcade versions of the games.
 * @author Katie Brugha
 * @author student ID : 17186293
 * @version 18-1-2018
 */
public class arcade extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_arcade);
    }

    /**
     * Button to start the laser arcade game.
     * The numbers from 0 to 4 start the different Labyrinth levels,
     * here 4 refers to the LabLaserArcade.
     * @param view
     */
    public void laserGame(View view) {//Button
        startActivity(new Intent(arcade.this, LasersArcade.class));
    }

    /**
     * Button to start the Binary Arcade game
     * @param view
     */
    public void binaryGame(View view) {//Button
        startActivity(new Intent(arcade.this, BinaryArcade.class));
    }

    /**
     * Button to start the catch arcade game.
     * @param view
     */
    public void catchGame(View view) { // Button
        startActivity(new Intent(arcade.this, CatchGameArcade.class));
    }
    /**
     * Button to start the tic tac toe arcade game.
     * @param view
     */
    public void ticTacToeGame(View view) { startActivity(new Intent(arcade.this, TicTacToeArcade.class)); }
    /**
     * Button to start the snake arcade game.
     * @param view
     */
    public void snakeGame(View view) { //Third Button
        startActivity(new Intent(arcade.this, snakeArcade.class));
    }
}
