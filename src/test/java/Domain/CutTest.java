package Domain;

import Domain.ThirdDimension.Vertex;
import Domain.ThirdDimension.VertexDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.UUID;

public class CutTest {
    private Cut cut;
    private Cut cutWithDTO;
    private ArrayList<Vertex> pointsList;
    private ArrayList<VertexDTO> pointsListDTO;

    @BeforeEach
    void setUp(){
        pointsList = new ArrayList<Vertex>();
        Vertex temp1 = new Vertex(300, 400, 500);
        Vertex temp2 = new Vertex(100, 200, 300);
        pointsList.add(temp1);
        pointsList.add(temp2);
        cut = new Cut(temp1, CutType.RECTANGULAR, pointsList, 0,
                5.0f);

        VertexDTO temp3 = new VertexDTO(1, 2, 3);
        VertexDTO temp4 = new VertexDTO(4, 5, 6);
        VertexDTO temp5 = new VertexDTO(7, 8, 9);
        pointsListDTO = new ArrayList<VertexDTO>();
        pointsListDTO.add(temp3);
        pointsListDTO.add(temp4);
        pointsListDTO.add(temp5);
        CutDTO cutDTO = new CutDTO(new UUID(1000, 1000),
                1.0f, 0, CutType.L_SHAPE, pointsListDTO);
        cutWithDTO = new Cut(cutDTO);
    }

    @Test
    void constructor_with_arguments_postconditions(){
        // Arrange - BeforeEach

        // Act

        // Assert
        Assertions.assertEquals(cut.getDepth(), 5.0f);
        Assertions.assertEquals(cut.getPoints(), pointsList);
        Assertions.assertEquals(cut.getBitIndex(), 0);
        Assertions.assertEquals(cut.getClass(), Cut.class);
        Assertions.assertEquals(cut.getStartPoint(), pointsList.getFirst());
        Assertions.assertEquals(cut.getType(), CutType.RECTANGULAR);
    }

    @Test
    void constructor_with_DTO_postconditions(){
        // Arrange - BeforeEach

        // Act

        // Assert
        Assertions.assertEquals(cutWithDTO.getDepth(), 1.0f);
        Assertions.assertEquals(cutWithDTO.getPoints().size(), pointsListDTO.size());
        Assertions.assertEquals(cutWithDTO.getBitIndex(), 0);
        Assertions.assertEquals(cutWithDTO.getClass(), Cut.class);
        Assertions.assertEquals(cutWithDTO.getStartPoint().getX(), pointsListDTO.getFirst().getX());
        Assertions.assertEquals(cutWithDTO.getStartPoint().getY(), pointsListDTO.getFirst().getY());
        Assertions.assertEquals(cutWithDTO.getStartPoint().getZ(), pointsListDTO.getFirst().getZ());
        Assertions.assertEquals(cutWithDTO.getType(), CutType.L_SHAPE);
    }
}
