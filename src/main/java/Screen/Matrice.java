package Screen;

import java.util.Arrays;

public class Matrice {
    private double[] matrix;

    public Matrice(double[] matrix) {
        this.matrix = matrix;
    }

    public Matrice multiplyMatrice3x3(Matrice other) {
       double[] newMatrice = new double[9];
        int element = 0;
        for (int i = 0; i < 3; i++) {
            int place = 0;
            int start = 0;
            int value = 0;
            for (int j = 0; j < 9; j++) {
                value += this.matrix[(i*3)+place/3] * other.getMatrix()[place];
                if (place + 3 >= 9) {
                    newMatrice[element] = value;
                    value = 0;
                    element++;
                    start++;
                    place = start;

                } else {
                    place += 3;
                }
            }
        }
        return new Matrice(newMatrice);
    }

    public double[] getMatrix() {
        return matrix;
    }

    public void setMatrix(double[] matrix) {
        this.matrix = matrix;
    }

    public Vertex matriceXVertex3x3(Vertex vertex) {

        double xComponent = vertex.getX() * matrix[0] + vertex.getY() * matrix[3] + vertex.getZ() * matrix[6];
        double yComponent = vertex.getX() * matrix[1] + vertex.getY() * matrix[4] + vertex.getZ() * matrix[7];
        double zComponent = vertex.getX() * matrix[2] + vertex.getY() * matrix[5] + vertex.getZ() * matrix[8];
        return new Vertex(xComponent, yComponent, zComponent);
    }

    @Override
    public String toString() {
        return "Matrice{" +
                "matrix=" + Arrays.toString(matrix) +
                '}';
    }
}
