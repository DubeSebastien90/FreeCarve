package Domain;

import Common.DTO.CutDTO;
import Common.DTO.RequestCutDTO;
import Common.Interfaces.IMemorizer;

import java.util.Optional;
import java.util.UUID;

/**
 * Class that represents the CNC machine
 *
 * @author Adam Côté
 * @author Kamran Charles Nayebi
 * @since 2024-10-20
 */
class CNCMachine {
    private PanelCNC panel;
    private BitStorage bitStorage;
    private final IMemorizer memorizer;

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
     * @param panel   The panel.
     */
    CNCMachine(BitStorage bitStorage, PanelCNC panel, IMemorizer memorizer) {
        this.panel = panel;
        setBitStorage(bitStorage);
        this.memorizer = memorizer;
    }

    public PanelCNC getPanel() {
        return panel;
    }

    void setPanel(PanelCNC panel) {
        this.panel = panel;
        this.panel.validateCuts(this);
    }

    public BitStorage getBitStorage() {
        return bitStorage;
    }

    public void setBitStorage(BitStorage bitStorage) {
        this.bitStorage = bitStorage;
        this.panel.validateCuts(this);
    }

    public Optional<UUID> requestCut(RequestCutDTO requestCutDTO){
        Optional<UUID> id = panel.requestCut(requestCutDTO);
        this.panel.verifyCuts(this);
        return id;
    }

    public Optional<UUID> modifyCut(CutDTO cutDTO){
        Optional<UUID> id = panel.modifyCut(cutDTO);
        this.panel.verifyCuts(this);
        return id;
    }

    public double edgeEdgeToCenterCenter(double edgeEdge, int bitIndex1, int bitIndex2){

        double bitDiameter1 = bitStorage.getBitDiameter(bitIndex1);
        double bitDiameter2 = bitStorage.getBitDiameter(bitIndex2);
        if(edgeEdge < 0){
            return edgeEdge - bitDiameter1/2 -bitDiameter2/2;
        }
        else{
            return edgeEdge + bitDiameter1/2 + bitDiameter2/2;
        }

    }

    public double centerCenterToEdgeEdge(double centerCenter, int bitIndex1, int bitIndex2){
        double bitDiameter1 = bitStorage.getBitDiameter(bitIndex1);
        double bitDiameter2 = bitStorage.getBitDiameter(bitIndex2);

        if(centerCenter > 0){
            return centerCenter - bitDiameter1/2 -bitDiameter2/2;
        }
        else{
            return centerCenter + bitDiameter1/2 + bitDiameter2 /2;
        }
    }

    public void resetPanelCNC(){
        PanelCNC copy = this.panel;
        memorizer.executeAndMemorize(() -> {
            this.panel = new PanelCNC(memorizer);
        }, () -> {
            this.panel = copy;
        });
    }
}
