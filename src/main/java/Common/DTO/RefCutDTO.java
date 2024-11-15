package Common.DTO;

import java.util.List;

/**
 * DTO of the {@code RefCut object}
 */
public class RefCutDTO {
    private CutDTO cut;
    private int index;

    public RefCutDTO(CutDTO cut, int index){
        this.cut = cut;
        this.index = index;
    }

    public VertexDTO getFirstPoint(){
        return this.cut.getPoints().get(index);
    }

    public VertexDTO getSecondPoint(){
        return this.cut.getPoints().get(index + 1);
    }

    public VertexDTO getAbsoluteFirstPoint(){
        return getFirstPoint().add(getOffset());
    }

    public VertexDTO getAbsoluteSecondPoint(){
        return getSecondPoint().add(getOffset());
    }

    public CutDTO getCut(){
        return this.cut;
    }

    /**
     * Recursive offset function to get the absolute offset -- use the getAbsolutePosition on the CutDTO to get the absolute position
     * @return an absolute offset
     */
    public VertexDTO getOffset(){
        if(cut.getRefCutDTO() == null){
            return new VertexDTO(0, 0,0);
        }
        else{
            return cut.getRefCutDTO().getOffset();
        }
    }

}
