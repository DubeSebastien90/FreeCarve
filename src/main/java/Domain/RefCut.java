package Domain;

import Common.DTO.RefCutDTO;

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
        this.cut = cut;
        this.index = index;
        this.interpolation = interpolation;
    }

    public RefCut(RefCutDTO refCutDTO){
        this.cut = new Cut(refCutDTO.getCut());
        this.index = refCutDTO.getIndex();
        this.interpolation = refCutDTO.getInterpolation();
    }

    RefCutDTO getDTO(){
        return new RefCutDTO(cut.getDTO(), this.index, interpolation);
    }

}
