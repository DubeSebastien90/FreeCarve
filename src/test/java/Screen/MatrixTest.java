package Screen;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MatrixTest {
    @Test
    void matrix_multiply_matrix_test() {
        Vertex vertex = new Vertex(0, 0, 0);
        Matrix matrix = new Matrix(new double[]{1, 2, 3,
                4, 5, 6,
                7, 8, 9});
        Vertex answer = matrix.matriceXVertex3x3(vertex);
        Assertions.assertEquals(new Vertex(0, 0, 0), answer);
        vertex = new Vertex(1, 1, 1);
        answer = matrix.matriceXVertex3x3(vertex);
        Assertions.assertEquals(new Vertex(12, 15, 18), answer);
        vertex = new Vertex(-1, 5, 9);
        answer = matrix.matriceXVertex3x3(vertex);
        Assertions.assertEquals(new Vertex(82, 95, 108), answer);

    }
}
