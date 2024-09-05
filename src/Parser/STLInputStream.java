package Parser;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public class STLInputStream extends DataInputStream {

    private final byte[] readBuffer = new byte[4];

    /**
     * Creates a DataInputStream that uses the specified
     * underlying InputStream.
     *
     * @param in the specified input stream
     */
    public STLInputStream(InputStream in) {
        super(in);
    }

    public int readIntLittleEndian() throws IOException {
        readFully(readBuffer, 0, 4);
        int result = 0;
        for (int i = 0; i < 4; i++) {
            result += readBuffer[i] << (i * 8);
        }
        return result;
    }

    public float readFloatLittleEndian() throws IOException {
        int intBits = readIntLittleEndian();
        return Float.intBitsToFloat(intBits);
    }
}
