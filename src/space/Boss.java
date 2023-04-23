package space;
import edu.macalester.graphics.*;

import java.awt.Canvas;
import java.util.Random;
import java.util.random.*;
import java.util.List;
import java.util.ArrayList;


public class Boss {
    private Image bossShipIcon = new Image(0,0);
    private final double Y_VELOCITY = 1;
    private Random rand = new Random();
    private Image explosion = new Image("ship-icons/explosion.png");
    private GraphicsObject element;

    private Laser selectedLaser; 
    private EnemyShip selectedEnemyShip;
    private double originalX, originalY, centerX, centerY;
    private int bossHealth = 20;


    public Boss(double centerX, double centerY, double scale) {
        this.centerX = centerX;
        this.centerY = centerY;
        this.originalX = centerX;
        this.originalY = centerY;
        bossShipIcon.setImagePath("ship-icons/bossShip.png");
        bossShipIcon.setScale(scale);
        bossShipIcon.setCenter(centerX, centerY);
    }  

    public Image getBossIcon(){
        return bossShipIcon;
    }

    public double getBossX(){
        return bossShipIcon.getCenter().getX();
    }

    public double getBossY(){
        return bossShipIcon.getCenter().getY();
    }

    public void setBossHealth(int health) {
        bossHealth = health;
    }

    /**
    * Moves the boss according to current dx and dy
    */
    public void moveBoss(){
        if (centerY == 30) {
            return;
        }
        bossShipIcon.moveBy(0, Y_VELOCITY);
        centerX = bossShipIcon.getCenter().getX();
        centerY = bossShipIcon.getCenter().getY();
    }

    /**
     * Removes enemy ship from canvas and adds explosion when laser hits enemy's ship
     * @param groupManager
    */
    public boolean checkLaserCollision(GroupManager groupManager) {
        GraphicsObject element = checkCollisionPoints(groupManager);
        for (Laser laser : groupManager.getLaserList()) {
            if (element == laser.getLaserImage()) {
                selectedLaser = laser;
            }
        }
        if (selectedLaser != null) {
            groupManager.removePlayerLaser(selectedLaser);
            selectedLaser = null;
            decreaseCurrentHealth();
            if (bossHealth<= 0) {
                groupManager.getExplosion().setScale(2);
                groupManager.getExplosion().setCenter(centerX, centerY);
                groupManager.getCanvas().add(groupManager.getExplosion());
                return true;
            }
        }
        return false;
    }

    private void decreaseCurrentHealth() {
        bossHealth -= 10;
    }

    private GraphicsObject checkCollisionPoints(GroupManager groupManager) {
        double shipCenterX = bossShipIcon.getCenter().getX();
        double shipCenterY = bossShipIcon.getCenter().getY();

        double shipRightX = shipCenterX + 30;
        double shipLeftX = shipCenterX - 30;
        double shipSideY = shipCenterY + 100;

        element = groupManager.getLaserGroup().getElementAt(shipCenterX, shipCenterY + 100);

        if (element == null) {
            element = groupManager.getLaserGroup().getElementAt(shipRightX, shipSideY);
        }
        if (element == null) {
            element = groupManager.getLaserGroup().getElementAt(shipLeftX, shipSideY);
        }
        return element;
    }
}