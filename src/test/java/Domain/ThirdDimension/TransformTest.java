package Domain.ThirdDimension;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class TransformTest {

    @Test
    void setRotationEuler_HappyPath_SetsRotation() {
        // Arrange
        Transform transform = Mesh.createBox(Vertex.zero(), 1, 1, 1, Color.GRAY);
        Vertex rotation = new Vertex(Math.PI/2, 0, 0);
        // Act
        transform.setRotationEuler(rotation);
        // Assert
        Assertions.assertEquals(rotation, transform.getRotationEuler());
    }

    @Test
    void setRotationEuler_OutsideBounds_ClampsValue() {
        // Arrange
        Transform transform = Mesh.createBox(Vertex.zero(), 1, 1, 1, Color.GRAY);
        Vertex rotation = new Vertex(2*Math.PI,2*Math.PI, 2*Math.PI);
        // Act
        transform.setRotationEuler(rotation);
        // Assert
        Assertions.assertNotEquals(rotation, transform.getRotationEuler());
        Assertions.assertEquals(new Vertex(0, 0, 0), transform.getRotationEuler());
    }

    @Test
    void setPosition_HappyPath_SetsPosition() {
        // Arrange
        Transform transform = Mesh.createBox(Vertex.zero(), 1, 1, 1, Color.GRAY);
        Vertex position = new Vertex(1,1, 1);
        // Act
        transform.setPosition(position);
        // Assert
        Assertions.assertNotEquals(Vertex.zero(), transform.getPosition());
        Assertions.assertEquals(position, transform.getPosition());
    }

    @Test
    void setScale_HappyPath_SetsScale() {
        // Arrange
        Transform transform = Mesh.createBox(Vertex.zero(), 1, 1, 1, Color.GRAY);
        float scale = 2;
        // Act
        transform.setScale(scale);
        // Assert
        Assertions.assertNotEquals(1, transform.getScale());
        Assertions.assertEquals(scale, transform.getScale());
    }

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
        Mesh mesh = Mesh.createBox(new Vertex(0, 0, 0), 100, 100, 100, Color.GRAY);
        mesh.setRotationEuler(new Vertex(0, Math.PI, 0));
        Triangle triangle = mesh.getLocalTriangles().getFirst();

        // Act
        Triangle result = mesh.getTransformedTriangle(triangle);

        // Assert
        Assertions.assertEquals(new Vertex(-50, 50, 50), result.getVertex(1));
    }

    @Test
    void getTransformedTriangle_ScaleOnly_ScalesCorrectly() {
        // Arrange
        Mesh mesh = Mesh.createBox(new Vertex(0, 0, 0), 100, 100, 100, Color.GRAY);
        mesh.setScale(10);
        Triangle triangle = mesh.getLocalTriangles().getFirst();

        // Act
        Triangle result = mesh.getTransformedTriangle(triangle);

        // Assert
        Assertions.assertEquals(new Vertex(-500, 500, 500), result.getVertex(1));
    }

    @Test
    void pan() {
        // Given
        Mesh mesh = Mesh.createBox(new Vertex(0,0,2), 1, 1, 1, Color.GRAY);
        // When
        mesh.pan((float)Math.PI/2, (float)Math.PI/2);
        // Then
        Assertions.assertEquals(new Vertex(0, -2, 0), mesh.getPosition());
        Assertions.assertEquals(new Vertex(Math.PI/2, Math.PI/2, 0), mesh.getRotationEuler());
    }
}