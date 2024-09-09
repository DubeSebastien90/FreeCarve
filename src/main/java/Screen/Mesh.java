package Screen;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The Mesh class allows complex solids made out of triangles to be rendered and modified in the scene.
 * @author Sébastien Dubé
 * @version 1.0
 * @since 2024-09-08
 */
public abstract class Mesh {
    protected List<Triangle> trianglesList;
    protected List<Vertex> verticesList;
    protected List<List<Integer>> edgesList;
    protected Vertex position;
    protected Color color;
    protected Vertex center;

    /**
     * Abstract constructor for a Mesh object
     * @param position - the position of the mesh in the scene
     * @param color - the color of the mesh
     */
    public Mesh(Vertex position, Color color) {
        this.position = position;
        this.color = color;
        this.center = new Vertex(0,0,0);
    }

    /**
     * Calculates the center of the mesh depending on the shape
     * @return the 3D coordinates of the center of the mesh
     */
    public abstract Vertex calculateCenter();

    /**
     * Returns the center point of the mesh
     * @return the center of the mesh
     */
    public Vertex getCenter() {
        return this.calculateCenter();
    }

    /**
     * Function that sets reference points of the vertices of the mesh in verticesList
     */
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

    /**
     * Function that sets the attribute edgesList at the creation of the mesh
     */
    protected void findEdges(){
        List<List<Integer>> newEdgesList = new ArrayList<>();
        for(Triangle t : trianglesList){
            //TODO - find connecting vertices
        }
        this.edgesList = newEdgesList;
    }

    /**
     * Returns the mesh's vertices in a list
     * @return the list of the mesh's Vertices
     */
    public List<Vertex> getVerticesList(){
        return this.verticesList;
    }

    /**
     * Returns the list of the edges of the mesh in a tuple format. (1,2), (1,3), (2,4)... The integers represents the index of the Vertex in verticesList
     * @return the edges of the mesh
     */
    public List<List<Integer>> getEdgesList(){
        return this.edgesList;
    }

    /**
     * Creates the triangles of the mesh
     */
    abstract void setTrianglesList();

    /**
     * Returns the triangles of the mesh
     * @return the triangles of the mesh in a list
     */
    List<Triangle> getTrianglesList(){
        return this.trianglesList;
    }
}
