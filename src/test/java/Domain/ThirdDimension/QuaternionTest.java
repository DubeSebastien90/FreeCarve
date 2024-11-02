package Domain.ThirdDimension;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class QuaternionTest {

    @Test
    void normalize_HappyPath_LengthOfOne() {
        // Arrange
        Quaternion quaternion = new Quaternion(1, 2, 3, 4);

        // Act
        Quaternion normalizedQuaternion = quaternion.normalize();
        double length = normalizedQuaternion.length();

        // Assert
        assertEquals(1.0, length, 1e-6);
    }

    @Test
    void multiply_TwoQuaternions_CorrectResult() {
        // Arrange
        Quaternion q1 = new Quaternion(1, 2, 3, 4);
        Quaternion q2 = new Quaternion(5, 6, 7, 8);

        // Act
        Quaternion result = q1.multiply(q2);

        // Assert
        assertEquals(-60, result.getW());
        assertEquals(12, result.getX());
        assertEquals(30, result.getY());
        assertEquals(24, result.getZ());
    }

    @Test
    void multiply_QuaternionAndScalar_CorrectResult() {
        // Arrange
        Quaternion quaternion = new Quaternion(1, 2, 3, 4);
        double scalar = 2.0;

        // Act
        Quaternion result = quaternion.multiply(scalar);

        // Assert
        assertEquals(2, result.getW());
        assertEquals(4, result.getX());
        assertEquals(6, result.getY());
        assertEquals(8, result.getZ());
    }

    @Test
    void congugate_HappyPath_InvertsImaginaryParts() {
        // Arrange
        Quaternion quaternion = new Quaternion(1, 2, 3, 4);

        // Act
        Quaternion result = quaternion.congugate();

        // Assert
        assertEquals(1, result.getW());
        assertEquals(-2, result.getX());
        assertEquals(-3, result.getY());
        assertEquals(-4, result.getZ());
    }

    @Test
    void fromEulerAngles_givenEulerAngles_CorrectQuaternionReturned() {
        // Arrange
        Vertex eulerAngles = new Vertex(Math.PI/2, 0, 0);

        // Act
        Quaternion result = Quaternion.fromEulerAngles(eulerAngles);

        // Assert
        assertEquals(Math.cos(Math.toRadians(45)), result.getW(), 1e-6);
        assertEquals(Math.sin(Math.toRadians(45)), result.getX(), 1e-6);
        assertEquals(0, result.getY());
        assertEquals(0, result.getZ());
    }
}
