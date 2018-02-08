package com.cs385.teamnull.projectdesign.Labyrinth;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

import com.cs385.teamnull.projectdesign.R;
import com.cs385.teamnull.projectdesign.adventure;
import com.cs385.teamnull.projectdesign.snake;

import java.util.ArrayList;

import static com.cs385.teamnull.projectdesign.Constants.*;
import static com.cs385.teamnull.projectdesign.Labyrinth.LabyrinthThread.averageFPS;

import android.media.MediaPlayer;

/**
 * Labyrinth class to introduce the snake game.
 * A moving snake and 2 moving guards are added.
 * The player must dodge all the obstacles and find the exit
 *
 * @author Katie Brugha
 * @author student ID : 17186293
 * @version 18-1-2018
 */
public class LabSnakeLevel extends LabLevel {
    private ArrayList<Rect> snake;
    private ArrayList<Integer> direction;//ltrb
    private Rect guard1;
    private Rect guard2;
    private boolean dir;
    private int move = SCREEN_WIDTH/200;
    private long hitTime;
    private int snakeSize;
    private int next;


    /**
     * LabSnakeLevel Constructor -
     * Very similar to other Labyrinth Level constructors.
     * The snake is an ArrayList of Rects and the guards are single rects
     * @param context
     */
    LabSnakeLevel(Context context){
        super(context);
        initPlayerPoint = new Point(PLAY_WIDTH /2, wallDepth +(wally[5]+wally[6])/2);
        playerPoint = initPlayerPoint;
        tempPlayerPoint = playerPoint;
        textBox.setText("","   AHHHHH SNAKE  ","","");
        move = SCREEN_WIDTH/70;
        player.update(playerPoint);
        guard1 = new Rect((wallx[1]+wallx[2])/2- wallDepth /2,(wally[1]+wally[2])/2- wallDepth /2,(wallx[1]+wallx[2])/2+ wallDepth + wallDepth /2,(wally[1]+wally[2])/2+ wallDepth + wallDepth /2);
        guard2 = new Rect((wallx[3]+wallx[4])/2- wallDepth /2,(wally[6]+wally[7])/2- wallDepth /2,(wallx[3]+wallx[4])/2+ wallDepth + wallDepth /2,(wally[6]+wally[7])/2+ wallDepth + wallDepth /2);
        door = new Rect(wallx[2]+2* wallDepth,wally[6]+2* wallDepth,wallx[2]+4* wallDepth,wally[6]+4* wallDepth);
        dir = true;
        startTime = hitTime = System.currentTimeMillis();
        next = 0;
        snake = new ArrayList<>();
        direction = new ArrayList<>();
        addSnake();
        door = new Rect(0, wallDepth, wallDepth, wallDepth +wally[1]);
        m_mediaPlayer = MediaPlayer.create(context, R.raw.snakelab); //assign song to the media player
        if(musicSetting) {
            m_mediaPlayer.start(); //start the media player
            playing = true;
        }
        System.out.println(move);
        move = SCREEN_WIDTH/70;
        snakeSize = 40;
    }

    /**
     * When this is called another segment is added to the snake at the initial point
     */
    public void addSnake(){
        snake.add(new Rect(-(wallx[0]+wallx[1])/2- wallDepth /2,(wally[0]+wally[1])/2- wallDepth /2,-(wallx[0]+wallx[1])/2+ wallDepth + wallDepth /2,(wally[0]+wally[1])/2+ wallDepth + wallDepth /2));
        direction.add(2);
    }

    /**
     * Function to move all the segments in the snake by the inputted pixels
     * The direction the individual segments are going is set by an array of integers
     * @param move - integer to move the snake by (pixels)
     */
    public void moveSnake(int move){
        int i = 0;
        for(Rect segment : snake) {
            switch (direction.get(i)){//ltrb
                case 0:
                    segment.left-=move;
                    segment.right-=move;
                    if(segment.left<=(wallx[0]+wallx[1])/2- wallDepth /2){direction.set(i,1);}
                    break;
                case 1:
                    segment.top-=move;
                    segment.bottom-=move;
                    if(segment.top<=(wally[0]+wally[1])/2- wallDepth /2){direction.set(i,2);}
                    break;
                case 2:
                    segment.left+=move;
                    segment.right+=move;
                    if(segment.right>=(wallx[4]+wallx[5])/2+ wallDepth + wallDepth /2){direction.set(i,3);}
                    break;
                case 3:
                    segment.top+=move;
                    segment.bottom+=move;
                    if(segment.bottom>=(wally[7]+wally[8])/2+ wallDepth + wallDepth /2){direction.set(i,0);}
                    break;
            }
            i++;
        }
    }


    /**
     * Adding ordinary walls to the screen
     * For loop of walls commented out at the bottom for reference of all the possible walls
     */
    @Override
    public void addWalls(){
        //VERTICAL
        labyrinthWalls.addWall(wallx[0],wally[1]+ offset,wallx[0]+ wallDepth,wally[8]+ wallDepth - offset);
        labyrinthWalls.addWall(wallx[5],wally[0]+ offset,wallx[5]+ wallDepth,wally[8]+ wallDepth - offset);

        labyrinthWalls.addWall(wallx[1],wally[1]+ offset,wallx[1]+ wallDepth,wally[7]+ wallDepth - offset);
        labyrinthWalls.addWall(wallx[2],wally[1]+ offset,wallx[2]+ wallDepth,wally[2]+ wallDepth - offset);
        labyrinthWalls.addWall(wallx[2],wally[3]+ offset,wallx[2]+ wallDepth,wally[6]+ wallDepth - offset);
        labyrinthWalls.addWall(wallx[3],wally[1]+ offset,wallx[3]+ wallDepth,wally[3]+ wallDepth - offset);
        labyrinthWalls.addWall(wallx[3],wally[4]+ offset,wallx[3]+ wallDepth,wally[6]+ wallDepth - offset);
        labyrinthWalls.addWall(wallx[4],wally[1]+ offset,wallx[4]+ wallDepth,wally[7]+ wallDepth - offset);

        //HORIZONTAL

        labyrinthWalls.addWall(wallx[0],wally[0]+ offset,wallx[5]+ wallDepth,wally[0]+ wallDepth - offset);
        labyrinthWalls.addWall(wallx[0],wally[8]+ offset,wallx[5]+ wallDepth,wally[8]+ wallDepth - offset);

        labyrinthWalls.addWall(wallx[1],wally[1],wallx[2]+ wallDepth,wally[1]+ wallDepth);
        labyrinthWalls.addWall(wallx[3],wally[1],wallx[4]+ wallDepth,wally[1]+ wallDepth);
        labyrinthWalls.addWall(wallx[2],wally[3],wallx[3]+ wallDepth,wally[3]+ wallDepth);
        labyrinthWalls.addWall(wallx[2],wally[6],wallx[3]+ wallDepth,wally[6]+ wallDepth);
        labyrinthWalls.addWall(wallx[1],wally[7],wallx[4]+ wallDepth,wally[7]+ wallDepth);

        //labyrinthWalls.addWall(wallx[1],wally[1]+offset,wallx[1]+wallDepth,wally[3]+wallDepth-offset);//1
        /*for(int i = 0 ; i<6 ; i++){
        labyrinthWalls.addWall(wallx[i],0,wallx[i]+wallDepth,PLAY_HEIGHT);
    }
        for(int i = 0 ; i<9 ; i++){
        labyrinthWalls.addWall(0,wally[i], PLAY_WIDTH,wally[i]+wallDepth);
    }*/
    }

    /**
     * Draw method to draw all the elements to the screen
     * @param canvas
     */
    @Override
    public void draw(Canvas canvas){
        super.draw(canvas);
        Paint paint = new Paint();
        paint.setColor(obstacleColour);
        canvas.drawRect(guard1,paint);
        canvas.drawRect(guard2,paint);
        for(Rect segment : snake) {
            canvas.drawRect(segment,paint);
            if((Rect.intersects(segment, player.getPlayerRectangle())&&(System.currentTimeMillis() - hitTime >=500))){
                adventure.SCORE++;
                canvas.drawColor(Color.RED);
                hitTime = System.currentTimeMillis();
            }
        }
        if(((Rect.intersects(guard1, player.getPlayerRectangle())
                ||Rect.intersects(guard2, player.getPlayerRectangle()))&&(System.currentTimeMillis() - hitTime >=500))){
            adventure.SCORE++;
            canvas.drawColor(Color.RED);
            hitTime = System.currentTimeMillis();
        }
        if((System.currentTimeMillis()-startTime)<textBoxTime){textBox.draw(canvas);}
    }


    /**
     * Update method checks if the snake has moved far enough along to add another segment
     * Also checks if the snake is at max size yet, and stops adding more segments
     * Also moves the guards up and down
     * On the player reaching the exit starts the snake game and finishes this one
     */
    @Override
    public void update(){
        super.update();
        moveSnake(move);
        if(snake.size()<snakeSize&&snake.get(next).contains((wallx[0]+wallx[1])/2- wallDepth *2- wallDepth /2,(wally[0]+wally[1])/2- wallDepth /2)){
            addSnake();
            next++;
        }
        if(dir){
            guard1.top+=move;
            guard1.bottom+=move;
            guard2.top-=move;
            guard2.bottom-=move;
            if(guard1.bottom>=wally[7]){dir = false;}
        }
        else{
            guard1.top-=move;
            guard1.bottom-=move;
            guard2.top+=move;
            guard2.bottom+=move;
            if(guard2.bottom>=wally[7]){dir = true;}
        }
        if(!won&&winEvent()) {
            won=true;
            playing = false;
            m_mediaPlayer.release(); //stop the music
            Intent intent = new Intent().setClass(context, snake.class);
            context.startActivity(intent);
        }
    }
}
