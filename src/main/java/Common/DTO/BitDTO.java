package Common.DTO;

import java.io.Serializable;
import java.util.Objects;

/**
 * This class is a DTO wrapper of the {@code Bit} class in order to transfer READ-ONLY informations
 *
 * @author Louis-Etienne Messier
 * @version 1.0
 * @since 2024-10-12
 */
public class BitDTO implements Serializable {
    private String name;
    private double diameter;

    public BitDTO(String name, double diameter) {
        this.name = name;
        this.diameter = diameter;
    }

    public String getName() {
        return name;
    }

    public double getDiameter() {
        return diameter;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BitDTO bitDTO = (BitDTO) o;

        if (Double.compare(diameter, bitDTO.diameter) != 0) return false;
        return Objects.equals(name, bitDTO.name);
    }
}
