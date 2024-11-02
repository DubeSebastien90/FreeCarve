package Domain.ThirdDimension;

import Annotations.VariableSource;
import Domain.ThirdDimension.Vertex;
import Domain.ThirdDimension.Matrix;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

public class MatrixTest {

    public static Stream<Arguments> matriceXVertex3x3_HappyPath = Stream.of(
        Arguments.of(new Vertex(0, 0, 0), new Matrix(new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9}), new Vertex(0, 0, 0)),
        Arguments.of(new Vertex(1, 1, 1), new Matrix(new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9}), new Vertex(12, 15, 18)),
        Arguments.of(new Vertex(-1, 5, 9), new Matrix(new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9}), new Vertex(82, 95, 108)));

    @ParameterizedTest
    @VariableSource("matriceXVertex3x3_HappyPath")
    void matrixXVertex3X3_happyPath_multipliesCorrectly(Vertex vertex, Matrix matrix, Vertex expected) {
        // Act
        Vertex answer = matrix.matrixXVertex3X3(vertex);
        // Assert
        Assertions.assertEquals(expected, answer);
    }

    public static Stream<Arguments> matriceXVertex3x3_Not3x3 = Stream.of(
            Arguments.of(new Matrix(new double[]{1})),
            Arguments.of(new Matrix(new double[]{1, 2, 3, 4})),
            Arguments.of(new Matrix(new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10})));

    @ParameterizedTest
    @VariableSource("matriceXVertex3x3_Not3x3")
    void matrixXVertex3X3_not3X3_throwsError(Matrix matrix) {
        // Arrange
        Vertex vertex = new Vertex(1,1,1);
        // Act and Assert
        Assertions.assertThrows(ArithmeticException.class, ()->matrix.matrixXVertex3X3(vertex));
    }
}
