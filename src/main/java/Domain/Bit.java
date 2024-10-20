package Domain;

/**
 * The {@code Tool} class encapsulates a tool (bit) object and all of it's attributes
 *
 * @author Louis-Etienne Messier
 * @version 0.1
 * @since 2024-09-21
 */
public class Bit {
    private String name;
    private float diameter;

    /**
     * Constructs a {@code Bit} with all of it's attributes
     *
     * @param name     the name of the tool (bit)
     * @param diameter The diameter of the {@code Bit}
     */
    public Bit(String name, float diameter) {
        setName(name);
        setDiameter(diameter);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getDiameter() {
        return diameter;
    }

    public void setDiameter(float diameter) {
        this.diameter = diameter;
    }
}
