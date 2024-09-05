package Screen;

import Parser.ParsedSTL;
import Parser.STLParser;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.Vector;

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

    public static void printTriangles(JPanel panel, Graphics2D graphics2D, ArrayList<Triangle> triangles){

        graphics2D.setColor(Color.BLACK);
        for (Triangle t : triangles) {
            Path2D path = new Path2D.Double();
            path.moveTo(t.vertex1.getX()+ (panel.getWidth()/2), t.vertex1.getY()+ panel.getHeight()/2);
            path.lineTo(t.vertex2.getX()+ (panel.getWidth()/2), t.vertex2.getY()+panel.getHeight()/2);
            path.lineTo(t.vertex3.getX()+ (panel.getWidth()/2), t.vertex3.getY()+panel.getHeight()/2);
            path.closePath();
            graphics2D.draw(path);
        }
    }

    public static Triangle[] fromParsedSTL(ParsedSTL parsedSTL, Color color){
        Triangle[] triangles = new Triangle[parsedSTL.normals().length];
        for (int i = 0; i < parsedSTL.normals().length; i++) {
            triangles[i] = new Triangle(
                    new Vertex(parsedSTL.vertices()[i*3]),
                    new Vertex(parsedSTL.vertices()[i*3+1]),
                    new Vertex(parsedSTL.vertices()[i*3+2]),
                    new Vertex(parsedSTL.normals()[i]),
                    color);
        }
        return triangles;
    }
}
