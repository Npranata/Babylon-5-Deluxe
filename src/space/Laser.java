package space;

import edu.macalester.graphics.*;
import java.util.Random;

/**
 * Represents a ball that follows a straight path on the canvas, bouncing off of anything that it collides with.
 * When the ball hits a brick, the brick is broken.
 */
public class Laser {
    private static final double LASER_RADIUS = 10;

    private double centerX, centerY, initialSpeed, initialAngle, currentXVelocity, currentYVelocity;
    private int chooseColor;

    private Random random;

    private Image laserImage;

    public Laser(
            double centerX,
            double centerY,
            double initialSpeed,
            double initialAngle,
            int chooseColor) {

        this.centerX = centerX;
        this.centerY = centerY;
        this.initialSpeed = initialSpeed;
        this.initialAngle = initialAngle;

        laserImage = getImageColor(chooseColor);
        laserImage.setCenter(centerX, centerY);
        laserImage.setScale(0.3);

        double initialAngleRadians = Math.toRadians(initialAngle);
        double initialXVelocity = initialSpeed * Math.cos(initialAngleRadians);
        double initialYVelocity = initialSpeed * -Math.sin(initialAngleRadians);
        currentXVelocity = initialXVelocity;
        currentYVelocity = initialYVelocity;
    }

    private Image getImageColor(int chooseColor) {
        Image laserImage;
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
     * Returns the x-value of the laser's upper left corner box for use in collision detection.
     */
    public double getX() {
        return laserImage.getPosition().getX();
    }

    /**
     * Returns the y-value of the laser's upper left corner box for use in collision detection.
     */
    public double getY() {
        return laserImage.getPosition().getY();
    }

    /**
     * Sets the ball's current velocity in the horizontal direction.
     */
    public void setXVelocity(double velocity) {
        currentXVelocity = velocity;
    }

    /**
     * Sets the ball's current velocity in the vertical direction.
     */
    public void setYVelocity(double velocity) {
        currentYVelocity = velocity;
    }

    /**
     * Moves the laser according to current dx and dy
     */
    public void moveLaser(){
        laserImage.moveBy(currentXVelocity, currentYVelocity);
        centerX = laserImage.getCenter().getX();
        centerY = laserImage.getCenter().getY();
    }

    /**
     * Adds the ball's shape to the given canvas.
     */
    // public void addToCanvas(CanvasWindow canvas) {
    //     canvas.add(laserImage);
    // }

    /**
     * Convenience to return a random floating point number, min ≤ n < max.
     */
    public double randomDouble(double min, double max) {
        return random.nextDouble() * (max - min) + min;
    }
    
    public Image getLaserImage(){
        return laserImage;
    }
    
    public String toString() {
        return "A laser with center at " 
        + laserImage.getCenter() + ", an initial speed of " + initialSpeed + ", and an initial angle of " + initialAngle;
    }
}
