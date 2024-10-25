package UI.Widgets;

import java.awt.*;

/**
 * Represents a point in a 2D space with a specified location, radius, and color.
 * This class provides methods to access and modify the point's properties.
 *
 * @author Adam Côté
 * @version 1.0
 * @since 2024-10-25
 */
public class PersoPoint {
    private double locationX;
    private double locationY;
    private double radius;
    private final Color color = Color.BLACK;

    /**
     * Constructs a PersoPoint with the specified location and radius.
     *
     * @param locationX The x-coordinate of the point.
     * @param locationY The y-coordinate of the point.
     * @param radius    The radius of the point.
     */
    public PersoPoint(double locationX, double locationY, double radius) {
        this.locationX = locationX;
        this.locationY = locationY;
        this.radius = radius;
    }

    /**
     * @return The x-coordinate of the point.
     */
    public double getLocationX() {
        return locationX;
    }

    /**
     * Sets the x-coordinate of the point.
     *
     * @param locationX The new x-coordinate of the point.
     */
    public void setLocationX(double locationX) {
        this.locationX = locationX;
    }

    /**
     * @return The y-coordinate of the point.
     */
    public double getLocationY() {
        return locationY;
    }

    /**
     * Sets the y-coordinate of the point.
     *
     * @param locationY The new y-coordinate of the point.
     */
    public void setLocationY(double locationY) {
        this.locationY = locationY;
    }

    /**
     * @return The radius of the point.
     */
    public double getRadius() {
        return radius;
    }

    /**
     * Sets the radius of the point.
     *
     * @param radius The new radius of the point.
     */
    public void setRadius(double radius) {
        this.radius = radius;
    }

    /**
     * @return The color of the point, which is black by default.
     */
    public Color getColor() {
        return color;
    }

    /**
     * Moves the point to a new location specified by the given coordinates.
     *
     * @param locationX The new x-coordinate of the point.
     * @param locationY The new y-coordinate of the point.
     */
    public void movePoint(double locationX, double locationY) {
        setLocationY(locationY);
        setLocationX(locationX);
    }
}
