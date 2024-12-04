package Domain;

import Common.*;
import Common.DTO.*;
import Common.Exceptions.ClampZoneException;
import Common.DTO.*;
import Common.DTO.CutDTO;
import Common.DTO.PanelDTO;
import Common.DTO.RequestCutDTO;
import Common.DTO.VertexDTO;
import Common.Interfaces.IMemorizer;
import Common.Util;

import java.util.*;
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
    private static final VertexDTO defaultPanelDimension = new VertexDTO(1219.2, 914.4, 50); // dimension in mm

    PanelCNC(IMemorizer memorizer) {
        this(defaultPanelDimension, memorizer);
    }

    /**
     * Constructs a new {@code PanelCNC} with no {@code Cut} or {@code ClampZone} on it. The dimensions of the board are determined by the {@code VertexDTO} passed as parameter.
     *
     * @param panelDimension dimensions of the board
     */
    PanelCNC(VertexDTO panelDimension, IMemorizer memorizer) {
        this.cutList = new ArrayList<>();
        this.clamps = new ArrayList<>();
        resize(panelDimension.getX(), panelDimension.getY(), panelDimension.getZ());
        this.memorizer = memorizer;
        updateBorderCut();
    }

    public PanelCNC(PanelDTO panelDTO, IMemorizer memorizer) {
        this.panelDimension = panelDTO.getPanelDimension();
        this.memorizer = memorizer;
        this.borderCut = new Cut(panelDTO.getBorderCut(), new ArrayList<>());
        this.clamps = new ArrayList<>();
        List<Cut> cuts = new ArrayList<>();
        cuts.add(borderCut);
        for (CutDTO cutDTO : panelDTO.getCutsDTO()){
            cuts.add(new Cut(cutDTO, cuts));
        }
        cuts.remove(borderCut);
        this.cutList = cuts;
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
        memorizer.executeAndMemorize(() -> this.cutList.add(createPanelCut(cutDTO)), () -> this.cutList.removeIf(e -> e.getId() == newUUID));
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
     * Removes a cut from the current {@code CNCMachine} board
     *
     * @param id The id of the {@code Cut} the needs to be removed
     * @return Boolean : true if cut is removed, false if it can't be removed
     */
    boolean removeCut(UUID id, CNCMachine cncMachine) {
        List<Cut> list = getCutList();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId() == id) {
                cleanupRemove(list.get(i), cncMachine);
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
    void cleanupRemove(Cut cut, CNCMachine cncMachine){
        for(Cut c : cutList){
            for(RefCut ref : c.getRefs()){
                if(ref.getCut() == cut){
                    c.setInvalidAndNoRef(cncMachine);
                    cleanupRemove(c, cncMachine);
                    break;
                }
            }
        }
    }

    /**
     * Updates the validity state of the cuts depending on the bits
     *
     * @param bitStorage the bit storage containing the bits
     */
    void validateCuts(BitStorage bitStorage){
        Map<Integer, BitDTO> configuredBits = bitStorage.getConfiguredBits();
        for (Cut c : cutList) {
            if (!c.getRefs().isEmpty()){
                c.setCutState(configuredBits.containsKey(c.getBitIndex()) ? CutState.VALID : CutState.NOT_VALID);
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

    Cut createPanelCut(CutDTO cutDTO) {
        return new Cut(cutDTO, this.getCutAndBorderList());
    }

    Cut getBorderCut() {
        return this.borderCut;
    }

    List<Cut> getCutAndBorderList() {
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
    Optional<UUID> addClamps(ClampZoneDTO clamp) throws ClampZoneException {
        for (VertexDTO points : clamp.getZone()) {
            if (!isPointOnPanel(points)) {
                return Optional.empty();
            }
        }
        ClampZone newClamp = new ClampZone(clamp);
        memorizer.executeAndMemorize(()->this.clamps.add(newClamp), ()->this.clamps.removeIf(e->e.getId() == newClamp.getId()));
        return Optional.of(newClamp.getId());
    }

    /**
     * Removes a {@code ClampZone} from the current {@code ProjectState} board
     * @param id The id of the {@code ClampZone} the needs to be removed
     * @return Boolean : true if clamp is removed, false if it can't be removed
     */
    boolean removeClamp(UUID id){
        for(ClampZone clamp : clamps){
            if(clamp.getId() == id){
                memorizer.executeAndMemorize(()->this.clamps.remove(clamp), ()->this.clamps.add(clamp));
                return true;
            }
        }
        return false;
    }

    boolean modifyClamp(ClampZoneDTO clamp) throws ClampZoneException {
        for (VertexDTO points : clamp.getZone()) {
            if (!isPointOnPanel(points)) {
                return false;
            }
        }
        for (ClampZone c : clamps) {
            if (c.getId() == clamp.getClampId().orElseThrow(() ->new ClampZoneException("La zone interdite n'existe pas"))) {
                ClampZoneDTO copy = new ClampZoneDTO(c.getZone()[0], c.getZone()[1], Optional.of(c.getId()));
                memorizer.executeAndMemorize(()-> {
                    try {
                        c.modifyClamp(clamp);
                    } catch (ClampZoneException e) {
                        System.out.println("Modification impossible"); // Might never happen but was obliged to surround
                    }
                }, ()-> {
                    try {
                        c.modifyClamp(copy);
                    } catch (ClampZoneException e) {
                        System.out.println("Modification impossible"); // Might never happen but was obliged to surround
                    }
                });
                return true;
            }
        }
        return false;
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
    void resize(double width, double height, double depth) {
        double newWidth = Math.min(Math.max(0, width), Util.feet_to_mm(MAX_FEET_WIDTH));
        double newHeight = Math.min(Math.max(0, height), Util.feet_to_mm(MAX_FEET_HEIGHT));
        double newDepth = depth;
        panelDimension = new VertexDTO(newWidth, newHeight, newDepth);
        updateBorderCut();
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

    void updateBorderCut() {
        if (this.borderCut == null) {
            ArrayList<VertexDTO> borderPoints = new ArrayList<>();
            borderPoints.add(new VertexDTO(0, 0, 0));
            borderPoints.add(new VertexDTO(0, panelDimension.getY(), 0));
            borderPoints.add(new VertexDTO(panelDimension.getX(), panelDimension.getY(), 0));
            borderPoints.add(new VertexDTO(panelDimension.getX(), 0, 0));
            borderPoints.add(new VertexDTO(0, 0, 0));

            // Deliberately setting the bixIndex to -1, so that the borderCut has a recognizable bit, that has it's own property
            this.borderCut = new Cut(CutType.RECTANGULAR, borderPoints, -1, getDepth());
        } else {
            ArrayList<VertexDTO> borderPoints = new ArrayList<>();
            borderPoints.add(new VertexDTO(0, 0, 0));
            borderPoints.add(new VertexDTO(0, panelDimension.getY(), 0));
            borderPoints.add(new VertexDTO(panelDimension.getX(), panelDimension.getY(), 0));
            borderPoints.add(new VertexDTO(panelDimension.getX(), 0, 0));
            borderPoints.add(new VertexDTO(0, 0, 0));
            this.borderCut.setPoints(borderPoints);
        }
    }

    public void verifyCuts(){
        for (ClampZone clampZone: clamps){
            for (Cut cut: cutList){
                if(clampZone.intersectCut(cut)){
                    cut.setInvalidAndNoRef();
                }
            }
        }
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PanelCNC panelCNC)) return false;
        return Objects.equals(cutList, panelCNC.cutList) && Objects.equals(clamps, panelCNC.clamps) && Objects.equals(panelDimension, panelCNC.panelDimension) && Objects.equals(borderCut, panelCNC.borderCut);
    }
}
