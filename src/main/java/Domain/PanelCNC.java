package Domain;

import Domain.DTO.*;
import Domain.ThirdDimension.Vertex;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * The {@code PanelCNC} class represents the board on the CNC. It can have {@code Cut}s and {@code ClampZone}s
 *
 * @author Adam Côté
 * @version 1.0
 * @since 2024-10-20
 */
public class PanelCNC {
    private List<Cut> cutList;
    private List<Bit> bitList;
    private List<ClampZone> clamps;
    private final Vertex[] board = new Vertex[2];
    private float depth;

    /**
     * Constructs a new {@code PanelCNC} with no {@code Cut} or {@code ClampZone} on it. The dimensions of the board are determined by the {@code Vertex} passed as parameter.
     *
     * @param firstCorner  A position of one of the corner of the rectangular board.
     * @param secondCorner The position of the corner opposite to the firstCorner.
     */
    PanelCNC(Vertex firstCorner, Vertex secondCorner, float depth) {
        this.cutList = new ArrayList<>();
        this.clamps = new ArrayList<>();
        this.board[0] = firstCorner;
        this.board[1] = secondCorner;
        this.depth = depth;
    }

    public float getDepth() {
        return depth;
    }

    public void setDepth(float depth) {
        this.depth = depth;
    }

    List<Cut> getCutList() {
        return cutList;
    }

    /**
     * Adds a new Cut, if the Cut is valid. If so, it's UUID is returned.
     *
     * @param cut The RequestCut that needs to be valid.
     */
    Optional<UUID> newCut(RequestCutDTO cut) {
        //todo
        return null;
    }

    List<ClampZone> getClamps() {
        return clamps;
    }

    /**
     * Adds a new {@code ClampZone}
     *
     * @param clamp The new {@code CLampZone}
     */
    void addClamps(ClampZone clamp) {
        this.clamps.add(clamp);
    }

    Vertex[] getBoard() {
        return board;
    }

    /**
     * @return The width of the board.
     */
    double getWidth() {
        //todo
        return 0;
    }

    /**
     * @return The height of the board.
     */
    double getHeight() {
        //todo
        return 0;
    }

    /**
     * Deletes a {@code Cut} using the Cut's id.
     *
     * @param id The id of the Cut
     */
    void deleteCut(UUID id) {
        //todo
    }

    /**
     * Resizes the board to the desired size. The desired size must respect the CNC size constraints.
     *
     * @param width  The new width of the board.
     * @param height The new height of the board.
     */
    void resize(float width, float height) {
        //todo
    }

    /**
     * Calculate all meeting point on the grid that cover the board.
     *
     * @param precision The size of each square's side of the grid.
     * @return All the coordinates of the points of the grid.
     */
    List<Vertex> calculateGridPoint(int precision) {
        //todo
        return null;
    }

    /**
     * Gets the {@code Cut} or the {@code ClampZone} at the specified coordinates if there is any.
     *
     * @param coordinates The coordinates which are maybe part of a {@code Cut} or a {@code ClampZone}
     * @return The id of the element if an element is found.
     */
    Optional<UUID> getElementAtmm(VertexDTO coordinates) {
        //todo
        return null;
    }

    public PanelDTO getPanelDTO() {
        //todo
        return new PanelDTO(new ArrayList<CutDTO>());
    }
}
