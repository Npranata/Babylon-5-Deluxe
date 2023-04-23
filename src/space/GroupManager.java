package space;

import edu.macalester.graphics.CanvasWindow;

import java.util.ArrayList;
import edu.macalester.graphics.GraphicsGroup;
import edu.macalester.graphics.Image;
import java.util.List;

public class GroupManager {
    
    private CanvasWindow canvas;
    private GraphicsGroup enemyGroup;
    private List<EnemyShip> enemyList;
    private List<Laser> laserList;
    private GraphicsGroup laserGroup;
    private List<Laser> enemyLaserList;
    private GraphicsGroup enemyLaserGroup;
    private Image explosionImage = new Image("ship-icons/explosion.png");


    /*
     * Constructs a brick manager for the specified window object.
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

    public Image getExplosion() {
        return explosionImage;
    }

    public List<Laser> getLaserList() {
        return laserList;
    }

    public GraphicsGroup getLaserGroup(){
        return laserGroup;
    }

    public List<EnemyShip> getEnemyShipList() {
        return enemyList;
    }

    public GraphicsGroup getEnemyShipGroup(){
        return enemyGroup;
    }

    public List<Laser> getEnemyLaserList() {
        return enemyLaserList;
    }
    
    public GraphicsGroup getEnemyLaserGroup(){
        return enemyLaserGroup;
    }

    public CanvasWindow getCanvas() {
        return canvas;
    }

    public void removePlayerLaser(Laser laser) {
        laserGroup.remove(laser.getLaserImage());
        laserList.remove(laser);
    }

    public void removePlayerShip(PlayerShip playerShip) {
        canvas.remove(playerShip.getPlayerShipImage());
    }

    public void removeEnemyLaser(Laser laser) {
        enemyLaserGroup.remove(laser.getLaserImage());
        enemyLaserList.remove(laser);
    }

    public void addLaser(Laser laser) {
        laserGroup.add(laser.getLaserImage());
        laserList.add(laser);
    }

    public void addEnemyLaser(Laser laser) {
        enemyLaserGroup.add(laser.getLaserImage());
        enemyLaserList.add(laser);
    }

    public void addEnemyShip(EnemyShip enemyShip) {
        enemyGroup.add(enemyShip.getEnemyShipImage());
        enemyList.add(enemyShip);
    }

    public void addBossShip(Boss boss){
        canvas.add(boss.getBossIcon());
    }

    public void removeEnemyShip(EnemyShip enemyShip) {
        enemyGroup.remove(enemyShip.getEnemyShipImage());
        enemyList.remove(enemyShip);
    }

    public boolean enemiesExist() {
        if (enemyList.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Removes all the lasers and enemyships stored in lists and GraphicsGroups.
     */
    public void removeAll() {
        for (Laser l : laserList) {
            removePlayerLaser(l);
        }

        for (EnemyShip ES : enemyList) {
            removeEnemyShip(ES);
        }
    }
}
