package Domain;

import Common.DTO.PanelDTO;
import Common.DTO.RequestCutDTO;
import Common.DTO.VertexDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class PanelCNCTest {
    private PanelCNC panelCNC;
    private VertexDTO boardDimension;
    private float depth;

    @BeforeEach
    void SetUp(){
        boardDimension = new VertexDTO(1,2,3);
        depth = 13.0f;
        panelCNC = new PanelCNC(boardDimension, depth);
    }

    @Test
    void constructor_with_dimension_and_depth_postconditions(){
        // Arrange - BeforeEach

        // Act

        // Assert
        Assertions.assertEquals(panelCNC.getWidth(), 1);
        Assertions.assertEquals(panelCNC.getHeight(), 2);
        Assertions.assertEquals(panelCNC.getPanelDimension().getX(), 1);
        Assertions.assertEquals(panelCNC.getPanelDimension().getY(), 2);
        Assertions.assertEquals(panelCNC.getCutList().size(), 0);
        Assertions.assertEquals(panelCNC.getDepth(), 13.0f);
    }

    @Test
    void add_new_cut(){
        // Arrange - BeforeEach
        Assertions.assertEquals(panelCNC.getCutList().size(), 0);

        // Act
        ArrayList<VertexDTO> pointList = new ArrayList<>();
        pointList.add(new VertexDTO(13, 14, 15));
        pointList.add(new VertexDTO(16, 17, 18));
        RequestCutDTO rcDTO = new RequestCutDTO(pointList, CutType.LINE_VERTICAL, 0, 3.0f);
        panelCNC.requestCut(rcDTO);
        ArrayList<Cut> refCutList = (ArrayList<Cut>) panelCNC.getCutList();

        // Assert
        Assertions.assertEquals(panelCNC.getCutList().size(), 1);
        Assertions.assertEquals(refCutList.getFirst().getStartPoint().getX(), 13);
        Assertions.assertEquals(refCutList.getFirst().getStartPoint().getY(), 14);
        Assertions.assertEquals(refCutList.getFirst().getStartPoint().getZ(), 15);
    }



    @Test
    void valid_panelDTO_return(){
        // Arrange
        ArrayList<VertexDTO> pointList = new ArrayList<>();
        pointList.add(new VertexDTO(13, 14, 15));
        pointList.add(new VertexDTO(16, 17, 18));

        // Act
        Assertions.assertEquals(panelCNC.getCutList().size(), 0);
        RequestCutDTO rcDTO = new RequestCutDTO(pointList, CutType.LINE_VERTICAL, 0, 3.0f);
        panelCNC.requestCut(rcDTO);
        PanelDTO pDTO = panelCNC.getDTO();

        // Assert
        Assertions.assertEquals(pDTO.getCutsDTO().size(), panelCNC.getCutList().size());
        Assertions.assertEquals(pDTO.getCutsDTO().getFirst().getPoints().size(), 2);
        Assertions.assertEquals(pDTO.getCutsDTO().getFirst().getPoints().getFirst().getX(), 13);
        Assertions.assertEquals(pDTO.getCutsDTO().getFirst().getPoints().getFirst().getY(), 14);
        Assertions.assertEquals(pDTO.getCutsDTO().getFirst().getPoints().getFirst().getZ(), 15);
    }
}
