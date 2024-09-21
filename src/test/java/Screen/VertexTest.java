package Screen;

import Annotations.VariableSource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

public class VertexTest {
    public static Stream<Arguments> substract_HappyPath = Stream.of(
            Arguments.of(new Vertex(-1, 2, 3), new Vertex(-1, 2, 3), new Vertex(0, 0, 0)),
            Arguments.of(new Vertex(5, 99, -200), new Vertex(-1, 2, 3), new Vertex(6, 97, -203)));

    @ParameterizedTest
    @VariableSource("substract_HappyPath")
    void substract_happyPath_substractsCorrectlyAndChangesVector(Vertex v1, Vertex v2, Vertex expected) {
        // Arrange
        Vertex previousV1 = new Vertex(v1);
        Vertex previousV2 = new Vertex(v2);
        // Act
        v1.subtract(v2);
        // Assert
        Assertions.assertEquals(v1, expected);
        Assertions.assertNotEquals(v1, previousV1);
        Assertions.assertEquals(v2, previousV2);
    }

    public static Stream<Arguments> add_HappyPath = Stream.of(
            Arguments.of(new Vertex(-1, 2, 3), new Vertex(-1, 2, 3), new Vertex(-2, 4, 6)),
            Arguments.of(new Vertex(5, 5, 5), new Vertex(-1, 2, 3), new Vertex(4, 7, 8)));

    @ParameterizedTest
    @VariableSource("add_HappyPath")
    void add_happyPath_addsCorrectlyAndChangesVector(Vertex v1, Vertex v2, Vertex expected) {
        // Arrange
        Vertex previousV1 = new Vertex(v1);
        Vertex previousV2 = new Vertex(v2);
        // Act
        v1.add(v2);
        // Assert
        Assertions.assertEquals(v1, expected);
        Assertions.assertNotEquals(v1, previousV1);
        Assertions.assertEquals(v2, previousV2);
    }

    public static Stream<Arguments> multiply_HappyPath = Stream.of(
            Arguments.of(new Vertex(0, 0, 0), 213, new Vertex(0, 0, 0)),
            Arguments.of(new Vertex(1, 1, -1), 213, new Vertex(213, 213, -213)),
            Arguments.of(new Vertex(3, 0, -1), -1, new Vertex(-3, 0, 1)));

    @ParameterizedTest
    @VariableSource("multiply_HappyPath")
    void multiply_happyPath_addsCorrectlyAndChangesVector(Vertex v1, int factor, Vertex expected) {
        // Act
        v1.multiply(factor);
        // Assert
        Assertions.assertEquals(v1, expected);
    }

    public static Stream<Arguments> isParallel_ParallelVectors = Stream.of(
            Arguments.of(new Vertex(0, 0, 0), new Vertex(0, 0, 0)),
            Arguments.of(new Vertex(1, 1, 1), new Vertex(213, 213, 213)),
            Arguments.of(new Vertex(-1, -1, -1), new Vertex(1, 1, 1)));

    @ParameterizedTest
    @VariableSource("isParallel_ParallelVectors")
    void isParallel_parallelVectors_returnsTrue(Vertex v1, Vertex v2) {
        // Act
        boolean result = v1.isParallel(v2);
        // Assert
        Assertions.assertTrue(result);
    }

    public static Stream<Arguments> isParallel_VectorsNotParallel = Stream.of(
            Arguments.of(new Vertex(0, 0, 0), new Vertex(0, 1, 0)),
            Arguments.of(new Vertex(-100, 100, 100), new Vertex(213, 213, 213)));

    @ParameterizedTest
    @VariableSource("isParallel_VectorsNotParallel")
    void isParallel_vectorsNotParallel_returnsFalse(Vertex v1, Vertex v2) {
        // Act
        boolean result = v1.isParallel(v2);
        // Assert
        Assertions.assertFalse(result);
    }

    public static Stream<Arguments> addStatic_HappyPath = Stream.of(
            Arguments.of(new Vertex(-1, 2, 3), new Vertex(-1, 2, 3), new Vertex(-2, 4, 6)),
            Arguments.of(new Vertex(5, 5, 5), new Vertex(-1, 2, 3), new Vertex(4, 7, 8)),
            Arguments.of(new Vertex(5, 99, -200), new Vertex(-1, 2, 3), new Vertex(4, 101, -197)));

    @ParameterizedTest
    @VariableSource("addStatic_HappyPath")
    void addStatic_happyPath_addsCorrectlyAndDoesntChangeVector(Vertex v1, Vertex v2, Vertex expected) {
        // Arrange
        Vertex previousV1 = new Vertex(v1);
        Vertex previousV2 = new Vertex(v2);
        // Act
        Vertex result = Vertex.add(v1, v2);
        // Assert
        Assertions.assertEquals(result, expected);
        Assertions.assertEquals(v1, previousV1);
        Assertions.assertEquals(v2, previousV2);
    }
}