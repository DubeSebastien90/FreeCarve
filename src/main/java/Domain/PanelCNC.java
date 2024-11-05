package Domain;

import Domain.ThirdDimension.VertexDTO;
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
class PanelCNC {
    private List<Cut> cutList;
    private List<ClampZone> clamps;
    private final Vertex panelDimension;
    private float depth;

    /**
     * Constructs a new {@code PanelCNC} with no {@code Cut} or {@code ClampZone} on it. The dimensions of the board are determined by the {@code Vertex} passed as parameter.
     *
     * @param panelDimension dimensions of the board
     * @param depth depth of the board
     */
    PanelCNC(Vertex panelDimension, float depth) {
        this.cutList = new ArrayList<>();
        this.clamps = new ArrayList<>();
        this.panelDimension = panelDimension;
        this.depth = depth;
    }

    public float getDepth() {
        return depth;
    }

    public void setDepth(float depth) {
        this.depth = depth;
    }

    public List<Cut> getCutList() {
        return cutList;
    }

    /**
     * Adds a new Cut, if the Cut is valid. If so, it's UUID is returned.
     *
     * @param cut The RequestCut that needs to be valid.
     */
    public Optional<UUID> requestCut(RequestCutDTO cut) {
        //todo tester si la coupe est bonne ou non!!
        UUID newUUID = new UUID(1000000, 1000000);
        CutDTO cutDTO = new CutDTO(
                newUUID,
                cut.getDepth(),
                cut.getBitLocation(),
                cut.getType(),
                cut.getPoints()
        );
        this.cutList.add(new Cut(cutDTO));
        return Optional.of(newUUID);
    }

    /**
     * Modify an existing cut, check if the new cut params are valid. If so the UUID is returned
     * @param cut The new CutDTO need to be valid
     * @return Optional<UUID>
     */
    Optional<UUID> modifyCut(CutDTO cut){
        //todo tester si la  modification de la coupe est bonne ou non!!
        for (int i =0; i < this.cutList.size(); i++){

            if (cut.getId() == this.cutList.get(i).getId()){
                this.cutList.set(i, new Cut(cut));
                return Optional.of(cut.getId());
            }
        }
        return Optional.empty();
    }

    /**
     * Removes a cut from the current {@code ProjectState} board
     *
     * @param id The id of the {@code Cut} the needs to be removed
     * @return Boolean : true if cut is removed, false if it can't be removed
     */
    boolean removeCut(UUID id) {
        List<Cut> list = getCutList();
        for(int i=0; i<list.size(); i++){
            if(list.get(i).getId() == id){
                list.remove(i);
                //todo look for potential non removable cut
                return true;
            }
        }
        return false;
    }

    /**
     * Finds a specific cut with id
     * @param id id of the cut
     * @return Optional<CutDTO> : CutDTO if found, null if not found
     */
    Optional<CutDTO> findSpecificCut(UUID id) {
        List<CutDTO> cutsDTO = getPanelDTO().getCutsDTO();
        for (CutDTO c : cutsDTO) {
            if (c.getId() == id) {
                return Optional.of(c);
            }
        }
        return Optional.empty();
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

    Vertex getPanelDimension() {
        return panelDimension;
    }

    /**
     * @return The width of the board.
     */
    double getWidth() {
        return panelDimension.getX();
    }

    /**
     * @return The height of the board.
     */
    double getHeight() {
        return panelDimension.getY();
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
    void resize(double width, double height) {
        //todo
    }

    /**
     * Computeds all meeting point on the grid that cover the board.
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

    /**
     * @return a DTO version of the Panel
     */
    public PanelDTO getPanelDTO() {
        return new PanelDTO(this);
    }

}
