package Screen;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Plane extends Mesh{

    private double size;

    public Plane(Vertex postion, double size, Color color){
        super(postion,color);
        this.size = size;
        setTrianglesList();
    }

    @Override
    public Vertex calculateCenter() {
        return null;
    }

    @Override
    void setTrianglesList() {
        this.trianglesList = new ArrayList<>(List.of(
                new Triangle(new Vertex(position.getX()-size/2,position.getY()-size/2,0), new Vertex(position.getX()+size/2,position.getY()-size/2,0), new Vertex(position.getX()-size/2,position.getY()+size/2,0), new Vertex(0,0,100),this.color),
                new Triangle(new Vertex(position.getX()+size/2,position.getY()+size/2,0), new Vertex(position.getX()-size/2,position.getY()+size/2,0), new Vertex(position.getX()+size/2,position.getY()-size/2,0), new Vertex(0,0,100),this.color)
        ));
        this.setVerticesList();
        this.findEdges();
        this.calculateCenter();
        this.setPosition(center);
    }
}
