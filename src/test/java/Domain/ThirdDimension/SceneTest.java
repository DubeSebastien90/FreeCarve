package Domain.ThirdDimension;

import Common.VertexDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.security.InvalidKeyException;
import java.util.List;
import java.util.UUID;

class SceneTest {
    @Test
    void getMesh_HappyPath_ReturnsMesh() throws InvalidKeyException {
        // Arrange
        Mesh mesh = Mesh.createBox(Vertex.zero(), 1 ,1 ,1 , Color.RED);
        Scene scene = new Scene(List.of(mesh));

        // Act
        Mesh result = scene.getMesh(mesh.getId());

        // Assert
        Assertions.assertEquals(mesh, result);
    }

    @Test
    void getMesh_InvalidId_ThrowsException(){
        // Arrange
        Mesh mesh = Mesh.createBox(Vertex.zero(), 1 ,1 ,1 , Color.RED);
        Scene scene = new Scene(List.of(mesh));

        // Act and Assert
        Assertions.assertThrows(InvalidKeyException.class, ()->scene.getMesh(UUID.randomUUID()));
    }

    @Test
    void applyTransform_HappyPath_AppliesAllTransformations() {
        // Arrange
        VertexDTO positionChange = new VertexDTO(1,1,1);
        VertexDTO rotationChange = new VertexDTO(-1,-1,1);
        float scaleChange = 1;

        Mesh mesh = Mesh.createBox(new Vertex(1,1,1), 1, 1, 1, Color.RED);
        Scene scene = new Scene(List.of(mesh));

        mesh.setRotationEuler(new Vertex(0.5, 0.5, 0.5));
        mesh.setScale(2);
        // Act
        Assertions.assertDoesNotThrow(()->scene.applyTransform(mesh.getId(), positionChange, rotationChange, scaleChange));

        // Assert
        Assertions.assertEquals(new Vertex(2, 2, 2), mesh.getPosition());
        Assertions.assertEquals(new Vertex(-0.5, -0.5, 1.5), mesh.getRotationEuler());
        Assertions.assertEquals(3, mesh.getScale());
    }
}