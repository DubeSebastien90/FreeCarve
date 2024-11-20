package Domain.IO;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

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