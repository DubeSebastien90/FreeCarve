package Domain.IO;

import Common.CutState;
import Common.DTO.*;
import Domain.Controller;
import Domain.CutType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GcodeGeneratorTest {

    @Test
    void convertCutList_AtLeastOne_AsExpected() throws IOException {
        //Arrange
        List<VertexDTO> list = new ArrayList<>();
        list.add(new VertexDTO(100, 100, 0));
        list.add(new VertexDTO(50, 100, 0));
        list.add(new VertexDTO(50, 50, 0));
        RequestCutDTO cut1 = new RequestCutDTO( list, CutType.LINE_FREE, 1, 5, new ArrayList<>());
        Controller controller = Controller.initialize();
        controller.requestCut(cut1);
        //act
        String actual = GcodeGenerator.convertToGCode(controller);
        String expectedGcode = """
                G21;
                G17;
                G28;
                G90;
                G92 Z0;
                F3;
                T2 M06;
                G00 X100.0 Y100.0;
                M03 S20000;
                G82 X100.0 Y100.0 Z-5.5;
                G01 X100.0 Y100.0 Z-5.5;
                G01 X50.0 Y100.0 Z-5.5;
                G01 X50.0 Y50.0 Z-5.5;
                G01 Z0;
                M05;
                G28;
                G00 X0 Y0;
                M02;
                """;
        //Assert
        Assertions.assertEquals(expectedGcode, actual);
    }
}
