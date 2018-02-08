package com.cs385.teamnull.projectdesign;


import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;


/**
 * Subclass of the original Binary Lock in the adventure game
 * This version is accessed through the arcade menu
 * Successful input resets the numbers and increments the score
 *
 * @author Katie Brugha
 * @author student ID : 17186293
 * @version 18-1-2018
 */
public class BinaryArcade extends BinaryLock{
    private int arcadeBinaryScore = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_binary_arcade);

        codeTextView = findViewById(R.id.code);
        scoreText = findViewById(R.id.score);
        inputText = findViewById(R.id.input);

        displayScore();

        String binaryText = getBinary();
        codeTextView.setText(binaryText);
    }

    /**
     * Overrides the function in the superclass that displays the adventure score
     * This score is displayed in green to indicate that more points is good here
     */
    @Override
    public void displayScore(){
        scoreText.setText("SCORE : "+arcadeBinaryScore);//Displaying the current Score
    }

    /**
     * Bank of onClick functions for the 9 numbered button plus a tenth for the enter button
     * Calls the userAnswer function with the selected number
     * @param view
     */
    public void send1(View view) {
        userAnswer(1);
    }
    public void send2(View view) {
        userAnswer(2);
    }
    public void send3(View view) {
        userAnswer(3);
    }
    public void send4(View view) {
        userAnswer(4);
    }
    public void send5(View view) {
        userAnswer(5);
    }
    public void send6(View view) {
        userAnswer(6);
    }
    public void send7(View view) {
        userAnswer(7);
    }
    public void send8(View view) {
        userAnswer(8);
    }
    public void send9(View view) {
        userAnswer(9);
    }
    public void sendEnter(View view) {
        userAnswer(10);
    }

    /**
     * Overrides the function in the superclass that completes the mini-game
     * Here success reruns the random number generator and displays the new number
     * Also increments the binary game score
     * An incorrect answer resets the input and score
     */
    @Override
    public void checkWin(){
        TextView inputText = (TextView) findViewById(R.id.input);
        if(user1==num1&&user2==num2&&user3==num3&&user4==num4){
            arcadeBinaryScore++;
            user1=user2=user3=user4=0;
            inputText.setText("___ CORRECT ___");
            displayScore();
            String binaryText = getBinary();
            codeTextView.setText(binaryText);
        }
        else{
            user1=user2=user3=user4=0;
            arcadeBinaryScore = 0;
            inputText.setText("___INCORRECT___");
            TextView scoreText = (TextView) findViewById(R.id.score);
            scoreText.setText("SCORE : "+arcadeBinaryScore);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

}
