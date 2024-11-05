package Domain.ThirdDimension;

/**
 * The {@code VertexDTO} class is a read-only {@code Vertex}
 *
 * @author Adam Côté
 * @author Kamran Charles Nayebi
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

    public static VertexDTO zero(){
        return new VertexDTO(0,0,0);
    }

    public double getDistance(){
        return Math.sqrt(Math.pow(x,2)+Math.pow(y,2)+Math.pow(z,2));
    }

    public double getDistance(VertexDTO other){
        return Math.sqrt(Math.pow(x - other.x,2)+Math.pow(y - other.y,2)+Math.pow(z - other.z,2));
    }


    /**
     * @return a formatted string of the 2D components of the vertex
     */
    public String format2D (){
        return "(" + x + "," + y + ")";
    }
}
