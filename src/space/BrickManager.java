package space;

import edu.macalester.graphics.CanvasWindow;

import java.util.ArrayList;
import edu.macalester.graphics.GraphicsGroup;
import java.util.List;

import java.awt.Color;

/*
 * This class is responsible for creating the bricks on the canvas and contains methods used for breaking the bricks.
 */
public class BrickManager {
    
    private CanvasWindow canvas;
    private List<Brick> bricks;
    private GraphicsGroup brickGroup = new GraphicsGroup();

    // 530
    //320
    private static final int MIN_Y_POSITION = 85,
                            MAX_Y_POSITION = 320,
                            MIN_X_POSITION = 10,
                            MAX_X_POSITION = 520,
                            BRICK_WIDTH = 50,
                            BRICK_HEIGHT = 20;

    /*
     * Constructs a brick manager for the specified window object.
     */
    public BrickManager(CanvasWindow canvas) {
        bricks = new ArrayList<>();
        this.canvas = canvas;
    }

    /*
     * Creates ten rows of ten bricks along the top of the play zone.
     * After each brick is created, it is then added to a brick-specific GraphicsGroup for use in breaking
     * the bricks as well as a list to keep track of them.
     */
    public void generateBricks() {
        int colorCounter = 0;
        for (int i = MIN_Y_POSITION; i < MAX_Y_POSITION; i += 25) {
            for (int j = MIN_X_POSITION; j < MAX_X_POSITION; j += 52) {
                double x = j;
                double y = i;
                Brick brick = new Brick(x, y, BRICK_WIDTH, BRICK_HEIGHT, selectBrickColor(colorCounter));
                brickGroup.add(brick);
                bricks.add(brick);
                colorCounter ++;
            }
        }
        canvas.add(brickGroup);
    }

    /*
     * Taking in the current number of bricks already created as a parameter, this chooses the correct color for the
     * brick currently being made.
     * Ensures that there are twenty of each color of brick.
     */
    private Color selectBrickColor(int counter) {
        if (counter < 20) {
            return Color.red;
        } else if (counter < 40) {
            return Color.orange;
        } else if (counter < 60) {
            return Color.yellow;
        } else if (counter < 80) {
            return Color.green;
        } else {
            return Color.blue;
        }
    }

    /*
     * Check for existing bricks.
     * 
     * @return true if bricks still exist that have not been broken.
     */
    public boolean bricksStillExist() {
        return bricks.size() > 0;
    }

    /*
     * Used in detecting whether the user has won.
     * 
     * @return The number of bricks that have not yet been removed from their original list.
     */
    public int getNumberOfBricks() {
        return bricks.size();
    }

    /*
     * Used in collision detection by the ball.
     * 
     * @return The list of bricks created by the BrickManager object.
     */
    public List<Brick> getBricks() {
        return bricks;
    }

    /*
     * Used to remove a brick from the canvas and the list of brick objects when a ball hits a brick.
     */
    public void breakBrick(Brick b) {
        brickGroup.remove(b);
        bricks.remove(b);
    }

    /*
     * Removes all the bricks from the brick GraphicsGroup and the brick list.
     */
    public void removeAllBricks() {
        for (Brick b : bricks) {
            brickGroup.remove(b);
        }
        bricks.clear();
    }

    public String toString() {
        return "Creates a BrickManager that creates bricks on the canvas and contains methods used for breaking the bricks";
    }
}
