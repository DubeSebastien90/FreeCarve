package Domain;

import Common.DTO.BitDTO;
import Common.DTO.ProjectStateDTO;
import Common.DTO.VertexDTO;
import Common.Exceptions.InvalidBitException;
import Domain.Interfaces.IMemorizer;

import java.util.Arrays;

/**
 * The {@code ProjectState} class represent the current state of the project.
 *
 * @author Adam Côté
 * @version 1.0
 * @since 2024-10-20
 */
class ProjectState {
    private final Bit[] bitList;
    private PanelCNC panel;
    private static final VertexDTO defaultPanelDimension = new VertexDTO(1219.2, 914.4, 0); // dimension in mm
    private static final float defaultPanelDepth = 1.0f; // depth in mm
    private final IMemorizer memorizer;

    /**
     * Constructs a default new {@code ProjectState}.
     */
    ProjectState(IMemorizer memorizer) {
        this(Arrays.stream(new Bit[12]).map(bit -> new Bit()).toList().toArray(Bit[]::new), new PanelCNC(defaultPanelDimension, defaultPanelDepth, memorizer), memorizer);
    }

    /**
     * Constructs a new {@code ProjectState}.
     *
     * @param bitList The list of {@code Bit} of the CNC.
     * @param panel   The {@code PanelCNC} of the project.
     */
    ProjectState(Bit[] bitList, PanelCNC panel, IMemorizer memorizer) {
        this.bitList = bitList;
        bitList[0] = new Bit("Défaut", 0.5f);
        setPanel(panel);
        this.memorizer = memorizer;
    }

    public Bit[] getBitList() {
        return bitList;
    }

    public ProjectStateDTO getDTO() {
        return new ProjectStateDTO(Arrays.stream(bitList).map(bit -> (bit != null) ? bit.getDTO() : null).toList().toArray(new BitDTO[]{}), getPanel().getDTO());
    }

    /**
     * @param bit   The {@code Bit} that needs to be added to the bitList
     * @param index the index of this {@code Bit}.
     * @throws InvalidBitException When the index is not between 0 and 11
     */
    void setBit(Bit bit, int index) throws InvalidBitException {
        if (index < 0 || index > bitList.length)
            throw new InvalidBitException("L'index doit être entre 0 et 11");
        this.bitList[index] = bit;
    }

    public PanelCNC getPanel() {
        return panel;
    }

    void setPanel(PanelCNC panel) {
        this.panel = panel;
    }

    /**
     * Updates the bit at the specified position. Called when the user wants to
     * change the name or diameter of a bit.
     *
     * @param position The position of the bit in the bitList
     * @param bitDTO   The DTO of the bit
     */
    void updateBit(int position, BitDTO bitDTO) {
        if (position < 0 || position > bitList.length)
            throw new IllegalArgumentException("L'index doit être entre 0 et 11");

        if (bitList[position] == null) {
            bitList[position] = new Bit(bitDTO.getName(), bitDTO.getDiameter());
        } else {
            bitList[position].setName(bitDTO.getName());
            bitList[position].setDiameter(bitDTO.getDiameter());
        }
    }

    /**
     * Removes a bit of the bt list. The bit is not set to null, but to default.
     * @param position The position of the bit
     * @throws IndexOutOfBoundsException When the position is not between 0 and 11 inclusively
     * @throws InvalidBitException If the bit to remove is a default bit
     */
    void removeBit(int position) throws IndexOutOfBoundsException, InvalidBitException{
        if(position < 0 || position > 11)
            throw new IndexOutOfBoundsException("La position doit être entre 0 et 11 inclusivement");

        if(bitList[position].getDiameter() == 0)
            throw new InvalidBitException("Il doit y avoir un bit présent pour le supprimer");

        bitList[position] = new Bit();
    }
}
