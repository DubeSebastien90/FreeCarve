package Domain.ThirdDimension;

import Common.DTO.*;
import Domain.CutType;
import Domain.IO.ParsedSTL;
import Domain.IO.STLParser;
import eu.mihosoft.vrl.v3d.CSG;
import eu.mihosoft.vrl.v3d.Cube;

import java.awt.*;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
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

        CSG panneau = new Cube(panel.getPanelDimension().getX(), panel.getPanelDimension().getY(), panel.getPanelDimension().getZ()).toCSG();
        for (CutDTO cut : panel.getCutsDTO()) {
            if (cut.getCutType() == CutType.LINE_VERTICAL) {
                panneau = panneau.difference(createVerticalCut(cut, bits, panel));
            } else if (cut.getCutType() == CutType.LINE_HORIZONTAL) {
                panneau = panneau.difference(createHorizontalCut(cut, bits, panel));
            } else if (cut.getCutType() == CutType.RECTANGULAR) {
                CSG[] cuts = createRectangularCut(cut, bits, panel);
                for (CSG si : cuts) {
                    panneau = panneau.difference(si);
                }
            } else if (cut.getCutType() == CutType.BORDER) {
                CSG[] cuts = createBorderCut(cut, bits, panel);
                for (CSG si : cuts) {
                    panneau = panneau.difference(si);
                }
            } else if (cut.getCutType() == CutType.L_SHAPE) {
                CSG[] cuts = createLCut(cut, bits, panel);
                for (CSG si : cuts) {
                    panneau = panneau.difference(si);
                }
            } else if (cut.getCutType() == CutType.LINE_FREE) {
                panneau = panneau.difference(createFreeCut(cut, bits, panel));
            }
        }
        Mesh finalMesh = new Mesh(new Vertex(50, 50, 0), 0.5, Color.BLUE, convertStringToVertex(panneau.toStlString()));
        List<Mesh> arr = new ArrayList<>();
        arr.add(finalMesh);
        return arr;
    }

    private static CSG createHorizontalCut(CutDTO cut, BitDTO[] bits, PanelDTO panel) {
        CSG cut3d = new Cube(bits[cut.getBitIndex()].getDiameter(), Math.abs(cut.getAbsolutePointsPosition().get(0).getX() - cut.getAbsolutePointsPosition().get(1).getX()), panel.getPanelDimension().getZ()).toCSG();
        cut3d = cut3d.transformed(eu.mihosoft.vrl.v3d.Transform.unity().rotZ(90));
        cut3d = cut3d.transformed(eu.mihosoft.vrl.v3d.Transform.unity().translate((cut.getAbsolutePointsPosition().get(0).getX() + cut.getAbsolutePointsPosition().get(1).getX()) / 2 - panel.getPanelDimension().getX() / 2, cut.getAbsolutePointsPosition().get(0).getY() - panel.getPanelDimension().getY() / 2 + bits[cut.getBitIndex()].getDiameter() / 2, 0));
        return cut3d;
    }

    private static CSG createVerticalCut(CutDTO cut, BitDTO[] bits, PanelDTO panel) {
        CSG cut3d = new Cube(bits[cut.getBitIndex()].getDiameter(), Math.abs(cut.getAbsolutePointsPosition().get(0).getY() - cut.getAbsolutePointsPosition().get(1).getY()), panel.getPanelDimension().getZ()).toCSG();
        cut3d = cut3d.transformed(eu.mihosoft.vrl.v3d.Transform.unity().translate(cut.getAbsolutePointsPosition().get(0).getX() - panel.getPanelDimension().getX() / 2 + bits[cut.getBitIndex()].getDiameter() / 2, (cut.getAbsolutePointsPosition().get(0).getY() + cut.getAbsolutePointsPosition().get(1).getY()) / 2 - panel.getPanelDimension().getY() / 2, 0));
        return cut3d;
    }

    private static CSG[] createRectangularCut(CutDTO cut, BitDTO[] bits, PanelDTO panel) {
        List<VertexDTO> arr1 = new ArrayList<>();
        arr1.add(cut.getAbsolutePointsPosition().get(0));
        arr1.add(cut.getAbsolutePointsPosition().get(1));
        List<RefCutDTO> what = new ArrayList<>();
        List<VertexDTO> arr2 = new ArrayList<>();
        arr2.add(cut.getAbsolutePointsPosition().get(1));
        arr2.add(cut.getAbsolutePointsPosition().get(2));
        List<VertexDTO> arr3 = new ArrayList<>();
        arr3.add(cut.getAbsolutePointsPosition().get(2));
        arr3.add(cut.getAbsolutePointsPosition().get(3));
        List<VertexDTO> arr4 = new ArrayList<>();
        arr4.add(cut.getAbsolutePointsPosition().get(3));
        arr4.add(cut.getAbsolutePointsPosition().get(0));
        CSG cut1 = createHorizontalCut(new CutDTO(new UUID(1, 1), cut.getDepth(), cut.getBitIndex(), CutType.LINE_HORIZONTAL, arr1, what), bits, panel);
        CSG cut2 = createVerticalCut(new CutDTO(new UUID(1, 1), cut.getDepth(), cut.getBitIndex(), CutType.LINE_VERTICAL, arr2, what), bits, panel);
        CSG cut3 = createHorizontalCut(new CutDTO(new UUID(1, 1), cut.getDepth(), cut.getBitIndex(), CutType.LINE_HORIZONTAL, arr3, what), bits, panel);
        CSG cut4 = createVerticalCut(new CutDTO(new UUID(1, 1), cut.getDepth(), cut.getBitIndex(), CutType.LINE_VERTICAL, arr4, what), bits, panel);
        return new CSG[]{cut1, cut2, cut3, cut4};
    }

    private static CSG[] createBorderCut(CutDTO cut, BitDTO[] bits, PanelDTO panel) {
        return new CSG[]{};
    }

    private static CSG[] createLCut(CutDTO cut, BitDTO[] bits, PanelDTO panel) {
        return new CSG[]{};
    }

    private static CSG createFreeCut(CutDTO cut, BitDTO[] bits, PanelDTO panel) {
        return null;
    }

    public static List<Triangle> convertStringToVertex(String stlString) {
        stlString = stlString.replaceAll("v3d.csg", "");
        stlString = stlString.replaceAll("normal ", "\\$");
        stlString = stlString.replaceAll("vertex ", "_");
        stlString = stlString.replaceAll("(?<=\\\\d)([eE])(?=[+-]?\\\\d+)", "!");
        stlString = stlString.replaceAll("[A-Za-z]", "");
        stlString = stlString.replaceAll("\n", "");
        stlString = stlString.replaceAll("!", "E");
        stlString = stlString.trim();
        stlString = stlString.replaceFirst("\\$", "");
        String[] stlStrings = stlString.split("\\$");
        List<Triangle> allVertex = new ArrayList<>();
        for (String s : stlStrings) {
            for (int i = 0; i < 4; i++) {
                List<Vertex> li = new ArrayList<>();
                for (String single : s.split("_")) {
                    single = single.trim();
                    String[] trio = single.split(" ");
                    Double d1;
                    Double d2;
                    Double d3;
                    try {
                        d1 = Double.parseDouble(trio[0]);
                    } catch (Exception e) {
                        d1 = 0.0;
                    }
                    try {
                        d2 = Double.parseDouble(trio[1]);
                    } catch (Exception e) {
                        d2 = 0.0;
                    }
                    try {
                        d3 = Double.parseDouble(trio[2]);
                    } catch (Exception e) {
                        d3 = 0.0;
                    }
                    Vertex vert = new Vertex(d1, d2, d3);
                    li.add(vert);
                }
                System.out.println(new Triangle(li.get(1), li.get(2), li.get(3), li.get(0), Color.BLUE));
                allVertex.add(new Triangle(li.get(1), li.get(2), li.get(3), li.get(0), Color.BLUE));
            }
        }
        return allVertex;
    }
}
