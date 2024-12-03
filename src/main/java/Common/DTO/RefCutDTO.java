package Common.DTO;


import Domain.Controller;

import javax.naming.ldap.Control;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * DTO of the {@code RefCut object}
 */
public class RefCutDTO implements Serializable {
    private CutDTO cut;
    private int index;
    private double interpolation;

    public RefCutDTO(CutDTO cut, int index, double interpolation) {
        if (index >= cut.getPoints().size() - 1) {
            throw new IllegalArgumentException("The index " + index + " of the refCut is invalid, it doesn't correspond to the size of the points");
        }
        if (interpolation > 1 || interpolation < 0) {
            throw new IllegalArgumentException("The interpolation is outside of it's designed boundaries : 0 and 1");
        }

        this.cut = cut;
        this.index = index;
        this.interpolation = interpolation;
    }

    public CutDTO getCut() {
        return this.cut;
    }

    public int getIndex() {
        return this.index;
    }

    public double getInterpolation() {
        return this.interpolation;
    }

    public RefCutDTO(RefCutDTO refCutDTO) {
        this.cut = new CutDTO(refCutDTO.getCut());
        this.index = refCutDTO.getIndex();
        this.interpolation = refCutDTO.getInterpolation();
    }

    public VertexDTO getAbsoluteOffset(Controller controller) {
        List<VertexDTO> absoluteVertex = controller.getAbsolutePointsPosition(cut);

        VertexDTO p1 = absoluteVertex.get(index);
        VertexDTO p2 = absoluteVertex.get(index + 1);

        return p1.interpolation(p2, interpolation);
    }

    public VertexDTO getAbsoluteFirstPoint(Controller controller) {
        List<VertexDTO> absoluteVertex = controller.getAbsolutePointsPosition(cut);
        return absoluteVertex.get(index);
    }

    public VertexDTO getAbsoluteSecondPoint(Controller controller) {
        List<VertexDTO> absoluteVertex = controller.getAbsolutePointsPosition(cut);
        return absoluteVertex.get(index + 1);
    }

    @Override
    public String toString() {
        return "RefCutDTO{" +
                "cut=" + cut +
                ", index=" + index +
                ", interpolation=" + interpolation +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RefCutDTO refCutDTO)) return false;
        return index == refCutDTO.index && Double.compare(interpolation, refCutDTO.interpolation) == 0 && Objects.equals(cut, refCutDTO.cut);
    }
}
