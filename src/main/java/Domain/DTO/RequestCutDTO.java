package Domain.DTO;

import Domain.CutType;

import java.util.List;

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
    private final float depth;

    public RequestCutDTO(List<VertexDTO> points, CutType type, int bitLocation, float depth) {
        this.points = points;
        this.type = type;
        this.bitLocation = bitLocation;
        this.depth = depth;
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

    public float getDepth() {
        return depth;
    }
}