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

    private Random random;

    private Image laserImage;


    // private Ship selectedShip; // The ship the ball has collided with.

    public Laser(
            double centerX,
            double centerY,
            double initialSpeed,
            double initialAngle) {

        this.centerX = centerX;
        this.centerY = centerY;
        this.initialSpeed = initialSpeed;
        this.initialAngle = initialAngle;

        laserImage = new Image("ship-icons/laser.png");
        laserImage.rotateBy(-90);
        laserImage.setCenter(centerX, centerY);
        laserImage.setScale(0.3);

        double initialAngleRadians = Math.toRadians(initialAngle);
        double initialXVelocity = initialSpeed * Math.cos(initialAngleRadians);
        double initialYVelocity = initialSpeed * -Math.sin(initialAngleRadians);
        currentXVelocity = initialXVelocity;
        currentYVelocity = initialYVelocity;
    }

    /**
     * Returns the x-value of the ball's upper left corner box for use in collision detection.
     */
    public double getX() {
        return laserImage.getPosition().getX();
    }

    /**
     * Returns the y-value of the ball's upper left corner box for use in collision detection.
     */
    public double getY() {
        return laserImage.getPosition().getY();
    }

    /**
     * Update the ball's position every time the canvas updates in canvas.update.
     * @param dt The amount that the ball's x and y position change each time the canvas updates.
     */
    public void updatePosition(double dt) {  
        // Calculate new x and y coordinates by adding the x and y velocities times dt to the current coordinates.
        double newX = getX() + (currentXVelocity * dt);
        double newY = getY() + (currentYVelocity * dt);
        laserImage.setPosition(newX, newY);
    }

    /**
     * Loops through the bricks that have yet to be destroyed by the ball and identifies whether the ball
     * is intersecting with one of the bricks.
     * Breaks the intersecting element is it is a brick.
     * 
     * @param midpoint The point on the ball that is intersecting with an element.
     * @return true If the interesecting element is not a brick, but a paddle.
     * @return false If the intersecting element is a brick.
     */
    // private boolean checkElement(Point midpoint, BrickManager brickManager, CanvasWindow canvas, Paddle paddle) {
    //     GraphicsObject currentElement = canvas.getElementAt(midpoint);
    //     for (Brick b : brickManager.getBricks()) {
    //         if (currentElement == b) {
    //             selectedBrick = b;
    //         }
    //     }

    //     if (currentElement == paddle) {
    //         return true;
    //     }
        
    //     if (selectedBrick != null) {
    //         brickManager.breakBrick(selectedBrick);
    //         selectedBrick = null;
    //     }
    //     return false;
    // }

    /**
     * Identifies four points directly off the surface of the ball and checks whether those points are
     * intersecting with another element on the canvas.
     * If the ball is intersecting something, its velocity is reversed to the opposite direction of the collision. This
     * encapsulates collisions with four points on the ball: the top middle, bottom middle, left middle, and right middle.
     * Also ensures that, when the ball hits a point on the paddle to the right of the paddle's midpoint, the ball bounces
     * toward the right side of the screen, while the opposite happens if it collides to the left of the midpoint.
     */
    // public void checkCollisions(CanvasWindow canvas, BrickManager brickManager, Paddle paddle, GraphicsText textDisplay) {
    //     ballTopMidpoint = new Point(getX() + BALL_RADIUS, getY() - 0.5);
    //     ballBottomMidpoint = new Point(getX() + BALL_RADIUS, getY() + 2 * BALL_RADIUS + 0.5);
    //     ballRightMidpoint = new Point(getX() + 2 * BALL_RADIUS + 0.5, getY() + BALL_RADIUS);
    //     ballLeftMidpoint = new Point(getX() - 0.5, getY() + BALL_RADIUS);

    //     if (canvas.getElementAt(ballTopMidpoint) != null && canvas.getElementAt(ballTopMidpoint) != textDisplay) {
    //         setYVelocity(currentYVelocity * -1);
    //         checkElement(ballTopMidpoint, brickManager, canvas, paddle);
    //     }

    //     if (canvas.getElementAt(ballRightMidpoint) != null && canvas.getElementAt(ballRightMidpoint) != textDisplay) {
    //         setXVelocity(currentXVelocity * -1);
    //         checkElement(ballRightMidpoint, brickManager, canvas, paddle);
    //     }

    //     if (canvas.getElementAt(ballBottomMidpoint) != null && canvas.getElementAt(ballBottomMidpoint) != textDisplay) {
    //         setYVelocity(Math.abs(currentYVelocity) * -1);
    //         // If ball to the right of the middle of the paddle, it bounces to the right.
    //         if(checkElement(ballBottomMidpoint, brickManager, canvas, paddle)) {
    //             if (getX() > paddle.getLeftX() + (paddle.getPaddleWidth()/2)) { 
    //                 currentXVelocity = Math.abs(currentXVelocity);
    //             } else {
    //                 currentXVelocity = Math.abs(currentXVelocity) * -1;
    //             }
    //         }
    //     }

    //     if (canvas.getElementAt(ballLeftMidpoint) != null && canvas.getElementAt(ballLeftMidpoint) != textDisplay) {
    //         setXVelocity(currentXVelocity * -1);
    //         checkElement(ballLeftMidpoint, brickManager, canvas, paddle);
    //     }
    // }

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
     * Moves the enemy's spaceship according to current dx and dy
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
