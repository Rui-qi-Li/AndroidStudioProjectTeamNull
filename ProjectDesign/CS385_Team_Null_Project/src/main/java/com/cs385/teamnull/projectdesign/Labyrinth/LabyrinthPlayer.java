package com.cs385.teamnull.projectdesign.Labyrinth;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

import static com.cs385.teamnull.projectdesign.Constants.playerColour;
import static com.cs385.teamnull.projectdesign.Constants.playerSize;

/**
 * Class that represents the player character,
 * A Rectangle object and a colour (Green for the whole game)
 *
 * reference : Adapted from https://www.youtube.com/playlist?list=PL2xjPbQaM7JZ_FmXwTAesiAciHEPlGmiW
 *
 * @author Katie Brugha
 * @author student ID : 17186293
 * @version 18-1-2018
 */
public class LabyrinthPlayer {
    private Rect playerRectangle;
    private int colour;

    /**
     * Constructor that makes the player to preset configuration variables
     */
    public LabyrinthPlayer(){
        playerRectangle = new Rect(0,0,playerSize,playerSize);
        this.colour = playerColour ;
    }

    /**
     * Gets the Rect object that is the player
     * @return - Rect playerRectangle
     */
    public Rect getPlayerRectangle(){
        return playerRectangle;
    }

    /**
     * Draws the player to the canvas
     * @param canvas
     */
    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(colour);
        canvas.drawRect(playerRectangle, paint);
    }

    /**
     * moves the player to the inputted point.
     * @param point - (x,y) of where the player should go
     */
    public void update(Point point){
        playerRectangle.set(point.x-playerSize/2,point.y- playerSize/2,point.x+playerSize/2,point.y+playerSize/2);
    }
}

