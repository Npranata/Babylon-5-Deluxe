package space;

import edu.macalester.graphics.*;

public class Ship {
    
    private double centerX, centerY, scale;
    private Image shipIcon = new Image(0,0);
    
    public Ship(double centerX, double centerY, double scale) {
        this.centerX = centerX;
        this.centerY = centerY;
        this.scale = scale;
        shipIcon.setImagePath("ship-icons/playerShip.png");
        shipIcon.setCenter(centerX, centerY);
        shipIcon.setScale(scale);
    }
}
