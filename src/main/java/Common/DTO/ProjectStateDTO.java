package Common.DTO;

/**
 * The {@code ProjectStateDTO} class is a read-only {@code ProjectState}
 *
 * @author Adam Côté
 * @version 1.0
 * @since 2024-10-20
 */
public class ProjectStateDTO {
    private final BitDTO[] bitList = new BitDTO[12];
    private final PanelDTO panel;

    public ProjectStateDTO(BitDTO[] bitList, PanelDTO panel) {
        for (int i = 0; i < bitList.length; i++) {
            this.bitList[i] = bitList[i];
        }
        this.panel = panel;
    }

    public BitDTO[] getBitList() {
        return bitList;
    }

    public PanelDTO getPanelDTO() {
        return panel;
    }
}
