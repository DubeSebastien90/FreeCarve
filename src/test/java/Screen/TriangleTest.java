package Screen;

import Parser.ParsedSTL;
import Parser.STLParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.io.*;
import java.util.Objects;

public class TriangleTest {

    @Test
    void triangle_from_parse_stl() throws IOException {
        String cubeFile = Thread.currentThread().getContextClassLoader().getResource("cube.stl").getPath();
        InputStream inputStream = new BufferedInputStream(new FileInputStream(cubeFile));
        ParsedSTL parsedSTL = STLParser.parse(inputStream);
        Triangle[] triangles = Triangle.fromParsedSTL(parsedSTL, Color.ORANGE);
        Assertions.assertEquals(triangles[0], new Triangle(new Vertex(1, 1, 1), new Vertex(-1, 1, 1), new Vertex(-1, -1, 1), new Vertex(0, 0, 1), Color.ORANGE));
    }

    @Test
    void triangle_calculate_normal_test() {
        Triangle triangle1 = new Triangle(new Vertex(0, 0, 0), new Vertex(1, 0, 0), new Vertex(0, 1, 0));
        triangle1.calculateNormal();
        Assertions.assertEquals(triangle1.getNormal(), new Vertex(0, 0, 1));
        Triangle triangle2 = new Triangle(new Vertex(0, 0, 0), new Vertex(0, 0, 0), new Vertex(0, 0, 0));
        triangle2.calculateNormal();
        Assertions.assertEquals(triangle2.getNormal(), new Vertex(0, 0, 0));
    }

}
