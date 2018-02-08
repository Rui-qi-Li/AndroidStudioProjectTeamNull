package com.cs385.teamnull.projectdesign.Labyrinth;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.view.MotionEvent;
import android.content.Context;

import com.cs385.teamnull.projectdesign.R;
import com.cs385.teamnull.projectdesign.adventure;

import static com.cs385.teamnull.projectdesign.Constants.*;

import android.media.MediaPlayer;

/**
 * The class for the LabLaserLevel, reached from the LabIntroLevel and starts the TicTacToe game
 *
 * @author Katie Brugha
 * @author student ID : 17186293
 * @version 18-1-2018
 */
public class LabLaserLevel extends LabLevel {
    private LaserObstacleManager laserObstacleManager;
    private long laserTouchTime;
    private long flashTime;
    private int winScore = 10;

    /**
     * Constructor for the LaserLevel,
     * sets the initial location for this level,
     * sets the text for the textBox,
     * sets the music for this level
     * @param context
     */
    public LabLaserLevel(Context context){
        super(context);
        textBox.setText("      PHEW!      ","You escaped!.....","      Nope       ","MORE LASERS,MOVE!");
        laserTouchTime=System.currentTimeMillis();
        laserObstacleManager = new LaserObstacleManager();
        initPlayerPoint = new Point(PLAY_WIDTH /2,PLAY_HEIGHT-100);
        playerPoint = initPlayerPoint;
        tempPlayerPoint = playerPoint;
        player.update(playerPoint);
        if(musicSetting) {
            m_mediaPlayer = MediaPlayer.create(context, R.raw.minigame3); //assign song to the media player
            m_mediaPlayer.start(); //start the media player
            playing = true;
        }
    }

    /**
     * adds the walls for this level,
     * two visible walls on both sides, one invisible wall at the top.
     * The wall on top will become visible at the end of the level,
     * to show that the player has reached the end of a corridor
     */
    @Override
    public void addWalls(){
        labyrinthWalls.addWall(0,0, wallDepth,PLAY_HEIGHT,true);//E
        labyrinthWalls.addWall(PLAY_WIDTH - wallDepth,0, PLAY_WIDTH,PLAY_HEIGHT,true);//W
        labyrinthWalls.addWall(0,0, PLAY_WIDTH, wallDepth,false);//N
    }
    public void addLastWall(){
        labyrinthWalls.makeVisible(2);
    }

    /**
     * adds resetting the LaserObstacleManager to the reset method
     */
    @Override
    public void reset(){
        super.reset();
        laserObstacleManager = new LaserObstacleManager();
    }

    /**
     * receives the motionEvent from the LabLevelManager
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
     * draw method includes drawing the lasers
     * if the player hits the lasers the screen flashes red briefly to indicate a point gained
     * @param canvas
     */
    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        laserObstacleManager.draw(canvas);
        if(laserObstacleManager.playerCollide(player)&&System.currentTimeMillis() - flashTime >=500){
            canvas.drawColor(Color.RED);
            flashTime = System.currentTimeMillis();
        }
    }

    /**
     * checks if the allotted number of lasers have passed to win the level
     * @return - boolean winEvent
     */
    public boolean winEvent(){
        if(laserObstacleManager.score>=winScore){return true;}
        return(false);
    }

    /**
     * Adds several things to the update method.
     * Updates the laserObstacleManager on every frame.
     * If the player hits a laser, the adventureScore is incremented.
     * The player is immune for a half second so they can get out of the laser without gaining any more points
     * The update also checks if the player has won the level and deploys the InteractionBox.
     * When the player hits the InteractionBox the TicTacToe game is initilised and the music is ended
     */
    @Override
    public void update() {
        super.update();
        laserObstacleManager.update();
        if(laserObstacleManager.playerCollide(player)&&System.currentTimeMillis() - laserTouchTime >=500){
            adventure.SCORE++;
            laserTouchTime = System.currentTimeMillis();
            //gameOver = true;
        }
        if(winEvent()&&!won){
            laserObstacleManager.making = false;
            if(laserObstacleManager.empty&&laserObstacleManager.xsandosbox.arrived()){addLastWall();}
            if(laserObstacleManager.ending&&laserObstacleManager.playerxsandosCollide(player)) {
                playing = false;
                m_mediaPlayer.release(); //stop the music
                won = true;
                Intent intent = new Intent().setClass(context, com.cs385.teamnull.projectdesign.TicTacToe.class);
                context.startActivity(intent);
            }
        }

    }
}
