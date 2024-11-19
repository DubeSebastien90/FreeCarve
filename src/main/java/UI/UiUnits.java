package UI;

import Common.Units;

/**
 * Display that maps to domain units
 * @author Kamran Charles Nayebi
 * @since 2024-11-16
 */
public enum UiUnits {
    MILLIMETERS(Units.MM, "mm"), CENTIMETERS(Units.CM, "cm"), METERS(Units.M, "m"), INCHES(Units.INCH, "'"), FEET(Units.FEET, "\"");

    final Units unit;
    final String display;

    UiUnits(Units unit, String display){
        this.unit = unit;
        this.display = display;
    }

    public Units getUnit(){
        return unit;
    }

    @Override
    public String toString() {
        return display;
    }
}
