package space;

import edu.macalester.graphics.*;

/**
 * Represents a laser shot from a ship that follows a straight path on the canvas.
 * When the laser hits an opposing ship, the laser is removed.
 */
public class Laser {
    private double centerX, centerY, initialSpeed, initialAngle, currentXVelocity, currentYVelocity;
    private Image laserImage;
    private int chooseColor;

   /**
    * Constructs a laser object that is shot from a ship. 
    *
    * @param centerX The x-value of the center of the laser.
    * @param centerY The y-value of the center of the laser.
    * @param initialSpeed The speed of the laser when it is first shot.
    * @param initialAngle The angle of the laser when it is first shot.
    * @param chooseColor An integer used to determine which color the laser should be.
    */
    public Laser(double centerX, double centerY, double initialSpeed, double initialAngle, int chooseColor) {
        this.centerX = centerX;
        this.centerY = centerY;
        this.initialSpeed = initialSpeed;
        this.initialAngle = initialAngle;
        this.chooseColor = chooseColor;

        laserImage = getImageColor(chooseColor);
        laserImage.setCenter(centerX, centerY);
        laserImage.setScale(0.3);

        double initialAngleRadians = Math.toRadians(initialAngle);
        double initialXVelocity = initialSpeed * Math.cos(initialAngleRadians);
        double initialYVelocity = initialSpeed * -Math.sin(initialAngleRadians);
        currentXVelocity = initialXVelocity;
        currentYVelocity = initialYVelocity;
    }

    /**
     * Chooses the image for the laser to be shot from a ship.
     * @param chooseColor An integer used to differentiate between player lasers and enemy lasers.
     * @return The image of the laser with the appropriate color for which ship shot it.
     */
    private Image getImageColor(int chooseColor) {
        if (chooseColor == 1) {
            laserImage = new Image("ship-icons/laser.png");
            laserImage.rotateBy(-90);
        } else {
            laserImage = new Image("ship-icons/enemyLaser.png");
            laserImage.rotateBy(90);
        }
        return laserImage;
    }

    /**
     * Returns the x-value of the laser's upper left corner box for use in bounds detection.
     */
    public double getLaserX() {
        centerX = laserImage.getCenter().getX();
        return centerX;
    }

    /**
     * Returns the y-value of the laser's upper left corner box for use in bounds detection.
     */
    public double getLaserY() {
        centerY = laserImage.getCenter().getY();
        return centerY;
    }

    /**
     * Sets the laser's current velocity in the horizontal direction.
     */
    public void setXVelocity(double velocity) {
        currentXVelocity = velocity;
    }

    /**
     * Sets the laser's current velocity in the vertical direction.
     */
    public void setYVelocity(double velocity) {
        currentYVelocity = velocity;
    }

    /**
     * Moves the laser using its set horizontal and vertical velocities.
     */
    public void moveLaser(){
        laserImage.moveBy(currentXVelocity, currentYVelocity);
        centerX = getLaserX();
        centerY = getLaserY();
    }
    
    /**
     * @return The visual representation of a laser.
     */
    public Image getLaserImage(){
        return laserImage;
    }
    
    public String toString() {
        return "A laser with center at x-value " 
        + getLaserX() + ", center at y-value " + getLaserY() + ", an initial speed of " 
        + initialSpeed + ", an initial angle of " + initialAngle + ", and color identifier " + chooseColor + ".";
    }
}
