package Screen;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Pyramid extends Cube {
    /**
     * Constructor for a Cube object
     *
     * @param position - the position of the cube in the scene
     * @param color    - the color of the cube
     */
    public Pyramid(Vertex position, Color color) {
        super(position, color);
        setTrianglesList();
    }

    @Override
    public void setTrianglesList() {
        this.trianglesList = new ArrayList<>(List.of(
                new Triangle(new Vertex(0 + position.getX(), 0 + position.getY(), 0 + position.getZ()), new Vertex(100 + position.getX(), 0 + position.getY(), 0 + position.getZ()), new Vertex(0 + position.getX(), 100 + position.getY(), 0 + position.getZ()), new Vertex(0, 0, -100), this.color),
                new Triangle(new Vertex(0 + position.getX(), 0 + position.getY(), 0 + position.getZ()), new Vertex(100 + position.getX(), 0 + position.getY(), 0 + position.getZ()), new Vertex(0 + position.getX(), 0 + position.getY(), 100 + position.getZ()), new Vertex(0, -100, 0), this.color),
                new Triangle(new Vertex(0 + position.getX(), 0 + position.getY(), 0 + position.getZ()), new Vertex(0 + position.getX(), 100 + position.getY(), 0 + position.getZ()), new Vertex(0 + position.getX(), 0 + position.getY(), 100 + position.getZ()), new Vertex(-100, 0, 0), this.color),
                new Triangle(new Vertex(100 + position.getX(), 0 + position.getY(), 0 + position.getZ()), new Vertex(0 + position.getX(), 100 + position.getY(), 0 + position.getZ()), new Vertex(0 + position.getX(), 0 + position.getY(), 100 + position.getZ()), new Vertex(100, 100, 100), this.color)
        ));
        this.setVerticesList();
        this.findEdges();
        this.calculateCenter();
    }

    /**
     * Calculates the center of the pyramid with the vertices
     * @return the 3D coordinates of the center of the pyramid
     */
    @Override
    public Vertex calculateCenter(){
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
