package Domain;

import Common.DTO.BitDTO;

/**
 * The {@code Tool} class encapsulates a tool (bit) object and all of it's attributes
 *
 * @author Louis-Etienne Messier
 * @version 0.1
 * @since 2024-09-21
 */
class Bit {
    private String name;
    private double diameter;

    /**
     * Constructs a {@code Bit} with all of it's attributes
     *
     * @param name     the name of the tool (bit)
     * @param diameter The diameter of the {@code Bit}
     */
    public Bit(String name, double diameter) {
        if (diameter <= 0) {
            throw new IllegalArgumentException("Le diametre doit être plus grand que 0");
        }
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Le nom ne peut pas être vide");
        }
        this.name = name;
        this.diameter = diameter;
    }

    /**
     * Constructs a {@code Bit} with no attributes
     */
    public Bit() {
        this.name = "Aucun outil assigné";
        this.diameter = 0.0f;
    }

    /**
     * @return the name of the {@code Bit}
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the {@code Bit}
     *
     * @param name the name of the {@code Bit}
     */
    public void setName(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Le nom ne peut pas être vide");
        }
        this.name = name;
    }

    /**
     * @return the diameter of the {@code Bit}
     */
    public double getDiameter() {
        return diameter;
    }

    /**
     * Sets the diameter of the {@code Bit}
     *
     * @param diameter the diameter of the {@code Bit}
     */
    public void setDiameter(double diameter) {
        if (diameter <= 0) {
            throw new IllegalArgumentException("Le diametre doit être plus grand que 0");
        }
        this.diameter = diameter;
    }

    /**
     * @return the DTO of the {@code Bit}
     */
    public BitDTO getDTO() {
        return new BitDTO(getName(), getDiameter());
    }
}
