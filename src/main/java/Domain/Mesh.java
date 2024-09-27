package Domain;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The Mesh class allows complex solids made out of triangles to be rendered and modified in the scene.
 *
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
     *
     * @param position - the position of the mesh in the scene
     * @param color    - the color of the mesh
     */
    public Mesh(Vertex position, Color color) {
        this.position = position;
        this.color = color;
        this.center = new Vertex(0, 0, 0);
        setTrianglesList();
        setVerticesList();
        calculateCenter();
    }

    public void setEdgesList(List<List<Integer>> edgesList) {
        this.edgesList = edgesList;
    }

    /**
     * Calculates the center of the mesh depending on the shape
     *
     * @return the 3D coordinates of the center of the mesh
     */
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

    /**
     * Returns the center point of the mesh
     *
     * @return the center of the mesh
     */
    public Vertex getCenter() {
        return this.calculateCenter();
    }

    /**
     * Function that sets reference points of the vertices of the mesh in verticesList
     */
    public void setVerticesList() {
        List<Vertex> newVerticesList = new ArrayList<>();
        newVerticesList.add(this.trianglesList.get(0).getVertex1());
        for (Triangle t : this.trianglesList) {
            boolean add1 = true;
            boolean add2 = true;
            boolean add3 = true;
            for (Vertex v : newVerticesList) {
                if (t.getVertex1().equals(v)) {
                    add1 = false;
                }
                if (t.getVertex2().equals(v)) {
                    add2 = false;
                }
                if (t.getVertex3().equals(v)) {
                    add3 = false;
                }
            }
            if (add1) {
                newVerticesList.add(t.getVertex1());
            }
            if (add2) {
                newVerticesList.add(t.getVertex2());
            }
            if (add3) {
                newVerticesList.add(t.getVertex3());
            }
        }
        this.verticesList = newVerticesList;
    }

    /**
     * Function that sets the attribute edgesList at the creation of the mesh
     */
    public void findEdges() {
        List<List<Integer>> newEdgesList = new ArrayList<>();
        for (Triangle t : trianglesList) {
            boolean add1 = true, add2 = true, add3 = true;
            for (List<Integer> edge : newEdgesList) {
                if ((t.getVertex1().equals(verticesList.get(edge.get(0))) && t.getVertex2().equals(verticesList.get(edge.get(1)))) || (t.getVertex1().equals(verticesList.get(edge.get(1))) && t.getVertex2().equals(verticesList.get(edge.get(0))))) {
                    add1 = false;
                }
                if ((t.getVertex1().equals(verticesList.get(edge.get(0))) && t.getVertex3().equals(verticesList.get(edge.get(1)))) || (t.getVertex1().equals(verticesList.get(edge.get(1))) && t.getVertex3().equals(verticesList.get(edge.get(0))))) {
                    add2 = false;
                }
                if ((t.getVertex2().equals(verticesList.get(edge.get(0))) && t.getVertex3().equals(verticesList.get(edge.get(1)))) || (t.getVertex2().equals(verticesList.get(edge.get(1))) && t.getVertex3().equals(verticesList.get(edge.get(0))))) {
                    add3 = false;
                }
            }
            if (add1) {
                int num1 = 0;
                int num2 = 0;
                for (int i = 0; i < verticesList.size(); i++) {
                    if (verticesList.get(i).equals(t.getVertex1())) {
                        num1 = i;
                    } else if (verticesList.get(i).equals(t.getVertex2())) {
                        num2 = i;
                    }
                }
                boolean add = true;
                for (Triangle t2 : trianglesList) {
                    if (t != t2) {
                        if ((t2.getVertex1().equals(t.getVertex1()) || t2.getVertex2().equals(t.getVertex1()) || t2.getVertex3().equals(t.getVertex1())) && (t2.getVertex1().equals(t.getVertex2()) || t2.getVertex2().equals(t.getVertex2()) || t2.getVertex3().equals(t.getVertex2()))) {
                            if (t.getNormal().isParallel(t2.getNormal())) {
                                add = false;
                            }
                        }
                    }
                }
                if (add) {
                    newEdgesList.add(new ArrayList<>(List.of(num1, num2))); //ajouter les index des sommets dans verticesList à la liste d'arrêtes
                }
            }
            if (add2) {
                int num1 = 0;
                int num2 = 0;
                for (int i = 0; i < verticesList.size(); i++) {
                    if (verticesList.get(i).equals(t.getVertex1())) {
                        num1 = i;
                    } else if (verticesList.get(i).equals(t.getVertex3())) {
                        num2 = i;
                    }
                }
                boolean add = true;
                for (Triangle t2 : trianglesList) {
                    if (t != t2) {
                        if ((t2.getVertex1().equals(t.getVertex1()) || t2.getVertex2().equals(t.getVertex1()) || t2.getVertex3().equals(t.getVertex1())) && (t2.getVertex1().equals(t.getVertex3()) || t2.getVertex2().equals(t.getVertex3()) || t2.getVertex3().equals(t.getVertex3()))) {
                            if (t.getNormal().isParallel(t2.getNormal())) {
                                add = false;
                            }
                        }
                    }
                }
                if (add) {
                    newEdgesList.add(new ArrayList<>(List.of(num1, num2))); //ajouter les index des sommets dans verticesList à la liste d'arrêtes
                }
            }
            if (add3) {
                int num1 = 0;
                int num2 = 0;
                for (int i = 0; i < verticesList.size(); i++) {
                    if (verticesList.get(i).equals(t.getVertex2())) {
                        num1 = i;
                    } else if (verticesList.get(i).equals(t.getVertex3())) {
                        num2 = i;
                    }
                }
                boolean add = true;
                for (Triangle t2 : trianglesList) {
                    if (t != t2) {
                        if ((t2.getVertex1().equals(t.getVertex3()) || t2.getVertex2().equals(t.getVertex3()) || t2.getVertex3().equals(t.getVertex3())) && (t2.getVertex1().equals(t.getVertex2()) || t2.getVertex2().equals(t.getVertex2()) || t2.getVertex3().equals(t.getVertex2()))) {
                            if (t.getNormal().isParallel(t2.getNormal())) {
                                add = false;
                            }
                        }
                    }
                }
                if (add) {
                    newEdgesList.add(new ArrayList<>(List.of(num1, num2))); //ajouter les index des sommets dans verticesList à la liste d'arrêtes
                }
            }
        }
        this.edgesList = newEdgesList;
    }

    /**
     * Returns the mesh's vertices in a list
     *
     * @return the list of the mesh's Vertices
     */
    public List<Vertex> getVerticesList() {
        return this.verticesList;
    }

    /**
     * Returns the list of the edges of the mesh in a tuple format. (1,2), (1,3), (2,4)... The integers represents the index of the Vertex in verticesList
     *
     * @return the edges of the mesh
     */
    public List<List<Integer>> getEdgesList() {
        return this.edgesList;
    }

    public void setPosition(Vertex v) {
        this.position = v;
    }

    public Vertex getPosition() {
        return this.position;
    }

    /**
     * Creates the triangles of the mesh
     */
    public abstract void setTrianglesList();

    public void setTriangles(List<Triangle> list) {
        this.trianglesList = list;
    }

    /**
     * Returns the triangles of the mesh
     *
     * @return the triangles of the mesh in a list
     */
    public List<Triangle> getTrianglesList() {
        return this.trianglesList;
    }


}
