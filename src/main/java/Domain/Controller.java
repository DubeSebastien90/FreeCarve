package Domain;

import Domain.ThirdDimension.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.security.InvalidKeyException;
import java.security.KeyException;
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
    private final ProjectState currentProjectState;
    private Grid grid;
    private Scene scene;
    private Camera camera;

    public Controller() {
        this(new UndoRedo(), new ProjectState(), new Scene());
    }

    public Controller(UndoRedo undoRedo, ProjectState projectState, Scene scene) {
        this.undoRedo = undoRedo;
        this.currentProjectState = projectState;
        this.scene = scene;
        this.camera = new Camera(scene);
    }


    /**
     * Requests a cut to do on the panel of the current {@code ProjectState}
     *
     * @param cut A requestCutDTO with all the information about the cut.
     * @return The UUID of the Cut if the RequestCutDTO was valid.
     */
    public Optional<UUID> requestCut(RequestCutDTO cut) {
        return this.currentProjectState.getPanel().requestCut(cut);
    }

    /**
     * @return The current {@code ProjectState}
     */
    public ProjectStateDTO getProjectStateDTO() {
        return this.currentProjectState.getCurrentStateDTO();
    }

    /**
     * @return The board of the current {@code ProjectState}
     */
    public PanelDTO getPanelDTO() {
        return getProjectStateDTO().getPanelDTO();
    }

    public Optional<CutDTO> findSpecificCut(UUID id) {
        List<CutDTO> cutsDTO = getProjectStateDTO().getPanelDTO().getCutsDTO();
        for (CutDTO c : cutsDTO) {
            if (c.getId() == id) {
                return Optional.of(c);
            }
        }
        return Optional.empty();
    }

    public List<CutDTO> getCutListDTO() {
        return getProjectStateDTO().getPanelDTO().getCutsDTO();
    }

    /**
     * Resizes the board to the desired size. The desired size must respect the CNC size constraints.
     *
     * @param width  The new width of the board.
     * @param height The new height of the board.
     */
    public void resizePanel(float width, float height) {
        this.currentProjectState.getPanel().resize(width, height);
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
        this.currentProjectState.getPanel().modifyCut(cut);
    }

    /**
     * @return The list of Bit of the CNC
     */
    public BitDTO[] getBitsDTO() {
        return getProjectStateDTO().getBitList();
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
        this.currentProjectState.updateBit(index, bit);
    }

    /**
     * Does the Redo action on the project.
     */
    public ProjectStateDTO redo() {
        this.undoRedo.redo();
        return getProjectStateDTO();
    }

    /**
     * Does the Undo action on the project.
     */
    public ProjectStateDTO undo() {
        this.undoRedo.undo();
        return getProjectStateDTO();
    }

    /**
     * Calculates and return the list of {@code Vertex} that defines the grid of the board.
     *
     * @param precision The precision with which the grid should be calculated.
     * @return The list of intersections of the grid.
     */
    public List<VertexDTO> putGrid(int precision, int magnetPrecision) {
        this.grid = new Grid(precision, magnetPrecision);
        return null;
    }


    /**
     * Function that returns the grid
     *
     * @return the grid
     */
    public GridDTO getGrid() {
        return new GridDTO(this.grid.getSize(),this.grid.getMagnetPrecision());
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

    public Optional<UUID> renderImage(BufferedImage image, VertexDTO position) {
        return camera.renderImage(image, position);
    }

    public UUID getCameraId(){
        return camera.getId();
    }

    /**
     * Rotates the {@code Transform} around the transform's origin as if it were on a gimbal.
     * Mostly meant to be used with the camera, but it is generic to any transform
     *
     * @param transformId The id of the {@code Transform} to move
     * @param XAxisRotation The amount of rotation in rad to apply around the X axis
     * @param YAxisRotation The amount of rotation in rad to apply around the Y axis
     *
     * @throws InvalidKeyException if the given id does not correspond to a transform
     */
    public void panTransform(UUID transformId, float XAxisRotation, float YAxisRotation) throws InvalidKeyException {
        if (camera.getId() == transformId){
            camera.pan(XAxisRotation, YAxisRotation);
        }
        else {
            scene.getMesh(transformId).pan(XAxisRotation, YAxisRotation);
        }
    }

    /**
     * Applies a position, rotation, and scale change to the {@code Transform}
     * @param transformId The id of the {@code Transform} to transform
     * @param positionChange The delta to apply to the position
     * @param rotationChange The euler angle vector in radians to apply to the rotation
     * @param scaleChange The scale delta to apply to the scale
     *
     * @throws InvalidKeyException if the given id does not correspond to a transform
     */
    public void applyTransform(UUID transformId, VertexDTO positionChange, VertexDTO rotationChange, float scaleChange) throws InvalidKeyException {
        scene.applyTransform(transformId, positionChange, rotationChange, scaleChange);
    }
}
