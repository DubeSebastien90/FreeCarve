package Domain;

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
    private float depth;
    private int bitIndex;
//    private List<Vertex3dDTO>;
    private CutType type;

    public CutDTO(UUID idCut, float depth, int bitIndex, CutType type){
        this.idCut = idCut;
        this.depth = depth;
        this.bitIndex = bitIndex;
        this.type = type;
    }
}
