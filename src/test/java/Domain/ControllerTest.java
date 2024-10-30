package Domain;

import Domain.ThirdDimension.Vertex;
import Domain.ThirdDimension.VertexDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.UUID;

public class ControllerTest {

    private static Controller controllerTest;

    @BeforeAll
    static void setup(){
        controllerTest = new Controller();
    }

    @Test
    void constructorTest(){
        // Arrange
        ProjectStateDTO state = controllerTest.getProjectStateDTO();

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
        ProjectStateDTO state = controllerTest.getProjectStateDTO();

        // Assert
        Assertions.assertEquals("test", state.getBitList()[0].getName());
    }

    @Test
    void get_cuts_dto(){
        ArrayList<VertexDTO> points = new ArrayList<>();
        points.add(new VertexDTO(12, 13, 14));
        points.add(new VertexDTO(17, 18, 19));
        controllerTest.requestCut(new RequestCutDTO(points, CutType.LINE_VERTICAL, 3, 7.0f));

        Assertions.assertEquals(controllerTest.getCutListDTO().size(), 1);
        Assertions.assertEquals(controllerTest.getCutListDTO().getFirst().getPoints().getFirst().getX(), 12);
        Assertions.assertEquals(controllerTest.getCutListDTO().getFirst().getPoints().getFirst().getY(), 13);
        Assertions.assertEquals(controllerTest.getCutListDTO().getFirst().getPoints().getFirst().getZ(), 14);
        Assertions.assertEquals(controllerTest.getCutListDTO().getFirst().getClass(), CutDTO.class);
    }

}
