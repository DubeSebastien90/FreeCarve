package Screen;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public abstract class Mesh {
    protected List<Triangle> trianglesList;
    protected List<Vertex> verticesList;
    protected List<List<Integer>> edgesList;
    protected Vertex position;
    protected Color color;
    protected Vertex center;

    public Mesh(Vertex position, Color color) {
        this.position = position;
        this.color = color;
        this.center = new Vertex(0,0,0);
    }

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

    public Vertex getCenter() {
        return this.calculateCenter();
    }

    protected void setVerticesList(){
        List<Vertex> newVerticesList = new ArrayList<>();
        newVerticesList.add(this.trianglesList.get(0).getVertex1());
        for(Triangle t : this.trianglesList){
            boolean add1 = true;
            boolean add2 = true;
            boolean add3 = true;
            for(Vertex v : newVerticesList){
                if(t.getVertex1().equals(v)){
                    add1 = false;
                }
                if(t.getVertex2().equals(v)){
                    add2 = false;
                }
                if(t.getVertex3().equals(v)){
                    add3 = false;
                }
            }
            if (add1){
                newVerticesList.add(t.getVertex1());
            }
            if (add2){
                newVerticesList.add(t.getVertex2());
            }
            if (add3){
                newVerticesList.add(t.getVertex3());
            }
        }
        this.verticesList = newVerticesList;
    }

    protected void findEdges(){
        List<List<Integer>> newEdgesList = new ArrayList<>();
        for(Triangle t : trianglesList){
            //TODO - find connecting vertices
        }
        this.edgesList = newEdgesList;
    }

    public List<Vertex> getVerticesList(){
        return this.verticesList;
    }

    public List<List<Integer>> getEdgesList(){
        return this.edgesList;
    }

    abstract void setTrianglesList();
    abstract List<Triangle> getTrianglesList();
}
