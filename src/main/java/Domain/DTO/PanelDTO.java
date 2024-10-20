package Domain.DTO;

import java.util.ArrayList;

/**
 * This class is a DTO wrapper of the {@code Panel} class in order to transfer READ-ONLY informations
 *
 * @author Louis-Etienne Messier
 * @version 1.0
 * @since 2024-10-12
 */
public class PanelDTO {
    private final ArrayList<CutDTO> cutsDTO;

    public PanelDTO(ArrayList<CutDTO> cutsDTO) {
        this.cutsDTO = cutsDTO;
    }

    public ArrayList<CutDTO> getCutsDTO() {
        return cutsDTO;
    }
}
