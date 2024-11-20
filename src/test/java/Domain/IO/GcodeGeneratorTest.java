package Domain.IO;

import Common.DTO.*;
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
        CutDTO cut1 = new CutDTO(new UUID(1, 1), 2, 1, CutType.LINE_FREE, list);
        List<CutDTO> c = List.of(new CutDTO[]{cut1});
        PanelDTO pnel = new PanelDTO(c, new VertexDTO(300, 300, 5), 300, 300);
        BitDTO[] bits = {new BitDTO("g", 3)};
        ProjectStateDTO pjt = new ProjectStateDTO(bits, pnel);

        //Act
        String actual = GcodeGenerator.convertToGCode(pjt);
        String expectedGcode = """
                G21;
                G17;
                G28;
                G90;
                G92 Z0;
                F50;
                T2 M06;
                G00 X100.0 Y100.0;
                M03 S1200;
                G82 X100.0 Y100.0 Z-2.0;
                G09 X50.0 Y100.0 Z-2.0;
                G09 X50.0 Y50.0 Z-2.0;
                M05;
                G00 Z0;
                M05;
                G28;
                G00 X0 Y0;
                M02;
                """;
        //Assert
        Assertions.assertEquals(expectedGcode, actual);
    }
}
