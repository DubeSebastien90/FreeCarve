package Domain;

import Common.CutState;
import Common.DTO.CutDTO;
import Common.DTO.RefCutDTO;
import Common.DTO.RequestCutDTO;
import Common.DTO.VertexDTO;
import Common.Pair;

import java.util.*;
import java.sql.Ref;
import java.util.*;
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
    private List<RefCut> refs;
    private CutState cutState;



    public Cut(CutDTO uiCut, List<Cut> cutAndBorderList) {
        this.type = uiCut.getCutType();
        this.points = uiCut.getPoints();
        this.bitIndex = uiCut.getBitIndex();
        this.depth = uiCut.getDepth();
        this.id = uiCut.getId();
        this.cutState = uiCut.getState();

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
        this.cutState = uiCut.getState();

        refs = new ArrayList<>();
        for(RefCutDTO ref : uiCut.getRefsDTO()){
            refs.add(new RefCut(ref, cutAndBorderList));
        }
    }


    /**
     * Constructs a new {@code Cut} with all of it's attributes, set the ref to null
     *
     * @param type     the type of the cut
     * @param points   all the other point that characterise the cut
     * @param bitIndex the index of the bit that is used for the cut
     * @param depth    the depth of the cut
     */
    public Cut(CutType type, List<VertexDTO> points, int bitIndex, double depth) {
        this.type = type;
        this.points = points;
        this.bitIndex = bitIndex;
        this.depth = depth;
        this.id = UUID.randomUUID();
        this.refs = new ArrayList<>();
        this.cutState = CutState.VALID;
    }

    /**
     * Constructs a new {@code Cut} with all of it's attributes
     *
     * @param type     the type of the cut
     * @param points   all the other point that characterise the cut
     * @param bitIndex the index of the bit that is used for the cut
     * @param depth    the depth of the cut
     * @param refCut   reference to the anchor point of the cut
     */
    public Cut(CutType type, List<VertexDTO> points, int bitIndex, double depth, ArrayList<RefCut> refCut) {
        this.type = type;

        this.points = new ArrayList<>();
        for (VertexDTO point : points){
            this.points.add(new VertexDTO(point));
        }

        this.bitIndex = bitIndex;
        this.depth = depth;
        this.id = UUID.randomUUID();
        this.refs = refCut;
        this.cutState = CutState.VALID;
    }

    public Cut(RequestCutDTO requestCutDTO) {
        //todo
    }

    public CutDTO getDTO() {
        return new CutDTO(id, depth, bitIndex, type, points.stream().toList(), refs.stream().map(RefCut::getDTO).collect(Collectors.toList()), cutState);
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

    public void setInvalidAndNoRef(CNCMachine cncMachine){
        this.points = getAbsolutePointsPosition(cncMachine);
        this.refs = new ArrayList<>();
        this.cutState = CutState.NOT_VALID;
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

    public CutState getCutState(){return this.cutState;}

    public void setCutState(CutState state) {
        this.cutState = state;
    }

    public static List<VertexDTO> generateRectanglePoints(VertexDTO centerAnchor, double width, double height){
        if (width < 0) {throw new IllegalArgumentException("Width of rectangle points is negative, should be positive");}
        if (height < 0) {throw new IllegalArgumentException("Height of rectangle points is negative, should be positive");}
        VertexDTO p1 = new VertexDTO(centerAnchor.getX() - width/2, centerAnchor.getY() - height/2, centerAnchor.getZ());
        VertexDTO p2 = new VertexDTO(centerAnchor.getX() - width/2, centerAnchor.getY()  + height/2, centerAnchor.getZ());
        VertexDTO p3 = new VertexDTO(centerAnchor.getX() + width/2, centerAnchor.getY()  + height/2, centerAnchor.getZ());
        VertexDTO p4 = new VertexDTO(centerAnchor.getX() + width/2, centerAnchor.getY()  - height/2, centerAnchor.getZ());
        VertexDTO p5 = new VertexDTO(p1);
        return new ArrayList<>(List.of(p1,p2,p3,p4,p5));
    }

    public static List<VertexDTO> getAbsolutePointsPositionOfCutDTO(CutDTO cutDTO, CNCMachine cncMachine){
        Cut c = cncMachine.getPanel().createPanelCut(cutDTO);
        return c.getAbsolutePointsPosition(cncMachine);
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

    public static List<VertexDTO> generateHorizontalPointsRelativeEdgeEdgeFromAbsolute(VertexDTO p1Abs, VertexDTO p2Abs, int bitIndex, List<RefCutDTO> refs, Controller controller, CNCMachine cncMachine){
        int bit1Index = bitIndex;
        int bit2Index = refs.getFirst().getCut().getBitIndex();
        VertexDTO anchor = refs.getFirst().getAbsoluteOffset(controller);
        double diameter1 = cncMachine.getBitStorage().getBitDiameter(bit1Index);
        double diameter2 = cncMachine.getBitStorage().getBitDiameter(bit2Index);
        if(p1Abs.getY() - anchor.getY() != 0){
            VertexDTO directionAnchor = (new VertexDTO(0, p1Abs.getY() - anchor.getY(), 0)).normalize();
            anchor = anchor.add(directionAnchor.mul(diameter1/2));
            anchor = anchor.add(directionAnchor.mul(diameter2/2));
        }

        List<VertexDTO> output = new ArrayList<>();
        output.add(p1Abs.sub(anchor));
        output.add(p2Abs.sub(anchor));
        return output;
    }

    public static List<VertexDTO> generateVerticalPointsRelativeEdgeEdgeFromAbsolute(VertexDTO p1Abs, VertexDTO p2Abs, int bitIndex, List<RefCutDTO> refs, Controller controller, CNCMachine cncMachine){
        int bit1Index = bitIndex;
        int bit2Index = refs.getFirst().getCut().getBitIndex();
        VertexDTO anchor = refs.getFirst().getAbsoluteOffset(controller);
        double diameter1 = cncMachine.getBitStorage().getBitDiameter(bit1Index);
        double diameter2 = cncMachine.getBitStorage().getBitDiameter(bit2Index);
        if(p1Abs.getX() - anchor.getX() != 0){
            VertexDTO directionAnchor = (new VertexDTO(p1Abs.getX() - anchor.getX(), 0, 0)).normalize();
            anchor = anchor.add(directionAnchor.mul(diameter1/2));
            anchor = anchor.add(directionAnchor.mul(diameter2/2));
        }

        List<VertexDTO> output = new ArrayList<>();
        output.add(p1Abs.sub(anchor));
        output.add(p2Abs.sub(anchor));
        return output;
    }

    public static List<VertexDTO> generateLPointsRelativeEdgeEdgeFromAbsolute(VertexDTO p1Abs, int bitIndex, List<RefCutDTO> refs, Controller controller, CNCMachine cncMachine){
        // Compute the diameters of the bits used
        int bitIndexRef1 = refs.getFirst().getCut().getBitIndex();
        int bitIndexRef2 = refs.get(1).getCut().getBitIndex();
        int bitIndexL = bitIndex;
        double diameterRef1 = cncMachine.getBitStorage().getBitDiameter(bitIndexRef1);
        double diameterRef2 = cncMachine.getBitStorage().getBitDiameter(bitIndexRef2);
        double diameterL = cncMachine.getBitStorage().getBitDiameter(bitIndexL);

        // Needs to calculate the absolute two points
        VertexDTO p1a = refs.getFirst().getAbsoluteOffset(controller);
        VertexDTO p1b = refs.getFirst().getAbsoluteFirstPoint(controller);
        if(p1b.getDistance(p1a) < VertexDTO.doubleTolerance){
            p1b = refs.getFirst().getAbsoluteSecondPoint(controller);// Changing the other ref point to prevent accidental colinearity
        }

        // Get the first absolute reference point
        VertexDTO p2a = refs.get(1).getAbsoluteOffset(controller);
        VertexDTO p2b = refs.get(1).getAbsoluteFirstPoint(controller);
        if(p2b.getDistance(p2a) < VertexDTO.doubleTolerance){
            p2b = refs.get(1).getAbsoluteSecondPoint(controller);// Changing the other ref point to prevent accidental colinearity
        }

        VertexDTO dirX = VertexDTO.zero();
        VertexDTO dirY = VertexDTO.zero();
        if(p1Abs.getX() - p1a.getX() != 0){
            dirX = new VertexDTO(p1Abs.getX() - p1a.getX(), 0, 0).normalize();
        }
        if(p1Abs.getY() - p2a.getY() != 0){
            dirY = new VertexDTO(0, p1Abs.getY() - p2a.getY(), 0).normalize();
        }

        // Ajout des diametres pour faire la compensation
        p1a = p1a.add(dirX.mul(diameterRef1/2));
        p1b = p1b.add(dirX.mul(diameterRef1/2));

        p2a = p2a.add(dirY.mul(diameterRef2/2));
        p2b = p2b.add(dirY.mul(diameterRef2/2));

        Optional<VertexDTO> intersectionPoint = VertexDTO.isLineIntersectNoLimitation(p1a,
                p1b, p2a, p2b);

        if(intersectionPoint.isPresent()){
            ArrayList<VertexDTO> outputs = new ArrayList<>();
            VertexDTO relativeEdgeEdge = p1Abs.sub(intersectionPoint.get());
            relativeEdgeEdge = relativeEdgeEdge.sub(dirX.mul(diameterL/2));
            relativeEdgeEdge = relativeEdgeEdge.sub(dirY.mul(diameterL/2));
            outputs.add(relativeEdgeEdge);
            return outputs;
        }
        else{
            throw new IllegalArgumentException("Cut arguments invalid");
        }
    }

    public static List<VertexDTO> generateRectangleRelativeEdgeEdgeFromAbsolute(VertexDTO p1Abs, VertexDTO p3Abs, int bitIndex, List<RefCutDTO> refs, Controller controller, CNCMachine cncMachine){
        // Compute the diameters of the bits used
        int bitIndexRef1 = refs.getFirst().getCut().getBitIndex();
        int bitIndexRef2 = refs.get(1).getCut().getBitIndex();
        int bitIndexL = bitIndex;
        double diameterRef1 = cncMachine.getBitStorage().getBitDiameter(bitIndexRef1);
        double diameterRef2 = cncMachine.getBitStorage().getBitDiameter(bitIndexRef2);
        double diameterL = cncMachine.getBitStorage().getBitDiameter(bitIndexL);

        // Needs to calculate the absolute two points
        VertexDTO p1a = refs.getFirst().getAbsoluteOffset(controller);
        VertexDTO p1b = refs.getFirst().getAbsoluteFirstPoint(controller);
        if(p1b.getDistance(p1a) < VertexDTO.doubleTolerance){
            p1b = refs.getFirst().getAbsoluteSecondPoint(controller);// Changing the other ref point to prevent accidental colinearity
        }

        // Get the first absolute reference point
        VertexDTO p2a = refs.get(1).getAbsoluteOffset(controller);
        VertexDTO p2b = refs.get(1).getAbsoluteFirstPoint(controller);
        if(p2b.getDistance(p2a) < VertexDTO.doubleTolerance){
            p2b = refs.get(1).getAbsoluteSecondPoint(controller);// Changing the other ref point to prevent accidental colinearity
        }

        VertexDTO dirX = VertexDTO.zero();
        VertexDTO dirY = VertexDTO.zero();
        if(p1Abs.getX() - p1a.getX() != 0){
            dirX = new VertexDTO(p1Abs.getX() - p1a.getX(), 0, 0).normalize();
        }
        if(p1Abs.getY() - p2a.getY() != 0){
            dirY = new VertexDTO(0, p1Abs.getY() - p2a.getY(), 0).normalize();
        }

        // Ajout des diametres pour faire la compensation
        p1a = p1a.add(dirX.mul(diameterRef1/2));
        p1b = p1b.add(dirX.mul(diameterRef1/2));

        p2a = p2a.add(dirY.mul(diameterRef2/2));
        p2b = p2b.add(dirY.mul(diameterRef2/2));

        Optional<VertexDTO> intersectionPoint = VertexDTO.isLineIntersectNoLimitation(p1a,
                p1b, p2a, p2b);

        if(intersectionPoint.isPresent()){
            VertexDTO p1 = p1Abs.sub(intersectionPoint.get());
            VertexDTO p3 = p3Abs.sub(intersectionPoint.get());
            VertexDTO diagonal = p3.sub(p1);
            double edgeEdgeWidth = Math.abs(diagonal.getX()) - diameterL;
            double edgeEdgeHeight = Math.abs(diagonal.getY()) - diameterL;
            VertexDTO center = p1.add(diagonal.mul(0.5));
            return  generateRectanglePoints(center, edgeEdgeWidth, edgeEdgeHeight);
        }
        else{
            throw new IllegalArgumentException("Cut arguments invalid");
        }
    }
//
//    public static List<VertexDTO> generateFreeRelativeEdgeEdgeFromAbsolute(){
//
//    }

    /**
     * Get the copied absolute points of the cut, based on it's references
     * @return List<VertexDTO> of the copied absolute points
     */
    public List<VertexDTO> getAbsolutePointsPosition(CNCMachine cncMachine) {
        // 5 possibilies :

        // 1 : ref list is empty : just return the points

        // 2 : CutType = RETAILLER :    -number of refs : >=1
        //                              -number of relative points : 2 - twice the same, they are empty except for the margin contained in the X axis
        //                              -how absolute points are computed : based on it's single reference point (the first border ref), returns the same border cut but shifted by the margin of the relative point

        // 3 : CutType = Line_Vertical or Line_Horizontal or Free_Line :
        //                              -number of refs : >=1
        //                              -number of relative points : 2, the points stored are EdgeEdge
        //                              -how absolute points are computed : based on it's first reference point, returns the shifted relative points by the ref offset

        // 4 : CutType = L :            -number of refs : >=2
        //                              -number of relative points : 1
        //                              -how absolute points are computed : gets the intersection points of it's first two references, and offset the relative point by this offset. Then the first, third points are found by looking for the intersection of the rectangular lines to the ref

        // 5 : CutType = Rectangular ;  -number of refs : >=2
        //                              -number of relative points : 5 - twice the anchor point at position 0 and 4 + 3 other points of rect

        //                              -how absolute points are computed : gets the intersection points of it's first two references, and offset the relative point by this offset.
                                        //

        if (refs.isEmpty()){
            return this.getCopyPoints();
        }
        if(type == CutType.RETAILLER){
            for(int i =0 ; i < refs.size(); i++){ // There could be references that are not the border of the pannel
                if(refs.get(i).getCut().getBitIndex() == -1){ // This is indeed a border reference if bitIndex == -1
                    ArrayList<VertexDTO> outputList = new ArrayList<>();
                    RefCut ref = refs.getFirst();

                    VertexDTO ap1 = ref.getAbsoluteFirstPoint(cncMachine);
                    VertexDTO ap2 = ref.getAbsoluteSecondPoint(cncMachine);

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
        if(type == CutType.LINE_FREE){
            return  this.getCopyPointsWithOffset(refs.getFirst().getAbsoluteOffset(cncMachine));
        }
        if(type == CutType.LINE_VERTICAL){
            int bit1Index = this.bitIndex;
            int bit2Index = refs.getFirst().getCut().getBitIndex();
            VertexDTO anchor = refs.getFirst().getAbsoluteOffset(cncMachine);
            double diameter1 = cncMachine.getBitStorage().getBitDiameter(bit1Index);
            double diameter2 = cncMachine.getBitStorage().getBitDiameter(bit2Index);
            if(points.getFirst().getX() != 0){
                VertexDTO directionAnchor = (new VertexDTO(points.getFirst().getX(), 0, 0)).normalize();
                anchor = anchor.add(directionAnchor.mul(diameter1/2));
                anchor = anchor.add(directionAnchor.mul(diameter2/2));
            }

            return getCopyPointsWithOffset(anchor);
        }
        if(type == CutType.LINE_HORIZONTAL){
            int bit1Index = this.bitIndex;
            int bit2Index = refs.getFirst().getCut().getBitIndex();
            VertexDTO anchor = refs.getFirst().getAbsoluteOffset(cncMachine);
            double diameter1 = cncMachine.getBitStorage().getBitDiameter(bit1Index);
            double diameter2 = cncMachine.getBitStorage().getBitDiameter(bit2Index);
            if(points.getFirst().getY() != 0){
                VertexDTO directionAnchor = (new VertexDTO(0, points.getFirst().getY(), 0)).normalize();
                anchor = anchor.add(directionAnchor.mul(diameter1/2));
                anchor = anchor.add(directionAnchor.mul(diameter2/2));
            }

            return getCopyPointsWithOffset(anchor);
        }
        if(type == CutType.RECTANGULAR){

            if(refs.size() < 2){throw new AssertionError(type + " needs two refs, it has " + refs.size());}

            // Compute the diameters of the bits used
            int bitIndexRef1 = refs.getFirst().getCut().getBitIndex();
            int bitIndexRef2 = refs.get(1).getCut().getBitIndex();
            int bitIndexL = getBitIndex();
            double diameterRef1 = cncMachine.getBitStorage().getBitDiameter(bitIndexRef1);
            double diameterRef2 = cncMachine.getBitStorage().getBitDiameter(bitIndexRef2);
            double diameterL = cncMachine.getBitStorage().getBitDiameter(bitIndexL);


            // Needs to calculate the absolute two points
            VertexDTO p1a = refs.getFirst().getAbsoluteOffset(cncMachine);
            VertexDTO p1b = refs.getFirst().getAbsoluteFirstPoint(cncMachine);
            if(p1b.getDistance(p1a) < VertexDTO.doubleTolerance){
                p1b = refs.getFirst().getAbsoluteSecondPoint(cncMachine);// Changing the other ref point to prevent accidental colinearity
            }

            // Get the first absolute reference point
            VertexDTO p2a = refs.get(1).getAbsoluteOffset(cncMachine);
            VertexDTO p2b = refs.get(1).getAbsoluteFirstPoint(cncMachine);
            if(p2b.getDistance(p2a) < VertexDTO.doubleTolerance){
                p2b = refs.get(1).getAbsoluteSecondPoint(cncMachine);// Changing the other ref point to prevent accidental colinearity
            }

            VertexDTO cornerRelatif = points.getFirst();


            VertexDTO dirX = VertexDTO.zero();
            VertexDTO dirY = VertexDTO.zero();
            if(cornerRelatif.getX() != 0){
                dirX = new VertexDTO(cornerRelatif.getX(), 0, 0).normalize();
            }
            if(cornerRelatif.getY() != 0){
                dirY = new VertexDTO(0, cornerRelatif.getY(), 0).normalize();
            }

            // Ajout des diametres pour faire la compensation
            p1a = p1a.add(dirX.mul(diameterRef1/2));
            p1b = p1b.add(dirX.mul(diameterRef1/2));

            p2a = p2a.add(dirY.mul(diameterRef2/2));
            p2b = p2b.add(dirY.mul(diameterRef2/2));

            Optional<VertexDTO> intersectionPoint = VertexDTO.isLineIntersectNoLimitation(p1a,
                    p1b, p2a, p2b);

            if(intersectionPoint.isPresent()){
                VertexDTO p1 = points.getFirst();
                VertexDTO p3 = points.get(2);
                VertexDTO diagonal = p3.sub(p1);
                double centerCenterWidth = Math.abs(diagonal.getX()) + diameterL;
                double centerCenterHeight = Math.abs(diagonal.getY()) + diameterL;
                VertexDTO center = intersectionPoint.get().add(p1).add(diagonal.mul(0.5));
                return  generateRectanglePoints(center, centerCenterWidth, centerCenterHeight);
            }
            else{
                return this.getCopyPoints();
            }

        }
        if(type == CutType.L_SHAPE){
            if(refs.size() < 2){throw new AssertionError(type + " needs two refs, it has " + refs.size());}

            ArrayList<VertexDTO> outputPoints = new ArrayList<>();

            // Compute the diameters of the bits used
            int bitIndexRef1 = refs.getFirst().getCut().getBitIndex();
            int bitIndexRef2 = refs.get(1).getCut().getBitIndex();
            int bitIndexL = getBitIndex();
            double diameterRef1 = cncMachine.getBitStorage().getBitDiameter(bitIndexRef1);
            double diameterRef2 = cncMachine.getBitStorage().getBitDiameter(bitIndexRef2);
            double diameterL = cncMachine.getBitStorage().getBitDiameter(bitIndexL);

            // Needs to calculate the absolute two points
            VertexDTO p1a = refs.getFirst().getAbsoluteOffset(cncMachine);
            VertexDTO p1b = refs.getFirst().getAbsoluteFirstPoint(cncMachine);
            if(p1b.getDistance(p1a) < VertexDTO.doubleTolerance){
                p1b = refs.getFirst().getAbsoluteSecondPoint(cncMachine);// Changing the other ref point to prevent accidental colinearity
            }

            // Get the first absolute reference point
            VertexDTO p2a = refs.get(1).getAbsoluteOffset(cncMachine);
            VertexDTO p2b = refs.get(1).getAbsoluteFirstPoint(cncMachine);
            if(p2b.getDistance(p2a) < VertexDTO.doubleTolerance){
                p2b = refs.get(1).getAbsoluteSecondPoint(cncMachine);// Changing the other ref point to prevent accidental colinearity
            }

            VertexDTO cornerRelatif = points.getFirst();

            VertexDTO dirX = VertexDTO.zero();
            VertexDTO dirY = VertexDTO.zero();
            if(cornerRelatif.getX() != 0){
                dirX = new VertexDTO(cornerRelatif.getX(), 0, 0).normalize();
            }
            if(cornerRelatif.getY() != 0){
                dirY = new VertexDTO(0, cornerRelatif.getY(), 0).normalize();
            }

            // Ajout des diametres pour faire la compensation
            p1a = p1a.add(dirX.mul(diameterRef1/2));
            p1b = p1b.add(dirX.mul(diameterRef1/2));

            p2a = p2a.add(dirY.mul(diameterRef2/2));
            p2b = p2b.add(dirY.mul(diameterRef2/2));


            Optional<VertexDTO> intersectionPoint = VertexDTO.isLineIntersectNoLimitation(p1a,
                    p1b, p2a, p2b);


            if(intersectionPoint.isPresent()){

                VertexDTO anchor = intersectionPoint.get();
                VertexDTO corner = anchor.add(cornerRelatif);
                corner = corner.add(dirY.mul(diameterL/2));
                corner = corner.add(dirX.mul(diameterL/2));

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cut cut)) return false;
        return bitIndex == cut.bitIndex && Double.compare(depth, cut.depth) == 0 && type == cut.type && Objects.equals(points, cut.points) && Objects.equals(id, cut.id) && Objects.equals(refs, cut.refs) && cutState == cut.cutState;
    }
}
