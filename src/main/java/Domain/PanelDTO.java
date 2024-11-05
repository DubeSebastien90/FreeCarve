package Domain;

import Domain.ThirdDimension.VertexDTO;

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
    private final List<CutDTO> cutList;
    private final VertexDTO panelDimension;

    public PanelDTO(PanelCNC domainPanel) {
        panelDimension = new VertexDTO(domainPanel.getPanelDimension());
        cutList = new ArrayList<>();
        for (Cut cDomain : domainPanel.getCutList()) {
            cutList.add(new CutDTO(cDomain));
        }
    }

    public List<CutDTO> getCutsDTO() {
        return cutList;
    }

    public VertexDTO getPanelDimension() {
        return panelDimension;
    }
}
