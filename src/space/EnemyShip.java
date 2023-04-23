package space;

import edu.macalester.graphics.*;
import java.util.Random;
import java.util.List;

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

    public Image getEnemyShipImage(){
        return enemyShipIcon;
    }

    public double getEnemyX() {
        return enemyShipIcon.getCenter().getX();
    }

    public double getEnemyY() {
        return enemyShipIcon.getCenter().getY();
    }

    public void setEnemyHealth(int health) {
        currentHealth = health;
    }

    /**
     * Sets the enemy spaceship's starting angle and velocity
     */
    private void setAngleAndVelocity(){
        double angleDegrees = rand.nextDouble(280, 320);
        double angleRadians = Math.toRadians(angleDegrees);
        currentXVelocity = Math.cos(angleRadians) * SPEED;
        currentYVelocity = - Math.sin(angleRadians) * SPEED;
    }
    /**
     * Moves the enemy's spaceship according to current dx and dy
     */
    public void moveEnemyShip(CanvasWindow canvas){
        enemyShipIcon.moveBy(currentXVelocity, currentYVelocity);
        if (enemyShipIcon.getCenter().getX() > canvas.getWidth() + 100 || enemyShipIcon.getCenter().getY() > canvas.getHeight() + 300 
        || enemyShipIcon.getCenter().getX() < -200) {
            enemyShipIcon.setCenter(originalX, originalY);
        }
    }

    /**
     * Removes enemy ship from canvas and adds explosion when laser hits enemy's ship
     * @param canvas
     * @param list
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

    private void decreaseCurrentHealth() {
        this.currentHealth -= 10;
    }

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
