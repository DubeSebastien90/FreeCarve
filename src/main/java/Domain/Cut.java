package Domain;

import Common.CutState;
import Common.DTO.CutDTO;
import Common.DTO.RefCutDTO;
import Common.DTO.RequestCutDTO;
import Common.DTO.VertexDTO;
import Common.InvalidCutState;

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
    private InvalidCutState invalidCutState;

    public Cut(CutDTO uiCut, List<Cut> cutAndBorderList) {
        this.type = uiCut.getCutType();
        this.points = uiCut.getPoints();
        this.bitIndex = uiCut.getBitIndex();
        this.depth = uiCut.getDepth();
        this.id = uiCut.getId();
        this.cutState = uiCut.getState();
        this.invalidCutState = uiCut.getInvalidCutState();

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
        this.invalidCutState = uiCut.getInvalidCutState();

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
        this.invalidCutState = InvalidCutState.OUT_OF_BOUND;
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
        this.invalidCutState = InvalidCutState.OUT_OF_BOUND;
    }

    public Cut(RequestCutDTO requestCutDTO) {
        //todo
    }

    public CutDTO getDTO() {
        return new CutDTO(id, depth, bitIndex, type, points.stream().toList(), refs.stream().map(RefCut::getDTO).collect(Collectors.toList()), cutState, invalidCutState);
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
        setInvalidCutState(InvalidCutState.NO_REFERENCE);
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

    public InvalidCutState getInvalidCutState(){return this.invalidCutState;}

    public void setCutState(CutState cutState){
        this.cutState = cutState;
    }

    public void setInvalidCutState(InvalidCutState cutState){
        this.invalidCutState = cutState;
    }

    /**
     * Get the copied absolute points of the cut, based on it's references
     * @return List<VertexDTO> of the copied absolute points
     */
    public List<VertexDTO> getAbsolutePointsPosition(CNCMachine cncMachine) {
        return CutPointsFactory.generateAbsolutePointsPosition(this, cncMachine);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cut cut)) return false;
        return bitIndex == cut.bitIndex && Double.compare(depth, cut.depth) == 0 && type == cut.type && Objects.equals(points, cut.points) && Objects.equals(id, cut.id) && Objects.equals(refs, cut.refs) && cutState == cut.cutState;
    }
}
