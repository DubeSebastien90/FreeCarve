package Domain;

import Common.CutState;
import Common.DTO.CutDTO;
import Common.DTO.RefCutDTO;
import Common.DTO.VertexDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.UUID;

public class CutDTOTest {
    private CutDTO cut;
    private CutDTO cutDTOwithCut;
    private ArrayList<VertexDTO> pointsList;
    private ArrayList<VertexDTO> pointsListToDTO;

    @BeforeEach
    void setUp(){
        pointsList = new ArrayList<VertexDTO>();
        VertexDTO temp1 = new VertexDTO(300, 400, 500);
        VertexDTO temp2 = new VertexDTO(100, 200, 300);
        pointsList.add(temp1);
        pointsList.add(temp2);
        cut = new CutDTO(new UUID(10000, 10000), 5.0f, 0,
                CutType.CLAMP, pointsList, new ArrayList<RefCutDTO>(), CutState.VALID);

        VertexDTO temp3 = new VertexDTO(1, 2, 3);
        VertexDTO temp4 = new VertexDTO(4, 5, 6);
        VertexDTO temp5 = new VertexDTO(7, 8, 9);
        pointsListToDTO = new ArrayList<VertexDTO>();
        pointsListToDTO.add(temp3);
        pointsListToDTO.add(temp4);
        pointsListToDTO.add(temp5);
        Cut cut = new Cut(CutType.LINE_VERTICAL, pointsListToDTO, 0, 3.0f);
        cutDTOwithCut = cut.getDTO();
    }

    @Test
    void constructor_with_arguments_postconditions(){
        // Arrange - BeforeEach

        // Act

        // Assert
        Assertions.assertEquals(cut.getCutType(), CutType.CLAMP);
        Assertions.assertEquals(cut.getPoints(), pointsList);
        Assertions.assertEquals(cut.getBitIndex(), 0);
        Assertions.assertEquals(cut.getDepth(), 5.0f);
        Assertions.assertEquals(cut.getClass(), CutDTO.class);
    }

    @Test
    void constructor_with_normalCut_postconditions(){
        // Arrange - BeforeEach

        // Act

        // Assert
        Assertions.assertEquals(cutDTOwithCut.getDepth(), 3.0f);
        Assertions.assertEquals(cutDTOwithCut.getPoints().size(), pointsListToDTO.size());
        Assertions.assertEquals(cutDTOwithCut.getPoints().getFirst().getX(), pointsListToDTO.getFirst().getX());
        Assertions.assertEquals(cutDTOwithCut.getPoints().getFirst().getY(), pointsListToDTO.getFirst().getY());
        Assertions.assertEquals(cutDTOwithCut.getPoints().getFirst().getZ(), pointsListToDTO.getFirst().getZ());
        Assertions.assertEquals(cutDTOwithCut.getBitIndex(), 0);
        Assertions.assertEquals(cutDTOwithCut.getClass(), CutDTO.class);
        Assertions.assertEquals(cutDTOwithCut.getCutType(), CutType.LINE_VERTICAL);
    }
}
