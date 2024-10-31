package Domain;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is a DTO wrapper of the {@code Panel} class in order to transfer READ-ONLY informations
 *
 * @author Louis-Etienne Messier
 * @version 1.0
 * @since 2024-10-12
 */
public class PanelDTO {
    private final List<CutDTO> cutsDTO;

    public PanelDTO(PanelCNC domainPanel){
        cutsDTO = new ArrayList<>();
        for(Cut cDomain : domainPanel.getCutList()){
            cutsDTO.add(new CutDTO(cDomain));
        }
    }
    public PanelDTO(List<CutDTO> cutsDTO) {
        this.cutsDTO = cutsDTO;
    }

    public List<CutDTO> getCutsDTO() {
        return cutsDTO;
    }
}
