package Screen;

import Parser.STLParser;

/**
 * The {@code Vertex} class provides methods to interact in a 3-dimensional space, with the vertex itself being a location in this space.
 * It has 3 coordinates (x, y, z).
 *
 * @author Adam Côté
 * @version 0.1
 * @since 2024-09-07
 */
public class Vertex {
    private double x;
    private double y;
    private double z;

    /**
     * Constructs a {@code Vertex} with 3 space coordinates.
     *
     * @param x the position on the x-axis
     * @param y the position on the y-axis
     * @param z the position on the z-axis
     */
    public Vertex(double x, double y, double z) {
        setX(x);
        setY(y);
        setZ(z);
    }

    /**
     * Constructs a {@code Vertex} with the values in a float array.
     * <b>Note that the float array in the parameter should have the correct dimensions.</b>
     *
     * @param dimensions the array containing the position on each axis
     */
    public Vertex(float[] dimensions) {
        if (dimensions.length != STLParser.DIMENSIONS) {
            throw new ArithmeticException("Array should have " + STLParser.DIMENSIONS + " dimensions");
        }
        this.x = dimensions[0];
        this.y = dimensions[1];
        this.z = dimensions[2];
    }

    /**
     * @return the x position
     */
    public double getX() {
        return x;
    }

    /**
     * Sets the x value to a new one
     *
     * @param x the new position on the x-axis
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * @return the y position
     */
    public double getY() {
        return y;
    }

    /**
     * Sets the y value to a new one
     *
     * @param y the new position on the y-axis
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * @return the z position
     */
    public double getZ() {
        return z;
    }

    /**
     * Sets the z value to a new one
     *
     * @param z the new position on the z-axis
     */
    public void setZ(double z) {
        this.z = z;
    }


    public void setVertex(Vertex other){
        this.x = other.getX();
        this.y = other.getY();
        this.z = other.getZ();
    }


    /**
     * @return a string representation of the {@code Vertex} object
     */

    @Override
    public String toString() {
        return "{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }

    /**
     * Subtracts this instance of {@code Vertex} with another one.
     *
     * @param other the vertex that get subtracted to the current {@code Vertex}
     * @return The resulting Vertex
     */
    public Vertex substraction(Vertex other) {
        return new Vertex(x - other.getX(), y - other.getY(), z - other.getZ());
    }

    public Vertex addition(Vertex other){
        return new Vertex(x + other.getX(), y + other.getY(), z + other.getZ());
    }

    public Vertex multiplication(double number){
        return new Vertex(x*number,y*number,z*number);
    }
}
