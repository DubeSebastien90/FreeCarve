package Domain;

import Common.CutState;
import Common.DTO.*;
import Common.Interfaces.IMemorizer;
import Common.InvalidCutState;
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
    private List<Cut> cutList;
    private VertexDTO panelDimension;
    private Cut borderCut; // This is to generalise the concept of reference cut, the panelCNC has a rectangular Cut that represents it's borders. When the panel is resized, you need to resize the cut as well
    private final IMemorizer memorizer;
    private static final int MAX_FEET_WIDTH = 10;
    private static final int MAX_FEET_HEIGHT = 5;
    private static VertexDTO defaultPanelDimension = new VertexDTO(2438.4, 1219.2, 50); // dimension in mm
    private final List<List<CutDTO>> savedCut = new ArrayList<>();
    private final List<Integer> pos = new ArrayList<>();

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
        resize(panelDimension.getX(), panelDimension.getY(), panelDimension.getZ());
        this.memorizer = memorizer;
        updateBorderCut();
    }

    public PanelCNC(PanelDTO panelDTO, IMemorizer memorizer) {
        this.panelDimension = panelDTO.getPanelDimension();
        this.memorizer = memorizer;
        this.borderCut = new Cut(panelDTO.getBorderCut(), new ArrayList<>());
        List<Cut> cuts = new ArrayList<>();
        cuts.add(borderCut);
        for (CutDTO cutDTO : panelDTO.getCutsDTO()) {
            cuts.add(new Cut(cutDTO, cuts));
        }
        cuts.remove(borderCut);
        this.cutList = cuts;


    }

    public static VertexDTO getDefaultPanelDimensions() {
        return defaultPanelDimension;
    }

    public static void setDefaultPanelDimension(VertexDTO v) {
        defaultPanelDimension = v;
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
    public Optional<UUID> requestCut(CNCMachine cncMachine, RequestCutDTO cut) {
        UUID newUUID = UUID.randomUUID();
        CutDTO cutDTO = new CutDTO(newUUID, cut);
        memorizer.executeAndMemorize(() -> {
            this.cutList.add(createPanelCut(cutDTO));
            validateAll(cncMachine);
        }, () -> {
            int index = -1;
            for (Cut ct : cutList) {
                if (ct.getId() == newUUID) {
                    index = cutList.indexOf(ct);
                }
            }
            if (index > -1) {
                this.cutList.remove(index);
                this.pos.add(index);
                //this.cutList.removeIf(e -> e.getId() == newUUID);
            }
            validateAll(cncMachine);
        });
        return Optional.of(newUUID);
    }

    /**
     * Modify an existing cut, check if the new cut params are valid. If so the UUID is returned
     *
     * @param cut The new CutDTO need to be valid
     * @return Optional<UUID>
     */
    Optional<UUID> modifyCut(CNCMachine cncMachine, CutDTO cut, boolean canMemorize) {
        for (int i = 0; i < this.cutList.size(); i++) {
            if (cut.getId() == this.cutList.get(i).getId()) {
                int finalI = i;
                CutDTO ct = this.cutList.get(finalI).getDTO();
                if (canMemorize) {
                    memorizer.executeAndMemorize(() -> {
                        this.cutList.get(finalI).modifyCut(cut, this.getCutAndBorderList());
                        validateAll(cncMachine);
                    }, () -> {
                        this.cutList.get(finalI).modifyCut(ct, this.getCutAndBorderList());
                        validateAll(cncMachine);
                    });
                } else {
                    this.cutList.get(finalI).modifyCut(cut, this.getCutAndBorderList());
                    validateAll(cncMachine);
                }

                return Optional.of(cut.getId());
            }
        }
        return Optional.empty();
    }


    private void replaceWithModifiedRef(Cut oldRef, Cut newRef) {
        for (Cut ct : cutList) {
            for (RefCut rfc : ct.getRefs()) {
                Cut currCutRef = rfc.getCut();
                if (currCutRef.equals(oldRef)) {
                    rfc.setCut(newRef);
                }
            }
        }

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
                int finalI = i;
                final Cut[] ct = {list.get(finalI)};
                List<Cut> ct2 = findReference(ct[0], cncMachine, new ArrayList<>());
                memorizer.executeAndMemorize(() -> {
                    ArrayList<CutDTO> a = new ArrayList<>();
                    for (Cut c : cutList) {
                        a.add(c.getDTO());
                    }
                    cleanupRemove(list.get(finalI), cncMachine);
                    savedCut.add(a);
                    list.remove(finalI);
                    validateAll(cncMachine);
                }, () -> {
                    cutList.clear();
                    for (CutDTO c : savedCut.removeLast()) {
                        Cut cte = new Cut(c, getCutAndBorderList());
                        cutList.add(cte);
                        if (cte.equals(ct[0])) {
                            ct[0] = cte;
                        }
                    }
                    validateAll(cncMachine);
                });

                return true;
            }
        }
        return false;
    }

    private void restoreRefList(List<Cut> goodCut) {
        HashMap<Integer, Cut> dic = new HashMap<>();
        for (Cut c : cutList) {
            for (Cut oldC : goodCut) {
                if (oldC.getId() == c.getId()) {
                    int i = cutList.indexOf(c);
                    dic.put(i, oldC);
                }
            }
        }
        for (Integer i : dic.keySet()) {
            cutList.remove(i.intValue());
            cutList.add(i, dic.get(i));
        }
    }

    private List<Cut> findReference(Cut cut, CNCMachine cncMachine, List<Cut> li) {
        for (Cut c : cutList) {
            for (RefCut ref : c.getRefs()) {
                if (ref.getCut() == cut) {
                    ArrayList<RefCut> r = new ArrayList<>();
                    r.add(ref);
                    li.add(new Cut(c.getDTO(), getCutAndBorderList()));
                    findReference(c, cncMachine, li);
                    break;
                }
            }
        }
        return li;
    }

    /**
     * Cleanup all the references of the parameter cut used by all of the other cuts, otherwise, there is references problem
     * All the necessary cuts will become invalid and lose all of their references
     *
     * @param cut cut to cleanup in other cut
     */
    void cleanupRemove(Cut cut, CNCMachine cncMachine) {
        for (Cut c : cutList) {
            for (RefCut ref : c.getRefs()) {
                Cut refc = ref.getCut();
                if (refc.getBitIndex() == cut.getBitIndex() && Double.compare(refc.getDepth(), cut.getDepth()) == 0 && refc.getType() == cut.getType() && Objects.equals(refc.getPoints(), cut.getPoints()) && Objects.equals(refc.getId(), cut.getId()) && Objects.equals(refc.getRefs(), cut.getRefs())) {
                    c.setInvalidAndNoRef(cncMachine);
                    cleanupRemove(c, cncMachine);
                    break;
                }
            }
        }
    }

    boolean validateCutBasedOnBits(CNCMachine cncMachine, CutDTO cutDTO) {
        if(cutDTO.getCutType() == CutType.CLAMP){return true;}
        Map<Integer, BitDTO> configuredBits = cncMachine.getBitStorage().getConfiguredBits();
        return configuredBits.containsKey(cutDTO.getBitIndex());
    }

    boolean validateCutBasedOnRefs(Cut cut) {
        return cut.areCutRefsValid();
    }

    boolean validateCutOnBoard(Cut cut, CNCMachine cncMachine) {
        for (VertexDTO absPoints : cut.getAbsolutePointsPosition(cncMachine)) {
            if (!isPointOnPanel(absPoints)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns a list of all of the errors related to the cut
     *
     * @param cncMachine ref to CNCMachine
     * @param cutToCheck cut to analyse to find it's errors
     * @return List of the possible cutInvalidStates
     */
    List<InvalidCutState> getInvalidCutStates(CNCMachine cncMachine, UUID cutToCheck) {
        List<InvalidCutState> outputList = new ArrayList<>();
        Optional<Cut> cut = findSpecificCut(cutToCheck);

        if (cut.isPresent()) {
            if (!validateCutBasedOnBits(cncMachine, cut.get().getDTO())) {
                outputList.add(InvalidCutState.INVALID_BIT);
            }
            if (!validateCutWithClamps(cncMachine, cut.get())) {
                outputList.add(InvalidCutState.CLAMPED);
            }
            if (!validateCutBasedOnRefs(cut.get())) {
                outputList.add(InvalidCutState.INVALID_REF);
            }
            if (!validateCutOnBoard(cut.get(), cncMachine)) {
                outputList.add(InvalidCutState.OUT_OF_BOARD);
            }
        }
        return outputList;
    }

    void validateAll(CNCMachine cncMachine) {
        for (Cut c : this.cutList) {
            List<InvalidCutState> invalidCutStates = getInvalidCutStates(cncMachine, c.getId());
            if (invalidCutStates.isEmpty()) {
                c.setCutState(CutState.VALID);
            } else {
                c.setCutState(CutState.NOT_VALID);
            }
        }
    }

    /**
     * Finds a specific cut with id
     *
     * @param id id of the cut
     * @return Optional<CutDTO> : CutDTO if found, null if not found
     */
    Optional<CutDTO> findSpecificCutDTO(UUID id) {
        List<CutDTO> cutsDTO = getDTO().getCutsDTO();
        for (CutDTO c : cutsDTO) {
            if (c.getId() == id) {
                return Optional.of(c);
            }
        }
        return Optional.empty();
    }

    /**
     * Finds a specific cut with id
     *
     * @param id id of the cut
     * @return Optional<CutDTO> : CutDTO if found, null if not found
     */
    Optional<Cut> findSpecificCut(UUID id) {
        List<Cut> cuts = getCutList();
        for (Cut c : cuts) {
            if (c.getId() == id) {
                return Optional.of(c);
            }
        }
        return Optional.empty();
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
        return point.getX() + VertexDTO.doubleTolerance >= 0 && point.getY() + VertexDTO.doubleTolerance >= 0 &&
                point.getX() - VertexDTO.doubleTolerance <= getWidth() && point.getY() - VertexDTO.doubleTolerance <= getHeight();
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PanelCNC panelCNC)) return false;
        return Objects.equals(cutList, panelCNC.cutList) && Objects.equals(panelDimension, panelCNC.panelDimension) && Objects.equals(borderCut, panelCNC.borderCut);
    }

    private boolean validateCutWithClamps(CNCMachine cncMachine, Cut cut) {
        ArrayList<Cut> clampZones = this.getCutList().stream()
                .filter(currentCut -> currentCut.getType() == CutType.CLAMP)
                .collect(Collectors.toCollection(ArrayList::new));

        for (Cut clampZone : clampZones) {
            if (cut.getType() != CutType.CLAMP &&
                    (cutIntersectsClampZone(cut.getAbsolutePointsPosition(cncMachine), clampZone, cncMachine.getBitStorage().getBitDiameter(cut.getBitIndex())) ||
                            cutInClampZone(cncMachine, cut, clampZone))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Check if a cut intersects a clamp zone
     *
     * @param cutPoints The absolute points of a cut
     * @param clampZone The clamp zone
     * @param diameter  The diameter of the cut
     * @return True if it intersects, false otherwise
     */
    public boolean cutIntersectsClampZone(List<VertexDTO> cutPoints, Cut clampZone, double diameter) {
        RoundedCut clampRound1 = new RoundedCut(clampZone.getPoints().get(0), clampZone.getPoints().get(1), 0.01);
        RoundedCut clampRound2 = new RoundedCut(clampZone.getPoints().get(1), clampZone.getPoints().get(2), 0.01);
        RoundedCut clampRound3 = new RoundedCut(clampZone.getPoints().get(2), clampZone.getPoints().get(3), 0.01);
        RoundedCut clampRound4 = new RoundedCut(clampZone.getPoints().get(3), clampZone.getPoints().get(0), 0.01);

        for (int i = 0; i < cutPoints.size() - 1; i++) {
            RoundedCut cutRound = new RoundedCut(cutPoints.get(i),
                    cutPoints.get(i + 1),
                    diameter);

            if (clampRound1.intersectRoundedCut(cutRound) ||
                    clampRound2.intersectRoundedCut(cutRound) ||
                    clampRound3.intersectRoundedCut(cutRound) ||
                    clampRound4.intersectRoundedCut(cutRound)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check if a cut is in a clamp zone
     *
     * @param cut       The cut
     * @param clampZone The clamp zone
     * @return True if it is in the clamp zone, false otherwise
     */
    public boolean cutInClampZone(CNCMachine cncMachine, Cut cut, Cut clampZone) {
        double smolX = clampZone.getPoints().stream()
                .mapToDouble(VertexDTO::getX)
                .min()
                .orElse(0);
        double bigX = clampZone.getPoints().stream()
                .mapToDouble(VertexDTO::getX)
                .max()
                .orElse(Double.MAX_VALUE);
        double smoly = clampZone.getPoints().stream()
                .mapToDouble(VertexDTO::getY)
                .min()
                .orElse(0);
        double bigY = clampZone.getPoints().stream()
                .mapToDouble(VertexDTO::getY)
                .max()
                .orElse(Double.MAX_VALUE);

        VertexDTO topLeft = new VertexDTO(smolX, bigY,0);
        VertexDTO bottomRight = new VertexDTO(bigX, smoly,0);

        for (VertexDTO point : cut.getAbsolutePointsPosition(cncMachine)) {
            if (point.getX() >= topLeft.getX() &&
                    point.getX() <= bottomRight.getX() &&
                    point.getY() <= topLeft.getY() &&
                    point.getY() >= bottomRight.getY()) {
                return true;
            }
        }
        return false;
    }

}
