package com.cs385.teamnull.projectdesign.Labyrinth;
import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;

/**
 * LabLevelManager controls the individual levels of the labyrinth,
 * and also redirects any functions called onto the current level
 * Stores the context from the activity that created the Labyrinth
 *
 * reference : Adapted from https://www.youtube.com/playlist?list=PL2xjPbQaM7JZ_FmXwTAesiAciHEPlGmiW
 *
 * @author Katie Brugha
 * @author student ID : 17186293
 * @version 18-1-2018
 */
public class LabLevelManager {
    private static Context context;
    public static LabLevel currentLevel;
    public static int level;

    /**
     * Constructor for the LabLevelManager.
     * Saves the context and sets the level as the level referred to by the input number
     * @param context
     * @param newLevel
     */
    public LabLevelManager(Context context,int newLevel){
        this.context = context;
        setLevel(newLevel);
    }

    /**
     * Assigns the levels integer values so the levels can be called easily
     * @param newLevel - integer that refers to the various labyrinth levels
     */
    public static void setLevel(int newLevel){
        switch (newLevel){
            case 0:
                newIntroLab();
                break;
            case 1:
                newLaserLab();
                break;
            case 2:
                newSnakeLab();
                break;
            case 3:
                newBinaryLab();
                break;
            case 4:
                newLaserArcade();
                break;
        }
    }

    /**
     * Bank of methods creating the levels and assigning them to the current level
     */
    public static void newIntroLab(){
        currentLevel = new LabIntroLevel(context);
        level = 0;
    }
    public static void newLaserLab(){
        currentLevel = new LabLaserLevel(context);
        level = 1;
    }
    public static void newSnakeLab(){
        currentLevel = new LabSnakeLevel(context);
        level = 2;
    }
    public static void newBinaryLab(){
        currentLevel = new LabBinaryLevel(context);
        level = 3;
    }
    public static void newLaserArcade(){
        currentLevel = new LabLaserArcade(context);
        level = 4;
    }


    /**
     * Obsolete function now but could be useful in the future
     * Remakes the current level, could be used as a refresh function in future
     */
    public static void levelReset(){setLevel(level);}

    /**
     * Bank of functions pushing external functions to the current running level
     */
    public static void solvedPuzzle(){currentLevel.solvedPuzzle();}
    public static void resume(){currentLevel.resume();}
    public static void pause(){currentLevel.pause();}
    public static void terminate(){currentLevel.terminate();}
    public void receiveTouch(MotionEvent event){currentLevel.receiveTouch(event);}
    public void update(){currentLevel.update();}
    public void draw(Canvas canvas){currentLevel.draw(canvas);}
    public boolean getRunning(){return(currentLevel.getRunning());}
    public boolean isFinished(){return(currentLevel.isFinished());}

}
