package Common.DTO;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import Common.Util;
import Domain.CutType;

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
    private CutDTO borderCut;
    private final double maxFeetWidth;
    private final double maxFeetHeight;

    public PanelDTO(List<CutDTO> cutList, VertexDTO panelDimension, double maxFeetWidth, double maxFeetHeight, UUID borderUUID) {
        this.cutList = cutList;
        this.panelDimension = panelDimension;
        this.maxFeetWidth = maxFeetWidth;
        this.maxFeetHeight = maxFeetHeight;

        ArrayList<VertexDTO> borderPoints = new ArrayList<>();
        borderPoints.add(new VertexDTO(0, 0,0 ));
        borderPoints.add(new VertexDTO(0, panelDimension.getY(), 0 ));
        borderPoints.add(new VertexDTO(panelDimension.getX(), panelDimension.getY(), 0 ));
        borderPoints.add(new VertexDTO(panelDimension.getX(), 0,0 ));
        borderPoints.add(new VertexDTO(0, 0,0 ));
        this.borderCut = new CutDTO(borderUUID, panelDimension.getZ(), 0, CutType.RECTANGULAR, borderPoints, new ArrayList<RefCutDTO>());
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
        return this.borderCut.getPoints();
    }

    public CutDTO getBorderCut(){
        return this.borderCut;
    }
}
