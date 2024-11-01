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
public class Mesh extends Transform {
    protected List<Triangle> localTriangles;
    private Color color;

    /**
     * Base constructor for a Mesh object
     *
     * @param position - the position of the mesh in the scene
     * @param color    - the color of the mesh
     * @param scale - The scaling factor to apply to the triangles
     * @param triangles - The initial triangles of the mesh
     */
    protected Mesh(Vertex position, float scale, Color color, List<Triangle> triangles) {
        super(position, scale, Vertex.zero());
        this.color = color;
        setLocalTriangles(triangles);
    }

    /**
     * Constructor for a Mesh object with a file
     * @param position - the position of the mesh in the scene
     * @param color - the color of the mesh
     * @param stlFilePath - Absolute path of the file containing the triangles
     * @param scale - The scaling factor to apply to the read triangles
     */
    public Mesh(Vertex position, Color color, String stlFilePath, float scale) throws IOException {
        this(position, scale, color, Arrays.asList(Triangle.fromParsedSTL(parseStlFile(stlFilePath), color)));
    }

    /**
     * Copy constructor of mesh (deep copy)
     * @param mesh Mesh to be copied
     */
    public Mesh(Mesh mesh) {
        this(new Vertex(mesh.getPosition()), mesh.getScale(), new Color(mesh.color.getRGB()), cloneTriangles(mesh.localTriangles));
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
     * This method exists because the existing clone method of List makes a deep copy using the cloneable interface
     * and we use copy constructors instead as they are more concise when used and no casting is involved.
     *
     * @param triangles Original triangles
     * @return Deep copy of the triangles
     */
    private static List<Triangle> cloneTriangles(List<Triangle> triangles) {
        List<Triangle> newTriangles = new ArrayList<>(triangles.size());
        for(Triangle t : triangles){
            newTriangles.add(new Triangle(t));
        }
        return newTriangles;
    }

    /**
     * Calculates the center of the mesh depending on the shape
     *
     * @return the 3D coordinates of the center of the mesh
     */
    public Vertex calculateCenter() {
        Vertex center = Vertex.zero();
        for(Triangle triangle : localTriangles) {
            for (Vertex v : triangle.getVertices()) {
                center.add(v);
            }
        }
        center.multiply(1.0/(localTriangles.size()*3));
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

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    private void setLocalTriangles(List<Triangle> list){
        localTriangles = list;
        Vertex translation = Vertex.multiply(calculateCenter(), -1);
        for (Triangle t : getLocalTriangles()) {
            for(Vertex v : t.getVertices()){
                v.add(translation);
            }
        }
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
