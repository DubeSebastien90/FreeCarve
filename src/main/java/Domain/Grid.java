package Domain;

import Domain.ThirdDimension.Vertex;

import java.util.List;

/**
 * The {@code Grid} class regroup functions that are useful for putting a grid on a {@code PanelCNC}
 *
 * @author Adam Côté
 * @version 1.0
 * @since 2024-10-20
 */
public class Grid {
    private int size;
    private int magnetPrecision;

    Grid(int size, int magnetPrecision) {
        this.size = size;
        this.magnetPrecision = magnetPrecision;
    }

    public int getSize() {
        return this.size;
    }

    void setSize(int size) {
        this.size = size;
    }

    public int getMagnetPrecision() {
        return this.magnetPrecision;
    }

    void setMagnetPrecision(int magnetPrecision) {
        this.magnetPrecision = magnetPrecision;
    }
}

