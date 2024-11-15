package Common.DTO;

import java.util.ArrayList;
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
    private final double depth;
    private final double maxFeetWidth;
    private final double maxFeetHeight;

    public PanelDTO(List<CutDTO> cutList, VertexDTO panelDimension, double depth, double maxFeetWidth, double maxFeetHeight) {
        this.cutList = cutList;
        this.panelDimension = panelDimension;
        this.maxFeetWidth = maxFeetWidth;
        this.maxFeetHeight = maxFeetHeight;
        this.depth = depth;
    }

    public List<CutDTO> getCutsDTO() {
        return cutList;
    }

    public VertexDTO getPanelDimension() {
        return panelDimension;
    }

    public double getMaxMMWidth() {
        return Util.feet_to_mm(maxFeetWidth);
    }

    public double getMaxMMHeight() {
        return Util.feet_to_mm(maxFeetHeight);
    }


    /**
     * Gets a list of the four points that represent the borders of the board
     * @return List<VertexDTO> of the four points of the board
     */
    public List<VertexDTO> getListBoardPointsDTO(){
        List<VertexDTO> list = new ArrayList<>();
        list.add(new VertexDTO(0.0f, 0.0f, depth));
        list.add(new VertexDTO(panelDimension.getX(), 0.0f, depth));
        list.add(new VertexDTO(panelDimension.getX(), panelDimension.getY(), depth));
        list.add(new VertexDTO(0.0f, panelDimension.getY(), depth));
        list.add(new VertexDTO(0.0f, 0.0f, depth));

        return list;
    }
}
