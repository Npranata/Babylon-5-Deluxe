package space;

import edu.macalester.graphics.CanvasWindow;

import java.util.ArrayList;
import edu.macalester.graphics.GraphicsGroup;
import edu.macalester.graphics.Image;
import java.util.List;

/**
 * An class used to keep track of the enemy ships and lasers added during the game. Any additions or removals of enemy ships
 * and lasers from the canvas is done through this class.
 */
public class GroupManager {
    
    private CanvasWindow canvas;
    private GraphicsGroup enemyGroup;
    private List<EnemyShip> enemyList;
    private List<Laser> laserList;
    private GraphicsGroup laserGroup;
    private List<Laser> enemyLaserList;
    private GraphicsGroup enemyLaserGroup;
    private Image explosionImage = new Image("ship-icons/explosion.png");


    /**
     * Constructs a group manager for the specified window object.
     * @param canvas The game window that the group manager will be working with.
     */
    public GroupManager(CanvasWindow canvas) {
        laserList = new ArrayList<>();
        laserGroup = new GraphicsGroup();

        enemyLaserList = new ArrayList<>();
        enemyLaserGroup = new GraphicsGroup();

        enemyGroup = new GraphicsGroup();
        enemyList = new ArrayList<>();
        this.canvas = canvas;
    }

    /**
     * @return The explosion image that is placed on the canvas whenever a ship is destroyed.
     */
    public Image getExplosion() {
        return explosionImage;
    }

    /**
     * @return A list that contains all of the lasers that are currently acive in the game.
     */
    public List<Laser> getLaserList() {
        return laserList;
    }

    /**
     * @return A graphics group that contains all of the lasers that are currently active in the game.
     */
    public GraphicsGroup getLaserGroup(){
        return laserGroup;
    }

    /**
     * @return A list that contains all of the enemy ships that are currently active in the game.
     */
    public List<EnemyShip> getEnemyShipList() {
        return enemyList;
    }

    /**
     * @return A graphics group that contains all of the enemy ships that are currently active in the game.
     */
    public GraphicsGroup getEnemyShipGroup(){
        return enemyGroup;
    }

    /**
     * @return A list that contains all of the enemy ships' lasers that are currently acive in the game.
     */
    public List<Laser> getEnemyLaserList() {
        return enemyLaserList;
    }
    
    /**
     * @return A graphics group that contains all of the lasers that are currently active in the game.
     */
    public GraphicsGroup getEnemyLaserGroup(){
        return enemyLaserGroup;
    }

    /**
     * @return The canvas window for the game.
     */
    public CanvasWindow getCanvas() {
        return canvas;
    }

    /**
     * Removes the selected laser from the laser graphics group and list.
     * @param laser The player laser to be removed.
     */
    public void removePlayerLaser(Laser laser) {
        laserGroup.remove(laser.getLaserImage());
        laserList.remove(laser);
    }

    /**
     * Removes the player ship from the canvas.
     * @param playerShip The player ship to be removed.
     */
    public void removePlayerShip(PlayerShip playerShip) {
        canvas.remove(playerShip.getPlayerShipImage());
    }

    /**
     * Removes the selected laser from the enemy laser graphics group and list.
     * @param laser The enemy laser to be removed.
     */
    public void removeEnemyLaser(Laser laser) {
        enemyLaserGroup.remove(laser.getLaserImage());
        enemyLaserList.remove(laser);
    }

    /**
     * Adds the selected laser to the laser graphics group and list.
     * @param laser The laser to be added.
     */
    public void addLaser(Laser laser) {
        laserGroup.add(laser.getLaserImage());
        laserList.add(laser);
    }

    /**
     * Adds the selected laser to the enemy laser graphics group and list.
     * @param laser The enemy laser to be added.
     */
    public void addEnemyLaser(Laser laser) {
        enemyLaserGroup.add(laser.getLaserImage());
        enemyLaserList.add(laser);
    }

    /**
     * Adds an enemy ship to the enemy ship graphics group and list.
     * @param enemyShip The enemy ship to be added.
     */
    public void addEnemyShip(EnemyShip enemyShip) {
        enemyGroup.add(enemyShip.getEnemyShipImage());
        enemyList.add(enemyShip);
    }

    /**
     * Adds a boss ship to the canvas.
     * @param boss The boss ship to be added.
     */
    public void addBossShip(Boss boss){
        canvas.add(boss.getBossIcon());
    }

    /**
     * Removes an enemy ship from the enemy ship graphics group and list.
     * @param enemyShip The enemy ship to be removed.
     */
    public void removeEnemyShip(EnemyShip enemyShip) {
        enemyGroup.remove(enemyShip.getEnemyShipImage());
        enemyList.remove(enemyShip);
    }

    /**
     * @return Whether there are any active enemy ships in the game.
     */
    public boolean enemiesExist() {
        if (enemyList.size() > 0) {
            return true;
        }
        return false;
    }

    /**
     * Removes all the lasers and enemy ships stored in lists and graphics groups.
     */
    public void removeAll() {
        laserGroup.removeAll();
        laserList.clear();

        enemyLaserGroup.removeAll();
        enemyLaserList.clear();

        enemyGroup.removeAll();
        enemyList.clear();
    }
}
