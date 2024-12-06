package Common.DTO;

import Common.CutState;
import Common.InvalidCutState;
import Domain.CutType;

import java.io.Serializable;
import java.util.*;

/**
 * This class is a DTO wrapper of the {@code Cut} class in order to transfer READ-ONLY informations
 *
 * @author Louis-Etienne Messier
 * @version 1.0
 * @since 2024-10-12
 */
public class CutDTO implements Serializable {
    private UUID idCut;
    private double depth;
    private int bitIndex;
    private List<VertexDTO> points;
    private CutType type;
    private List<RefCutDTO> refs; // reference to another cut
    private CutState cutState;
    private InvalidCutState invalidCutState;

    /**
     * Basic constructor of the {@code CutDTO}
     *
     * @param idCut    id of the cut
     * @param depth    depth of the cut
     * @param bitIndex index of the bit used to make the cut
     * @param type     type of the cut {@code CutType}
     */
    public CutDTO(UUID idCut, double depth, int bitIndex, CutType type, List<VertexDTO> points, List<RefCutDTO> refs, CutState cutState, InvalidCutState invalidCutState) {
        this.idCut = idCut;
        this.depth = depth;
        this.bitIndex = bitIndex;
        this.type = type;
        this.points = points;
        this.refs = refs;
        this.cutState = cutState;
        this.invalidCutState = invalidCutState;
    }

    public CutDTO(UUID uuid, RequestCutDTO requestCutDTO) {
        this.idCut = uuid;
        this.depth = requestCutDTO.getDepth();
        this.bitIndex = requestCutDTO.getBitLocation();
        this.type = requestCutDTO.getType();
        this.points = requestCutDTO.getPoints();
        this.refs = requestCutDTO.getRefs();
        this.cutState = CutState.VALID;
    }

    public CutDTO(CutDTO other) {
        this.idCut = other.idCut;
        this.depth = other.depth;
        this.bitIndex = other.bitIndex;
        this.type = other.type;
        this.points = new ArrayList<>();
        this.cutState = other.getState();
        this.invalidCutState = other.getInvalidCutState();
        for (VertexDTO p : other.getPoints()) {
            this.points.add(new VertexDTO(p));
        }

        refs = new ArrayList<>();
        for (RefCutDTO ref : other.getRefsDTO()) {
            this.refs.add(new RefCutDTO(ref));
        }
    }

    public int getBitIndex() {
        return this.bitIndex;
    }

    public double getDepth() {
        return this.depth;
    }

    public UUID getId() {
        return this.idCut;
    }

    public CutType getCutType() {
        return this.type;
    }

    public InvalidCutState getInvalidCutState() {return this.invalidCutState;}

    public CutState getState() {
        return this.cutState;
    }

    public List<VertexDTO> getPoints() {
        return this.points;
    }

    public List<RefCutDTO> getRefsDTO() {
        return this.refs;
    }

    public CutDTO addOffsetToPoints(VertexDTO offset) {
        List<VertexDTO> newPoints = new ArrayList<>();
        for (VertexDTO point : this.points) {
            newPoints.add(new VertexDTO(point.getX() + offset.getX(),
                    point.getY() + offset.getY(), point.getZ() + offset.getZ()));
        }
        return new CutDTO(this.idCut, this.depth, this.bitIndex, this.type, newPoints, refs, this.cutState, this.invalidCutState);
    }

    @Override
    public String toString() {
        return "CutDTO{" +
                "idCut=" + idCut +
                ", depth=" + depth +
                ", bitIndex=" + bitIndex +
                ", points=" + points +
                ", type=" + type +
                ", refs=" + refs +
                ", cutState=" + cutState +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CutDTO cutDTO)) return false;
        return Double.compare(depth, cutDTO.depth) == 0 && bitIndex == cutDTO.bitIndex && Objects.equals(idCut, cutDTO.idCut) && Objects.equals(points, cutDTO.points) && type == cutDTO.type && Objects.equals(refs, cutDTO.refs) && cutState == cutDTO.cutState;
    }
}
