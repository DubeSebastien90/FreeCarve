package Common.DTO;

import Common.Pair;
import Domain.CutType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * This class is a DTO wrapper of the {@code Cut} class in order to transfer READ-ONLY informations
 * @author Louis-Etienne Messier
 * @version 1.0
 * @since 2024-10-12
 */
public class CutDTO {
    private UUID idCut;
    private double depth;
    private int bitIndex;
    private List<VertexDTO> points;
    private CutType type;
    private List<RefCutDTO> refs; // reference to another cut

    /**
     * Basic constructor of the {@code CutDTO}
     * @param idCut id of the cut
     * @param depth depth of the cut
     * @param bitIndex index of the bit used to make the cut
     * @param type type of the cut {@code CutType}
     */
    public CutDTO(UUID idCut, double depth, int bitIndex, CutType type, List<VertexDTO> points, List<RefCutDTO> refs){
        this.idCut = idCut;
        this.depth = depth;
        this.bitIndex = bitIndex;
        this.type = type;
        this.points = points;
        this.refs = refs;
    }

    public CutDTO(UUID uuid, RequestCutDTO requestCutDTO){
        this.idCut = uuid;
        this.depth = requestCutDTO.getDepth();
        this.bitIndex = requestCutDTO.getBitLocation();
        this.type = requestCutDTO.getType();
        this.points = requestCutDTO.getPoints();
        this.refs = requestCutDTO.getRefs();
    }

    public CutDTO(CutDTO other){
        this.idCut = other.idCut;
        this.depth = other.depth;
        this.bitIndex = other.bitIndex;
        this.type = other.type;
        this.points = new ArrayList<>();
        for(VertexDTO p : other.getPoints()){
            this.points.add(new VertexDTO(p));
        }

        refs = new ArrayList<>();
        for(RefCutDTO ref : other.getRefsDTO()){
            this.refs.add(new RefCutDTO(ref));
        }
    }

    public int getBitIndex() {
        return this.bitIndex;
    }

    public double getDepth(){
        return this.depth;
    }

    public UUID getId(){
        return this.idCut;
    }

    public CutType getCutType(){
        return this.type;
    }

    public List<VertexDTO> getPoints() {return this.points;}

    public List<RefCutDTO> getRefsDTO() {
        return this.refs;
    }

    public CutDTO addOffsetToPoints(VertexDTO offset){
        List<VertexDTO> newPoints = new ArrayList<>();
        for(VertexDTO point : this.points){
            newPoints.add(new VertexDTO(point.getX() + offset.getX(),
                    point.getY() + offset.getY(), point.getZ() + offset.getZ()));
        }
        return new CutDTO(this.idCut, this.depth, this.bitIndex, this.type, newPoints, refs);
    }

    public List<VertexDTO> getCopyPoints(){
        ArrayList<VertexDTO> copyList = new ArrayList<>();
        for(VertexDTO point : points){
            copyList.add(new VertexDTO(point));
        }
        return copyList;
    }

    public List<VertexDTO> getCopyPointsWithOffset(VertexDTO offset){
        ArrayList<VertexDTO> copyList = new ArrayList<>();
        for(VertexDTO point : points){
            copyList.add(new VertexDTO(point).add(offset));
        }
        return copyList;
    }

    /**
     * Get the copied absolute points of the cut, based on it's references
     * @return List<VertexDTO> of the copied absolute points
     */
    public List<VertexDTO> getAbsolutePointsPosition() {
        // 3 possibilies :
        // 1 : CutType = Rectangular, or Line_Vertical or Line_Horizontal or Free_Line : get first ref and use it as anchor point
        // 2 : CutType = L : get 2 ref points and use them as position of the L cut
        // 3 : ref list is empty : just return the points
        if (refs.isEmpty()){
            return this.getCopyPoints();
        }
        if (type == CutType.LINE_HORIZONTAL || type==CutType.LINE_VERTICAL || type==CutType.RECTANGULAR || type== CutType.LINE_FREE){
            return  this.getCopyPointsWithOffset(refs.getFirst().getAbsoluteOffset());
        }
        if(type == CutType.L_SHAPE){
            if(refs.size() < 2){throw new AssertionError(type + " needs two refs, it has " + refs.size());}

            ArrayList<VertexDTO> outputPoints = new ArrayList<>();

            // Needs to calculate the absolute two points
            VertexDTO p1a = refs.getFirst().getAbsoluteOffset();
            VertexDTO p1b = refs.getFirst().getAbsoluteFirstPoint();
            if(p1b.getDistance(p1a) < VertexDTO.doubleTolerance){
                p1b = refs.getFirst().getAbsoluteSecondPoint();// Changing the other ref point to prevent accidental colinearity
            }

            // Get the first absolute reference point
            VertexDTO p2a = refs.get(1).getAbsoluteOffset();
            VertexDTO p2b = refs.get(1).getAbsoluteFirstPoint();
            if(p2b.getDistance(p2a) < VertexDTO.doubleTolerance){
                p2b = refs.get(1).getAbsoluteSecondPoint();// Changing the other ref point to prevent accidental colinearity
            }

            // Needs to find the absolute corner point of the L-cut
            // 1. Find the perpendicular lines of the two refs
            // 2. Find the intersection of those 2 slopes

            Pair<VertexDTO, VertexDTO> paPerpendicular = VertexDTO.perpendicularPointsAroundP1(p1a, p1b);
            Pair<VertexDTO, VertexDTO> pbPerpendicular = VertexDTO.perpendicularPointsAroundP1(p2a, p2b);

            Optional<VertexDTO> intersectionPoint = VertexDTO.isLineIntersectNoLimitation(paPerpendicular.getFirst(),
                    paPerpendicular.getSecond(), pbPerpendicular.getFirst(), pbPerpendicular.getSecond());

            if(intersectionPoint.isEmpty()) {
                VertexDTO midpoint1 = p1a.add(p2a).mul(0.5);
                outputPoints.add(p1a);
                outputPoints.add(midpoint1);
                outputPoints.add(p2a);
            }
            else{
                outputPoints.add(p1a);
                outputPoints.add(intersectionPoint.get());
                outputPoints.add(p2a);
            }

            return outputPoints;
        }
        else{
            throw new NullPointerException("Invalid cuttype");
        }
    }
}
