package Domain;

import Common.DTO.BitDTO;
import Common.DTO.ProjectStateDTO;
import Common.DTO.VertexDTO;
import Common.Exceptions.InvalidBitException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class BitStorageTest {

    private BitStorage bitStorage;

    @BeforeEach
    void setUp() {
        bitStorage = new BitStorage();
    }

    @Test
    void testConstructor() {
        // Arrange
        // Act
        // Assert
        Assertions.assertEquals(12, bitStorage.getBitList().length);
    }

    @Test
    void updateBit_WhenPositionNegative_ThrowsException() {
        // Arrange
        BitDTO oldBit = new BitDTO("test", 1.0f);
        // Act
        // Assert
        Assertions.assertThrows(Exception.class, () -> {
            bitStorage.updateBit(-1, oldBit);
        });
    }

    @Test
    void updateBit_WhenNameEmpty_ThrowsException() {
        // Arrange
        BitDTO newBit = new BitDTO("", 1.0f);
        // Act
        // Assert
        Assertions.assertThrows(Exception.class, () -> {
            bitStorage.updateBit(0, newBit);
        });
    }

    @Test
    void updateBit_WhenBitCorrect_BitUpdated() {
        // Arrange
        BitDTO newBit = new BitDTO("test", 1.0f);
        // Act
        bitStorage.updateBit(0, newBit);
        // Assert
        Assertions.assertEquals("test", bitStorage.getBitList()[0].getName());
        Assertions.assertEquals(1.0f, bitStorage.getBitList()[0].getDiameter());
    }
    @Test
    void removeBit_WhenBitIsValid_SetsBitToDefault() throws Exception {
        // Arrange
        bitStorage.setBit(new Bit("Test", 0.5f), 0);
        Bit defaultBit = new Bit();

        // Act
        bitStorage.removeBit(0);

        // Assert
        Assertions.assertEquals(bitStorage.getBitList()[0].getDiameter(), defaultBit.getDiameter());
        Assertions.assertEquals(bitStorage.getBitList()[0].getName(), defaultBit.getName());
    }

    @Test
    void removeBit_WhenPositionNegative_ThrowsIndexOutOfBoundException(){
        // Arrange

        // Act

        // Assert
        Assertions.assertThrows(IndexOutOfBoundsException.class, ()-> {
            bitStorage.removeBit(-1);});
    }

    @Test
    void removeBit_WhenPositionBiggerThen12_ThrowsIndexOutOfBoundException(){
        // Arrange

        // Act

        // Assert
        Assertions.assertThrows(IndexOutOfBoundsException.class, ()-> {
            bitStorage.removeBit(12);});
    }

    @Test
    void removeBit_WhenRemoveBitIsDefault_ThrowsInvalidBitException() throws Exception {
        // Arrange

        // Act
        bitStorage.setBit(new Bit(), 1);

        // Assert
        Assertions.assertThrows(InvalidBitException.class, ()-> {
            bitStorage.removeBit(1);});
    }

    @Test
    void getConfiguredBits_WhenBitsConfigured_IsPutInMap() throws InvalidBitException {
        // Arrange
        bitStorage.setBit(new Bit("Test", 0.2f), 1);

        // Act
        Map<Integer, BitDTO> configuredBits = bitStorage.getConfiguredBits();

        // Assert
        Assertions.assertEquals(configuredBits.size(), 2);
        Assertions.assertEquals(configuredBits.get(1).getDiameter(), 0.2f);
    }
}
