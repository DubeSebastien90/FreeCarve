package Domain;

import Domain.BitDTO;
import Domain.ProjectStateDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class ControllerTest {

    private static Controller controllerTest;

    @BeforeAll
    static void setup(){
        controllerTest = new Controller();
    }

    @Test
    void constructorTest(){
        // Arrange
        ProjectStateDTO state = controllerTest.getProjectState();

        // Act

        // Assert
        Assertions.assertEquals(12, state.getBitList().length);
    }

    @Test
    void updateBitTest_WhenUpdateName_BitNameChange(){
        // Arrange
        BitDTO newBit = new BitDTO("test", 1.0f);
        // Act
        controllerTest.modifyBit(0, newBit);
        ProjectStateDTO state = controllerTest.getProjectState();

        // Assert
        Assertions.assertEquals("test", state.getBitList()[0].getName());
    }
}
