package Domain;

import Common.DTO.*;
import Common.Exceptions.InvalidBitException;
import Common.Exceptions.InvalidFileExtensionException;
import Common.Interfaces.*;
import Common.Units;
import Common.Interfaces.IDoAction;
import Common.Interfaces.IRefreshable;
import Common.Interfaces.IUndoAction;
import Domain.IO.GcodeGenerator;
import Domain.IO.ProjectFileManager;
import Domain.ThirdDimension.Camera;
import Domain.ThirdDimension.Mesh;
import Domain.ThirdDimension.Scene;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * The {@code Controller} class is a Larman's Controller which will be the entry point if an interface wants to interact with the Domain.
 *
 * @author Adam Côté
 * @version 1.0
 * @since 2024-10-20
 */
public class Controller implements IUnitConverter, IMemorizer {
    private final UndoRedoManager undoRedoManager;
    private final CNCMachine cncMachine;
    private Grid grid;
    private final int defaultGridPrecision = 5;
    private final int defaultMagnetPrecision = 5;
    private Scene scene;
    private final Camera camera;

    Controller(UndoRedoManager undoRedoManager, CNCMachine CNCMachine, Scene scene) {
        this.undoRedoManager = undoRedoManager;
        this.cncMachine = CNCMachine;
        this.scene = scene;
        this.camera = new Camera(scene);
        putGrid(defaultGridPrecision, defaultMagnetPrecision);
    }

    public static Controller initialize() {
        UndoRedoManager undoRedoManager = new UndoRedoManager();
        return new Controller(undoRedoManager, new CNCMachine(undoRedoManager), new Scene());
    }

    public void setScene() {
        this.scene = new Scene(Mesh.PanelToMesh(this, getPanelDTO(), getBitsDTO()));
        this.camera.setScene(this.scene);
    }

    public List<UUID> getMeshesOfScene() {
        return this.scene.getMeshesID();
    }

    /**
     * Requests a cut to do on the panel of the current {@code CNCMachine}
     *
     * @param cut A requestCutDTO with all the information about the cut.
     * @return The UUID of the Cut if the RequestCutDTO was valid.
     */
    public Optional<UUID> requestCut(RequestCutDTO cut) {
        return cncMachine.getPanel().requestCut(cut);
    }

    /**
     * @return The board of the current {@code CNCMachine}
     */
    public PanelDTO getPanelDTO() {
        return cncMachine.getPanel().getDTO();
    }

    /**
     * Finds a specific cut with id
     *
     * @param id id of the cut
     * @return Optional<CutDTO> : CutDTO if found, null if not found
     */
    public Optional<CutDTO> findSpecificCut(UUID id) {
        return this.cncMachine.getPanel().findSpecificCut(id);
    }

    /**
     * Get the CutDTO list of the domain
     *
     * @return List<CutDTO> of the cuts in the domain
     */
    public List<CutDTO> getCutListDTO() {
        return cncMachine.getPanel().getDTO().getCutsDTO();
    }

    /**
     * Resizes the board to the desired size. The desired size must respect the CNC size constraints.
     *
     * @param width  The new width of the board.
     * @param height The new height of the board.
     */
    public void resizePanel(double width, double height) {
        cncMachine.getPanel().resize(width, height, cncMachine.getPanel().getDepth());
    }

    public void resizePanel(double width, double height, double depth) {
        cncMachine.getPanel().resize(width, height, depth);
    }


    /**
     * Removes a cut from the current {@code CNCMachine} board
     *
     * @param id The id of the {@code Cut} the needs to be removed
     * @return Boolean : true if cut is removed, false if it can't be removed
     */
    public boolean removeCut(UUID id) {
        return this.cncMachine.getPanel().removeCut(id, cncMachine);
    }

    /**
     * Modify a cut with the attributes of another one.
     *
     * @param cut The modified Cut.
     */
    public Optional<UUID> modifyCut(CutDTO cut) {
        return this.cncMachine.getPanel().modifyCut(cut);
    }

    /**
     * @return The list of Bit of the CNC
     */
    public BitDTO[] getBitsDTO() {
        return cncMachine.getBitStorage().getBitList();
    }

    /**
     * Removes a {@code Bit} from the bit list of the project.
     *
     * @param index The index of the bit that needs to be removed.
     */
    public void removeBit(int index) throws InvalidBitException {
        cncMachine.getBitStorage().removeBit(index);
        cncMachine.getPanel().validateCuts(cncMachine.getBitStorage());
    }

    /**
     * Modify a {@code Bit} in the bit list of the project.
     *
     * @param index The index of the {@code bit}
     * @param bit   A DTO representing the new {@code Bit}
     */
    public void modifyBit(int index, BitDTO bit) {
        cncMachine.getBitStorage().updateBit(index, bit);
        cncMachine.getPanel().validateCuts(cncMachine.getBitStorage());
    }

    /**
     * Does the Redo action on the project.
     */
    public void redo() {
        this.undoRedoManager.redo();
    }

    /**
     * Does the Undo action on the project.
     */
    public void undo() {
        this.undoRedoManager.undo();
    }

    /**
     * Calculates and return the list of {@code Vertex} that defines the grid of the board.
     *
     * @param precision The precision with which the grid should be calculated.
     * @return The list of intersections of the grid.
     */
    public List<VertexDTO> putGrid(double precision, int magnetPrecision) {
        if (this.grid == null) {
            this.grid = new Grid(precision, magnetPrecision);
        } else {
            this.grid.setMagnetPrecision(magnetPrecision);
            this.grid.setSize(precision);
        }
        return null;
    }


    /**
     * Function that returns the grid
     *
     * @return the grid
     */
    public GridDTO getGrid() {
        return new GridDTO(this.grid.getSize(), this.grid.getMagnetPrecision(), grid.isMagnetic(), grid.isActive());
    }

    /**
     * Saves the current project.
     */
    public void saveProject(File file) throws InvalidFileExtensionException, IOException {
        ProjectFileManager.saveProject(file, getPanelDTO());
    }

    /**
     * Opens a file and set the current project as the project saved in the file.
     */
    public void loadProject(File path) throws InvalidFileExtensionException, IOException, ClassNotFoundException {
        PanelDTO result = ProjectFileManager.loadProject(path);
        PanelDTO copy = getPanelDTO();
        undoRedoManager.executeAndMemorize(() -> cncMachine.setPanel(new PanelCNC(result, undoRedoManager)), () -> cncMachine.setPanel(new PanelCNC(copy, undoRedoManager)));
    }

    /**
     * Saves the current tools.
     */
    public void saveTools(File path) throws InvalidFileExtensionException, IOException {
        ProjectFileManager.saveBits(path, getBitsDTO());
    }

    /**
     * Opens a file and set the current project as the project saved in the file.
     */
    public void loadTools(File path) throws InvalidFileExtensionException, IOException, ClassNotFoundException {
        BitDTO[] result = ProjectFileManager.loadBits(path);
        BitDTO[] copy = getBitsDTO();
        undoRedoManager.executeAndMemorize(()->cncMachine.setBitStorage(new BitStorage(result)), ()->cncMachine.setBitStorage(new BitStorage(copy)));;
    }

    /**
     * Saves the Gcode of the project.
     */
    public void saveGcode(File path) {
        ProjectFileManager.saveString(path, convertToGCode());
    }

    /**
     * Modifies the {@code PanelCNC} of the current {@code CNCMachine}
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
     * Converts the current {@code CNCMachine} as GCode instructions.
     *
     * @return The String that represent the GCode instructions.
     */
    public String convertToGCode() {
        return GcodeGenerator.convertToGCode(this, getPanelDTO());
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

    public UUID getCameraId() {
        return camera.getId();
    }

    /**
     * Rotates the {@code Transform} around the transform's origin as if it were on a gimbal.
     * Mostly meant to be used with the camera, but it is generic to any transform
     *
     * @param transformId   The id of the {@code Transform} to move
     * @param XAxisRotation The amount of rotation in rad to apply around the X axis
     * @param YAxisRotation The amount of rotation in rad to apply around the Y axis
     * @throws InvalidKeyException if the given id does not correspond to a transform
     */
    public void panTransform(UUID transformId, double XAxisRotation, double YAxisRotation) throws InvalidKeyException {
        if (camera.getId() == transformId) {
            camera.pan(XAxisRotation, YAxisRotation);
        } else {
            scene.getMesh(transformId).pan(XAxisRotation, YAxisRotation);
        }
    }

    /**
     * Applies a position, rotation, and scale change to the {@code Transform}
     *
     * @param transformId    The id of the {@code Transform} to transform
     * @param positionChange The delta to apply to the position
     * @param rotationChange The euler angle vector in radians to apply to the rotation
     * @param scaleChange    The scale delta to apply to the scale
     * @throws InvalidKeyException if the given id does not correspond to a transform
     */
    public void applyTransform(UUID transformId, VertexDTO positionChange, VertexDTO rotationChange, double scaleChange) throws InvalidKeyException {
        scene.applyTransform(transformId, positionChange, rotationChange, scaleChange);
    }

    /**
     * Returns an optional closest line point to the board outlines + cuts based on a reference point (cursor)
     *
     * @param p1        initial point of the cut
     * @param cursor    current cursor position
     * @param threshold threshold of the distance
     * @return Optional<VertexDTO> : null if no line nearby, the closest Point if point nearby
     */
    public Optional<VertexDTO> getGridLineNearAllBorderAndCuts(VertexDTO p1, VertexDTO cursor, double threshold) {
        return this.grid.getLineNearAllBorderAndCuts(p1, cursor, this.cncMachine, threshold);
    }


    /**
     * Returns an optionnal closest point to the board outlines + cuts based on a reference point
     *
     * @param point     reference point
     * @param threshold threshold of the distance
     * @return Optional<VertexDTO> : null if no line nearby, the closest Point if point nearby
     */
    public Optional<VertexDTO> getGridPointNearAllBorderAndCuts(VertexDTO point, double threshold) {
        return this.grid.getPointNearAllBorderAndCuts(point, this.cncMachine, threshold);
    }

    /**
     * Returns an optionnal closest point to the board outlines based on a reference point
     *
     * @param point     reference point
     * @param threshold threshold of the distance
     * @return Optional<VertexDTO> : null if no line nearby, the closest Point if point nearby
     */
    public Optional<VertexDTO> getGridPointNearBorder(VertexDTO point, double threshold) {
        return this.grid.getPointNearAllBorder(point, this.cncMachine, threshold, Optional.empty());
    }


    public Optional<VertexDTO> getGridPointNearCuts(VertexDTO point, double threshold) {
        return this.grid.getPointNearAllCuts(point, this.cncMachine, threshold, Optional.empty());
    }

    /**
     * Returns an optionnal closest point to all intersections on the board
     *
     * @param point     reference point
     * @param threshold threshold of the distance
     * @return Optional<VertexDTO> : null if no intersection nearby, the closest Point if point nearby
     */
    public Optional<VertexDTO> getPointNearIntersections(VertexDTO point, double threshold) {
        return this.grid.isPointNearIntersections(point, threshold);
    }

    public void setIntersectionMagnetic(){
        this.grid.setIntersectionIfGrid(this.cncMachine);
    }
    /**
     * Computes all of the intersection points  on the board, stores them in the grid class
     */
    public void computeGridIntersections() {
        this.grid.computeIntersectionPointList(this.cncMachine);
    }

    /**
     * Return the list of the reference Cut that are touching the input point
     *
     * @param point point position to analyse
     * @return list of reference Cut touching the point
     */
    public List<RefCutDTO> getRefCutsAndBorderOnPoint(VertexDTO point) {
        return this.grid.getRefCutsAndBorderOnPoint(point, this.cncMachine);
    }

    public void setGridMagnetism(boolean magnetism) {
        grid.setMagnetic(magnetism);
    }

    public void setGridAvtive(boolean active) {
        grid.setActive(active);

    }

    /**
     * Executes the doAction and memorizes it for the undoRedo system
     *
     * @param doAction   lambda of method to execute
     * @param undoAction lambda of method undoing the first one
     */
    public void executeAndMemorize(IDoAction doAction, IUndoAction undoAction) {
        undoRedoManager.executeAndMemorize(doAction, undoAction);
    }

    /**
     * Register a method to be called when an undo or redo is done
     *
     * @param refreshable class that implements a refresh method
     */
    public void addRefreshListener(IRefreshable refreshable) {
        undoRedoManager.addRefreshListener(refreshable);
    }

    /**
     * @return True if the point is on the board.
     */
    public boolean isPointOnPanel(VertexDTO point) {
        return this.cncMachine.getPanel().isPointOnPanel(point);
    }

    /**
     * Returns a Map of the Bits as (Position of the bit, BitDTO) to know what bit has a value
     *
     * @return Map containing Position of the bit, BitDTO
     */
    public Map<Integer, BitDTO> getConfiguredBitsMap() {
        return cncMachine.getBitStorage().getConfiguredBits();
    }

    public DimensionDTO convertUnit(DimensionDTO toConvert, Units targetUnit) {
        return new DimensionDTO((toConvert.value() * toConvert.unit().getRatio()) / targetUnit.getRatio(), targetUnit);
    }

    public void resetPanelCNC() {
        cncMachine.resetPanelCNC();
    }


    /**
     * Returns the bit diameter if index is valid. If index not valid, returns 0
     *
     * @param bitIndex index
     * @return diameter of the bit
     */
    public double getBitDiameter(int bitIndex) {
        return cncMachine.getBitStorage().getBitDiameter(bitIndex);
    }

    /**
     * Generate a fixed list of points used by the rectangle cut according to an anchor point, a width and a height
     *
     * @param anchor center point of the wanted rectangle
     * @param width full width of the wanted rectangle
     * @param height full height of the wanted rectangle
     * @return
     */
    public List<VertexDTO> generateRectanglePoints(VertexDTO anchor, double width, double height) {
        return CutPointsFactory.generateRectanglePoints(anchor, width, height);
    }

    /**
     * From a reference, and absolute positions, computes the valid relative vertical cuts points
     *
     * @param p1Abs first absolute point
     * @param p2Abs second absolute point
     * @param bitIndex index of the bit of the cut
     * @param refs list of the refs of the cut
     * @return the list of relative points of the wanted cut
     */
    public List<VertexDTO> generateVerticalPointsRelativeEdgeEdgeFromAbsolute(VertexDTO p1Abs, VertexDTO p2Abs, int bitIndex, List<RefCutDTO> refs){
        return CutPointsFactory.generateVerticalPointsRelativeEdgeEdgeFromAbsolute(p1Abs, p2Abs, bitIndex, refs, this, cncMachine);
    }

    /**
     * From a reference, and absolute positions, computes the valid relative horizontal cuts points
     *
     * @param p1Abs first absolute point
     * @param p2Abs second absolute point
     * @param bitIndex index of the bit of the cut
     * @param refs list of the refs of the cut
     * @return the list of relative points of the wanted cut
     */
    public List<VertexDTO> generateHorizontalPointsRelativeEdgeEdgeFromAbsolute(VertexDTO p1Abs, VertexDTO p2Abs, int bitIndex, List<RefCutDTO> refs){
        return CutPointsFactory.generateHorizontalPointsRelativeEdgeEdgeFromAbsolute(p1Abs, p2Abs, bitIndex, refs, this, cncMachine);
    }

    /**
     * From two reference, and an absolute positions, computes the valid relative L cuts points
     * @param p1Abs absolute corner of the wanted cut
     * @param bitIndex index of the bit of the cut
     * @param refs list of the refs of the cut
     * @return the list of relative points of the wanted cut
     */
    public List<VertexDTO> generateLPointsRelativeEdgeEdgeFromAbsolute(VertexDTO p1Abs, int bitIndex, List<RefCutDTO> refs){
        return CutPointsFactory.generateLPointsRelativeEdgeEdgeFromAbsolute(p1Abs, bitIndex, refs, this, cncMachine);
    }

    /**
     * From a reference, and absolute positions, computes the valid relative free cuts points
     * @param p1Abs first absolute point
     * @param p2Abs second absolute point
     * @param bitIndex index of the bit of the cut
     * @param refs list of the refs of the cut
     * @return the list of relative points of the wanted cut
     */
    public List<VertexDTO> generateFreeCutPointsRelativeEdgeEdgeFromAbsolute(VertexDTO p1Abs, VertexDTO p2Abs, int bitIndex, List<RefCutDTO> refs){
        return CutPointsFactory.generateFreeCutPointsRelativeEdgeEdgeFromAbsolute(p1Abs, p2Abs, bitIndex, refs, this, cncMachine);
    }

    /**
     * From two references, and absolute positions, computes the valid rectangle relative cuts points
     * @param p1Abs first corner of the rectangle in absolute coords
     * @param p3Abs third corner (diagonal to p1Abs) of the rectangle in absolute coords
     * @param bitIndex index of the bit of the cut
     * @param refs list of the refs of the cut
     * @return the list of relative points of the wanted cut
     */
    public List<VertexDTO> generateRectanglePointsRelativeEdgeEdgeFromAbsolute(VertexDTO p1Abs, VertexDTO p3Abs, int bitIndex, List<RefCutDTO> refs){
        return CutPointsFactory.generateRectangleRelativeEdgeEdgeFromAbsolute(p1Abs, p3Abs, bitIndex, refs, this, cncMachine);
    }

    /**
     * compute the valid base border cut points
     * @param bitIndex index of the bit of the cut
     * @return the list of relative points of the wanted cut
     */
    public List<VertexDTO> generateBorderPointsRelativeEdgeEdgeFromAbsolute(int bitIndex){
        return CutPointsFactory.generateBorderPointsRelativeEdgeEdgeFromAbsolute(bitIndex,this, cncMachine);
    }

    /**
     * From a cut DTO queries the corresponding Cut Object to get it's absolute position
     *
     * @param cutDTO cutDto to query
     * @return the list of absolute points
     */
    public List<VertexDTO> getAbsolutePointsPosition(CutDTO cutDTO) {
        return cncMachine.getAbsolutePointsPositionOfCutDTO(cutDTO);
    }

    /**
     * Test if the ref and the cut have a circular reference
     * @param refCutDTO to test
     * @param cutToTest to test
     * @ true if circular, false if not circular
     */
    public boolean isRefCircular(RefCutDTO refCutDTO, CutDTO cutToTest) {
        return RefCut.isRefCircular(refCutDTO, cutToTest);
    }

    /**
     * Test if a cursor point has collided with the rounded cut width a width of it's bit diameter
     * @param cutDTO cut to test
     * @param cursor cursor point to test
     * @return true if the point is inside one of the cut
     */
    public boolean isRoundedCutDTOHoveredByCursor(CutDTO cutDTO, VertexDTO cursor){
        return RoundedCut.isRoundedCutHoveredByMouse(cutDTO, cursor, cncMachine);
    }
}

