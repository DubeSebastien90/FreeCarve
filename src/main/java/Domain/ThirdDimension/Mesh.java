package Domain.ThirdDimension;

import IO.ParsedSTL;
import IO.STLParser;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * The Mesh class allows complex solids made out of triangles to be rendered and modified in the scene.
 *
 * @author Sébastien Dubé
 * @author Kamran Charles Nayebi
 * @since 2024-09-08
 */
public class Mesh extends Transform {
    protected List<Triangle> localTriangles;
    private Color color;
    private UUID id;

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
        this.id = UUID.randomUUID();
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
                new Triangle(new Vertex(0, 0, 0), new Vertex(0, length, height), new Vertex(0, length, 0), new Vertex(-1, 0, 0), color),
                new Triangle(new Vertex(0, 0, 0), new Vertex(0, 0, height), new Vertex(0, length, height), new Vertex(-1, 0, 0), color),
                new Triangle(new Vertex(0, 0, 0), new Vertex(width, 0, 0), new Vertex(width, 0, height), new Vertex(0, -1, 0), color),
                new Triangle(new Vertex(0, 0, 0), new Vertex(width, 0, height), new Vertex(0, 0, height), new Vertex(0, -1, 0), color),
                new Triangle(new Vertex(0, 0, 0), new Vertex(width, length, 0), new Vertex(0, length, 0), new Vertex(0, 0, -1), color),
                new Triangle(new Vertex(0, 0, 0), new Vertex(width, 0, 0), new Vertex(width, length, 0), new Vertex(0, 0, -1), color),
                new Triangle(new Vertex(width, length, height), new Vertex(width, 0, 0), new Vertex(width, 0, height), new Vertex(1, 0, 0), color),
                new Triangle(new Vertex(width, length, height), new Vertex(width, length, 0), new Vertex(width, 0, 0), new Vertex(1, 0, 0), color),
                new Triangle(new Vertex(width, length, height), new Vertex(0, 0, height), new Vertex(width, 0, height), new Vertex(0, 0, 1), color),
                new Triangle(new Vertex(width, length, height), new Vertex(0, length, height), new Vertex(0, 0, height), new Vertex(0, 0, 1), color),
                new Triangle(new Vertex(width, length, height), new Vertex(width, length, 0), new Vertex(0, length, 0), new Vertex(0, 1, 0), color),
                new Triangle(new Vertex(width, length, height), new Vertex(0, length, 0), new Vertex(0, length, height), new Vertex(0, 1, 0), color)
        ));
    }

    /**
     * This method exists because the existing clone method of List makes a deep copy using the cloneable interface.
     * We use copy constructors instead as they are more concise when used and no casting is involved.
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
     * @return the triangles of the mesh in a list
     */
    List<Triangle> getLocalTriangles() {
        return localTriangles;
    }

    /**
     * @return the random id of the mesh
     */
    public UUID getId() {
        return id;
    }

}
