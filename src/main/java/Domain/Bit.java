package Domain;

import Domain.DTO.BitDTO;

/**
 * The {@code Tool} class encapsulates a tool (bit) object and all of it's attributes
 *
 * @author Louis-Etienne Messier
 * @version 0.1
 * @since 2024-09-21
 */
class Bit {
    private String name;
    private float diameter;

    /**
     * Constructs a {@code Bit} with all of it's attributes
     *
     * @param name     the name of the tool (bit)
     * @param diameter The diameter of the {@code Bit}
     */
    public Bit(String name, float diameter) {
        if (diameter <= 0) {
            throw new IllegalArgumentException("Le diametre doit être plus grand que 0");
        }
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Le nom ne peut pas être vide");
        }
        setName(name);
        setDiameter(diameter);
    }

    public Bit(){
        this.name = "Aucun outil assigné";
        this.diameter = 0.0f;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if(name == null || name.isEmpty()){
            throw new IllegalArgumentException("Le nom ne peut pas être vide");
        }
        this.name = name;
    }

    public float getDiameter() {
        return diameter;
    }

    public void setDiameter(float diameter) {
        if (diameter <= 0) {
            throw new IllegalArgumentException("Le diametre doit être plus grand que 0");
        }
        this.diameter = diameter;
    }

    public BitDTO getBitDTO(){
        return new BitDTO(this);
    }
}
