package com.cs385.teamnull.projectdesign;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import static android.view.WindowManager.*;
import static com.cs385.teamnull.projectdesign.Constants.musicSetting;

/**
 * @author Brid Walsh
 * @author student ID : 17185645
 */
public class TicTacToeArcade extends AppCompatActivity implements View.OnClickListener {

    private ArcadeCell[] cells = new ArcadeCell[9]; //initialise array of 9 cell objects
    private int index=0; //initialise button index to 0
    private TextView scoreText,scoreTextDisplay, highScoreText1; //declare text view  scoreText

    private TextView winLoseText; //declare text view to display if the user has won, lost or a draw
    private Button playAgainButton; //declare play again button
    private ImageButton silenceButton,silenceOffButton; //declare buttons to mute and unmute music
    private MediaPlayer m_mediaPlayer; //declare media player that is used to play music
    private ArcadeScore score; //declare score variable of type Score

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(LayoutParams.FLAG_FULLSCREEN, LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_tic_tac_toe_arcade);


        if(musicSetting) {
            m_mediaPlayer = MediaPlayer.create(this, R.raw.tictactoe); //assign song to the media player
            m_mediaPlayer.start(); //start the media player
            m_mediaPlayer.setLooping(true); //make the music keep looping
        }

        for (int i = 0; i < cells.length; i++) { //from 0 to 8
            int buttonId = R.id.xo11 + i; //initialise each button id
            int imageId = R.id.imageView1 + i; //initialise each image view id
            Button button = (Button) findViewById(buttonId); //initialise each button
            button.setOnClickListener(this);  //set the on click listener for the button
            cells[i] = new ArcadeCell(button, (ImageView) findViewById(imageId)); //assign the ith element of the cells array to be a cell object containing the button and correspondin image view
        }

        score = new ArcadeScore(); //initialise score object
        scoreText = (TextView) findViewById(R.id.score); //assign score text view to scoreText
        scoreTextDisplay = (TextView) findViewById(R.id.scoreText1); //assign score text view to scoreText
        highScoreText1 = (TextView) findViewById(R.id.highScoreText1); //assign high score text view to highScoreText
        winLoseText = (TextView) findViewById(R.id.textView); //assign winLoseText to textView
        playAgainButton = (Button) findViewById(R.id.playAgainButton); //assign playAgainButton to playAgainButton in the xml file
        silenceButton = (ImageButton) findViewById(R.id.imageButton);  //assign silenceButton to imageButton in the xml file
        silenceOffButton = (ImageButton) findViewById(R.id.imageButton2); //assign silenceOffButton to imageButton2 in the xml file

        scoreText.setText("Score: " + score.getScore()); //set the score text view to display the score

    }
    /**
    * The onClick method is activated when a button is clicked.
    * */
    @Override
    public void onClick(View v) { //when a button is clicked

        int id = v.getId() - R.id.xo11; // buttonId - lowestButtonId ->This is an int between 0 & 8 for the array index
        //countButtonClick++; //increment

        //users turn
        if(!gameOver()&&!gameWon(R.mipmap.x)&&!gameWon(R.mipmap.o)) { //if the game is not over
            updateCell(id, R.mipmap.x); //call the updateCell method to show the x image in place of the button that has been clicked
        }
        if(gameWon(R.mipmap.x)){ //if the user wins
            score.incrementScore(); //increment the users score
        }

        // computers turn
        if(!gameOver()&&!gameWon(R.mipmap.x)&&!gameWon(R.mipmap.o)) { //if the game is not over
            int computerId = calculateComputerMove(); //set computerID to a random number between 0 and 8
            updateCell(computerId, R.mipmap.o); //call the updateCell method to show the o image in place of the cell index the same as computerID
        }
        if(gameWon(R.mipmap.o)){ //if the computer wins
            winLoseText.setVisibility(View.VISIBLE); //display winLoseText
            playAgainButton.setVisibility(View.VISIBLE); //display the play again button
            scoreTextDisplay.setVisibility(View.VISIBLE);
            scoreTextDisplay.setTextColor(Color.parseColor("#ff0000")); //set colour to red
            highScoreText1.setVisibility(View.VISIBLE);
            highScoreText1.setTextColor(Color.parseColor("#ff0000")); //set colour to red

            winLoseText.setText("You Lose"); //set the winLoseText text view to display "You Lose"

            scoreTextDisplay.setText("Score: "+score.getScore()); //display the score in a text view

            SharedPreferences scoreDataXsAndOs  = getSharedPreferences("highScoreDataXsAndOs", Context.MODE_PRIVATE); //initialise shared preferences to hold high scores
            int highScoreXsAndOs = scoreDataXsAndOs.getInt("highScoreDataXsAndOs",0); //set high score

            if(score.getScore() > highScoreXsAndOs){ //if the players score is greater than the stored high score
                highScoreText1.setText("High Score: "+score.getScore()); //set the new high score to be the players score

                //Store the high score in shared preferences
                SharedPreferences.Editor editor = scoreDataXsAndOs.edit();
                editor.putInt("highScoreDataXsAndOs",score.getScore());
                editor.commit();
            }else{
                highScoreText1.setText("High Score: "+highScoreXsAndOs); //set the high score text to display the current high score
            }

            winLoseText.setTextColor(Color.parseColor("#ff0000")); //set the colour of the winLoseText text view to red
            score.setScore(0); //set the score to 0

        }else if(gameWon(R.mipmap.x)){ //if the user wins
            winLoseText.setVisibility(View.VISIBLE); //display winLoseText
            playAgainButton.setVisibility(View.VISIBLE); //display the play again button
            winLoseText.setText("You Win!"); //set the winLoseText text view to display "You Win"
            winLoseText.setTextColor(Color.parseColor("#00ff00")); //set the colour of the winLoseText text view to green

        }else if(!gameWon(R.mipmap.x) && !gameWon(R.mipmap.o) && gameOver()){ //if the game is a draw
            winLoseText.setVisibility(View.VISIBLE); //display winLoseText
            playAgainButton.setVisibility(View.VISIBLE); //display the play again button
            winLoseText.setText("Draw"); //set the winLoseText text view to display "Draw"
            winLoseText.setTextColor(Color.parseColor("#ffffff")); //set the colour of the winLoseText text view to white
        }

        scoreText.setText("Score: "+score.getScore()); //set the text in the text box to display the score
        makeUnclickable(cells); //make all buttons unclickable when the game has been won
    }
    public void playAgain(View view){ //when the play again button is clicked
        resetCells(); //reset the cells to start a new game
        clearResult(); //start the Xs and Os class again
    }

    private void resetCells(){
        for(ArcadeCell cell : cells) { //for each cell
            cell.getImage().setVisibility(View.INVISIBLE); //set the image at this cell to invisible
            cell.setImageResource(0); // reset to default
            cell.getButton().setVisibility(View.VISIBLE); //set the button at this cell to visible
        }
    }

    private void clearResult() {
        winLoseText.setVisibility(View.INVISIBLE); //set the winLoseText to invisible
        playAgainButton.setVisibility(View.INVISIBLE); //set the play again button to invisible
        scoreTextDisplay.setVisibility(View.INVISIBLE); //set the winLoseText to invisible
        highScoreText1.setVisibility(View.INVISIBLE); //set the winLoseText to invisible
        winLoseText.setText(""); //remove text
        winLoseText.setTextColor(Color.parseColor("#000000")); //set the colour of the winLoseText text view to black
    }

    private int calculateComputerMove() {
        do {
            index = (int) (Math.random() * 9); //generate random number between 0 and 8 inclusive
        } while(cells[index].isInUse()); //while the button index has already been clicked
        return index; //return the randomly generated index
    }
    private void updateCell(int id, int playerImage)
    {
        ArcadeCell selectedCell = cells[id]; //assign the cell object selected cell to the cell in the array index id
        selectedCell.getImage().setVisibility(View.VISIBLE); //set the visibility of the image at this cell to visible
        selectedCell.getButton().setVisibility(View.INVISIBLE); //set the visibility of the button at this cell to invisible
        selectedCell.setImageResource(playerImage); //set the image at this cell to the x or o image depending on which player is playing
    }

    private boolean gameOver() {
        for(int i=0;i<cells.length;i++) //for i from 0 to 8
        {
            if(!cells[i].isInUse()) //if a cell is found that isnt in use
            {
                return false; //the game is not over
            }
        }
        return true; //the game is over
    }

    private boolean gameWon(int xOrO){
        if(cells[0].getImageResource() == cells[1].getImageResource() && cells[0].getImageResource() == cells[2].getImageResource()){ //if the top row is all the same image
            if(cells[0].getImageResource() == xOrO) { //if the image at cell 0 is xOrO
                return true; //the game has been won
            }
        }
        if(cells[3].getImageResource() == cells[4].getImageResource() && cells[3].getImageResource() == cells[5].getImageResource()){ //if the middle row is all the same image
            if(cells[3].getImageResource() == xOrO) { //if the image at cell 3 is xOrO
                return true; //the game has been won
            }
        }
        if(cells[6].getImageResource() == cells[7].getImageResource() && cells[6].getImageResource() == cells[8].getImageResource()){ //if the bottom row is all the same image
            if(cells[6].getImageResource()==xOrO) { //if the image at cell 6 is xOrO
                return true; //the game has been won
            }
        }
        if(cells[0].getImageResource()==cells[3].getImageResource() && cells[0].getImageResource()==cells[6].getImageResource()){ //if the first column is all the same image
            if(cells[0].getImageResource()==xOrO) { //if the image at cell 0 is xOrO
                return true; //the game has been won
            }
        }
        if(cells[1].getImageResource()==cells[4].getImageResource() && cells[1].getImageResource()==cells[7].getImageResource()){ //if the second column is all the same image
            if(cells[1].getImageResource()==xOrO) { //if the image at cell 1 is xOrO
                return true; //the game has been won
            }
        }
        if(cells[2].getImageResource()== cells[5].getImageResource() && cells[2].getImageResource()==cells[8].getImageResource()){ //if the third column is all the same image
            if(cells[2].getImageResource()==xOrO) { //if the image at cell 2 is xOrO
                return true; //the game has been won
            }
        }
        if(cells[0].getImageResource()==cells[4].getImageResource() && cells[0].getImageResource()==cells[8].getImageResource()){ //if the left diagonal is all the same image
            if(cells[0].getImageResource()==xOrO) { //if the image at cell 0 is xOrO
                return true; //the game has been won
            }
        }
        if(cells[2].getImageResource()==cells[4].getImageResource()&& cells[2].getImageResource()==cells[6].getImageResource()){ //if the right diagonal is all the same image
            if(cells[2].getImageResource()==xOrO) { //if the image at cell 2 is xOrO
                return true;//the game has been won
            }
        }
        return false; //the game has not been won
    }
    private void makeUnclickable(ArcadeCell[] cells){
        if(gameWon(R.mipmap.x)||gameWon(R.mipmap.o)){ //if the game has been won
            for(int i=0;i<9;i++){ //for i from 0 to 8 inclusive
                cells[i].getButton().setVisibility(View.INVISIBLE); //set the button at this index to be invisible
            }
        }
    }

    //Music
    @Override
    protected void onPause() {
        super.onPause();
        if(musicSetting) {
            m_mediaPlayer.release(); //stop the music
        }
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(musicSetting){m_mediaPlayer.release();} //stop the music
        finish();
    }

}
class ArcadeCell {
    private Button button; //initialise button of type Button
    private ImageView image; //initialise image of type ImageView
    private int imageResource; //initialise imageResource of type int

    ArcadeCell(Button button, ImageView image) { //Cell constructor that takes in a Button and ImageView
        this.button = button; //assign the button to button
        this.image = image; //assign the image to image
    }

    public Button getButton() {
        return button;
    } //getter method for button

    public void setButton(Button button) {
        this.button = button;
    } //setter method for button

    public ImageView getImage() {
        return image;
    } //getter method for ImageView

    public void setImage(ImageView image) {
        this.image = image;
    } //setter method for ImageView

    public boolean isInUse() {
        return button.getVisibility() == View.INVISIBLE; //return true if the button is invisible, meaning it is in use
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource; //store the number that corresponds to the image
        image.setImageResource(imageResource); //set the imageResource of the image to this value
    }

    public int getImageResource() { //getter method for imageResource, this is the number that corresponds to the image
        return imageResource;
    }
}
class ArcadeScore{
    private int count; //declare count to keep track of the score
    public ArcadeScore(){
        count=0;
    } //Constructor initialises count to 0
    public void setScore(int a){
        count=a;
    } //set method to set the score
    public int getScore(){
        return count;
    } //get method to retrieve the score
    public void incrementScore() {
        ++count;
    } //incrementScore method to add 1 to the current score
}
