package Domain;

import Common.DTO.CutDTO;
import Common.DTO.RefCutDTO;
import Common.DTO.VertexDTO;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Objects;

/**
 * RefCut object to store references to other cuts with an index.
 * The index represents the first point of the line of reference, for example, in a square cut, a reference to the
 * right-most line would be the index 1 :
 * //     2
 * //   o - o
 * // 3 |   |  1
 * //   o - o
 * 0
 */
public class RefCut {
    private Cut cut;
    private int index;
    private double interpolation; // between 0 and 1, represents the interpolated point on the ref line

    public RefCut(Cut cut, int index, double interpolation) {
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

    public RefCut(RefCutDTO refCutDTO, List<Cut> cutList) {
        for (Cut c : cutList) {
            if (c.getId() == refCutDTO.getCut().getId()) {
                this.cut = c;
            }
        }
        if (cut == null) throw new InvalidParameterException("The reference of the Domain Cut was not found");
        this.index = refCutDTO.getIndex();
        this.interpolation = refCutDTO.getInterpolation();
    }


    /**
     * Based on the refCut interpolation, cut reference and index, returns the absolute position
     *
     * @param cncMachine
     * @return
     */
    public VertexDTO getAbsoluteOffset(CNCMachine cncMachine) {
        List<VertexDTO> absoluteVertex = cut.getAbsolutePointsPosition(cncMachine);

        VertexDTO p1 = absoluteVertex.get(index);
        VertexDTO p2 = absoluteVertex.get(index + 1);

        return p1.interpolation(p2, interpolation);
    }

    /**
     * Returns the absolute first point of the cut reference
     *
     * @param cncMachine
     * @return
     */
    public VertexDTO getAbsoluteFirstPoint(CNCMachine cncMachine) {
        List<VertexDTO> absoluteVertex = cut.getAbsolutePointsPosition(cncMachine);
        return absoluteVertex.get(index);
    }

    /**
     * Returns the absolute second point of the cut reference
     *
     * @param cncMachine
     * @return
     */
    public VertexDTO getAbsoluteSecondPoint(CNCMachine cncMachine) {
        List<VertexDTO> absoluteVertex = cut.getAbsolutePointsPosition(cncMachine);
        return absoluteVertex.get(index + 1);
    }

    RefCutDTO getDTO() {
        return new RefCutDTO(cut.getDTO(), this.index, interpolation);
    }

    public void setCut(Cut c) {
        this.cut = c;
    }

    Cut getCut() {
        return this.cut;
    }

    /**
     * Given a refCut to test, and a Cut, check if the refCut is already a reference to the Cut,
     * necessary to check, otherwise the reference could be circular and infinite
     *
     * @param refsCutDTO
     * @param cutToTest
     * @return
     */
    public static boolean isRefCircular(RefCutDTO refsCutDTO, CutDTO cutToTest) {
        boolean out = false;
        out = refsCutDTO.getCut().getId() == cutToTest.getId();
        List<RefCutDTO> refsOfRef = refsCutDTO.getCut().getRefsDTO();
        for (RefCutDTO ref : refsOfRef) {
            out = out | isRefCircular(ref, cutToTest);
        }
        return out;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RefCut refCut)) return false;
        return index == refCut.index && Double.compare(interpolation, refCut.interpolation) == 0 && Objects.equals(cut, refCut.cut);
    }
}
