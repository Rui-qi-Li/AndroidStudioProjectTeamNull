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

import java.util.ArrayList;
import java.util.Random;

import static com.cs385.teamnull.projectdesign.Constants.*;
import static com.cs385.teamnull.projectdesign.snake.snakedensity;


/**
 * Created by Ruiqi Li on 2017/12/10.
 *
 *This is a customised view aiming to draw every elements on the screen for the game Snake.
 *Player can control the direction of this virtual snake (green bar in the pictures) by just tapping on the screen. When the snake eat
 *(touch) the food (green stroke square), it will become longer and keep moving. The food will disappear after eaten by the snake and
 *appear randomly around the screen immediately. The speed is increased every successful eaten and the lose condition is touching the wall
 *or "eat" (touch) snake itself. In the whole game mode, once the player have eaten 4 consecutive foods, the exit door (white stroke square)
 *will apear on the center of the screen and the player can leave and enter the next mini game.
 *
 *Reference:
 *http://blog.csdn.net/poorkick/article/details/51203618
 * @author Ruiqi Li
 * @author student ID : 17251911
 * @author Katie Brugha
 * @author student ID : 17186293
 * @version 18-1-2018
 */
public class SnakeView extends View {
    //win or lose boolean variable, when have 5 successful eaten the exit door will appear
    public boolean win = false;
    public boolean lose = false;
    private int winSize = 3;

    //Arraylist to contain the point of snake body
    private ArrayList<Point>mSnakeList;
    private int mSnakeDirection = 0;
    private final int UP = 1;
    private final int DOWN = 2;
    private final int LEFT = 3;
    private final int RIGHT = 4;
    
    //boundary size and gap
    private int wallsize;
    private int wallGap;
    
    //the width of the snake and food square 
    private final int SnakeAndFoodWidth = 3*playerSize/4;
    private Random random = new Random();
    private Point mFoodPosition;
    private boolean isFoodEaten = true;

    //paints object for all different elements
    private Paint mSnakePaint;
    private Paint mBackgroundPaint;
    private Paint mFoodPaint;
    private Paint mFramePaint;
    private Paint mExitPaint;
    private Paint mScoreTextPaint;
    private Point mExitPosition;

    //information text show box
    private TextBox textBox;
    private String text0 = "                   ";
    private String text1 = "    Game Over!     ";
    private String text2 = " Please Try Again! ";
    private String text3 = "                   ";

    //reset the snake game boundary to make sure it's the multiple of the width of the snake and food square
    private final int hcout = SCREEN_WIDTH/SnakeAndFoodWidth;
    private final int vcout = (PLAY_HEIGHT+SnakeAndFoodWidth)/SnakeAndFoodWidth;
    private int snakeWidth = hcout * SnakeAndFoodWidth;
    private int snakePlayHeight = vcout * SnakeAndFoodWidth;
    
    //point array to contain all positions
    private int[]xPosition = new int[hcout-2];
    private int[]yPosition = new int[vcout-2];

    public int snakeScore = 0;

    public SnakeView(Context context, AttributeSet attrs) {
        super(context,attrs);
    }
    
  /**
   *constructor, dervied from Context super class
   *initialise the wall, snake, food, Paint object, snake body list and food position list
   */
    public SnakeView (Context context){
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

   /**
   *private function
   *initialise the snake list and add the first point into it. It’s the same as having the first successful eaten food and increased by 
   *a fixed length.
   *initialise the movement direction is DOWN
   *win condition or lose condition is false
   */
    private void initSnake(){
        win = false;
        lose = false;
        mSnakeList.add(0,new Point(4*SnakeAndFoodWidth,4*SnakeAndFoodWidth));
        mSnakeDirection = DOWN; 
        isFoodEaten = true;
    }
  /**
   *private function
   *initialise the array of potential points composed of snake body
   *all the points are predefined based on the width and height of the game boundary
   */
    private void initPosition(){
        for(int i=0;i<hcout-2;i++)
            xPosition[i]=(1+i) * SnakeAndFoodWidth;
        for(int i=0;i<vcout-2;i++)
            yPosition[i]=(1+i) * SnakeAndFoodWidth;
    }
    
  /**
   *private function
   *initialise all the Paint object for different elements drawing later
   *snake body: green square with fill style paint
   *food: green square with fill style paint
   *information text: red text with serif-monospace style paint
   *wall: red frame with fill style paint
   *exit: white square with stroke paint
   */
    private void initPaint(){
        mSnakePaint = new Paint();
        mSnakePaint.setColor(Color.GREEN);
        mSnakePaint.setStyle(Paint.Style.FILL);
        mSnakePaint.setTypeface(Typeface.create("serif-monospace",Typeface.NORMAL));
        mSnakePaint.setTextSize(snakedensity*20);

        mFoodPaint = new Paint();
        mFoodPaint.setColor(Color.GREEN);
        mFoodPaint.setStyle(Paint.Style.FILL);
        mFoodPaint.setStyle(Paint.Style.STROKE);
        mFoodPaint.setStrokeWidth(snakedensity*3);

        mBackgroundPaint = new Paint();
        mBackgroundPaint.setColor(Color.RED);
        mBackgroundPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mBackgroundPaint.setFakeBoldText(true);
        mBackgroundPaint.setTypeface(Typeface.create("serif-monospace",Typeface.NORMAL));
        mBackgroundPaint.setAntiAlias(true);
        mBackgroundPaint.setTextSize(snakedensity*20);

        mFramePaint = new Paint();
        mFramePaint.setColor(Color.RED);
        mFramePaint.setStyle(Paint.Style.FILL);

        mExitPaint = new Paint();
        mExitPaint.setColor(Color.WHITE);
        mExitPaint.setStyle(Paint.Style.STROKE);
        mExitPaint.setStrokeWidth(snakedensity*3);

        mScoreTextPaint = new Paint();
        mScoreTextPaint.setTypeface(Typeface.create("serif-monospace",Typeface.NORMAL));
        mScoreTextPaint.setColor(Color.GREEN);
        mScoreTextPaint.setTextSize(smallTextSize);
    }

    //create new Point object for food location
    private void initFood(){
        mFoodPosition = new Point();
    }

  /**
   *override onTouchEvent method
   *This method will listen the touch event from the player and change "snake direction" based on the touch location on the screen.
   *when lose condition is met (touch the wall or snake body), the method will run the if statement - initialise the snake body list
   *and set the lose to false value, which will allow the player to restart the game by touching the screen.
   *@param - MotionEvent event
   *@return - boolean value - super.onTouchEvent(event)
   */
    @Override
    public boolean onTouchEvent (MotionEvent event){
        //if lose, restart the game by touching the screen
        //if statement will create a new snake body list and initialise the "snake" by iniSnake() method, the lose variable will be set
        //to false to avoid if statement running when lose condition isn't be met.
        if(lose){
            mExitPosition = new Point();
            mSnakeList = new ArrayList<>();
            initSnake();//run the initSnake() method to redraw the snake
            lose = false;
        }
        //else, change the snake direction based on the touch location on the screen
        else {
            
            int x = (int) (event.getX());
            int y = (int) (event.getY());

            Point head = mSnakeList.get(0);
            //only allow the snake to turn left/right when the current movement towards up/down, avoid turning directly with 180 degree 
            if (mSnakeDirection == UP || mSnakeDirection == DOWN) {
                if (x < head.x)
                    mSnakeDirection = LEFT;
                if (x > head.x)
                    mSnakeDirection = RIGHT;
            } 
            //only allow the snake to turn up/down when the current movement towards left/right, avoid turning directly with 180 degree
            else if (mSnakeDirection == LEFT || mSnakeDirection == RIGHT) {
                if (y < head.y)
                    mSnakeDirection = UP;
                if (y > head.y)
                    mSnakeDirection = DOWN;
            }
        }
        return super.onTouchEvent(event);
    }

 /**
   *override onDraw method
   *This method is a foundational method for a game, because it will be run after every refresh interval time by invalidate() method from
   *another control class- snake.java. In order word, this method will "redraw" all elements over and over again on a customised view to
   *show the updated movement on the screen. It will make the snake "move" when an appropriate refresh frequency is set.
   *@param - Canvas canvas
   */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBackground(canvas,mBackgroundPaint);
        drawFrame(canvas,mFramePaint);
        drawFood(canvas,mFoodPaint);
        if(snakeScore>=winSize){//if the player win the game (several successful eaten) the exit will be drawed 
            drawExit(canvas,mExitPaint);
        }
        drawSnake(canvas,mSnakePaint);
        
        //draw the score on the bottom of the screen
        canvas.drawText(String.valueOf(snakeScore) + "/" + String.valueOf(winSize) ,snakeWidth-(SnakeAndFoodWidth*2),scoreTextPlaceY,mScoreTextPaint);
        canvas.drawText("SCORE : "+ adventure.SCORE, scoreTextPlaceX,scoreTextPlaceY, adventureScoreTextPaint);
       
        //if lose, draw the lose information on the screen
        if(lose){
            textBox.setTextColour(Color.RED);
            textBox.draw(canvas);
        }

    }
    
 /**
   *private function
   *draw the exit, a stroke rectangle, on the scrren
   */
    private void drawExit(Canvas canvas, Paint mExitPaint){
        mExitPosition = new Point();
        mExitPosition.x = hcout/2*SnakeAndFoodWidth;
        mExitPosition.y = vcout/2*SnakeAndFoodWidth;
        canvas.drawRect(new Rect(mExitPosition.x,mExitPosition.y,mExitPosition.x+SnakeAndFoodWidth,mExitPosition.y+SnakeAndFoodWidth),mExitPaint);
    }
    
 /**
   *private function
   *draw the frame, 4 long and thin rectangle, to the side of the screen
   */
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
    
 /**
   *private function
   *this method update the state of "snake" - how the "snake" will be look like in the next refreshing. It will draw the snake body 
   *from scratch. When lose condition has not met, the method will lengthen the "snake" forward and examine if the exit or food has been 
   *touched. 
   *If the lose condition has met, the method will only draw the snake body from scratch
   *otherwise, the method will add a new point at the head of the "snake" and set the latest direction. It will decide whether the last 
   *point of the "snake" need to be removed based on the food eaten.
   */
    private void drawSnake(Canvas canvas, Paint mSnakePaint) {
        for(int i=0;i<mSnakeList.size();i++)
        {
            Point point = mSnakeList.get(i);
            Rect rect = new Rect(point.x,point.y,point.x+SnakeAndFoodWidth,point.y+SnakeAndFoodWidth);
            canvas.drawRect(rect,mSnakePaint);
        }
       
        if(!lose) {
            
            SnakeMove(mSnakeList, mSnakeDirection);
            if (mSnakeList.contains(mExitPosition))
                win = true;
            if (mSnakeList.contains(mFoodPosition)) {
                isFoodEaten = true;
                snakeScore += 1;
            } else
                mSnakeList.remove(mSnakeList.size() - 1);
        }
    }

 /**
   *private function
   *This method will draw the food rectangle on the screen randomly when the old one has been "eaten" by the "snake"
   */
    private void drawFood(Canvas canvas, Paint mFoodPaint) {
        if(isFoodEaten){
            mFoodPosition.x = xPosition[random.nextInt(hcout-2)];
            mFoodPosition.y = yPosition[random.nextInt(vcout-2)];
            isFoodEaten = false;
        }
        //draw the food rectangel on the point location
        Rect food = new Rect(mFoodPosition.x,mFoodPosition.y,mFoodPosition.x + SnakeAndFoodWidth,mFoodPosition.y + SnakeAndFoodWidth);
        canvas.drawRect(food,mFoodPaint);
    }

    private void drawBackground(Canvas canvas, Paint mBackgroundPaint) {
        canvas.drawColor(Color.BLACK);
    }

 /**
   * public function
   * This method aims to add a new "head" (a new point) into the snake list (index:0) and set the new direction based on the touch event.
   * It will also check if the lose condition meet - whether the "snake" touch the wall or eat itself.
   * @param mSnakeList -
   * @param direction -  it receive the snake body list and the current direction
   */
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

            //eat itself or touch the wall!
            if (mSnakeList.contains(newHead) || newHead.x == 0 || newHead.y == 0 || newHead.x == snakeWidth - SnakeAndFoodWidth || newHead.y == snakePlayHeight - SnakeAndFoodWidth) {
                lose = true;
                adventure.SCORE++;
                mExitPosition = new Point();
            } else
                mSnakeList.add(0, newHead);
        }

    }
}
