package com.cs385.teamnull.projectdesign;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.view.View;
import android.widget.ImageButton;

import static com.cs385.teamnull.projectdesign.Constants.*;

/**
 * GAME CREATED BY TEAM NULL
 * @author Katie Brugha
 * @author student ID : 17186293
 * @author Brid Walsh
 * @author student ID : 17185645
 * @author Ruiqi Li
 * @author student ID : 17251911
 *
 * Main Menu Activity contains the menu that includes the adventuregame mode,
 * the arcade mode, the Leaderboard, an exit button and a volume control
 * If more settings are added later a settings menu would be added and a link would be here.
 *
 * This class also includes the initilisations of all the constants
 *
 * References :
 * Created music using: www.beepbox.co
 */
public class MainMenuActivity extends AppCompatActivity {
    private ImageButton silenceButton,silenceOffButton; //initialise buttons to mute and unmute music

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        SCREEN_WIDTH = dm.widthPixels;
        SCREEN_HEIGHT = dm.heightPixels;
        DENSITY = dm.density;
        setConstants();
        makePaint();

        Context context = this;
        populateInternalScores(context);
        silenceButton = findViewById(R.id.imageButton);  //assign silenceButton to imageButton in the xml file
        silenceOffButton = findViewById(R.id.imageButton2); //assign silenceOffButton to imageButton2 in the xml file
        if(musicSetting){
            silenceButton.setVisibility(View.VISIBLE);
            silenceOffButton.setVisibility(View.INVISIBLE);
        }
        else{
            silenceButton.setVisibility(View.INVISIBLE);
            silenceOffButton.setVisibility(View.VISIBLE);
        }
    }


    /**
     * Button to start the adventure game.
     * @param view
     */
    public void adventure(View view) {//First Button
        startActivity(new Intent(MainMenuActivity.this, adventure.class));
    }

    /**
     * Button to start the arcade game.
     * @param view
     */
    public void arcade(View view) { //Second Button
        startActivity(new Intent(MainMenuActivity.this, arcade.class));
    }

    /**
     * Button to view the Leaderboard.
     * Accessing the leaderboard from this link will not check if there's a new high score
     * @param view
     */
    public void scores(View view) { //third Button
        startActivity(new Intent(MainMenuActivity.this, LeaderBoard.class));
    }

    /**
     * Button to finish and exit the app
     * @param view
     */
    public void exit(View view) { //fourth Button
        finish();
        System.exit(0);
    }

    /**
     * Button to mute the music, if clicked the imageButton is made invisible and the play button is made visible.
     * The music setting is stored in both a static variable and a sharedPreference file
     * @param view
     */
    public void pause(View view) { //when the mute button has been clicked
        musicSetting = false;
        silenceButton.setVisibility(View.INVISIBLE); //set the
        silenceOffButton.setVisibility(View.VISIBLE);

        SharedPreferences sp = MainMenuActivity.this.getSharedPreferences("HighScores", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("MusicSetting",musicSetting).apply();
        editor.commit();
    }

    /**
     * Button to switch the music back on, the image buttons are switched back also.
     *
     * @param view
     */
    public void play(View view) { //when the play music button has been clicked
        musicSetting = true;
        silenceOffButton.setVisibility(View.INVISIBLE); //set the silenceOffButton to be invisible
        silenceButton.setVisibility(View.VISIBLE); //set the silenceButton to be visible

        SharedPreferences sp = MainMenuActivity.this.getSharedPreferences("HighScores", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("MusicSetting",musicSetting).apply();
        editor.commit();
    }


    /**
     * Finds the SharedPreferences file that contains the
     * HighScores and saves them to the internal static variables.
     *
     * @param context
     */
    public static void populateInternalScores(Context context){
        //Find the high scores and save
        SharedPreferences sp = context.getSharedPreferences("HighScores", Context.MODE_PRIVATE);
        musicSetting = sp.getBoolean("MusicSetting",true);
        HighScores.topLaserArcade = sp.getInt("LaserArcadeTopScore",0);
        HighScores.leaderBoardScores[0] = sp.getInt("leaderBoardScores0",9999);
        HighScores.leaderBoardScores[1] = sp.getInt("leaderBoardScores1",9999);
        HighScores.leaderBoardScores[2] = sp.getInt("leaderBoardScores2",9999);
        HighScores.leaderBoardScores[3] = sp.getInt("leaderBoardScores3",9999);
        HighScores.leaderBoardScores[4] = sp.getInt("leaderBoardScores4",9999);

        HighScores.leaderBoardTimes[0] = sp.getLong("leaderBoardTimes0",9999);
        HighScores.leaderBoardTimes[1] = sp.getLong("leaderBoardTimes1",9999);
        HighScores.leaderBoardTimes[2] = sp.getLong("leaderBoardTimes2",9999);
        HighScores.leaderBoardTimes[3] = sp.getLong("leaderBoardTimes3",9999);
        HighScores.leaderBoardTimes[4] = sp.getLong("leaderBoardTimes4",9999);

        HighScores.leaderBoardNames[0] = sp.getString("leaderBoardNames0","");
        HighScores.leaderBoardNames[1] = sp.getString("leaderBoardNames1","");
        HighScores.leaderBoardNames[2] = sp.getString("leaderBoardNames2","");
        HighScores.leaderBoardNames[3] = sp.getString("leaderBoardNames3","");
        HighScores.leaderBoardNames[4] = sp.getString("leaderBoardNames4","");
    }

    /**
     * Make paint objects that will be used in various mini-games
     */
    public void makePaint(){
        adventureScoreTextPaint = new Paint();
        adventureScoreTextPaint.setTypeface(Typeface.create("serif-monospace",Typeface.NORMAL));
        adventureScoreTextPaint.setTextSize(textSize);
        adventureScoreTextPaint.setColor(Color.RED);
        altTextPaint = new Paint();
        altTextPaint.setTypeface(Typeface.create("serif-monospace",Typeface.NORMAL));
        altTextPaint.setTextSize(textSize);
        altTextPaint.setColor(Color.GREEN);

    }


    /**
     * Here the constants are set that the sizes of all the graphics of the games are based on.
     * All constants are based on SCREEN_WIDTH so it will work for different screen resolutions
     * Optimised for the average screen ratio which is 16:9 to cover most phones.
     * The game should stretch to other screen sizes and still function although it may not look as good
     *
     *
     * The labyrinth maze width are all split into 5 corridors and 6 walls.
     * wallDepth = 5*5 + 6 ==> 5 corridors and 6 walls, each corridor is as wide as 5 walls
     * offset = Offset needed because the wall class has a problem where the player gets caught on some x-y intersections
     * maze wall locations are stored in an Array for ease of maze design
     *
     * Uses the 16:9 as basis
     * PLAY_WIDTH should help rescale the game screen if it is more square than 16:9
     * I would center it if it was a bigger difference
     * If the phone isn't 16:9 we want to keep the walls still as is but stretch the corridors
     * 16:9 -> 55 : 31
     * to make the corridor height and width close for 16:9 cut the screen into equal chunks
     * each 55th of the height is similar to each 31st of the width
     * heightCorridorSize 9*5+10=55  9 corridors of size 5 plus the 10 walls
     */
    public void setConstants(){
        playerSize = SCREEN_WIDTH /10;
        laserPlayerGap = playerSize+7*playerSize/10;
        laserObstacleGap = SCREEN_WIDTH /3;
        laserObstacleDepth = SCREEN_WIDTH /55;
        //maze constants
        wallDepth = SCREEN_WIDTH /31;
        offset = 2;
        widthCorridorSize = (SCREEN_WIDTH -6* wallDepth)/5;//Screen width cut into 5 parts with 6 walls
        //maze wall locations
        wallx = new int[6];
        wallx[0]=0;
        for(int i=1;i<6;i++){
            wallx[i]=wallx[i-1]+ widthCorridorSize + wallDepth;
        }
        PLAY_WIDTH = wallx[5]+ wallDepth;
        heightCorridorSize = (SCREEN_HEIGHT-10* wallDepth)/9 ;
        wally = new int[10];
        wally[0]=0;
        for(int i=1;i<10;i++){
            wally[i]=wally[i-1]+ heightCorridorSize + wallDepth;
        }
        PLAY_HEIGHT = wally[8]+ wallDepth;
        textSize = PLAY_WIDTH /15;
        smallTextSize = PLAY_WIDTH /20;
        scoreTextPlaceX = 3* PLAY_WIDTH /10;
        scoreTextPlaceY = SCREEN_HEIGHT-((SCREEN_HEIGHT - PLAY_HEIGHT)/2);
        playing = false;
    }
}
