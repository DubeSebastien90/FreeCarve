package Domain.DTO;

/**
 * The {@code ProjectStateDTO} class is a read-only {@code ProjectState}
 *
 * @author Adam Côté
 * @version 1.0
 * @since 2024-10-20
 */
public class ProjectStateDTO {
    private final BitDTO[] bitList = new BitDTO[12];
    private final PanelDTO board;

    public ProjectStateDTO(BitDTO[] bitList, PanelDTO board) {
        for (int i = 0; i < bitList.length; i++) {
            this.bitList[i] = bitList[i];
        }
        this.board = board;
    }

    public BitDTO[] getBitList() {
        return bitList;
    }

    public PanelDTO getBoard() {
        return board;
    }
}
