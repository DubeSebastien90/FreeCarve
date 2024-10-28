package Domain;

import Domain.ThirdDimension.TriangleDTO;

import java.awt.*;
import java.util.List;

/**
 * The {@code MeshDTO} class is a read-only {@code Mesh}
 *
 * @author Adam Côté
 * @version 1.0
 * @since 2024-10-20
 */
class MeshDTO {
    private final List<TriangleDTO> triangles;
    private Color color = Color.GRAY;

    public MeshDTO(List<TriangleDTO> triangles, Color color) {
        this.triangles = triangles;
        this.color = color;
    }

    public MeshDTO(List<TriangleDTO> triangles) {
        this.triangles = triangles;
    }

    public List<TriangleDTO> getTriangles() {
        return triangles;
    }

    public Color getColor() {
        return color;
    }
}
