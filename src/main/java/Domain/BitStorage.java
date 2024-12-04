package Domain;

import Common.DTO.BitDTO;
import Common.Exceptions.InvalidBitException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

/**
 * Represents the bit storage in a {@code CNCMachine}
 *
 * @author Kamran Charles Nayebi
 */
class BitStorage {
    private final Bit[] bitList;
    private Map<Integer, BitDTO> configuredBits;

    BitStorage() {
        this(new BitDTO[12]);
    }

    BitStorage(BitDTO[] bitList) {
        this.configuredBits = new HashMap<>();
        this.bitList = new Bit[bitList.length];

        for (int i = 0; i < bitList.length; i++) {
            if (bitList[i] != null && bitList[i].getDiameter() != 0.0f) {
                configuredBits.put(i, bitList[i]);
                this.bitList[i] = new Bit(bitList[i].getName(), bitList[i].getDiameter());
            } else {
                this.bitList[i] = new Bit();
            }
        }

        if (configuredBits.isEmpty()) {
            try {
                setBit(new Bit("Défaut", 0.5f), 0);
            } catch (InvalidBitException ignored) {}
        }
    }

    public BitDTO[] getBitList() {
        return Arrays.stream(bitList).map(Bit::getDTO).toArray(BitDTO[]::new);
    }

    /**
     * @param bit   The {@code Bit} that needs to be added to the bitList
     * @param index the index of this {@code Bit}.
     * @throws InvalidBitException When the index is not between 0 and 11
     */
    void setBit(Bit bit, int index) throws InvalidBitException {
        if (index < 0 || index > bitList.length)
            throw new InvalidBitException("L'index doit être entre 0 et 11");
        this.bitList[index] = bit;
        configuredBits.put(index, bit.getDTO());
    }

    /**
     * Updates the bit at the specified position. Called when the user wants to
     * change the name or diameter of a bit.
     *
     * @param position The position of the bit in the bitList
     * @param bitDTO   The DTO of the bit
     */
    void updateBit(int position, BitDTO bitDTO) {
        if (position < 0 || position > bitList.length)
            throw new IllegalArgumentException("L'index doit être entre 0 et 11");

        if (bitList[position] == null) {
            bitList[position] = new Bit(bitDTO.getName(), bitDTO.getDiameter());
        } else {
            bitList[position].setName(bitDTO.getName());
            bitList[position].setDiameter(bitDTO.getDiameter());
        }
        configuredBits.put(position, bitDTO);
    }

    /**
     * Removes a bit of the bt list. The bit is not set to null, but to default.
     *
     * @param position The position of the bit
     * @throws IndexOutOfBoundsException When the position is not between 0 and 11 inclusively
     * @throws InvalidBitException       If the bit to remove is a default bit
     */
    void removeBit(int position) throws IndexOutOfBoundsException, InvalidBitException {
        if (position < 0 || position > 11)
            throw new IndexOutOfBoundsException("La position doit être entre 0 et 11 inclusivement");

        if (bitList[position].getDiameter() == 0)
            throw new InvalidBitException("Il doit y avoir un bit présent pour le supprimer");

        configuredBits.remove(position);
        bitList[position] = new Bit();
    }

    Map<Integer, BitDTO> getConfiguredBits() {
        return configuredBits;
    }

    public double getBitDiameter(int bitIndex) {
        if (bitIndex < 0 || bitIndex > 11) {
            return 0; // This is an invalid index, so it means it is a border reference
        } else {
            return bitList[bitIndex].getDiameter();
        }
    }
}
