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

}
