package Domain;

import Common.DTO.RefCutDTO;
import Common.DTO.VertexDTO;

import java.security.InvalidParameterException;
import java.util.List;

/**
 * RefCut object to store references to other cuts with an index.
 * The index represents the first point of the line of reference, for example, in a square cut, a reference to the
 * right-most line would be the index 1 :
 //     2
 //   o - o
 // 3 |   |  1
 //   o - o
        0
 */
public class RefCut {
    private Cut cut;
    private int index;
    private double interpolation; // between 0 and 1, represents the interpolated point on the ref line

    public RefCut(Cut cut, int index, double interpolation){
        if (index >= cut.getPoints().size() - 1){throw new IllegalArgumentException("The index " + index + " of the refCut is invalid, it doesn't correspond to the size of the points");}
        if (interpolation > 1 || interpolation < 0) {throw new IllegalArgumentException("The interpolation is outside of it's designed boundaries : 0 and 1");}

        this.cut = cut;
        this.index = index;
        this.interpolation = interpolation;
    }

    public RefCut(RefCutDTO refCutDTO, List<Cut> cutList){
        for(Cut c : cutList){
            if(c.getId() == refCutDTO.getCut().getId()){
                this.cut = c;
            }
        }
        if(cut == null) throw new InvalidParameterException("The reference of the Domain Cut was not found");
        this.index = refCutDTO.getIndex();
        this.interpolation = refCutDTO.getInterpolation();
    }


    public VertexDTO getAbsoluteOffset(){
        List<VertexDTO> absoluteVertex = cut.getAbsolutePointsPosition();

        VertexDTO p1 = absoluteVertex.get(index);
        VertexDTO p2 = absoluteVertex.get(index+1);

        return p1.interpolation(p2, interpolation);
    }

    public VertexDTO getAbsoluteFirstPoint(){
        List<VertexDTO> absoluteVertex = cut.getAbsolutePointsPosition();
        return absoluteVertex.get(index);
    }

    RefCutDTO getDTO(){
        return new RefCutDTO(cut.getDTO(), this.index, interpolation);
    }

}
