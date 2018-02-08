package com.cs385.teamnull.projectdesign;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Random;

import static com.cs385.teamnull.projectdesign.Constants.*;

/**
 * A modified version of the SnakeView activity for the arcade mode
 * Very similar to SnakeView.java except it is designed to be called directly from the Arcade menu
 * As opposed to the original SnakeView and snake is not a level in the main adventure game
 * In Ardade mode, once the snake eat a food, the refresh time is shortened by 5 millisecond, which made the game harder
 *
 * This version has no win event, it progresses infinitely until the user loses
 * The snake speeds up over time by decreasing the Refreshinterval
 * The game begins at 5 frames a second and there is a cap at 20 fps
 *
 *
 * @author Ruiqi Li
 * @author student ID : 17251911
 * @author Katie Brugha
 * @author student ID : 17186293
 * @version 18-1-2018
 */

public class SnakeViewArcade extends View {


    public boolean lose = false;

    private ArrayList<Point>mSnakeList;
    private int mSnakeDirection = 0;
    private final int UP = 1;
    private final int DOWN = 2;
    private final int LEFT = 3;
    private final int RIGHT = 4;

    private int wallsize;
    private int wallGap;
    private final int SnakeAndFoodWidth = 3*playerSize/4;
    private Random random = new Random();
    private Point mFoodPosition;
    private boolean isFoodEaten = true;

    private Paint mSnakePaint;
    private Paint mBackgroundPaint;
    private Paint mFoodPaint;
    private Paint mFramePaint;
    private Paint mTextPaint;
    private TextBox textBox;
    private String text0 = "                   ";
    private String text1 = "    Game Over!     ";
    private String text2 = " Please Try Again! ";
    private String text3 = "                   ";

    /*
    UPDATE
     */
    private final int hcout = SCREEN_WIDTH/SnakeAndFoodWidth;
    private final int vcout = (PLAY_HEIGHT+SnakeAndFoodWidth)/SnakeAndFoodWidth;
    private int snakeWidth = hcout * SnakeAndFoodWidth;
    private int snakePlayHeight = vcout * SnakeAndFoodWidth;
    private int[]xPosition = new int[hcout-2];
    private int[]yPosition = new int[vcout-2];

    public int snakeScore = 0;

    public SnakeViewArcade(Context context, AttributeSet attrs) {
        super(context,attrs);
    }
    /*
    UPDATE
    */

    public SnakeViewArcade (Context context){
        super(context);
        wallsize = laserObstacleDepth;
        wallGap = SnakeAndFoodWidth - wallsize;
        mSnakeList = new ArrayList<>();
        initSnake();
        initFood();
        initPaint();
        initPosition();
        textBox = new TextBox(text0,text1,text2,text3);
    }

    private void initSnake(){
        lose = false;
        mSnakeList.add(0,new Point(4*SnakeAndFoodWidth,4*SnakeAndFoodWidth));
        mSnakeDirection = DOWN;
        isFoodEaten = true;
    }

    private void initPosition(){
        for(int i=0;i<hcout-2;i++)
            xPosition[i]=(1+i) * SnakeAndFoodWidth;
        for(int i=0;i<vcout-2;i++)
            yPosition[i]=(1+i) * SnakeAndFoodWidth;
    }
    private void initPaint(){
        mSnakePaint = new Paint();
        mSnakePaint.setColor(Color.GREEN);
        mSnakePaint.setStyle(Paint.Style.FILL);
        mSnakePaint.setTypeface(Typeface.create("serif-monospace",Typeface.NORMAL));
        mSnakePaint.setTextSize(DENSITY*20);

        mFoodPaint = new Paint();
        mFoodPaint.setColor(Color.GREEN);
        mFoodPaint.setStyle(Paint.Style.FILL);
        mFoodPaint.setStyle(Paint.Style.STROKE);
        mFoodPaint.setStrokeWidth(DENSITY*3);

        mBackgroundPaint = new Paint();
        mBackgroundPaint.setColor(Color.GREEN);
        mBackgroundPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mBackgroundPaint.setFakeBoldText(true);
        mBackgroundPaint.setTypeface(Typeface.create("serif-monospace",Typeface.NORMAL));
        mBackgroundPaint.setAntiAlias(true);
        mBackgroundPaint.setTextSize(DENSITY*20);


        mTextPaint = new Paint();
        mTextPaint.setColor(Color.GREEN);

        mFramePaint = new Paint();
        mFramePaint.setColor(Color.RED);
        mFramePaint.setStyle(Paint.Style.FILL);
    }

    private void initFood(){
        mFoodPosition = new Point();
    }

    @Override
    public boolean onTouchEvent (MotionEvent event){
        if(lose){
            mSnakeList = new ArrayList<>();
            initSnake();
            lose = false;
        }
        else {
            int x = (int) (event.getX());
            int y = (int) (event.getY());

            Point head = mSnakeList.get(0);
            if (mSnakeDirection == UP || mSnakeDirection == DOWN) {
                if (x < head.x)
                    mSnakeDirection = LEFT;
                if (x > head.x)
                    mSnakeDirection = RIGHT;
            } else if (mSnakeDirection == LEFT || mSnakeDirection == RIGHT) {
                if (y < head.y)
                    mSnakeDirection = UP;
                if (y > head.y)
                    mSnakeDirection = DOWN;
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBackground(canvas,mBackgroundPaint);
        drawFrame(canvas,mFramePaint);
        drawFood(canvas,mFoodPaint);
        drawSnake(canvas,mSnakePaint);
        canvas.drawText("SCORE : "+ snakeScore, scoreTextPlaceX,scoreTextPlaceY,altTextPaint);
        if(lose){
            textBox.setTextColour(Color.RED);
            textBox.draw(canvas);
        }

    }

    private void drawFrame(Canvas canvas,Paint mFramePaint){
        Rect frame1 = new Rect(wallGap,wallGap,snakeWidth-wallGap,wallsize+wallGap);//top
        canvas.drawRect(frame1,mFramePaint);
        Rect frame2 = new Rect(wallGap,wallGap,wallsize+wallGap,snakePlayHeight-wallGap);//left
        canvas.drawRect(frame2,mFramePaint);
        Rect frame3 = new Rect(wallGap,snakePlayHeight-wallsize-wallGap,snakeWidth-wallGap,snakePlayHeight-wallGap);//bottom
        canvas.drawRect(frame3,mFramePaint);
        Rect frame4 = new Rect((snakeWidth-wallsize-wallGap),wallGap,snakeWidth-wallGap,snakePlayHeight-wallGap);//right
        canvas.drawRect(frame4,mFramePaint);
    }
    private void drawSnake(Canvas canvas, Paint mSnakePaint) {
        for(int i=0;i<mSnakeList.size();i++)
        {
            Point point = mSnakeList.get(i);
            Rect rect = new Rect(point.x,point.y,point.x+SnakeAndFoodWidth,point.y+SnakeAndFoodWidth);
            canvas.drawRect(rect,mSnakePaint);
        }
       
        if(!lose) {
            SnakeMove(mSnakeList, mSnakeDirection);
      
            if (mSnakeList.contains(mFoodPosition)) {
                isFoodEaten = true;
                snakeScore += 1;
                //in Ardade mode, once the snake eat a food, the refresh time is shortened by 5 millisecond, which made the game harder
                if(snakeArcade.REFRESHINTERVAL>=50){snakeArcade.REFRESHINTERVAL-=5;}
            } else
                mSnakeList.remove(mSnakeList.size() - 1);
        }
    }

    private void drawFood(Canvas canvas, Paint mFoodPaint) {
        if(isFoodEaten){
            mFoodPosition.x = xPosition[random.nextInt(hcout-2)];
            mFoodPosition.y = yPosition[random.nextInt(vcout-2)];
            isFoodEaten = false;
        }
        //draw the same food when refresh
        Rect food = new Rect(mFoodPosition.x,mFoodPosition.y,mFoodPosition.x + SnakeAndFoodWidth,mFoodPosition.y + SnakeAndFoodWidth);
        canvas.drawRect(food,mFoodPaint);
    }

    private void drawBackground(Canvas canvas, Paint mBackgroundPaint) {
        canvas.drawColor(Color.BLACK);
    }

    public void SnakeMove(ArrayList<Point> mSnakeList, int direction)
    {
        if(!lose) {
            Point oldHead = mSnakeList.get(0);
            Point newHead = new Point(oldHead);

            switch (direction) {
                case (UP):
                    newHead.y -= SnakeAndFoodWidth;
                    break;
                case (DOWN):
                    newHead.y += SnakeAndFoodWidth;
                    break;
                case (LEFT):
                    newHead.x -= SnakeAndFoodWidth;
                    break;
                case (RIGHT):
                    newHead.x += SnakeAndFoodWidth;
                    break;
                default:
                    break;
            }

            if (mSnakeList.contains(newHead) || newHead.x == 0 || newHead.y == 0 || newHead.x == snakeWidth - SnakeAndFoodWidth || newHead.y == snakePlayHeight - SnakeAndFoodWidth) {
                //eat itself or touch the wall!
                snakeScore = 0;
                snakeArcade.REFRESHINTERVAL = 200;
                lose = true;
            } else
                mSnakeList.add(0, newHead);
        }

    }
}
