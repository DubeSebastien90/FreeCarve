package Screen;

import Annotations.VariableSource;
import Parser.ParsedSTL;
import Parser.STLParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;

import java.awt.*;
import java.io.*;
import java.util.Objects;
import java.util.stream.Stream;

public class TriangleTest {

    public static Stream<Arguments> fromParsedSTL_firstTriangle = Stream.of(
            Arguments.of("cube.stl", new Triangle(new Vertex(1, 1, 1), new Vertex(-1, 1, 1), new Vertex(-1, -1, 1), new Vertex(0, 0, 1), Color.ORANGE)));

    @ParameterizedTest
    @VariableSource("fromParsedSTL_firstTriangle")
    void fromParsedSTL_firstTriangle_parseCorrect(String ressouceFile, Triangle expected) throws IOException {
        String cubeFile = Thread.currentThread().getContextClassLoader().getResource(ressouceFile).getPath();
        InputStream inputStream = new BufferedInputStream(new FileInputStream(cubeFile));
        ParsedSTL parsedSTL = STLParser.parse(inputStream);
        Triangle[] triangles = Triangle.fromParsedSTL(parsedSTL, Color.ORANGE);
        Assertions.assertEquals(triangles[0], expected);
    }

    public static Stream<Arguments> calculateNormal_niceTriangle = Stream.of(
            Arguments.of(new Triangle(new Vertex(0, 0, 0), new Vertex(1, 0, 0), new Vertex(0, 1, 0)), new Vertex(0, 0, 1)),
            Arguments.of(new Triangle(new Vertex(0, 0, 0), new Vertex(0, 0, 0), new Vertex(0, 0, 0)), new Vertex(0, 0, 0)),
            Arguments.of(new Triangle(new Vertex(1000, 0, 0), new Vertex(0, 1000, 0), new Vertex(0, 0, 0)), new Vertex(0, 0, 1)));

    @ParameterizedTest
    @VariableSource("calculateNormal_niceTriangle")
    void calculateNormal_niceTriangle_niceNormal(Triangle triangle, Vertex expected) {
        triangle.calculateNormal();
        Assertions.assertEquals(triangle.getNormal(), expected);
    }

    public static Stream<Arguments> findBarycentric_inTriangle = Stream.of(
            Arguments.of(new Triangle(new Vertex(0, 0, 0), new Vertex(1, 0, 0), new Vertex(0, 1, 0)), 0.5, 0, new Vertex(0.5, 0.5, 0)),
            Arguments.of(new Triangle(new Vertex(0, 0, 0), new Vertex(6, 0, 0), new Vertex(3, 6, 6)), 1.5, 3, new Vertex(0.5, 0, 0.5)));

    @ParameterizedTest
    @VariableSource("findBarycentric_inTriangle")
    void findBarycentric_inTriangle_goodCoords(Triangle triangle, double xCoord, double yCoord, Vertex expected) {
        Vertex bary = triangle.findBarycentric(xCoord, yCoord);
        Assertions.assertEquals(expected, bary);
    }

}
