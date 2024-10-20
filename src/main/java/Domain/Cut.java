package Domain;

import Domain.DTO.RequestCutDTO;
import Domain.DTO.VertexDTO;
import Domain.ThirdDimension.Vertex;

import java.util.List;
import java.util.UUID;

/**
 * The {@code Cut} class encapsulates the basic attributes of a cuto
 *
 * @author Louis-Etienne Messier
 * @version 0.1
 * @since 2024-09-21
 */
public class Cut {
    private Vertex startPoint;
    private CutType type;
    private List<Vertex> points;
    private int bitIndex;
    private double depth;
    private UUID id;

    /**
     * Constructs a new {@code Cut} with all of it's attributes
     *
     * @param startPoint the initial {@code Point} of the cut
     * @param type       the type of the cut
     * @param points     all the other point that characterise the cut
     * @param bitIndex   the index of the bit that is used for the cut
     * @param depth      the depth of the cut
     */
    public Cut(Vertex startPoint, CutType type, List<Vertex> points, int bitIndex, double depth) {
        this.startPoint = startPoint;
        this.type = type;
        this.points = points;
        this.bitIndex = bitIndex;
        this.depth = depth;
        //todo determine the id
    }

    public Cut(RequestCutDTO requestCutDTO) {
        //todo
    }

    public Vertex getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(Vertex startPoint) {
        this.startPoint = startPoint;
    }

    public List<Vertex> getPoints() {
        return points;
    }

    public void setPoints(List<Vertex> points) {
        this.points = points;
    }

    public int getBitIndex() {
        return bitIndex;
    }

    public void setBitIndex(int bitIndex) {
        this.bitIndex = bitIndex;
    }

    public double getDepth() {
        return depth;
    }

    public void setDepth(double depth) {
        this.depth = depth;
    }


    /**
     * Finds if a coordinate is in the current {@code Cut} zone.
     *
     * @param coordinatesmm The coordinate.
     * @return A boolean indicating if the point is in the Cut's zone.
     */
    boolean pointCollision(VertexDTO coordinatesmm) {
        //todo
        return false;
    }
}
