package com.cs385.teamnull.projectdesign.Labyrinth;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.media.MediaPlayer;

import com.cs385.teamnull.projectdesign.R;
import com.cs385.teamnull.projectdesign.adventure;

import java.util.ArrayList;

import static com.cs385.teamnull.projectdesign.Constants.*;

/**
 * The first Labyrinth level in the adventure game
 * Contains flashing lasers to avoid while the player goes around a maze.
 *
 * @author Katie Brugha
 * @author student ID : 17186293
 * @version 18-1-2018
 */
public class LabIntroLevel extends LabLevel {
    private ArrayList<Rect> flashingLasers;
    private int laserOffset;
    private boolean lasers;
    private long onTime = 1000;
    private long offTime = 1000;
    private long flashingTime;
    private long laserTouchTime;


    /**
     * Constructor for the Intro level
     * Initilises the first player point, the lasers, the music and timing values
     * @param context
     */
    LabIntroLevel(Context context){
        super(context);
        initPlayerPoint = new Point(PLAY_WIDTH /2, wallDepth +(wally[0]+wally[1])/2);
        playerPoint = initPlayerPoint;
        tempPlayerPoint = playerPoint;
        textBox.setText("","  AH!  LASERS! ","","Get out of here!!");
        laserTouchTime=System.currentTimeMillis();
        flashingTime = System.currentTimeMillis();
        laserOffset = (wallDepth -laserObstacleDepth)/2;
        player.update(playerPoint);
        flashingLasers = new ArrayList<>();
        addLasers();
        if(musicSetting){
            m_mediaPlayer = MediaPlayer.create(context, R.raw.laserlab); //assign song to the media player
            m_mediaPlayer.start();//start the media player
            playing = true;
        }
    }


    /**
     * @return - boolean true when the player has reached the end of the maze
     */
    public boolean winEvent(){
        if(player.getPlayerRectangle().intersect(door)){return true;}
        return(false);
    }

    /**
     * Uses the already figured out wallx and wally locations to place obstacles around the maze
     */
    public void addLasers(){
        //Horizontal
        flashingLasers.add(new Rect(wallx[4],wally[4]+laserOffset,wallx[5]+ wallDepth,wally[4]+ wallDepth -laserOffset));
        flashingLasers.add(new Rect(wallx[4],wally[6]+laserOffset,wallx[5]+ wallDepth,wally[6]+ wallDepth -laserOffset));

        //Vertical
        flashingLasers.add(new Rect(wallx[1]+laserOffset,wally[0],wallx[1]+ wallDepth -laserOffset,wally[5]+ wallDepth));
        flashingLasers.add(new Rect(wallx[1]+laserOffset,wally[6],wallx[1]+ wallDepth -laserOffset,wally[7]+ wallDepth));
        flashingLasers.add(new Rect(wallx[3]+laserOffset,wally[0],wallx[3]+ wallDepth -laserOffset,wally[8]+ wallDepth));
        flashingLasers.add(new Rect(wallx[4]+laserOffset,wally[0],wallx[4]+ wallDepth -laserOffset,wally[1]+ wallDepth));
    }

    /**
     * Adds the door out of the maze
     * And the walls that make up the maze
     */
    @Override
    public void addWalls(){//Default add all NSEW labyrinthWalls
        //ltrb
        door = new Rect((PLAY_WIDTH - laserPlayerGap)/2,PLAY_HEIGHT- wallDepth,(PLAY_WIDTH + laserPlayerGap)/2,PLAY_HEIGHT);

        labyrinthWalls.addWall(0,0, wallDepth,PLAY_HEIGHT);//E
        labyrinthWalls.addWall(0,0,PLAY_WIDTH, wallDepth);//N
        labyrinthWalls.addWall(PLAY_WIDTH- wallDepth,0,PLAY_WIDTH,PLAY_HEIGHT);//W
        labyrinthWalls.addWall(0,wally[8],wallx[2]+ wallDepth,wally[8]+ wallDepth);//SL
        labyrinthWalls.addWall(wallx[3],wally[8],PLAY_WIDTH,wally[8]+ wallDepth);//SR

        //VERTICAL WALLS
        //Need an offset because of a quirk of the wall class,
        //without the offset the player gets stuck at x-y wall intersections
        labyrinthWalls.addWall(wallx[1],wally[1]+ offset,wallx[1]+ wallDepth,wally[3]+ wallDepth - offset);//1
        labyrinthWalls.addWall(wallx[2],wally[0]+ offset,wallx[2]+ wallDepth,wally[1]+ wallDepth - offset);//2
        labyrinthWalls.addWall(wallx[2],wally[3]+ offset,wallx[2]+ wallDepth,wally[4]+ wallDepth - offset);//3
        labyrinthWalls.addWall(wallx[2],wally[5]+ offset,wallx[2]+ wallDepth,wally[7]+ wallDepth - offset);//4
        labyrinthWalls.addWall(wallx[3],wally[0]+ offset,wallx[3]+ wallDepth,wally[3]+ wallDepth - offset);//5
        labyrinthWalls.addWall(wallx[3],wally[4]+ offset,wallx[3]+ wallDepth,wally[6]+ wallDepth - offset);//6
        labyrinthWalls.addWall(wallx[3],wally[7]+ offset,wallx[3]+ wallDepth,wally[8]+ wallDepth - offset);//7
        labyrinthWalls.addWall(wallx[4],wally[1]+ offset,wallx[4]+ wallDepth,wally[4]+ wallDepth - offset);//8
        labyrinthWalls.addWall(wallx[4],wally[6]+ offset,wallx[4]+ wallDepth,wally[7]+ wallDepth - offset);//9

        //HORIZONTAL WALLS
        labyrinthWalls.addWall(wallx[1],wally[2],wallx[3]+ wallDepth,wally[2]+ wallDepth);//10
        labyrinthWalls.addWall(wallx[0],wally[4],wallx[4]+ wallDepth,wally[4]+ wallDepth);//11
        labyrinthWalls.addWall(wallx[1],wally[5],wallx[2]+ wallDepth,wally[5]+ wallDepth);//12
        labyrinthWalls.addWall(wallx[4],wally[5],wallx[5]+ wallDepth,wally[5]+ wallDepth);//13
        labyrinthWalls.addWall(wallx[0],wally[6],wallx[1]+ wallDepth,wally[6]+ wallDepth);//14
        labyrinthWalls.addWall(wallx[3],wally[6],wallx[4]+ wallDepth,wally[6]+ wallDepth);//15
        labyrinthWalls.addWall(wallx[1],wally[7],wallx[3]+ wallDepth,wally[7]+ wallDepth);//16

    }


    /**
     * Draws the elements of the maze to the screen
     * The walls, the lasers when the lasers boolean is true
     *
     * @param canvas
     */
    @Override
    public void draw(Canvas canvas){
        canvas.drawColor(backgroundColour);
        Paint paint = new Paint();
        paint.setTypeface(Typeface.create("serif-monospace",Typeface.NORMAL));
        paint.setTextSize(textSize);
        paint.setColor(Color.RED);
        canvas.drawText("SCORE : "+ adventure.SCORE, scoreTextPlaceX,scoreTextPlaceY,paint);
        paint.setColor(obstacleColour);
        if(!labyrinthWalls.playerCollide(player)){
            player.draw(canvas);
        }
        else if(labyrinthWalls.playerCollide(player)){//Double check walls if the player is intersecting
            playerPoint = labyrinthWalls.playerConstraint(playerPoint,player);
            player.update(playerPoint);
            player.draw(canvas);
        }

        if(!lasers&&System.currentTimeMillis()-flashingTime>offTime){//if the lasers are off and the time is over offTime, switch lasers on and reset clock
            lasers=true;
            flashingTime = System.currentTimeMillis();
        }
        if(lasers&&System.currentTimeMillis()-flashingTime>onTime){
            lasers=false;
            flashingTime = System.currentTimeMillis();
        }
        if(lasers){
            paint.setColor(obstacleColour);
            for(Rect laser : flashingLasers) {
                canvas.drawRect(laser,paint);
                if((Rect.intersects(laser, player.getPlayerRectangle()))&&System.currentTimeMillis() - laserTouchTime >=500){
                    adventure.SCORE++;
                    canvas.drawColor(Color.RED);
                    laserTouchTime = System.currentTimeMillis();
                }
            }
        }
        labyrinthWalls.draw(canvas);
        if(System.currentTimeMillis()-startTime<textBoxTime){textBox.draw(canvas);}
    }

    /**
     * Updates the maze each frame and checks if the maze has been completed
     * When it is completed the LabLaserLevel is started
     */
    @Override
    public void update(){
        super.update();
        if(!won&&winEvent()) {
            won = true;
            playing = false;
            m_mediaPlayer.release(); //stop the music
            LabLevelManager.newLaserLab();
        }
    }
}
