package com.cs385.teamnull.projectdesign;

/**
 * Class to store local copies of sharedPreferences data
 * Is populated on start and updated if a new High score is achieved
 *
 * @author Katie Brugha
 * @author student ID : 17186293
 * @version 18-1-2018
 */
public class HighScores {
    public static int[] leaderBoardScores = new int[5];
    public static long[] leaderBoardTimes = new long[5];
    public static String[] leaderBoardNames = new String[5];
    public static int topLaserArcade;
}
