package com.cs385.teamnull.projectdesign.Labyrinth;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.ArrayList;

import static com.cs385.teamnull.projectdesign.Constants.*;

/**
 * Class to manage the laser objects.
 * This manager is used both for the LabLaserLevel and LabLaserArcade games.
 * In both games a laser travels down the screen and the player must try to get through a break somewhere in the laser.
 * The location of the gap in the laser generates randomly,
 * and the speed at which they move at speeds up over time.
 * This class also manages the xsandosbox, an object made from Rects that introduces the next game in LabLaserLevel
 *
 * reference : Adapted from https://www.youtube.com/playlist?list=PL2xjPbQaM7JZ_FmXwTAesiAciHEPlGmiW
 *
 * @author Katie Brugha
 * @author student ID : 17186293
 * @version 18-1-2018
 */
public class LaserObstacleManager {
    public int score;
    private ArrayList<LaserObstacle> laserObstacles;
    public InteractionBox xsandosbox;
    private long frameTime;
    private long managerStartTime;
    private boolean running;
    public boolean making;
    public boolean ending;
    private int obstaclesNum = 0;
    public boolean empty;

    /**
     * Constructor for the laser manager.
     * ending is a boolean that is false until the game is completed in the adventure mode.
     * When ending, no more lasers are generated.
     * empty is a boolean that says if the lasers have all left the screen or not.
     * When the screen is empty of lasers the final wall is made visible behind the interaction box.
     * This animation looks as if the player has discovered the TicTacToe board at the end of a corridor.
     * The lasers are all stored in an ArrayList.
     */
    public LaserObstacleManager(){
        ending = false;
        empty = false;
        making = true;
        score = 0;
        running = true;

        frameTime = managerStartTime = System.currentTimeMillis();
        laserObstacles = new ArrayList<>();
        addLaser();
    }

    /**
     * Checks all of the lasers in the ArrayList to see if the player has hit any of them
     * @param player - current player object
     * @return - returns true if the player is currently in any of the lasers
     */
    public boolean playerCollide(LabyrinthPlayer player){
        for(LaserObstacle ob : laserObstacles){
            if(ob.playerCollide(player))return true;
        }
        return false;
    }

    /**
     * Checks if the player has reached the TicTacToe board
     * @param player - current player object
     * @return - returns true when the player reaches the interaction Rect in the InteractionBox class
     */
    public boolean playerxsandosCollide(LabyrinthPlayer player){
        return(xsandosbox.playerCollide(player));
    }

    /**
     * adds a laser to the screen.
     * Randomly generates where the gap in the laser goes.
     * Increments the number of obstacles.
     */
    private void addLaser(){
        obstaclesNum++;
        int xStart = (int)(Math.random()*(PLAY_WIDTH - laserPlayerGap -2* wallDepth))+ wallDepth;
        laserObstacles.add(new LaserObstacle(laserObstacleDepth,obstacleColour,xStart,0, laserPlayerGap));
    }


    /**
     * update function.
     * This is called every game update, every frame
     * Controls the making and moving of the lasers and the interaction box
     * Distance to move the lasers is calculated by getting the time between frames and multiplying by speed
     */
    public void update(){
        if(!ending&&!making){//if the game has just stopped making lasers create the interaction box
            ending = true;
            xsandosbox = new InteractionBox();
        }
        //if the last laser is past the distance between lasers do this
        if(laserObstacles.get(laserObstacles.size() - 1).getRectangle().top> laserObstacleGap){
            if(making){//first check if more lasers are wanted
                addLaser();
                //now check if there are plenty of lasers and start deleting them as more are added
                if(laserObstacles.size()>10){laserObstacles.remove(laserObstacles.get(laserObstacles.size()-9));}
            }
        }
        //if the game isn't making any more lasers and the last one is out of the screen empty is true
        if(!making&&(laserObstacles.get(laserObstacles.size()-1).getRectangle().top>PLAY_HEIGHT)){
            empty=true;
        }
        //frameTime is the start of the frame
        //making sure starTime is correct, it should always be less than INIT_TIME
        if (frameTime < INIT_TIME) {
            frameTime = INIT_TIME;
        }
        //time since the last frameTime, should be equivalent to the time between frames
        int elapsedTime = (int) (System.currentTimeMillis() - frameTime);
        //reset the frameTime
        frameTime = System.currentTimeMillis();
        //speeding up over time
        //speed is based on the time because the time between frames isn't constant
        //also based on PLAY_HEIGHT so the speed is constant over devices
        float speed = (float) (Math.sqrt(1 + (frameTime - managerStartTime) / 10000.0)) * PLAY_HEIGHT / 5000.0f;
        for (LaserObstacle ob : laserObstacles) {
            ob.incrementY(speed * elapsedTime);
        }
        if(ending&&(!xsandosbox.arrived())){xsandosbox.incrementBox((int)(speed*elapsedTime)) ;}//increments the box until it is far enough down and the lasers are gone
        score = obstaclesNum;
    }

    /**
     * Draw method for all the lasers and the InteractionBox
     * @param canvas
     */
    public void draw(Canvas canvas){
        if(ending){xsandosbox.draw(canvas);}
        for(LaserObstacle laser : laserObstacles){
            if(laser.getRectangle().bottom<PLAY_HEIGHT){laser.draw(canvas);}
        }
    }

}

/**
 * A graphic made to introduce the TicTacToe game.
 * It is made out of a series of Rect objects designed to look like a TicTacToe board.
 * The Rects all proceed down the screen after the lasers game is completed.
 * The box stops a third of the way down the screen and the game waits for the user to reach it
 * The interaction rectangle is larger than the visible box so the player is sent to the TicTacToe game
 * when they get close to the box instead of when they reach it
 *
 * @author Katie Brugha
 * @author student ID : 17186293
 * @version 18-1-2018
 */
class InteractionBox{
    private ArrayList<Rect> miniboxes;
    private Rect rectangleIn;
    private Rect rectangleOut;
    private Rect rectangleInteract;
    private int miniBoxSize = PLAY_WIDTH /15;
    private int gaps = PLAY_WIDTH /100;
    private int rim = PLAY_WIDTH /100;
    private int step = PLAY_WIDTH /15;
    private int boxSize;
    private int largeBoxSize;
    private int interactBoxSize;
    private int out= PLAY_WIDTH;

    /**
     * Constructor for the interaction box that places it vertically out of the screen
     * mini box is the initial size that the rest of it is calculated from .
     * These are the squares on the board.
     * gaps is the distance between the squares and the rim
     * step is how large around the box the player can interact with
     *
     */
    public InteractionBox(){
        boxSize = (miniBoxSize*3)+(gaps*4);
        largeBoxSize =  (miniBoxSize*3)+(gaps*4)+(rim*2);
        interactBoxSize = (miniBoxSize*3)+(gaps*4)+(step*2);

        //ltrb
        rectangleIn = new Rect((PLAY_WIDTH -boxSize)/2,-gaps-out,(PLAY_WIDTH +boxSize)/2,boxSize-gaps-out);//inner box
        rectangleOut =  new Rect((PLAY_WIDTH -largeBoxSize)/2,-gaps-rim-out,(PLAY_WIDTH +largeBoxSize)/2,largeBoxSize-gaps-rim-out);//outer box
        rectangleInteract =  new Rect((PLAY_WIDTH -interactBoxSize)/2,-gaps-step-out,(PLAY_WIDTH +interactBoxSize)/2,interactBoxSize-step-gaps-out);//outer box

        miniboxes = new ArrayList<>();//The others could go in this array but I would prefer them separate
        int xl = (PLAY_WIDTH -miniBoxSize)/2-miniBoxSize-gaps;
        int xm = (PLAY_WIDTH -miniBoxSize)/2;
        int xr = (PLAY_WIDTH +miniBoxSize)/2+gaps;
        int yt = -out;
        int ym = miniBoxSize+gaps-out;
        int yb = (miniBoxSize+gaps)*2-out;

        miniboxes.add(new Rect(xl,yt,xl+miniBoxSize,yt+miniBoxSize));
        miniboxes.add(new Rect(xm,yt,xm+miniBoxSize,yt+miniBoxSize));
        miniboxes.add(new Rect(xr,yt,xr+miniBoxSize,yt+miniBoxSize));

        miniboxes.add(new Rect(xl,ym,xl+miniBoxSize,ym+miniBoxSize));
        miniboxes.add(new Rect(xm,ym,xm+miniBoxSize,ym+miniBoxSize));
        miniboxes.add(new Rect(xr,ym,xr+miniBoxSize,ym+miniBoxSize));

        miniboxes.add(new Rect(xl,yb,xl+miniBoxSize,yb+miniBoxSize));
        miniboxes.add(new Rect(xm,yb,xm+miniBoxSize,yb+miniBoxSize));
        miniboxes.add(new Rect(xr,yb,xr+miniBoxSize,yb+miniBoxSize));
    }

    /**
     * Draws all the Rects that make up the box to the screen.
     * They are set in turn to black or white to create an image of a TicTacToe board.
     * @param canvas
     */
    public void draw(Canvas canvas){
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setColor(Color.BLACK);
        canvas.drawRect(rectangleOut,paint);
        paint.setColor(Color.WHITE);
        canvas.drawRect(rectangleIn,paint);
        paint.setColor(Color.BLACK);
        for(Rect box : miniboxes) {
            canvas.drawRect(box, paint);
        }
    }


    /**
     * The box proceeds down the screen until this method returns false.
     *
     * @return - returns false until the box is a third of the way down the screen
     */
    public boolean arrived(){
        if(rectangleIn.centerY()>SCREEN_HEIGHT/3){return true;}
        return false;
    }


    /**
     * Moves all the boxes together the number of pixels specified
     *
     * @param y - pixels for the boxes to move
     */
    public void incrementBox(int y){
        rectangleInteract.top += y;
        rectangleInteract.bottom += y;
        rectangleIn.top += y;
        rectangleIn.bottom += y;
        rectangleOut.top += y;
        rectangleOut.bottom += y;
        for(Rect box : miniboxes) {
            box.top += y;
            box.bottom += y;
        }
    }

    /**
     * Method that returns true once the player reaches the rim of the visible box.
     *
     * @param player - Object that represents the player
     * @return - True once the player reaches the box
     */
    public boolean playerCollide(LabyrinthPlayer player){
        return(Rect.intersects(rectangleInteract, player.getPlayerRectangle()));
    }
}

/**
 * Class for the LaserObstacles found in both the adventure laser game and the arcade version
 * stores the 2 rectangles that represent the sides of the lasers and methods to draw, move and interact with them
 *
 * @author Katie Brugha
 * @author student ID : 17186293
 * @version 18-1-2018
 */
class LaserObstacle{

    private Rect rectangle;
    private Rect rectangle2;
    private int colour;


    /**
     * Constructor for each individual laser that is sent towards the player
     * @param rectHeight -
     * @param colour - Colour of the laser
     * @param startX - Where the gap in the laser starts
     * @param startY - Where the laser starts on the screen
     * @param playerGap - how large the gap is for the player to get through
     */
    public LaserObstacle(int rectHeight, int colour, int startX, int startY, int playerGap){
        this.colour=colour;
        rectangle = new Rect(wallDepth,startY,startX,startY+rectHeight);//left laser
        rectangle2 =  new Rect(startX+playerGap,startY, PLAY_WIDTH - wallDepth,startY+rectHeight);//right rectangle
    }

    public Rect getRectangle(){
        return rectangle;
    }

    /**
     * Moves both sides of the laser the specified pixels
     *
     * @param y - pixels for the laser to move
     */
    public void incrementY(float y){
        rectangle.top += y;
        rectangle.bottom += y;
        rectangle2.top += y;
        rectangle2.bottom += y;
    }

    /**
     * Checks whether the player has collided with this laser
     * @param player - The player object
     * @return - Returns a boolean indicating if the player is currently in this laser
     */
    public boolean playerCollide(LabyrinthPlayer player){
        return(Rect.intersects(rectangle, player.getPlayerRectangle()) || Rect.intersects(rectangle2, player.getPlayerRectangle()));
    }

    /**
     * Draws the 2 Recs that represent the laser to the screen
     * @param canvas
     */
    public void draw(Canvas canvas){
        Paint paint = new Paint();
        paint.setColor(colour);
        canvas.drawRect(rectangle,paint);
        canvas.drawRect(rectangle2,paint);
    }
}
