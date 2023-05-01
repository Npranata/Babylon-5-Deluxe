package space;
import edu.macalester.graphics.*;

/**
 * A class that represents a larger and more dangerous enemy ship. It enters the game when all of the other enemy ships
 * have been destroyed by the player.
 */
public class Boss {
   private Image bossShipIcon = new Image(0,0);
   private final double Y_VELOCITY = 1;
   private Image explosion = new Image("ship-icons/explosion.png");
   private GraphicsObject element;

   private Laser selectedLaser;
   private double centerX, centerY;
   private int bossHealth = 500;

   /**
    * Constructs a boss ship based on parameters that set its center and size. 
    * @param centerX The x-value of the center of the boss ship.
    * @param centerY The y-value of the center of the boss ship.
    * @param scale A value used to set the size of the boss ship.
    */
   public Boss(double centerX, double centerY, double scale) {
       this.centerX = centerX;
       this.centerY = centerY;
       bossShipIcon.setImagePath("ship-icons/bossShip.png");
       bossShipIcon.setScale(scale);
       bossShipIcon.setCenter(centerX, centerY);
   } 

   /**
    * @return The visual representation of the boss ship.
    */
   public Image getBossIcon() {
       return bossShipIcon;
   }

   /**
    * @return The x-value of the center of the boss ship.
    */
   public double getBossX(){
       return bossShipIcon.getCenter().getX();
   }

   /**
    * @return The y-value of the center of the boss ship.
    */
   public double getBossY(){
       return bossShipIcon.getCenter().getY();
   }

  /**
   * Moves the boss ship vertically at a set velocity.
   */
   public void moveBoss() {
       bossShipIcon.moveBy(0, Y_VELOCITY);
       centerX = bossShipIcon.getCenter().getX();
       centerY = bossShipIcon.getCenter().getY();
   }

   /**
    * Removes any player lasers intersecting with the boss ship's weak points, decreasing the boss' health.
    * If the boss' health goes to zero, places an explosion at the ship's location.
    * @param groupManager The class that controls lists and groups of lasers and ships.
   */
   public boolean checkLaserCollision(GroupManager groupManager) {
       GraphicsObject element = checkCollisionPoints(groupManager);
       for (Laser laser : groupManager.getLaserList()) { // Loops through the player ship's lasers
           if (element == laser.getLaserImage()) {
               selectedLaser = laser;
           }
       }
       if (selectedLaser != null) {
           groupManager.removePlayerLaser(selectedLaser);
           selectedLaser = null;
           decreaseCurrentHealth();
           if (bossHealth <= 0) {
               groupManager.getExplosion().setScale(2);
               groupManager.getExplosion().setCenter(centerX, centerY);
               groupManager.getCanvas().add(groupManager.getExplosion());
               return true;
           }
       }
       return false;
   }

   /**
    * Decreases the boss ship's health by a set amount.
    */
   private void decreaseCurrentHealth() {
       bossHealth -= 10;
   }

   /**
    * Checks several points near the center of the boss ship and determines whether there is something
    * intersecting with those points.
    * @param groupManager The class that controls lists and groups of lasers and ships.
    * @return A graphics object that is intersecting with one of the boss ship's weak points.
    */
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
