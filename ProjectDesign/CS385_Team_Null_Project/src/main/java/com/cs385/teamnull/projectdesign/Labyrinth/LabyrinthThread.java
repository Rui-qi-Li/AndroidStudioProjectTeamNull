package com.cs385.teamnull.projectdesign.Labyrinth;


import android.content.Context;
import android.graphics.Canvas;
import android.view.SurfaceHolder;

/**
 * Thread class that controls the Labyrinth running
 * While the labyrinth is running this class updates the game up to the MAX_FPS times a second
 * If the wait-time is not implemented the game tries to update as many times as possible and eventually freezes
 *
 * reference : Adapted from https://www.youtube.com/playlist?list=PL2xjPbQaM7JZ_FmXwTAesiAciHEPlGmiW
 *
 * @author Katie Brugha
 * @author student ID : 17186293
 * @version 18-1-2018
 */
public class LabyrinthThread extends Thread{
    public static final int MAX_FPS = 60 ;
    public static double averageFPS;
    public static boolean running = false;
    public static boolean pause = false;
    private SurfaceHolder surfaceHolder;
    public LabyrinthManager labyrinthManager;
    private Context context;
    public static Canvas canvas;

    /**
     * Constructor that starts the thread of the Labyrinth
     * @param surfaceHolder
     * @param labyrinthManager
     * @param context
     */
    public LabyrinthThread(SurfaceHolder surfaceHolder, LabyrinthManager labyrinthManager, Context context){
        super();
        running = true;
        this.context=context;
        this.surfaceHolder = surfaceHolder;
        this.labyrinthManager = labyrinthManager;
        averageFPS = 30; // initilise just in case
    }

    /**
     * Controls if the whole labyrinth is running or not,
     * if this is set to false after the labyrinth is started it will end
     * @param running - boolean to set if the labyrinth is running or not
     */
    public void setRunning(boolean running){
        this.running = running;
    }

    @Override
    public void run(){
        long nano = 1000000000; //used for converting nano seconds
        long milli = 1000;
        long nanoSecondsPerFrame = nano/MAX_FPS; //Min nano seconds allotted for each frame
        long startTime;//Time at start of frame
        long timeMillis;
        long waitTime;
        int frameCount = 0;
        long totalTime = 0;
        while(running){//The core of the game, each run of this while loop is a frame
            startTime = System.nanoTime();//gets the time where the frame starts
            canvas = this.surfaceHolder.lockCanvas(); // set the canvas to be drawn to
            try{
                if(canvas!=null){//Make sure there is a canvas first
                    synchronized (surfaceHolder){
                        this.labyrinthManager.update();//Here the game is updated
                        this.labyrinthManager.draw(canvas);//Here the whole game is drawn to the screen
                    }
                }
            }catch(Exception e) {
                e.printStackTrace();
            } finally {
                if(canvas != null){
                    try{
                        surfaceHolder.unlockCanvasAndPost(canvas); //finished editing the canvas
                    }catch(Exception e){e.printStackTrace();}
                }
            }
            timeMillis = (System.nanoTime()-startTime); //Time taken for the frame
            waitTime = nanoSecondsPerFrame - timeMillis; //Calculate the rest of the time to get to the set fps
            try{
                if(waitTime > 0){
                    this.sleep((waitTime*milli)/nano);//Wait the rest of the frame
                }
            }catch(Exception e){e.printStackTrace();}
            totalTime+=(System.nanoTime()-startTime);
            frameCount++;
            //Checking what the averageFPS is
            //Not useful anymore but interesting nevertheless for debugging
            if(frameCount == MAX_FPS){
                averageFPS = 1000/((totalTime/frameCount)/1000000);
                frameCount = 0;
                totalTime = 0;
            }
        }
    }
}