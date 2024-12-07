package Domain;

import Common.DTO.CutDTO;
import Common.DTO.VertexDTO;
import Common.Pair;

import java.util.List;

/**
 * This class is used as an abstraction of the actual physical representation of the cuts of the CNC machine :
 *                  - A line width a certain width
 *                  - Width rounded corners
 * The goal is to easily translate Absolute Cut points into easy collision detection of roundedCuts
 */
public class RoundedCut {

    VertexDTO p1;
    VertexDTO p2;
    double bitDiameter;

    public RoundedCut(VertexDTO p1, VertexDTO p2, double bitDiameter){
        this.p1 = p1;
        this.p2 = p2;
        this.bitDiameter = bitDiameter;
    }

    /**
     * Compute the internal rectangle that composese the rounded cut
     * @return
     */
    List<VertexDTO> getInternalRectanglePoints(){
        VertexDTO diff = p2.sub(p1).normalize();
        Pair<VertexDTO, VertexDTO> results = VertexDTO.perpendicularPointsAroundP1(VertexDTO.zero(), diff);
        VertexDTO perpendicularDiff = results.getSecond();
        VertexDTO scaledPerpendicularDiff = perpendicularDiff.mul(bitDiameter/2);
        VertexDTO rect1 = p1.add(scaledPerpendicularDiff);
        VertexDTO rect2 = p1.add(scaledPerpendicularDiff.mul(-1));
        VertexDTO rect3 = p2.add(scaledPerpendicularDiff.mul(-1));
        VertexDTO rect4 = p2.add(scaledPerpendicularDiff);
        return List.of(new VertexDTO[]{rect1, rect2, rect3, rect4});
    }

    /**
     * Check if a point is inside the internal rectangle
     * @param point point to test
     * @return true if inside
     */
    boolean pointInInternalRectangle(VertexDTO point){
        List<VertexDTO> rectPoints = getInternalRectanglePoints();
        VertexDTO ab = rectPoints.get(1).sub(rectPoints.get(0));
        VertexDTO bc = rectPoints.get(2).sub(rectPoints.get(1));
        VertexDTO cd = rectPoints.get(3).sub(rectPoints.get(2));
        VertexDTO da = rectPoints.get(0).sub(rectPoints.get(3));

        VertexDTO ap = point.sub(rectPoints.get(0));
        VertexDTO bp = point.sub(rectPoints.get(1));
        VertexDTO cp = point.sub(rectPoints.get(2));
        VertexDTO dp = point.sub(rectPoints.get(3));

        double cross1 = VertexDTO.crossProduct(ab, ap);
        double cross2 = VertexDTO.crossProduct(bc, bp);
        double cross3 = VertexDTO.crossProduct(cd, cp);
        double cross4 = VertexDTO.crossProduct(da, dp);


        return ( (cross1 > 0 && cross2 > 0 && cross3 > 0 && cross4 > 0) ||
                (cross1 < 0 && cross2 < 0 && cross3 < 0 && cross4 < 0));
    }

    /**
     * Check if the point is in either of the 2 circles at the end of the cut
     * @param point point to test
     * @return true if inside one of the circle
     */
    boolean pointInCircles(VertexDTO point){
        double circleRadius = bitDiameter/2;
        double distanceFromP1 = p1.getDistance(point);
        double distanceFromP2 = p2.getDistance(point);
        return distanceFromP2 <= circleRadius || distanceFromP1 <= circleRadius;
    }

    /**
     * Test if the point is in the internal rectangle or in the circles
     * @param point point to test
     * @return true if inside any
     */
    public boolean pointInRoundedCut(VertexDTO point){
        return pointInInternalRectangle(point) || pointInCircles(point);
    }

    /**
     * Test the rounded cut created from a cutDTO if a point is inside of it
     * @param cutDTO cutDTO to build all of the roundedCut with
     * @param cursor point to test
     * @param cncMachine ref to CNCMachine
     * @return
     */
    public static boolean isRoundedCutHoveredByMouse(CutDTO cutDTO, VertexDTO cursor, CNCMachine cncMachine){
        List<VertexDTO> points = cncMachine.getAbsolutePointsPositionOfCutDTO(cutDTO);
        double bitDiameter = cncMachine.getBitStorage().getBitDiameter(cutDTO.getBitIndex());
        for(int i = 0; i < points.size()-1; i++){
            RoundedCut roundedCut = new RoundedCut(points.get(i), points.get(i+1), bitDiameter);
            if(roundedCut.pointInRoundedCut(cursor)) return true;
        }
        return false;
    }

    /**
     * Test if the current rounded cut intersect with another rounded cut
     * @param other other rounded cut to test
     * @return true if intersect
     */
    public boolean intersectRoundedCut(RoundedCut other){
        // Check if the internal rectangle of the other cut is in the current cut
        for (VertexDTO point : this.getInternalRectanglePoints()) {
            if (other.pointInRoundedCut(point)) {
                return true;
            }
        }

        for (VertexDTO point : other.getInternalRectanglePoints()) {
            if (this.pointInRoundedCut(point)) {
                return true;
            }
        }

        List<VertexDTO> rect1 = this.getInternalRectanglePoints();
        List<VertexDTO> rect2 = other.getInternalRectanglePoints();

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (VertexDTO.isLineIntersectLimited(rect1.get(i), rect1.get((i + 1) % 4), rect2.get(j), rect2.get((j + 1) % 4)).isPresent()) {
                    return true;
                }
            }
        }

        double thisRadius = this.bitDiameter/2;
        double otherRadius = other.bitDiameter/2;

        double distance1 = this.p1.getDistance(other.p1);
        double distance2 = this.p1.getDistance(other.p2);
        double distance3 = this.p2.getDistance(other.p1);
        double distance4 = this.p2.getDistance(other.p2);

        if(distance1 <= thisRadius + otherRadius ||
                distance2 <= thisRadius + otherRadius ||
                distance3 <= thisRadius + otherRadius ||
                distance4 <= thisRadius + otherRadius){
            return true;
        }

        return false;
    }



}
