package Domain.ThirdDimension;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Creates a mesh in the shape of a box.
 * @author Sébastien Dubé
 * @version 1.0
 * @since 2024-09-08
 */
public class Box extends Mesh {

    double width;
    double length;
    double height;

    /**
     * Constructor for a Box object
     *
     * @param position - the position of the box in the scene
     * @param width    = the width of the box
     * @param length   = the length of the box
     * @param height   = the height of the box
     * @param color    - the color of the box
     */
    public Box(Vertex position, double width, double length, double height, Color color) {
        super(position, color);
        this.width = width;
        this.length = length;
        this.height = height;
        setTrianglesList();
    }

    /**
     * Creates the triangles of the box
     */
    public void setTrianglesList() {
        //create triangles
        this.trianglesList = new ArrayList<>(List.of(
                new Triangle(new Vertex(0 + position.getX(), 0 + position.getY(), 0 + position.getZ()), new Vertex(0 + position.getX(), length + position.getY(), height + position.getZ()), new Vertex(0 + position.getX(), length + position.getY(), 0 + position.getZ()), new Vertex(-100, 0, 0), this.color),
                new Triangle(new Vertex(0 + position.getX(), 0 + position.getY(), 0 + position.getZ()), new Vertex(0 + position.getX(), 0 + position.getY(), height + position.getZ()), new Vertex(0 + position.getX(), length + position.getY(), height + position.getZ()), new Vertex(-100, 0, 0), this.color),
                new Triangle(new Vertex(0 + position.getX(), 0 + position.getY(), 0 + position.getZ()), new Vertex(width + position.getX(), 0 + position.getY(), 0 + position.getZ()), new Vertex(width + position.getX(), 0 + position.getY(), height + position.getZ()), new Vertex(0, -100, 0), this.color),
                new Triangle(new Vertex(0 + position.getX(), 0 + position.getY(), 0 + position.getZ()), new Vertex(width + position.getX(), 0 + position.getY(), height + position.getZ()), new Vertex(0 + position.getX(), 0 + position.getY(), height + position.getZ()), new Vertex(0, -100, 0), this.color),
                new Triangle(new Vertex(0 + position.getX(), 0 + position.getY(), 0 + position.getZ()), new Vertex(width + position.getX(), length + position.getY(), 0 + position.getZ()), new Vertex(0 + position.getX(), length + position.getY(), 0 + position.getZ()), new Vertex(0, 0, -100), this.color),
                new Triangle(new Vertex(0 + position.getX(), 0 + position.getY(), 0 + position.getZ()), new Vertex(width + position.getX(), 0 + position.getY(), 0 + position.getZ()), new Vertex(width + position.getX(), length + position.getY(), 0 + position.getZ()), new Vertex(0, 0, -100), this.color),
                new Triangle(new Vertex(width + position.getX(), length + position.getY(), height + position.getZ()), new Vertex(width + position.getX(), 0 + position.getY(), 0 + position.getZ()), new Vertex(width + position.getX(), 0 + position.getY(), height + position.getZ()), new Vertex(100, 0, 0), this.color),
                new Triangle(new Vertex(width + position.getX(), length + position.getY(), height + position.getZ()), new Vertex(width + position.getX(), length + position.getY(), 0 + position.getZ()), new Vertex(width + position.getX(), 0 + position.getY(), 0 + position.getZ()), new Vertex(100, 0, 0), this.color),
                new Triangle(new Vertex(width + position.getX(), length + position.getY(), height + position.getZ()), new Vertex(0 + position.getX(), 0 + position.getY(), height + position.getZ()), new Vertex(width + position.getX(), 0 + position.getY(), height + position.getZ()), new Vertex(0, 0, 100), this.color),
                new Triangle(new Vertex(width + position.getX(), length + position.getY(), height + position.getZ()), new Vertex(0 + position.getX(), length + position.getY(), height + position.getZ()), new Vertex(0 + position.getX(), 0 + position.getY(), height + position.getZ()), new Vertex(0, 0, 100), this.color),
                new Triangle(new Vertex(width + position.getX(), length + position.getY(), height + position.getZ()), new Vertex(width + position.getX(), length + position.getY(), 0 + position.getZ()), new Vertex(0 + position.getX(), length + position.getY(), 0 + position.getZ()), new Vertex(0, 100, 0), this.color),
                new Triangle(new Vertex(width + position.getX(), length + position.getY(), height + position.getZ()), new Vertex(0 + position.getX(), length + position.getY(), 0 + position.getZ()), new Vertex(0 + position.getX(), length + position.getY(), height + position.getZ()), new Vertex(0, 100, 0), this.color)
        ));
        //find unique vertices
        this.setVerticesList();
        this.findEdges();
        this.calculateCenter();
        this.setPosition(center);
    }


    /**
     * Calculates the center of the cube with the vertices
     *
     * @return the 3D coordinates of the center of the cube
     */
    @Override
    public Vertex calculateCenter() {
        double centerX = 0.0, centerY = 0.0, centerZ = 0.0;
        for (Vertex v : verticesList) {
            centerX += v.getX();
            centerY += v.getY();
            centerZ += v.getZ();
        }
        centerX = centerX / ((double) (verticesList.size()));
        centerY = centerY / ((double) (verticesList.size()));
        centerZ = centerZ / ((double) (verticesList.size()));
        this.center.setVertex(new Vertex(centerX, centerY, centerZ));
        return center;
    }
}
