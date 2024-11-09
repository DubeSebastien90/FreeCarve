package Common.DTO;

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

    public PanelDTO(List<CutDTO> cutList, VertexDTO panelDimension) {
        this.cutList = cutList;
        this.panelDimension = panelDimension;
    }

    public List<CutDTO> getCutsDTO() {
        return cutList;
    }

    public VertexDTO getPanelDimension() {
        return panelDimension;
    }
}
