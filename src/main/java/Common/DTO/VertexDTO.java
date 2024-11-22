package Common.DTO;


import Common.Pair;

import java.util.Optional;

/**
 * The {@code VertexDTO} class is a read-only {@code Vertex}
 *
 * @author Adam Côté
 * @author Kamran Charles Nayebi
 * @version 1.0
 * @since 2024-10-20
 */
public class VertexDTO {
    private final double x;
    private final double y;
    private final double z;

    public VertexDTO(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public VertexDTO(VertexDTO other){
        this.x = other.x;
        this.y = other.y;
        this.z = other.z;
    }

    public String toString(){
        return "(" + x + ", " + y + "," + z + ")";
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public static VertexDTO zero(){
        return new VertexDTO(0,0,0);
    }

    public double getDistance(){
        return Math.sqrt(Math.pow(x,2)+Math.pow(y,2)+Math.pow(z,2));
    }

    public double getDistance(VertexDTO other){
        return Math.sqrt(Math.pow(x - other.x,2)+Math.pow(y - other.y,2)+Math.pow(z - other.z,2));
    }

    public VertexDTO add(VertexDTO other){
        return new VertexDTO(this.getX() + other.getX(),
                this.getY() + other.getY(),
                this.getZ() + other.getZ());
    }

    public VertexDTO sub(VertexDTO other){
        return new VertexDTO(this.getX() - other.getX(),
                this.getY() - other.getY(),
                this.getZ() - other.getZ());
    }

    public VertexDTO mul(double factor){
        return new VertexDTO(this.getX() * factor,
                this.getY() * factor,
                this.getZ() * factor);
    }

    /**
     * Interpolated between two points : this and the other point in parameter
     * @param other point to interpolate between
     * @param interpolationFactor factor of interpolation, normally should be between 0 and 1
     * @return the resulting VertexDTO
     */
    public VertexDTO interpolation(VertexDTO other, double interpolationFactor){
        return this.mul(1-interpolationFactor).add(other.mul(interpolationFactor));
    }

    /**
     * Rotates the p1 by 90 degres around itself to find the line perpendicular to the line p1-p2
     * @param p1 first point
     * @param p2 second point
     * @return pair of the two transformed points
     */
    public static Pair<VertexDTO, VertexDTO> perpendicularPointsAroundP1(VertexDTO p1, VertexDTO p2){
        double xPrime = p2.getX() - p1.getX();
        double yPrime = p2.getY() - p1.getY();

        double rotatedXPrime = -yPrime;
        double rotatedYPrime = xPrime;

        rotatedXPrime += p1.getX();
        rotatedYPrime += p1.getY();

        VertexDTO newP2 = new VertexDTO(rotatedXPrime, rotatedYPrime, p2.getZ());
        return new Pair<>(p1, newP2);
    }

    /**
     * Compute the intersection between two lines.
     * Limited means that if the point is not on the lines, nothing is returned.
     * Returns nothing is colinear, except if the cursor point is contained on the colinear point
     *
     * @param p1          first point of the cursor line
     * @param cursorPoint cursor
     * @param p3          first point of the reference line
     * @param p4          second point of the reference line
     * @return {@code Optional<VertexDTO>} can be null if no intersection, or VertexDTO if there is an intersection
     */
    public static Optional<VertexDTO> isLineIntersectLimited(VertexDTO p1, VertexDTO cursorPoint, VertexDTO p3, VertexDTO p4) {
        double a1 = cursorPoint.getY() - p1.getY();
        double b1 = p1.getX() - cursorPoint.getX();
        double c1 = a1 * p1.getX() + b1 * p1.getY();

        double a2 = p4.getY() - p3.getY();
        double b2 = p3.getX() - p4.getX();
        double c2 = a2 * p3.getX() + b2 * p3.getY();

        double det = a1 * b2 - a2 * b1;
        if ((cursorPoint.getY() - p1.getY()) * (p3.getX() - cursorPoint.getX())
                == (p3.getY() - cursorPoint.getY()) * (cursorPoint.getX() - p1.getX())) {
            // The lines are colinear
            if (Math.min(p3.getX(), p4.getX()) <= cursorPoint.getX() &&
                    cursorPoint.getX() <= Math.max(p3.getX(), p4.getX())) {
                // Point of the cursor is contained in the colinear line
                return Optional.of(new VertexDTO(cursorPoint.getX(), cursorPoint.getY(), 0.0f));
            }

        } else if (det == 0) {
            return Optional.empty(); // Parrallel lines or colinear
        }

        double x = (c1 * b2 - c2 * b1) / det;
        double y = (a1 * c2 - a2 * c1) / det;
        if (Math.min(p1.getX(), cursorPoint.getX()) <= x && x <= Math.max(p1.getX(), cursorPoint.getX())
                && Math.min(p1.getY(), cursorPoint.getY()) <= y && y <= Math.max(p1.getY(), cursorPoint.getY())
                && Math.min(p3.getX(), p4.getX()) <= x && x <= Math.max(p3.getX(), p4.getX())
                && Math.min(p3.getY(), p4.getY()) <= y && y <= Math.max(p3.getY(), p4.getY())) {
            VertexDTO outputIntersect = new VertexDTO(x, y, 0.0f);
            return Optional.of(outputIntersect); // Intersection is true

        }

        return Optional.empty();
    }

    /**
     * Compute the intersection between two lines.
     * No limitation means that if the point is not on the lines, the intersection is still returned.
     * Returns nothing if colinear
     *
     * @param p1          first point of the cursor line
     * @param cursorPoint cursor
     * @param p3          first point of the reference line
     * @param p4          second point of the reference line
     * @return {@code Optional<VertexDTO>} can be null if no intersection, or VertexDTO if there is an intersection
     */
    public static Optional<VertexDTO> isLineIntersectNoLimitation(VertexDTO p1, VertexDTO cursorPoint, VertexDTO p3, VertexDTO p4) {
        double a1 = cursorPoint.getY() - p1.getY();
        double b1 = p1.getX() - cursorPoint.getX();
        double c1 = a1 * p1.getX() + b1 * p1.getY();

        double a2 = p4.getY() - p3.getY();
        double b2 = p3.getX() - p4.getX();
        double c2 = a2 * p3.getX() + b2 * p3.getY();

        double det = a1 * b2 - a2 * b1;
        if ((cursorPoint.getY() - p1.getY()) * (p3.getX() - cursorPoint.getX())
                == (p3.getY() - cursorPoint.getY()) * (cursorPoint.getX() - p1.getX())) {
            return Optional.empty(); // Parrallel lines or colinear

        } else if (det == 0) {
            return Optional.empty(); // Parrallel lines or colinear
        }

        double x = (c1 * b2 - c2 * b1) / det;
        double y = (a1 * c2 - a2 * c1) / det;
        VertexDTO outputIntersect = new VertexDTO(x, y, 0.0f);
        return Optional.of(outputIntersect);
    }

    /**
     * @return a formatted string of the 2D components of the vertex
     */
    public String format2D (){
        return "(" + x + "," + y + ")";
    }
}
