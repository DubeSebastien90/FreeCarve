package Domain;

import java.util.ArrayList;

/**
 * This class is a DTO wrapper of the {@code Panel} class in order to transfer READ-ONLY informations
 * @author Louis-Etienne Messier
 * @version 1.0
 * @since 2024-10-12
 */
public class PanelDTO {
    private ArrayList<CutDTO> cutsDTO;

    public PanelDTO(){}
}
