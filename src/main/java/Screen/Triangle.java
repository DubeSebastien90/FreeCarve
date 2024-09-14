package Screen;

import Parser.ParsedSTL;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.util.List;

/**
 * The {@code Triangle} class provides methods to render a triangle on the screen using a {@code JPanel} as a container.
 * Each triangle is composed of 3 vertices, a color, and a normal, which can be calculated later if not known before creation.
 *
 * @author Adam Côté, with the help of the links in the see also section.
 * @version 1.0
 * @since 2024-09-07
 */
public class Triangle {
    private Vertex vertex1;
    private Vertex vertex2;
    private Vertex vertex3;
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
        setVertex1(vertex1);
        setVertex2(vertex2);
        setVertex3(vertex3);
        setNormal(normal);
        setColor(color);
    }

    /**
     * Construct a new {@code Triangle} with the specified vertices and color but no normal.
     * The normal will be initialized at (0,0,0).
     *
     * @param vertex1 the first vertex of the triangle
     * @param vertex2 the second vertex of the triangle
     * @param vertex3 the third vertex of the triangle
     * @param color   the color of the triangle
     */
    public Triangle(Vertex vertex1, Vertex vertex2, Vertex vertex3, Color color) {
        this(vertex1, vertex2, vertex3, new Vertex(0, 0, 0), color);
    }

    /**
     * Constructs a new {@code Triangle} with the specified vertices and color.
     * The default color is blue.
     *
     * @param vertex1 the first vertex of the triangle
     * @param vertex2 the second vertex of the triangle
     * @param vertex3 the third vertex of the triangle
     * @param normal  the normal of the triangle
     */
    public Triangle(Vertex vertex1, Vertex vertex2, Vertex vertex3, Vertex normal) {
        this(vertex1, vertex2, vertex3, normal, Color.BLUE);
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
     * Construct a new {@code Triangle} with the value of another one. Similar to a clone function.
     *
     * @param triangle the other triangle.
     */
    public Triangle(Triangle triangle) {
        this(triangle.getVertex1(), triangle.getVertex2(), triangle.getVertex3(), triangle.getNormal(), triangle.getColor());
    }

    /**
     * Returns the first vertex of the triangle.
     *
     * @return the first vertex of the triangle
     */
    public Vertex getVertex1() {
        return vertex1;
    }

    /**
     * Sets the first vertex of the triangle.
     *
     * @param vertex1 the new first vertex of the triangle
     */
    public void setVertex1(Vertex vertex1) {
        this.vertex1 = vertex1;
    }

    /**
     * Returns the second vertex of the triangle.
     *
     * @return the second vertex of the triangle
     */
    public Vertex getVertex2() {
        return vertex2;
    }

    /**
     * Sets the second vertex of the triangle.
     *
     * @param vertex2 the new second vertex of the triangle
     */
    public void setVertex2(Vertex vertex2) {
        this.vertex2 = vertex2;
    }

    /**
     * Returns the third vertex of the triangle.
     *
     * @return the third vertex of the triangle
     */
    public Vertex getVertex3() {
        return vertex3;
    }

    /**
     * Sets the third of the triangle.
     *
     * @param vertex3 the new third vertex of the triangle
     */
    public void setVertex3(Vertex vertex3) {
        this.vertex3 = vertex3;
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
     * Paints all the {@code Triangle} objects in a {@code List} in 3D on a {@code JPanel}.
     * To be painted on the JPanel, the triangles must be drawn using a {@code Graphics2D} object.
     * The {@code Graphics2D} object must already be associated with the JPanel.<br/><br/>
     * The function uses a depth map for to be sure the triangles are displayed correctly,
     * barycentric coordinates to paint the pixels of the triangle and normal vectors to implement base shading.
     * <br/><br/>
     *
     * @param panel      the panel on which the triangles will be painted
     * @param graphics2D the {@code Graphics2D} object associated with the panel
     * @param meshes     the list of meshes to be drawn
     */
    public static Mesh printTriangles(JPanel panel, Graphics2D graphics2D, List<Mesh> meshes, Vertex mousePos) {
        BufferedImage img = new BufferedImage(panel.getWidth(), panel.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Double[][] pixelsDepthMap = new Double[img.getWidth()][img.getHeight()];
        double panelHalfWidth = panel.getWidth() / 2.0;
        double panelHalfHeight = panel.getHeight() / 2.0;
        Mesh meshSelected = null;

        for (Mesh m : meshes) {

            for (Triangle t : m.getTrianglesList()) {
                double[] newCoordinates = {
                        t.getVertex1().getX() + panelHalfWidth, t.getVertex1().getY() + panelHalfHeight,
                        t.getVertex2().getX() + panelHalfWidth, t.getVertex2().getY() + panelHalfHeight,
                        t.getVertex3().getX() + panelHalfWidth, t.getVertex3().getY() + panelHalfHeight};

                Vertex[] newVertices = {
                        new Vertex(newCoordinates[0], newCoordinates[1], t.getVertex1().getZ()),
                        new Vertex(newCoordinates[2], newCoordinates[3], t.getVertex2().getZ()),
                        new Vertex(newCoordinates[4], newCoordinates[5], t.getVertex3().getZ())};

                Triangle newTriangle = new Triangle(newVertices[0], newVertices[1], newVertices[2], t.getNormal(), t.getColor());

                int minX = (int) Math.max(0, Math.min(newCoordinates[0], Math.min(newCoordinates[2], newCoordinates[4])));
                int maxX = (int) Math.min(img.getWidth(), Math.max(newCoordinates[0], Math.max(newCoordinates[2], newCoordinates[4])));

                int minY = (int) Math.max(0, Math.min(newCoordinates[1], Math.min(newCoordinates[3], newCoordinates[5])));
                int maxY = (int) Math.min(img.getHeight(), Math.max(newCoordinates[1], Math.max(newCoordinates[3], newCoordinates[5])));

                for (int y = minY; y <= maxY; y++) {
                    for (int x = minX; x <= maxX; x++) {
                        Vertex bary = findBarycentric(newTriangle, x, y);
                        if (isInBarycentric(bary)) {
                            double depth = bary.getX() * newVertices[0].getZ() + bary.getY() * newVertices[1].getZ() + bary.getZ() * newVertices[2].getZ();

                            if (x < panel.getWidth() && y < panel.getHeight()) {
                                Double existingPixel = pixelsDepthMap[x][y];
                                if ((existingPixel == null || existingPixel < depth)) {
                                    pixelsDepthMap[x][y] = depth;
                                    newTriangle.calculateNormal();
                                    Color printedColor = newTriangle.calculate_lighting();

                                    double up = t.getVertex1().getU() * bary.getX() + t.getVertex2().getU() * bary.getY() + t.getVertex3().getU() * bary.getZ();
                                    double vp = (t.getVertex1().getV() * bary.getX()) + (t.getVertex2().getV() * bary.getY()) + (t.getVertex3().getV() * bary.getZ());

                                    if ((vp < 0.30 && vp > 0.29) || (up < 0.30 && up > 0.29)) {
                                        img.setRGB(x, y, Color.black.getRGB());
                                    } else {
                                        img.setRGB(x, y, printedColor.getRGB());
                                    }
                                    if (mousePos.getX() == x && mousePos.getY() == y) {
                                        meshSelected = m;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        graphics2D.drawImage(img, 0, 0, null);

        return meshSelected;
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
                vertex1 +
                vertex2 +
                vertex3 +
                ", normal =" + normal +
                ", color =" + color +
                '}' + "\n";
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
     * Validates if a vertex meets the requirements to represent barycentric coordinates of a triangle.
     *
     * @param coordinates the vertex which may contain barycentric coordinates
     * @return true if the vertex represents barycentric coordinates
     */
    public static boolean isInBarycentric(Vertex coordinates) {
        double sum = coordinates.getY() + coordinates.getX() + coordinates.getZ();
        return (sum < 1.05) && (sum > .95) && coordinates.getX() >= 0 && coordinates.getY() >= 0 && coordinates.getZ() >= 0;
    }

    /**
     * Finds the barycentric coordinates of a specific point inside a triangle using the Edge approach.
     * If the point is not within the triangle, invalid barycentric coordinates will be returned.
     *
     * @param triangle the triangle in which the point may or may not be located
     * @param pointX   the x-coordinate of the point
     * @param pointY   the y-coordinate of the point
     * @return the barycentric coordinates of the point
     */
    public static Vertex findBarycentric(Triangle triangle, double pointX, double pointY) {

        Vertex v1 = triangle.getVertex1();
        Vertex v2 = triangle.getVertex2();
        Vertex v3 = triangle.getVertex3();

        double denominateur = (v2.getY() - v3.getY()) * (v1.getX() - v3.getX()) + (v3.getX() - v2.getX()) * (v1.getY() - v3.getY());
        double firstBary = ((v2.getY() - v3.getY()) * (pointX - v3.getX()) + (v3.getX() - v2.getX()) * (pointY - v3.getY())) / denominateur;
        double secondBary = ((v3.getY() - v1.getY()) * (pointX - v3.getX()) + (v1.getX() - v3.getX()) * (pointY - v3.getY())) / denominateur;
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
        Vertex u = getVertex2().substraction(getVertex1());
        Vertex v = getVertex3().substraction(getVertex1());
        double normalX = u.getY() * v.getZ() - u.getZ() * v.getY();
        double normalY = u.getZ() * v.getX() - u.getX() * v.getZ();
        double normalZ = u.getX() * v.getY() - u.getY() * v.getX();
        double magnitude = Math.sqrt(Math.pow(normalX, 2) + Math.pow(normalY, 2) + Math.pow(normalZ, 2));
        setNormal(new Vertex(normalX / magnitude, normalY / magnitude, normalZ / magnitude));
    }

    /**
     * Uses the normal vector of this instance of {@code Triangle} to calculate the shading to be applied to the triangle.
     * It uses the dot product formula to find the cosine between the light source and the normal vector.
     * The resulting value will be multiplied by all the RGB values of the triangle's color.
     * <b>Note that the normal vector of this instance of {@code Triangle} must be normalized.</b>
     * If something doesn't work, the triangle's original color will be returned.
     *
     * @return the new color with shading applied
     */
    public Color calculate_lighting() {
        Vertex lightDirection = new Vertex(1, 1, 1);
        double ligthMagnitude = Math.sqrt(Math.pow(lightDirection.getX(), 2) + Math.pow(lightDirection.getY(), 2) + Math.pow(lightDirection.getZ(), 2));
        float darker = (float) ((lightDirection.getX() * Math.abs(normal.getX()) + lightDirection.getY() * Math.abs(normal.getY()) + lightDirection.getZ() * Math.abs(normal.getZ())) / ligthMagnitude);
        float[] component = color.getRGBColorComponents(null);
        try {
            return new Color(component[0] * darker, component[1] * darker, component[2] * darker);
        } catch (Exception ignored) {
            return color;
        }
    }


}