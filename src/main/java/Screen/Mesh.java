package Screen;

import java.awt.*;
import java.util.List;

public abstract class Mesh {
    List<Triangle> trianglesList;
    Vertex position;
    Color color;

    public Mesh(Vertex position, Color color){
        this.position = position;
        this.color = color;
    }

    abstract void setPosition();
    abstract List<Triangle>getTrianglesList();
}
