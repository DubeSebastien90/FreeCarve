package Domain;

import Domain.ThirdDimension.VertexDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * The {@code Controller} class is a Larman's Controller which will be the entry point if an interface wants to interact with the Domain.
 *
 * @author Adam Côté
 * @version 1.0
 * @since 2024-10-20
 */
public class Controller {
    private final FileManager fileManager = new FileManager();
    private final UndoRedo undoRedo;

    public Controller() {
        undoRedo = new UndoRedo();
    }
    public Controller(UndoRedo undoRedo) {
        this.undoRedo = undoRedo;
    }

    /**
     * Requests a cut to do on the panel of the current {@code ProjectState}
     *
     * @param cut A requestCutDTO with all the information about the cut.
     * @return The UUID of the Cut if the RequestCutDTO was valid.
     */
    public Optional<UUID> requestCut(RequestCutDTO cut) {
        //todo
        return null;
    }

    /**
     * @return The current {@code ProjectState}
     */
    public ProjectStateDTO getProjectState() {
        //todo
        return undoRedo.getCurrentState().getCurrentStateDTO();
    }

    /**
     * @return The board of the current {@code ProjectState}
     */
    public PanelDTO getPanelCNC() {
        return getProjectState().getBoard();
    }

    /**
     * Resizes the board to the desired size. The desired size must respect the CNC size constraints.
     *
     * @param width  The new width of the board.
     * @param height The new height of the board.
     */
    public void resizePanel(float width, float height) {
        this.undoRedo.getCurrentState().getBoard().resize(width, height);
    }

    /**
     * Removes a cut from the current {@code ProjectState} board
     *
     * @param id The id of the {@code Cut} the needs to be removed
     */
    public void removeCut(UUID id) {
        //todo
    }

    /**
     * Modify a cut with the attributes of another one.
     *
     * @param cut The modified Cut.
     */
    public void modifyCut(CutDTO cut) {
        //todo
    }

    /**
     * @return The list of Bit of the CNC
     */
    public BitDTO[] getBits() {
        return getProjectState().getBitList();
    }

    /**
     * Removes a {@code Bit} from the bit list of the project.
     *
     * @param index The index of the bit that needs to be removed.
     */
    public void removeBit(int index) {
        //todo
    }

    /**
     * Modify a {@code Bit} in the bit list of the project.
     *
     * @param index The index of the {@code bit}
     * @param bit   A DTO representing the new {@code Bit}
     */
    public void modifyBit(int index, BitDTO bit) {
        undoRedo.getCurrentState().updateBit(index, bit);
    }

    /**
     * Does the Redo action on the project.
     */
    public ProjectStateDTO redo() {
        this.undoRedo.redo();
        return getProjectState();
    }

    /**
     * Does the Undo action on the project.
     */
    public ProjectStateDTO undo() {
        this.undoRedo.undo();
        return getProjectState();
    }

    /**
     * Calculates and return the list of {@code Vertex} that defines the grid of the board.
     *
     * @param precision The precision with which the grid should be calculated.
     * @return The list of intersections of the grid.
     */
    public List<VertexDTO> putGrid(int precision) {
        //todo
        return null;
    }

    /**
     * Saves the current state of the project.
     */
    public void saveProject() {
        fileManager.saveProject(undoRedo.getCurrentState());
    }

    /**
     * Opens a file and set the current project as the project saved in the file.
     */
    public void openProject() {
        fileManager.openProject();
        //todo
    }

    /**
     * Modifies the {@code PanelCNC} of the current {@code ProjectState}
     *
     * @param panel The new PanelCNC as a DTO
     */
    public void modifyPanel(PanelDTO panel) {
        //todo
    }

    /**
     * Adds a {@code ClampZone} to the current {@code PanelCNC}
     *
     * @param points the list of points that define the {@code ClampZone}
     * @return The id of the clampZone if it could be created.
     */
    public Optional<UUID> addClampZone(VertexDTO[] points) {
        //todo
        return null;
    }

    /**
     * Modifies an existing clampZone
     *
     * @param id    The id of the ClampZone that is modified
     * @param clamp The new ClampZone
     */
    public void modifyClampZone(UUID id, ClampZoneDTO clamp) {
        //todo
    }

    /**
     * Removes a ClampZone form the current {@code PanelCNC}
     *
     * @param id The id of the {@code ClampZone} that needs to be removed.
     */
    public void removeClampZone(UUID id) {
        //todo
    }

    /**
     * Converts the current {@code ProjectState} as GCode instructions.
     *
     * @return The String that represent the GCode instructions.
     */
    public String convertToGCode() {
        return fileManager.convertToGCode(undoRedo.getCurrentState());
    }


    /**
     * Gets the {@code Cut} or {@code ClampZone} at the specified mm position
     *
     * @param position The position which we want to know if there is an element.
     * @return The id of the element if an element as been found.
     */
    public Optional<UUID> getElementAtmm(VertexDTO position) {
        //todo
        return null;
    }

}
