package Domain.ThirdDimension;

import Domain.ThirdDimension.Mesh;
import Domain.ThirdDimension.Triangle;
import Domain.ThirdDimension.Vertex;
import Domain.ThirdDimension.Matrix;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;

/**
 * The {@code Renderer} class provides function to render meshes on it, it extends {@code JPanel}.
 *
 * @author Adam Côté
 * @author Sébastien Dubé
 * @author Kamran Charles Nayebi
 * @since 2024-09-08
 */
public class Camera extends Transform {
    private List<Mesh> meshes;
    private Vertex mousePos;

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
    public Camera(Vertex position, double scale, List<Mesh> meshes) {
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
        panelHalfWidth = getWidth() / 2f;
        panelHalfHeight = getHeight() / 2f;
        pixelsDepthMap = new Double[this.getWidth()][this.getHeight()];
        Mesh mesh = null;
        BufferedImage img = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        for (Mesh m : meshes) {
            for (Triangle t : m.getLocalTriangles()) {
                if (printTriangle(img, t, m)) {
                    mesh = m;
                }
            }
        }
        graphics2D.drawImage(img, 0, 0, null);
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
     * @param img the {@code Image} object associated with the panel
     */
    private boolean printTriangle(BufferedImage img, Triangle triangle, Mesh parent) {
        boolean isSelected = false;

        Triangle t = parent.getTransformedTriangle(triangle);
        Arrays.stream(t.getVertices()).forEach(vertex -> vertex.add(new Vertex(panelHalfWidth, panelHalfHeight, 0)));

        Color printedColor = calculateLighting(t);
        int[] area = t.findBoundingRectangle(img.getWidth(null), img.getHeight(null));
        for (int y = area[2]; y <= area[3]; y++) {
            for (int x = area[0]; x <= area[1]; x++) {
                Vertex bary = t.findBarycentric(x, y);
                if (isInBarycentric(bary)) {
                    double depth = bary.getX() * t.getVertex(0).getZ() + bary.getY() * t.getVertex(1).getZ() + bary.getZ() * t.getVertex(2).getZ();
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

        return isSelected;
    }

    /**
     * Rotate all of the meshes around the (0,0,0) point
     *
     * @param rotationMatrix - the rotation matrix
     */
    public void rotateWorld(Matrix rotationMatrix) {
        for (Mesh m : meshes) {
            for (Triangle t : m.getLocalTriangles()) {
                Vertex[] vertices = t.getVertices();
                for(int i = 0; i < vertices.length; i++) {
                    t.setVertex(rotationMatrix.matrixXVertex3X3(vertices[i]),i);
                }
            }
            m.setPosition(rotationMatrix.matrixXVertex3X3(m.getPosition()));
        }
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

}
