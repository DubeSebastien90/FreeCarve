package Screen;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.awt.*;

public class MeshTest {

    @Test
    void translateTriangles_HappyPath_TranslatesCorrectly() {
        // Arrange
        Renderer.resetWorldRotation();
        Vertex translationVertex = new Vertex(1, 1, 1);
        Mesh mesh = TestHelper.createMesh();
        mesh.localTriangles.getFirst().setVertices(new Vertex[]{new Vertex(0, 0, 0), new Vertex(15, 15, 0), new Vertex(15, 0, 0)});
        mesh.localTriangles.getLast().setVertex(new Vertex(100, 100, 100), 0);

        // Act
        mesh.translateTriangles(translationVertex);

        // Assert
        Assertions.assertEquals(translationVertex, mesh.getLocalTriangles().getFirst().getVertex(0));
        Assertions.assertEquals(new Vertex(16, 16, 1), mesh.getLocalTriangles().getFirst().getVertex(1));
        Assertions.assertEquals(new Vertex(16, 1, 1), mesh.getLocalTriangles().getFirst().getVertex(2));

        Assertions.assertEquals(new Vertex(101, 101, 101), mesh.getLocalTriangles().getLast().getVertex(0));
    }

    @Test
    void rotateTriangles_HappyPath_RotatesCorrectly() {
        // Arrange
        Mesh mesh = Mesh.createBox(new Vertex(0, 0, 0), 100, 100, 100, Color.BLUE);

        // Act
        mesh.rotateTriangles(new Vertex(1, 0, 0), Math.PI);

        // Assert
        Triangle firstTriangle = mesh.getLocalTriangles().getFirst();
        Triangle secondTriangle = mesh.getLocalTriangles().get(1);

        Assertions.assertEquals(firstTriangle.getVertex(1), new Vertex(0, -100, -100));
        Assertions.assertEquals(secondTriangle.getVertex(2), new Vertex(0, -100, -100));

    }
}
