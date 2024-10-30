package Domain;

import Domain.BitDTO;
import Domain.ProjectStateDTO;
import Domain.ThirdDimension.Vertex;
import Domain.ThirdDimension.VertexDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Objects;

import org.junit.jupiter.api.Assertions;

public class ProjectStateTest {

    private ProjectState stateTest;

    @BeforeEach
    void setUp() {
        Bit[] bitList = new Bit[12];
        PanelCNC board = new PanelCNC(new Vertex(15.0, 15.0, 0.0), 5);
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
    void getCurrentStateDTO_WhenCalled_ReturnDTO() {
        // Arrange
        // Act
        ProjectStateDTO dto = stateTest.getCurrentStateDTO();
        // Assert
        Assertions.assertEquals(ProjectStateDTO.class, dto.getClass());
    }

    @Test
    void valid_board(){
        PanelCNC panelCNC = new PanelCNC(new Vertex(12, 13, 0), 5.0f);
        Bit[] bitList = new Bit[12];
        ProjectState projectState = new ProjectState(bitList, panelCNC);
        Assertions.assertEquals(projectState.getBoard(), panelCNC);
    }

    @Test
    void valid_projectStateDTO() throws Exception {

        Assertions.assertEquals(stateTest.getCurrentStateDTO().getClass(), ProjectStateDTO.class);

        stateTest.setBit(new Bit("Test", 3.0f), 3);
        ProjectStateDTO test = stateTest.getCurrentStateDTO();
        Assertions.assertEquals(test.getBitList()[3].getName(), "Test");
        Assertions.assertEquals(test.getBitList()[3].getDiameter(), 3.0f);

        ArrayList<VertexDTO> points = new ArrayList<>();
        points.add(new VertexDTO(45, 46, 47));
        points.add(new VertexDTO(50, 46, 47));
        stateTest.getBoard().newCut(new RequestCutDTO(points, CutType.LINE_VERTICAL, 5,  2.0f));
        Assertions.assertEquals(stateTest.getCurrentStateDTO().getBoardDTO().getCutsDTO().size(), 1);
        Assertions.assertEquals(stateTest.getCurrentStateDTO().getBoardDTO().getCutsDTO().getFirst().getPoints().getFirst().getX(), 45);
        Assertions.assertEquals(stateTest.getCurrentStateDTO().getBoardDTO().getCutsDTO().getFirst().getPoints().getFirst().getY(), 46);
        Assertions.assertEquals(stateTest.getCurrentStateDTO().getBoardDTO().getCutsDTO().getFirst().getPoints().getFirst().getZ(), 47);

    }
}
