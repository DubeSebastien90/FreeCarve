package Common.DTO;

/**
 * DTO of the {@code RefCut object}
 */
public class RefCutDTO {
    private CutDTO cut;
    private int index;
    private double interpolation;

    public RefCutDTO(CutDTO cut, int index, double interpolation){
        if (interpolation < 0 || interpolation > 0) throw new IllegalArgumentException("L'interpolation doit etre entre 0 et 1");
        this.cut = cut;
        this.index = index;
    }

    public RefCutDTO(RefCutDTO other){
        this.cut = new CutDTO(other.getCut());
        this.index = other.getIndex();
        this.interpolation = other.getInterpolation();
    }

    public VertexDTO getFirstPoint(){
        return this.cut.getPoints().get(index);
    }

    public VertexDTO getSecondPoint(){
        return this.cut.getPoints().get(index + 1);
    }

    public CutDTO getCut(){
        return this.cut;
    }

    public int getIndex(){
        return this.index;
    }

    public double getInterpolation() {return this.interpolation;}

    /**
     * Recursive offset function to get the absolute offset -- use the getAbsolutePosition on the CutDTO to get the absolute position
     * @return an absolute offset
     */
    public VertexDTO getAbsoluteOffset(){
        return new VertexDTO(0, 0, 0);
    }

}
