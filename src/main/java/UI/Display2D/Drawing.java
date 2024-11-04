package UI.Display2D;

import Domain.CutType;
import UI.Events.ChangeCutEvent;
import UI.MainWindow;
import UI.Display2D.DrawCutWrapper.DrawCutWrapper;
import UI.Widgets.PersoPoint;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Utility class to draw the cuts on the board
 * @author Louis-Etienne Messier
 */
public class Drawing {
    private MouseMotionListener cutListener;
    private MouseListener cutMouseClickListener;
    private List<DrawCutWrapper> cutWrappers;
    private DrawCutWrapper currentDrawingCut;
    private final Rendering2DWindow renderer;
    private final MainWindow mainWindow;

    /**
     * Create the {@code Drawing} utility class
     * @param renderer reference to the renderer
     * @param mainWindow reference to the mainWindow
     */
    public Drawing(Rendering2DWindow renderer, MainWindow mainWindow){
        this.renderer = renderer;
        this.mainWindow = mainWindow;
        currentDrawingCut = DrawCutWrapper.createEmptyWrapper(CutType.LINE_VERTICAL, renderer, mainWindow);
        initCutMouseListener();
    }

    /**
     * Initiate a specific cut
     * @param type type of the cut
     */
    public void initCut(CutType type){
        deactivateCutListener();
        currentDrawingCut = DrawCutWrapper.createEmptyWrapper(type, renderer, mainWindow);
        activateCutListener();
    }

    /**
     * Refresh all the cuts : i.e recreate the all the DrawCutWrappers and repaint
     */
    public void updateCuts(){
        this.cutWrappers = DrawCutWrapper.createListDrawCutWrapper(mainWindow.getController().getCutListDTO(), renderer, mainWindow);
        this.renderer.repaint();
    }

    /**
     * @return all the DrawCutWrappers instances
     */
    public List<DrawCutWrapper> getCutWrappers(){
        return this.cutWrappers;
    }

    /**
     * @return the cursor {@code PersoPoint}
     */
    public PersoPoint getCursorPoint(){
        return this.currentDrawingCut.getCursorPoint();
    }

    /**
     *
     * @return the {@code DrawCutWrapper} that represents the cut being done, can be null
     */
    public DrawCutWrapper getCurrentDrawingCut(){
        return  this.currentDrawingCut;
    }

    /**
     * Check if a given point is nearby a line defined with two points, returns a the point on the line closest if exists
     * @param point point to check
     * @param linePoint1 first point of the line
     * @param linePoint2 second point of the line
     * @return Optional<Point> : null if not close to the threshold, a Point if close enough
     */
    private Optional<PersoPoint> isPointNearLine(PersoPoint point, PersoPoint linePoint1, PersoPoint linePoint2){
        double projector_t_numerator = (point.getLocationX() - linePoint1.getLocationX()) * (linePoint2.getLocationX() - linePoint1.getLocationX()) +
                (point.getLocationY() - linePoint1.getLocationY()) * (linePoint2.getLocationY() - linePoint1.getLocationY());
        double projector_t_denominator = Math.pow(linePoint2.getLocationX() - linePoint1.getLocationX(), 2) +
                Math.pow(linePoint2.getLocationY() - linePoint1.getLocationY(), 2);

        double t = projector_t_numerator / projector_t_denominator;

        PersoPoint closestPoint;

        double distance = 0;
        if (t >= 0 && t <= 1){
            double numerator =  Math.abs((linePoint2.getLocationY() - linePoint1.getLocationY()) * point.getLocationX() -
                    (linePoint2.getLocationX() - linePoint1.getLocationX()) * point.getLocationY() +
                    linePoint2.getLocationX() * linePoint1.getLocationY() - linePoint2.getLocationY()*linePoint1.getLocationX());
            double denominator = Math.sqrt(Math.pow(linePoint2.getLocationY() - linePoint1.getLocationY(), 2) +
                    Math.pow(linePoint2.getLocationX() - linePoint1.getLocationX(), 2));

            distance = numerator/denominator;

            closestPoint = new PersoPoint( (linePoint1.getLocationX() + t * (linePoint2.getLocationX() - linePoint1.getLocationX())),
                    (linePoint1.getLocationY() + t * (linePoint2.getLocationY() - linePoint1.getLocationY())), 10.0f, true);
        }
        else if (t < 0){ // outside of range of the line but closest to first point
            distance = Math.sqrt(Math.pow(point.getLocationX() - linePoint1.getLocationX(), 2) +
                    Math.pow(point.getLocationY() - linePoint1.getLocationY(), 2));
            closestPoint = new PersoPoint(linePoint1.getLocationX(),  linePoint1.getLocationY(), 10.0f, true);
        }
        else{ // outside of range of the line but closest to second point
            distance = Math.sqrt(Math.pow(point.getLocationX() - linePoint2.getLocationX(), 2) +
                    Math.pow(point.getLocationY() - linePoint2.getLocationY(), 2));
            closestPoint = new PersoPoint( linePoint2.getLocationX(),  linePoint2.getLocationY(), 10.0f, true);
        }

        double threshold = 10;
        threshold = renderer.scaleMMToPixel(threshold);

        if (distance < threshold){
            return Optional.of(closestPoint);
        }

        return Optional.empty();
    }

    /**
     * Compute the intersection between two lines (on line made with the cursor and one reference line)
     * @param p1 first point of the cursor line
     * @param cursorPoint cursor
     * @param p3 first point of the reference line
     * @param p4 second point of the reference line
     * @return {@code Optional<PersoPoint>} can be null if no intersection, or PersoPoint if there is an intersection
     */
    private Optional<PersoPoint> isLineIntersect(PersoPoint p1, PersoPoint cursorPoint, PersoPoint p3, PersoPoint p4){
        double a1 = cursorPoint.getLocationY() - p1.getLocationY();
        double b1 = p1.getLocationX() - cursorPoint.getLocationX();
        double c1 = a1 * p1.getLocationX() + b1 * p1.getLocationY();

        double a2 = p4.getLocationY() - p3.getLocationY();
        double b2 = p3.getLocationX() - p4.getLocationX();
        double c2 = a2 * p3.getLocationX() + b2 * p3.getLocationY();

        PersoPoint closestPoint;

        double det = a1 * b2 - a2 * b1;
        if((cursorPoint.getLocationY() - p1.getLocationY()) * (p3.getLocationX() - cursorPoint.getLocationX())
                == (p3.getLocationY() - cursorPoint.getLocationY()) * (cursorPoint.getLocationX() - p1.getLocationX()) ){
            // The lines are colinear
            if(Math.min(p3.getLocationX(), p4.getLocationX()) <= cursorPoint.getLocationX() &&
                    cursorPoint.getLocationX() <= Math.max(p3.getLocationX(), p4.getLocationX())){
                // Point of the cursor is contained in the colinear line
                return Optional.of(new PersoPoint(cursorPoint.getLocationX(), cursorPoint.getLocationY(), 10.0f, true));
            }

        }
        else if (det == 0){
            return Optional.empty(); // Parrallel lines or colinear
        }

        double x = (c1 * b2 - c2 * b1) / det;
        double y = (a1 * c2 - a2 * c1) / det;
        double threshold = 10;
        threshold = renderer.scaleMMToPixel(threshold);
        if(Math.min(p1.getLocationX(), cursorPoint.getLocationX()) <= x && x <= Math.max(p1.getLocationX(), cursorPoint.getLocationX())
        && Math.min(p1.getLocationY(), cursorPoint.getLocationY()) <= y && y <= Math.max(p1.getLocationY(), cursorPoint.getLocationY())
                && Math.min(p3.getLocationX(), p4.getLocationX()) <= x && x <= Math.max(p3.getLocationX(), p4.getLocationX())
                && Math.min(p3.getLocationY(), p4.getLocationY()) <= y && y <= Math.max(p3.getLocationY(), p4.getLocationY()))
        {
            PersoPoint outputIntersect = new PersoPoint(x, y, 10.0f, true);
            if(outputIntersect.getDistance(cursorPoint)< threshold){ // check if the intersection is even close to the cursor
                if(outputIntersect.getDistance(p1) != 0){ // check if the intersection isn't on the first point of the cut
                    return Optional.of(outputIntersect); // Intersection is true
                }
            }

        }

        return Optional.empty();

    }

    /**
     * Returns an optionnal closest point to the lines based on a reference point
     * @param point reference point
     * @return Optional<Point> : null if no line nearby, the closest Point if point nearby
     */
    public Optional<PersoPoint> getPointNearAllLine(PersoPoint point){
        PersoPoint closestPoint = null;

        // Testing all of the cuts
        for (DrawCutWrapper wrapper : this.cutWrappers){
            List<PersoPoint> points = wrapper.getPersoPoints();
            if (points.size() > 1){
                for(int i=0; i < points.size()-1; i++){
                    Optional<PersoPoint> checkPoint = isPointNearLine(point, points.get(i), points.get(i + 1));
                    if(checkPoint.isPresent()) {
                        if (closestPoint == null){
                            closestPoint = checkPoint.get();
                        }
                        else if (checkPoint.get().getDistance() < closestPoint.getDistance()){
                            closestPoint = checkPoint.get();
                        }
                    }
                }
            }
        }

        // Testing the border
        List<PersoPoint> borderList = new ArrayList<>();
        Rectangle2D resizedBoard = renderer.getBoard();
        PersoPoint borderP1 = new PersoPoint(resizedBoard.getX(), resizedBoard.getY(), 10.0f, true);
        PersoPoint borderP2 = new PersoPoint(resizedBoard.getX() + resizedBoard.getWidth(), resizedBoard.getY(), 10.0f, true);
        PersoPoint borderP3 = new PersoPoint(resizedBoard.getWidth() + resizedBoard.getX(), resizedBoard.getHeight() + resizedBoard.getY(), 10.0f, true);
        PersoPoint borderP4 = new PersoPoint(resizedBoard.getX(), resizedBoard.getY()+ resizedBoard.getHeight(), 10.0f, true);
        PersoPoint borderP5 = new PersoPoint(resizedBoard.getX(), resizedBoard.getY(), 10.0f, true);
        borderList.add(borderP1); borderList.add(borderP2); borderList.add(borderP3); borderList.add(borderP4); borderList.add(borderP5);
        for(int i=0; i < borderList.size()-1; i++){
            if(i == 3){

            }
            Optional<PersoPoint> checkPoint = isPointNearLine(point, borderList.get(i), borderList.get(i + 1));

            if(checkPoint.isPresent()) {
                if (closestPoint == null){
                    closestPoint = checkPoint.get();
                }
                else if (checkPoint.get().getDistance() < closestPoint.getDistance()){
                    closestPoint = checkPoint.get();
                }
            }
        }

        if (closestPoint != null){
            return Optional.of(closestPoint);
        }
        return Optional.empty();

    }

    /**
     * Returns an optionnal closest point on line to the lines based on a reference point
     * @param p1 reference point, i.e first point of the line
     * @param cursor point of the cursor that wants to detect if overrid
     * @return Optional<Point> : null if no line nearby, the closest PersoPoint on line if point nearby
     */
    public Optional<PersoPoint> getLineNearAllLine(PersoPoint p1, PersoPoint cursor){
        PersoPoint closestPoint = null;

        // Testing all of the cuts
        for (DrawCutWrapper wrapper : this.cutWrappers){
            List<PersoPoint> points = wrapper.getPersoPoints();
            if (points.size() > 1){
                for(int i=0; i < points.size()-1; i++){
                    Optional<PersoPoint> checkPoint = isLineIntersect(p1, cursor, points.get(i), points.get(i + 1));
                    if(checkPoint.isPresent()) {
                        if (closestPoint == null){
                            closestPoint = checkPoint.get();
                        }
                        else if (checkPoint.get().getDistance() < closestPoint.getDistance()){
                            closestPoint = checkPoint.get();
                        }
                    }
                }
            }
        }

        // Testing the border
        List<PersoPoint> borderList = new ArrayList<>();
        Rectangle2D resizedBoard = renderer.getBoard();
        PersoPoint borderP1 = new PersoPoint(resizedBoard.getX(), resizedBoard.getY(), 10.0f, true);
        PersoPoint borderP2 = new PersoPoint(resizedBoard.getX() + resizedBoard.getWidth(), resizedBoard.getY(), 10.0f, true);
        PersoPoint borderP3 = new PersoPoint(resizedBoard.getWidth() + resizedBoard.getX(), resizedBoard.getHeight() + resizedBoard.getY(), 10.0f, true);
        PersoPoint borderP4 = new PersoPoint(resizedBoard.getX(), resizedBoard.getY()+ resizedBoard.getHeight(), 10.0f, true);
        PersoPoint borderP5 = new PersoPoint(resizedBoard.getX(), resizedBoard.getY(), 10.0f, true);
        borderList.add(borderP1); borderList.add(borderP2); borderList.add(borderP3); borderList.add(borderP4); borderList.add(borderP5);
        for(int i=0; i < borderList.size()-1; i++){
            if(i == 3){

            }
            Optional<PersoPoint> checkPoint = isLineIntersect(p1, cursor, borderList.get(i), borderList.get(i + 1));

            if(checkPoint.isPresent()) {
                if (closestPoint == null){
                    closestPoint = checkPoint.get();
                }
                else if (checkPoint.get().getDistance() < closestPoint.getDistance()){
                    closestPoint = checkPoint.get();
                }
            }
        }

        if (closestPoint != null){
            return Optional.of(closestPoint);
        }
        return Optional.empty();

    }

    /**
     * Initialize all of the mouse listeners : both MouseAdapter(s)
     */
    private void initCutMouseListener(){
        cutListener = new MouseAdapter() {

            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
                if(currentDrawingCut.getCursorPoint() != null){
                    currentDrawingCut.cursorUpdate(renderer, Drawing.this);
                    renderer.repaint();
                }
            }

            @Override
            public void mouseDragged(MouseEvent e){
            }
        };

        cutMouseClickListener = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (currentDrawingCut.getCursorPoint() != null) {
                    if (currentDrawingCut.getCursorPoint().getValid() == PersoPoint.Valid.NOT_VALID)  // Cut invalid
                    {
                        deactivateCutListener();
                    }
                    else // Cut valid
                    {
                        boolean isOver = currentDrawingCut.addPoint(renderer, new PersoPoint(currentDrawingCut.getCursorPoint()));
                        if(isOver){
                            Optional<UUID> id = currentDrawingCut.end();
                            if(id.isPresent()){
                                renderer.getChangeCutListener().addCutEventOccured(new ChangeCutEvent(renderer, id.get()));
                                updateCuts();
                                initCut(currentDrawingCut.getCutType());
                            }
                        }
                    }
                }
            }
        };
    }

    /**
     * Activates the cutListener so that the board reacts when a cut is being made
     */
    private void activateCutListener(){
        renderer.addMouseMotionListener(cutListener);
        renderer.addMouseListener(cutMouseClickListener);
        currentDrawingCut.createCursorPoint(this.renderer);
    }

    /**
     * Deactivate the cutListener
     */
    private void deactivateCutListener(){
        renderer.removeMouseMotionListener(cutListener);
        renderer.removeMouseListener(cutMouseClickListener);
        currentDrawingCut.destroyCursorPoint();
        renderer.repaint();
    }
}

