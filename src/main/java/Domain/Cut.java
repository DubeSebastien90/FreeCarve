package Domain;

import Common.DTO.CutDTO;
import Common.DTO.RefCutDTO;
import Common.DTO.RequestCutDTO;
import Common.DTO.VertexDTO;
import Common.Pair;

import java.sql.Ref;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * The {@code Cut} class encapsulates the basic attributes of a cuto
 *
 * @author Louis-Etienne Messier
 * @version 0.1
 * @since 2024-09-21
 */
class Cut {
    private VertexDTO startPoint;
    private CutType type;
    private List<VertexDTO> points; // IMPORTANT : For rectangular cuts, there is always 5 points, i.e 2 times the first point to comeback to the original place
    private int bitIndex;
    private double depth;
    private UUID id;
    private boolean valid;
    private List<RefCut> refs;


    public Cut(CutDTO uiCut, List<Cut> cutAndBorderList) {
        this.startPoint = uiCut.getPoints().getFirst();
        this.type = uiCut.getCutType();
        this.points = uiCut.getPoints();
        this.bitIndex = uiCut.getBitIndex();
        this.depth = uiCut.getDepth();
        this.id = uiCut.getId();

        refs = new ArrayList<>();
        for(RefCutDTO ref : uiCut.getRefsDTO()){
            refs.add(new RefCut(ref, cutAndBorderList));
        }

    }

    public void modifyCut(CutDTO uiCut, List<Cut> cutAndBorderList){
        this.startPoint = uiCut.getPoints().getFirst();
        this.type = uiCut.getCutType();
        this.points = uiCut.getPoints();
        this.bitIndex = uiCut.getBitIndex();
        this.depth = uiCut.getDepth();
        this.id = uiCut.getId();

        refs = new ArrayList<>();
        for(RefCutDTO ref : uiCut.getRefsDTO()){
            refs.add(new RefCut(ref, cutAndBorderList));
        }
    }


    /**
     * Constructs a new {@code Cut} with all of it's attributes, set the ref to null
     *
     * @param startPoint the initial {@code Point} of the cut
     * @param type       the type of the cut
     * @param points     all the other point that characterise the cut
     * @param bitIndex   the index of the bit that is used for the cut
     * @param depth      the depth of the cut
     */
    public Cut(VertexDTO startPoint, CutType type, List<VertexDTO> points, int bitIndex, double depth) {
        this.startPoint = startPoint;
        this.type = type;
        this.points = points;
        this.bitIndex = bitIndex;
        this.depth = depth;
        this.id = UUID.randomUUID();
        this.refs = new ArrayList<>();
    }

    /**
     * Constructs a new {@code Cut} with all of it's attributes
     *
     * @param startPoint the initial {@code Point} of the cut
     * @param type       the type of the cut
     * @param points     all the other point that characterise the cut
     * @param bitIndex   the index of the bit that is used for the cut
     * @param depth      the depth of the cut
     * @param refCut        reference to the anchor point of the cut
     */
    public Cut(VertexDTO startPoint, CutType type, List<VertexDTO> points, int bitIndex, double depth, ArrayList<RefCut> refCut) {
        this.startPoint = startPoint;
        this.type = type;

        this.points = new ArrayList<>();
        for (VertexDTO point : points){
            this.points.add(new VertexDTO(point));
        }

        this.bitIndex = bitIndex;
        this.depth = depth;
        this.id = UUID.randomUUID();
        this.refs = refCut;
    }

    public Cut(RequestCutDTO requestCutDTO) {
        //todo
    }

    public CutDTO getDTO() {
        return new CutDTO(id, depth, bitIndex, type, points.stream().toList(), refs.stream().map(RefCut::getDTO).collect(Collectors.toList()));
    }

    public VertexDTO getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(VertexDTO startPoint) {
        this.startPoint = startPoint;
    }

    public List<VertexDTO> getPoints() {
        return points;
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

    public void setPoints(List<VertexDTO> points) {
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

    public UUID getId() {
        return this.id;
    }

    public CutType getType() {
        return this.type;
    }

    public List<RefCut> getRefs() {return this.refs;}

    public void setRefs(List<RefCut> refs){
        this.refs = refs;
    }

    public static List<VertexDTO> generateRectanglePoints(VertexDTO anchor, double width, double height){
        VertexDTO p1 = new VertexDTO(anchor);
        VertexDTO p2 = new VertexDTO(anchor.getX() + width, anchor.getY(), anchor.getZ());
        VertexDTO p3 = new VertexDTO(anchor.getX() + width, anchor.getY()  + height, anchor.getZ());
        VertexDTO p4 = new VertexDTO(anchor.getX(), anchor.getY()  + height, anchor.getZ());
        VertexDTO p5 = new VertexDTO(anchor);
        return new ArrayList<>(List.of(p1,p2,p3,p4,p5));
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
