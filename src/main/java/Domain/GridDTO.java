package Domain;

public class GridDTO {
    private int size;
    private int magnetPrecision;

    public GridDTO(int size, int magnetPrecision) {
        this.size = size;
        this.magnetPrecision = magnetPrecision;
    }

    public int getSize() {
        return this.size;
    }

    public int getMagnetPrecision() {
        return this.magnetPrecision;
    }
}
