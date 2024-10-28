package Domain;

import Domain.ThirdDimension.VertexDTO;
import Domain.ThirdDimension.Vertex;

import java.util.ArrayList;
import java.util.UUID;

/**
 * The {@code Cut} class encapsulates the basic attributes of a cuto
 *
 * @author Louis-Etienne Messier
 * @version 0.1
 * @since 2024-09-21
 */
class Cut {
    private Vertex startPoint;
    private CutType type;
    private ArrayList<Vertex> points;
    private int bitIndex;
    private double depth;
    private UUID id;


    public Cut(CutDTO uiCut){
        this.startPoint = new Vertex(uiCut.getPoints().getFirst());
        this.type = uiCut.getCutType();
        this.points = new ArrayList<>();
        for(VertexDTO domainVertex : uiCut.getPoints()){
            points.add(new Vertex(domainVertex));
        }
        this.bitIndex = uiCut.getBitIndex();
        this.depth = uiCut.getDepth();
        this.id = uiCut.getId();
    }
    /**
     * Constructs a new {@code Cut} with all of it's attributes
     *
     * @param startPoint the initial {@code Point} of the cut
     * @param type       the type of the cut
     * @param points     all the other point that characterise the cut
     * @param bitIndex   the index of the bit that is used for the cut
     * @param depth      the depth of the cut
     */
    public Cut(Vertex startPoint, CutType type, ArrayList<Vertex> points, int bitIndex, double depth) {
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

    public ArrayList<Vertex> getPoints() {
        return points;
    }

    public void setPoints(ArrayList<Vertex> points) {
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

    public UUID getId(){return this.id;}

    public CutType getType() {return this.type;}

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
