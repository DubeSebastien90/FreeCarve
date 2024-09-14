package Screen;

import java.util.Arrays;

/**
 * The {@code Matrix} class provides methods to use a double matrice effectively and to do mathematics operation with matrices
 *
 * @author Adam Côté
 * @version 1.0
 * @since 2024-09-07
 */
public class Matrix {
    private double[] matrix;

    /**
     * Constructs a new {@code Matrix} with the specified array of doubles.
     *
     * @param matrix the array of doubles that compose the matrix
     */
    public Matrix(double[] matrix) {
        setMatrix(matrix);
    }

    /**
     * Calculates the resulting matrix from the multiplication of two 3x3 matrices, one being this instance of {@code Matrix}.
     * <b>Note that it can only multiply 3x3 matrices.</b>
     *
     * @param other the other {@code Matrix}
     * @return the resulting {@code Matrix}
     */
    public Matrix multiplyMatrice3x3(Matrix other) {
        double[] newMatrix = new double[9];
        int element = 0;
        for (int i = 0; i < 3; i++) {
            int place = 0;
            int start = 0;
            int value = 0;
            for (int j = 0; j < 9; j++) {
                value += this.matrix[(i * 3) + place / 3] * other.getMatrix()[place];
                if (place + 3 >= 9) {
                    newMatrix[element] = value;
                    value = 0;
                    element++;
                    start++;
                    place = start;

                } else {
                    place += 3;
                }
            }
        }
        return new Matrix(newMatrix);
    }

    /**
     * Returns the values of the current {@code Matrix}
     *
     * @return the values
     */
    public double[] getMatrix() {
        return matrix;
    }

    /**
     * Sets the values of the matrix
     *
     * @param matrix the new values of the matrix
     */
    public void setMatrix(double[] matrix) {
        this.matrix = matrix;
    }

    /**
     * Multiplies this instance of {@code Matrix} with a {@code Vertex}.
     * <b>Note that the matrix must be 3x3 or it won't work.</b>
     *
     * @param vertex the {@code Vertex} to be multiplied
     * @return the resulting {@code Vertex}
     */
    public Vertex matriceXVertex3x3(Vertex vertex) {

        double xComponent = vertex.getX() * matrix[0] + vertex.getY() * matrix[3] + vertex.getZ() * matrix[6];
        double yComponent = vertex.getX() * matrix[1] + vertex.getY() * matrix[4] + vertex.getZ() * matrix[7];
        double zComponent = vertex.getX() * matrix[2] + vertex.getY() * matrix[5] + vertex.getZ() * matrix[8];
        return new Vertex(xComponent, yComponent, zComponent, vertex.getU(), vertex.getV());
    }

    /**
     * @return a string representation of the {@code Matrix} object
     */
    @Override
    public String toString() {
        return "Matrix{" +
                "matrix=" + Arrays.toString(matrix) +
                '}';
    }
}
