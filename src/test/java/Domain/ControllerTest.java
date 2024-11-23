package Domain;

import Annotations.VariableSource;
import Common.DTO.*;
import Common.Units;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Stream;

public class ControllerTest {

    private static Controller controllerTest;

    @BeforeAll
    static void setup(){
        controllerTest = Controller.initialize();
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
    void requestCut_Accepted_SetsCutList(){
        // Arrange
        ArrayList<VertexDTO> points = new ArrayList<>();
        points.add(new VertexDTO(12, 13, 14));
        points.add(new VertexDTO(17, 18, 19));

        // Act
        controllerTest.requestCut(new RequestCutDTO(points, CutType.LINE_VERTICAL, 3, 7.0f, new ArrayList<RefCutDTO>()));

        // Assert
        Assertions.assertEquals(controllerTest.getCutListDTO().size(), 1);
        Assertions.assertEquals(controllerTest.getCutListDTO().getFirst().getPoints().getFirst().getX(), 12);
        Assertions.assertEquals(controllerTest.getCutListDTO().getFirst().getPoints().getFirst().getY(), 13);
        Assertions.assertEquals(controllerTest.getCutListDTO().getFirst().getPoints().getFirst().getZ(), 14);
        Assertions.assertEquals(controllerTest.getCutListDTO().getFirst().getClass(), CutDTO.class);
    }

    public static Stream<Arguments> convertUnit_HappyPath = Stream.of(
            Arguments.of(new DimensionDTO(1.0, Units.MM), Units.MM, 1.0),
            Arguments.of(new DimensionDTO(4.0, Units.FEET), Units.MM, 1219.2),
            Arguments.of(new DimensionDTO(4.0, Units.FEET), Units.INCH, 48),
            Arguments.of(new DimensionDTO(4.0, Units.INCH), Units.M, 0.1016));

    @ParameterizedTest
    @VariableSource("convertUnit_HappyPath")
    void convertUnit_HappyPath_ConvertsValueCorrectlyAndReturnsCorrectUnit(DimensionDTO input, Units target, double expectedValue){
        // Act
        DimensionDTO converted = controllerTest.convertUnit(input, target);
        // Assert
        Assertions.assertEquals(expectedValue, converted.value(), 0.001);
        Assertions.assertEquals(target, converted.unit());
    }

}
