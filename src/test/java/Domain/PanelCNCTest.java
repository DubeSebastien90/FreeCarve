package Domain;

import Common.DTO.*;
import Common.Exceptions.ClampZoneException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Optional;
import java.util.UUID;

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
        ArrayList<VertexDTO> pointList = new ArrayList<>();
        pointList.add(new VertexDTO(13, 14, 15));
        pointList.add(new VertexDTO(16, 17, 18));

        RequestCutDTO rcDTO = new RequestCutDTO(pointList, CutType.LINE_VERTICAL, 0, 3.0f, new ArrayList<RefCutDTO>());
        panelCNC.requestCut(rcDTO);

        PanelDTO panelDTO = panelCNC.getDTO();

        // Act
        PanelCNC result = new PanelCNC(panelDTO, new UndoRedoManager());
        // Assert
        Assertions.assertEquals(panelCNC.getWidth(), result.getWidth());
        Assertions.assertEquals(panelCNC.getHeight(), result.getHeight());
        Assertions.assertEquals(panelCNC.getPanelDimension(), result.getPanelDimension());
        Assertions.assertEquals(panelCNC.getCutList(), result.getCutList());
        Assertions.assertEquals(panelCNC.getDepth(), result.getDepth());
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

    @Test
    void addClamp_WhenClampValid_AddsToClampList() throws ClampZoneException {
        // Arrange
        ClampZoneDTO clampZoneDTO = new ClampZoneDTO(new VertexDTO(0, 0, 0), new VertexDTO(1, 1, 1), Optional.empty());

        // Act
        panelCNC.addClamps(clampZoneDTO);

        // Assert
        Assertions.assertEquals(panelCNC.getClamps().size(), 1);
    }

    @Test
    void addClamp_WhenPointOutsidePanel_IDisEmpty() throws ClampZoneException {
        // Arrange
        ClampZoneDTO clampZoneDTO = new ClampZoneDTO(new VertexDTO(-1f, -1f, 0), new VertexDTO(1, 1, 1), Optional.empty());

        // Act
        Optional<UUID> id = panelCNC.addClamps(clampZoneDTO);

        // Assert
        Assertions.assertEquals(id, Optional.empty());
    }

    @Test
    void addClamps_WhenClampInvalid_ThrowsClampZoneException(){
        // Arrange
        ClampZoneDTO clampZoneDTO = new ClampZoneDTO(new VertexDTO(1f, 1f, 1), new VertexDTO(1, 1, 1), Optional.empty());

        // Act
        Assertions.assertThrows(ClampZoneException.class, () -> panelCNC.addClamps(clampZoneDTO));
    }

    @Test
    void removeClamps_WhenClampExists_RemovesFromClampList() throws ClampZoneException {
        // Arrange
        ClampZoneDTO clampZoneDTO = new ClampZoneDTO(new VertexDTO(0, 0, 0), new VertexDTO(1, 1, 1), Optional.empty());
        Optional<UUID> newID = panelCNC.addClamps(clampZoneDTO);

        // Act
        Assertions.assertTrue(panelCNC.removeClamp(newID.orElse(null)));

        // Assert
        Assertions.assertEquals(panelCNC.getClamps().size(), 0);
    }

    @Test
    void removeClamps_WhenClampDoentExist_returnsFalse(){
        // Arrange
        int sizeBeginning = panelCNC.getClamps().size();

        // Act
        boolean result = panelCNC.removeClamp(UUID.randomUUID());

        // Assert
        Assertions.assertFalse(result);
        Assertions.assertEquals(sizeBeginning, panelCNC.getClamps().size());
    }

    @Test
    void removeClamps_WhenUUIDNull_returnsFalse(){
        // Arrange
        int sizeBeginning = panelCNC.getClamps().size();

        // Act
        boolean result = panelCNC.removeClamp(null);

        // Assert
        Assertions.assertFalse(result);
        Assertions.assertEquals(sizeBeginning, panelCNC.getClamps().size());
    }

    @Test
    void modifyClamp_WhenClampExist_Modify() throws ClampZoneException {
        // Arrange
        ClampZoneDTO newClamp = new ClampZoneDTO(new VertexDTO(0.0f, 0.0f, 0.0f),
                            new VertexDTO(0.1f, 0.1f, 0.1f),
                        Optional.empty());

        UUID newID = panelCNC.addClamps(newClamp).get();

        // Act
        panelCNC.modifyClamp(new ClampZoneDTO(new VertexDTO(0.0f, 0.0f, 0.0f),
                new VertexDTO(0.2f, 0.2f, 0.2f),
                Optional.of(newID)));
        // Assert
        Assertions.assertEquals(0.2f, panelCNC.getClamps().get(0).getZone()[1].getX());
        Assertions.assertEquals(0.2f, panelCNC.getClamps().get(0).getZone()[1].getX());
    }
}
