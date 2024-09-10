package Parser;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

class STLParserTest {

    @Test
    void parse_StandardBlenderCube_ParsesCorrectly() throws IOException {
        // Arrange
        String cubeFile = Thread.currentThread().getContextClassLoader().getResource("cube.stl").getPath();
        InputStream inputStream = new BufferedInputStream(new FileInputStream(cubeFile));
        // Act
        ParsedSTL parsedSTL = STLParser.parse(inputStream);
        // Assert
        Assertions.assertArrayEquals(parsedSTL.normals(), new float[][]{
                {0f, 0f, 1f},
                {0f, -0f, 1f},
                {0f, -1f, 0f},
                {0f, -1f, 0f},
                {-1f, -0f, -0f},
                {-1f, -0f, 0f},
                {0f, 0f, -1f},
                {0f, 0f, -1f},
                {1f, -0f, 0f},
                {1f, -0f, 0f},
                {0f, 1f, 0f},
                {0f, 1f, -0f}});
        Assertions.assertArrayEquals(parsedSTL.vertices(), new float[][]{
                {1f, 1f, 1f}, {-1f, 1f, 1f}, {-1f, -1f, 1f},
                {1f, 1f, 1f}, {-1f, -1f, 1f}, {1f, -1f, 1f},
                {1f, -1f, -1f}, {1f, -1f, 1f}, {-1f, -1f, 1f},
                {1f, -1f, -1f}, {-1f, -1f, 1f}, {-1f, -1f, -1f},
                {-1f, -1f, -1f}, {-1f, -1f, 1f}, {-1f, 1f, 1f},
                {-1f, -1f, -1f}, {-1f, 1f, 1f}, {-1f, 1f, -1f},
                {-1f, 1f, -1f}, {1f, 1f, -1f}, {1f, -1f, -1f},
                {-1f, 1f, -1f}, {1f, -1f, -1f}, {-1f, -1f, -1f},
                {1f, 1f, -1f}, {1f, 1f, 1f}, {1f, -1f, 1f},
                {1f, 1f, -1f}, {1f, -1f, 1f}, {1f, -1f, -1f},
                {-1f, 1f, -1f}, {-1f, 1f, 1f}, {1f, 1f, 1f},
                {-1f, 1f, -1f}, {1f, 1f, 1f}, {1f, 1f, -1f}});
    }
}