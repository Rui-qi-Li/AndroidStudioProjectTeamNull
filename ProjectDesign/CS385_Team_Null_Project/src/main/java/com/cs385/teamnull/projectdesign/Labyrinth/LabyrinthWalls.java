package com.cs385.teamnull.projectdesign.Labyrinth;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

import java.util.ArrayList;

import static com.cs385.teamnull.projectdesign.Constants.playerSize;
import static com.cs385.teamnull.projectdesign.Constants.wallColour;
/**
 * A class for the walls that make up the obstacles for the Labyrinth rooms.
 * In each update in the Labyrinth it is checked if the player is in a wall,
 * if so the player is pushed out of the wall before the player is drawn onto the canvas.
 *
 * @author Katie Brugha
 * @author student ID : 17186293
 * @version 18-1-2018
 */
public class LabyrinthWalls {

    private ArrayList<Rect> walls;
    private ArrayList<Boolean> visibility;

    /**
     * Constructor for the walls initilises both arrays for the wall Rectangles and corresponding visibility.
     */
    public LabyrinthWalls(){
        visibility = new ArrayList<>();
        walls = new ArrayList<>();
    }

    /**
     * Called when adding each wall to the Array of walls.
     * The parameters l, t, r, b represent the sides of the wall
     * This function is if the visibility is default to true.
     *
     * @param l - integer representation of the left side of the wall.
     * @param t - integer representation of the top side of the wall.
     * @param r - integer representation of the right side of the wall.
     * @param b - integer representation of the bottom side of the wall.
     */
    public void addWall(int l, int t, int r, int b){
        //walls.add(new Rect(PLAY_WIDTH-wallDepth,0,PLAY_WIDTH,PLAY_HEIGHT));//W
        walls.add(new Rect(l,t,r,b));
        visibility.add(true);
    }

    /**
     * Called when adding each wall to the Array of walls.
     * The parameters l, t, r, b represent the sides of the wall
     * This function is if the visibility is specified
     *
     * @param l - integer representation of the left side of the wall.
     * @param t - integer representation of the top side of the wall.
     * @param r - integer representation of the right side of the wall.
     * @param b - integer representation of the bottom side of the wall.
     * @param v - boolean value for the visibility of the wall.
     */
    public void addWall(int l, int t, int r, int b, Boolean v){
        //walls.add(new Rect(PLAY_WIDTH-wallDepth,0,PLAY_WIDTH,PLAY_HEIGHT));//W
        walls.add(new Rect(l,t,r,b));
        visibility.add(v);
    }

    /**
     * Makes an already created wall visible.
     * @param i - The number of the wall to make visible
     */
    public void makeVisible(int i){
        //walls.add(new Rect(PLAY_WIDTH-wallDepth,0,PLAY_WIDTH,PLAY_HEIGHT));//W
        visibility.set(i,true);
    }


    /**
     * The draw function for all the walls.
     * @param canvas
     */
    public void draw(Canvas canvas){
        Paint paint = new Paint();
        paint.setColor(wallColour);
        int i = 0;
        for(Rect wall : walls) {
            if(visibility.get(i)){canvas.drawRect(wall,paint);}
            i++;
        }
    }

    /**
     * Function to check if the player is in any of the walls.
     * @param player - A player object.
     * @return - boolean true is the player is in any of the walls, false if not.
     */
    public boolean playerCollide(LabyrinthPlayer player){
        for(Rect wall : walls) {
            if(Rect.intersects(wall, player.getPlayerRectangle())){return true;}
        }
        return false;
    }

    /**
     * Function for the moving walls in the LabBinaryLevel
     * Moves all the walls together up or down the inputted integer
     *
     * @param y - pixels to be moved
     */
    public void moveY(int y){
        for(Rect wall : walls) {
            wall.top += y;
            wall.bottom += y;
        }
    }

    /**
     * This function is called after the new playerpoint has been calculated but before the draw.
     * If the player intersects any of the walls this is called.
     * The first four while statements are if the player intersects directly on a side.
     * The final four are if the player intersects on a corner.
     * The first four checks the wall against 3 points on a side of the player and in the
     * while loop pushes the player out of the wall incrementally.
     * Only when the player is no longer in the wall is the point returned.
     *
     * If a corner is intersected, the point on the corner is checked if it is a wall.
     * Then in the four while loops both the x and y sides are incremented
     * until the playerPoint is outside of the wall.
     *
     * This function is the core of the wall class.
     * Obstacles that allow the player to smoothly move along and around but not through
     *
     * @param playerPoint - A point that is the proposed new player location inside a wall
     * @param player - The player object
     * @return - A new point for the player that is just outside the wall
     */
    public Point playerConstraint(Point playerPoint, LabyrinthPlayer player){
        for(Rect wall : walls) {
            if(Rect.intersects(wall, player.getPlayerRectangle())){
                while(wall.contains(playerPoint.x-playerSize/2,playerPoint.y)
                    ||wall.contains(playerPoint.x-playerSize/2,playerPoint.y+playerSize/4)
                    ||wall.contains(playerPoint.x-playerSize/2,playerPoint.y-playerSize/4)) {//Vertical wall on the left
                    playerPoint.set(playerPoint.x+1,playerPoint.y);
                    //System.out.println("LEFT");
                }
                while(wall.contains(playerPoint.x+playerSize/2,playerPoint.y)
                    ||wall.contains(playerPoint.x+playerSize/2,playerPoint.y+playerSize/4)
                    ||wall.contains(playerPoint.x+playerSize/2,playerPoint.y-playerSize/4)){//Vertical wall on the right
                    playerPoint.set(playerPoint.x-1,playerPoint.y);
                    //System.out.println("RIGHT");
                }
                while(wall.contains(playerPoint.x,playerPoint.y-playerSize/2)
                    ||wall.contains(playerPoint.x+playerSize/4,playerPoint.y-playerSize/2)
                    ||wall.contains(playerPoint.x-playerSize/4,playerPoint.y-playerSize/2)) {//Horizontal wall above
                    playerPoint.set(playerPoint.x,playerPoint.y+1);
                    //System.out.println("ABOVE");
                }
                while(wall.contains(playerPoint.x,playerPoint.y+playerSize/2)
                    ||wall.contains(playerPoint.x+playerSize/4,playerPoint.y+playerSize/2)
                    ||wall.contains(playerPoint.x-playerSize/4,playerPoint.y+playerSize/2)){//Horizontal wall below
                    playerPoint.set(playerPoint.x,playerPoint.y-1);
                    //System.out.println("BELOW");
                }
                while(wall.contains(playerPoint.x-playerSize/2,playerPoint.y-playerSize/2)) {//LeftTop corner
                    playerPoint.set(playerPoint.x+1,playerPoint.y+1);
                    //System.out.println("LT");
                }
                while(wall.contains(playerPoint.x+playerSize/2,playerPoint.y-playerSize/2)) {//Right Top corner
                    playerPoint.set(playerPoint.x-1,playerPoint.y+1);
                    //System.out.println("RT");
                }
                while(wall.contains(playerPoint.x-playerSize/2,playerPoint.y+playerSize/2)) {//Left Bottom Corner
                    playerPoint.set(playerPoint.x+1,playerPoint.y-1);
                    //System.out.println("LB");
                }
                while(wall.contains(playerPoint.x+playerSize/2,playerPoint.y+playerSize/2)) {//Right Bottom Corner
                    playerPoint.set(playerPoint.x-1,playerPoint.y-1);
                    // System.out.println("RB");
                }
            }
        }
        return playerPoint;
    }
}
