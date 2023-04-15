package space;

import edu.macalester.graphics.Rectangle;

import java.awt.Color;

/*
 * Represents a brick that lines the top region of the play zone. Objects created from this class are broken when the
 * ball hits them. Control for creating objects of this class is associated with the BrickManager class.
 */
public class Brick extends Rectangle {

    private double upperLeftX, upperLeftY, brickWidth, brickHeight;
    private Color color;

    /*
     * Constructs a brick with upper left corner in x/y
     * position with the specified width and height.
     */
    public Brick(double upperLeftX, double upperLeftY, double brickWidth, double brickHeight, Color color) {
        super(upperLeftX, upperLeftY, brickWidth, brickHeight);
        setFillColor(color);
        setFilled(true);
        setStroked(false);
    }

    public String toString() {
        return "Creates a " + color + " brick with upper left corner at " + upperLeftX + "," + upperLeftY + ", a width of " 
        + brickWidth + ", and a height of " + brickHeight;
    }
}
