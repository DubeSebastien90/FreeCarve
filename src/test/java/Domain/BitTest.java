package Domain;

import Domain.BitDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BitTest {
    private Bit bit;
    @BeforeEach
    void setUp() {
        bit = new Bit("test", 1.0f);
    }

    @Test
    void constructor_WhenDiameterNegative_ThrowsArgumentException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Bit bit = new Bit("test", -1.0f);
        });
    }

    @Test
    void constructor_WhenDiameterZero_ThrowsArgumentException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Bit bit = new Bit("test", 0.0f);
        });
    }

    @Test
    void constructor_WhenDiameterPositive_CreatesBit() {
        Assertions.assertEquals("test", bit.getName());
        Assertions.assertEquals(1.0f, bit.getDiameter());
    }

    @Test
    void constructor_WhenNameNull_ThrowsArgumentException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Bit bit = new Bit(null, 1.0f);
        });
    }

    @Test
    void constructor_WhenNameEmpty_ThrowsArgumentException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Bit bit = new Bit("", 1.0f);
        });
    }

    @Test
    void defaultConstructor_WhenCalled_CreatesBit() {
        Bit bit = new Bit();
        Assertions.assertEquals("Aucun outil assigné", bit.getName());
        Assertions.assertEquals(0.0f, bit.getDiameter());
    }

    @Test
    void setName_WhenNameEmpty_ThrowsArgumentException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            bit.setName("");
        });
    }

    @Test
    void setName_WhenNameNull_ThrowsArgumentException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            bit.setName(null);
        });
    }

    @Test
    void getName_WhenNameSet_ReturnsName() {
        Assertions.assertEquals("test", bit.getName());
    }

    @Test
    void setDiameter_WhenDiameterIsNegative_ThrowsArgumentException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            bit.setDiameter(-1.0f);
        });
    }

    @Test
    void setDiameter_WhenDiameterIsZero_ThrowsArgumentException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            bit.setDiameter(0.0f);
        });
    }

    void setDiameter_WhenDiameterIsPositive_SetsDiameter() {
        bit.setDiameter(2.0f);
        Assertions.assertEquals(2.0f, bit.getDiameter());
    }

    @Test
    void getDiameter_WhenDiameterSet_ReturnsDiameter() {
        Assertions.assertEquals(1.0f, bit.getDiameter());
    }

    @Test
    void getBitDTO_WhenCalled_ReturnsDTO() {
        // Arrange
        // Act
        BitDTO dto = bit.getBitDTO();
        // Assert
        Assertions.assertEquals(BitDTO.class, dto.getClass());
    }

    @Test
    void getBitDTO_WhenCalled_ReturnsDTOWithCorrectValues() {
        // Arrange
        // Act
        BitDTO dto = bit.getBitDTO();
        // Assert
        Assertions.assertEquals("test", dto.getName());
        Assertions.assertEquals(1.0f, dto.getDiameter());
    }
}
