package space;

import edu.macalester.graphics.*;

/*
 * Represents the paddle the user uses to bounce the ball upward toward the bricks. The user can use the mouse to move the
 * paddle to the right and left at the same y-level along the lower region of the canvas.
 */
public class PlayerShip {

    private Image playerShipIcon = new Image(0,0);
    private double centerX, centerY, playerScale;
    private int currentHealth = 100;
    private GraphicsObject element;
    private Laser selectedLaser;

    /*
     * Calls the Rectangle constructor to create a paddle with upper left corner at (upperLeftX, upperLeftY),
     * width = paddleWidth, and height = paddleHeight.
     */
    public PlayerShip(double centerX, double centerY, double playerScale) {
        this.centerX = centerX;
        this.centerY = centerY;
        this.playerScale = playerScale;
        playerShipIcon.setImagePath("ship-icons/playerShip.png");
        playerShipIcon.setCenter(centerX, centerY);
        playerShipIcon.setScale(playerScale);
    }

    public void setLocation(Point mouse) {
        playerShipIcon.setCenter(mouse);
    }

    public void decreasePlayerHealth(){
        this.currentHealth -= 10;
    }

    /*
     * @return The x-value of the upper left corner the paddle.
     */
    public double getCenterX() {
        return playerShipIcon.getCenter().getX();
    }

    public Point getPosition() {
        return playerShipIcon.getCenter();
    }

    public double getCenterY() {
        return playerShipIcon.getCenter().getY();
    }

    public Image getPlayerShipImage() {
        return playerShipIcon;
    }

    public int getPlayerHealth() {
        return currentHealth;
    }

    private GraphicsObject checkCollisionPoints(GroupManager groupManager) {
        double shipCenterX = playerShipIcon.getCenter().getX();
        double shipCenterY = playerShipIcon.getCenter().getY();
        double shipRightX = shipCenterX + 20; //was 5
        double shipLowerY = shipCenterY + 5;
        double shipLeftX = shipCenterX - 20; //was 5
        double shipUpperY = shipCenterY - 5;

        element = groupManager.getEnemyLaserGroup().getElementAt(shipCenterX, shipCenterY);
        if (element == null) {
            element = groupManager.getEnemyLaserGroup().getElementAt(shipCenterX, shipLowerY);
        }
        if (element == null) {
            element = groupManager.getEnemyLaserGroup().getElementAt(shipRightX, shipCenterY + 15); 
        }
        if (element == null) {
            element = groupManager.getEnemyLaserGroup().getElementAt(shipLeftX, shipCenterY + 15);
        }
        if (element == null) {
            element = groupManager.getEnemyLaserGroup().getElementAt(shipCenterX, shipUpperY);
        }

        return element;
    }

    public boolean checkLaserCollision(GroupManager groupManager) {
        GraphicsObject element = checkCollisionPoints(groupManager);
        for (Laser laser : groupManager.getEnemyLaserList()) {
            if (element == laser.getLaserImage()) {
                selectedLaser = laser;
            }
        }
        if (selectedLaser != null) {
            groupManager.removeEnemyLaser(selectedLaser);
            selectedLaser = null;
            decreasePlayerHealth();
            if (currentHealth == 0) {
                groupManager.getExplosion().setScale(0.2);
                groupManager.getExplosion().setCenter(getCenterX(), getCenterY());
                groupManager.getCanvas().add(groupManager.getExplosion());
                return true;
            }
        }
        return false;
    }

    public String toString() {
        return "";
    }

}