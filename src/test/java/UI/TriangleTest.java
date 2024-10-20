package UI;

import Annotations.VariableSource;
import Domain.ThirdDimension.Triangle;
import Domain.ThirdDimension.Vertex;
import Parser.ParsedSTL;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;

import java.awt.*;
import java.util.stream.Stream;

public class TriangleTest {

    @Test
    void findBoundingRectangle_withinBounds_calculatesArea(){
        // Arrange
        int maxWidth = 5;
        int maxHeight = 5;
        Triangle triangle = new Triangle(new Vertex(0,0,0), new Vertex(maxWidth,0,0), new Vertex(0,maxHeight,0));

        // Act
        int[] result = triangle.findBoundingRectangle(maxWidth, maxHeight);

        // Assert
        Assertions.assertArrayEquals(new int[]{0, maxWidth, 0, maxHeight}, result);
    }

    @Test
    void findBoundingRectangle_outOfBounds_calculatesAreaWithinBounds(){
        // Arrange
        int maxWidth = 5;
        int maxHeight = 5;
        Triangle triangle = new Triangle(new Vertex(0,0,0), new Vertex(maxWidth*2,0,0), new Vertex(0,maxHeight*2,0));

        // Act
        int[] result = triangle.findBoundingRectangle(maxWidth, maxHeight);

        // Assert
        Assertions.assertArrayEquals(new int[]{0, maxWidth, 0, maxHeight}, result);
    }

    @Test
    void fromParsedSTL_firstTriangle_parseCorrect() {
        // Arrange
        float[][] vertices = new float[][]{{1, 0, 0}, {0, -1, 0}, {0, 0, 1}};
        float[][] normals = new float[][]{{-1, 0, 0}};
        ParsedSTL parsedSTL = new ParsedSTL(vertices, normals);
        Triangle expected = new Triangle(new Vertex(1, 0, 0), new Vertex(0, -1, 0), new Vertex(0, 0, 1), new Vertex(-1, 0, 0), Color.ORANGE);

        // Act
        Triangle[] triangles = Triangle.fromParsedSTL(parsedSTL, Color.ORANGE);

        // Assert
        Assertions.assertEquals(triangles[0], expected);
    }

    public static Stream<Arguments> calculateNormal_ValidTriangle = Stream.of(
            Arguments.of(new Triangle(new Vertex(0, 0, 0), new Vertex(1, 0, 0), new Vertex(0, 1, 0)), new Vertex(0, 0, 1)),
            Arguments.of(new Triangle(new Vertex(0, 0, 0), new Vertex(0, 0, 0), new Vertex(0, 0, 0)), new Vertex(0, 0, 0)),
            Arguments.of(new Triangle(new Vertex(1000, 0, 0), new Vertex(0, 1000, 0), new Vertex(0, 0, 0)), new Vertex(0, 0, 1)));

    @ParameterizedTest
    @VariableSource("calculateNormal_ValidTriangle")
    void calculateNormal_validTriangle_correctNormal(Triangle triangle, Vertex expected) {
        // Act
        triangle.calculateNormal();

        // Assert
        Assertions.assertEquals(triangle.getNormal(), expected);
    }

    public static Stream<Arguments> findBarycentric_inTriangle = Stream.of(
            Arguments.of(new Triangle(new Vertex(0, 0, 0), new Vertex(1, 0, 0), new Vertex(0, 1, 0)), 0.5, 0, new Vertex(0.5, 0.5, 0)),
            Arguments.of(new Triangle(new Vertex(0, 0, 0), new Vertex(6, 0, 0), new Vertex(3, 6, 6)), 1.5, 3, new Vertex(0.5, 0, 0.5)));

    @ParameterizedTest
    @VariableSource("findBarycentric_inTriangle")
    void findBarycentric_inTriangle_goodCoords(Triangle triangle, double xCoord, double yCoord, Vertex expected) {
        // Act
        Vertex bary = triangle.findBarycentric(xCoord, yCoord);

        // Assert
        Assertions.assertEquals(expected, bary);
    }

}
