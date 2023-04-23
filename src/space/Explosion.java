package space;

import edu.macalester.graphics.CanvasWindow;
import edu.macalester.graphics.Image;

public class Explosion {
    
    private double centerX, centerY;
    private Image explosionIcon = new Image("ship-icons/explosion.png");


    public Explosion (double centerX, double centerY) {
        this.centerX = centerX;
        this.centerY = centerY;
    }
    
    public double getCenterX(){
        return explosionIcon.getCenter().getX();
    }

    public void setScale(double scale) {
        explosionIcon.setScale(scale);
    }

    public double getCenterY(){
        return explosionIcon.getCenter().getY();
    }

    public void removeExplosion(CanvasWindow canvas) {
        canvas.remove(explosionIcon);
    }

    public void addExplosion(CanvasWindow canvas) {
        canvas.add(explosionIcon);
    }
}
