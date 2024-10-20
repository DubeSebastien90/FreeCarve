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
    private int precision;
    private float width;
    private float height;
    private List<Vertex> grid;

    Grid(int precision, float width, float height) {
        this.precision = precision;
        this.width = width;
        this.height = height;
        calculateAllIntersection();
    }

    int getPrecision() {
        return precision;
    }

    void setPrecision(int precision) {
        this.precision = precision;
    }

    float getWidth() {
        return width;
    }

    void setWidth(float width) {
        this.width = width;
    }

    float getHeight() {
        return height;
    }

    void setHeight(float height) {
        this.height = height;
    }

    List<Vertex> getGrid() {
        return grid;
    }

    /**
     * Calculates the intersections using the height, width and precision of the current {@code Grid}
     */
    void calculateAllIntersection() {
        //todo, must set the grid to the list of intersection (this.grid == intersection)
    }
}
