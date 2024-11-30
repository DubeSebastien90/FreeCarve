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
    public boolean drawMM(Graphics2D graphics2D, Rendering2DWindow renderer, boolean canSelect) {
        boolean selected = false;
        Point2D temp = renderer.mmTopixel(new Point2D.Double(locationX, locationY));
        double diamPixel = this.radius * renderer.getZoom() * 2;
        if (mouse_on_top(renderer.getMousePt().getX(), renderer.getMousePt().getY(), temp.getX(), temp.getY(), diamPixel / 2) && canSelect) {
            graphics2D.setColor(Color.MAGENTA);
            selected = true;
        } else {
            graphics2D.setColor(this.color);
        }
        graphics2D.fillOval((int) (temp.getX() - diamPixel / 2.0), ((int) (temp.getY() - diamPixel / 2.0)),((int) diamPixel), ((int) diamPixel));

        return selected;
    }

    public static boolean mouse_on_top(double mouse_x, double mouse_y, double pointX, double pointY, double pointRadius) {
        double dist = Math.sqrt(Math.pow(mouse_x - pointX, 2) + Math.pow(mouse_y - pointY, 2));
        return dist <= pointRadius;
    }

    /**
     * Draws a line between this PersoPoint and another, converts MM to pixel first
     *
     * @param graphics2D
     * @param renderer
     * @param to         PersoPoint to draw to
     */
    public boolean drawLineMM(Graphics2D graphics2D, Rendering2DWindow renderer, PersoPoint to, boolean canSelect) {
        Point2D temp1 = renderer.mmTopixel(new Point2D.Double(locationX, locationY));
        Point2D temp2 = renderer.mmTopixel(new Point2D.Double(to.locationX, to.locationY));
        boolean selected = false;
        if (canSelect && mouse_on_top_line(renderer.getMousePt().getX(), renderer.getMousePt().getY(), temp1, temp2, (radius * renderer.getZoom()) / PRECISION)) {
            selected = true;
            graphics2D.setColor(Color.MAGENTA);
        } //else graphics2D.setColor(this.color);
        graphics2D.drawLine((int) (temp1.getX()), (int) (temp1.getY()),
                (int) (temp2.getX()), (int) (temp2.getY()));
        return selected;
    }

    public static boolean mouse_on_top_line(double mouseX, double mouseY, Point2D point_from, Point2D point_to, double _radius) {

        double dx = point_to.getX() - point_from.getX();
        double dy = point_to.getY() - point_from.getY();

        double length = Math.sqrt(dx * dx + dy * dy);

        double ux = dx / length;
        double uy = dy / length;

        double nx = -uy * _radius;
        double ny = ux * _radius;

        double x1 = point_from.getX() - nx;
        double y1 = point_from.getY() - ny;

        double x2 = point_from.getX() + nx;
        double y2 = point_from.getY() + ny;

        double x3 = point_to.getX() + nx;
        double y3 = point_to.getY() + ny;

        double x4 = point_to.getX() - nx;
        double y4 = point_to.getY() - ny;

        return pointInRectangle(mouseX, mouseY, x1, y1, x2, y2, x3, y3, x4, y4);
    }

    public static boolean pointInRectangle(double px, double py,
                                     double x1, double y1,
                                     double x2, double y2,
                                     double x3, double y3,
                                     double x4, double y4) {

        double cross1 = (px - x1) * (y2 - y1) - (py - y1) * (x2 - x1);
        double cross2 = (px - x2) * (y3 - y2) - (py - y2) * (x3 - x2);
        double cross3 = (px - x3) * (y4 - y3) - (py - y3) * (x4 - x3);
        double cross4 = (px - x4) * (y1 - y4) - (py - y4) * (x1 - x4);

        return (cross1 >= 0 && cross2 >= 0 && cross3 >= 0 && cross4 >= 0) ||
                (cross1 <= 0 && cross2 <= 0 && cross3 <= 0 && cross4 <= 0);
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
}
