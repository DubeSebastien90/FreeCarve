package Common.DTO;

import java.util.List;

import Common.Util;

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
    private final double MAX_FEET_WIDTH;
    private final double MAX_FEET_HEIGHT;

    public PanelDTO(List<CutDTO> cutList, VertexDTO panelDimension, double MAX_FEET_WIDTH, double MAX_FEET_HEIGHT) {
        this.cutList = cutList;
        this.panelDimension = panelDimension;
        this.MAX_FEET_WIDTH = MAX_FEET_WIDTH;
        this.MAX_FEET_HEIGHT = MAX_FEET_HEIGHT;
    }

    public List<CutDTO> getCutsDTO() {
        return cutList;
    }

    public VertexDTO getPanelDimension() {
        return panelDimension;
    }

    public double getMaxMMWidth() {
        return Util.feet_to_mm(MAX_FEET_WIDTH);
    }

    public double getMaxMMHeight() {
        return Util.feet_to_mm(MAX_FEET_HEIGHT);
    }
}
