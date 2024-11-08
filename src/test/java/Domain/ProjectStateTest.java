package Domain;

import Common.BitDTO;
import Common.ProjectStateDTO;
import Common.RequestCutDTO;
import Common.VertexDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import org.junit.jupiter.api.Assertions;

public class ProjectStateTest {

    private ProjectState stateTest;

    @BeforeEach
    void setUp() {
        Bit[] bitList = new Bit[12];
        PanelCNC board = new PanelCNC(new VertexDTO(15.0, 15.0, 0.0), 5);
        stateTest = new ProjectState(bitList, board);
    }

    @Test
    void testConstructor() {
        // Arrange
        // Act
        // Assert
        Assertions.assertEquals(12, stateTest.getBitList().length);
    }

    @Test
    void updateBit_WhenPositionNegative_ThrowsException() {
        // Arrange
        BitDTO oldBit = new BitDTO("test", 1.0f);
        // Act
        // Assert
        Assertions.assertThrows(Exception.class, () -> {
            stateTest.updateBit(-1, oldBit);
        });
    }

    @Test
    void updateBit_WhenNameEmpty_ThrowsException() {
        // Arrange
        BitDTO newBit = new BitDTO("", 1.0f);
        // Act
        // Assert
        Assertions.assertThrows(Exception.class, () -> {
            stateTest.updateBit(0, newBit);
        });
    }

    @Test
    void updateBit_WhenBitCorrect_BitUpdated() {
        // Arrange
        BitDTO newBit = new BitDTO("test", 1.0f);
        // Act
        stateTest.updateBit(0, newBit);
        // Assert
        Assertions.assertEquals("test", stateTest.getBitList()[0].getName());
        Assertions.assertEquals(1.0f, stateTest.getBitList()[0].getDiameter());
    }

    @Test
    void getDTO_WhenCalled_ReturnDTO() {
        // Arrange
        // Act
        ProjectStateDTO dto = stateTest.getDTO();
        // Assert
        Assertions.assertEquals(ProjectStateDTO.class, dto.getClass());
    }

    @Test
    void valid_board(){
        // Arrange
        PanelCNC panelCNC = new PanelCNC(new VertexDTO(12, 13, 0), 5.0f);
        Bit[] bitList = new Bit[12];

        // Act
        ProjectState projectState = new ProjectState(bitList, panelCNC);

        // Assert
        Assertions.assertEquals(projectState.getPanel(), panelCNC);
    }

    @Test
    void valid_projectStateDTO() throws Exception {
        // Arrange
        stateTest.setBit(new Bit("Test", 3.0f), 3);
        ProjectStateDTO test = stateTest.getDTO();

        ArrayList<VertexDTO> points = new ArrayList<>();
        points.add(new VertexDTO(45, 46, 47));
        points.add(new VertexDTO(50, 46, 47));

        // Act
        stateTest.getPanel().requestCut(new RequestCutDTO(points, CutType.LINE_VERTICAL, 5,  2.0f));


        // Assert
        Assertions.assertEquals(test.getBitList()[3].getName(), "Test");
        Assertions.assertEquals(test.getBitList()[3].getDiameter(), 3.0f);

        Assertions.assertEquals(stateTest.getDTO().getClass(), ProjectStateDTO.class);

        Assertions.assertEquals(stateTest.getDTO().getPanelDTO().getCutsDTO().size(), 1);
        Assertions.assertEquals(stateTest.getDTO().getPanelDTO().getCutsDTO().getFirst().getPoints().getFirst().getX(), 45);
        Assertions.assertEquals(stateTest.getDTO().getPanelDTO().getCutsDTO().getFirst().getPoints().getFirst().getY(), 46);
        Assertions.assertEquals(stateTest.getDTO().getPanelDTO().getCutsDTO().getFirst().getPoints().getFirst().getZ(), 47);

    }
}
