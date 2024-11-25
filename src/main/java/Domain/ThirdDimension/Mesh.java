package Domain.ThirdDimension;

import Common.DTO.BitDTO;
import Common.DTO.CutDTO;
import Common.DTO.PanelDTO;
import Common.DTO.VertexDTO;
import Domain.CutType;
import Domain.IO.ParsedSTL;
import Domain.IO.STLParser;

import java.awt.*;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.*;

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
//        Vertex translation = Vertex.multiply(calculateCenter(), -1);
//        for (Triangle t : getLocalTriangles()) {
//            for (Vertex v : t.getVertices()) {
//                v.add(translation);
//            }
//        }
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
     * trouver les 4 points de la coupe,
     * trouver les impacted mesh
     * trouver les points d'intersection avec chaque mesh si les points de la coupe dépassent le mesh
     * faire les formes M1-C1-C2-M2, M2-C2-C3-M3, M3-C3-C4-M4, M4-C4-C1-M1
     * traiter l'intérieur des coupes rectangles et en L
     * traiter la profondeur de chaque coupe
     *
     * @param panel
     * @return
     */
    public static List<Mesh> PanelToMesh(PanelDTO panel, BitDTO[] bits) {
        List<Vertex[]> meshes = new ArrayList<>();
        meshes.add(new Vertex[]{new Vertex(0, 0, 0), new Vertex(0, panel.getPanelDimension().getY(), 0), new Vertex(panel.getPanelDimension().getX(), panel.getPanelDimension().getY(), 0), new Vertex(panel.getPanelDimension().getX(), 0, 0)});

        for (CutDTO cut : panel.getCutsDTO()) {
            List<Vertex[]> impacted_meshes = new ArrayList<>();
            impacted_meshes = impactedMeshes(meshes, cut.getAbsolutePointsPosition());
            for (Vertex[] imp : impacted_meshes) {
                meshes.remove(imp);
            }
            List<Vertex[]> new_mesh = cutMesh(impacted_meshes, pointsOfCut(cut, bits[cut.getBitIndex()]));
            meshes.addAll(new_mesh);
        }
        return transformListVertexToMeshes(meshes, 10);
    }

    public static List<Vertex> pointsOfCut(CutDTO cut, BitDTO bit) {
        List<Vertex> points = new ArrayList<>();
        if (cut.getCutType() == CutType.LINE_VERTICAL) {
            double wichMin = Math.min((cut.getAbsolutePointsPosition().get(0).getY()), cut.getAbsolutePointsPosition().get(1).getY());
            double wichMax = Math.max((cut.getAbsolutePointsPosition().get(0).getY()), cut.getAbsolutePointsPosition().get(1).getY());
            Vertex v0 = new Vertex(cut.getAbsolutePointsPosition().get(0).getX(), wichMin, cut.getAbsolutePointsPosition().get(0).getZ());
            Vertex v1 = new Vertex(cut.getAbsolutePointsPosition().get(0).getX(), wichMax, cut.getAbsolutePointsPosition().get(0).getZ());
            points.add(Vertex.add(v0, new Vertex(-bit.getDiameter() / 2, 0, 0)));
            points.add(Vertex.add(v1, new Vertex(-bit.getDiameter() / 2, 0, 0)));
            points.add(Vertex.add(v1, new Vertex(bit.getDiameter() / 2, 0, 0)));
            points.add(Vertex.add(v0, new Vertex(bit.getDiameter() / 2, 0, 0)));
        } else if (cut.getCutType() == CutType.RECTANGULAR) {

            points.add(new Vertex(cut.getAbsolutePointsPosition().get(0)));
            points.add(new Vertex(cut.getAbsolutePointsPosition().get(1)));
            points.add(new Vertex(cut.getAbsolutePointsPosition().get(2)));
            points.add(new Vertex(cut.getAbsolutePointsPosition().get(3)));
        } else if (cut.getCutType() == CutType.L_SHAPE) {

        }
        return ordonnerPoints(points);
    }

    private static List<Vertex> ordonnerPoints(List<Vertex> list) {
        List<Vertex> points = new ArrayList<>();
        Vertex v0 = list.stream()
                .min(Comparator.comparingDouble(Vertex::getY)
                        .thenComparingDouble(Vertex::getX))
                .orElseThrow();
        Vertex v2 = list.stream()
                .max(Comparator.comparingDouble(Vertex::getY)
                        .thenComparingDouble(Vertex::getX))
                .orElseThrow();
        Vertex v1 = list.stream()
                .filter(p -> p.getX() <= v2.getX())
                .max(Comparator.comparingDouble(Vertex::getY))
                .orElseThrow();
        Vertex v3 = list.stream()
                .filter(p -> !p.equals(v0) && !p.equals(v1) && !p.equals(v2))
                .findFirst()
                .orElseThrow();
        points.add(v0);
        points.add(v1);
        points.add(v2);
        points.add(v3);
        System.out.println(points);
        return points;
    }

    private static List<Mesh> transformListVertexToMeshes(List<Vertex[]> vertices, double depth) {
        List<Mesh> meshes = new ArrayList<>();
        for (Vertex[] meshVertex : vertices) {
            List<Triangle> triangles = new ArrayList<>(List.of(
                    new Triangle(new Vertex(meshVertex[0]), new Vertex(meshVertex[1].getX(), meshVertex[1].getY(), depth), new Vertex(meshVertex[1]), new Vertex(-1, 0, 0), Color.BLUE),
                    new Triangle(new Vertex(meshVertex[0]), new Vertex(meshVertex[1].getX(), meshVertex[1].getY(), depth), new Vertex(meshVertex[0].getX(), meshVertex[0].getY(), depth), new Vertex(-1, 0, 0), Color.BLUE),
                    new Triangle(new Vertex(meshVertex[0]), new Vertex(meshVertex[3]), new Vertex(meshVertex[3].getX(), meshVertex[3].getY(), depth), new Vertex(0, -1, 0), Color.BLUE),
                    new Triangle(new Vertex(meshVertex[0]), new Vertex(meshVertex[3].getX(), meshVertex[3].getY(), depth), new Vertex(meshVertex[0].getX(), meshVertex[0].getY(), depth), new Vertex(0, -1, 0), Color.BLUE),
                    new Triangle(new Vertex(meshVertex[0]), new Vertex(meshVertex[2]), new Vertex(meshVertex[1]), new Vertex(0, 0, -1), Color.BLUE),
                    new Triangle(new Vertex(meshVertex[0]), new Vertex(meshVertex[3]), new Vertex(meshVertex[2]), new Vertex(0, 0, -1), Color.BLUE),
                    new Triangle(new Vertex(meshVertex[2].getX(), meshVertex[2].getY(), depth), new Vertex(meshVertex[3]), new Vertex(meshVertex[3].getX(), meshVertex[3].getY(), depth), new Vertex(1, 0, 0), Color.BLUE),
                    new Triangle(new Vertex(meshVertex[2].getX(), meshVertex[2].getY(), depth), new Vertex(meshVertex[2]), new Vertex(meshVertex[3]), new Vertex(1, 0, 0), Color.BLUE),
                    new Triangle(new Vertex(meshVertex[2].getX(), meshVertex[2].getY(), depth), new Vertex(meshVertex[0].getX(), meshVertex[0].getY(), depth), new Vertex(meshVertex[3].getX(), meshVertex[3].getY(), depth), new Vertex(0, 0, 1), Color.BLUE),
                    new Triangle(new Vertex(meshVertex[2].getX(), meshVertex[2].getY(), depth), new Vertex(meshVertex[1].getX(), meshVertex[1].getY(), depth), new Vertex(meshVertex[0].getX(), meshVertex[0].getY(), depth), new Vertex(0, 0, 1), Color.BLUE),
                    new Triangle(new Vertex(meshVertex[2].getX(), meshVertex[2].getY(), depth), new Vertex(meshVertex[2]), new Vertex(meshVertex[1]), new Vertex(0, 1, 0), Color.BLUE),
                    new Triangle(new Vertex(meshVertex[2].getX(), meshVertex[2].getY(), depth), new Vertex(meshVertex[1]), new Vertex(meshVertex[1].getX(), meshVertex[1].getY(), depth), new Vertex(0, 1, 0), Color.BLUE)
            ));
            meshes.add(new Mesh(new Vertex(0, 0, 0), 0.3, Color.BLUE, triangles));
        }
        return meshes;
    }

    private static List<Vertex[]> cutMesh(List<Vertex[]> meshes, List<Vertex> points) {
        List<Vertex[]> cutMeshes = new ArrayList<>();
//        if (cut.getCutType() == CutType.LINE_VERTICAL) {
//            for (Vertex[] vertices : meshes) {
//                double wichMin = Math.min((cut.getAbsolutePointsPosition().get(0).getY()), cut.getAbsolutePointsPosition().get(1).getY());
//                double wichMax = Math.max((cut.getAbsolutePointsPosition().get(0).getY()), cut.getAbsolutePointsPosition().get(1).getY());
//                if (wichMin == vertices[0].getY() && wichMax == vertices[1].getX()) {
//                    Vertex v1 = new Vertex(vertices[0]);
//                    Vertex v2 = new Vertex(cut.getAbsolutePointsPosition().get(0).getX(), wichMin, cut.getAbsolutePointsPosition().get(0).getZ());
//                    Vertex v3 = new Vertex(cut.getAbsolutePointsPosition().get(0).getX(), wichMax, cut.getAbsolutePointsPosition().get(0).getZ());
//                    Vertex v4 = new Vertex(vertices[1]);
//                    cutMeshes.add(new Vertex[]{v1, v4, Vertex.add(v3, new Vertex(-bit.getDiameter() / 2, 0, 0)), Vertex.add(v2, new Vertex(-bit.getDiameter() / 2, 0, 0))});
//                    cutMeshes.add(new Vertex[]{Vertex.add(v2, new Vertex(bit.getDiameter() / 2, 0, 0)), Vertex.add(v3, new Vertex(bit.getDiameter() / 2, 0, 0)), new Vertex(vertices[2]), new Vertex(vertices[3])});
//                }
//            }
//        }
        for (Vertex[] vertices : meshes) {
            cutMeshes.add(new Vertex[]{vertices[0], vertices[1], points.get(1), points.get(0)});
            cutMeshes.add(new Vertex[]{points.get(1), vertices[1], vertices[2], points.get(2)});
            cutMeshes.add(new Vertex[]{points.get(3), points.get(2), vertices[2], vertices[3]});
            cutMeshes.add(new Vertex[]{vertices[0], points.get(0), points.get(3), vertices[3]});
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

    /**
     * Calcule la pente (m) de la ligne passant par deux points.
     *
     * @param x1 Coordonnée x du premier point.
     * @param y1 Coordonnée y du premier point.
     * @param x2 Coordonnée x du second point.
     * @param y2 Coordonnée y du second point.
     * @return La pente m de la ligne.
     */
    public static Double calculerPente(double x1, double y1, double x2, double y2) {
        if (x1 == x2) {
            return null;
        }
        return (y2 - y1) / (x2 - x1);
    }

    /**
     * Calcule l'ordonnée à l'origine (c) de la ligne y = mx + c.
     *
     * @param x Coordonnée x d'un point.
     * @param y Coordonnée y d'un point.
     * @param m Pente de la ligne.
     * @return L'ordonnée à l'origine c.
     */
    public static double calculerOrdonneeALOrigine(double x, double y, double m) {
        return y - m * x;
    }

    /**
     * Détermine la position d'un point par rapport à une fonction linéaire y = mx + c.
     *
     * @param x Coordonnée x du point.
     * @param y Coordonnée y du point.
     * @param m Pente de la ligne.
     * @param c Ordonnée à l'origine de la ligne.
     * @return Un int
     */
    public static int positionParRapportALigne(double x, double y, double m, double c) {
        double yLigne = m * x + c;

        if (y > yLigne) {
            return 1;
        } else if (y < yLigne) {
            return -1;
        } else {
            return 0;
        }
    }

    private static double area(Vertex a, Vertex b, Vertex c) {
        return Math.abs(a.getX() * (b.getY() - c.getY()) +
                b.getX() * (c.getY() - a.getY()) +
                c.getX() * (a.getY() - b.getY())) / 2.0;
    }
}
