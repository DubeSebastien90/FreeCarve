package Common.DTO;

/**
 * This class is a DTO wrapper of the {@code Bit} class in order to transfer READ-ONLY informations
 *
 * @author Louis-Etienne Messier
 * @version 1.0
 * @since 2024-10-12
 */
public class BitDTO {
    private String name;
    private float diameter;

    public BitDTO(String name, float diameter) {
        this.name = name;
        this.diameter = diameter;
    }

    public String getName() {
        return name;
    }

    public float getDiameter() {
        return diameter;
    }
}
