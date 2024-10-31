package Domain;

import Domain.ThirdDimension.Vertex;
import Domain.ThirdDimension.VertexDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.UUID;

public class CutDTOTest {
    private CutDTO cut;
    private CutDTO cutDTOwithCut;
    private ArrayList<VertexDTO> pointsList;
    private ArrayList<Vertex> pointsListToDTO;

    @BeforeEach
    void setUp(){
        pointsList = new ArrayList<VertexDTO>();
        VertexDTO temp1 = new VertexDTO(300, 400, 500);
        VertexDTO temp2 = new VertexDTO(100, 200, 300);
        pointsList.add(temp1);
        pointsList.add(temp2);
        cut = new CutDTO(new UUID(10000, 10000), 5.0f, 0,
                CutType.BORDER, pointsList);

        Vertex temp3 = new Vertex(1, 2, 3);
        Vertex temp4 = new Vertex(4, 5, 6);
        Vertex temp5 = new Vertex(7, 8, 9);
        pointsListToDTO = new ArrayList<Vertex>();
        pointsListToDTO.add(temp3);
        pointsListToDTO.add(temp4);
        pointsListToDTO.add(temp5);
        Cut cut = new Cut(temp3, CutType.LINE_VERTICAL, pointsListToDTO, 0, 3.0f);
        cutDTOwithCut = new CutDTO(cut);
    }

    @Test
    void constructor_with_arguments_postconditions(){
        // Arrange - BeforeEach

        // Act

        // Assert
        Assertions.assertEquals(cut.getCutType(), CutType.BORDER);
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
