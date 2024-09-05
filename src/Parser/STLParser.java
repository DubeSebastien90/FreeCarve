package Parser;

import java.io.DataInputStream;
import java.io.IOException;

public class STLParser {
    private static final int BYTES_HEADER = 80;
    private static final int BYTES_ATTRIBUTE = 2;
    private static final int DIMENSIONS = 3;
    private static final int SIDES = 3;

    public static ParsedSTL parse(DataInputStream dis) throws IOException {
        STLInputStream stlis = new STLInputStream(dis);

        stlis.skipBytes(BYTES_HEADER); // Skip header

        int nbTriangles = stlis.readIntLittleEndian();
        System.out.println("Number of triangles : " + nbTriangles);

        float[][] points = new float[nbTriangles*SIDES][DIMENSIONS];
        int[][] vectors = new int[nbTriangles][DIMENSIONS];
        float[][] normals = new float[nbTriangles][DIMENSIONS];

        for (int i = 0; i < nbTriangles; i++) {
            System.out.println("Triangle " + i);
            System.out.print("Normal : [");
            for (int k = 0; k < DIMENSIONS; k++) {
                normals[i][k] = stlis.readFloatLittleEndian();
                System.out.print(points[i][k] + ", ");
            }
            System.out.println("]");

            for (int j = 0; j < SIDES; j++) {
                System.out.print("[");
                for (int k = 0; k < DIMENSIONS; k++) {
                    points[i + j][k] = stlis.readFloatLittleEndian();
                    System.out.print(points[i + j][k] + ", ");
                }
                System.out.println("]");
            }
            dis.skipBytes(BYTES_ATTRIBUTE); // Skip attribute byte count
        }

        return new ParsedSTL(points, vectors, normals);
    }
}

