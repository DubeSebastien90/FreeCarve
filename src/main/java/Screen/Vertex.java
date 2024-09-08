package Screen;

import Parser.STLParser;

public class Vertex {
    private double x;
    private double y;
    private double z;

    public Vertex(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vertex(float[] dimensions) {
        if (dimensions.length != STLParser.DIMENSIONS) {
            throw new ArithmeticException("Array should have " + STLParser.DIMENSIONS + " dimensions");
        }
        this.x = dimensions[0];
        this.y = dimensions[1];
        this.z = dimensions[2];
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public void setVertex(Vertex other){
        this.x = other.getX();
        this.y = other.getY();
        this.z = other.getZ();
    }

    @Override
    public String toString() {
        return "{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }

    public Vertex substraction(Vertex other) {
        return new Vertex(x - other.getX(), y - other.getY(), z - other.getZ());
    }

    public Vertex addition(Vertex other){
        return new Vertex(x + other.getX(), y + other.getY(), z + other.getZ());
    }
}
