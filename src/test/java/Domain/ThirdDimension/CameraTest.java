package Domain.ThirdDimension;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;
import java.util.List;
import java.awt.*;
import java.util.Optional;
import java.util.UUID;


public class CameraTest {

    @Test
    void renderImage_Image_RendersCorrectly() {
        // Arrange
        Scene scene = new Scene(List.of(Mesh.createBox(Vertex.zero(), 1, 1, 1, Color.RED)));
        Camera camera = new Camera(scene);
        BufferedImage image = new BufferedImage(4, 4, BufferedImage.TYPE_INT_ARGB);

        // Act
        camera.renderImage(image, VertexDTO.zero());

        // Assert
        Assertions.assertEquals(0, image.getRGB(0, 0));
        Assertions.assertEquals(0, image.getRGB(0, 1));
        Assertions.assertEquals(0, image.getRGB(0, 2));
        Assertions.assertEquals(0, image.getRGB(0, 3));

        Assertions.assertEquals(0, image.getRGB(1, 0));
        Assertions.assertEquals(0, image.getRGB(1, 1));
        Assertions.assertEquals(0, image.getRGB(1, 2));
        Assertions.assertEquals(0, image.getRGB(1, 3));

        Assertions.assertEquals(0, image.getRGB(2, 0));
        Assertions.assertEquals(0, image.getRGB(2, 1));
        Assertions.assertEquals(Color.RED.getRGB(), image.getRGB(2, 2));
        Assertions.assertEquals(0, image.getRGB(2, 3));

        Assertions.assertEquals(0, image.getRGB(3, 0));
        Assertions.assertEquals(0, image.getRGB(3, 1));
        Assertions.assertEquals(0, image.getRGB(3, 2));
        Assertions.assertEquals(0, image.getRGB(3, 3));
    }

    @Test
    void renderImage_NormalFacingAway_MinimumLighting() {
        // Arrange
        Mesh mesh = Mesh.createBox(Vertex.zero(), 1, 1, 1, Color.RED);
        Scene scene = new Scene(List.of(mesh));

        mesh.getLocalTriangles().forEach(triangle -> triangle.setNormal(new Vertex(1, 0, 0)));

        Camera camera = new Camera(scene);
        BufferedImage image = new BufferedImage(4, 4, BufferedImage.TYPE_INT_ARGB);

        // Act
        camera.renderImage(image, VertexDTO.zero());

        // Assert
        Assertions.assertEquals(new Color(Camera.MIN_LIGHTING, 0, 0).getRGB(), image.getRGB(2, 2));
    }

    @Test
    void renderImage_MousePosValid_ReturnsMeshId() {
        // Arrange
        Mesh mesh = Mesh.createBox(Vertex.zero(), 1, 1, 1, Color.RED);
        Scene scene = new Scene(List.of(mesh));
        Camera camera = new Camera(scene);
        BufferedImage image = new BufferedImage(4, 4, BufferedImage.TYPE_INT_ARGB);

        VertexDTO mousePos = new VertexDTO(2, 2, 0);

        // Act
        Optional<UUID> result = camera.renderImage(image, mousePos);

        // Assert
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(mesh.getId(), result.get());
    }

    @Test
    void renderImage_MousePosInvalid_ReturnsNothing() {
        // Arrange
        Mesh mesh = Mesh.createBox(Vertex.zero(), 1, 1, 1, Color.RED);
        Scene scene = new Scene(List.of(mesh));
        Camera camera = new Camera(scene);
        BufferedImage image = new BufferedImage(4, 4, BufferedImage.TYPE_INT_ARGB);

        VertexDTO mousePos = new VertexDTO(1, 2, 0);

        // Act
        Optional<UUID> result = camera.renderImage(image, mousePos);
        
        // Assert
        Assertions.assertFalse(result.isPresent());
    }
}
