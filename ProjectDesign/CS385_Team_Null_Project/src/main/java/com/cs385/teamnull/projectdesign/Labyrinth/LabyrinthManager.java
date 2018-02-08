package com.cs385.teamnull.projectdesign.Labyrinth;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.cs385.teamnull.projectdesign.R;

import static com.cs385.teamnull.projectdesign.Constants.*;
/**
 * Manages the whole of Labyrinth part of the game
 * Contains the context of the activity that created it.
 * Creates the LabyrinthThread that controls the update and timing of the Labyrinth
 * Creates the LabLevelManager that manages and controls the several rooms in the Labyrinth
 *
 * @author Katie Brugha
 * @author student ID : 17186293
 * @version 18-1-2018
 */
public class LabyrinthManager extends SurfaceView implements SurfaceHolder.Callback{
    public LabyrinthThread labyrinthThread;
    public LabLevelManager labLevelManager;
    private Context context;

    /**
     * Constructor for the Labyrinth manager, creates the level manager and thread
     *
     * @param context
     * @param level
     */
    public LabyrinthManager(Context context, int level){ //constructor
        super(context);
        getHolder().addCallback(this);
        labyrinthThread = new LabyrinthThread(getHolder(),this,context);
        labLevelManager = new LabLevelManager(context,level);
        setFocusable(true);
        this.context = context;
    }

    /**
     * Overrides the surfaceChanged method for the SurfaceView
     * @param holder
     * @param format
     * @param width
     * @param height
     */
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){}

    /**
     * Called when the surface is created, initilises and runs the thread
     * @param holder
     */
    @Override
    public void surfaceCreated(SurfaceHolder holder){
        labyrinthThread = new LabyrinthThread(getHolder(),this,context);
        INIT_TIME = System.currentTimeMillis();
        labyrinthThread.setRunning(true);
        labyrinthThread.start();

    }

    /**
     * Stops the Labyrinth when the surface is destroyed
     * @param holder
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder holder){
        boolean retry = true ;
        while(retry){
            try{
                labyrinthThread.setRunning(false);
                labyrinthThread.join();
            } catch(Exception e) {e.printStackTrace();}
            retry = false;
        }
    }

    /**
     * Recieves the MotionEvents and redirects them to the labLevelManager,
     * where it is used to control the game
     * @param event - motionEvent
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event){
        labLevelManager.receiveTouch(event);
        return true;
    }

    /**
     * Checks the status of the labLevelManager,
     * finishes the whole labyrinth if the the game is finished in the Level Manager
     * updates the LevelManager if still running
     */
    public void update(){
        if(!labLevelManager.getRunning()){
            labyrinthThread.setRunning(false);
        }
        else if(labLevelManager.isFinished()){
            synchronized (getHolder()){
                ((Activity) context).finish();
            }
            labyrinthThread.setRunning(false);
        }
        labLevelManager.update();
    }

    /**
     * Calls the draw function on the LabLevelManager
     * @param canvas
     */
    @Override
    public void draw(Canvas canvas){
        super.draw(canvas);
        labLevelManager.draw(canvas);
    }
}
