package UI.Display2D;

import Domain.CutType;

import Domain.GridDTO;
import UI.Events.ChangeAttributeListener;
import UI.Events.ChangeCutEvent;
import UI.Events.ChangeCutListener;
import UI.Events.ChangeAttributeListener;
import UI.LeftBar;
import UI.MainWindow;
import UI.Widgets.DrawCutWrapper.DrawCutWrapper;
import UI.Widgets.PersoPoint;
import Util.UiUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * The {@code Rendering2DWindow} class is used to construct and display a board which represent the panel on the CNC. This
 * class also manage how the board is perceived. This includes the zoom, and the display of the mouse position relative to the board.
 *
 * @author Sébastien Dubé & Adam Côté
 * @version 1.2
 * @since 2024-10-22
 */
public class Rendering2DWindow extends JPanel {
    private final Rectangle2D board = new Rectangle2D.Double(0, 0, 1219.2, 914.4);
    private Point2D mousePt = new Point2D.Double(0, 0);
    private final Point2D fakeMousePt = new Point2D.Double(0, 0);
    private double offsetX = 100;
    private double offsetY = 100;
    private double zoom = 1;
    private Point2D mmMousePt;
    private double prevZoom;
    private int wW;
    private int wH;
    private ChangeCutListener changeCutListener;
    private final MainWindow mainWindow;
    ArrayList<Double> areammBoard = new ArrayList<>();
    private boolean draggingAPoint = false;
    private final ArrayList<PersoPoint> points = new ArrayList<>();
    private final Afficheur afficheur;
    private final Scaling scaling;
    private final ChangeAttributeListener listener;

    // Attributes for cut
    private MouseMotionListener cutListener;
    private MouseListener cutMouseClickListener;
    private List<DrawCutWrapper> cutWrappers;
    private ArrayList<PersoPoint> cutPoints;
    private DrawCutWrapper currentDrawingCut;
    private boolean isCutValid;

    public Point2D.Double getMmMousePt() {
        return (Point2D.Double) mmMousePt;
    }

    /**
     * Constructor for Renderinf2DWIndow
     *
     * @param mainWindow The main window to get the controller
     */

    public Rendering2DWindow(MainWindow mainWindow, ChangeAttributeListener listener, ChangeCutListener changeCutListener) {
        super();
        this.mainWindow = mainWindow;
        this.changeCutListener = changeCutListener;
        zoom = 1;
        mousePt = new Point(0, 0);
        mmMousePt = new Point2D.Double(0, 0);
        offsetY = 100;
        offsetX = 100;
        mainWindow.getController().putGrid(100, 10);
        addMouseListener();
        addMouseMotionListener();
        addMouseWheelListener();
        addComponentListener();
        initCutMouseListener();
        updateCuts();
        afficheur = new Afficheur(this);
        scaling = new Scaling(this);
        this.listener = listener;
    }

    public void updateCuts(){
        this.cutWrappers = DrawCutWrapper.createListDrawCutWrapper(mainWindow.getController().getCutListDTO(), this, mainWindow);
        this.repaint();

    }

    /**
     * Returns the listener responsible for handling attribute changes.
     *
     * @return the ChangeAttributeListener instance.
     */
    public ChangeAttributeListener getListener() {
        return listener;
    }

    /**
     * Returns the board area as a 2D rectangle.
     *
     * @return the Rectangle2D representing the board area.
     */
    public Rectangle2D getBoard() {
        return this.board;
    }

    /**
     * Returns the coordinates of the simulated mouse point.
     *
     * @return the Point2D representing the fake mouse point.
     */
    public Point2D getFakeMousePt() {
        return this.fakeMousePt;
    }

    /**
     * Returns the coordinates of the actual mouse point.
     *
     * @return the Point2D representing the mouse point.
     */
    public Point2D getMousePt() {
        return mousePt;
    }

    /**
     * Returns the X-axis offset for positioning.
     *
     * @return the X-axis offset as a double.
     */
    public double getOffsetX() {
        return offsetX;
    }

    /**
     * Returns the Y-axis offset for positioning.
     *
     * @return the Y-axis offset as a double.
     */
    public double getOffsetY() {
        return offsetY;
    }

    /**
     * Returns the current zoom level.
     *
     * @return the current zoom level as a double.
     */
    public double getZoom() {
        return zoom;
    }

    /**
     * Returns the previous zoom level before the last change.
     *
     * @return the previous zoom level as a double.
     */
    public double getPrevZoom() {
        return prevZoom;
    }

    /**
     * Returns the width of the window or canvas in pixels.
     *
     * @return the window width as an integer.
     */
    public int getwW() {
        return wW;
    }

    /**
     * Returns the height of the window or canvas in pixels.
     *
     * @return the window height as an integer.
     */
    public int getwH() {
        return wH;
    }

    /**
     * Indicates whether a point is currently being dragged.
     *
     * @return true if a point is being dragged, false otherwise.
     */
    public boolean isDraggingAPoint() {
        return draggingAPoint;
    }

    /**
     * Returns the display object responsible for visual rendering.
     *
     * @return the Afficheur instance.
     */
    public Afficheur getAfficheur() {
        return afficheur;
    }


    /**
     * @return The points displayed on the board.
     */
    public ArrayList<PersoPoint> getPoints() {
        return points;
    }

    /**
     * Sets the dragging a point attribute to a new value.
     *
     * @param draggingAPoint The new boolean value.
     */
    public void setDraggingAPoint(boolean draggingAPoint) {
        this.draggingAPoint = draggingAPoint;
    }

    /**
     * Initiates the basic mouse listener for when the mouse is pressed.
     */
    private void addMouseListener() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mousePt = e.getPoint();
                super.mousePressed(e);
                if (!mouseOnSomething(e)) {
                    clearPoints();
                    repaint();
                } else {
                    for (PersoPoint point : points) {
                        if (isPointClose(e, point) && !point.getFilled()) {
                            draggingAPoint = true;
                        }
                    }
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (isPointonPanel() && mainWindow.getLeftBar().getToolBar().getTool(LeftBar.ToolBar.Tool.SCALE).isEnabled()) {
                    scale();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (draggingAPoint) {
                    super.mouseReleased(e);
                    draggingAPoint = false;
                    clearPoints();
                    repaint();
                }
            }
        });
    }

    /**
     * Instantiate and manage the mouse movement related action.
     * When the mouse is dragged or simply moved different listener defined in this function are called.
     */
    private void addMouseMotionListener() {
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (!draggingAPoint) {
                    offsetX -= ((mousePt.getX() - e.getPoint().x) / zoom);
                    offsetY += ((mousePt.getY() - e.getPoint().y) / zoom);
                    mousePt = e.getPoint();
                    points.clear();
                    repaint();
                } else {
                    super.mouseDragged(e);
                    for (PersoPoint point : points) {
                        point.movePoint(Math.max(offsetX * zoom, e.getX()), Math.min(getHeight() - offsetY * zoom, e.getY()));
                        repaint();
                    }
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                if (!draggingAPoint) {
                    mousePt = e.getPoint();
                    mmMousePt.setLocation((mousePt.getX() - (offsetX * zoom)) / zoom, ((-1 * (mousePt.getY() - wH)) - (offsetY * zoom)) / zoom);
                    repaint();
                }
            }

        });
    }

    /**
     * Instantiates a listener for the mouse wheel. This is used to manage the zoom property of the board.
     */
    private void addMouseWheelListener() {
        addMouseWheelListener(e -> {
            double zoomFactor = ((double) 25 / Math.signum(e.getWheelRotation()));
            zoom -= zoom / zoomFactor;
            offsetX = (mousePt.getX() - (mmMousePt.getX() * zoom)) / zoom;
            offsetY = ((-1 * (mousePt.getY() - wH)) - (mmMousePt.getY() * zoom)) / zoom;
            points.clear();
            repaint();
        });
    }

    /**
     * Instantiate a global listener for all component in the {@code Rendering2DWindow}. It reinitialises the view of the board when a component is resized.
     */
    private void addComponentListener() {
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                wW = getWidth();
                wH = getHeight();
                repaint();
            }
        });
    }

    /**
     * Function executed when rendering the screen
     *
     * @param graphics the <code>Graphics</code> object to protect
     */
    @Override
    public void paintComponent(Graphics graphics) {
        Graphics2D graphics2D = ((Graphics2D) graphics);
        this.setBackground(UIManager.getColor("SubWindow.background"));
        UiUtil.makeJPanelRoundCorner(this, graphics2D);
        super.paintComponent(graphics2D);
        afficheur.drawRectangle(graphics2D);
        afficheur.drawMousePos(graphics2D);
        afficheur.drawPoints(graphics2D);
        afficheur.drawGrid(graphics2D);
    }

    /**
     * Draws the cuts on this JPanel.
     * @param graphics2D A graphics object which is painted on the JPanel.
     */
    private void drawCuts(Graphics2D graphics2D){
        for(DrawCutWrapper cutWrapper : cutWrappers){
            cutWrapper.draw(graphics2D, this);
        }

        for (PersoPoint point : cutPoints){
            point.drawMM(graphics2D, this);
        }

        if (currentDrawingCut != null && !cutPoints.isEmpty()){
            currentDrawingCut.beingDrawned(graphics2D, this, cutPoints.getLast());
        }
    }


    /**
     * @return True if the mouse is on the board.
     */
    boolean isPointonPanel() {
        return fakeMousePt.getX() >= 0 && fakeMousePt.getY() >= 0 && fakeMousePt.getX() <= board.getWidth() && fakeMousePt.getY() <= board.getHeight();
    }

    /**
     * Changes the width and the height of the board
     *
     * @param newWidth  The new width.
     * @param newHeight The new height.
     */
    public void resizePanneau(double newWidth, double newHeight) {
        board.setRect(board.getX(), board.getY(), newWidth, newHeight);
        mainWindow.getController().resizePanel(board.getWidth(), board.getHeight());
        repaint();
    }

    /**
     * Changes the size of the board using a delta to subtract.
     *
     * @param deltaWidth  The width difference.
     * @param deltaHeight The height difference.
     */
    private void deltaResizePanneau(double deltaWidth, double deltaHeight) {
        board.setRect(board.getX(), board.getY(), board.getWidth() - deltaWidth, board.getHeight() - deltaHeight);
        mainWindow.getController().resizePanel(board.getWidth(), board.getHeight());
        repaint();
    }

    /**
     * Function that returns the magnetised position of the mouse based on the grid
     *
     * @param mousePt The mouse coordinates point in pixel
     * @return the mouse coordinates if it's not close enough to an intersection, the intersection coordinates in pixels if the mouse is close enough
     */
    public Point2D getMagnetisedPos(Point2D mousePt) {
        GridDTO grid = mainWindow.getController().getGrid();
        for (double i = areammBoard.get(0); i < areammBoard.get(1); i += grid.getSize() * zoom) {
            if (Math.abs((mousePt.getX() - i)) <= grid.getMagnetPrecision()) {
                for (double j = areammBoard.get(3); j > areammBoard.get(2); j -= grid.getSize() * zoom) {
                    if (Point.distance(mousePt.getX(), mousePt.getY(), i, j) <= grid.getMagnetPrecision()) {
                        return new Point((int) i, (int) j);
                    }
                }
                break;
            }
        }
        return mousePt;
    }

    /**
     * Converts a point in mm into pixels
     *
     * @param mmPt The point in mm
     * @return The point in pixel
     */
    public Point2D mmTopixel(Point2D mmPt) {
        return new Point2D.Double(((mmPt.getX() * zoom) + (offsetX * zoom)), ((-1 * ((mmPt.getY() * zoom) + (offsetY * zoom))) + wH));
    }

    public double scaleMMToPixel(double mm){
        return mm / zoom;
    }

    /**
     * Converts a point in pixels into milimeters, the Point's value are integers
     *
     * @param pixelPt The point in pixel
     * @return The point in double mm
     */
    public Point2D pixelTomm(Point2D pixelPt) {
        return new Point2D.Double(((pixelPt.getX() - (offsetX * zoom)) / zoom), (((-1 * (pixelPt.getY() - wH)) - (offsetY * zoom)) / zoom));
    }

    /**
     * Converts a board to be displayed with the zoom and offset
     *
     * @param rectangle the board
     * @return A rectangle object shaped with the zoom
     */
    Rectangle2D convertBoardTomm(Rectangle2D rectangle) {
        areammBoard.clear();
        areammBoard.add((rectangle.getX() + offsetX) * zoom);
        areammBoard.add((rectangle.getX() + offsetX) * zoom + (rectangle.getWidth() * zoom));
        areammBoard.add((((-1 * (rectangle.getY() - wH)) - ((offsetY + rectangle.getHeight()) * zoom))));
        areammBoard.add((((-1 * (rectangle.getY() - wH)) - ((offsetY + rectangle.getHeight()) * zoom))) + (rectangle.getHeight() * zoom));
        return new Rectangle2D.Double(areammBoard.get(0).intValue(), areammBoard.get(2).intValue(), (rectangle.getWidth() * zoom), (rectangle.getHeight() * zoom));
    }

    /**
     * Zooms to the center of this panel.
     *
     * @param zoomFactor The zooming delta. A negative one will make the board seems bigger.
     */
    public void zoomOrigin(double zoomFactor) {
        points.clear();
        zoom -= zoom / zoomFactor;
        repaint();
    }

    /**
     * Initializes the cut event
     */
    public void cut(CutType type){
        deactivateCutListener();
        double radius = 25;
        PersoPoint p = new PersoPoint(mmMousePt.getX(), mmMousePt.getY(), radius, true, Color.GREEN);
        cutPoints.add(p);
        currentDrawingCut = DrawCutWrapper.createEmptyWrapper(type, this, mainWindow);
        activateCutListener();
    }

    /**
     * Determines whether the mouse cursor is currently positioned over
     * the board or a any other component.
     *
     * @param e The mouse event that triggered this check, providing details
     *          about the current mouse position and state.
     * @return {@code true} if the mouse is over the board or another component; {@code false} otherwise.
     */
    boolean mouseOnSomething(MouseEvent e) {
        if (isPointonPanel()) {
            return true;
        }
        for (PersoPoint point : points) {
            if (isPointClose(e, point)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if a given mouse event is close to a specified point.
     *
     * @param e     The mouse event that contains the current mouse position.
     * @param point The point to check against.
     * @return {@code true} if the mouse event's position is within a threshold
     * distance of the point; {@code false} otherwise.
     */
    boolean isPointClose(MouseEvent e, PersoPoint point) {
        double dx = e.getX() - point.getLocationX();
        double dy = e.getY() - point.getLocationY();
        return Math.sqrt(dx * dx + dy * dy) < 10;
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
        threshold = scaleMMToPixel(threshold);

        if (distance < threshold){
            return Optional.of(closestPoint);
        }

        return Optional.empty();
    }


    /**
     * Returns an optionnal closest point to the lines based on a reference point
     * @param point reference point
     * @return Optional<Point> : null if no line nearby, the closest Point if point nearby
     */
    private Optional<PersoPoint> getPointNearAllLine(PersoPoint point){
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
        Rectangle2D resizedBoard = this.board;
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
     * Initializes the points and behavior for resizing the board.
     * This includes setting up the necessary components and defining how
     * the board responds to resize events.
     */
    public void scale() {
        scaling.initiateScaling();
        activateScaleListener();
        repaint();
    }

    /**
     * Set up a mouse motion listener to handle cut events on the board
     */
    private void initCutMouseListener(){
        cutPoints = new ArrayList<>();
        cutListener = new MouseAdapter() {

            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
                if(!cutPoints.isEmpty()){
                    PersoPoint p = cutPoints.getLast();
                    p.movePoint(mmMousePt.getX(), mmMousePt.getY());
                    Optional<PersoPoint> closestPoint = getPointNearAllLine(p);
                    if(closestPoint.isPresent()){
                        p.movePoint(closestPoint.get().getLocationX(),closestPoint.get().getLocationY());
                        p.setColor(Color.GREEN);
                        p.setValid(PersoPoint.Valid.VALID);
                    }
                    else{
                        p.setColor(Color.RED);
                        p.setValid(PersoPoint.Valid.NOT_VALID);
                    }
                    repaint();
                }
            }

            @Override
            public void mouseDragged(MouseEvent e){
            }
        };

        cutMouseClickListener = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!cutPoints.isEmpty()) {
                    if (cutPoints.getLast().getValid() == PersoPoint.Valid.NOT_VALID)  // Cut invalid
                    {
                        deactivateCutListener();
                    }
                    else // Cut valid
                    {
                        boolean isOver = currentDrawingCut.addPoint(Rendering2DWindow.this, new PersoPoint(cutPoints.getLast()));
                        if(isOver){
                            System.out.println("Cut over");
                            Optional<UUID> id = currentDrawingCut.end();
                            if(id.isPresent()){
                                changeCutListener.addCutEventOccured(new ChangeCutEvent(Rendering2DWindow.this, id.get()));
                                updateCuts();
                                cut(currentDrawingCut.getCutType());
                            }
                        }
                    }
                }
            }
        };
    }

    /**
     * Activates the scaleListener so the board will change size when the points are dragged
     */
    private void activateScaleListener() {
        addMouseMotionListener(scaling.getScaleListener());
    }

    /**
     * Activates the cutListener so that the board reacts when a cut is being made
     */
    private void activateCutListener(){
        System.out.println("Cut activated");
        addMouseMotionListener(cutListener);
        addMouseListener(cutMouseClickListener);
    }

    /**
     * Deactivate the CutListener
     */
    private void deactivateCutListener(){
        System.out.println("Cut deactivated");
        currentDrawingCut = null;
        removeMouseMotionListener(cutListener);
        removeMouseListener(cutMouseClickListener);
        cutPoints.clear();
        repaint();
    }

    /**
     * Deactivate the scaleListener so the board won't resize when points are dragged.
     */
    private void clearPoints() {
        points.clear();
        removeMouseMotionListener(scaling.getScaleListener());
    }

    /**
     * Getter for main window
     * @return mainWindow
     */
    public MainWindow getMainWindow(){
        return mainWindow;
    }

    /**
     * Getter for areammBoard
     * @return areammBoard
     */
    public ArrayList<Double> getAreammBoard(){
        return areammBoard;
    }

}

