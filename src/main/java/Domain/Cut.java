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

    private CutType type;
    private List<VertexDTO> points; // IMPORTANT : For rectangular cuts, there is always 5 points, i.e 2 times the first point to comeback to the original place
    private int bitIndex;
    private double depth;
    private UUID id;
    private boolean valid;
    private List<RefCut> refs;


    public Cut(CutDTO uiCut, List<Cut> cutAndBorderList) {
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

    public static List<VertexDTO> getAbsolutePointsPositionOfCutDTO(CutDTO cutDTO, PanelCNC panelCNC){
        Cut c = panelCNC.createPanelCut(cutDTO);
        return c.getAbsolutePointsPosition();
    }

    /**
     * Returns a valid bordercut point, based on a margin
     * @param margin
     * @return
     */
    public static VertexDTO getBorderPointCut(double margin){
        return new VertexDTO(margin, 0, 0);// The border margin is stored in the X axis
    }

    public static VertexDTO getBorderPointCutDefaultMargins(){
        double defaultMargins = 50;
        return new VertexDTO(defaultMargins, 0, 0); // The border margin is stored in the X axis
    }

    /**
     * Get the copied absolute points of the cut, based on it's references
     * @return List<VertexDTO> of the copied absolute points
     */
    public List<VertexDTO> getAbsolutePointsPosition() {
        // 5 possibilies :

        // 1 : ref list is empty : just return the points

        // 2 : CutType = RETAILLER :    -number of refs : >=1
        //                              -number of relative points : 2 - twice the same, they are empty except for the margin contained in the X axis
        //                              -how absolute points are computed : based on it's single reference point (the first border ref), returns the same border cut but shifted by the margin of the relative point

        // 3 : CutType = Line_Vertical or Line_Horizontal or Free_Line :
        //                              -number of refs : >=1
        //                              -number of relative points : 2
        //                              -how absolute points are computed : based on it's first reference point, returns the shifted relative points by the ref offset

        // 4 : CutType = L :            -number of refs : >=2
        //                              -number of relative points : 1
        //                              -how absolute points are computed : gets the intersection points of it's first two references, and offset the relative point by this offset. Then the first, third points are found by looking for the intersection of the rectangular lines to the ref

        // 5 : CutType = Rectangular ;  -number of refs : >=2
        //                              -number of relative points : 5 - twice the anchor point at position 0 and 4 + 3 other points of rect
        //                              -how absolute points are computed : gets the intersection points of it's first two references, and offset the relative point by this offset.

        if (refs.isEmpty()){
            return this.getCopyPoints();
        }
        if(type == CutType.RETAILLER){
            for(int i =0 ; i < refs.size(); i++){ // There could be references that are not the border of the pannel
                if(refs.get(i).getCut().getBitIndex() == -1){ // This is indeed a border reference if bitIndex == -1
                    ArrayList<VertexDTO> outputList = new ArrayList<>();
                    RefCut ref = refs.getFirst();

                    VertexDTO ap1 = ref.getAbsoluteFirstPoint();
                    VertexDTO ap2 = ref.getAbsoluteSecondPoint();

                    VertexDTO v = ap2.sub(ap1);
                    Pair<VertexDTO, VertexDTO> rotated = VertexDTO.perpendicularPointsAroundP1(VertexDTO.zero(), v);
                    VertexDTO diff = rotated.getSecond().normalize().mul(-points.get(i).getX());
                    VertexDTO p1 = ap1.add(diff);
                    VertexDTO p2 = ap2.add(diff);
                    outputList.add(p1);
                    outputList.add(p2);
                    return outputList;
                }
            }
            return getCopyPoints();
        }
        if (type == CutType.LINE_HORIZONTAL || type==CutType.LINE_VERTICAL || type== CutType.LINE_FREE){
            return  this.getCopyPointsWithOffset(refs.getFirst().getAbsoluteOffset());
        }
        if(type == CutType.RECTANGULAR){



            if(refs.size() < 2){throw new AssertionError(type + " needs two refs, it has " + refs.size());}

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

            Optional<VertexDTO> intersectionPoint = VertexDTO.isLineIntersectNoLimitation(p1a,
                    p1b, p2a, p2b);

            if(intersectionPoint.isPresent()){
                return  this.getCopyPointsWithOffset(intersectionPoint.get());
            }
            else{
                return this.getCopyPoints();
            }

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

            Optional<VertexDTO> intersectionPoint = VertexDTO.isLineIntersectNoLimitation(p1a,
                    p1b, p2a, p2b);


            if(intersectionPoint.isPresent()){

                VertexDTO anchor = intersectionPoint.get();
                VertexDTO corner = anchor.add(points.getFirst());

                VertexDTO vToAnchor = anchor.sub(corner);

                VertexDTO firstLineLineIntersect = new VertexDTO(vToAnchor.getX(), 0 ,0);
                firstLineLineIntersect = firstLineLineIntersect.add(corner);
                VertexDTO secondLineLineIntersect = new VertexDTO(0, vToAnchor.getY(), 0);
                secondLineLineIntersect = secondLineLineIntersect.add(corner);

                Optional<VertexDTO> intersection1 = VertexDTO.isLineIntersectNoLimitation(corner, firstLineLineIntersect, p1a, p1b);
                Optional<VertexDTO> intersection2 = VertexDTO.isLineIntersectNoLimitation(corner, secondLineLineIntersect, p2a, p2b);

                if(intersection1.isPresent() && intersection2.isPresent()){
                    outputPoints.add(intersection1.get());
                    outputPoints.add(corner);
                    outputPoints.add(intersection2.get());
                }
                else{
                    valid = false;
                    outputPoints.add(firstLineLineIntersect);
                    outputPoints.add(corner);
                    outputPoints.add(secondLineLineIntersect);
                }
            }
            else{
                VertexDTO midpoint1 = p1a.add(p2a).mul(0.5);
                outputPoints.add(p1a);
                outputPoints.add(midpoint1);
                outputPoints.add(p2a);
            }

            return outputPoints;
        }
        else{
            throw new NullPointerException("Invalid cuttype");
        }
    }
}
