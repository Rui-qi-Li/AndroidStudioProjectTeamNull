package com.cs385.teamnull.projectdesign;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.Window;
import static android.view.WindowManager.*;

import com.cs385.teamnull.projectdesign.Labyrinth.LabLevelManager;

import java.util.Timer;
import java.util.TimerTask;

import static com.cs385.teamnull.projectdesign.Constants.*;

/**
 * Catch Game
 * Hold down the left and right of the screen to move the character (the green square).
 * Hit the green circles to gain points and avoid the red circles
 * If a red circle is hit, the game ends and the score and high score are displayed
 * A play again button is also displayed and when pressed, it will start the CatchGame class all over again
 *
 * References:
 * Tutorial for game layout: https://www.youtube.com/playlist?list=PLRdMAPi4QUfbIg6dRXf56cbMfeYtTdNSA
 *
 * @author Brid Walsh
 * @author student ID : 17185645
 */
public class CatchGame extends AppCompatActivity {
    //Display variables
    private TextView scoreText,scoreText1,scoreText2,highScoreText,gameover;
    private ImageView character,redcircle,redcircle2,redcircle3,greencircle,greencircle2,greencircle3;
    private Button button1;

    //Timer for hitting the red circles
    private long circleTouchTime; //

    //Size variables
    private int frameWidth,characterSize,screenWidth,screenHeight;

    //Position variables
    private int charPosition,redcircleX,redcircleY,redcircleX2,redcircleY2,redcircleX3,redcircleY3,greencircleX,greencircleY,greencircleX2,greencircleY2,greencircleX3,greencircleY3;

    private int score=0; //initialise score to 0
    private int actionX; //x coordinate where the screen has been pressed

    //timer to move character
    private Handler handler = new Handler();
    private Timer timer;

    //check
    private boolean action = false,start = false;

    private MediaPlayer m_mediaPlayer; //initialise media player that is used to play music

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(LayoutParams.FLAG_FULLSCREEN, LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_catch_game);

        scoreText = (TextView) findViewById(R.id.score); //assign scoreText text view to score in the activity_catch_game xml
        scoreText2 = (TextView) findViewById(R.id.score2); //assign scoreText2 text view to score2 in the activity_catch_game xml
        character = (ImageView) findViewById(R.id.character);  //assign character image view to character in the activity_catch_game xml
        redcircle = (ImageView) findViewById(R.id.redcircle); //assign redcircle image view to redcircle in the activity_catch_game xml
        redcircle2 = (ImageView) findViewById(R.id.redcircle2); //assign redcircle2 image view to redcircle2 in the activity_catch_game xml
        redcircle3 = (ImageView) findViewById(R.id.redcircle3); //assign redcircle3 image view to redcircle3 in the activity_catch_game_arcade xml
        greencircle = (ImageView) findViewById(R.id.greencircle); //assign greencircle image view to greencircle in the activity_catch_game xml
        greencircle2 = (ImageView) findViewById(R.id.greencircle2); //assign greencircle image view to greencircle in the activity_catch_game xml
        greencircle3 = (ImageView) findViewById(R.id.greencircle3); //assign greencircle3 image view to greencircle in the activity_catch_game_arcade xml
        button1 = (Button) findViewById(R.id.button1); //assign button1 button to button1 in the activity_catch_game xml
        scoreText1 = (TextView) findViewById(R.id.textView1);  //assign scoreText1 text view to textView1 in the activity_catch_game xml
        highScoreText = (TextView) findViewById(R.id.textView2); //assign highScoreText text view to textView2 in the activity_catch_game xml
        gameover = (TextView) findViewById(R.id.textView); //assign gameOver text view to textView in the activity_catch_game xml
        scoreText.setText("SCORE : "+adventure.SCORE); //set the score text view to display the overall score

        //screen size needed for ball characters
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Point screenSize = new Point();
        display.getSize(screenSize); //get the size of the screen

        m_mediaPlayer = MediaPlayer.create(this, R.raw.catchgame); //assign song to the media player
        if(musicSetting) {
            playing = true; //set playing to true
            m_mediaPlayer.start(); //start the media player
        }

        screenWidth=screenSize.x; //the width of the screen is the x axis of screenSize
        screenHeight=screenSize.y;  //the height of the screen is the y axis of screenSize
        circleTouchTime=System.currentTimeMillis();
        timer = new Timer();
    }

    /**
     * The changePos function is used to change the position of each of the circles on the screen
     * It uses Math.random to generate where on the screen the circles will appear.
     * Values are added to the y coordinate of each circle so that they will move down the screen.
     * It also checks which side of the screen has been pressed and moves the character to that side of the
     * screen while the screen is pressed
     * */
    public void changePos(){

        circleHit();//call circleHit function

        //set positions of the redcircle
        redcircleY+=20; //add 20 to the y coordinate of the redcircle
        if(redcircleY>screenHeight){ //if the y coordinate goes beyond the screen height
            redcircleY = -40; //set the y coordinate to be -40 so that the redcircle will always be inside the screen
            redcircleX = (int) Math.floor(Math.random()*(frameWidth-redcircle.getWidth())); //randomize where the redcircle will be on the x axis
        }
        redcircle.setY(redcircleY); //set the y coordinate of redcircle to redcircleY
        redcircle.setX(redcircleX); //set the x coordinate of redcircle to redcircleX

        //set positions of the redcircle
        redcircleY2+=17; //add 17 to the y coordinate of the redcircle2
        if(redcircleY2>screenHeight){ //if the y coordinate is beyond the screen height
            redcircleY2 = -40; //set the y coordinate to be -40 so that redcircle2 will always be inside the screen
            redcircleX2 = (int) Math.floor(Math.random()*((frameWidth-redcircle2.getWidth())/2)); //randomize where redcircle2 will be on the x axis
        }
        redcircle2.setY(redcircleY2); //set the y coordinate of redcircle2 to redcircleY2
        redcircle2.setX(redcircleX2); //set the x coordinate of redcircle2 to redcircleX2

        //set positions of the redcircle
        redcircleY3+=22; //add 22 to the y coordinate of the redcircle3
        if(redcircleY3>screenHeight){ //if the y coordinate is beyond the screen height
            redcircleY3 = -40; //set the y coordinate to be -40 so that redcircle2 will always be inside the screen
            redcircleX3 = (int) Math.floor(Math.random()*((frameWidth-redcircle3.getWidth())/2))+(frameWidth-redcircle3.getWidth())/2; //randomize where redcircle2 will be on the x axis at the right of the screen
        }
        redcircle3.setY(redcircleY3); //set the y coordinate of redcircle3 to redcircleY3
        redcircle3.setX(redcircleX3); //set the x coordinate of redcircle3 to redcircleX3


        //set positions of the greencircle
        greencircleY+=22; //add 22 to the y coordinate of the greencircle
        if(greencircleY>screenHeight){ //if the y coordinate is beyond the screen height
            greencircleY = -20; //set the y coordinate to be -20 so that greencircle will always be inside the screen
            greencircleX = (int) Math.floor(Math.random()*((frameWidth-greencircle.getWidth())/2)); //randomize where the greencircle will be on the x axis
        }
        greencircle.setY(greencircleY); //set the y coordinate of greencircle to greencircleY
        greencircle.setX(greencircleX); //set the x coordinate of greencircle to greencircleX

        //set positions of the greencircle
        greencircleY2+=23;
        if(greencircleY2>screenHeight){
            greencircleY2 = -20; //the redcircle will always be inside the screen
            greencircleX2 = (int) Math.floor(Math.random()*((frameWidth-greencircle2.getWidth())/2))+(frameWidth-greencircle2.getWidth())/2; //randomize where the greencircle2 will be on the x axis
        }
        greencircle2.setY(greencircleY2); //set the y coordinate of greencircle2 to greencircleY2
        greencircle2.setX(greencircleX2); //set the x coordinate of greencircle2 to greencircleX2

        //set positions of the greencircle
        greencircleY3+=19;
        if(greencircleY3>screenHeight){
            greencircleY3 = -20; //the greencircle will always be inside the screen
            greencircleX3 = (int) Math.floor(Math.random()*(frameWidth-greencircle3.getWidth())); //randomize where the greencircle2 will be on the x axis
        }
        greencircle3.setY(greencircleY3); //set the y coordinate of greencircle2 to greencircleY2
        greencircle3.setX(greencircleX3); //set the x coordinate of greencircle2 to greencircleX2


        if(action==true && actionX<screenWidth/2){ //if the screen has been pressed and the x coordinate of the screen touch is less than the screen width divided by 2 (the screen has been pressed in the left half of the screen)
            charPosition -= 20; //the box moves left
        }else if(action==true && actionX>screenWidth/2){ //if the screen has been pressed and the x coordinate of the screen touch is greater than the screen width divided by 2 (the screen has been pressed in the right half of the screen)
            charPosition += 20; //the box moves right
        }

        //check if character goes out of frame
        if(charPosition<0){ //if the character is outside the screen
            charPosition=0; //set the character position to 0
        }
        if(charPosition> frameWidth - characterSize){ //if the character position is outside the frame width
            charPosition=frameWidth - characterSize; //set the character position to be just inside the frame
        }
        character.setX(charPosition); //set the x coordinate of the character to be charPosition
        scoreText.setText("SCORE : "+adventure.SCORE); //set the text in the scoreText text view to display the score
    }

    /**
     * The circle hit function is used to check if a circle has been hit by the character.
     * It does this by checking if the centre of the circle has hit the character.
     * The score is incremented if a green circle is hit.
     * The game is over if a red circle is hit and the endGameDisplay function is called
     * */
    public void circleHit(){

        //if the center of the circle is in the box, it is a hit
        int greencircleCenterX = greencircleX + greencircle.getWidth()/2;
        int greencircleCenterY = greencircleY + greencircle.getHeight()/2;
        if(character.getX()<=greencircleCenterX && greencircleCenterX<= character.getX()+ characterSize && character.getY()<=greencircleCenterY && greencircleCenterY<=character.getY() + characterSize){ //if the character touches the center of the green circle
            score+=1; //increment the score
            scoreText2.setText(score+"/10"); //set the score text view to show how many points the user has out of 10
            greencircleY=-20; //set the green circle to be back at the top of the screen
            greencircleX = (int) Math.floor(Math.random()*(frameWidth-greencircle.getWidth())); //randomise where the green circle will be generated on the x axis
        }

        int greencircleCenterX2 = greencircleX2 + greencircle2.getWidth()/2;
        int greencircleCenterY2 = greencircleY2 + greencircle2.getHeight()/2;
        if(character.getX()<=greencircleCenterX2 && greencircleCenterX2<= character.getX()+ characterSize && character.getY()<=greencircleCenterY2 && greencircleCenterY2<=character.getY() + characterSize){
            score+=1; //increment the score
            scoreText2.setText(score+"/10"); //set the score text view to show how many points the user has out of 10
            greencircleY2=-20;  //set the green circle to be back at the top of the screen
            greencircleX2 = (int) Math.floor(Math.random()*(frameWidth-greencircle2.getWidth()));
        }

        int greencircleCenterX3 = greencircleX3 + greencircle3.getWidth()/2;
        int greencircleCenterY3 = greencircleY3 + greencircle3.getHeight()/2;
        if(character.getX()<=greencircleCenterX3 && greencircleCenterX3<= character.getX()+ characterSize && character.getY()<=greencircleCenterY3 && greencircleCenterY3<=character.getY() + characterSize){
            score+=1; //increment the score
            scoreText2.setText(score+"/10"); //set the score text view to show how many points the user has out of 10
            greencircleY3=-20;  //set the green circle to be back at the top of the screen
            greencircleX3 = (int) Math.floor(Math.random()*(frameWidth-greencircle3.getWidth()));
        }

        //if the center of the circle is in the box, it is a hit
        int redcircleCenterX = redcircleX + redcircle.getWidth()/2;
        int redcircleCenterY = redcircleY + redcircle.getHeight()/2;
        if((character.getX()<=redcircleCenterX && redcircleCenterX<= character.getX()+ characterSize && character.getY()<=redcircleCenterY && redcircleCenterY<=character.getY() + characterSize)&&(System.currentTimeMillis() - circleTouchTime >=500)){
            adventure.SCORE++;  //increment overall score
            circleTouchTime = System.currentTimeMillis();
        }

        //if the center of the circle is in the box, it is a hit
        int redcircleCenterX2 = redcircleX2 + redcircle2.getWidth()/2;
        int redcircleCenterY2 = redcircleY2 + redcircle2.getHeight()/2;
        if((character.getX()<=redcircleCenterX2 && redcircleCenterX2<= character.getX()+ characterSize && character.getY()<=redcircleCenterY2 && redcircleCenterY2<=character.getY() + characterSize)&&(System.currentTimeMillis() - circleTouchTime >=500)){
            adventure.SCORE++;  //increment overall score
            circleTouchTime = System.currentTimeMillis();
        }

        //if the center of the circle is in the box, it is a hit
        int redcircleCenterX3 = redcircleX3 + redcircle3.getWidth()/2;
        int redcircleCenterY3 = redcircleY3 + redcircle3.getHeight()/2;
        if((character.getX()<=redcircleCenterX3 && redcircleCenterX3<= character.getX()+ characterSize && character.getY()<=redcircleCenterY3 && redcircleCenterY3<=character.getY() + characterSize)&&(System.currentTimeMillis() - circleTouchTime >=500)){
            adventure.SCORE++; //increment overall score
            circleTouchTime = System.currentTimeMillis();
        }

        if(score==10){ //finish the game when the user has caught 10 green circles
            if(timer!=null){ //if timer is not null
                timer.cancel();
            }//Stop the timer
            timer = null; //set timer to null
            finish();
            LabLevelManager.newBinaryLab(); //enter te binary lab
        }

    }
    public void playAgain(View view){
        startActivity(new Intent(getApplicationContext(),CatchGame.class)); //start the game class again
    }

    public boolean onTouchEvent(MotionEvent event1){ //when the screen is pressed
        if(!start){ //if start is false
            start = true; //set start to true

            FrameLayout gameFrame = (FrameLayout) findViewById(R.id.gameFrame); //initialise frame layout
            frameWidth = gameFrame.getWidth(); //get frame width and assign it to the variable frameWidth

            redcircleX=(int) Math.floor(Math.random()*(frameWidth-redcircle.getWidth())); //set red x to a random position at the start
            redcircleX2=(int) Math.floor(Math.random()*(frameWidth-redcircle2.getWidth())); //set red x2 to a random position at the start
            redcircleX3=(int) Math.floor(Math.random()*(frameWidth-redcircle3.getWidth())); //set red x3 to a random position at the start
            greencircleX=(int) Math.floor(Math.random()*(frameWidth-greencircle.getWidth())); //set green x to a random position at the start
            greencircleX2=(int) Math.floor(Math.random()*(frameWidth-greencircle2.getWidth())); //set green x2 to a random position at the start
            greencircleX3=(int) Math.floor(Math.random()*(frameWidth-greencircle3.getWidth())); //set green x3 to a random position at the start

            charPosition = (int) character.getX(); //get character position and parse to integer
            characterSize = character.getHeight(); //get character height

            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            changePos();
                        }
                    });
                }
            },0,20); //call changePos() every 20 milliseconds
        }else{
            if(event1.getAction() == MotionEvent.ACTION_DOWN){
                actionX = (int)event1.getRawX(); //get the X value where the screen has been pressed
                action = true; //set action to true
            }else if(event1.getAction() == MotionEvent.ACTION_UP) {
                action = false; //set action to false
            }
        }
        return true;
    }


    //Back button click should end everything
    @Override
    public void onBackPressed() {
        playing =false; //set playing to false
        m_mediaPlayer.release(); //stop the music
        LabLevelManager.terminate(); //end the game and go back to te main menu
        timer.cancel(); //Stop the timer
        timer = null; //set timer to null
        finish();
    }

    //Music
    @Override
    protected void onPause() {
        super.onPause();
        if(playing&&musicSetting){m_mediaPlayer.pause();} //stop the music

    }

    public void onResume(){
        super.onResume();
        timer = new Timer(); //initialise timer
        if(musicSetting&&playing){ //musicSetting and playing are true
            m_mediaPlayer.start(); //start the music
        }
    }
}
