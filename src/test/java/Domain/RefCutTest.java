package Domain;

import Common.DTO.CutDTO;
import Common.DTO.VertexDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class RefCutTest {
    private Cut cut;
    private Cut cut_interpolation_0;
    private Cut cut_interpolation_0_5;
    private RefCut ref;

    @BeforeEach
    void setUp(){
        ArrayList<VertexDTO> points = new ArrayList<>();
        points.add(new VertexDTO(0, 0, 0));
        points.add(new VertexDTO(0, 100, 0));
        cut = new Cut(new VertexDTO(0, 0, 0), CutType.LINE_VERTICAL, points, 0, 5.0f);

        ArrayList<RefCut> refs = new ArrayList<>();
        ref = new RefCut(cut, 0, 0.5);
        refs.add(ref);
        ArrayList<VertexDTO> points2 = new ArrayList<>();
        points2.add(new VertexDTO(0, 0, 0));
        points2.add(new VertexDTO(100, 0, 0));
        cut_interpolation_0_5 = new Cut(new VertexDTO(0, 0, 0), CutType.LINE_HORIZONTAL, points2, 0, 5.0f, refs);


        ArrayList<RefCut> refs2 = new ArrayList<>();
        ref = new RefCut(cut, 0, 0);
        refs2.add(ref);
        ArrayList<VertexDTO> points3 = new ArrayList<>();
        points3.add(new VertexDTO(0, 0, 0));
        points3.add(new VertexDTO(100, 0, 0));
        cut_interpolation_0 = new Cut(new VertexDTO(0, 0, 0), CutType.LINE_HORIZONTAL, points3, 0, 5.0f, refs2);

    }

    @Test
    void ref_test_offset(){
        // Arrange
        // Act
        VertexDTO offset0_5 = cut_interpolation_0_5.getRefs().getFirst().getAbsoluteOffset();
        VertexDTO offset0 = cut_interpolation_0.getRefs().getFirst().getAbsoluteOffset();
        // Assert
        Assertions.assertEquals(offset0_5.getX(), 0);
        Assertions.assertEquals(offset0_5.getY(), 50);
        Assertions.assertEquals(offset0.getX(), 0);
        Assertions.assertEquals(offset0.getY(), 0);
    }

    @Test
    void ref_interpolation_0(){
        // Arrange

        // Act
        List<VertexDTO> absolutePoints = cut_interpolation_0.getAbsolutePointsPosition();
        VertexDTO p1 = absolutePoints.get(0);
        VertexDTO p2 = absolutePoints.get(1);

        // Assert
        Assertions.assertEquals(absolutePoints.size(), 2);
        Assertions.assertEquals(p1.getX(), 0);
        Assertions.assertEquals(p1.getY(), 0);
        Assertions.assertEquals(p2.getX(), 100);
        Assertions.assertEquals(p2.getY(), 0);
    }

    @Test
    void ref_interpolation_0_5(){
        // Arrange

        // Act
        List<VertexDTO> absolutePoints = cut_interpolation_0_5.getAbsolutePointsPosition();
        VertexDTO p1 = absolutePoints.get(0);
        VertexDTO p2 = absolutePoints.get(1);

        // Assert
        Assertions.assertEquals(absolutePoints.size(), 2);
        Assertions.assertEquals(p1.getX(), 0);
        Assertions.assertEquals(p1.getY(), 50);
        Assertions.assertEquals(p2.getX(), 100);
        Assertions.assertEquals(p2.getY(), 50);
    }

    @Test
    void ref_interpolation_move_ref(){
        // Arrange

        // Act
        double offsetX = 30;
        double offsetY = 25;
        ArrayList<VertexDTO> points = new ArrayList<>();
        points.add(new VertexDTO(0 + offsetX, 0 + offsetY, 0));
        points.add(new VertexDTO(0 + offsetX, 100 + offsetY, 0));
        cut.setPoints(points);

        List<VertexDTO> absolutePoints = cut_interpolation_0_5.getAbsolutePointsPosition();
        VertexDTO p1 = absolutePoints.get(0);
        VertexDTO p2 = absolutePoints.get(1);

        // Assert
        Assertions.assertEquals(absolutePoints.size(), 2);
        Assertions.assertEquals(p1.getX(), 0 + offsetX);
        Assertions.assertEquals(p1.getY(), 50 + offsetY);
        Assertions.assertEquals(p2.getX(), 100 + offsetX);
        Assertions.assertEquals(p2.getY(), 50 + offsetY);

        Assertions.assertEquals(cut_interpolation_0_5.getPoints().getFirst().getX(), 0);
        Assertions.assertEquals(cut_interpolation_0_5.getPoints().getFirst().getY(), 0);
        Assertions.assertEquals(cut_interpolation_0_5.getPoints().get(1).getX(), 100);
        Assertions.assertEquals(cut_interpolation_0_5.getPoints().get(1).getY(), 0);

    }

    @Test
    void ref_4_parent_ref(){
        // Arrange
        ArrayList<VertexDTO> pointsVertical = new ArrayList<>();
        pointsVertical.add(new VertexDTO(0, 0, 0));
        pointsVertical.add(new VertexDTO(0, 100, 0));

        ArrayList<VertexDTO> pointsHorizontal = new ArrayList<>();
        pointsHorizontal.add(new VertexDTO(0, 0, 0));
        pointsHorizontal.add(new VertexDTO(100, 0, 0));


        Cut cut1 = new Cut(new VertexDTO(0, 0, 0), CutType.LINE_VERTICAL, pointsVertical, 0, 5.0f);
        RefCut ref1 = new RefCut(cut1, 0, 0.5);
        ArrayList<RefCut> refs1 = new ArrayList<>();
        refs1.add(ref1);

        Cut cut2 = new Cut(new VertexDTO(0, 0, 0), CutType.LINE_HORIZONTAL, pointsHorizontal, 0, 5.0f, refs1);
        RefCut ref2 = new RefCut(cut2, 0, 0.5);
        ArrayList<RefCut> refs2 = new ArrayList<>();
        refs2.add(ref2);

        Cut cut3 = new Cut(new VertexDTO(0, 0, 0), CutType.LINE_VERTICAL, pointsVertical, 0, 5.0f, refs2);
        RefCut ref3 = new RefCut(cut3, 0, 0.5);
        ArrayList<RefCut> refs3 = new ArrayList<>();
        refs3.add(ref3);

        Cut cut4 = new Cut(new VertexDTO(0, 0, 0), CutType.LINE_HORIZONTAL, pointsHorizontal, 0, 5.0f, refs3);


        // Act
        List<VertexDTO> absolutePoints = cut4.getAbsolutePointsPosition();
        VertexDTO p1 = absolutePoints.get(0);
        VertexDTO p2 = absolutePoints.get(1);

        // Assert
        Assertions.assertEquals(absolutePoints.size(), 2);
        Assertions.assertEquals(p1.getX(), 50 );
        Assertions.assertEquals(p1.getY(), 100);
        Assertions.assertEquals(p2.getX(), 150);
        Assertions.assertEquals(p2.getY(), 100);


        Assertions.assertEquals(cut4.getPoints().getFirst().getX(), 0);
        Assertions.assertEquals(cut4.getPoints().getFirst().getY(), 0);
        Assertions.assertEquals(cut4.getPoints().get(1).getX(), 100);
        Assertions.assertEquals(cut4.getPoints().get(1).getY(), 0);
    }

    @Test
    void ref_rectangle(){

        // Arrange
        ArrayList<VertexDTO> pointsRect = new ArrayList<>();
        pointsRect.add(new VertexDTO(0, 0, 0));
        pointsRect.add(new VertexDTO(0, 100, 0));
        pointsRect.add(new VertexDTO(100, 100, 0));
        pointsRect.add(new VertexDTO(100, 0, 0));
        pointsRect.add(new VertexDTO(0, 0, 0));

        ArrayList<VertexDTO> pointsHorizontal = new ArrayList<>();
        pointsHorizontal.add(new VertexDTO(0, 0, 0));
        pointsHorizontal.add(new VertexDTO(100, 0, 0));

        Cut cut1 = new Cut(new VertexDTO(0, 0, 0), CutType.RECTANGULAR, pointsRect, 0, 5.0f);
        RefCut ref1 = new RefCut(cut1, 2, 0.5); // INDEX IS 2 for the appropriate segment
        ArrayList<RefCut> refs1 = new ArrayList<>();
        refs1.add(ref1);


        Cut cut2 = new Cut(new VertexDTO(0, 0, 0), CutType.LINE_HORIZONTAL, pointsHorizontal, 0, 5.0f, refs1);

        // ACT
        List<VertexDTO> absolutePoints = cut2.getAbsolutePointsPosition();
        VertexDTO p1 = absolutePoints.get(0);
        VertexDTO p2 = absolutePoints.get(1);

        // ASSERT
        Assertions.assertEquals(absolutePoints.size(), 2);
        Assertions.assertEquals(p1.getX(), 100 );
        Assertions.assertEquals(p1.getY(), 50);
        Assertions.assertEquals(p2.getX(), 200);
        Assertions.assertEquals(p2.getY(), 50);

    }

    @Test
    void ref_L(){

        // Arrange
        ArrayList<VertexDTO> pointsRect = new ArrayList<>();
        pointsRect.add(new VertexDTO(0, 0, 0));
        pointsRect.add(new VertexDTO(0, 100, 0));
        pointsRect.add(new VertexDTO(100, 100, 0));
        pointsRect.add(new VertexDTO(100, 0, 0));
        pointsRect.add(new VertexDTO(0, 0, 0));

        ArrayList<VertexDTO> pointsL = new ArrayList<>();
        pointsL.add(new VertexDTO(50, 0, 0));
        pointsL.add(new VertexDTO(50, 50, 0));
        pointsL.add(new VertexDTO(0, 50, 0));

        Cut cut1 = new Cut(new VertexDTO(0, 0, 0), CutType.RECTANGULAR, pointsRect, 0, 5.0f);
        RefCut ref1 = new RefCut(cut1, 0, 0.5);
        RefCut ref2 = new RefCut(cut1, 3, 0.5);
        ArrayList<RefCut> refs1 = new ArrayList<>();
        refs1.add(ref1);
        refs1.add(ref2);

        Cut cut2 = new Cut(new VertexDTO(0, 0, 0), CutType.L_SHAPE, pointsL, 0, 5.0f, refs1);

        // ACT
        List<VertexDTO> absolutePoints = cut2.getAbsolutePointsPosition();
        VertexDTO p1 = absolutePoints.get(0);
        VertexDTO p2 = absolutePoints.get(1);
        VertexDTO p3 = absolutePoints.get(2);

        // ASSERT
        Assertions.assertEquals(absolutePoints.size(), 3);
        Assertions.assertEquals(p1.getX(), 0 );
        Assertions.assertEquals(p1.getY(), 50);
        Assertions.assertEquals(p2.getX(), 50);
        Assertions.assertEquals(p2.getY(), 50);
        Assertions.assertEquals(p3.getX(), 50);
        Assertions.assertEquals(p3.getY(), 0);

    }


    @Test
    void ref_L_other_intersection(){

        // Arrange
        ArrayList<VertexDTO> pointsRect = new ArrayList<>();
        pointsRect.add(new VertexDTO(0, 0, 0));
        pointsRect.add(new VertexDTO(0, 100, 0));
        pointsRect.add(new VertexDTO(100, 100, 0));
        pointsRect.add(new VertexDTO(100, 0, 0));
        pointsRect.add(new VertexDTO(0, 0, 0));

        ArrayList<VertexDTO> pointsL = new ArrayList<>();
        pointsL.add(new VertexDTO(50, 0, 0));
        pointsL.add(new VertexDTO(50, 50, 0));
        pointsL.add(new VertexDTO(0, 50, 0));

        Cut cut1 = new Cut(new VertexDTO(0, 0, 0), CutType.RECTANGULAR, pointsRect, 0, 5.0f);
        RefCut ref1 = new RefCut(cut1, 1, 0.5);
        RefCut ref2 = new RefCut(cut1, 2, 0.5);
        ArrayList<RefCut> refs1 = new ArrayList<>();
        refs1.add(ref1);
        refs1.add(ref2);

        Cut cut2 = new Cut(new VertexDTO(0, 0, 0), CutType.L_SHAPE, pointsL, 0, 5.0f, refs1);

        // ACT
        List<VertexDTO> absolutePoints = cut2.getAbsolutePointsPosition();
        VertexDTO p1 = absolutePoints.get(0);
        VertexDTO p2 = absolutePoints.get(1);
        VertexDTO p3 = absolutePoints.get(2);

        // ASSERT
        Assertions.assertEquals(absolutePoints.size(), 3);
        Assertions.assertEquals(p1.getX(), 50 );
        Assertions.assertEquals(p1.getY(), 100);
        Assertions.assertEquals(p2.getX(), 50);
        Assertions.assertEquals(p2.getY(), 50);
        Assertions.assertEquals(p3.getX(), 100);
        Assertions.assertEquals(p3.getY(), 50);

    }
}



