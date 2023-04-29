package space;

import edu.macalester.graphics.*;
import java.util.Random;
import java.util.List;

/*
 * Represents the enemy ship the user uses. The enemy ships appear as soon as  
 * the user presses play. 
 */
public class EnemyShip {
    
    private Image enemyShipIcon = new Image(0,0);
    private double speed, randomAngle, currentXVelocity, currentYVelocity;
    private Random rand = new Random();
    private static final int SPEED = 4;

    private GraphicsObject element;

    private Laser selectedLaser; 
    private int currentHealth = 50;
    private EnemyShip selectedEnemyShip;
    private double originalX, originalY;
    private double centerX, centerY;

    /**
    * Constructs a boss ship based on parameters that set its center and size. 
    * @param centerX The x-value of the center of the enemy's ship.
    * @param centerY The y-value of the center of the enemy's ship.
    * @param enemyScale A value used to set the size of the enemy's  ship.
    */
    public EnemyShip(double centerX, double centerY, double enemyScale) {
        this.centerX = centerX;
        this.centerY = centerY;
        enemyShipIcon.setImagePath("ship-icons/enemyShip.png");
        enemyShipIcon.setCenter(centerX, centerY);
        enemyShipIcon.setScale(enemyScale);
        enemyShipIcon.rotateBy(150);
        setAngleAndVelocity();
        originalX = centerX;
        originalY = centerY;
    }
    /**
    * @return The visual representation of the enemy ship.
    */
    public Image getEnemyShipImage(){
        return enemyShipIcon;
    }
    /**
    * @return The x-value of the center of the enemy ship.
    */
    public double getEnemyX() {
        return enemyShipIcon.getCenter().getX();
    }
    /**
    * @return The y-value of the center of the enemy ship.
    */
    public double getEnemyY() {
        return enemyShipIcon.getCenter().getY();
    }
    /**
    * Sets the initial health of the enemy ship.
    */
    public void setEnemyHealth(int health) {
        currentHealth = health;
    }

    /**
     * Sets the enemy spaceship's starting angle and velocity.
     */
    private void setAngleAndVelocity(){
        double angleDegrees = rand.nextDouble(280, 320);
        double angleRadians = Math.toRadians(angleDegrees);
        currentXVelocity = Math.cos(angleRadians) * SPEED;
        currentYVelocity = - Math.sin(angleRadians) * SPEED;
    }
    /**
     * Moves the enemy's spaceship according to current horizontal and vertical velocity.
     * If an enemy ship leaves the canvas window, it returns to its original position.
     * @param canvas The game window in order to check if the enemy ship goes outside of it.
     */
    public void moveEnemyShip(CanvasWindow canvas){
        enemyShipIcon.moveBy(currentXVelocity, currentYVelocity);
        if (enemyShipIcon.getCenter().getX() > canvas.getWidth() + 100 || 
        enemyShipIcon.getCenter().getY() > canvas.getHeight() + 300 || enemyShipIcon.getCenter().getX() < -200) {
            enemyShipIcon.setCenter(originalX, originalY);
        }
    }

   /**
    * Removes any player lasers intersecting with the enemy ship's weak points, decreasing the enemy ship"s health.
    * If the enemy ship's health goes to zero, places an explosion at the ship's location.
    * @param groupManager The class that controls lists and groups of lasers and ships.
   */
   public boolean checkLaserCollision(GroupManager groupManager) {
        GraphicsObject element = checkCollisionPoints(groupManager);
        for (Laser laser : groupManager.getLaserList()) {
            if (element == laser.getLaserImage()) {
                selectedLaser = laser;
                selectedEnemyShip = this;
            }
        }
        if (selectedLaser != null) {
            groupManager.removePlayerLaser(selectedLaser);
            selectedLaser = null;
            decreaseCurrentHealth();
            if (currentHealth <= 0) {
                groupManager.getExplosion().setCenter(getEnemyX(), getEnemyY());
                groupManager.getExplosion().setScale(0.2);
                groupManager.getCanvas().add(groupManager.getExplosion());
                return true;
            }
        }
        return false;
    }

    /**
    * Decreases the enemy ship's health by a set amount.
    */
    private void decreaseCurrentHealth() {
        this.currentHealth -= 10;
    }

   /**
    * Checks several points on the enemy ship and determines whether there is something
    * intersecting with those points.
    * @param groupManager The class that controls lists and groups of lasers and ships.
    * @return A graphics object that is intersecting with one of the enemy ship's weak points.
    */
    private GraphicsObject checkCollisionPoints(GroupManager groupManager) {
        double shipCenterX = enemyShipIcon.getCenter().getX();
        double shipCenterY = enemyShipIcon.getCenter().getY();
        double shipRightX = shipCenterX + 5;
        double shipLowerY = shipCenterY + 5;
        double shipLeftX = shipCenterX - 5;
        double shipUpperY = shipCenterY - 5;

        element = groupManager.getLaserGroup().getElementAt(shipCenterX, shipCenterY);
        if (element == null) {
            element = groupManager.getLaserGroup().getElementAt(shipCenterX, shipLowerY);
        }
        if (element == null) {
            element = groupManager.getLaserGroup().getElementAt(shipRightX, shipCenterY);
        }
        if (element == null) {
            element = groupManager.getLaserGroup().getElementAt(shipLeftX, shipCenterY);
        }
        if (element == null) {
            element = groupManager.getLaserGroup().getElementAt(shipCenterX, shipUpperY);
        }

        return element;
    }
    

    public String toString() {
        return "An enemy ship with center at " 
        + enemyShipIcon.getCenter() + ", an initial speed of " + speed + ", and an initial angle of " + randomAngle;
    }
}
