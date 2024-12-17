package UI.Widgets;

import UI.Display2D.Rendering2DWindow;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

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
    private Valid valid;
    public final double PRECISION = 1.7;
    private boolean hoveredView;

    private Color color = Color.BLACK;
    private boolean filled;

    public enum Valid {
        VALID,
        NOT_VALID,
    }

    /**
     * Constructs a PersoPoint with the specified location and radius.
     *
     * @param locationX The x-coordinate of the point.
     * @param locationY The y-coordinate of the point.
     * @param radius    The radius of the point.
     */
    public PersoPoint(double locationX, double locationY, double radius, boolean filled) {
        this.locationX = locationX;
        this.locationY = locationY;
        this.radius = radius;
        this.filled = filled;
        this.valid = Valid.NOT_VALID;
    }

    /**
     * Constructs a PersoPoint with the specified location and radius.
     *
     * @param locationX The x-coordinate of the point.
     * @param locationY The y-coordinate of the point.
     * @param radius    The radius of the point.
     * @param color     The color of the point.
     */
    public PersoPoint(double locationX, double locationY, double radius, boolean filled, Color color) {
        this.locationX = locationX;
        this.locationY = locationY;
        this.radius = radius;
        this.filled = filled;
        this.color = color;
        this.valid = Valid.NOT_VALID;
        this.hoveredView = false;
    }

    /**
     * Copy constructor
     *
     * @param persoPoint PersoPoint to be copied
     */
    public PersoPoint(PersoPoint persoPoint) {
        this.locationX = persoPoint.locationX;
        this.locationY = persoPoint.locationY;
        this.radius = persoPoint.radius;
        this.filled = persoPoint.filled;
        this.color = persoPoint.color;
        this.valid = persoPoint.valid;
        this.hoveredView = persoPoint.hoveredView;
    }

    public void setHoveredView(boolean hoveredView) {
        this.hoveredView = hoveredView;
    }

    public boolean isHoveredView() {
        return hoveredView;
    }

    /**
     * Draws the PersoPoint, DOESN'T CONVERT it's coordinate
     *
     * @param graphics2D
     * @param renderer
     */
    public void draw(Graphics2D graphics2D, Rendering2DWindow renderer) {
        graphics2D.setColor(this.color);
        graphics2D.fillOval((int) (this.getLocationX() - this.getRadius() / 2.0), ((int) (this.getLocationY() - this.getRadius() / 2.0)),
                ((int) this.getRadius()), ((int) this.getRadius()));
    }

    /**
     * Draws the PersoPoint, converts it's coordinate from MM to pixels
     *
     * @param graphics2D
     * @param renderer
     */
    public void drawMM(Graphics2D graphics2D, Rendering2DWindow renderer) {
        Point2D temp = renderer.mmTopixel(new Point2D.Double(locationX, locationY));
        double diamPixel = this.radius * renderer.getZoom() * 2;
        graphics2D.fillOval((int) (temp.getX() - diamPixel / 2.0), ((int) (temp.getY() - diamPixel / 2.0)),((int) diamPixel), ((int) diamPixel));
    }

    /**
     * Draws the PersoPoint, converts it's coordinate from MM to pixels,
     * set it's color to it's internal color
     *
     * @param graphics2D
     * @param rendering2DWindow
     */
    public void drawMMWithPersoPointColor(Graphics2D graphics2D, Rendering2DWindow rendering2DWindow){
        graphics2D.setColor(getColor());
        drawMM(graphics2D, rendering2DWindow);
    }

    /**
     * Draws a line between this PersoPoint and another, converts MM to pixel first
     *
     * @param graphics2D
     * @param renderer
     * @param to         PersoPoint to draw to
     */
    public void drawLineMM(Graphics2D graphics2D, Rendering2DWindow renderer, PersoPoint to) {
        Point2D temp1 = renderer.mmTopixel(new Point2D.Double(locationX, locationY));
        Point2D temp2 = renderer.mmTopixel(new Point2D.Double(to.locationX, to.locationY));
        graphics2D.drawLine((int) (temp1.getX()), (int) (temp1.getY()), (int) (temp2.getX()), (int) (temp2.getY()));
    }

    /**
     * @return The filled value of the point.
     */
    public boolean getFilled() {
        return filled;
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
     * Set the color of the point
     *
     * @param color new color of the point
     */
    public void setColor(Color color) {
        this.color = color;
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

    /**
     * Get state of the PersoPoint
     *
     * @return State
     */
    public Valid getValid() {
        return valid;
    }

    /**
     * Set state of the PersoPoint
     *
     * @param valid new State
     */
    public void setValid(Valid valid) {
        this.valid = valid;
    }

    /**
     * Get the distance from origin
     *
     * @return value of distance from origin
     */
    public double getDistance() {
        return Math.sqrt(locationX * locationX + locationY * locationY);
    }

    /**
     * Get distance from point
     *
     * @param other other point
     * @return the distance this point and the other
     */
    public double getDistance(PersoPoint other) {
        return Math.sqrt(Math.pow(locationX - other.getLocationX(), 2) +
                Math.pow(locationY - other.getLocationY(), 2));
    }

    @Override
    public String toString() {
        return "(" + getLocationX() + " - " + getLocationY() + ")";
    }
}
