package Util;

import java.awt.*;

/**
 * The {@code Cut} class encapsulates the basic attributes of a cuto
 *
 * @author Louis-Etienne Messier
 * @version 0.1
 * @since 2024-09-21
 */
public class Cut {
    private Point startPoint;
    private Point endPoint;
    private Bit bitUsed;
    private double depth;

    /**
     * Constructs a new {@code Cut} with all of it's attributes
     *
     * @param a the initial {@code Point} of the cut
     * @param b the end {@code Point} of the cut
     * @param bit the {@code Tool} used for the cut
     * @param depth the depth of the cut
     */
    public Cut(Point a, Point b, Bit bit, double depth){
        this.startPoint = a;
        this.endPoint = b;
        this.bitUsed = bit;
        this.depth = depth;
    }

}
