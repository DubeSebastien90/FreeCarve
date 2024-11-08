package Common.DTO;

import Domain.CutType;

import java.util.List;
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

    /**
     * Basic constructor of the {@code CutDTO}
     * @param idCut id of the cut
     * @param depth depth of the cut
     * @param bitIndex index of the bit used to make the cut
     * @param type type of the cut {@code CutType}
     */
    public CutDTO(UUID idCut, double depth, int bitIndex, CutType type, List<VertexDTO> points){
        this.idCut = idCut;
        this.depth = depth;
        this.bitIndex = bitIndex;
        this.type = type;
        this.points = points;
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
}