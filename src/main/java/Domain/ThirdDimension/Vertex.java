package Domain.ThirdDimension;

import Parser.STLParser;

/**
 * The {@code Vertex} class provides methods to interact in a 3-dimensional space, with the vertex itself being a location in this space.
 * It has 3 coordinates (x, y, z).
 *
 * @author Adam Côté
 * @version 0.1
 * @since 2024-09-07
 */
public class Vertex implements Cloneable {
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
     * Constructs a new {@code Vertex} with the values of another {@code Vertex}
     *
     * @param vertex the {@code Vertex} that get cloned.
     */
    public Vertex(Vertex vertex) {
        this(vertex.getX(), vertex.getY(), vertex.getZ());
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

    /**
     * Sets all the coordinates of the {@code Vertex} in function of another one
     *
     * @param vertex the cloned vertex
     */
    public void setVertex(Vertex vertex) {
        setX(vertex.getX());
        setY(vertex.getY());
        setZ(vertex.getZ());
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
     * Subtracts a {@code Vertex} to this instance.
     *
     * @param other the vertex that get subtracted to the other one
     */
    public void subtract(Vertex other) {
        setX(x - other.getX());
        setY(y - other.getY());
        setZ(z - other.getZ());
    }

    /**
     * Adds this instance of {@code Vertex} with another one
     *
     * @param other the {@code Vertex} that get added to the current one
     */
    public void add(Vertex other) {
        setX(x + other.getX());
        setY(y + other.getY());
        setZ(z + other.getZ());
    }

    public static Vertex add(Vertex first, Vertex second) {
        return new Vertex(first.getX() + second.getX(), first.getY() + second.getY(), first.getZ() + second.getZ());
    }

    /**
     * multiply this instance of {@code Vertex} with number
     *
     * @param number the number that get multiplied to the current {@code Vertex}
     */
    public void multiply(double number) {
        setX(x * number);
        setY(y * number);
        setZ(z * number);
    }

    /**
     * Checks if this instance of {@code Vertex} is equal to another one
     *
     * @param o the other {@code Vertex}
     * @return true if the two objects are equals
     */
    @Override
    public boolean equals(Object o) {
        Vertex vertex = (Vertex) o;
        return (this.x == vertex.getX() && this.y == vertex.getY() && this.z == vertex.getZ());
    }

    /**
     * Checks if the current {@code Vertex} is parallel to another one
     *
     * @param v the other {@code Vertex}
     * @return true if the two are parallel
     */
    public boolean isParallel(Vertex v) {
        double kx = 0, ky = 0, kz = 0;
        if (v.getX() != 0) {
            kx = this.x / v.getX();
        } else if (this.x == 0) {
            kx = 1;
        }
        if (v.getY() != 0) {
            ky = this.y / v.getY();
        } else if (this.y == 0) {
            ky = 1;
        }
        if (v.getZ() != 0) {
            kz = this.z / v.getZ();
        } else if (this.z == 0) {
            kz = 1;
        }
        return (isClose(kx, ky, 0.1) && isClose(kx, kz, 0.1));
    }

    /**
     * Checks if two values are close enough
     *
     * @param val1   the first value
     * @param val2   the second value
     * @param margin the given margin in which the value must be close
     * @return true if the two value are within the margin
     */
    private static boolean isClose(double val1, double val2, double margin) {
        return (Math.abs(val1 - val2) < margin);
    }
}
