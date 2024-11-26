package Domain;

import Common.*;
import Common.DTO.CutDTO;
import Common.DTO.PanelDTO;
import Common.DTO.RequestCutDTO;
import Common.DTO.VertexDTO;
import Common.Interfaces.IMemorizer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * The {@code PanelCNC} class represents the board on the CNC. It can have {@code Cut}s and {@code ClampZone}s
 *
 * @author Adam Côté
 * @version 1.0
 * @since 2024-10-20
 */
class PanelCNC {
    private final List<Cut> cutList;
    private final List<ClampZone> clamps;
    private VertexDTO panelDimension;
    private Cut borderCut; // This is to generalise the concept of reference cut, the panelCNC has a rectangular Cut that represents it's borders. When the panel is resized, you need to resize the cut as well
    private final IMemorizer memorizer;
    private static final int MAX_FEET_WIDTH = 10;
    private static final int MAX_FEET_HEIGHT = 5;

    /**
     * Constructs a new {@code PanelCNC} with no {@code Cut} or {@code ClampZone} on it. The dimensions of the board are determined by the {@code VertexDTO} passed as parameter.
     *
     * @param panelDimension dimensions of the board
     */
    PanelCNC(VertexDTO panelDimension, IMemorizer memorizer) {
        this.cutList = new ArrayList<>();
        this.clamps = new ArrayList<>();
        this.panelDimension = panelDimension;
        this.memorizer = memorizer;
        updateBorderCut();
    }

    public PanelCNC(PanelCNC other){
        this.cutList = other.cutList.stream().toList();
        this.clamps = other.clamps.stream().toList();
        this.panelDimension = other.panelDimension;
        this.memorizer = other.memorizer;
        updateBorderCut();
    }

    public PanelDTO getDTO() {
        return new PanelDTO(cutList.stream().map(Cut::getDTO).collect(Collectors.toList()), panelDimension, MAX_FEET_WIDTH, MAX_FEET_HEIGHT, borderCut.getId());
    }

    public double getDepth() {
        return panelDimension.getZ();
    }

    public static double getMaxFeetWidth() {
        return MAX_FEET_WIDTH;
    }

    public static double getMaxFeetHeight() {
        return MAX_FEET_HEIGHT;
    }

    public void setDepth(double depth) {
        this.panelDimension = new VertexDTO(panelDimension.getX(), panelDimension.getY(), depth);
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
        UUID newUUID = UUID.randomUUID();
        CutDTO cutDTO = new CutDTO(newUUID, cut);
        memorizer.executeAndMemorize(()->this.cutList.add(createPanelCut(cutDTO)), ()->this.cutList.removeIf(e->e.getId() == newUUID));
        return Optional.of(newUUID);
    }

    /**
     * Modify an existing cut, check if the new cut params are valid. If so the UUID is returned
     *
     * @param cut The new CutDTO need to be valid
     * @return Optional<UUID>
     */
    Optional<UUID> modifyCut(CutDTO cut) {
        //todo tester si la  modification de la coupe est bonne ou non!!
        for (int i = 0; i < this.cutList.size(); i++) {

            if (cut.getId() == this.cutList.get(i).getId()) {
                this.cutList.get(i).modifyCut(cut, this.getCutAndBorderList());
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
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId() == id) {
                cleanupRemove(list.get(i));
                list.remove(i);
                //todo look for potential non removable cut
                return true;
            }
        }
        return false;
    }

    /**
     * Cleanup all the references of the parameter cut used by all of the other cuts, otherwise, there is references problem
     * All the necessary cuts will become invalid and lose all of their references
     *
     * @param cut cut to cleanup in other cut
     */
    void cleanupRemove(Cut cut){
        for(Cut c : cutList){
            for(RefCut ref : c.getRefs()){
                if(ref.getCut() == cut){
                    c.setInvalidAndNoRef();
                    cleanupRemove(c);
                    break;
                }
            }
        }
    }

    /**
     * Finds a specific cut with id
     *
     * @param id id of the cut
     * @return Optional<CutDTO> : CutDTO if found, null if not found
     */
    Optional<CutDTO> findSpecificCut(UUID id) {
        List<CutDTO> cutsDTO = getDTO().getCutsDTO();
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

    Cut createPanelCut(CutDTO cutDTO){
        return new Cut(cutDTO, this.getCutAndBorderList());
    }

    Cut getBorderCut(){
        return this.borderCut;
    }

    List<Cut> getCutAndBorderList(){
        List<Cut> addAll = new ArrayList<>();
        addAll.addAll(this.cutList);
        addAll.add(borderCut);
        return addAll;
    }

    /**
     * Adds a new {@code ClampZone}
     *
     * @param clamp The new {@code CLampZone}
     */
    void addClamps(ClampZone clamp) {
        this.clamps.add(clamp);
    }

    VertexDTO getPanelDimension() {
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
        double newWidth = Math.min(Math.max(0, width), Util.feet_to_mm(MAX_FEET_WIDTH));
        double newHeight = Math.min(Math.max(0, height), Util.feet_to_mm(MAX_FEET_HEIGHT));
        editCutsOnResize(null, new double[]{newWidth, newHeight});
        panelDimension = new VertexDTO(newWidth, newHeight, 0);
        updateBorderCut();
    }

    /**
     * Edits the cuts which have points referenced to the modified cut
     * @param modifiedCut - the modified cut whih can be a reference for another cut, null if a border is modified
     * @param border - the dimentions of the new pannel
     */
    void editCutsOnResize(Cut modifiedCut, double[] border) {
        for (Cut cut : this.cutList) {
            List<VertexDTO> listPoints = new ArrayList<>();
            boolean edited = false;
            for (int j = 0; j < cut.getPoints().size(); j++) {
                VertexDTO point = cut.getPoints().get(j);
                if (modifiedCut == null) {
                    //look if point is on border
                    if (point.getX() == panelDimension.getX()) {
                        listPoints.add(new VertexDTO(border[0], point.getY(), point.getZ())); //stick to border
                        edited = true;
                    } else if (point.getY() == panelDimension.getY()) {
                        listPoints.add(new VertexDTO(point.getX(), border[1], point.getZ())); //stick to border
                        edited = true;
                    } else {
                        listPoints.add(new VertexDTO(point.getX(), point.getY(), point.getZ())); //dont modify
                    }
                    //look if border is reference
                    System.out.println("A implementer - ref");

                    //if cut is modified, edit cut

                } else{
                    System.out.println("À implementer - coupes");
                }
            }
            //edit the points
            if (edited) {
                cut.setPoints(listPoints);
                editCutsOnResize(cut, border);
            }
        }
    }

    /**
     * Computeds all meeting point on the grid that cover the board.
     *
     * @param precision The size of each square's side of the grid.
     * @return All the coordinates of the points of the grid.
     */
    List<VertexDTO> calculateGridPoint(int precision) {
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
     * @return True if the point is on the board.
     */
    boolean isPointOnPanel(VertexDTO point) {
        return point.getX() >= 0 && point.getY() >= 0 && point.getX() <= getWidth() && point.getY() <= getHeight();
    }

    void updateBorderCut(){
        if(this.borderCut == null){
            ArrayList<VertexDTO> borderPoints = new ArrayList<>();
            borderPoints.add(new VertexDTO(0, 0,0 ));
            borderPoints.add(new VertexDTO(0, panelDimension.getY(), 0 ));
            borderPoints.add(new VertexDTO(panelDimension.getX(), panelDimension.getY(), 0 ));
            borderPoints.add(new VertexDTO(panelDimension.getX(), 0,0 ));
            borderPoints.add(new VertexDTO(0, 0,0 ));

            // Deliberately setting the bixIndex to -1, so that the borderCut has a recognizable bit, that has it's own property
            this.borderCut = new Cut(new VertexDTO(0,0,0), CutType.RECTANGULAR, borderPoints, -1, getDepth());
        }
        else{
            ArrayList<VertexDTO> borderPoints = new ArrayList<>();
            borderPoints.add(new VertexDTO(0, 0,0 ));
            borderPoints.add(new VertexDTO(0, panelDimension.getY(), 0 ));
            borderPoints.add(new VertexDTO(panelDimension.getX(), panelDimension.getY(), 0 ));
            borderPoints.add(new VertexDTO(panelDimension.getX(), 0,0 ));
            borderPoints.add(new VertexDTO(0, 0,0 ));
            this.borderCut.setPoints(borderPoints);
        }
    }

}
