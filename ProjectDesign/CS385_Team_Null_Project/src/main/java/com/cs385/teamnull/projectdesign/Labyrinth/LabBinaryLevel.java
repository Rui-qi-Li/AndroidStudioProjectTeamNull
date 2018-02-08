package com.cs385.teamnull.projectdesign.Labyrinth;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.view.MotionEvent;

import com.cs385.teamnull.projectdesign.BinaryLock;
import com.cs385.teamnull.projectdesign.LeaderBoard;
import com.cs385.teamnull.projectdesign.R;
import com.cs385.teamnull.projectdesign.adventure;

import java.util.ArrayList;

import static com.cs385.teamnull.projectdesign.Constants.*;
import static com.cs385.teamnull.projectdesign.adventure.ENDTIME;

/**
 * The class that defines the final labyrinth level.
 * This level has a keypad that when interacted with starts the BinaryLock.
 * When solved the walls move to allow the user to access the door that completes the game.
 *
 * @author Katie Brugha
 * @author student ID : 17186293
 * @version 18-1-2018
 */
public class LabBinaryLevel extends LabLevel {
    private Rect keypad;
    private ArrayList<ArrayList<Rect>> keys;
    private ArrayList<Rect> tempList;
    private boolean accessingKeypad;
    private boolean movingWalls;
    private LabyrinthWalls uMove;
    private LabyrinthWalls lMove;
    private int totalMoved;
    private int move;
    private int keySize;


    /**
     * Constructor for the LabBinaryLevel.
     * Creates the Rects that make up the keypad for the player to interact with.
     * Creates the extra uMove and lMove LabyrinthWalls objects, both only contain one wall.
     * When the binaryLock is solved both walls move independently when BinaryLock has been solved.
     * Also creates the exit for the player to interact with on the other side of the moving walls
     * @param context
     */
    LabBinaryLevel(Context context){
        super(context);
        keySize = SCREEN_WIDTH/50;
        textBox.setText("  Freedom!.....","    Almost?    ","What's on that ","   keypad?  ");
        move = SCREEN_WIDTH/150;
        playerPoint = new Point(wallx[1], wally[1]);
        uMove = new LabyrinthWalls();
        uMove.addWall(wallx[3]+ offset,wally[3]- wallDepth /4,wallx[3]+ wallDepth,wally[4]+ wallDepth /4);//UPPER MOVING WALL
        lMove = new LabyrinthWalls();
        lMove.addWall(wallx[3],wally[4]+ wallDepth - wallDepth /4,wallx[3]+ wallDepth,wally[5]+ wallDepth +(wallDepth /4));//LOWER MOVING WALL
        player.update(playerPoint);
        door = new Rect(wallx[3]+2* wallDepth,wally[3]+ wallDepth,wallx[4]+2* wallDepth,wally[5]);
        keypad = new Rect(wallx[2] + 2*keySize, wally[2],  wallx[2] + 3*2*keySize+2*keySize, wally[2] + 3*2*keySize+2*keySize);
        keys = new ArrayList<>();
        for(int i = 1; i<4;i++){
            tempList = new ArrayList<>();
            for(int j = 1; j<4;j++) {
                tempList.add(new Rect(wallx[2] + i*2*keySize, wally[2]+ j*2*keySize,  wallx[2] + i*2*keySize+2*keySize, wally[2] + j*2*keySize+2*keySize));
            }
            keys.add(tempList);
        }

        m_mediaPlayer = MediaPlayer.create(context, R.raw.binarylab); //assign song to the media player
        if(musicSetting) {
            m_mediaPlayer.start(); //start the media player
            playing = true;
        }

        //labyrinthWalls.addWall(wallx[3],wally[3],wallx[4]+wallDepth,wally[3]+wallDepth);
        //labyrinthWalls.addWall(wallx[3],wally[5],wallx[4]+wallDepth,wally[5]+wallDepth);
    }

    /**
     * Overrides the super draw function, draws all the components to the screen
     *
     * @param canvas
     */
    @Override
    public void draw(Canvas canvas){
        canvas.drawColor(backgroundColour);
        Paint paint = new Paint();
        canvas.drawText("SCORE : "+ adventure.SCORE, scoreTextPlaceX,scoreTextPlaceY, adventureScoreTextPaint);
        //if(door.intersect(player.getPlayerRectangle())){player.draw(canvas);}
        if(!labyrinthWalls.playerCollide(player)){
            player.draw(canvas);
        }
        else if(labyrinthWalls.playerCollide(player)){//Double check walls if the player is intersecting
            playerPoint = labyrinthWalls.playerConstraint(playerPoint,player);
            player.update(playerPoint);
            player.draw(canvas);
        }
        labyrinthWalls.draw(canvas);
        uMove.draw(canvas);
        lMove.draw(canvas);
        if(System.currentTimeMillis()-startTime<textBoxTime){textBox.draw(canvas);}
        paint.setColor(Color.GRAY);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(DENSITY*3);
        canvas.drawRect(keypad, paint);
        //canvas.drawRect(keypad2, paint);
        for(int i=0;i<3;i++) {
            for (int j = 0; j < 3; j++) {
                canvas.drawRect(keys.get(i).get(j), paint);
            }
        }
    }

    /**
     * Adds the ordinary walls to the screen.
     */
    public void addWalls(){//Default add all NSEW labyrinthWalls
        //ltrb
        door = new Rect((PLAY_WIDTH - laserPlayerGap)/2,PLAY_HEIGHT- wallDepth,(PLAY_WIDTH + laserPlayerGap)/2,PLAY_HEIGHT);
        //VERTICAL WALLS
        labyrinthWalls.addWall(wallx[4],wally[0]+ offset,wallx[4]+ wallDepth,wally[3]+ wallDepth - offset);
        labyrinthWalls.addWall(wallx[4],wally[5]+ offset,wallx[4]+ wallDepth,wally[8]+ wallDepth - offset);
        //door = new Rect(wallx[3],wally[4],wallx[3]+wallDepth,wally[5];

        //HORIZONTAL WALLS
        labyrinthWalls.addWall(wallx[3]+2* offset,wally[3],wallx[4]+ wallDepth,wally[3]+ wallDepth);
        labyrinthWalls.addWall(wallx[3]+ offset,wally[5],wallx[4]+ wallDepth,wally[5]+ wallDepth);


        //Putting the left of these walls firmly outside of the screen so the user can't get around them
        //Thanks to the kids I babysit for finding that exploit...
        labyrinthWalls.addWall(-300,wally[0]+ offset,wallx[4]+ wallDepth,wally[0]+ wallDepth - offset);
        labyrinthWalls.addWall(-300,wally[8]+ offset,wallx[4]+ wallDepth,wally[8]+ wallDepth - offset);
    }

    /**
     * When the BinaryLock is solved this is called, it starts the walls moving
     */
    public void solvedPuzzle(){
        movingWalls = true;
        totalMoved = 0;
    }


    /**
     * Receives the User touch from the LabyrinthManager
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
     * Same move function as in the super class but it is completely overridden to add in the moving walls.
     * @param event
     */
    @Override
    public void move(MotionEvent event){
        if(movingPlayer) {
            int s = 5;
            int offset = PLAY_WIDTH /15;
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
            if(labyrinthWalls.playerCollide(tempPlayer)||uMove.playerCollide(tempPlayer)||lMove.playerCollide(tempPlayer)){//
                tempPlayerPoint = labyrinthWalls.playerConstraint(tempPlayerPoint,tempPlayer);
                tempPlayerPoint = uMove.playerConstraint(tempPlayerPoint,tempPlayer);
                playerPoint = lMove.playerConstraint(tempPlayerPoint,tempPlayer);
                player.update(playerPoint);
            }
            else{
                playerPoint.set(newx, newy);
                player.update(playerPoint);}
        }
    }

    /**
     * update function.
     * When the player intersects with the keypad the binary game is started
     * when the binaryLock is solved here the walls are moved a set amount every frame.
     * The winEvent here completes the entire game.
     * The time is saved, music stopped and the Labyrinth terminated.
     * The LeaderBoard is started with a boolean to tell that class to check if there is a new high score
     */
    @Override
    public void update(){
        super.update();
        if(movingWalls){
            totalMoved +=move;
            uMove.moveY(-move);
            lMove.moveY(move);
            if(totalMoved>wally[1]){movingWalls = false;}
        }
        if(Rect.intersects(keypad, player.getPlayerRectangle())&&!accessingKeypad) {
            accessingKeypad = true;
            Intent intent = new Intent().setClass(context, BinaryLock.class);
            context.startActivity(intent);
        }
        if(winEvent()&&!won) {
            ENDTIME = System.currentTimeMillis();
            won = true;
            playing = false;
            m_mediaPlayer.release(); //stop the music
            LabLevelManager.terminate();
            Intent intent = new Intent().setClass(context, LeaderBoard.class);
            intent.putExtra("displayNewHighScore", true);
            context.startActivity(intent);
        }
    }
}

