package Screen;

import java.awt.*;
import java.util.List;

public abstract class Mesh {
    protected List<Triangle> trianglesList;
    protected Vertex position;
    protected Color color;
    protected Vertex center;

    public Mesh(Vertex position, Color color) {
        this.position = position;
        this.color = color;
    }

    public Vertex calculateCenter() {
        double centerX = 0.0, centerY = 0.0, centerZ = 0.0;
        for (Triangle t : trianglesList) {
            centerX += t.getVertex1().getX() + t.getVertex2().getX() + t.getVertex3().getX();
            centerY += t.getVertex1().getY() + t.getVertex2().getY() + t.getVertex3().getY();
            centerZ += t.getVertex1().getZ() + t.getVertex2().getZ() + t.getVertex3().getZ();
        }
        centerX = centerX / ((double) (trianglesList.size()) * 3.0);
        centerY = centerY / ((double) (trianglesList.size()) * 3.0);
        centerZ = centerZ / ((double) (trianglesList.size()) * 3.0);
        this.center = new Vertex(centerX, centerY, centerZ);
        return center;
    }

    public Vertex getCenter() {
        return this.calculateCenter();
    }

    abstract void setPosition();

    abstract List<Triangle> getTrianglesList();
}
