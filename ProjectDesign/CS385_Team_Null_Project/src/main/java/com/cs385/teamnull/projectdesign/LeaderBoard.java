package com.cs385.teamnull.projectdesign;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import static com.cs385.teamnull.projectdesign.Constants.musicSetting;
import static com.cs385.teamnull.projectdesign.HighScores.leaderBoardNames;
import static com.cs385.teamnull.projectdesign.HighScores.leaderBoardScores;
import static com.cs385.teamnull.projectdesign.HighScores.leaderBoardTimes;

/**
 * Activity to display high scores
 * Can be accessed directly through the main menu
 * The user is also sent to this page after completing the adventure game
 * If the user arrives at this page through the game it is checked if the user has achieved a high score
 * When a high score is achieved the user is prompted to enter their name
 * The scores and names are stored in SharedPreferences
 * A reset button deletes all the high scores including the Arcade High score
 *
 * @author Katie Brugha
 * @author student ID : 17186293
 * @version 18-1-2018
 */
public class LeaderBoard extends AppCompatActivity {
    private int tempScore;
    private long tempTime;
    private int tempRank;
    private Context context;


    /**
     * In the onCreate function it is checked whether the user has achieved a high score
     * If they have their score is compared to the current high scores to find their rank
     * Their rank, score and time are then passed to the displayNewHighScore function
     * The scores are ranked with lower scores ahead of higher,
     * if two scores are equal their times are compared and the faster time is ranked higher
     *
     * @param savedInstanceState -
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Intent intent = getIntent();
        boolean isNewHighScore = intent.getBooleanExtra("displayNewHighScore", false);
        int score = adventure.SCORE;
        long time = adventure.ENDTIME - adventure.STARTTIME;
        context = this;
        populateScoreBoard();

        if (isNewHighScore && score <= leaderBoardScores[4]) {//first check if the score is even on the list
            if(!(score== leaderBoardScores[4]&&time> leaderBoardTimes[4])){//last check in case the score is equal to lowest score & not faster
                for (int i = 0; i < 5; i++) {
                    if (score < leaderBoardScores[i] || (score == leaderBoardScores[i] && time < leaderBoardTimes[i])) {
                        displayNewHighScore(i,score,time);
                        break;
                    }
                }
            }
        }
    }

    /**
     * If the user has achieved a high score this is called from the onCreate
     * The new score and time are displayed at the top
     * An editText and an enter button are made visible for the user to enter their name
     * The score, time and rank are save to temporary variables until the user clicks the enter button
     *
     * @param rank - from 0 to 4, 0 is the top score
     * @param score - 0 is the best possible score
     * @param time - Compared if two scores are equal
     */
    public void displayNewHighScore(int rank, int score, long time){
        TextView textView = findViewById(R.id.NewScore);
        textView.setVisibility(View.VISIBLE);
        textView.setText(""+score);
        textView = findViewById(R.id.NewTime);
        textView.setVisibility(View.VISIBLE);
        textView.setText(longToTime(time));
        EditText editText= (EditText) findViewById(R.id.newName);
        editText.setVisibility(View.VISIBLE);
        textView = findViewById(R.id.Title);
        textView.setText("NEW HIGH SCORE!!!");
        Button saveNameButton = (Button) findViewById(R.id.saveNameButton);
        saveNameButton.setVisibility(View.VISIBLE);
        tempScore=score;
        tempTime=time;
        tempRank=rank;
    }

    /**
     * Function to convert a time stored in long to a String
     * The time is displayed to the user in minutes and seconds
     *
     * @param time - The time score in long
     * @return - A String representing the time in minutes and seconds
     */
    public String longToTime(long time){
        int seconds = (int)(Math.round(time / 1000.0) % 60 );
        int minutes = (int)(Math.round(time / (1000.0*60)) % 60);
        String Ssec;
        String Smin;
        if(seconds<10){Ssec = "0"+seconds;}
        else{Ssec = ""+seconds;}
        if(minutes<10){Smin = "0"+minutes;}
        else{Smin = ""+minutes;}
        return(Smin+":"+Ssec);
    }

    /**
     * When a new score is achieved the old scores must be moved to make space
     * The scores are all moved down until the new rank location is free
     *
     * @param x - integer representing the rank of the new High Score
     */
    public void shuffle(int x){
        SharedPreferences sp = context.getSharedPreferences("HighScores", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if(x!=4){
            for(int i = 4 ; i > x ; i--){
                leaderBoardScores[i] = leaderBoardScores[i-1];
                leaderBoardNames[i] = leaderBoardNames[i-1];
                leaderBoardTimes[i] = leaderBoardTimes[i-1];
                editor.putInt("leaderBoardScores"+i, leaderBoardScores[i-1]).apply();
                editor.putLong("leaderBoardTimes"+i, leaderBoardTimes[i-1] ).apply();
                editor.putString("leaderBoardNames"+i, leaderBoardNames[i-1]).apply();
                editor.commit();
            }
        }
    }

    /**
     * Function to fill the ScoreBoard display with the saved scores.
     * If the scores or times are set to default (9999) they are not displayed.
     *
     * makeInvisible is called at the end to ensure that the views remaining from the displayNewHighScore
     * are all invisible as they are not now used.
     */
    public void populateScoreBoard(){
        TextView textView;
        textView = findViewById(R.id.Name1);
        textView.setText(leaderBoardNames[0]);
        textView = findViewById(R.id.Name2);
        textView.setText(leaderBoardNames[1]);
        textView = findViewById(R.id.Name3);
        textView.setText(leaderBoardNames[2]);
        textView = findViewById(R.id.Name4);
        textView.setText(leaderBoardNames[3]);
        textView = findViewById(R.id.Name5);
        textView.setText(leaderBoardNames[4]);

        textView = findViewById(R.id.Score1);
        if(leaderBoardScores[0]!=9999){
            if(leaderBoardScores[0]==0){textView.setText("NULL");}
            else{textView.setText(""+ leaderBoardScores[0]);}
        }
        else{textView.setText("");}
        textView = findViewById(R.id.Score2);
        if(leaderBoardScores[1]!=9999){
            if(leaderBoardScores[1]==0){textView.setText("NULL");}
            else{textView.setText(""+ leaderBoardScores[1]);}
        }
        else{textView.setText("");}
        textView = findViewById(R.id.Score3);
        if(leaderBoardScores[2]!=9999){
            if(leaderBoardScores[2]==0){textView.setText("NULL");}
            else{textView.setText(""+ leaderBoardScores[2]);}
        }
        else{textView.setText("");}
        textView = findViewById(R.id.Score4);
        if(leaderBoardScores[3]!=9999){
            if(leaderBoardScores[3]==0){textView.setText("NULL");}
            else{textView.setText(""+ leaderBoardScores[3]);}
        }
        else{textView.setText("");}
        textView = findViewById(R.id.Score5);
        if(leaderBoardScores[4]!=9999){
            if(leaderBoardScores[4]==0){textView.setText("NULL");}
            else{textView.setText(""+ leaderBoardScores[4]);}
        }
        else{textView.setText("");}

        for(int i = 0 ; i<5 ; i++){
            switch (i) {
                case 0:
                    textView = findViewById(R.id.Time1);
                    break;
                case 1:
                    textView = findViewById(R.id.Time2);
                    break;
                case 2:
                    textView = findViewById(R.id.Time3);
                    break;
                case 3:
                    textView = findViewById(R.id.Time4);
                    break;
                case 4:
                    textView = findViewById(R.id.Time5);
                    break;
            }
            if(leaderBoardTimes[i]!=9999){
                String sTime = longToTime(leaderBoardTimes[i]);
                textView.setText(sTime);
            }
            else{textView.setText("");}
        }
        makeInvisible();
    }

    /**
     * This function is called when the user clicks the enter button.
     * The scores aren't saved until enter is clicked in the event the user doesn't want to save a high score.
     * If the enter button is clicked both the shared preferences and local high scores are shuffled to make room.
     * The new name, score and time are stores in the shared preferences file and in the local leaderBoardTimes.
     *
     * @param view
     */
    public void saveName(View view){
        SharedPreferences sp = context.getSharedPreferences("HighScores", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        String name = ((EditText)findViewById(R.id.newName)).getText().toString();
        shuffle(tempRank);
        editor.putInt("leaderBoardScores"+tempRank, tempScore);
        editor.putLong("leaderBoardTimes"+tempRank,tempTime);
        editor.putString("leaderBoardNames"+tempRank, name);
        editor.apply();
        leaderBoardTimes[tempRank]=tempTime;
        leaderBoardNames[tempRank]=name;
        leaderBoardScores[tempRank]=tempScore;
        populateScoreBoard();
    }

    /**
     * Resets the leaderBoard to the default view without the enter button, the enterText
     * and the title back to "Leaderboard".
     *
     */
    public void makeInvisible(){
        TextView textView = findViewById(R.id.NewScore);
        textView.setVisibility(View.INVISIBLE);
        textView = findViewById(R.id.NewTime);
        textView.setVisibility(View.INVISIBLE);
        EditText editText= findViewById(R.id.newName);
        editText.setVisibility(View.INVISIBLE);
        textView = findViewById(R.id.Title);
        textView.setText("LEADERBOARD");
        Button saveNameButton = (Button) findViewById(R.id.saveNameButton);
        saveNameButton.setVisibility(View.INVISIBLE);
    }

    /**
     * Called from the Reset High Scores button,
     * clears the HighScores sharedPreferences file.
     *
     * The musicSetting boolean is replaced in the cleared file
     * so this function does not delete musicSetting too.
     * MusicSetting could have its own file but for a single boolean
     * variable it can be added to the HighScores file to save space.
     * If future settings are added a new settings file can be made.
     * @param view
     */

    public void clearScores(View view){
        SharedPreferences scores = context.getSharedPreferences("HighScores", Context.MODE_PRIVATE);
        scores.edit().clear().commit();
        SharedPreferences.Editor editor = scores.edit();
        editor.putBoolean("MusicSetting",musicSetting).apply(); // need to put the music setting back
        editor.commit();
        MainMenuActivity.populateInternalScores(context);
        populateScoreBoard();
    }
}
