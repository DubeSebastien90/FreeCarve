package Domain;

import Common.DTO.PanelDTO;
import Common.DTO.RefCutDTO;
import Common.DTO.RequestCutDTO;
import Common.DTO.VertexDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.LinkedList;

public class PanelCNCTest {
    private PanelCNC panelCNC;
    private VertexDTO boardDimension;
    private float depth;

    @BeforeEach
    void SetUp(){
        boardDimension = new VertexDTO(1,2,3);
        panelCNC = new PanelCNC(boardDimension, new UndoRedoManager());
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
        Assertions.assertEquals(panelCNC.getDepth(), 3);
    }

    @Test
    void constructor_WithDTO_BuildsCorrectPanel(){
        // Arrange
        PanelDTO panelDTO = panelCNC.getDTO();
        // Act
        PanelCNC result = new PanelCNC(panelDTO, new UndoRedoManager());
        // Assert
        Assertions.assertEquals(panelCNC.getWidth(), result.getWidth());
        Assertions.assertEquals(panelCNC.getHeight(), result.getHeight());
        Assertions.assertEquals(panelCNC.getPanelDimension().getX(), result.getPanelDimension().getX());
        Assertions.assertEquals(panelCNC.getPanelDimension().getY(), result.getPanelDimension().getY());
        Assertions.assertEquals(panelCNC.getCutList(), result.getCutList());
        Assertions.assertEquals(panelCNC.getDepth(), result.getDepth());
        Assertions.assertEquals(panelCNC, result);
    }

    @Test
    void add_new_cut(){
        // Arrange - BeforeEach
        Assertions.assertEquals(panelCNC.getCutList().size(), 0);

        // Act
        ArrayList<VertexDTO> pointList = new ArrayList<>();
        pointList.add(new VertexDTO(13, 14, 15));
        pointList.add(new VertexDTO(16, 17, 18));
        RequestCutDTO rcDTO = new RequestCutDTO(pointList, CutType.LINE_VERTICAL, 0, 3.0f, new ArrayList<RefCutDTO>());
        panelCNC.requestCut(rcDTO);
        ArrayList<Cut> refCutList = (ArrayList<Cut>) panelCNC.getCutList();

        // Assert
        Assertions.assertEquals(panelCNC.getCutList().size(), 1);
    }

    @Test
    void valid_panelDTO_return(){
        // Arrange
        ArrayList<VertexDTO> pointList = new ArrayList<>();
        pointList.add(new VertexDTO(13, 14, 15));
        pointList.add(new VertexDTO(16, 17, 18));

        // Act
        Assertions.assertEquals(panelCNC.getCutList().size(), 0);
        RequestCutDTO rcDTO = new RequestCutDTO(pointList, CutType.LINE_VERTICAL, 0, 3.0f, new ArrayList<RefCutDTO>());
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
