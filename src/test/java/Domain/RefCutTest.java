package Domain;

import Common.DTO.CutDTO;
import Common.DTO.VertexDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class RefCutTest {
    private Cut cut1;
    private RefCut ref;

    @BeforeEach
    void setUp(){
        ArrayList<VertexDTO> points = new ArrayList<>();
        points.add(new VertexDTO(0, 0, 0));
        points.add(new VertexDTO(0, 100, 0));
        cut1 = new Cut(new VertexDTO(0, 0, 0), CutType.LINE_VERTICAL, points, 0, 5.0f);

    }

    @Test
    void ref_interpolation_0(){
        // Arrange
        ArrayList<RefCut> refs = new ArrayList<>();
        ref = new RefCut(cut1, 0, 0);
        refs.add(ref);

        ArrayList<VertexDTO> points2 = new ArrayList<>();
        points2.add(new VertexDTO(0, 0, 0));
        points2.add(new VertexDTO(100, 0, 0));
        Cut cut2 = new Cut(new VertexDTO(0, 0, 0), CutType.LINE_HORIZONTAL, points2, 0, 5.0f, refs);

        // Act
        List<VertexDTO> absolutePoints = cut2.getAbsolutePointsPosition();
        VertexDTO p1 = absolutePoints.get(0);
        VertexDTO p2 = absolutePoints.get(1);

        // Assert
        Assertions.assertEquals(absolutePoints.size(), 2);
        Assertions.assertEquals(p1.getX(), 0);
        Assertions.assertEquals(p1.getY(), 0);
        Assertions.assertEquals(p2.getX(), 100);
        Assertions.assertEquals(p2.getY(), 0);
    }
}
