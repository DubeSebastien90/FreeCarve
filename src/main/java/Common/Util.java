package Common;

/**
 * The {@code Util} class regroup function that can be useful for any other class in the Application.
 *
 * @author Adam Côté
 * @version 1.0
 * @since 2024-10-22
 */
public class Util {

    /**
     * Converts a value in inches to a value in millimeters
     *
     * @param inch The value in inches
     * @return The value in millimeters
     */
    public static double inch_to_mm(double inch) {
        return inch * 25.4;
    }

    /**
     * Converts a value in millimeters to a value in inches
     *
     * @param mm The value in millimeters
     * @return The value in inches
     */
    public static double mm_to_inch(double mm) {
        return mm / 25.4;
    }


    /**
     * Converts a value in feet to a value in millimeters
     *
     * @param feet The value in feet
     * @return The value in millimeters
     */
    public static double feet_to_mm(double feet) {
        return inch_to_mm(12 * feet);
    }
}
