package Domain.ThirdDimension;

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

    public static final Matrix RIGHT_ROTATION = new Matrix(new double[]{
            Math.cos(0.05), 0, -Math.sin(0.05),
            0, 1, 0,
            Math.sin(0.05), 0, Math.cos(0.05)
    });
    public static final Matrix LEFT_ROTATION = new Matrix(new double[]{
            Math.cos(-0.05), 0, -Math.sin(-0.05),
            0, 1, 0,
            Math.sin(-0.05), 0, Math.cos(-0.05)
    });
    public static final Matrix UP_ROTATION = new Matrix(new double[]{
            1, 0, 0,
            0, Math.cos(0.05), Math.sin(0.05),
            0, -Math.sin(0.05), Math.cos(0.05)
    });
    public static final Matrix DOWN_ROTATION = new Matrix(new double[]{
            1, 0, 0,
            0, Math.cos(-0.05), Math.sin(-0.05),
            0, -Math.sin(-0.05), Math.cos(-0.05)
    });

    /**
     * Constructs a new {@code Matrix} with the specified array of doubles.
     *
     * @param matrix the array of doubles that compose the matrix
     */
    public Matrix(double[] matrix) {
        setMatrix(matrix);
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
    public Vertex matrixXVertex3X3(Vertex vertex) {
        if (matrix.length != 9){
            throw new ArithmeticException("Matrix must be 3x3");
        }
        double xComponent = vertex.getX() * matrix[0] + vertex.getY() * matrix[3] + vertex.getZ() * matrix[6];
        double yComponent = vertex.getX() * matrix[1] + vertex.getY() * matrix[4] + vertex.getZ() * matrix[7];
        double zComponent = vertex.getX() * matrix[2] + vertex.getY() * matrix[5] + vertex.getZ() * matrix[8];
        return new Vertex(xComponent, yComponent, zComponent);
    }

    /**
     * Returns a rotation matrix to apply on a mesh for it to rotate on a certain angle
     *
     * @param v    the axis to rotate around
     * @param radians the radians of the rotation
     * @return the rotation matrix to apply on the vectors
     */
    public static Matrix getRotationMatrixAroundVector(Vertex v, double radians) {
        double ux = v.getX(), uy = v.getY(), uz = v.getZ();
        double c = Math.cos(radians), s = Math.sin(radians);
        return new Matrix(new double[]{
                Math.pow(ux, 2) * (1 - c) + c, ux * uy * (1 - c) - uz * s, ux * uz * (1 - c) + uy * s,
                ux * uy * (1 - c) + uz * s, Math.pow(uy, 2) * (1 - c) + c, uy * uz * (1 - c) - ux * s,
                ux * uz * (1 - c) - uy * s, uy * uz * (1 - c) + ux * s, Math.pow(uz, 2) * (1 - c) + c
        });
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
