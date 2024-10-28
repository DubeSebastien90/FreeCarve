package Domain.ThirdDimension;

/**
 * The {@code TriangleDTO} class is a read-only {@code Triangle}
 *
 * @author Adam Côté
 * @version 1.0
 * @since 2024-10-20
 */
public class TriangleDTO {
    private final VertexDTO vertex1;
    private final VertexDTO vertex2;
    private final VertexDTO vertex3;
    private final VertexDTO normal;

    public TriangleDTO(VertexDTO vertex1, VertexDTO vertex2, VertexDTO vertex3, VertexDTO normal) {
        this.vertex1 = vertex1;
        this.vertex2 = vertex2;
        this.vertex3 = vertex3;
        this.normal = normal;
    }

    public VertexDTO getVertex1() {
        return vertex1;
    }

    public VertexDTO getVertex2() {
        return vertex2;
    }

    public VertexDTO getVertex3() {
        return vertex3;
    }

    public VertexDTO getNormal() {
        return normal;
    }
}
