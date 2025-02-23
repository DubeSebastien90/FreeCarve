package Common.DTO;

import Domain.CutType;
import Domain.RefCut;

import java.util.List;
import java.util.Optional;

/**
 * The {@code RequestCutDTO} class is a read-only class useful to request cut without knowing if it's a valid cut.
 *
 * @author Adam Côté
 * @version 1.0
 * @since 2024-10-20
 */
public class RequestCutDTO {
    private final List<VertexDTO> points;
    private final CutType type;
    private final int bitLocation;
    private final double depth;
    private final List<RefCutDTO> refs;

    public RequestCutDTO(List<VertexDTO> points, CutType type, int bitLocation, double depth, List<RefCutDTO> refs) {
        this.points = points;
        this.type = type;
        this.bitLocation = bitLocation;
        this.depth = depth;
        this.refs = refs;
    }

    public List<VertexDTO> getPoints() {
        return points;
    }

    public CutType getType() {
        return type;
    }

    public int getBitLocation() {
        return bitLocation;
    }

    public double getDepth() {
        return depth;
    }

    public List<RefCutDTO> getRefs() {return this.refs;}
}
