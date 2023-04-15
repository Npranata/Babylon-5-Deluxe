package space;

import edu.macalester.graphics.Rectangle;
import java.awt.Color;

/*
 * Represents the paddle the user uses to bounce the ball upward toward the bricks. The user can use the mouse to move the
 * paddle to the right and left at the same y-level along the lower region of the canvas.
 */
public class Paddle extends Rectangle {

    private double upperLeftX, upperLeftY, paddleWidth, paddleHeight;

    /*
     * Calls the Rectangle constructor to create a paddle with upper left corner at (upperLeftX, upperLeftY),
     * width = paddleWidth, and height = paddleHeight.
     */
    public Paddle(double upperLeftX, double upperLeftY, double paddleWidth, double paddleHeight) {
        super(upperLeftX, upperLeftY, paddleWidth, paddleHeight);
        this.upperLeftX = upperLeftX;
        this.upperLeftY = upperLeftY;
        this.paddleWidth = paddleWidth;
        this.paddleHeight = paddleHeight;
        setFillColor(Color.black);
        setFilled(true);
        setStroked(false);
    }

    /*
     * Sets the x-value of the upper left corner of the paddle. Since the mouse position x-value is used to control 
     * the paddle, and the mouse is in the middle of the paddle, the x-value must be adjusted accordingly.
     */
    public void setUpperX(double x) {
        setPosition(x - 50, getY());
    }

    /*
     * @return The x-value of the upper left corner the paddle.
     */
    public double getLeftX () {
        return getX();
    }

    /*
     * @return The thickness of the paddle in the horizontal direction.
     */
    public double getPaddleWidth () {
        return paddleWidth;
    }

    public String toString() {
        return "Creates a paddle with upper left corner at " + upperLeftX + "," + upperLeftY + ", a width of " 
        + paddleWidth + ", and a height of " + paddleHeight;
    }
}
