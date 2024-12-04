package Domain;

import Common.DTO.ClampZoneDTO;
import Common.DTO.VertexDTO;
import Common.Exceptions.ClampZoneException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Optional;

public class ClampZoneTest {
    @Test
    void constructor_WhenFirstNull_ClampZoneException() {
        Assertions.assertThrows(ClampZoneException.class, () -> {
            ClampZone clampZone = new ClampZone(
                    new ClampZoneDTO(null, new VertexDTO(0.0f, 0.0f, 0.0f), Optional.empty()));
        });
    }

    @Test
    void constructor_WhenSecondNull_ClampZoneException() {
        Assertions.assertThrows(ClampZoneException.class, () -> {
            ClampZone clampZone = new ClampZone(
                    new ClampZoneDTO(new VertexDTO(0.0f, 0.0f, 0.0f), null, Optional.empty()));
        });
    }

    @Test
    void constructor_WhenFirstAndSecondNull_ClampZoneException() {
        Assertions.assertThrows(ClampZoneException.class, () -> {
            ClampZone clampZone = new ClampZone(
                    new ClampZoneDTO(null, null, Optional.empty()));
        });
    }

    @Test
    void constructor_WhenFirstAndSecondNotNull_CreatesClampZone() throws ClampZoneException {
        // Arrange
        ClampZone clampZone = new ClampZone(
                new ClampZoneDTO(new VertexDTO(0.0f, 0.0f, 0.0f),
                                new VertexDTO(0.1f, 0.1f, 0.1f),
                                Optional.empty()));
        // Act

        // Assert
        Assertions.assertNotNull(clampZone);
        Assertions.assertNotNull(clampZone.getId());
        Assertions.assertEquals(2, clampZone.getZone().length);
        Assertions.assertEquals(0.0f, clampZone.getZone()[0].getX());
        Assertions.assertEquals(0.0f, clampZone.getZone()[0].getY());
        Assertions.assertEquals(0.0f, clampZone.getZone()[0].getZ());
    }

    @Test
    void constructor_WhenFirstAndSecondEqual_ClampZoneException() {
        Assertions.assertThrows(ClampZoneException.class, () -> {
            ClampZone clampZone = new ClampZone(
                    new ClampZoneDTO(new VertexDTO(0.0f, 0.0f, 0.0f),
                            new VertexDTO(0.0f, 0.0f, 0.0f),
                            Optional.empty()));
        });
    }

    @Test
    void modifyClamp_WhenClampInvalid_DoesntModify() throws ClampZoneException {
        // Arrange
        ClampZone newClamp = new ClampZone(
                new ClampZoneDTO(new VertexDTO(0.0f, 0.0f, 0.0f),
                        new VertexDTO(0.1f, 0.1f, 0.1f),
                        Optional.empty()));

        // Act

        // Assert
        Assertions.assertThrows(ClampZoneException.class, () -> {
            newClamp.modifyClamp(new ClampZoneDTO(new VertexDTO(0.0f, 0.0f, 0.0f),
                    new VertexDTO(0.0f, 0.0f, 0.0f),
                    Optional.empty()));
        });
    }

    @Test
    void modifyClamp_WhenValidClamp_ModifyPoints() throws ClampZoneException {
        // Arrange
        ClampZone newClamp = new ClampZone(new ClampZoneDTO(new VertexDTO(0.0f, 0.0f, 0.0f),
                new VertexDTO(0.1f, 0.1f, 0.1f),
                Optional.empty()));

        // Act
        newClamp.modifyClamp(new ClampZoneDTO(new VertexDTO(0.0f, 0.0f, 0.0f),
                new VertexDTO(0.2f, 0.2f, 0.2f),
                Optional.empty()));

        // Assert
        Assertions.assertEquals(0.2f, newClamp.getZone()[1].getX());
    }

    @Test
    void pointCollision_WhenPointInside_ReturnsTrue() throws ClampZoneException {
        // Arrange
        ClampZone newClamp = new ClampZone(new ClampZoneDTO(new VertexDTO(0.0f, 0.0f, 0.0f),
                new VertexDTO(3.0f, 3.0f, 0.1f),
                Optional.empty()));

        // Act
        boolean result = newClamp.pointCollision(new VertexDTO(0.05f, 0.05f, 0.05f));

        // Assert
        Assertions.assertTrue(result);
    }

    @Test
    void pointCollision_WhenPointOutside_ReturnsFalse() throws ClampZoneException {
        // Arrange
        ClampZone newClamp = new ClampZone(new ClampZoneDTO(new VertexDTO(0.0f, 0.0f, 0.0f),
                new VertexDTO(3.0f, 3.0f, 0.1f),
                Optional.empty()));

        // Act
        boolean result = newClamp.pointCollision(new VertexDTO(3.05f, 3.05f, 0.05f));

        // Assert
        Assertions.assertFalse(result);
    }
}
