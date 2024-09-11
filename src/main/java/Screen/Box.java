package Screen;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
<<<<<<< HEAD
 * Creates a mesh in the shape of a box.
 *
=======
 * Creates a mesh in the shape of a cube.
>>>>>>> fca9d8e (ajout de la classe box et plane)
 * @author Sébastien Dubé
 * @version 1.0
 * @since 2024-09-08
 */
public class Box extends Mesh {

    double width;
    double length;
    double height;

    /**
<<<<<<< HEAD
     * Constructor for a Box object
     *
     * @param position - the position of the box in the scene
     * @param width    = the width of the box
     * @param length   = the length of the box
     * @param height   = the height of the box
     * @param color    - the color of the box
=======
     * Constructor for a Cube object
     * @param position - the position of the cube in the scene
     * @param color - the color of the cube
>>>>>>> fca9d8e (ajout de la classe box et plane)
     */
    public Box(Vertex position, double width, double length, double height, Color color) {
        super(position, color);
        this.width = width;
        this.length = length;
        this.height = height;
        setTrianglesList();
    }

    /**
<<<<<<< HEAD
     * Creates the triangles of the box
=======
     * Creates the triangles of the cube
>>>>>>> fca9d8e (ajout de la classe box et plane)
     */
    @Override
    public void setTrianglesList() {
        //create triangles
        this.trianglesList = new ArrayList<>(List.of(
                /*5*/new Triangle(new Vertex(position.getX(), position.getY(), position.getZ(), 1, 1), new Vertex(position.getX(), length + position.getY(), height + position.getZ(), 0, 0), new Vertex(0 + position.getX(), length + position.getY(), 0 + position.getZ(), 1, 0), new Vertex(-1, 0, 0), this.color),
                /*5*/new Triangle(new Vertex(position.getX(), position.getY(), position.getZ(), 1, 1), new Vertex(position.getX(), 0 + position.getY(), height + position.getZ(), 0, 1), new Vertex(0 + position.getX(), length + position.getY(), height + position.getZ(), 0, 0), new Vertex(-1, 0, 0), this.color),

                /*1*/ new Triangle(new Vertex(position.getX(), position.getY(), position.getZ(), 1, 0), new Vertex(width + position.getX(), 0 + position.getY(), 0 + position.getZ(), 1, 1), new Vertex(width + position.getX(), 0 + position.getY(), height + position.getZ(), 0, 1), new Vertex(0, -1, 0), this.color),
                /*1*/ new Triangle(new Vertex(position.getX(), position.getY(), position.getZ(), 1, 0), new Vertex(width + position.getX(), 0 + position.getY(), height + position.getZ(), 0, 1), new Vertex(0 + position.getX(), 0 + position.getY(), height + position.getZ(), 0, 0), new Vertex(0, -1, 0), this.color),

                /*6*/new Triangle(new Vertex(position.getX(), position.getY(), position.getZ(), 0, 1), new Vertex(width + position.getX(), length + position.getY(), 0 + position.getZ(), 1, 0), new Vertex(0 + position.getX(), length + position.getY(), 0 + position.getZ(), 0, 0), new Vertex(0, 0, -1), this.color),
                /*6*/new Triangle(new Vertex(position.getX(), position.getY(), position.getZ(), 0, 1), new Vertex(width + position.getX(), 0 + position.getY(), 0 + position.getZ(), 1, 1), new Vertex(width + position.getX(), length + position.getY(), 0 + position.getZ(), 1, 0), new Vertex(0, 0, -1), this.color),

                /*3*/new Triangle(new Vertex(width + position.getX(), length + position.getY(), height + position.getZ(), 0, 0), new Vertex(width + position.getX(), 0 + position.getY(), 0 + position.getZ(), 1, 1), new Vertex(width + position.getX(), 0 + position.getY(), height + position.getZ(), 1, 0), new Vertex(1, 0, 0), this.color),
                /*3*/new Triangle(new Vertex(width + position.getX(), length + position.getY(), height + position.getZ(), 0, 0), new Vertex(width + position.getX(), length + position.getY(), 0 + position.getZ(), 0, 1), new Vertex(width + position.getX(), 0 + position.getY(), 0 + position.getZ(), 1, 1), new Vertex(1, 0, 0), this.color),

                /*4*/new Triangle(new Vertex(width + position.getX(), length + position.getY(), height + position.getZ(), 0, 1), new Vertex(0 + position.getX(), 0 + position.getY(), height + position.getZ(), 1, 0), new Vertex(width + position.getX(), 0 + position.getY(), height + position.getZ(), 1, 1), new Vertex(0, 0, 1), this.color),
                /*4*/new Triangle(new Vertex(width + position.getX(), length + position.getY(), height + position.getZ(), 0, 1), new Vertex(0 + position.getX(), length + position.getY(), height + position.getZ(), 0, 0), new Vertex(0 + position.getX(), 0 + position.getY(), height + position.getZ(), 1, 0), new Vertex(0, 0, 1), this.color),

                /*2*/new Triangle(new Vertex(width + position.getX(), length + position.getY(), height + position.getZ(), 1, 0), new Vertex(width + position.getX(), length + position.getY(), 0 + position.getZ(), 1, 1), new Vertex(0 + position.getX(), length + position.getY(), 0 + position.getZ(), 0, 1), new Vertex(0, 1, 0), this.color),
                /*2*/new Triangle(new Vertex(width + position.getX(), length + position.getY(), height + position.getZ(), 1, 0), new Vertex(0 + position.getX(), length + position.getY(), 0 + position.getZ(), 0, 1), new Vertex(0 + position.getX(), length + position.getY(), height + position.getZ(), 0, 0), new Vertex(0, 1, 0), this.color)
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
