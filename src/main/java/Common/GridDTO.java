package Common;

public class GridDTO {
    private final int size;
    private final int magnetPrecision;
    private final boolean magnetic;
    private final boolean active;

    public GridDTO(int size, int magnetPrecision, boolean magnetic, boolean active) {
        this.size = size;
        this.magnetPrecision = magnetPrecision;
        this.magnetic = magnetic;
        this.active = active;
    }

    public int getSize() {
        return this.size;
    }

    public int getMagnetPrecision() {
        return this.magnetPrecision;
    }

    public boolean isMagnetic() {
        return this.magnetic;
    }

    public boolean isActive() {
        return active;
    }
}
