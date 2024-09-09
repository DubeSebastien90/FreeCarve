package Screen;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Cube extends Mesh {

    public Cube(Vertex position, Color color) {
        super(position, color);
        setPosition();
    }

    @Override
    public void setPosition() {
        this.trianglesList = new ArrayList<>(List.of(
                new Triangle(new Vertex(0 + position.getX(), 0 + position.getY(), 0 + position.getZ()), new Vertex(0 + position.getX(), 100 + position.getY(), 100 + position.getZ()), new Vertex(0 + position.getX(), 100 + position.getY(), 0 + position.getZ()), new Vertex(50, 0, 50), this.color),
                new Triangle(new Vertex(0 + position.getX(), 0 + position.getY(), 0 + position.getZ()), new Vertex(0 + position.getX(), 0 + position.getY(), 100 + position.getZ()), new Vertex(0 + position.getX(), 100 + position.getY(), 100 + position.getZ()), new Vertex(50, 0, 50), this.color),
                new Triangle(new Vertex(0 + position.getX(), 0 + position.getY(), 0 + position.getZ()), new Vertex(100 + position.getX(), 0 + position.getY(), 0 + position.getZ()), new Vertex(100 + position.getX(), 0 + position.getY(), 100 + position.getZ()), new Vertex(50, 0, 50), this.color),
                new Triangle(new Vertex(0 + position.getX(), 0 + position.getY(), 0 + position.getZ()), new Vertex(100 + position.getX(), 0 + position.getY(), 100 + position.getZ()), new Vertex(0 + position.getX(), 0 + position.getY(), 100 + position.getZ()), new Vertex(50, 0, 50), this.color),
                new Triangle(new Vertex(0 + position.getX(), 0 + position.getY(), 0 + position.getZ()), new Vertex(100 + position.getX(), 100 + position.getY(), 0 + position.getZ()), new Vertex(0 + position.getX(), 100 + position.getY(), 0 + position.getZ()), new Vertex(50, 0, 50), this.color),
                new Triangle(new Vertex(0 + position.getX(), 0 + position.getY(), 0 + position.getZ()), new Vertex(100 + position.getX(), 0 + position.getY(), 0 + position.getZ()), new Vertex(100 + position.getX(), 100 + position.getY(), 0 + position.getZ()), new Vertex(50, 0, 50), this.color),
                new Triangle(new Vertex(100 + position.getX(), 100 + position.getY(), 100 + position.getZ()), new Vertex(100 + position.getX(), 0 + position.getY(), 0 + position.getZ()), new Vertex(100 + position.getX(), 0 + position.getY(), 100 + position.getZ()), new Vertex(50, 0, 50), this.color),
                new Triangle(new Vertex(100 + position.getX(), 100 + position.getY(), 100 + position.getZ()), new Vertex(100 + position.getX(), 100 + position.getY(), 0 + position.getZ()), new Vertex(100 + position.getX(), 0 + position.getY(), 0 + position.getZ()), new Vertex(50, 0, 50), this.color),
                new Triangle(new Vertex(100 + position.getX(), 100 + position.getY(), 100 + position.getZ()), new Vertex(0 + position.getX(), 0 + position.getY(), 100 + position.getZ()), new Vertex(100 + position.getX(), 0 + position.getY(), 100 + position.getZ()), new Vertex(50, 0, 50), this.color),
                new Triangle(new Vertex(100 + position.getX(), 100 + position.getY(), 100 + position.getZ()), new Vertex(0 + position.getX(), 100 + position.getY(), 100 + position.getZ()), new Vertex(0 + position.getX(), 0 + position.getY(), 100 + position.getZ()), new Vertex(50, 0, 50), this.color),
                new Triangle(new Vertex(100 + position.getX(), 100 + position.getY(), 100 + position.getZ()), new Vertex(100 + position.getX(), 100 + position.getY(), 0 + position.getZ()), new Vertex(0 + position.getX(), 100 + position.getY(), 0 + position.getZ()), new Vertex(50, 0, 50), this.color),
                new Triangle(new Vertex(100 + position.getX(), 100 + position.getY(), 100 + position.getZ()), new Vertex(0 + position.getX(), 100 + position.getY(), 0 + position.getZ()), new Vertex(0 + position.getX(), 100 + position.getY(), 100 + position.getZ()), new Vertex(50, 0, 50), this.color)
        ));
        this.calculateCenter();
    }

    @Override
    public List<Triangle> getTrianglesList() {
        return this.trianglesList;
    }
}
