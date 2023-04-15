package space;
import edu.macalester.graphics.*;

import java.awt.Canvas;
import java.util.Random;
import java.util.random.*;
import java.util.List;
import java.util.ArrayList;


public class Boss extends Ship {
    private Image bossShapeIcon = new Image(0,0);
    private double speed,randomAngle,currentXVelocity,currentYVelocity;
    private Random rand = new Random();
    private static final int SPEED = 4;
    private Image explosion = new Image("ship-icons/explosion.png");

    private Laser selectedLaser; 
    private EnemyShip selectedEnemyShip;
    private double originalX, originalY;
    private double upperLeftX;
    private double upperLeftY;



public Boss(double upperLeftX, double upperLeftY, double scale, double randomAngle, double speed) {
    super(upperLeftX, upperLeftY, scale);  
    //bossShipIcon.setImagePath("ship-icons/bossShip.png");
    
}


}