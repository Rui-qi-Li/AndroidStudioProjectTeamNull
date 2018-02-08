package com.cs385.teamnull.projectdesign.Labyrinth;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.view.MotionEvent;

import com.cs385.teamnull.projectdesign.R;
import com.cs385.teamnull.projectdesign.TextBox;
import com.cs385.teamnull.projectdesign.adventure;

import static com.cs385.teamnull.projectdesign.Constants.*;
/**
 * A super class for all the Labyrinth levels -
 * LabIntroLevel, LabLaserLevel, LabSnakeLevel, LabBinaryLevel, and LabLaserArcade
 * Makes a general class all the levels can extend from
 * All the levels are constructed in the LabLevelManager,
 * the current level is stored there also and some methods are called from there on it.
 *
 * @author Katie Brugha
 * @author student ID : 17186293
 * @version 18-1-2018
 */
public class LabLevel{

    protected TextBox textBox;
    protected LabyrinthPlayer player;
    protected LabyrinthPlayer tempPlayer;
    protected Point playerPoint;
    protected Point initPlayerPoint;
    protected Point tempPlayerPoint;
    protected boolean movingPlayer = false;
    protected Context context;
    protected boolean running;
    protected static boolean labyrinthFinished;
    protected LabyrinthWalls labyrinthWalls;
    protected boolean won;
    protected Rect door;
    protected long startTime;
    protected long textBoxTime = 2000;

    protected MediaPlayer m_mediaPlayer; //initialise media player that is used to play music

    /**
     * Constructor for the base class LabLevel
     *
     * @param  context  The context of the Activity that contains the labyrinth (adventure usually)
     */
    public LabLevel(Context context){
        door = new Rect((PLAY_WIDTH - laserPlayerGap)/2,PLAY_HEIGHT- wallDepth,(PLAY_WIDTH + laserPlayerGap)/2,PLAY_HEIGHT);
        labyrinthWalls = new LabyrinthWalls();
        addWalls();
        won = false;
        labyrinthFinished = false;
        player = new LabyrinthPlayer();
        tempPlayer = new LabyrinthPlayer();
        this.context = context;
        running = true;
        textBox = new TextBox();
        startTime = System.currentTimeMillis();
        m_mediaPlayer = MediaPlayer.create(context, R.raw.binarylab);//Initilise the mediaplayer with a temp song
        playing = true;
        initPlayerPoint = new Point(PLAY_WIDTH /2, wallDepth +(wally[0]+wally[1])/2);
        playerPoint = initPlayerPoint;
        tempPlayerPoint = playerPoint;
    }

    /**
     * Populating the default walls for the labyrinth
     * In almost all cases these will be overwritten in the subclasses
     *
     */
    public void addWalls(){//Default add all NSEW labyrinthWalls with
        //labyrinthWalls.addWall(0,0,(PLAY_WIDTH-laserPlayerGap)/2,wallDepth);//NL
        //labyrinthWalls.addWall((PLAY_WIDTH+laserPlayerGap)/2,0,PLAY_WIDTH,wallDepth);//NR
        //labyrinthWalls.addWall(0,0,PLAY_WIDTH,wallDepth);//NL
        labyrinthWalls.addWall(0,PLAY_HEIGHT- wallDepth,(PLAY_WIDTH - laserPlayerGap)/2,PLAY_HEIGHT);//SL
        labyrinthWalls.addWall((PLAY_WIDTH + laserPlayerGap)/2,PLAY_HEIGHT- wallDepth, PLAY_WIDTH,PLAY_HEIGHT);//SR
        labyrinthWalls.addWall(0,0, PLAY_WIDTH, wallDepth);//N
        //labyrinthWalls.addWall(0,PLAY_HEIGHT-wallDepth,PLAY_WIDTH,PLAY_HEIGHT);//S
        labyrinthWalls.addWall(0,0, wallDepth,PLAY_HEIGHT);//E
        labyrinthWalls.addWall(PLAY_WIDTH - wallDepth,0, PLAY_WIDTH,PLAY_HEIGHT);//W

        //These are all the walls  x = 0 to 5, y = 0 to 8
        /*for(int i = 0 ; i<6 ; i++){
            labyrinthWalls.addWall(wallx[i],0,wallx[i]+wallDepth,PLAY_HEIGHT);
        }
        for(int i = 0 ; i<9 ; i++){
            labyrinthWalls.addWall(0,wally[i], PLAY_WIDTH,wally[i]+wallDepth);
        }*/

    }

    /**
     * Checks whether the player has reached the Rect, door
     * If this is true the player has completed the level
     * @return returns true once the player has finished level, false otherwise
     */
    public boolean winEvent(){
        return(Rect.intersects(door, player.getPlayerRectangle()));
    }


    /**
     * empty method is overridden in any base class Lab that has a separate puzzle as part of it
     * For now onl
     */
    public void solvedPuzzle(){;}

    /**
     * Puts the player back to the starting position
     */
    public void reset(){
        playerPoint = initPlayerPoint;
        player.update(playerPoint);
        movingPlayer=false;
    }

    /**
     * Call to fully finish the Labyrinth, when no more Labyrinth levels are left
     */
    public void terminate(){
        playing = false;
        m_mediaPlayer.release(); //stop the music
        labyrinthFinished=true;
    }

    /**
     * Called from the activity in adventure, through the LabLevelManager.
     * If the user navigates away from the screen, pauses the music
     */
    public void pause(){
        if(playing&&musicSetting){m_mediaPlayer.pause();}
    }

    /**
     * Called from the activity in adventure, through the LabLevelManager.
     * If the game is returned to after navigating away, restarts the music
     */
    public void resume(){
        if(playing&&musicSetting){m_mediaPlayer.start();}
    }


    /**
     * Checks if the Labyrinth has been set to running
     * @return - boolean running
     */
    public boolean getRunning(){return running;}

    /**
     * Checks if the Labyrinth has been set to finished
     * @return - boolean labyrinthFinished
     */
    public boolean isFinished(){
        return labyrinthFinished;
    }

    /**
     * receiveTouch called through the LabyrinthManager, the event is passed through
     * Checks which event has happened and passes the event to the relevant function
     * @param event - MotionEvent
     */
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
     * When the User touches the screen the player is allowed to move
     * @param event
     */
    public void actionDown(MotionEvent event){
        movingPlayer = true;
    }

    /**
     * When the User moves the player this is called,
     * the point where the User has touched is saved.
     * It moves the player at a slowed speed, not instantly.
     * The change is calculated by getting the difference between the old and new player coords,
     * and dividing by a constant.
     * The change must then be checked if it is above a cap.
     * The change in movement is capped because without the cap the player can jump straight over walls.
     * The cap means the player is caught on the walls.
     * The new playerPoint is then calculated from the change and assigned to a temporary player.
     * The walls of the maze are then checked to see if the temporary player is in them.
     * The playerConstraint function in the labyrinth walls class is called,
     * which pushes the player out of the wall and returns the point once the temporary player is out of the wall.
     * The new point is assigned to the player, with a slight offset vertically up that helps with the laser game
     * so that they can see the player and the gap better.
     *
     * @param event
     */
    public void move(MotionEvent event){
        if(movingPlayer) {
            int s = 5;
            int offset = PLAY_WIDTH /15; // Offset up to see the player rectangle better
            int cap = PLAY_WIDTH /40;
            float x = event.getX();
            float y = event.getY()-offset;
            float px = playerPoint.x;
            float py = playerPoint.y;
            float changeX = (x-px)/s;
            float changeY = (y-py)/s;
            if(Math.abs(changeX)>cap){
                if(changeX>0){changeX=cap;}
                else{changeX=-cap;}
            }
            if(Math.abs(changeY)>cap){
                if(changeY>0){changeY=cap;}
                else{changeY=-cap;}
            }
            int newx= (int)(px+changeX);
            int newy =(int)(py+changeY);
            tempPlayerPoint.set(newx, newy);
            tempPlayer.update(tempPlayerPoint);
            if(labyrinthWalls.playerCollide(tempPlayer)){//
                playerPoint = labyrinthWalls.playerConstraint(tempPlayerPoint,tempPlayer);
                player.update(playerPoint);
            }
            else{
                playerPoint.set(newx, newy);
                player.update(playerPoint);}
        }
    }


    /**
     * Once the User releases the screen the player stops moving
     *
     * @param event
     */
    public void actionUp(MotionEvent event){
        movingPlayer = false;
    }

    /**
     * Super draw function
     * Draws multiple things to the screen ->
     * First the background colour (Black).
     * Then the current adventure score.
     * Then the player double checking to see if they are in a wall
     * (The check solves a problem where sometimes the draw gets ahead of the update,
     * and then the player jitters in and out of the wall)
     * Then the walls are drawn to the screen
     * Then the textbox if it is within the first few seconds of starting the game
     * @param canvas
     */
    public void draw(Canvas canvas) {
        canvas.drawColor(backgroundColour);
        canvas.drawText("SCORE : "+ adventure.SCORE, scoreTextPlaceX,scoreTextPlaceY, adventureScoreTextPaint);
        if(!labyrinthWalls.playerCollide(player)){//only draw the player if they aren't in a wall
            player.draw(canvas);
        }
        else if(labyrinthWalls.playerCollide(player)){//Double check walls if the player is intersecting
            playerPoint = labyrinthWalls.playerConstraint(playerPoint,player);
            player.update(playerPoint);
            player.draw(canvas);
        }
        labyrinthWalls.draw(canvas);
        if((System.currentTimeMillis()-startTime)<textBoxTime){textBox.draw(canvas);}
    }

    /**
     * updating the level at the most basic just updates the player to the current playerPoint
     */
    public void update() {
        player.update(playerPoint);
    }
}
