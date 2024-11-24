package Domain.ThirdDimension;

import Common.DTO.CutDTO;
import Common.DTO.PanelDTO;
import Common.DTO.VertexDTO;
import Domain.CutType;
import Domain.IO.ParsedSTL;
import Domain.IO.STLParser;

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
     * @param position  - the position of the mesh in the scene
     * @param color     - the color of the mesh
     * @param scale     - The scaling factor to apply to the triangles
     * @param triangles - The initial triangles of the mesh
     */
    protected Mesh(Vertex position, double scale, Color color, List<Triangle> triangles) {
        super(position, scale, Vertex.zero());
        this.color = color;
        setLocalTriangles(triangles);
        this.id = UUID.randomUUID();
    }

    /**
     * Constructor for a Mesh object with a file
     *
     * @param position    - the position of the mesh in the scene
     * @param color       - the color of the mesh
     * @param stlInputStream - Input stream to the file containing the triangles
     * @param scale       - The scaling factor to apply to the read triangles
     */
    public Mesh(Vertex position, Color color, InputStream stlInputStream, double scale) throws IOException {
        this(position, scale, color, Arrays.asList(Triangle.fromParsedSTL(STLParser.parse(stlInputStream), color)));
    }

    /**
     * Copy constructor of mesh (deep copy)
     *
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
    public static Mesh createBox(Vertex position, double width, double length, double height, Color color) {
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
        for (Triangle t : triangles) {
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
        for (Triangle triangle : localTriangles) {
            for (Vertex v : triangle.getVertices()) {
                center.add(v);
            }
        }
        center.multiply(1.0 / (localTriangles.size() * 3));
        return center;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    private void setLocalTriangles(List<Triangle> list) {
        localTriangles = list;
        Vertex translation = Vertex.multiply(calculateCenter(), -1);
        for (Triangle t : getLocalTriangles()) {
            for (Vertex v : t.getVertices()) {
                v.add(translation);
            }
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


    /**
     * Faire toutes les droites en premier, puis traiter les rectangles et les coupe en L
     *
     * @param panel
     * @return
     */
    public static List<Mesh> PanelToMesh(PanelDTO panel) {
        List<Vertex[]> meshes = new ArrayList<Vertex[]>();
        meshes.add(new Vertex[]{new Vertex(0, 0, 0), new Vertex(0, panel.getPanelDimension().getY(), 0), new Vertex(panel.getPanelDimension().getX(), panel.getPanelDimension().getY(), 0), new Vertex(panel.getPanelDimension().getX(), 0, 0)});
        for (CutDTO cut : panel.getCutsDTO()) {
            meshes = cutMesh(meshes, cut);
        }
        return transformListVertexToMeshes(meshes, panel.getPanelDimension().getZ());
    }

    private static List<Mesh> transformListVertexToMeshes(List<Vertex[]> vertices, double depth) {
        List<Mesh> meshes = new ArrayList<>();
        for (Vertex[] meshVertex : vertices) {
            List<Triangle> triangles = new ArrayList<>(List.of(
                    new Triangle(meshVertex[0], new Vertex(meshVertex[1].getX(), meshVertex[1].getY(), depth), meshVertex[1], new Vertex(-1, 0, 0), Color.BLUE),
                    new Triangle(meshVertex[0], new Vertex(meshVertex[0].getX(), meshVertex[0].getY(), depth), new Vertex(meshVertex[1].getX(), meshVertex[1].getY(), depth), new Vertex(-1, 0, 0), Color.BLUE),
                    new Triangle(meshVertex[0], meshVertex[3], new Vertex(meshVertex[3].getX(), meshVertex[3].getY(), depth), new Vertex(0, -1, 0), Color.BLUE),
                    new Triangle(meshVertex[0], new Vertex(meshVertex[3].getX(), meshVertex[3].getY(), depth), new Vertex(meshVertex[0].getX(), meshVertex[0].getY(), depth), new Vertex(0, -1, 0), Color.BLUE),
                    new Triangle(meshVertex[0], meshVertex[2], meshVertex[1], new Vertex(0, 0, -1), Color.BLUE),
                    new Triangle(meshVertex[0], meshVertex[3], meshVertex[2], new Vertex(0, 0, -1), Color.BLUE)
//                    new Triangle(new Vertex(meshVertex[2].getX(), meshVertex[2].getY(), depth), new Vertex(width, 0, 0), new Vertex(width, 0, height), new Vertex(1, 0, 0), Color.BLUE),
//                    new Triangle(new Vertex(meshVertex[2].getX(), meshVertex[2].getY(), depth), new Vertex(width, length, 0), new Vertex(width, 0, 0), new Vertex(1, 0, 0), Color.BLUE),
//                    new Triangle(new Vertex(meshVertex[2].getX(), meshVertex[2].getY(), depth), new Vertex(0, 0, height), new Vertex(width, 0, height), new Vertex(0, 0, 1), Color.BLUE),
//                    new Triangle(new Vertex(meshVertex[2].getX(), meshVertex[2].getY(), depth), new Vertex(0, length, height), new Vertex(0, 0, height), new Vertex(0, 0, 1), Color.BLUE),
//                    new Triangle(new Vertex(meshVertex[2].getX(), meshVertex[2].getY(), depth), new Vertex(width, length, 0), new Vertex(0, length, 0), new Vertex(0, 1, 0), Color.BLUE),
//                    new Triangle(new Vertex(meshVertex[2].getX(), meshVertex[2].getY(), depth), new Vertex(0, length, 0), new Vertex(0, length, height), new Vertex(0, 1, 0), Color.BLUE)
            ));
            meshes.add(new Mesh(new Vertex(100, 100, 0), 0.3, Color.BLUE, triangles));
        }
        return meshes;
    }

    private static List<Vertex[]> cutMesh(List<Vertex[]> meshes, CutDTO cut) {
        List<Vertex[]> cutMeshes = new ArrayList<>();
        if (cut.getCutType() == CutType.LINE_VERTICAL) {
            for (Vertex[] vertices : meshes) {
                Vertex v1 = vertices[0];
                Vertex v2 = new Vertex(cut.getPoints().get(0));
                Vertex v3 = new Vertex(cut.getPoints().get(1));
                Vertex v4 = new Vertex(meshes.get(0)[1]);
                cutMeshes.add(new Vertex[]{v1, v4, v3, v2});
            }
        }
        return cutMeshes;
    }

    private static List<Vertex[]> impactedMeshes(List<Vertex[]> meshes, List<VertexDTO> points) {
        List<Vertex[]> impacted = new ArrayList<>();
        for (VertexDTO singlePoint : points) {
            for (Vertex[] singleMesh : meshes) {
                double totalArea = area(singleMesh[0], singleMesh[1], singleMesh[2]) + area(singleMesh[0], singleMesh[2], singleMesh[3]);
                double area1 = area(new Vertex(singlePoint), singleMesh[0], singleMesh[1]);
                double area2 = area(new Vertex(singlePoint), singleMesh[1], singleMesh[2]);
                double area3 = area(new Vertex(singlePoint), singleMesh[2], singleMesh[3]);
                double area4 = area(new Vertex(singlePoint), singleMesh[3], singleMesh[0]);
                if (Math.abs(totalArea - (area1 + area2 + area3 + area4)) < 1e-6 && !impacted.contains(singleMesh)) {
                    impacted.add(singleMesh);
                }
            }
        }
        return impacted;
    }

    private static double area(Vertex a, Vertex b, Vertex c) {
        return Math.abs(a.getX() * (b.getY() - c.getY()) +
                b.getX() * (c.getY() - a.getY()) +
                c.getX() * (a.getY() - b.getY())) / 2.0;
    }
}
