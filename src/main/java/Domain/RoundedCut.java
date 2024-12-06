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

    boolean pointInCircles(VertexDTO point){
        double circleRadius = bitDiameter/2;
        double distanceFromP1 = p1.getDistance(point);
        double distanceFromP2 = p2.getDistance(point);
        return distanceFromP2 <= circleRadius || distanceFromP1 <= circleRadius;
    }

    public boolean pointInRoundedCut(VertexDTO point){
        return pointInInternalRectangle(point) || pointInCircles(point);
    }

    public static boolean isRoundedCutHoveredByMouse(CutDTO cutDTO, VertexDTO cursor, CNCMachine cncMachine){
        List<VertexDTO> points = cncMachine.getAbsolutePointsPositionOfCutDTO(cutDTO);
        double bitDiameter = cncMachine.getBitStorage().getBitDiameter(cutDTO.getBitIndex());
        for(int i = 0; i < points.size()-1; i++){
            RoundedCut roundedCut = new RoundedCut(points.get(i), points.get(i+1), bitDiameter);
            if(roundedCut.pointInRoundedCut(cursor)) return true;
        }
        return false;
    }

}
