package Domain;

import Domain.DTO.BitDTO;
import Domain.DTO.ProjectStateDTO;
import Domain.ThirdDimension.Vertex;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
}
