package Parser;

import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

class STLInputStreamTest {

    @Test
    void readIntLittleEndian_MaxInt_DoesntOverflow() throws IOException {
        // Arrange
        InputStream is = new ByteArrayInputStream(new byte[] {(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF});
        STLInputStream stlInputStream = new STLInputStream(is);
        // Act
        int value = stlInputStream.readIntLittleEndian();
        // Assert
        Assertions.assertEquals(value, (int)0xFFFFFFFF);
    }

    @Test
    void readFloatLittleEndian_HappyPath_ReadsLittleEndian() throws IOException {
        // Arrange
        InputStream is = new ByteArrayInputStream(new byte[] {(byte) 0x0, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF});
        STLInputStream stlInputStream = new STLInputStream(is);
        // Act
        int value = stlInputStream.readIntLittleEndian();
        // Assert
        Assertions.assertEquals(value, (int)0xFFFFFF00);
    }

//    @Test
//    void readFloatLittleEndian_HappyPath_ReadsLittleEndian() throws IOException {
//        // Arrange
//        InputStream is = new ByteArrayInputStream(new byte[] {(byte) 0x0, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF});
//        STLInputStream stlInputStream = new STLInputStream(is);
//        // Act
//        int value = stlInputStream.readIntLittleEndian();
//        // Assert
//        Assertions.assertEquals(value, (int)0xFFFFFF00);
//    }
}