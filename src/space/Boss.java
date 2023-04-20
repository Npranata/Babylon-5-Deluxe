package space;
import edu.macalester.graphics.*;

import java.awt.Canvas;
import java.util.Random;
import java.util.random.*;
import java.util.List;
import java.util.ArrayList;


public class Boss {
    private Image bossShipIcon = new Image(0,0);
    private double speed,randomAngle,currentXVelocity,currentYVelocity;
    private Random rand = new Random();
    private static final int SPEED = 4;
    private Image explosion = new Image("ship-icons/explosion.png");
    private GraphicsObject element;

    private Laser selectedLaser; 
    private EnemyShip selectedEnemyShip;
    private double originalX, originalY;
    private double centerX, centerY;
    private int bossHealth =200;
    



public Boss(double centerX, double centerY, double scale) {
    this.centerX = centerX;
    this.centerY = centerY;
    bossShipIcon.setImagePath("ship-icons/bossShip.png");
    bossShipIcon.setScale(scale);
    bossShipIcon.setCenter(centerX, centerY);
    
}

public Image getBossIcon(){
    return bossShipIcon;
}

public Point getBossPosition(){
    return bossShipIcon.getCenter();
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
            }
        }
        if (selectedLaser != null) {
            groupManager.removePlayerLaser(selectedLaser);
            selectedLaser = null;
            decreaseCurrentHealth();
            if (bossHealth<= 0) {
                groupManager.getExplosion().setScale(0.2);
                groupManager.getExplosion().setCenter(centerX, centerY);
                groupManager.getCanvas().add(groupManager.getExplosion());
                return true;
            }
        }
        return false;
    }

    private void decreaseCurrentHealth() {
        this.bossHealth -= 10;
    }

    private GraphicsObject checkCollisionPoints(GroupManager groupManager) {
        double shipCenterX = bossShipIcon.getCenter().getX();
        double shipCenterY = bossShipIcon.getCenter().getY();
        double shipRightX = shipCenterX + 10;
        double shipLowerY = shipCenterY + 10;
        double shipLeftX = shipCenterX - 10;
        double shipUpperY = shipCenterY - 10;

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
 

}