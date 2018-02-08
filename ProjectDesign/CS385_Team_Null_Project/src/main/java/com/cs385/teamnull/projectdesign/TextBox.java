package com.cs385.teamnull.projectdesign;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

import java.util.ArrayList;

import static com.cs385.teamnull.projectdesign.Constants.SCREEN_HEIGHT;
import static com.cs385.teamnull.projectdesign.Constants.PLAY_WIDTH;
import static com.cs385.teamnull.projectdesign.Constants.textSize;

/**
 * Class to display text easily and uniformly
 * A set size and design so multiple levels can display text without needing to get the spacing correct each time
 * The size and shape of the box is based on the screen width so it should be uniform for all phones
 * 4 lines of text at a set size are chosen and can be set through the constructor or through a method
 *
 * @author Katie Brugha
 * @author student ID : 17186293
 * @version 18-1-2018
 *
 */
public class TextBox {
    private Rect rectangleBox;
    private Rect rectangleLargeBox;
    private int gap = PLAY_WIDTH / 30;
    private int rim = PLAY_WIDTH / 100;
    private int boxWidth = 8* PLAY_WIDTH /10;
    private int boxHeight = 3* PLAY_WIDTH /10;
    private int largeBoxWidth = boxWidth + rim*2;
    private int largeBoxHeight = boxHeight + rim*2;
    private int boxLeftSide = (PLAY_WIDTH -boxWidth)/2;
    private int boxRightSide = (PLAY_WIDTH +boxWidth)/2;
    private int largeBoxLeftSide = (PLAY_WIDTH -largeBoxWidth)/2;
    private int largeBoxRightSide = (PLAY_WIDTH +largeBoxWidth)/2;
    private int boxPosition = (SCREEN_HEIGHT-boxHeight)/2;
    private int largeBoxPosition = boxPosition - rim;
    private int line = boxHeight/4-rim;
    private int textColour = Color.WHITE;
    private Paint textBoxPaint;
    private String s1;
    private String s2;
    private String s3;
    private String s4;


    /**
     * Default constructor sets the text to some Lorem ipsum text
     * The default text should always be replaced
     * It only stands currently as a template of the max number of characters in a line
     *
     */
    public TextBox() {
        //ltrb
        rectangleBox = new Rect(boxLeftSide,boxPosition,boxRightSide,boxPosition+boxHeight);
        rectangleLargeBox = new Rect(largeBoxLeftSide,largeBoxPosition,largeBoxRightSide,largeBoxPosition+largeBoxHeight);
        s1 = "Lorem ipsum dolor  ";
        s2 = "sed do eiusmod tem ";
        s3 = "Ut enim ad minim vi";
        s4 = "quis nostrud exerci";
    }


    /**
     * Second constructor for directly assigning the text on construction
     *
     * @param in1 - String of the first line
     * @param in2 - String of the second line
     * @param in3 - String of the third line
     * @param in4 - String of the fourth line
     */
    public TextBox(String in1, String in2, String in3, String in4){
        //ltrb
        rectangleBox = new Rect(boxLeftSide,boxPosition,boxRightSide,boxPosition+boxHeight);
        rectangleLargeBox = new Rect(largeBoxLeftSide,largeBoxPosition,largeBoxRightSide,largeBoxPosition+largeBoxHeight);
        s1=in1;
        s2=in2;
        s3=in3;
        s4=in4;
    }

    /**
     * Function to reset all of the text in the textbox
     *
     * @param in1 - String of the first line
     * @param in2 - String of the second line
     * @param in3 - String of the third line
     * @param in4 - String of the fourth line
     */
    public void setText(String in1,String in2,String in3,String in4){
        s1=in1;
        s2=in2;
        s3=in3;
        s4=in4;
    }

    /**
     * Text colour is by default white which is used for informational boxes
     * Text is set to Green whenever the user has won something, or achieved a high score
     * Text is set to Red for a lose screen
     * @param colour - colour for the text
     */
    public void setTextColour(int colour){
        textColour = colour;
    }

    /**
     * Draws the full textbox onto the canvas
     * Uses a large box first to create a border
     * paint.setStyle(Paint.Style.STROKE) would also work
     * but the multiple Rects was used to make calculations more easier
     *
     * @param canvas
     */
    public void draw(Canvas canvas) {
        textBoxPaint = new Paint();
        textBoxPaint.setTextSize(textSize);
        textBoxPaint.setTypeface(Typeface.create("serif-monospace",Typeface.NORMAL));
        textBoxPaint.setColor(Color.WHITE);
        canvas.drawRect(rectangleLargeBox, textBoxPaint);
        textBoxPaint.setColor(Color.BLACK);
        canvas.drawRect(rectangleBox, textBoxPaint);
        textBoxPaint.setColor(textColour);
        canvas.drawText(s1,boxLeftSide+gap,boxPosition+line,textBoxPaint);
        canvas.drawText(s2,boxLeftSide+gap,boxPosition+2*line,textBoxPaint);
        canvas.drawText(s3,boxLeftSide+gap,boxPosition+3*line,textBoxPaint);
        canvas.drawText(s4,boxLeftSide+gap,boxPosition+4*line,textBoxPaint);
    }
}