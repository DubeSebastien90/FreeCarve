package Domain;
import Common.CutState;
import Common.DTO.*;
import Common.DTO.ProjectStateDTO;
import Common.DTO.RequestCutDTO;
import Common.DTO.VertexDTO;
import Common.Exceptions.ClampZoneException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Assertions;

public class CNCMachineTest {

    private CNCMachine cncMachine;

    @BeforeEach
    void setUp() {
        PanelCNC board = new PanelCNC(new VertexDTO(15.0, 15.0, 0.0),  new UndoRedoManager());
        cncMachine = new CNCMachine(new BitStorage(), board, new UndoRedoManager());
    }


    @Test
    void valid_board() {
        // Arrange
        PanelCNC panelCNC = new PanelCNC(new VertexDTO(12, 13, 0), new UndoRedoManager());
        BitDTO[] bitList = new BitDTO[12];

        // Act
        CNCMachine CNCMachine = new CNCMachine(new BitStorage(bitList), panelCNC, new UndoRedoManager());

        // Assert
        Assertions.assertEquals(CNCMachine.getPanel(), panelCNC);
    }

    @Test
    void resetPanelCNC_WhenCalled_ResetsPanel() throws Exception {
        // Arrange
        cncMachine.getBitStorage().setBit(new Bit("Test", 0.2f), 1);

        ArrayList<VertexDTO> pointList = new ArrayList<>();
        pointList.add(new VertexDTO(13, 14, 15));
        pointList.add(new VertexDTO(16, 17, 18));

        cncMachine.getPanel().requestCut(new RequestCutDTO(pointList, CutType.LINE_VERTICAL, 0, 0.0f, new ArrayList<RefCutDTO>()));
        Assertions.assertEquals(0.2f, cncMachine.getBitStorage().getBitList()[1].getDiameter());
        Assertions.assertEquals(1, cncMachine.getPanel().getCutList().size());

        // Act
        cncMachine.resetPanelCNC();

        // Assert
        Assertions.assertEquals(0.2f, cncMachine.getBitStorage().getBitList()[1].getDiameter());
        Assertions.assertEquals(0, cncMachine.getPanel().getCutList().size());
    }

    @Test
    void modifyCut_WhenNewCutIntersectsClamp_CutIsInvalid() throws ClampZoneException {
        // Arrange
        ClampZoneDTO clampZoneDTO = new ClampZoneDTO(new VertexDTO(0, 0, 0), new VertexDTO(1, 1, 1), Optional.empty());
        cncMachine.getPanel().addClamps(clampZoneDTO);

        ArrayList<VertexDTO> pointList = new ArrayList<>();
        pointList.add(new VertexDTO(2.5f, 2.5f, 0.5f));
        pointList.add(new VertexDTO(1.5f, 1.5f, 1.5f));
        RequestCutDTO rcDTO = new RequestCutDTO(pointList, CutType.LINE_VERTICAL, 0, 3.0f, new ArrayList<RefCutDTO>());
        Optional<UUID> id = cncMachine.requestCut(rcDTO);

        ArrayList<VertexDTO> newPointList = new ArrayList<>();
        newPointList.add(new VertexDTO(0.5f, 0.5f, 0.5f));
        newPointList.add(new VertexDTO(1.5f, 1.5f, 1.5f));
        CutDTO newRCDTO = new CutDTO(id.get(),new RequestCutDTO(newPointList, CutType.LINE_VERTICAL, 0, 3.0f, new ArrayList<RefCutDTO>()));

        // Act
        cncMachine.modifyCut(newRCDTO);

        // Assert
        Assertions.assertEquals(cncMachine.getPanel().getCutList().get(0).getCutState(), CutState.NOT_VALID);
    }

    @Test
    void modifyCut_WhenNewCutDoesntIntersectClamp_CutIsValid() throws ClampZoneException {
        // Arrange
        ClampZoneDTO clampZoneDTO = new ClampZoneDTO(new VertexDTO(0, 0, 0), new VertexDTO(1, 1, 1), Optional.empty());
        cncMachine.getPanel().addClamps(clampZoneDTO);

        ArrayList<VertexDTO> pointList = new ArrayList<>();
        pointList.add(new VertexDTO(2.5f, 2.5f, 0.5f));
        pointList.add(new VertexDTO(1.5f, 1.5f, 1.5f));
        RequestCutDTO rcDTO = new RequestCutDTO(pointList, CutType.LINE_VERTICAL, 0, 3.0f, new ArrayList<RefCutDTO>());
        Optional<UUID> id = cncMachine.requestCut(rcDTO);

        ArrayList<VertexDTO> newPointList = new ArrayList<>();
        newPointList.add(new VertexDTO(3.5f, 3.5f, 0.5f));
        newPointList.add(new VertexDTO(1.5f, 1.5f, 1.5f));
        CutDTO newRCDTO = new CutDTO(id.get(),new RequestCutDTO(newPointList, CutType.LINE_VERTICAL, 0, 3.0f, new ArrayList<RefCutDTO>()));

        // Act
        cncMachine.modifyCut(newRCDTO);

        // Assert
        Assertions.assertEquals(cncMachine.getPanel().getCutList().get(0).getCutState(), CutState.VALID);
    }

    @Test
    void addCut_WhenNewCutDoesntIntersectClamp_CutIsValid() throws ClampZoneException {
        // Arrange
        ClampZoneDTO clampZoneDTO = new ClampZoneDTO(new VertexDTO(0, 0, 0), new VertexDTO(1, 1, 1), Optional.empty());
        cncMachine.getPanel().addClamps(clampZoneDTO);

        ArrayList<VertexDTO> pointList = new ArrayList<>();
        pointList.add(new VertexDTO(2.5f, 2.5f, 0.5f));
        pointList.add(new VertexDTO(1.5f, 1.5f, 1.5f));
        RequestCutDTO rcDTO = new RequestCutDTO(pointList, CutType.LINE_VERTICAL, 0, 3.0f, new ArrayList<RefCutDTO>());

        // Act
        cncMachine.requestCut(rcDTO);

        // Assert
        Assertions.assertEquals(cncMachine.getPanel().getCutList().get(0).getCutState(), CutState.VALID);
    }

    @Test
    void addCut_WhenNewCutIntersectsClamp_CutIsInvalid() throws ClampZoneException {
        // Arrange
        ClampZoneDTO clampZoneDTO = new ClampZoneDTO(new VertexDTO(0, 0, 0), new VertexDTO(2, 2, 1), Optional.empty());
        cncMachine.getPanel().addClamps(clampZoneDTO);

        ArrayList<VertexDTO> pointList = new ArrayList<>();
        pointList.add(new VertexDTO(2.5f, 2.5f, 0.5f));
        pointList.add(new VertexDTO(1.5f, 1.5f, 1.5f));
        RequestCutDTO rcDTO = new RequestCutDTO(pointList, CutType.LINE_VERTICAL, 0, 3.0f, new ArrayList<RefCutDTO>());

        // Act
        cncMachine.requestCut(rcDTO);

        // Assert
        Assertions.assertEquals(cncMachine.getPanel().getCutList().get(0).getCutState(), CutState.NOT_VALID);
    }
}
