package Domain;

import Common.DTO.BitDTO;
import Common.DTO.ProjectStateDTO;
import Common.DTO.RequestCutDTO;
import Common.DTO.VertexDTO;
import Common.Exceptions.InvalidBitException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import org.junit.jupiter.api.Assertions;

public class ProjectStateTest {

    private ProjectState stateTest;

    @BeforeEach
    void setUp() {
        Bit[] bitList = new Bit[12];
        PanelCNC board = new PanelCNC(new VertexDTO(15.0, 15.0, 0.0), 5, new UndoRedoManager());
        stateTest = new ProjectState(bitList, board, new UndoRedoManager());
    }

    @Test
    void testConstructor() {
        // Arrange
        // Act
        // Assert
        Assertions.assertEquals(12, stateTest.getBitList().length);

        // Check that the default bit is created
        Assertions.assertEquals(0.5f, stateTest.getBitList()[0].getDiameter());
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
    void valid_board() {
        // Arrange
        PanelCNC panelCNC = new PanelCNC(new VertexDTO(12, 13, 0), 5.0f, new UndoRedoManager());
        Bit[] bitList = new Bit[12];

        // Act
        ProjectState projectState = new ProjectState(bitList, panelCNC, new UndoRedoManager());

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
        stateTest.getPanel().requestCut(new RequestCutDTO(points, CutType.LINE_VERTICAL, 5, 2.0f));


        // Assert
        Assertions.assertEquals(test.getBitList()[3].getName(), "Test");
        Assertions.assertEquals(test.getBitList()[3].getDiameter(), 3.0f);

        Assertions.assertEquals(stateTest.getDTO().getClass(), ProjectStateDTO.class);

        Assertions.assertEquals(stateTest.getDTO().getPanelDTO().getCutsDTO().size(), 1);
        Assertions.assertEquals(stateTest.getDTO().getPanelDTO().getCutsDTO().getFirst().getPoints().getFirst().getX(), 45);
        Assertions.assertEquals(stateTest.getDTO().getPanelDTO().getCutsDTO().getFirst().getPoints().getFirst().getY(), 46);
        Assertions.assertEquals(stateTest.getDTO().getPanelDTO().getCutsDTO().getFirst().getPoints().getFirst().getZ(), 47);

    }

    @Test
    void removeBit_WhenBitIsValid_SetsBitToDefault() throws Exception {
        // Arrange
        stateTest.setBit(new Bit("Test", 0.5f), 0);
        Bit defaultBit = new Bit();

        // Act
        stateTest.removeBit(0);

        // Assert
        Assertions.assertEquals(stateTest.getBitList()[0].getDiameter(), defaultBit.getDiameter());
        Assertions.assertEquals(stateTest.getBitList()[0].getName(), defaultBit.getName());
    }

    @Test
    void removeBit_WhenPositionNegative_ThrowsIndexOutOfBoundException(){
        // Arrange

        // Act

        // Assert
        Assertions.assertThrows(IndexOutOfBoundsException.class, ()-> {stateTest.removeBit(-1);});
    }

    @Test
    void removeBit_WhenPositionBiggerThen12_ThrowsIndexOutOfBoundException(){
        // Arrange

        // Act

        // Assert
        Assertions.assertThrows(IndexOutOfBoundsException.class, ()-> {stateTest.removeBit(12);});
    }

    @Test
    void removeBit_WhenRemoveBitIsDefault_ThrowsInvalidBitException() throws Exception {
        // Arrange

        // Act
        stateTest.setBit(new Bit(), 1);

        // Assert
        Assertions.assertThrows(InvalidBitException.class, ()-> {stateTest.removeBit(1);});
    }
}
