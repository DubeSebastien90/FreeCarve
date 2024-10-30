package Domain.ThirdDimension;

/**
 * The {@code VertexDTO} class is a read-only {@code Vertex}
 *
 * @author Adam Côté
 * @version 1.0
 * @since 2024-10-20
 */
public class VertexDTO {
    private final double x;
    private final double y;
    private final double z;

    public VertexDTO(Vertex vDomain){
        this.x = vDomain.getX();
        this.y = vDomain.getY();
        this.z = vDomain.getZ();
    }
    public VertexDTO(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }


    /**
     * @return a formatted string of the 2D components of the vertex
     */
    public String format2D (){
        return "(" + x + "," + y + ")";
    }
}
