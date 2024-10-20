package Domain;

/**
 * The {@code ProjectState} class represent the current state of the project.
 *
 * @author Adam Côté
 * @version 1.0
 * @since 2024-10-20
 */
public class ProjectState {
    private final Bit[] bitList = new Bit[12];
    private PanelCNC board;

    /**
     * Constructs a new {@code ProjectState}.
     *
     * @param bitList The list of {@code Bit} of the CNC.
     * @param board   The {@code PanelCNC} of the project.
     */
    ProjectState(Bit[] bitList, PanelCNC board) {
        for (int i = 0; i < bitList.length; i++) {
            setBit(bitList[i], i);
            if (i == 11) {
                break;
            }
        }
        setBoard(board);
    }

    Bit[] getBitList() {
        return bitList;
    }

    /**
     * @param bit   The {@code Bit} that needs to be added to the bitList
     * @param index the index of this {@code Bit}.
     */
    void setBit(Bit bit, int index) {
        if (index < 12 && index > 0) {
            this.bitList[index] = bit;
        }
    }

    PanelCNC getBoard() {
        return board;
    }

    void setBoard(PanelCNC board) {
        this.board = board;
    }
}
