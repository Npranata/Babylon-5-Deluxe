package space;

import edu.macalester.graphics.*;

/*
 * Represents the paddle the user uses to bounce the ball upward toward the bricks. The user can use the mouse to move the
 * paddle to the right and left at the same y-level along the lower region of the canvas.
 */
public class PlayerShip extends Ship {

    private Image playerShipIcon = new Image(0,0);
    private double centerX, centerY, scale;

    /*
     * Calls the Rectangle constructor to create a paddle with upper left corner at (upperLeftX, upperLeftY),
     * width = paddleWidth, and height = paddleHeight.
     */
    public PlayerShip(double centerX, double centerY, double scale) {
        super(centerX, centerY, scale);
        playerShipIcon.setImagePath("ship-icons/playerShip.png");
        playerShipIcon.setCenter(centerX, centerY);
        playerShipIcon.setScale(scale);
        
    }

    /*
     * Sets the x-value of the upper left corner of the paddle. Since the mouse position x-value is used to control 
     * the paddle, and the mouse is in the middle of the paddle, the x-value must be adjusted accordingly.
     */
    // public void setUpperX(double x) {
    //     playerShipIcon.setPosition(x - 100, upperLeftY);
    // }

    // public void setUpperY(double y) {
    //     playerShipIcon.setPosition(upperLeftX, y - 250);
    // }

    public void setLocation(Point mouse) {
        playerShipIcon.setCenter(mouse);
    }

    /*
     * @return The x-value of the upper left corner the paddle.
     */
    public double getUpperX() {
        return playerShipIcon.getX();
    }

    public Point getPosition() {
        return playerShipIcon.getCenter();
    }

    public double getUpperY() {
        return playerShipIcon.getY();
    }

    public Image getPlayerIcon() {
        return playerShipIcon;
    }

    // public void removeShip(){
    //      shipGroup.remove(playerShipIcon);
    // }

    // public GraphicsGroup getShipGroup() {
    //     return shipGroup;
    // }

    // public String toString() {
    //     return "Creates a paddle with upper left corner at " + upperLeftX + "," + upperLeftY + ", a width of " 
    //     + paddleWidth + ", and a height of " + paddleHeight;
    // }
}