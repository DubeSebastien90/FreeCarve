package Domain;

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
        PanelCNC board = new PanelCNC(new Vertex(0,0,0), new Vertex(15.0, 15.0, 0.0), 5);
        stateTest = new ProjectState(bitList, board);
    }

    @Test
    void testConstructor() {
        Bit[] bitList = new Bit[12];
        PanelCNC board = new PanelCNC(new Vertex(0,0,0), new Vertex(15.0, 15.0, 0.0), 5);
        ProjectState projectState = new ProjectState(bitList, board);

        Assertions.assertEquals(12, projectState.getBitList().length);
    }

    @Test
    void updateBit_WhenPositionNegative_ThrowsException() {
        Assertions.assertThrows(Exception.class, () -> {
            stateTest.updateBit(-1, "test", 1.0f);
        });
    }

    @Test
    void updateBit_WhenNameEmpty_ThrowsException() {
        Assertions.assertThrows(Exception.class, () -> {
            stateTest.updateBit(0, "", 1.0f);
        });
    }

    @Test
    void updateBit_WhenBitCorrect_BitUpdated() {
        stateTest.updateBit(0, "test", 1.0f);
        Assertions.assertEquals("test", stateTest.getBitList()[0].getName());
        Assertions.assertEquals(1.0f, stateTest.getBitList()[0].getDiameter());
    }
}
