package space;

import edu.macalester.graphics.CanvasWindow;

import java.util.ArrayList;
import edu.macalester.graphics.GraphicsGroup;
import java.util.List;

public class GroupManager {
    
    private CanvasWindow canvas;
    private GraphicsGroup enemyGroup;
    private List<EnemyShip> enemyList;
    private List<Laser> laserList;
    private GraphicsGroup laserGroup;

    /*
     * Constructs a brick manager for the specified window object.
     */
    public GroupManager(CanvasWindow canvas) {
        laserList = new ArrayList<>();
        laserGroup = new GraphicsGroup();
        enemyGroup = new GraphicsGroup();
        enemyList = new ArrayList<>();
        this.canvas = canvas;
    }

    public List<Laser> getLaserList() {
        return laserList;
    }

    public List<EnemyShip> getEnemyShipList() {
        return enemyList;
    }

    public GraphicsGroup getEnemyShipGroup(){
        return enemyGroup;
    }

    public GraphicsGroup getLaserGroup(){
        return laserGroup;
    }

    public CanvasWindow getCanvas() {
        return canvas;
    }

    public void removeLaser(Laser laser) {
        laserGroup.remove(laser.getLaserImage());
        laserList.remove(laser);
    }


    public void addLaser(Laser laser) {
        laserGroup.add(laser.getLaserImage());
        laserList.add(laser);
    }

    public void addEnemyShip(EnemyShip enemyShip) {
        enemyGroup.add(enemyShip.getEnemyShipImage());
        enemyList.add(enemyShip);
    }

    public void removeEnemyShip(EnemyShip enemyShip) {
        enemyGroup.remove(enemyShip.getEnemyShipImage());
        enemyList.remove(enemyShip);
    }
}
