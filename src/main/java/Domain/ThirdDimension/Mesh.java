package Domain.ThirdDimension;

import Common.CutState;
import Common.DTO.*;
import Common.InvalidCutState;
import Domain.Controller;
import Domain.CutType;
import Domain.IO.STLParser;
import eu.mihosoft.vrl.v3d.CSG;
import eu.mihosoft.vrl.v3d.Cube;
import eu.mihosoft.vrl.v3d.Cylinder;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
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
     * @param position       - the position of the mesh in the scene
     * @param color          - the color of the mesh
     * @param stlInputStream - Input stream to the file containing the triangles
     * @param scale          - The scaling factor to apply to the read triangles
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
     * Converts the current panel on the CNC into a Mesh, a 3d representation of the panel.
     *
     * @param controller the controller managing the current scene
     * @param panel      the panel currently being processed by the CNC machine
     * @param bits       the available CNC machine bits used for cutting
     * @return The Mesh representing the panel
     */
    public static List<Mesh> PanelToMesh(Controller controller, PanelDTO panel, BitDTO[] bits) {
        List<Vertex[]> meshes = new ArrayList<>();
        meshes.add(new Vertex[]{new Vertex(0, 0, 0), new Vertex(0, panel.getPanelDimension().getY(), 0), new Vertex(panel.getPanelDimension().getX(), panel.getPanelDimension().getY(), 0), new Vertex(panel.getPanelDimension().getX(), 0, 0)});

        CSG panneau = new Cube(panel.getPanelDimension().getX(), panel.getPanelDimension().getY(), panel.getPanelDimension().getZ()).toCSG();
        for (CutDTO cut : panel.getCutsDTO()) {
            if (cut.getState() == CutState.VALID) {
                if (cut.getCutType() == CutType.LINE_VERTICAL) {
                    panneau = panneau.difference(createVerticalCut(controller, cut, bits, panel));
                } else if (cut.getCutType() == CutType.LINE_HORIZONTAL) {
                    panneau = panneau.difference(createHorizontalCut(controller, cut, bits, panel));
                } else if (cut.getCutType() == CutType.RECTANGULAR) {
                    CSG[] cuts = createRectangularCut(controller, cut, bits, panel);
                    for (CSG si : cuts) {
                        panneau = panneau.difference(si);
                    }
                } else if (cut.getCutType() == CutType.RETAILLER) {
                    panneau = panneau.difference(createRectangularCut(controller, cut, bits, panel));
                } else if (cut.getCutType() == CutType.L_SHAPE) {
                    CSG[] cuts = createLCut(controller, cut, bits, panel);
                    for (CSG si : cuts) {
                        panneau = panneau.difference(si);
                    }
                } else if (cut.getCutType() == CutType.LINE_FREE) {
                    panneau = panneau.difference(createFreeCut(controller, cut, bits, panel));
                }
            }
        }
        Mesh finalMesh = new Mesh(new Vertex(50, 50, 0), 0.5, new Color(222, 184, 135), convertStringToVertex(panneau.toStlString()));
        List<Mesh> arr = new ArrayList<>();
        arr.add(finalMesh);
        return arr;
    }

    /**
     * Creates a CSG object (Mesh equivalent but from JCSG) from a Horizontal Cut.
     * <br><br>
     * This method converts a cut of type {@code CutType.LINE_HORIZONTAL} into a
     * corresponding CSG representation based on the specified cut attributes.
     * <br>
     *
     * @param controller the controller managing the current scene
     * @param cut        the Horizontal Cut to convert (must be of type {@code CutType.LINE_HORIZONTAL})
     * @param bits       the available CNC machine bits used for cutting
     * @param panel      the panel currently being processed by the CNC machine
     * @return the generated CSG object representing the Horizontal cut
     */
    private static CSG createHorizontalCut(Controller controller, CutDTO cut, BitDTO[] bits, PanelDTO panel) {
        double ray = bits[cut.getBitIndex()].getDiameter() / 2;
        List<VertexDTO> points = controller.getAbsolutePointsPosition(cut);
        CSG cut3d = new Cube(ray * 2, Math.abs(points.get(0).getX() - points.get(1).getX()), cut.getDepth()).toCSG();
        cut3d = cut3d.transformed(eu.mihosoft.vrl.v3d.Transform.unity().rotZ(90));
        cut3d = cut3d.transformed(eu.mihosoft.vrl.v3d.Transform.unity().translate(((points.get(0).getX() + points.get(1).getX()) / 2 - panel.getPanelDimension().getX() / 2), (points.get(0).getY() - panel.getPanelDimension().getY() / 2 + bits[cut.getBitIndex()].getDiameter() / 2) - ray, (panel.getPanelDimension().getZ() - cut.getDepth()) / 2));

        CSG cyl = new Cylinder(ray, cut.getDepth()).toCSG();
        cyl = cyl.transformed(eu.mihosoft.vrl.v3d.Transform.unity().translate(cut3d.getCenterX(), cut3d.getCenterY(), panel.getPanelDimension().getZ() / 2 - cut.getDepth()));
        CSG firstEnd = cyl.transformed(eu.mihosoft.vrl.v3d.Transform.unity().translate(cut3d.getMinX() - cut3d.getCenterX(), 0, 0));
        CSG secEnd = cyl.transformed(eu.mihosoft.vrl.v3d.Transform.unity().translate(cut3d.getMaxX() - cut3d.getCenterX(), 0, 0));
        cut3d = cut3d.union(firstEnd);
        cut3d = cut3d.union(secEnd);

        return cut3d;
    }

    /**
     * Creates a CSG object (Mesh equivalent but from JCSG) from a Vertical Cut.
     * <br><br>
     * This method converts a cut of type {@code CutType.LINE_VERTICAL} into a
     * corresponding CSG representation based on the specified cut attributes.
     * <br>
     *
     * @param controller the controller managing the current scene
     * @param cut        the Vertical Cut to convert (must be of type {@code CutType.LINE_VERTICAL})
     * @param bits       the available CNC machine bits used for cutting
     * @param panel      the panel currently being processed by the CNC machine
     * @return the generated CSG object representing the Vertical cut
     */
    private static CSG createVerticalCut(Controller controller, CutDTO cut, BitDTO[] bits, PanelDTO panel) {
        double ray = bits[cut.getBitIndex()].getDiameter() / 2;
        List<VertexDTO> points = controller.getAbsolutePointsPosition(cut);

        CSG cut3d = new Cube(ray * 2, Math.abs(points.get(0).getY() - points.get(1).getY()), cut.getDepth()).toCSG();
        cut3d = cut3d.transformed(eu.mihosoft.vrl.v3d.Transform.unity().translate(points.get(0).getX() - panel.getPanelDimension().getX() / 2, ((points.get(0).getY() + points.get(1).getY()) / 2 - panel.getPanelDimension().getY() / 2), (panel.getPanelDimension().getZ() - cut.getDepth()) / 2));

        CSG cyl = new Cylinder(ray, cut.getDepth()).toCSG();
        cyl = cyl.transformed(eu.mihosoft.vrl.v3d.Transform.unity().translate(cut3d.getCenterX(), cut3d.getCenterY(), panel.getPanelDimension().getZ() / 2 - cut.getDepth()));
        CSG firstEnd = cyl.transformed(eu.mihosoft.vrl.v3d.Transform.unity().translate(0, cut3d.getMinY() - cut3d.getCenterY(), 0));
        CSG secEnd = cyl.transformed(eu.mihosoft.vrl.v3d.Transform.unity().translate(0, cut3d.getMaxY() - cut3d.getCenterY(), 0));
        cut3d = cut3d.union(firstEnd);
        cut3d = cut3d.union(secEnd);
        return cut3d;
    }

    /**
     * Creates a CSG object (Mesh equivalent but from JCSG) from a Rectangular Cut.
     * <br><br>
     * This method converts a cut of type {@code CutType.RECTANGLE} into a
     * corresponding CSG representation based on the specified CNC settings.
     * <br>
     *
     * @param controller the controller managing the current scene
     * @param cut        the Rectangle Cut to convert (must be of type {@code CutType.RECTANGULAR})
     * @param bits       the available CNC machine bits used for cutting
     * @param panel      the panel currently being processed by the CNC machine
     * @return the generated CSG object representing the Rectangle cut
     */
    private static CSG[] createRectangularCut(Controller controller, CutDTO cut, BitDTO[] bits, PanelDTO panel) {
        List<VertexDTO> points = controller.getAbsolutePointsPosition(cut);
        List<VertexDTO> arr1 = new ArrayList<>();
        arr1.add(points.get(0));
        arr1.add(points.get(3));
        List<RefCutDTO> what = new ArrayList<>();
        List<VertexDTO> arr2 = new ArrayList<>();
        arr2.add(points.get(0));
        arr2.add(points.get(1));
        List<VertexDTO> arr3 = new ArrayList<>();
        arr3.add(points.get(1));
        arr3.add(points.get(2));
        List<VertexDTO> arr4 = new ArrayList<>();
        arr4.add(points.get(3));
        arr4.add(points.get(2));
        CSG cut1 = createHorizontalCut(controller, new CutDTO(new UUID(1, 1), cut.getDepth(), cut.getBitIndex(), CutType.LINE_HORIZONTAL, arr1, what, CutState.VALID, InvalidCutState.DEFAULT), bits, panel);
        CSG cut2 = createVerticalCut(controller, new CutDTO(new UUID(1, 1), cut.getDepth(), cut.getBitIndex(), CutType.LINE_VERTICAL, arr2, what, CutState.VALID, InvalidCutState.DEFAULT), bits, panel);
        CSG cut3 = createHorizontalCut(controller, new CutDTO(new UUID(1, 1), cut.getDepth(), cut.getBitIndex(), CutType.LINE_HORIZONTAL, arr3, what, CutState.VALID, InvalidCutState.DEFAULT), bits, panel);
        CSG cut4 = createVerticalCut(controller, new CutDTO(new UUID(1, 1), cut.getDepth(), cut.getBitIndex(), CutType.LINE_VERTICAL, arr4, what, CutState.VALID, InvalidCutState.DEFAULT), bits, panel);
        return new CSG[]{cut1, cut2, cut3, cut4};
    }

    /**
     * Creates a CSG object (Mesh equivalent but from JCSG) from a Border Cut.
     * <br><br>
     * This method converts a cut of type {@code CutType.RETAILLE} into a
     * corresponding CSG representation based on the specified CNC settings.
     * <br>
     *
     * @param controller the controller managing the current scene
     * @param cut        the Border Cut to convert (must be of type {@code CutType.RETAILLE})
     * @param bits       the available CNC machine bits used for cutting
     * @param panel      the panel currently being processed by the CNC machine
     * @return the generated CSG object representing the Border cut
     */
    private static CSG createBorderCut(Controller controller, CutDTO cut, BitDTO[] bits, PanelDTO panel) {
        VertexDTO point = cut.getPoints().get(0);
        List<RefCutDTO> what = new ArrayList<>();
        List<VertexDTO> arr1 = new ArrayList<>();
        if (cut.getRefsDTO().get(0).getIndex() % 2 != 0) {
            if (cut.getRefsDTO().get(0).getIndex() == 1) {
                arr1.add(new VertexDTO(0, panel.getPanelDimension().getY() - point.getX(), point.getZ()));
                arr1.add(new VertexDTO(panel.getPanelDimension().getX(), panel.getPanelDimension().getY() - point.getX(), point.getZ()));
                return createHorizontalCut(controller, new CutDTO(new UUID(1, 1), cut.getDepth(), cut.getBitIndex(), CutType.LINE_HORIZONTAL, arr1, what, CutState.VALID, InvalidCutState.DEFAULT), bits, panel);
            }
            arr1.add(new VertexDTO(0, point.getX(), point.getZ()));
            arr1.add(new VertexDTO(panel.getPanelDimension().getX(), point.getX(), point.getZ()));
            return createHorizontalCut(controller, new CutDTO(new UUID(1, 1), cut.getDepth(), cut.getBitIndex(), CutType.LINE_HORIZONTAL, arr1, what, CutState.VALID, InvalidCutState.DEFAULT), bits, panel);
        }
        if (cut.getRefsDTO().get(0).getIndex() == 2) {
            arr1.add(new VertexDTO(panel.getPanelDimension().getX() - point.getX(), 0, point.getZ()));
            arr1.add(new VertexDTO(panel.getPanelDimension().getX() - point.getX(), panel.getPanelDimension().getY(), point.getZ()));
            return createVerticalCut(controller, new CutDTO(new UUID(1, 1), cut.getDepth(), cut.getBitIndex(), CutType.LINE_VERTICAL, arr1, what, CutState.VALID, InvalidCutState.DEFAULT), bits, panel);
        }
        arr1.add(new VertexDTO(point.getX(), 0, point.getZ()));
        arr1.add(new VertexDTO(point.getX(), panel.getPanelDimension().getY(), point.getZ()));
        return createVerticalCut(controller, new CutDTO(new UUID(1, 1), cut.getDepth(), cut.getBitIndex(), CutType.LINE_VERTICAL, arr1, what, CutState.VALID, InvalidCutState.DEFAULT), bits, panel);
    }

    /**
     * Creates a CSG object (Mesh equivalent but from JCSG) from a L-shape Cut.
     * <br><br>
     * This method converts a cut of type {@code CutType.L_SHAPE} into a
     * corresponding CSG representation based on the specified CNC settings.
     * <br>
     *
     * @param controller the controller managing the current scene
     * @param cut        the L_shape Cut to convert (must be of type {@code CutType.L_SHAPE})
     * @param bits       the available CNC machine bits used for cutting
     * @param panel      the panel currently being processed by the CNC machine
     * @return the generated CSG object representing the L-shape cut
     */
    private static CSG[] createLCut(Controller controller, CutDTO cut, BitDTO[] bits, PanelDTO panel) {
        List<VertexDTO> points = controller.getAbsolutePointsPosition(cut);
        List<RefCutDTO> what = new ArrayList<>();
        List<VertexDTO> arr1 = new ArrayList<>();
        List<VertexDTO> arr2 = new ArrayList<>();
        arr1.add(points.get(2));
        arr1.add(points.get(1));
        arr2.add(points.get(1));
        arr2.add(points.get(0));
        CSG cut1 = createVerticalCut(controller, new CutDTO(new UUID(1, 1), cut.getDepth(), cut.getBitIndex(), CutType.LINE_VERTICAL, arr1, what, CutState.VALID, InvalidCutState.DEFAULT), bits, panel);
        CSG cut2 = createHorizontalCut(controller, new CutDTO(new UUID(1, 1), cut.getDepth(), cut.getBitIndex(), CutType.LINE_HORIZONTAL, arr2, what, CutState.VALID, InvalidCutState.DEFAULT), bits, panel);
        return new CSG[]{cut1, cut2};
    }

    /**
     * Creates a CSG object (Mesh equivalent but from JCSG) from a FreeCut Cut.
     * <br><br>
     * This method converts a cut of type {@code CutType.LINE_FREE} into a
     * corresponding CSG representation based on the specified CNC settings.
     * <br>
     *
     * @param controller the controller managing the current scene
     * @param cut        the FreeCut Cut to convert (must be of type {@code CutType.LINE_FREE})
     * @param bits       the available CNC machine bits used for cutting
     * @param panel      the panel currently being processed by the CNC machine
     * @return the generated CSG object representing the FreeCut
     */
    private static CSG createFreeCut(Controller controller, CutDTO cut, BitDTO[] bits, PanelDTO panel) {
        List<VertexDTO> points = controller.getAbsolutePointsPosition(cut);
        double ray = bits[cut.getBitIndex()].getDiameter() / 2;
        double hypothenus = Math.sqrt(Math.pow(points.get(0).getY() - points.get(1).getY(), 2) + Math.pow(points.get(0).getX() - points.get(1).getX(), 2));
        CSG cut3d = new Cube(bits[cut.getBitIndex()].getDiameter(), hypothenus - ray, cut.getDepth()).toCSG();
        cut3d = cut3d.transformed(eu.mihosoft.vrl.v3d.Transform.unity().rotZ(90));
        double ratio = (points.get(1).getY() - points.get(0).getY()) / (points.get(1).getX() - points.get(0).getX());
        double rot = -Math.toDegrees(Math.atan(ratio));
        cut3d = cut3d.transformed(eu.mihosoft.vrl.v3d.Transform.unity().rotZ(rot));
        cut3d = cut3d.transformed(eu.mihosoft.vrl.v3d.Transform.unity().translate((((points.get(0).getX() + points.get(1).getX()) / 2) - panel.getPanelDimension().getX() / 2), ((points.get(0).getY() + points.get(1).getY()) / 2 - panel.getPanelDimension().getY() / 2), (panel.getPanelDimension().getZ() - cut.getDepth()) / 2));
        CSG cyl = new Cylinder(bits[cut.getBitIndex()].getDiameter() / 2, cut.getDepth()).toCSG();
        CSG firstEnd = cyl.transformed(eu.mihosoft.vrl.v3d.Transform.unity().translate(hypothenus / 2, 0, 0));
        CSG secEnd = cyl.transformed(eu.mihosoft.vrl.v3d.Transform.unity().translate(hypothenus / -2, 0, 0));
        firstEnd = firstEnd.transformed(eu.mihosoft.vrl.v3d.Transform.unity().rotZ(rot));
        secEnd = secEnd.transformed(eu.mihosoft.vrl.v3d.Transform.unity().rotZ(rot));
        firstEnd = firstEnd.transformed(eu.mihosoft.vrl.v3d.Transform.unity().translate(cut3d.getCenterX(), cut3d.getCenterY(), panel.getPanelDimension().getZ() / 2 - cut.getDepth()));
        secEnd = secEnd.transformed(eu.mihosoft.vrl.v3d.Transform.unity().translate(cut3d.getCenterX(), cut3d.getCenterY(), panel.getPanelDimension().getZ() / 2 - cut.getDepth()));
        cut3d = cut3d.union(firstEnd);
        cut3d = cut3d.union(secEnd);
        return cut3d;
    }

    /**
     * Parses a the stl string format of the JCSG library into a list of triangle.
     *
     * @param stlString The string which represent the mesh in stl format.
     * @return A list of triangle formed based on the stl String.
     */
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
                allVertex.add(new Triangle(li.get(1), li.get(2), li.get(3), li.get(0), new Color(222, 184, 135)));
            }
        }
        return allVertex;
    }
}
