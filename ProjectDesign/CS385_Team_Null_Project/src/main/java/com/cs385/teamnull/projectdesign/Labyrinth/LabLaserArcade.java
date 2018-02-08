package com.cs385.teamnull.projectdesign.Labyrinth;

import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.content.Context;

import com.cs385.teamnull.projectdesign.HighScores;
import com.cs385.teamnull.projectdesign.TextBox;

import static com.cs385.teamnull.projectdesign.Constants.*;

/**
 * The arcade version of the laser game
 * Runs infinitely until the user quits,
 * the lasers progressively get faster until the user hits one.
 * The number of lasers generated is saved as the score.
 *
 * @author Katie Brugha
 * @author student ID : 17186293
 * @version 18-1-2018
 */
public class LabLaserArcade extends LabLevel{
    private LaserObstacleManager laserObstacleManager;
    private TextBox textBox;
    private boolean gameOver = false;
    private long gameOverTime;
    private int high;
    private Context context;
    private String s1;
    private String s2;
    private String s3;
    private String s4;

    /**
     * LabLaserArcade constructor.
     * Locally sets the current high score,
     * sets the text for the textBox,
     * initilises the obstacleManager.
     * @param context
     */
    public LabLaserArcade(Context context){
        super(context);
        this.context = context;
        high = HighScores.topLaserArcade;
        textBox = new TextBox();

        s1 = "                   ";
        s2 = "     Try again!    ";
        s3 = "                   ";
        s4 = "                   ";
        initPlayerPoint = new Point(PLAY_WIDTH /2,PLAY_HEIGHT-100);
        playerPoint = initPlayerPoint;
        player.update(playerPoint);
        laserObstacleManager = new LaserObstacleManager();
    }


    /**
     * Adds the walls for the left and right of the screen
     */
    public void addWalls(){
        labyrinthWalls.addWall(0,0, wallDepth,PLAY_HEIGHT,true);//E
        labyrinthWalls.addWall(PLAY_WIDTH - wallDepth,0, PLAY_WIDTH,PLAY_HEIGHT,true);//W
    }


    /**
     * adds to the reset method-
     * resets the local high score
     * resets the LaserObstacleManager
     */
    @Override
    public void reset(){
        super.reset();
        high = HighScores.topLaserArcade;
        laserObstacleManager = new LaserObstacleManager();
    }

    /**
     * Receives the TouchEvent from the User, through the LabLevelManager
     * @param event - MotionEvent
     */
    @Override
    public void receiveTouch(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                actionDown(event);
                break;
            case MotionEvent.ACTION_MOVE:
                move(event);
                break;
            case MotionEvent.ACTION_UP:
                actionUp(event);
                break;
        }
    }

    /**
     * Overrides the super action down.
     * Now if the game is over action down resets the game after a pause
     * Otherwise actionDown as normal
     */
    @Override
    public void actionDown(MotionEvent event){
        if(!gameOver){
            super.actionDown(event);
        }
        if(gameOver && System.currentTimeMillis() - gameOverTime >=250){
            gameOver = false;
            reset();
        }
    }

    /**
     * Only allows the player to move if the game is not over
     * @param event
     */
    @Override
    public void move(MotionEvent event) {
        if(!gameOver){super.move(event);}
    }

    /**
     * draws all the components to the screen
     * Draws the winning text to the textBox and displays it if won, including the previous and new high scores
     * If lost, draws the losing text plus the score that must be beaten to get a high score
     * @param canvas
     */
    @Override
    public void draw(Canvas canvas) {
        canvas.drawColor(backgroundColour);
        player.draw(canvas);
        labyrinthWalls.draw(canvas);
        canvas.drawText("SCORE : "+ laserObstacleManager.score, scoreTextPlaceX,scoreTextPlaceY, altTextPaint);
        laserObstacleManager.draw(canvas);
        if (gameOver) {
            if(laserObstacleManager.score>=high) {
                s1 = "     YOU WIN!      ";
                s2 = "   You beat : "+ high;
                s3 = "NEW HIGH SCORE :"+((laserObstacleManager.score < 9 ) ? " " : "")+laserObstacleManager.score;
                s4 = "                  ";
                if(laserObstacleManager.score>99){
                    s3 = "NEW HIGH SCORE :";
                    s4 = "        "+laserObstacleManager.score+"       ";
                }

                textBox.setText(s1,s2,s3,s4);
                textBox.setTextColour(Color.GREEN);
                textBox.draw(canvas);
                newHighScore();
            }
            else{
                s1 = "     YOU LOSE      ";
                s2 = "    Try again!     ";
                s3 = "  HIGH SCORE : "+high;
                s4 = "                   ";
                textBox.setText(s1,s2,s3,s4);
                textBox.setTextColour(Color.RED);
                textBox.draw(canvas);
            }
        }
    }

    /**
     * adds updating the laserObstacleManager to the update
     * checks each frame if the user has collided with a laser, setting the gameOver to true
     * The time is saved so the user can only restart after a short break
     */
    @Override
    public void update() {
        if(!gameOver) {
            super.update();
            laserObstacleManager.update();
            if(laserObstacleManager.playerCollide(player)){
                gameOver = true;
                gameOverTime = System.currentTimeMillis();
            }
        }
    }

    /**
     * If the User achieves a high score this is called to save it to sharedPreferences
     */
    public void newHighScore() {
        SharedPreferences sp = context.getSharedPreferences("HighScores", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        HighScores.topLaserArcade = laserObstacleManager.score;
        editor.putInt("LaserArcadeTopScore", laserObstacleManager.score).apply();
        editor.commit();
    }
}
