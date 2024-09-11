package Screen;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Pyramid extends Mesh {
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

    /**
     * Creates the triangles of the box
     */
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
        this.setPosition(center);
    }
}
