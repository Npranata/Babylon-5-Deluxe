package space;

import edu.macalester.graphics.*;

/*
 * Represents the player ship the user uses. The user can use the mouse to move the 
 * player ship.
 */
public class PlayerShip {

    private Image playerShipIcon = new Image(0,0);
    private double centerX, centerY, playerScale;
    private int currentHealth;
    private GraphicsObject element;
    private Laser selectedLaser;

    /**
    * Constructs a boss ship based on parameters that set its center and size. 
    * @param centerX The x-value of the center of the player's ship.
    * @param centerY The y-value of the center of the player's ship.
    * @param playerScale A value used to set the size of the player's  ship.
    */
    public PlayerShip(double centerX, double centerY, double playerScale) {
        this.centerX = centerX;
        this.centerY = centerY;
        this.playerScale = playerScale;
        playerShipIcon.setImagePath("ship-icons/playerShip.png");
        playerShipIcon.setCenter(centerX, centerY);
        playerShipIcon.setScale(playerScale);
    }

    /*
    * A method that sets the player ship's center location to where the mouse arrow is.
    */
    public void setLocation(Point mouse) {
        playerShipIcon.setCenter(mouse);
    }
    /*
    * A method that deducts the player ship's current health by 10. 
    */
    public void decreasePlayerHealth(){
        this.currentHealth -= 10;
    }
    /**
    * @return The x-value of the center of the boss ship.
    */
    public double getCenterX() {
        return playerShipIcon.getCenter().getX();
    }

    /*
    * Returns the player ship's center coordinates.
    */
    public Point getPosition() {
        return playerShipIcon.getCenter();
    }

    /**
    * @return The y-value of the center of the boss ship.
    */
    public double getCenterY() {
        return playerShipIcon.getCenter().getY();
    }
    /**
    * @return The visual representation of the boss ship.
    */
    public Image getPlayerShipImage() {
        return playerShipIcon;
    }

    /*
    * A method that returns the player's current health.
    */
    public int getPlayerHealth() {
        return currentHealth;
    }

    /*
    * A method that sets what the player health is. 
    */
    public void setPlayerHealth(int currentHP) {
        currentHealth = currentHP;
    }

     /**
    * Checks several points near the center of the player's ship and determines whether there is something
    * intersecting with those points.
    * @param groupManager The class that controls lists and groups of lasers and ships.
    * @return A graphics object that is intersecting with one of the boss ship's weak points.
    */
    private GraphicsObject checkCollisionPoints(GroupManager groupManager) {
        double shipCenterX = playerShipIcon.getCenter().getX();
        double shipCenterY = playerShipIcon.getCenter().getY();
        double shipRightX = shipCenterX + 20; 
        double shipLowerY = shipCenterY + 5;
        double shipLeftX = shipCenterX - 20; 
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
    
   /**
    * Removes any player lasers intersecting with the player ship's points, decreasing the player' health.
    * If the player's health goes to zero, places the explosion on the player's ship.
    * @param groupManager The class that controls lists and groups of lasers and ships.
   */
    public boolean checkLaserCollision(GroupManager groupManager) {
        GraphicsObject element = checkCollisionPoints(groupManager);
        for (Laser laser : groupManager.getEnemyLaserList()) { // Loops through the enemy ships' lasers
            if (element == laser.getLaserImage()) {
                selectedLaser = laser;
            }
        }
        if (selectedLaser != null) {
            groupManager.removeEnemyLaser(selectedLaser);
            selectedLaser = null;
            decreasePlayerHealth();
            if (currentHealth <= 0) {
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