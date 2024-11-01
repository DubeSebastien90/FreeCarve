package Domain.ThirdDimension;

import Parser.ParsedSTL;

import java.awt.*;
import java.util.Arrays;
import java.util.Objects;

/**
 * The {@code Triangle} class provides methods to render a triangle on the screen using a {@code JPanel} as a container.
 * Each triangle is composed of 3 vertices, a color, and a normal, which can be calculated later if not known before creation.
 *
 * @author Adam Côté
 * @version 1.0
 * @since 2024-09-07
 */
public class Triangle {
    private Vertex[] vertices;
    private Vertex normal;
    private Color color;

    /**
     * Constructs a new {@code Triangle} with the specified vertices and color.
     *
     * @param vertex1 the first vertex of the triangle
     * @param vertex2 the second vertex of the triangle
     * @param vertex3 the third vertex of the triangle
     * @param normal  the normal of the triangle
     * @param color   the color of the triangle
     */
    public Triangle(Vertex vertex1, Vertex vertex2, Vertex vertex3, Vertex normal, Color color) {
        setVertices(new Vertex[]{vertex1, vertex2, vertex3});
        setNormal(normal);
        setColor(color);
    }

    /**
     * Constructs a new {@code Triangle} with the specified vertices and color.
     * The default color is blue. The normal will be initialized at (0,0,0).
     *
     * @param vertex1 the first vertex of the triangle
     * @param vertex2 the second vertex of the triangle
     * @param vertex3 the third vertex of the triangle
     */
    public Triangle(Vertex vertex1, Vertex vertex2, Vertex vertex3) {
        this(vertex1, vertex2, vertex3, new Vertex(0, 0, 0), Color.BLUE);
    }

    /**
     * Construct a new {@code Triangle} that is a deep copy of another one. Similar to a clone function.
     *
     * @param triangle the other triangle.
     */
    public Triangle(Triangle triangle) {
        this(new Vertex(triangle.getVertex(0)), new Vertex(triangle.getVertex(1)), new Vertex(triangle.getVertex(2)), new Vertex(triangle.getNormal()), new Color(triangle.getColor().getRed(), triangle.getColor().getGreen(), triangle.getColor().getBlue()));
    }

    /**
     * Returns the vertices of the triangle.
     *
     * @return the vertices of the triangle
     */
    public Vertex[] getVertices() {
        return vertices;
    }

    /**
     * Returns the vertices of the triangle.
     *
     * @return the vertex at the index of the triangle
     */
    public Vertex getVertex(int index) {
        return vertices[index];
    }

    /**
     * Sets the first vertex of the triangle.
     *
     * @param vertices the new first vertex of the triangle
     */
    public void setVertices(Vertex[] vertices) {
        this.vertices = vertices;
    }

    /**
     * Sets the vertex at the index of the triangle.
     *
     * @param vertex the new vertex of the triangle
     * @param index the index of the triangle to set  
     */
    public void setVertex(Vertex vertex, int index) {
        this.vertices[index] = vertex;
    }

    /**
     * Returns the color of the triangle.
     *
     * @return the color of the triangle
     */
    public Color getColor() {
        return color;
    }

    /**
     * Sets the color of the triangle.
     *
     * @param color the new color of the triangle
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * Returns the normal of the triangle.
     *
     * @return the normal of the triangle
     */
    public Vertex getNormal() {
        return normal;
    }

    /**
     * Sets the normal of the triangle.
     *
     * @param normal the new normal of the triangle
     */
    public void setNormal(Vertex normal) {
        this.normal = normal;
    }

    /**
     * Finds a rectangular area around the triangle, it needs a maximum width and maximum height, the minimum x and y are 0
     *
     * @param maxWidth  The maximum width the x in the area should not exceed.
     * @param maxHeight The maximum height the y in the area should not exceed.
     * @return a list with the minimum x, maximum x, minimum y, maximum y
     */
    public int[] findBoundingRectangle(int maxWidth, int maxHeight) {
        int minX = (int) Math.max(0, Math.min(getVertex(0).getX(), Math.min(getVertex(1).getX(), getVertex(2).getX())));
        int maxX = (int) Math.min(maxWidth, Math.max(getVertex(0).getX(), Math.max(getVertex(1).getX(), getVertex(2).getX())));
        int minY = (int) Math.max(0, Math.min(getVertex(0).getY(), Math.min(getVertex(1).getY(), getVertex(2).getY())));
        int maxY = (int) Math.min(maxHeight, Math.max(getVertex(0).getY(), Math.max(getVertex(1).getY(), getVertex(2).getY())));

        return new int[]{minX, maxX, minY, maxY};
    }

    /**
     * Returns a string representation of the {@code Triangle} object.
     * The string includes the vertices, color, and normal of the triangle.
     *
     * @return a string representation of the {@code Triangle} object
     */
    @Override
    public String toString() {
        return "Triangle{" +
                vertices[0] +
                vertices[1] +
                vertices[2] +
                ", normal =" + normal +
                ", color =" + color +
                '}' + "\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Triangle triangle)) return false;
        return Arrays.equals(getVertices(), triangle.getVertices()) && Objects.equals(getNormal(), triangle.getNormal()) && getColor().equals(triangle.getColor());
    }

    /**
     * Returns a list of {@code Triangle} objects created from a parsed .STL file. A color needs to be specified.
     *
     * @param parsedSTL the parsed .STL file
     * @param color     the color of all the triangles
     * @return a list of triangles from the parsed .STL file
     */
    public static Triangle[] fromParsedSTL(ParsedSTL parsedSTL, Color color) {
        Triangle[] triangles = new Triangle[parsedSTL.normals().length];
        for (int i = 0; i < parsedSTL.normals().length; i++) {
            triangles[i] = new Triangle(
                    new Vertex(parsedSTL.vertices()[i * 3]),
                    new Vertex(parsedSTL.vertices()[i * 3 + 1]),
                    new Vertex(parsedSTL.vertices()[i * 3 + 2]),
                    new Vertex(parsedSTL.normals()[i]),
                    color);
        }
        return triangles;
    }

    /**
     * Finds the barycentric coordinates of a specific point inside a triangle using the Edge approach.
     * If the point is not within the triangle, invalid barycentric coordinates will be returned.
     *
     * @param pointX the x-coordinate of the point
     * @param pointY the y-coordinate of the point
     * @return the barycentric coordinates of the point
     */
    public Vertex findBarycentric(double pointX, double pointY) {

        Vertex v1 = getVertex(0);
        Vertex v2 = getVertex(1);
        Vertex v3 = getVertex(2);

        double denominator = (v2.getY() - v3.getY()) * (v1.getX() - v3.getX()) + (v3.getX() - v2.getX()) * (v1.getY() - v3.getY());
        double firstBary = ((v2.getY() - v3.getY()) * (pointX - v3.getX()) + (v3.getX() - v2.getX()) * (pointY - v3.getY())) / denominator;
        double secondBary = ((v3.getY() - v1.getY()) * (pointX - v3.getX()) + (v1.getX() - v3.getX()) * (pointY - v3.getY())) / denominator;
        double thirdBary = (1 - firstBary - secondBary);

        return new Vertex(firstBary, secondBary, thirdBary);
    }

    /**
     * Recalculates the normal of this instance of {@code Triangle} and sets its normal to the recalculated value.
     * The normal is calculated using the vector cross product of two edges of the triangle.
     * Note that the order of the vertices will affect the orientation of the normal.
     * The resulting vertex is normalized.
     */
    public void calculateNormal() {
        Vertex u = new Vertex(getVertex(1));
        u.subtract(getVertex(0));
        Vertex v = new Vertex(getVertex(2));
        v.subtract(getVertex(0));
        double normalX = u.getY() * v.getZ() - u.getZ() * v.getY();
        double normalY = u.getZ() * v.getX() - u.getX() * v.getZ();
        double normalZ = u.getX() * v.getY() - u.getY() * v.getX();
        double magnitude = Math.sqrt(Math.pow(normalX, 2) + Math.pow(normalY, 2) + Math.pow(normalZ, 2));
        if (magnitude == 0) {
            setNormal(new Vertex(0, 0, 0));
        } else {
            setNormal(new Vertex(normalX / magnitude, normalY / magnitude, normalZ / magnitude));
        }
    }

}