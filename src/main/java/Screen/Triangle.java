package Screen;

import Parser.ParsedSTL;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Triangle {
    private Vertex vertex1;
    private Vertex vertex2;
    private Vertex vertex3;
    private Vertex normal;
    private Color color;
    private JPanel panel = new JPanel();

    public Triangle(Vertex vertex1, Vertex vertex2, Vertex vertex3, Vertex normal, Color color) {
        this.vertex1 = vertex1;
        this.vertex2 = vertex2;
        this.vertex3 = vertex3;
        this.normal = normal;
        this.color = color;
    }

    public Vertex getVertex1() {
        return vertex1;
    }

    public void setVertex1(Vertex vertex1) {
        this.vertex1 = vertex1;
    }

    public Vertex getVertex2() {
        return vertex2;
    }

    public void setVertex2(Vertex vertex2) {
        this.vertex2 = vertex2;
    }

    public Vertex getVertex3() {
        return vertex3;
    }

    public void setVertex3(Vertex vertex3) {
        this.vertex3 = vertex3;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public JPanel getPanel() {
        return panel;
    }

    public void setPanel(JPanel panel) {
        this.panel = panel;
    }

    public Vertex getNormal() {
        return normal;
    }

    public void setNormal(Vertex normal) {
        this.normal = normal;
    }

    public static void printTriangles(JPanel panel, Graphics2D graphics2D, List<Triangle> triangles) {
        BufferedImage img = new BufferedImage(panel.getWidth(), panel.getHeight(), BufferedImage.TYPE_INT_ARGB);
        for (Triangle t : triangles) {
//            Path2D path = new Path2D.Double();
//            path.moveTo(t.vertex1.getX() + (panel.getWidth() / 2), t.vertex1.getY() + panel.getHeight() / 2);
//            path.lineTo(t.vertex2.getX() + (panel.getWidth() / 2), t.vertex2.getY() + panel.getHeight() / 2);
//            path.lineTo(t.vertex3.getX() + (panel.getWidth() / 2), t.vertex3.getY() + panel.getHeight() / 2);
//            path.closePath();
//            graphics2D.draw(path);
//
            double new1x = t.vertex1.getX() + panel.getWidth() / 2;
            double new1y = t.vertex1.getY() + panel.getHeight() / 2;
            double new2x = t.vertex2.getX() + panel.getWidth() / 2;
            double new2y = t.vertex2.getY() + panel.getHeight() / 2;
            double new3x = t.vertex3.getX() + panel.getWidth() / 2;
            double new3y = t.vertex3.getY() + panel.getHeight() / 2;

            Vertex newVertex1 = new Vertex(new1x, new1y, t.getVertex1().getZ());
            Vertex newVertex2 = new Vertex(new2x, new2y, t.getVertex2().getZ());
            Vertex newVertex3 = new Vertex(new3x, new3y, t.getVertex3().getZ());

            Triangle newTriangle = new Triangle(newVertex1, newVertex2, newVertex3, t.getNormal(), t.getColor());


            int minX = (int) Math.max(0, Math.min(new1x, Math.min(new2x, new3x)));
            int maxX = (int) Math.min(img.getWidth(), Math.max(new1x, Math.max(new2x, new3x)));

            int minY = (int) Math.max(0, Math.min(new1y, Math.min(new2y, new3y)));
            int maxY = (int) Math.min(img.getHeight(), Math.max(new1y, Math.max(new2y, new3y)));

            for (int y = minY; y <= maxY; y++) {
                for (int x = minX; x <= maxX; x++) {
                    if (isInBarycentric(findBarycentric(newTriangle, x, y)))
                        img.setRGB(x, y, t.color.getRGB());
                }
            }
        }

        graphics2D.drawImage(img, 0, 0, null);

    }

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

    public static boolean isInBarycentric(Vertex coordinates) {
        double sum = coordinates.getY() + coordinates.getX() + coordinates.getZ();
        return (sum < 1.05) && (sum > .95) && coordinates.getX() >= 0 && coordinates.getY() >= 0&& coordinates.getZ() >=0;
    }

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
}
