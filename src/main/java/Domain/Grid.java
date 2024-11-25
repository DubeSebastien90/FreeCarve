package Domain;

import Common.DTO.CutDTO;
import Common.DTO.RefCutDTO;
import Common.DTO.VertexDTO;
import Common.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * The {@code Grid} class regroup functions that are useful for putting a grid on a {@code PanelCNC}
 *
 * @author Louis-Étiene Messier
 * @version 1.0
 * @since 2024-10-20
 */
public class Grid {
    private int size;
    private int magnetPrecision;
    private boolean magnetic = false;
    private boolean active = false;
    private List<VertexDTO> intersectionPoints;

    Grid(int size, int magnetPrecision) {
        this.size = size;
        this.magnetPrecision = magnetPrecision;
        intersectionPoints = new ArrayList<>();
    }

    public int getSize() {
        return this.size;
    }

    void setSize(int size) {
        this.size = size;
    }

    public int getMagnetPrecision() {
        return this.magnetPrecision;
    }

    void setMagnetPrecision(int magnetPrecision) {
        this.magnetPrecision = magnetPrecision;
    }

    /**
     * Check if a given point is nearby a line defined with two points, returns a the point on the line closest if exists
     *
     * @param point      point to check
     * @param linePoint1 first point of the line
     * @param linePoint2 second point of the line
     * @param threshold  distance threshold
     * @return Optional<VertexDTO> : null if not close to the threshold, a Point if close enough
     */
    private Optional<VertexDTO> isPointNearLine(VertexDTO point, VertexDTO linePoint1, VertexDTO linePoint2,
                                                double threshold) {
        double projector_t_numerator = (point.getX() - linePoint1.getX()) * (linePoint2.getX() - linePoint1.getX()) +
                (point.getY() - linePoint1.getY()) * (linePoint2.getY() - linePoint1.getY());
        double projector_t_denominator = Math.pow(linePoint2.getX() - linePoint1.getX(), 2) +
                Math.pow(linePoint2.getY() - linePoint1.getY(), 2);

        double t = projector_t_numerator / projector_t_denominator;

        VertexDTO closestPoint;

        double distance = 0;
        if (t >= 0 && t <= 1) {
            double numerator = Math.abs((linePoint2.getY() - linePoint1.getY()) * point.getX() -
                    (linePoint2.getX() - linePoint1.getX()) * point.getY() +
                    linePoint2.getX() * linePoint1.getY() - linePoint2.getY() * linePoint1.getX());
            double denominator = Math.sqrt(Math.pow(linePoint2.getY() - linePoint1.getY(), 2) +
                    Math.pow(linePoint2.getX() - linePoint1.getX(), 2));

            distance = numerator / denominator;

            closestPoint = new VertexDTO((linePoint1.getX() + t * (linePoint2.getX() - linePoint1.getX())),
                    (linePoint1.getY() + t * (linePoint2.getY() - linePoint1.getY())), 0.0f);
        } else if (t < 0) { // outside of range of the line but closest to first point
            distance = Math.sqrt(Math.pow(point.getX() - linePoint1.getX(), 2) +
                    Math.pow(point.getY() - linePoint1.getY(), 2));
            closestPoint = new VertexDTO(linePoint1.getX(), linePoint1.getY(), 0.0f);
        } else { // outside of range of the line but closest to second point
            distance = Math.sqrt(Math.pow(point.getX() - linePoint2.getX(), 2) +
                    Math.pow(point.getY() - linePoint2.getY(), 2));
            closestPoint = new VertexDTO(linePoint2.getX(), linePoint2.getY(), 0.0f);
        }

        if (distance < threshold) {
            return Optional.of(closestPoint);
        }

        return Optional.empty();
    }

    private Optional<Pair<VertexDTO, Double>> isPointOnLineGetRef(VertexDTO point, VertexDTO linePoint1, VertexDTO linePoint2){
        double projector_t_numerator = (point.getX() - linePoint1.getX()) * (linePoint2.getX() - linePoint1.getX()) +
                (point.getY() - linePoint1.getY()) * (linePoint2.getY() - linePoint1.getY());
        double projector_t_denominator = Math.pow(linePoint2.getX() - linePoint1.getX(), 2) +
                Math.pow(linePoint2.getY() - linePoint1.getY(), 2);

        double t = projector_t_numerator / projector_t_denominator;
        if (t >= 0 && t <= 1) {
            VertexDTO closestPoint = new VertexDTO((linePoint1.getX() + t * (linePoint2.getX() - linePoint1.getX())),
                    (linePoint1.getY() + t * (linePoint2.getY() - linePoint1.getY())), 0.0f);

            double distanceSquared = Math.pow(closestPoint.getX() - point.getX(), 2) +
                    Math.pow(closestPoint.getY() - point.getY(), 2);

            if (distanceSquared <= VertexDTO.doubleTolerance * VertexDTO.doubleTolerance) {
                return Optional.of(new Pair<>(closestPoint, t));
            }
        }

        return Optional.empty();
    }

    /**
     * Compute the intersection between two lines (on line made with the cursor and one reference line)
     *
     * @param p1          first point of the cursor line
     * @param cursorPoint cursor
     * @param p3          first point of the reference line
     * @param p4          second point of the reference line
     * @param threshold   distance threshold
     * @return {@code Optional<VertexDTO>} can be null if no intersection, or VertexDTO if there is an intersection
     */
    private Optional<VertexDTO> isLineIntersectCursor(VertexDTO p1, VertexDTO cursorPoint, VertexDTO p3, VertexDTO p4,
                                                      double threshold) {

        VertexDTO initialCursorPoint = new VertexDTO(cursorPoint); // keep copy of the cursor position
        // offsets the cursor
        VertexDTO diff = cursorPoint.sub(p1);
        diff = diff.mul(1.0/diff.getDistance());
        diff = diff.mul(threshold);
        cursorPoint = cursorPoint.add(diff);


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
                return Optional.of(new VertexDTO(initialCursorPoint.getX(), initialCursorPoint.getY(), 0.0f));
            }

        } else if (det == 0) {
            return Optional.empty(); // Parrallel lines or colinear
        }

        double x = (c1 * b2 - c2 * b1) / det;
        double y = (a1 * c2 - a2 * c1) / det;
        VertexDTO outputIntersect = new VertexDTO(0, 0, 0);

        if(Math.min(p1.getX(), cursorPoint.getX()) <= x + VertexDTO.doubleTolerance && x - VertexDTO.doubleTolerance <= Math.max(p1.getX(), cursorPoint.getX()) // The threshold addition and substraction is to allow the snap
                && Math.min(p1.getY(), cursorPoint.getY()) <= y + VertexDTO.doubleTolerance && y - VertexDTO.doubleTolerance <= Math.max(p1.getY(), cursorPoint.getY())
                && Math.min(p3.getX(), p4.getX()) <= x + VertexDTO.doubleTolerance && x - VertexDTO.doubleTolerance <= Math.max(p3.getX(), p4.getX())
                && Math.min(p3.getY(), p4.getY()) <= y + VertexDTO.doubleTolerance && y - VertexDTO.doubleTolerance <= Math.max(p3.getY(), p4.getY())) {
            outputIntersect = new VertexDTO(x, y, 0.0f);
            if (outputIntersect.getDistance(initialCursorPoint) <= threshold) { // check if the intersection is even close to the cursor
                if (outputIntersect.getDistance(p1) > 0 + VertexDTO.doubleTolerance) { // check if the intersection isn't on the first point of the cut
                    return Optional.of(outputIntersect); // Intersection is true
                }
            }

        }
        return Optional.empty();
    }

    /**
     * Returns the optional closest point to all intersections of the cuts + board
     * @param point point of the cursor to compare to
     * @param threshold threshold of the snap
     * @return Optional<VertexDTO> of the closest point
     */
    public Optional<VertexDTO> isPointNearIntersections(VertexDTO point, double threshold){
        VertexDTO closestPoint = null;
        for(VertexDTO intersectionPoint : intersectionPoints){
            double distancePointIntersection = point.getDistance(intersectionPoint);
            if(distancePointIntersection < threshold){
                if (closestPoint == null){
                    closestPoint = intersectionPoint;
                }
                else{
                    if (distancePointIntersection  < point.getDistance(closestPoint)){
                        closestPoint = intersectionPoint;
                    }
                }
            }
        }

        if(closestPoint == null) {return Optional.empty();}
        return Optional.of(closestPoint);
    }

    /**
     * Returns an optional closest point to the cut lines based on a reference point
     *
     * @param point     reference point
     * @param board     reference to the board
     * @param threshold threshold of the distance
     * @return Optional<VertexDTO> : null if no line nearby, the closest Point if point nearby
     */
    public Optional<VertexDTO> getPointNearAllCuts(VertexDTO point, PanelCNC board, double threshold,
                                                   Optional<VertexDTO> closestPoint) {

        // Testing all of the cuts
        for (Cut wrapper : board.getCutList()) {
            List<VertexDTO> points = wrapper.getAbsolutePointsPosition();
            if (points.size() > 1) {
                for (int i = 0; i < points.size() - 1; i++) {
                    Optional<VertexDTO> checkPoint = isPointNearLine(point, points.get(i), points.get(i + 1), threshold);
                    if (checkPoint.isPresent()) {
                        if (closestPoint.isEmpty()) {
                            closestPoint = checkPoint;
                        } else if (checkPoint.get().getDistance() < closestPoint.get().getDistance()) {
                            closestPoint = checkPoint;
                        }
                    }
                }
            }
        }

        return closestPoint;
    }


    /**
     * Returns an optionnal closest point to the board outlines based on a reference point
     *
     * @param point     reference point
     * @param board     reference to the board
     * @param threshold threshold of the distance
     * @return Optional<VertexDTO> : null if no line nearby, the closest Point if point nearby
     */
    public Optional<VertexDTO> getPointNearAllBorder(VertexDTO point, PanelCNC board, double threshold, Optional<VertexDTO> closestPoint) {

        // Testing the border
        List<VertexDTO> borderList = board.getBorderCut().getAbsolutePointsPosition();
        for (int i = 0; i < borderList.size() - 1; i++) {
            Optional<VertexDTO> checkPoint = isPointNearLine(point, borderList.get(i), borderList.get(i + 1),
                    threshold);

            if (checkPoint.isPresent()) {
                if (closestPoint.isEmpty()) {
                    closestPoint = checkPoint;
                } else if (checkPoint.get().getDistance() < closestPoint.get().getDistance()) {
                    closestPoint = checkPoint;
                }
            }
        }
        return closestPoint;
    }


    /**
     * Returns an optionnal closest point to the board outlines + cuts based on a reference point
     *
     * @param point     reference point
     * @param board     reference to the board
     * @param threshold threshold of the distance
     * @return Optional<VertexDTO> : null if no line nearby, the closest Point if point nearby
     */
    public Optional<VertexDTO> getPointNearAllBorderAndCuts(VertexDTO point, PanelCNC board, double threshold) {
        Optional<VertexDTO> closestPoint = Optional.empty();
        closestPoint = this.getPointNearAllBorder(point, board, threshold, closestPoint);
        closestPoint = this.getPointNearAllCuts(point, board, threshold, closestPoint);
        return closestPoint;
    }

    /**
     * Test the borders of the panel to get the refs
     * @param point point to test
     * @param board ref to the board
     * @return the list of the refs obtained
     */
    public List<RefCutDTO> getRefBorderOnPoint(VertexDTO point, PanelCNC board){
        List<RefCutDTO> ref = new ArrayList<>();

        List<Cut> allLineList = new ArrayList<>();

        Cut borderCut = board.getBorderCut();
        allLineList.add(borderCut); // adding the border as cuts to consider any line intersection on the border of the board

        for(Cut cut : allLineList){

            List<VertexDTO> points = cut.getAbsolutePointsPosition();
            for(int i =0; i < points.size() - 1; i++){
                Optional<Pair<VertexDTO, Double>> isPointOnLine = isPointOnLineGetRef(point, points.get(i), points.get(i+1));
                if(isPointOnLine.isPresent()){
                    ref.add(new RefCutDTO(cut.getDTO(), i, isPointOnLine.get().getSecond())); // add the ref to the ref list with the index, the get second is to get the interpolation
                }
            }
        }
        return ref;
    }

    /**
     * Test the cuts to get the refs
     * @param point point to test
     * @param board ref to the board
     * @return the list of the refs obtained
     */
    public List<RefCutDTO> getRefCutsOnPoint(VertexDTO point, PanelCNC board){
        List<RefCutDTO> ref = new ArrayList<>();

        List<Cut> allLineList = board.getCutList();

        for(Cut cut : allLineList){

            List<VertexDTO> points = cut.getAbsolutePointsPosition();
            for(int i =0; i < points.size() - 1; i++){
                Optional<Pair<VertexDTO, Double>> isPointOnLine = isPointOnLineGetRef(point, points.get(i), points.get(i+1));
                if(isPointOnLine.isPresent()){
                    ref.add(new RefCutDTO(cut.getDTO(), i, isPointOnLine.get().getSecond())); // add the ref to the ref list with the index, the get secodn is to get the interpolation
                }
            }
        }
        return ref;
    }

    /**
     * Test the both the cuts and the borders of the board to get the refs
     * @param point point to test
     * @param board ref to the board
     * @return the list of the refs obtained
     */
    public List<RefCutDTO> getRefCutsAndBorderOnPoint(VertexDTO point, PanelCNC board){
        List<RefCutDTO> ref = new ArrayList<>();
        ref.addAll(getRefCutsOnPoint(point, board));
        ref.addAll(getRefBorderOnPoint(point, board));
        return ref;
    }

    /**
     * Checks for intersection points on all lines on the board, and store them in the grid
     *
     * @param board
     * @return
     */
    public void computeIntersectionPointList(PanelCNC board){

        intersectionPoints.clear();

        List<Cut> allLineList = new ArrayList<Cut>();
        allLineList.addAll(board.getCutList());
        Cut borderCut = board.getBorderCut();
        allLineList.add(borderCut); // adding the border as cuts to consider any line intersection on the border of the board

        for(Cut cuts : allLineList){
            List<VertexDTO> points = cuts.getAbsolutePointsPosition();

            if(points.size() > 1){
                for(Cut cuts2 : allLineList){

                    List<VertexDTO> points2 = cuts2.getAbsolutePointsPosition();
                    if(points2.size() > 1){
                        for(int i =0; i < points.size()-1; i++){
                            for(int j =0; j < points2.size() -1 ; j++){
                                // if(cuts == cuts2 && i == j){continue;}
                                // Checks intersection of all lines of all cuts
                                Optional<VertexDTO> checkPoint = VertexDTO.isLineIntersectLimited(points2.get(j), points2.get(j+1),
                                        points.get(i), points.get(i + 1));
                                checkPoint.ifPresent(vertexDTO -> intersectionPoints.add(vertexDTO));

                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Returns an optionnal closest line on  the border based on a reference point
     *
     * @param p1           initial point of the cut
     * @param cursor       cursor position
     * @param board        reference to the board
     * @param threshold    threashold of the distance
     * @param closestPoint initial closest point
     * @return Optional<VertexDTO> : null if no line nearby, the closest Point if point nearby
     */
    public Optional<VertexDTO> getLineNearAllBorder(VertexDTO p1, VertexDTO cursor, PanelCNC board, double threshold,
                                                    Optional<VertexDTO> closestPoint) {

        // Testing the border
        List<VertexDTO> borderList = board.getBorderCut().getPoints();
        for (int i = 0; i < borderList.size() - 1; i++) {
            Optional<VertexDTO> checkPoint = isLineIntersectCursor(p1, cursor, borderList.get(i), borderList.get(i + 1),
                    threshold);


            if (checkPoint.isPresent()) {
                if (closestPoint.isEmpty()) {
                    closestPoint = checkPoint;
                } else if (checkPoint.get().getDistance() < closestPoint.get().getDistance()) {

                    closestPoint = checkPoint;
                }
            }
        }

        return closestPoint;

    }

    /**
     * Returns an optionnal closest line on  the cuts based on a reference point
     *
     * @param p1           initial point of the cut
     * @param cursor       cursor position
     * @param board        reference to the board
     * @param threshold    threashold of the distance
     * @param closestPoint initial closest point
     * @return Optional<VertexDTO> : null if no line nearby, the closest Point if point nearby
     */
    public Optional<VertexDTO> getLineNearAllCuts(VertexDTO p1, VertexDTO cursor, PanelCNC board, double threshold,
                                                  Optional<VertexDTO> closestPoint) {

        // Testing all of the cuts
        for (Cut wrapper : board.getCutList()) {
            List<VertexDTO> points = wrapper.getAbsolutePointsPosition();
            for (int i = 0; i < points.size() - 1; i++) {
                Optional<VertexDTO> checkPoint = isLineIntersectCursor(p1, cursor, points.get(i), points.get(i + 1), threshold);
                if (checkPoint.isPresent()) {
                    if (closestPoint.isEmpty()) {
                        closestPoint = checkPoint;
                    } else if (checkPoint.get().getDistance() < closestPoint.get().getDistance()) {

                        closestPoint = checkPoint;
                    }
                }
            }
        }

        return closestPoint;

    }

    /**
     * Returns an optional closest line point to the board outlines + cuts based on a reference point (cursor)
     *
     * @param p1        initial point of the cut
     * @param cursor    current cursor position
     * @param board     reference to the board
     * @param threshold threshold of the distance
     * @return Optional<VertexDTO> : null if no line nearby, the closest Point if point nearby
     */
    public Optional<VertexDTO> getLineNearAllBorderAndCuts(VertexDTO p1, VertexDTO cursor, PanelCNC board, double threshold) {
        Optional<VertexDTO> closestPoint = Optional.empty();
        closestPoint = this.getLineNearAllBorder(p1, cursor, board, threshold, closestPoint);
        closestPoint = this.getLineNearAllCuts(p1, cursor, board, threshold, closestPoint);
        return closestPoint;
    }

    public boolean isMagnetic() {
        return magnetic;
    }

    public void setMagnetic(boolean is_magnetic) {
        this.magnetic = is_magnetic;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

}

