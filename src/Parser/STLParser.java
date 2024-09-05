package Parser;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Arrays;

public class STLParser {
    private static final int BYTES_HEADER = 80;
    private static final int BYTES_ATTRIBUTE = 2;
    public static final int DIMENSIONS = 3;
    public static final int SIDES = 3;
    private static final float SCALE = 100;

    public static ParsedSTL parse(DataInputStream dis) throws IOException {
        STLInputStream stlis = new STLInputStream(dis);

        stlis.skipBytes(BYTES_HEADER); // Skip header

        int nbTriangles = stlis.readIntLittleEndian();

        float[][] vertices = new float[nbTriangles*SIDES][DIMENSIONS];
        float[][] normals = new float[nbTriangles][DIMENSIONS];

        for (int i = 0; i < nbTriangles; i++) {
            for (int k = 0; k < DIMENSIONS; k++) {
                normals[i][k] = stlis.readFloatLittleEndian();
            }

            for (int j = 0; j < SIDES; j++) {
                for (int k = 0; k < DIMENSIONS; k++) {
                    vertices[i + j][k] = stlis.readFloatLittleEndian() * SCALE;
                }
            }
            dis.skipBytes(BYTES_ATTRIBUTE); // Skip attribute byte count
        }

        return new ParsedSTL(vertices, normals);
    }

    public static void printParsedSTL(ParsedSTL parsedSTL){
        System.out.println("Number of triangles : " + parsedSTL.normals().length);
        for (int i = 0; i < parsedSTL.normals().length; i++) {
            System.out.println("Triangle " + i);
            System.out.print("Normals : ");
            System.out.println(Arrays.toString(parsedSTL.normals()[i]));
            System.out.println("Vertices : ");
            System.out.println(Arrays.toString(parsedSTL.vertices()[i*3]));
            System.out.println(Arrays.toString(parsedSTL.vertices()[i*3+1]));
            System.out.println(Arrays.toString(parsedSTL.vertices()[i*3+2]));
        }
    }
}

