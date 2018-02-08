package com.cs385.teamnull.projectdesign;
import java.util.*;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.View;
import android.widget.TextView;

import com.cs385.teamnull.projectdesign.Labyrinth.LabLevelManager;

import static com.cs385.teamnull.projectdesign.Constants.musicSetting;

/**
 * The binary lock is a mini puzzle that generates a random 4 digit
 * The digits are converted into binary and displayed to the user
 * The user must enter the code on a keypad to progress
 * If the incorrect code is entered the adventure score is incremented
 * If the correct code is entered the Activity is finished and the walls in LabBinaryLevel open

 * @author Katie Brugha
 * @author student ID : 17186293
 * @version 18-1-2018
 */
public class BinaryLock extends AppCompatActivity {

    protected int num1;
    protected int num2;
    protected int num3;
    protected int num4;
    protected int user1=0;
    protected int user2=0;
    protected int user3=0;
    protected int user4=0;
    protected TextView codeTextView;
    protected TextView scoreText;
    protected TextView inputText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_binary_lock);

        codeTextView = findViewById(R.id.code);
        scoreText = findViewById(R.id.score);
        inputText = findViewById(R.id.input);

        displayScore();

        String binaryText = getBinary();
        codeTextView.setText(binaryText);
    }

    public void displayScore(){
        scoreText.setText("SCORE : "+adventure.SCORE);//Displaying the current Score
    }

    /**
     *
     * Small function to take the 4 generated numbers, putting them through the converter function
     * And Concatenating the results and returning them to be displayed
     *
     * @return String - Concatenated Strings of the 4 binary Strings for displaying to the user
     */
    public String getBinary(){
        num1 = this.newnum();
        num2 = this.newnum();
        num3 = this.newnum();
        num4 = this.newnum();
        String bin1 =  numToBinary(num1);
        String bin2 =  numToBinary(num2);
        String bin3 =  numToBinary(num3);
        String bin4 =  numToBinary(num4);
        return(bin1+" "+bin2+" "+bin3+" "+bin4);
    }

    /**
     * Generates random numbers from 1 to 9 inclusive
     *
     * @return n - a randomly generated number
     */
    public int newnum(){
        Random rand = new Random();
        return(rand.nextInt(9) + 1);
    }

    /**
     * Takes in a randomly generated number and creates a String of it in 4-bit binary
     * Uses Integer.toString specifying base 2
     * Pads with zeros if needed
     *
     * @param num - integer to be converted to a String representing the binary equivalent
     * @return - String of Binary
     */
    public String numToBinary(int num){
        String binNum="";
        if(num<8){
            binNum=binNum+"0";
            if(num<4){
                binNum=binNum+"0";
                if(num<2){
                    binNum=binNum+"0";
                }
            }
        }
        binNum=binNum+Integer.toString(num, 2);
        return binNum;
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
     * userAnswer function called from the 10 buttons
     *
     * Checks first if the enter button has been selected, if so checks if the answer is correct
     * Otherwise checks if the numbers have been selected yet
     * userIn==0 indicates not selected yet
     * When each number in turn is selected the number is stored in the user1, user2, user3, user4 variables
     * The selected number is displayed to the user too with the other selected numbers
     *
     * @param userIn - The number selected by the user
     */
    public void userAnswer(int userIn){
        if(userIn==10){
            checkWin();
        }
        else if(user1==0){
            user1=userIn;
            inputText.setText(" "+user1+"  "+" ___ ___ ___");
        }
        else if(user2==0){
            user2=userIn;
            inputText.setText(" "+user1+"  "+" "+user2+"  "+" ___ ___");
        }
        else if(user3==0){
            user3=userIn;
            inputText.setText(" "+user1+"  "+" "+user2+"  "+" "+user3+"  "+" ___");
        }
        else if(user4==0){
            user4=userIn;
            inputText.setText(" "+user1+"  "+" "+user2+"  "+" "+user3+"  "+" "+user4+"  ");
        }
    }

    /**
     * checkWin function compares the user's number selections to the randomly generated numbers
     * If they match the game is finished and the function LabLevelManager.solvedPuzzle() is called
     * This function in turn is called in the LabBinaryLevel where it opens the doors
     * If the numbers don't match the user is notified that they are incorrect
     * The adventure Score is also incremented and the user selections are reset to 0
     *
     */
    public void checkWin(){
        TextView inputText = (TextView) findViewById(R.id.input);
        if(user1==num1&&user2==num2&&user3==num3&&user4==num4){
            LabLevelManager.solvedPuzzle();
            finish();
        }
        else{
            user1=user2=user3=user4=0;
            inputText.setText("___INCORRECT___");
            adventure.SCORE++;
            displayScore();
        }
    }

    /**
     * Override the back button to ensure all the running activities are finished
     */
    @Override
    public void onBackPressed() {
        LabLevelManager.terminate();
        finish();
    }

}