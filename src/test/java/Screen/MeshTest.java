package Screen;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.awt.*;

public class MeshTest {

    @Test
    void getTransformedTriangle_TranslationOnly_TranslatesCorrectly() {
        // Arrange
        Triangle triangle = new Triangle(new Vertex(0, 0, 0), new Vertex(15, 15, 0), new Vertex(15, 0, 0));
        Vertex translationVertex = new Vertex(1, 1, 1);
        Mesh mesh = TestHelper.createMesh();
        mesh.setPosition(translationVertex);

        // Act
        Triangle result = mesh.getTransformedTriangle(triangle);

        // Assert
        Assertions.assertEquals(translationVertex, result.getVertex(0));
        Assertions.assertEquals(new Vertex(16, 16, 1), result.getVertex(1));
        Assertions.assertEquals(new Vertex(16, 1, 1), result.getVertex(2));

    }

    @Test
    void getTransformedTriangle_RotationOnly_RotatesCorrectly() {
        // Arrange
        Mesh mesh = Mesh.createBox(new Vertex(0, 0, 0), 100, 100, 100, Color.BLUE);
        mesh.setRotationEuler(new Vertex(0, Math.PI, 0));
        Triangle triangle = mesh.getLocalTriangles().getFirst();

        // Act
        Triangle result = mesh.getTransformedTriangle(triangle);

        // Assert
        Assertions.assertEquals(new Vertex(50, 50, -50), result.getVertex(1));
    }
}
