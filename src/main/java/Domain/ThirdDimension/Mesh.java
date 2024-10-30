package Domain.ThirdDimension;

import Parser.ParsedSTL;
import Parser.STLParser;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The Mesh class allows complex solids made out of triangles to be rendered and modified in the scene.
 *
 * @author Sébastien Dubé
 * @version 1.0
 * @since 2024-09-08
 */
public class Mesh {
    protected List<Triangle> localTriangles;
    protected List<Triangle> worldTriangles;
    protected Vertex position;
    private Vertex rotation;
    protected Color color;
    protected float scale;

    /**
     * Abstract constructor for a Mesh object
     *
     * @param position - the position of the mesh in the scene
     * @param color    - the color of the mesh
     */
    protected Mesh(Vertex position, float scale, Color color, List<Triangle> localTriangles) {
        this.position = position;
        this.color = color;
        this.scale = scale;
        this.rotation = Vertex.zero();
        this.worldTriangles = localTriangles;
        setLocalTriangles(localTriangles);
        //translateTriangles(Vertex.multiply(calculateCenter(), -1));
    }

    /**
     * Abstract constructor for a Mesh object
     * @param position - the position of the mesh in the scene
     * @param color - the color of the mesh
     */
    public Mesh(Vertex position, Color color, String stlFilePath, float scale) throws IOException {
        this(position, scale, color, Arrays.asList(Triangle.fromParsedSTL(parseStlFile(stlFilePath), color)));
    }

    /**
     * Creates a pre-made Box object
     *
     * @param position - the position of the box in the scene
     * @param width    = the width of the box
     * @param length   = the length of the box
     * @param height   = the height of the box
     * @param color    - the color of the box
     */
    public static Mesh createBox(Vertex position, double width, double length, double height, Color color){
        return new Mesh(position, 1, color, generateRectangularPrism(width, length, height, color));
    }
    
    private static List<Triangle> generateRectangularPrism(double width, double length, double height, Color color) {
        return new ArrayList<>(List.of(
                new Triangle(new Vertex(0, 0, 0), new Vertex(0, length, height), new Vertex(0, length, 0), new Vertex(-100, 0, 0), color),
                new Triangle(new Vertex(0, 0, 0), new Vertex(0, 0, height), new Vertex(0, length, height), new Vertex(-100, 0, 0), color),
                new Triangle(new Vertex(0, 0, 0), new Vertex(width, 0, 0), new Vertex(width, 0, height), new Vertex(0, -100, 0), color),
                new Triangle(new Vertex(0, 0, 0), new Vertex(width, 0, height), new Vertex(0, 0, height), new Vertex(0, -100, 0), color),
                new Triangle(new Vertex(0, 0, 0), new Vertex(width, length, 0), new Vertex(0, length, 0), new Vertex(0, 0, -100), color),
                new Triangle(new Vertex(0, 0, 0), new Vertex(width, 0, 0), new Vertex(width, length, 0), new Vertex(0, 0, -100), color),
                new Triangle(new Vertex(width, length, height), new Vertex(width, 0, 0), new Vertex(width, 0, height), new Vertex(100, 0, 0), color),
                new Triangle(new Vertex(width, length, height), new Vertex(width, length, 0), new Vertex(width, 0, 0), new Vertex(100, 0, 0), color),
                new Triangle(new Vertex(width, length, height), new Vertex(0, 0, height), new Vertex(width, 0, height), new Vertex(0, 0, 100), color),
                new Triangle(new Vertex(width, length, height), new Vertex(0, length, height), new Vertex(0, 0, height), new Vertex(0, 0, 100), color),
                new Triangle(new Vertex(width, length, height), new Vertex(width, length, 0), new Vertex(0, length, 0), new Vertex(0, 100, 0), color),
                new Triangle(new Vertex(width, length, height), new Vertex(0, length, 0), new Vertex(0, length, height), new Vertex(0, 100, 0), color)
        ));
    }

    /**
     * Calculates the center of the mesh depending on the shape
     *
     * @return the 3D coordinates of the center of the mesh
     */
    public Vertex calculateCenter() {
        double centerX = 0.0, centerY = 0.0, centerZ = 0.0;
        for(Triangle triangle : localTriangles) {
            for (Vertex v : triangle.getVertices()) {
                centerX += v.getX();
                centerY += v.getY();
                centerZ += v.getZ();
            }
        }
        Vertex center = new Vertex(centerX, centerY, centerZ);
        center.multiply(localTriangles.size());
        return center;
    }
    
    /**
     * Function that sets reference points of the vertices of the mesh in verticesList
     */
    protected List<Vertex> calculateVerticesList() {
        List<Vertex> newVerticesList = new ArrayList<>();
        newVerticesList.add(localTriangles.get(0).getVertex(0));
        for (Triangle t : localTriangles) {
            boolean add1 = true;
            boolean add2 = true;
            boolean add3 = true;
            for (Vertex v : newVerticesList) {
                if (t.getVertex(0).equals(v)) {
                    add1 = false;
                }
                if (t.getVertex(1).equals(v)) {
                    add2 = false;
                }
                if (t.getVertex(2).equals(v)) {
                    add3 = false;
                }
            }
            if (add1) {
                newVerticesList.add(t.getVertex(0));
            }
            if (add2) {
                newVerticesList.add(t.getVertex(1));
            }
            if (add3) {
                newVerticesList.add(t.getVertex(2));
            }
        }
        return newVerticesList;
    }

    /**
     * Function that calculates the edgesList at the creation of the mesh
     */
    protected List<List<Integer>> findEdges(List<Vertex> verticesList) {
        List<List<Integer>> newEdgesList = new ArrayList<>();
        for (Triangle t : localTriangles) {
            boolean add1 = true, add2 = true, add3 = true;
            for (List<Integer> edge : newEdgesList) {
                if ((t.getVertex(0).equals(verticesList.get(edge.get(0))) && t.getVertex(1).equals(verticesList.get(edge.get(1)))) || (t.getVertex(0).equals(verticesList.get(edge.get(1))) && t.getVertex(1).equals(verticesList.get(edge.get(0))))) {
                    add1 = false;
                }
                if ((t.getVertex(0).equals(verticesList.get(edge.get(0))) && t.getVertex(2).equals(verticesList.get(edge.get(1)))) || (t.getVertex(0).equals(verticesList.get(edge.get(1))) && t.getVertex(2).equals(verticesList.get(edge.get(0))))) {
                    add2 = false;
                }
                if ((t.getVertex(1).equals(verticesList.get(edge.get(0))) && t.getVertex(2).equals(verticesList.get(edge.get(1)))) || (t.getVertex(1).equals(verticesList.get(edge.get(1))) && t.getVertex(2).equals(verticesList.get(edge.get(0))))) {
                    add3 = false;
                }
            }
            if (add1) {
                int num1 = 0;
                int num2 = 0;
                for (int i = 0; i < verticesList.size(); i++) {
                    if (verticesList.get(i).equals(t.getVertex(0))) {
                        num1 = i;
                    } else if (verticesList.get(i).equals(t.getVertex(1))) {
                        num2 = i;
                    }
                }
                boolean add = true;
                for (Triangle t2 : localTriangles) {
                    if (t != t2) {
                        if ((t2.getVertex(0).equals(t.getVertex(0)) || t2.getVertex(1).equals(t.getVertex(0)) || t2.getVertex(2).equals(t.getVertex(0))) && (t2.getVertex(0).equals(t.getVertex(1)) || t2.getVertex(1).equals(t.getVertex(1)) || t2.getVertex(2).equals(t.getVertex(1)))) {
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
                    if (verticesList.get(i).equals(t.getVertex(0))) {
                        num1 = i;
                    } else if (verticesList.get(i).equals(t.getVertex(2))) {
                        num2 = i;
                    }
                }
                boolean add = true;
                for (Triangle t2 : localTriangles) {
                    if (t != t2) {
                        if ((t2.getVertex(0).equals(t.getVertex(0)) || t2.getVertex(1).equals(t.getVertex(0)) || t2.getVertex(2).equals(t.getVertex(0))) && (t2.getVertex(0).equals(t.getVertex(2)) || t2.getVertex(1).equals(t.getVertex(2)) || t2.getVertex(2).equals(t.getVertex(2)))) {
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
                    if (verticesList.get(i).equals(t.getVertex(1))) {
                        num1 = i;
                    } else if (verticesList.get(i).equals(t.getVertex(2))) {
                        num2 = i;
                    }
                }
                boolean add = true;
                for (Triangle t2 : localTriangles) {
                    if (t != t2) {
                        if ((t2.getVertex(0).equals(t.getVertex(2)) || t2.getVertex(1).equals(t.getVertex(2)) || t2.getVertex(2).equals(t.getVertex(2))) && (t2.getVertex(0).equals(t.getVertex(1)) || t2.getVertex(1).equals(t.getVertex(1)) || t2.getVertex(2).equals(t.getVertex(1)))) {
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
        return newEdgesList;
    }

    public void setPosition(Vertex v) {
        position = v;
    }

    public Vertex getPosition() {
        return position;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        color = color;
    }

    public Vertex getRotation() {
        return rotation;
    }

    public void setRotation(Vertex rotation) {
        if(rotation.getX() >= 2*Math.PI)
            rotation.setX(Math.asin(Math.sin(rotation.getX())));
        if(rotation.getY() >= 2*Math.PI)
            rotation.setY(Math.asin(Math.sin(rotation.getY())));
        if(rotation.getZ() >= 2*Math.PI)
            rotation.setZ(Math.asin(Math.sin(rotation.getZ())));
        this.rotation = rotation;
    }

    /**
     * Move a specific mesh in the scene
     *
     * @param translation - the movement vector
     */
    public void translateTriangles(Vertex translation) {
        Vertex translationModif = Vertex.zero();
        Vertex tempVertexX = new Vertex(Renderer.getWorldX());
        tempVertexX.multiply(translation.getX());
        translationModif.add(tempVertexX);

        Vertex tempVertexY = new Vertex(Renderer.getWorldY());
        tempVertexY.multiply(translation.getY());
        translationModif.add(tempVertexY);

        Vertex tempVertexZ = new Vertex(Renderer.getWorldZ());
        tempVertexZ.multiply(translation.getZ());
        translationModif.add(tempVertexZ);
        for (Triangle t : getLocalTriangles()) {
            for(Vertex v : t.getVertices()){
                v.add(translationModif);
            }
        }
        getPosition().add(translation);
    }

    /**
     * Rotate a specific mesh around it's center
     *
     * @param axis the axis to turn around
     * @param radians the angle of the rotation
     */
    public void rotateTriangles(Vertex axis, double radians) {
        Matrix rotationMatrice = Matrix.getRotationMatrixAroundVector(axis, radians);
        Vertex center = getPosition();
        for (Triangle t : getLocalTriangles()) {
            Vertex[] vertices = t.getVertices();
            for (int i = 0; i < vertices.length; i++) {
                vertices[i].subtract(center);
                t.setVertex(rotationMatrice.matrixXVertex3X3(vertices[i]), i);
                vertices[i].add(center);
            }
        }
    }

    public List<Triangle> getWorldTriangles(){
        return worldTriangles;
    }

    /**
     * Creates the triangles of the mesh
     */
    private void setLocalTriangles(List<Triangle> list){
        localTriangles = list;
        for (Triangle t : list) {
            t.getVertex(0).multiply(scale);
            t.getVertex(1).multiply(scale);
            t.getVertex(2).multiply(scale);
        }
        calculateCenter();
    }

    private static ParsedSTL parseStlFile(String path) throws IOException {
        DataInputStream dis = null;
        try{
            dis = new DataInputStream(new BufferedInputStream(new FileInputStream(path)));
            return STLParser.parse(dis);
        } finally {
            dis.close();
        }
    }


    /**
     * Returns the triangles of the mesh
     *
     * @return the triangles of the mesh in a list
     */
    List<Triangle> getLocalTriangles() {
        return localTriangles;
    }


}
