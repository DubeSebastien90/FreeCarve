package Domain;

import Domain.BitDTO;
import Domain.ProjectStateDTO;

import Domain.ThirdDimension.Vertex;

/**
 * The {@code ProjectState} class represent the current state of the project.
 *
 * @author Adam Côté
 * @version 1.0
 * @since 2024-10-20
 */
class ProjectState {
    private final Bit[] bitList = new Bit[12];
    private PanelCNC board;
    private final Vertex defaultBoardDimension = new Vertex(0.9144, 1.2192, 0);
    private final float defaultBoardDepth = 1.0f;

    /**
     * Constructs a default new {@code ProjectState}.
     *
     */
    ProjectState(){
        for (int i =0; i < bitList.length; i++){
            bitList[i] = new Bit("Null", -1);
        }
        board = new PanelCNC(defaultBoardDimension, defaultBoardDepth);
    }
    /**
     * Constructs a new {@code ProjectState}.
     *
     * @param bitList The list of {@code Bit} of the CNC.
     * @param board   The {@code PanelCNC} of the project.
     */
    ProjectState(Bit[] bitList, PanelCNC board) {
        try {
            for (int i = 0; i < bitList.length; i++) {
                setBit(bitList[i], i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        setBoard(board);
    }

    public Bit[] getBitList() {
        return bitList;
    }

    /**
     * @param bit   The {@code Bit} that needs to be added to the bitList
     * @param index the index of this {@code Bit}.
     */
    void setBit(Bit bit, int index) throws Exception {
        if (index < 0 || index > bitList.length)
            throw new Exception("L'index doit être entre 0 et 11");
        this.bitList[index] = bit;
    }

    public PanelCNC getBoard() {
        return board;
    }

    void setBoard(PanelCNC board) {
        this.board = board;
    }

    /**
     * Updates the bit at the specified position. Called when the user wants to
     * change the name or diameter of a bit.
     *
     * @param position The position of the bit in the bitList
     * @param bitDTO The DTO of the bit
     */
    void updateBit(int position, BitDTO bitDTO) {
        if (position < 0 || position > bitList.length)
            throw new IllegalArgumentException("L'index doit être entre 0 et 11");

        if(bitList[position] == null){
            bitList[position] = new Bit(bitDTO.getName(), bitDTO.getDiameter());
        }
        else {
            bitList[position].setName(bitDTO.getName());
            bitList[position].setDiameter(bitDTO.getDiameter());
        }
    }

    public ProjectStateDTO getCurrentStateDTO(){
        BitDTO[] bitDTOList = new BitDTO[12];
        for(int i = 0; i < bitList.length; i++){
            if(bitList[i] == null){
                bitDTOList[i] = new BitDTO(new Bit());
                continue;
            }
            bitDTOList[i] = bitList[i].getBitDTO();
        }
        return new ProjectStateDTO(bitDTOList, board.getPanelDTO());
    }
}
