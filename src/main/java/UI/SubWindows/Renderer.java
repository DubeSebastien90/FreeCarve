package UI.SubWindows;

import Domain.ThirdDimension.Mesh;
import Domain.ThirdDimension.Triangle;
import Domain.ThirdDimension.Vertex;
import UI.Widgets.BoutonRotation;
import Domain.ThirdDimension.Matrix;
import Util.UiUtil;

import javax.swing.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

/**
 * The {@code Renderer} class provides function to render meshes on it, it extends {@code JPanel}.
 *
 * @author Adam Côté
 * @since 2024-09-08
 */
public class Renderer extends JPanel {
    private final Vertex vertexX;
    private final Vertex vertexY;
    private final Vertex vertexZ;
    private List<Mesh> meshes;
    private Vertex mousePos;
    private final BoutonRotation boutonRotation;
    private Double[][] pixelsDepthMap;
    double panelHalfWidth;
    double panelHalfHeight;

    /**
     * @return the {@code List} of {@code Mesh} that need to be rendered
     */
    public List<Mesh> getMeshes() {
        return meshes;
    }

    /**
     * @return the mousePos of this instance of {@code Renderer}
     */
    public Vertex getMousePos() {
        return mousePos;
    }

    /**
     * Sets the {@code List} of {@code Mesh} that needs to be rendered to a new one
     *
     * @param meshes the new {@code List} of {@code Mesh}
     */
    public void setMeshes(List<Mesh> meshes) {
        this.meshes = meshes;
    }

    /**
     * Constructs a new {@code Renderer} object with a {@code List} of {@code Mesh} and initialize {@code Renderer}
     *
     * @param meshes the {@code List} of {@code Mesh}
     */
    public Renderer(List<Mesh> meshes) {
        setDoubleBuffered(true);
        setFocusable(true);
        requestFocusInWindow();
        requestFocus();
        this.vertexX = new Vertex(1, 0, 0);
        this.vertexY = new Vertex(0, 1, 0);
        this.vertexZ = new Vertex(0, 0, 1);
        setMeshes(meshes);
        boutonRotation = new BoutonRotation(this);
        addKeyListener(boutonRotation);
        addMouseListener(boutonRotation);
        this.mousePos = new Vertex(0, 0, 0);
    }

    @Override
    public void setSize(int width, int height) {
        super.setSize(width, height);
        panelHalfWidth = width / 2f;
        panelHalfHeight = height / 2f;
    }

    /**
     * Sets the mouse position variable at a specific {@code Vertex}
     *
     * @param mousePos the position of the mouse
     */
    public void setMousePos(Vertex mousePos) {
        this.mousePos = mousePos;
    }

    /**
     * Paint the {@code List} of {@code Mesh} on a {@code Graphics2D} and then draw it on the {@code Renderer}
     *
     * @param graphics a {@code Graphics} object on which the meshes will be drawn
     */
    @Override
    public void paintComponent(Graphics graphics) {
        Graphics2D graphics2D = ((Graphics2D) graphics);
        this.setBackground(Color.GRAY);
        UiUtil.makeJPanelRoundCorner(this, graphics2D);
        super.paintComponent(graphics2D);
        pixelsDepthMap = new Double[this.getWidth()][this.getHeight()];
        panelHalfWidth = getWidth() / 2f;
        panelHalfHeight = getHeight() / 2f;
        Mesh mesh = null;
        for (Mesh m : meshes) {
            for (Triangle t : m.getTrianglesList()) {
                if (printTriangle(graphics2D, t)) {
                    mesh = m;
                }
            }
        }
        if (mousePos.getZ() == 1) {
            boutonRotation.setSelectedMesh(mesh);
        }
        setMousePos(new Vertex(0, 0, 0));
    }

    /**
     * Paints all the {@code Triangle} objects in a {@code List} in 3D on a {@code JPanel}.
     * To be painted on the JPanel, the triangles must be drawn using a {@code Graphics2D} object.
     * The {@code Graphics2D} object must already be associated with the JPanel.<br/><br/>
     * The function uses a depth map for to be sure the triangles are displayed correctly,
     * barycentric coordinates to paint the pixels of the triangle and normal vectors to implement base shading.
     * <br/><br/>
     *
     * @param graphics2D the {@code Graphics2D} object associated with the panel
     */
    private boolean printTriangle(Graphics2D graphics2D, Triangle triangle) {
        BufferedImage img = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        boolean isSelected = false;

        Triangle newTriangle = new Triangle(
                Vertex.add(new Vertex(panelHalfWidth, panelHalfHeight, 0), triangle.getVertex1()),
                Vertex.add(new Vertex(panelHalfWidth, panelHalfHeight, 0), triangle.getVertex2()),
                Vertex.add(new Vertex(panelHalfWidth, panelHalfHeight, 0), triangle.getVertex3()),
                triangle.getNormal(), triangle.getColor());
        newTriangle.calculateNormal();

        Color printedColor = calculateLighting(newTriangle);
        int[] area = newTriangle.findBoundingRectangle(img.getWidth(), img.getHeight());
        for (int y = area[2]; y <= area[3]; y++) {
            for (int x = area[0]; x <= area[1]; x++) {
                Vertex bary = newTriangle.findBarycentric(x, y);
                if (isInBarycentric(bary)) {
                    double depth = bary.getX() * newTriangle.getVertex1().getZ() + bary.getY() * newTriangle.getVertex2().getZ() + bary.getZ() * newTriangle.getVertex3().getZ();
                    if (x < getWidth() && y < getHeight()) {
                        if ((pixelsDepthMap[x][y] == null || pixelsDepthMap[x][y] < depth)) {
                            pixelsDepthMap[x][y] = depth;
                            img.setRGB(x, y, printedColor.getRGB());
                            if (getMousePos().getX() == x && getMousePos().getY() == y) {
                                isSelected = true;
                            }
                        }
                    }
                }
            }
        }
        graphics2D.drawImage(img, 0, 0, null);

        return isSelected;
    }

    /**
     * Rotate all of the meshes around the (0,0,0) point
     *
     * @param rotationMatrix - the rotation matrix
     */
    public void rotateWorld(Matrix rotationMatrix) {
        for (Mesh m : meshes) {
            for (Triangle t : m.getTrianglesList()) {
                t.setVertex1(rotationMatrix.matriceXVertex3x3(t.getVertex1()));
                t.setVertex2(rotationMatrix.matriceXVertex3x3(t.getVertex2()));
                t.setVertex3(rotationMatrix.matriceXVertex3x3(t.getVertex3()));
            }
            //m.setVerticesList();
        }
        vertexX.setVertex(rotationMatrix.matriceXVertex3x3(vertexX));
        vertexY.setVertex(rotationMatrix.matriceXVertex3x3(vertexY));
        vertexZ.setVertex(rotationMatrix.matriceXVertex3x3(vertexZ));
        repaint();
    }

    /**
     * Move a specific mesh in the scene
     * TODO put this function in Mesh
     *
     * @param mesh        - the mesh to move
     * @param translation - the movement vector
     */
    public void translationMesh(Mesh mesh, Vertex translation) {
        Vertex translationModif = new Vertex(0, 0, 0);
        Vertex tempVertexX = new Vertex(vertexX);
        tempVertexX.multiply(translation.getX());
        translationModif.add(tempVertexX);

        Vertex tempVertexY = new Vertex(vertexY);
        tempVertexY.multiply(translation.getY());
        translationModif.add(tempVertexY);

        Vertex tempVertexZ = new Vertex(vertexZ);
        tempVertexZ.multiply(translation.getZ());
        translationModif.add(tempVertexZ);
        for (Triangle t : mesh.getTrianglesList()) {
            t.getVertex1().add(translationModif);
            t.getVertex2().add(translationModif);
            t.getVertex3().add(translationModif);
        }
        mesh.setVerticesList();
        mesh.getPosition().add(translation);
        repaint();
    }

    /**
     * Rotate a specific mesh around it's center
     * TODO put this function in Mesh
     *
     * @param mesh the mesh to rotate
     * @param axis the axis to turn around
     * @param size the direction of the rotation
     */
    public void rotationMesh(Mesh mesh, Vertex axis, double size) {
        Matrix rotationMatrice = getRotationMatrixAroundVector(axis, size);
        Vertex center = mesh.getCenter();
        for (Triangle t : mesh.getTrianglesList()) {
            t.getVertex1().subtract(center);
            t.setVertex1(rotationMatrice.matriceXVertex3x3(t.getVertex1()));
            t.getVertex1().add(center);

            t.getVertex2().subtract(center);
            t.setVertex2(rotationMatrice.matriceXVertex3x3(t.getVertex2()));
            t.getVertex2().add(center);

            t.getVertex3().subtract(center);
            t.setVertex3(rotationMatrice.matriceXVertex3x3(t.getVertex3()));
            t.getVertex3().add(center);
        }
        mesh.setVerticesList();
        repaint();
    }
//1,0,0,,,0,cos(0.05),-sin(0,05),,,,0,sin(0,05),cos(0,05),

    /**
     * Returns a rotation matrix to apply on a mesh for it to rotate on a certain angle
     *
     * @param v    the axis to rotate around
     * @param size the size of the rotation
     * @return the rotation matrix to apply on the vectors
     */
    private static Matrix getRotationMatrixAroundVector(Vertex v, double size) {
        double ux = v.getX(), uy = v.getY(), uz = v.getZ();
        double c = Math.cos(0.05 * size), s = Math.sin(0.05 * size);
        return new Matrix(new double[]{
                Math.pow(ux, 2) * (1 - c) + c, ux * uy * (1 - c) - uz * s, ux * uz * (1 - c) + uy * s,
                ux * uy * (1 - c) + uz * s, Math.pow(uy, 2) * (1 - c) + c, uy * uz * (1 - c) - ux * s,
                ux * uz * (1 - c) - uy * s, uy * uz * (1 - c) + ux * s, Math.pow(uz, 2) * (1 - c) + c
        });
    }

    /**
     * Uses the normal vector of this instance of {@code Triangle} to calculate the shading to be applied to the triangle.
     * It uses the dot product formula to find the cosine between the light source and the normal vector.
     * The resulting value will be multiplied by all the RGB values of the triangle's color.
     * <b>Note that the normal vector of this instance of {@code Triangle} must be normalized.</b>
     * If something doesn't work, the triangle's original color will be returned.
     *
     * @return the new color with shading applied
     */
    private static Color calculateLighting(Triangle triangle) {
        Vertex normal = triangle.getNormal();
        Color color = triangle.getColor();
        Vertex lightDirection = new Vertex(1, 1, 1);
        double ligthMagnitude = Math.sqrt(Math.pow(lightDirection.getX(), 2) + Math.pow(lightDirection.getY(), 2) + Math.pow(lightDirection.getZ(), 2));
        float darker = (float) ((lightDirection.getX() * Math.abs(normal.getX()) + lightDirection.getY() * Math.abs(normal.getY()) + lightDirection.getZ() * Math.abs(normal.getZ())) / ligthMagnitude);
        float[] component = color.getRGBColorComponents(null);
        try {
            return new Color(component[0] * darker, component[1] * darker, component[2] * darker);
        } catch (Exception ignored) {
            return color;
        }
    }

    /**
     * Validates if a vertex meets the requirements to represent barycentric coordinates of a triangle.
     *
     * @param coordinates the vertex which may contain barycentric coordinates
     * @return true if the vertex represents barycentric coordinates
     */
    private static boolean isInBarycentric(Vertex coordinates) {
        double sum = coordinates.getY() + coordinates.getX() + coordinates.getZ();
        return (sum < 1.05) && (sum > .95) && coordinates.getX() >= 0 && coordinates.getY() >= 0 && coordinates.getZ() >= 0;
    }

    /**
     * Getter of the vertexX attribute
     *
     * @return the vertexX attribute
     */
    public Vertex getVertexX() {
        return vertexX;
    }

    /**
     * Getter of the vertexY attribute
     *
     * @return the vertexY attribute
     */
    public Vertex getVertexY() {
        return vertexY;
    }

    /**
     * Getter of the vertexZ attribute
     *
     * @return the vertexZ attribute
     */
    public Vertex getVertexZ() {
        return vertexZ;
    }

}
