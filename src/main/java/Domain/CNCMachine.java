package Domain;

import Common.CutState;
import Common.DTO.CutDTO;
import Common.DTO.RequestCutDTO;
import Common.DTO.VertexDTO;
import Common.Interfaces.IMemorizer;
import Common.Interfaces.IRefreshable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Class that represents the CNC machine
 *
 * @author Adam Côté
 * @author Kamran Charles Nayebi
 * @since 2024-10-20
 */
class CNCMachine implements IRefreshable {
    private PanelCNC panel;
    private BitStorage bitStorage;
    private final IMemorizer memorizer;
    private int cuttingSpeed = 3; // in mm per minute
    private int rotationSpeed = 20000; // in revolution per second

    /**
     * Constructs a default new {@code CNCMachine}.
     */
    CNCMachine(IMemorizer memorizer) {
        this(new BitStorage(), new PanelCNC(memorizer), memorizer);
    }

    /**
     * Constructs a new {@code CNCMachine}.
     *
     * @param bitStorage The storage for the bits
     * @param panel      The panel.
     */
    CNCMachine(BitStorage bitStorage, PanelCNC panel, IMemorizer memorizer) {
        this.panel = panel;
        setBitStorage(bitStorage);
        this.memorizer = memorizer;
    }

    public int getCuttingSpeed() {
        return cuttingSpeed;
    }

    public void setCuttingSpeed(int cuttingSpeed) {
        this.cuttingSpeed = cuttingSpeed;
    }

    public int getRotationSpeed() {
        return rotationSpeed;
    }

    public void setRotationSpeed(int rotationSpeed) {
        this.rotationSpeed = rotationSpeed;
    }

    public PanelCNC getPanel() {
        return panel;
    }

    void setPanel(PanelCNC panel) {
        this.panel = panel;
        this.panel.validateAll(this);
    }

    public BitStorage getBitStorage() {
        return bitStorage;
    }

    public void setBitStorage(BitStorage bitStorage) {
        this.bitStorage = bitStorage;
        this.panel.validateAll(this);
    }

    public Optional<UUID> requestCut(RequestCutDTO requestCutDTO) {
        Optional<UUID> id = panel.requestCut(this, requestCutDTO);
        return id;
    }

    public Optional<UUID> modifyCut(CutDTO cutDTO,  boolean canMemorize) {
        Optional<UUID> id = panel.modifyCut(this, cutDTO, canMemorize);
        return id;
    }

    public void resetPanelCNC() {
        PanelCNC copy = this.panel;
        memorizer.executeAndMemorize(() -> {
            this.panel = new PanelCNC(memorizer);
        }, () -> {
            this.panel = copy;

        });
    }


    /**
     * From a CutDTO, returns it's absolute points position
     *
     * @param cutDTO
     * @return
     */
    public List<VertexDTO> getAbsolutePointsPositionOfCutDTO(CutDTO cutDTO) {
        Cut c = this.getPanel().createPanelCut(cutDTO);
        return c.getAbsolutePointsPosition(this);
    }

    @Override
    public void refresh() {
        panel.validateAll(this);
    }
}
