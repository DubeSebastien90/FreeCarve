package Domain.DTO;

/**
 * The {@code VertexDTO} class is a read-only {@code Vertex}
 *
 * @author Adam Côté
 * @version 1.0
 * @since 2024-10-20
 */
public class VertexDTO {
    private final float x;
    private final float y;
    private final float z;

    public VertexDTO(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    /**
     * @return a formatted string of the 2D components of the vertex
     */
    public String format2D (){
        return "(" + x + "," + y + ")";
    }
}
