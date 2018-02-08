package com.cs385.teamnull.projectdesign;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Class to store static variables used in all the levels in the game,
 * most are initilised in the main menu activity
 *
 * @author Katie Brugha
 * @author student ID : 17186293
 * @version 18-1-2018
 */
public class Constants {
    public static int SCREEN_WIDTH;
    public static int SCREEN_HEIGHT;
    public static int PLAY_HEIGHT;
    public static int PLAY_WIDTH;
    public static float DENSITY;
    public static long INIT_TIME;
    public static int playerSize;
    public static int laserPlayerGap;
    public static int laserObstacleGap;
    public static int laserObstacleDepth;
    public static int wallDepth; //wallDepth
    public static int offset; //offset
    public static int textSize;
    public static int smallTextSize;
    public static int scoreTextPlaceX;
    public static int scoreTextPlaceY;
    public static int widthCorridorSize;
    public static int heightCorridorSize;

    //Maze grid
    public static int[] wallx;
    public static int[] wally;

    public static int obstacleColour = Color.rgb(255,0,0);
    public static int backgroundColour = Color.BLACK;
    public static int playerColour = Color.rgb(0,255,0);
    public static int wallColour = Color.rgb(255,255,255);

    public static boolean musicSetting;
    public static boolean playing;

    public static Paint adventureScoreTextPaint;
    public static Paint altTextPaint;

}
