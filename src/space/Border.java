package space;

import edu.macalester.graphics.Rectangle;
import edu.macalester.graphics.GraphicsGroup;
import edu.macalester.graphics.CanvasWindow;
import java.awt.Color;

/**
 * Represents one of the borders that surround the gameplay zone. The ball collides with
 * objects created from this class to keep it within the viewable canvas.
 */
public class Border extends GraphicsGroup {
    
    private double upperLeftX, upperLeftY, borderHeight, borderWidth;

    private Rectangle borderShape;

    /**
     * Constructs a border with upper left corner in x/y
     * position with the specified width and height.
     */
    public Border(double upperLeftX, double upperLeftY, double borderWidth, double borderHeight) {
        this.upperLeftX = upperLeftX;
        this.upperLeftY = upperLeftY;
        this.borderWidth = borderWidth;
        this.borderHeight = borderHeight;
        this.setPosition(upperLeftX, upperLeftY);
        borderShape = new Rectangle(upperLeftX, upperLeftY, borderWidth, borderHeight);
        borderShape.setFillColor(Color.black);
        borderShape.setFilled(true);
        borderShape.setStroked(false);
        add(borderShape);
    }

    /**
     * @return The x-value of the border's upper left corner.
     */
    public double getBorderX() {
        return upperLeftX;
    }

    /**
     * @return The thickness of the border on the canvas in the horizontal direction.
     */
    public double getBorderWidth() {
        return borderWidth;
    }
    
    /**
     * Adds the border's shape to the given canvas.
     */
    public void addToCanvas(CanvasWindow canvas) {
        canvas.add(borderShape);
    }

    /**
     * Removes the border's shape from the given canvas.
     */
    public void removeFromCanvas(CanvasWindow canvas) {
        canvas.remove(borderShape);
    }

    public String toString() {
        return "Creates a border with upper left corner at " + upperLeftX + "," + upperLeftY + ", a width of " 
        + borderWidth + ", and a height of " + borderHeight;
    }
}
