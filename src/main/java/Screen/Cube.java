package Screen;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Creates a mesh in the shape of a cube.
 *
 * @author Sébastien Dubé
 * @version 1.0
 * @since 2024-09-08
 */
public class Cube extends Mesh {

    /**
     * Constructor for a Cube object
     *
     * @param position - the position of the cube in the scene
     * @param color    - the color of the cube
     */
    public Cube(Vertex position, Color color) {
        super(position, color);
        setTrianglesList();
    }

    /**
     * Creates the triangles of the cube
     */
    @Override
    public void setTrianglesList() {

        this.trianglesList = new ArrayList<>(List.of(

                /*1*/new Triangle(new Vertex(100, -100, 100, 0, 1), new Vertex(-100, -100, 100, 0, 0), new Vertex(-100, -100, -100, 1, 0), new Vertex(0, -1, 0), color),
                /*1*/ new Triangle(new Vertex(100, -100, -100, 1, 1), new Vertex(100, -100, 100, 0, 1), new Vertex(-100, -100, -100, 1, 0), new Vertex(0, -1, 0), color),


                /*2*/new Triangle(new Vertex(100, 100, -100, 1, 1), new Vertex(-100, 100, -100, 0, 1), new Vertex(-100, 100, 100, 0, 0), new Vertex(0, 1, 0), color),
                /*2*/new Triangle(new Vertex(100, 100, 100, 1, 0), new Vertex(100, 100, -100, 1, 1), new Vertex(-100, 100, 100, 0, 0), new Vertex(0, 1, 0), color),


                /*3*/new Triangle(new Vertex(100, 100, -100, 0, 1), new Vertex(100, 100, 100, 0, 0), new Vertex(100, -100, 100, 1, 0), new Vertex(1, 0, 0), color),
                /*3*/new Triangle(new Vertex(100, -100, -100, 1, 1), new Vertex(100, 100, -100, 0, 1), new Vertex(100, -100, 100, 1, 0), new Vertex(1, 0, 0), color),


                /*4*/new Triangle(new Vertex(100, -100, 100, 1, 1), new Vertex(100, 100, 100, 0, 1), new Vertex(-100, 100, 100, 0, 0), new Vertex(0, 0, 1), color),
                /*4*/new Triangle(new Vertex(-100, -100, 100, 1, 0), new Vertex(100, -100, 100, 1, 1), new Vertex(-100, 100, 100, 0, 0), new Vertex(0, 0, 1), color),

                /*5*/new Triangle(new Vertex(-100, 100, 100, 0, 0), new Vertex(-100, 100, -100, 1, 0), new Vertex(-100, -100, -100, 1, 1), new Vertex(-1, 0, 0), color),
                /*5*/new Triangle(new Vertex(-100, -100, 100, 0, 1), new Vertex(-100, 100, 100, 0, 0), new Vertex(-100, -100, -100, 1, 1), new Vertex(-1, 0, 0), color),


                /*6*/new Triangle(new Vertex(-100, 100, -100, 0, 0), new Vertex(100, 100, -100, 1, 0), new Vertex(-100, -100, -100, 0, 1), new Vertex(0, 0, -1), color),
                /*6*/new Triangle(new Vertex(100, 100, -100, 1, 0), new Vertex(100, -100, -100, 1, 1), new Vertex(-100, -100, -100, 0, 1), new Vertex(0, 0, -1), color)

        ));
        this.setVerticesList();
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
