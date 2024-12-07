package Domain;

import Common.DTO.VertexDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class RoundedCutTest {

    @Test
    void test_points_in_horizontal_roundedCut(){
        // Arrange
        VertexDTO p1 = new VertexDTO(50, 50, 0);
        VertexDTO p2 = new VertexDTO(100, 50, 0);
        double bitDiameter = 30;

        // Act
        RoundedCut roundedCut = new RoundedCut(p1, p2, bitDiameter);
        VertexDTO pTest1 = new VertexDTO(50, 50 ,0);
        VertexDTO pTest2 = new VertexDTO(75, 50 ,0);
        VertexDTO pTest3 = new VertexDTO(35, 50 ,0);
        VertexDTO pTest4 = new VertexDTO(115, 50 ,0);
        VertexDTO pTest5 = new VertexDTO(75, 55 ,0);
        VertexDTO pTest6 = new VertexDTO(75, 36 ,0);

        // Assert
        Assertions.assertTrue(roundedCut.pointInRoundedCut(pTest1));
        Assertions.assertTrue(roundedCut.pointInRoundedCut(pTest2));
        Assertions.assertTrue(roundedCut.pointInRoundedCut(pTest3));
        Assertions.assertTrue(roundedCut.pointInRoundedCut(pTest4));
        Assertions.assertTrue(roundedCut.pointInRoundedCut(pTest5));
        Assertions.assertTrue(roundedCut.pointInRoundedCut(pTest6));
    }

    @Test
    void test_points_NOT_horizontal_roundedCut(){
        // Arrange
        VertexDTO p1 = new VertexDTO(50, 50, 0);
        VertexDTO p2 = new VertexDTO(100, 50, 0);
        double bitDiameter = 30;

        // Act
        RoundedCut roundedCut = new RoundedCut(p1, p2, bitDiameter);
        VertexDTO pTest1 = new VertexDTO(50, 66 ,0);
        VertexDTO pTest2 = new VertexDTO(75, 30 ,0);
        VertexDTO pTest3 = new VertexDTO(34, 50 ,0);
        VertexDTO pTest4 = new VertexDTO(116, 50 ,0);
        VertexDTO pTest5 = new VertexDTO(75, 66 ,0);
        VertexDTO pTest6 = new VertexDTO(75, 34 ,0);

        // Assert
        Assertions.assertFalse(roundedCut.pointInRoundedCut(pTest1));
        Assertions.assertFalse(roundedCut.pointInRoundedCut(pTest2));
        Assertions.assertFalse(roundedCut.pointInRoundedCut(pTest3));
        Assertions.assertFalse(roundedCut.pointInRoundedCut(pTest4));
        Assertions.assertFalse(roundedCut.pointInRoundedCut(pTest5));
        Assertions.assertFalse(roundedCut.pointInRoundedCut(pTest6));
    }

    @Test
    void test_points_in_oblique_roundedCut(){
        // Arrange
        VertexDTO p1 = new VertexDTO(50, 50, 0);
        VertexDTO p2 = new VertexDTO(100, 100, 0);
        double bitDiameter = 30;

        // Act
        RoundedCut roundedCut = new RoundedCut(p1, p2, bitDiameter);
        VertexDTO pTest1 = new VertexDTO(50, 50, 0);
        VertexDTO pTest2 = new VertexDTO(100, 100 ,0);
        VertexDTO pTest3 = new VertexDTO(75, 75 ,0);
        double dist = 15 / Math.sqrt(2) - VertexDTO.doubleTolerance;
        VertexDTO pTest4 = new VertexDTO(100 + dist, 100 + dist ,0);
        VertexDTO pTest5 = new VertexDTO(50 - dist, 50 - dist ,0);
        VertexDTO pTest6 = new VertexDTO(89, 89 ,0);

        // Assert
        Assertions.assertTrue(roundedCut.pointInRoundedCut(pTest1));
        Assertions.assertTrue(roundedCut.pointInRoundedCut(pTest2));
        Assertions.assertTrue(roundedCut.pointInRoundedCut(pTest3));
        Assertions.assertTrue(roundedCut.pointInRoundedCut(pTest4));
        Assertions.assertTrue(roundedCut.pointInRoundedCut(pTest5));
        Assertions.assertTrue(roundedCut.pointInRoundedCut(pTest6));
    }

    @Test
    void test_points_NOT_oblique_roundedCut(){
        // Arrange
        VertexDTO p1 = new VertexDTO(50, 50, 0);
        VertexDTO p2 = new VertexDTO(100, 100, 0);
        double bitDiameter = 30;

        // Act
        RoundedCut roundedCut = new RoundedCut(p1, p2, bitDiameter);
        VertexDTO pTest1 = new VertexDTO(200, 200, 0);
        VertexDTO pTest2 = new VertexDTO(0, 0 ,0);
        VertexDTO pTest3 = new VertexDTO(300, 200 ,0);
        VertexDTO pTest4 = new VertexDTO(116, 116 ,0);
        VertexDTO pTest5 = new VertexDTO(34, 34 ,0);
        double dist = 50;
        VertexDTO pTest6 = new VertexDTO(75 + dist, 75 + dist ,0);

        // Assert
        Assertions.assertFalse(roundedCut.pointInRoundedCut(pTest1));
        Assertions.assertFalse(roundedCut.pointInRoundedCut(pTest2));
        Assertions.assertFalse(roundedCut.pointInRoundedCut(pTest3));
        Assertions.assertFalse(roundedCut.pointInRoundedCut(pTest4));
        Assertions.assertFalse(roundedCut.pointInRoundedCut(pTest5));
        Assertions.assertFalse(roundedCut.pointInRoundedCut(pTest6));
    }

    @Test
    void intersectRoundedCut_WhenCutsIntersects_ReturnTrue(){
        // Arrange
        VertexDTO p1 = new VertexDTO(50, 50, 0);
        VertexDTO p2 = new VertexDTO(100, 50, 0);
        double bitDiameter = 30;

        VertexDTO p3 = new VertexDTO(50, 50, 0);
        VertexDTO p4 = new VertexDTO(50, 100, 0);
        double bitDiameter2 = 0;

        // Act
        RoundedCut roundedCut = new RoundedCut(p1, p2, bitDiameter);
        RoundedCut roundedCut2 = new RoundedCut(p3, p4, bitDiameter2);
        boolean result = roundedCut.intersectRoundedCut(roundedCut2);

        // Assert
        Assertions.assertTrue(result);
    }

    @Test
    void intersectRoundedCut_WhenCutsBarelyIntersects_ReturnTrue(){
        // Arrange
        VertexDTO p1 = new VertexDTO(65, 50, 0);
        VertexDTO p2 = new VertexDTO(100, 50, 0);
        double bitDiameter = 30;

        VertexDTO p3 = new VertexDTO(50, 50, 0);
        VertexDTO p4 = new VertexDTO(50, 100, 0);
        double bitDiameter2 = 0;

        // Act
        RoundedCut roundedCut = new RoundedCut(p1, p2, bitDiameter);
        RoundedCut roundedCut2 = new RoundedCut(p3, p4, bitDiameter2);
        boolean result = roundedCut.intersectRoundedCut(roundedCut2);

        // Assert
        Assertions.assertTrue(result);
    }

    @Test
    void intersectRoundedCut_WhenCutsBarelyNotIntersects_ReturnTrue(){
        // Arrange
        VertexDTO p1 = new VertexDTO(66, 50, 0);
        VertexDTO p2 = new VertexDTO(100, 50, 0);
        double bitDiameter = 30;

        VertexDTO p3 = new VertexDTO(50, 50, 0);
        VertexDTO p4 = new VertexDTO(50, 100, 0);
        double bitDiameter2 = 0;

        // Act
        RoundedCut roundedCut = new RoundedCut(p1, p2, bitDiameter);
        RoundedCut roundedCut2 = new RoundedCut(p3, p4, bitDiameter2);
        boolean result = roundedCut.intersectRoundedCut(roundedCut2);

        // Assert
        Assertions.assertFalse(result);
    }

    @Test
    void intersectRoundedCut_WhenCutsNotIntersects_ReturnFalse(){
        // Arrange
        VertexDTO p1 = new VertexDTO(50, 45, 0);
        VertexDTO p2 = new VertexDTO(100, 50, 0);
        double bitDiameter = 15;

        VertexDTO p3 = new VertexDTO(50, 45, 0);
        VertexDTO p4 = new VertexDTO(50, 100, 0);
        double bitDiameter2 = 0;

        // Act
        RoundedCut roundedCut = new RoundedCut(p1, p2, bitDiameter);
        RoundedCut roundedCut2 = new RoundedCut(p3, p4, bitDiameter2);
        boolean result = roundedCut.intersectRoundedCut(roundedCut2);

        // Assert
        Assertions.assertFalse(result);
    }

    @Test
    void intersectRounedCut_WhenIntersectsMiddle_ReturnsTrue(){
        // Arrange
        VertexDTO p1 = new VertexDTO(50, 50, 0);
        VertexDTO p2 = new VertexDTO(100, 50, 0);
        double bitDiameter = 30;

        VertexDTO p3 = new VertexDTO(75, 25, 0);
        VertexDTO p4 = new VertexDTO(75, 75, 0);
        double bitDiameter2 = 2;

        // Act
        RoundedCut roundedCut = new RoundedCut(p1, p2, bitDiameter);
        RoundedCut roundedCut2 = new RoundedCut(p3, p4, bitDiameter2);
        boolean result = roundedCut.intersectRoundedCut(roundedCut2);

        // Assert
        Assertions.assertTrue(result);
    }
}
