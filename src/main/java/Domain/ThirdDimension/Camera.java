package Domain.ThirdDimension;

import Common.DTO.VertexDTO;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

/**
 * The {@code Renderer} class provides function to render meshes on it, it extends {@code JPanel}.
 *
 * @author Adam Côté
 * @author Sébastien Dubé
 * @author Kamran Charles Nayebi
 * @since 2024-09-08
 */
public class Camera extends Transform {
    private Scene scene;;
    Double[][] pixelsDepthMap;
    public static final float MIN_LIGHTING = 0.2f;

    /**
     * Constructs a new {@code Camera} object with a {@code List} of {@code Mesh} and initialize {@code Renderer}
     *
     * @param scene the {@code Scene} that the camera is observing
     */
    public Camera(Scene scene) {
        super(Vertex.zero(), 1, Vertex.zero());
        setScene(scene);
    }

    public Optional<UUID> renderImage(BufferedImage img, VertexDTO mousePosition) {
        pixelsDepthMap = new Double[img.getWidth()][img.getHeight()];
        Optional<UUID> cutId = Optional.empty();
        for (Mesh m : scene.getMeshes()) {
            for (Triangle t : m.getLocalTriangles()) {
                Triangle transformed = getTransformedTriangle(m.getTransformedTriangle(t));
                Arrays.stream(transformed.getVertices()).forEach(vertex -> vertex.add(new Vertex(img.getWidth()/2.0, img.getHeight()/2.0, 0)));
                if (renderTriangle(img, transformed, mousePosition)) {
                    cutId = Optional.of(m.getId());
                }
            }
        }
        return cutId;
    }

    /**
     * Renders a {@code Triangle} object onto a {@code BufferedImage}.
     * The function uses a depth map for to be sure the triangles are displayed correctly,
     * barycentric coordinates to paint the pixels of the triangle and normal vectors to implement base shading.
     * <br/><br/>
     *
     * @param img the {@code Image} object associated with the panel
     * @param t the {@code Triangle} to paint
     * @param mousePosition the position of a mouse click to select a mesh
     * @return true if the triangle has been clicked on
     */
    private boolean renderTriangle(BufferedImage img, Triangle t, VertexDTO mousePosition) {
        boolean isSelected = false;

        Color printedColor = calculateLighting(t);
        int[] area = t.findBoundingRectangle(img.getWidth(), img.getHeight());
        for (int y = area[2]; y <= area[3]; y++) {
            for (int x = area[0]; x <= area[1]; x++) {
                Vertex bary = t.findBarycentric(x, y);
                if (isInBarycentric(bary)) {
                    double depth = bary.getX() * t.getVertex(0).getZ() + bary.getY() * t.getVertex(1).getZ() + bary.getZ() * t.getVertex(2).getZ();
                    if (x < img.getWidth() && y < img.getHeight()) {
                        if ((pixelsDepthMap[x][y] == null || pixelsDepthMap[x][y] < depth)) {
                            pixelsDepthMap[x][y] = depth;
                            img.setRGB(x, img.getHeight()-y-1, printedColor.getRGB());
                            if (mousePosition.getX() == x && mousePosition.getY() == img.getHeight()-y-1) {
                                isSelected = true;
                            }
                        }
                    }
                }
            }
        }

        return isSelected;
    }

    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
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
        Vertex lightDirection = new Vertex(0, 0, 1);
        double darker = (lightDirection.getX() * normal.getX() + lightDirection.getY() * normal.getY() + lightDirection.getZ() * normal.getZ()) / lightDirection.length();
        if (darker < MIN_LIGHTING){
            darker = MIN_LIGHTING;
        }
        float[] component = color.getRGBColorComponents(null);
        try {
            return new Color((float)(component[0] * darker), (float)(component[1] * darker), (float)(component[2] * darker));
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
