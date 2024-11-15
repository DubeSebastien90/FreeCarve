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

    public RefCut(Cut cut, int index){
        this.cut = cut;
        this.index = index;
    }

    public RefCut(RefCutDTO refCutDTO){
        this.cut = new Cut(refCutDTO.getCut());
        this.index = refCutDTO.getIndex();
    }

    RefCutDTO getDTO(){
        return new RefCutDTO(cut.getDTO(), this.index);
    }


}
