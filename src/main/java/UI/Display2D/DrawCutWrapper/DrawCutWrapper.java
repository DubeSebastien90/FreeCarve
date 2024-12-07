package UI.Display2D.DrawCutWrapper;

import Common.CutState;
import Common.DTO.*;
import Common.Exceptions.BitNotSelectedException;
import Common.InvalidCutState;
import Domain.CutType;
import UI.Display2D.Drawing;
import UI.Display2D.Rendering2DWindow;
import UI.MainWindow;
import UI.Widgets.PersoPoint;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Polymorphic drawing class for cuts
 *
 * @author Louis-Etienne Messier
 */
public abstract class DrawCutWrapper {

    protected PersoPoint cursorPoint;
    protected double cursorRadius = 25;
    protected double snapThreshold = 10;
    protected ArrayList<PersoPoint> points; // Stores the PersoPoint in MM - necessary to keep precision
    protected CutDTO cut;
    protected Color strokeColor = Color.BLACK;
    protected double strokeWidth = 3.0f;
    protected Stroke stroke;
    protected MainWindow mainWindow;
    protected List<RefCutDTO> refs;
    protected List<VertexDTO> temporaryCreationPoints;
    protected DrawCutState state = DrawCutState.NOT_SELECTED;
    protected DrawCutState previousState = DrawCutState.NOT_SELECTED;

    public enum DrawCutState {
        SELECTED,
        NOT_SELECTED,
        HOVER,
        REF,
        INVALID,
    }

    protected final Color INVALID_COLOR = Color.RED;
    protected final Color ANCHOR_COLOR = Color.BLACK;
    protected final Color SNAP_COLOR = Color.YELLOW;
    protected final Color SELECTED_COLOR = Color.GREEN;
    protected final Color VALID_COLOR = Color.GREEN;
    protected final Color HOVER_COLOR = Color.BLUE;
    protected final Color ARROW_COLOR = Color.white;
    protected final Color DIMENSION_COLOR = Color.BLACK;
    protected final int ARROW_DIMENSION = 2;


    /**
     * Basic constructor with CutDTO
     *
     * @param cut        cut
     * @param renderer   reference to renderer
     * @param mainWindow reference to mainWindow instance
     */
    public DrawCutWrapper(CutDTO cut, Rendering2DWindow renderer, MainWindow mainWindow) {
        this.cut = cut;
        this.strokeWidth = mainWindow.getController().getBitsDTO()[cut.getBitIndex()].getDiameter() * (double) renderer.getZoom();
        this.stroke = new BasicStroke((float) renderer.scalePixelToMM(strokeWidth), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
        this.mainWindow = mainWindow;
        cursorPoint = null;
        this.update(renderer);
        this.refs = new ArrayList<>();
        temporaryCreationPoints = new ArrayList<>();

    }

    /**
     * Basic constructor with type
     *
     * @param type       type
     * @param renderer   reference to renderer
     * @param mainWindow reference to mainWindow instance
     */
    public DrawCutWrapper(CutType type, Rendering2DWindow renderer, MainWindow mainWindow) {
        if (mainWindow.getMiddleContent() == null)
            return;
        this.refs = new ArrayList<>();
        int selectedBit = mainWindow.getMiddleContent().getCutWindow().getBitSelectionPanel().getSelectedBit();
        try {
            BitDTO bit = validateSelectedBit(selectedBit);

            this.cut = new CutDTO(new UUID(1000, 1000), 0.0f, selectedBit, type, new ArrayList<VertexDTO>(), refs, CutState.VALID, InvalidCutState.DEFAULT);
            this.strokeWidth = mainWindow.getController().getBitsDTO()[cut.getBitIndex()].getDiameter() * renderer.getZoom();
            this.stroke = new BasicStroke((float) renderer.scalePixelToMM(strokeWidth),
                    BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
        } catch (BitNotSelectedException e) {
            e.printStackTrace(); // Est-ce qu'on veut une barre d'action ou on affiche les commandes a faire?
            this.cut = new CutDTO(new UUID(1000, 1000), 0.0f, -1, type, new ArrayList<VertexDTO>(), refs, CutState.VALID, InvalidCutState.DEFAULT);
            this.stroke = new BasicStroke((float) renderer.scalePixelToMM(strokeWidth),
                    BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
        }

        this.mainWindow = mainWindow;
        cursorPoint  = null;
        temporaryCreationPoints = new ArrayList<>();

        this.update(renderer);
    }

    /**
     * Draws the completed cut
     *
     * @param graphics2D reference to grahics
     * @param renderer   reference to renderer instance
     */
    public void draw(Graphics2D graphics2D, Rendering2DWindow renderer) {
        this.update(renderer);
        graphics2D.setStroke(stroke);
        graphics2D.setColor(this.strokeColor);

        boolean canSelect = mainWindow.getMiddleContent().getCutWindow().getRendering2DWindow().getDrawing().getState() == Drawing.DrawingState.IDLE && (renderer.isPointonPanel());

        //draw line
        Color c = Color.black;
        for(int i =0; i  < points.size() - 1; i++){
            if(this.points.get(i).getColor() != Color.MAGENTA){
                c = this.points.get(i).getColor();
            }
        }
        for(int i =0; i  < points.size() - 1; i++){
            graphics2D.setColor(c);
            this.points.get(i).drawLineMM(graphics2D, renderer, this.points.get(i+1), canSelect);
        }

        //draw points
        for (PersoPoint point : points) {
            point.drawMM(graphics2D, renderer, canSelect);
        }
    }

    /**
     * Draw the anchor of the specific cut
     *
     * @param graphics2D ref to graphics
     * @param renderer ref to renderer
     */
    public abstract void drawAnchor(Graphics2D graphics2D, Rendering2DWindow renderer);

    /**
     * Draw the cut that is still being created
     *
     * @param graphics2D reference to graphics
     * @param renderer   reference to renderer instance
     * @param cursor     cursor to draw
     */
    public abstract void drawWhileChanging(Graphics2D graphics2D, Rendering2DWindow renderer, PersoPoint cursor);

    /**
     * Draw function called when you want to draw the dimensions of the cuts on the board
     * @param graphics2D ref to graphics
     * @param rendering2DWindow ref to renderer instance
     */
    public abstract void drawDimensions(Graphics2D graphics2D, Rendering2DWindow rendering2DWindow);

    public void drawWhileModifyingAnchor(Graphics graphics, Rendering2DWindow rendering2DWindow, PersoPoint cursorPoint){
        System.out.println("MODIFIE POINT DE REF");
    }

    /**
     * Updates the cursor for the modifyAnchor
     *
     * @param renderer reference to the renderer
     * @param drawing  reference to the drawing
     */
    public abstract void cursorUpdateModifyAnchor(Rendering2DWindow renderer, Drawing drawing);

    /**
     * Add point to the cut being done
     *
     * @param renderer  reference to the renderer instance
     * @param pointInMM point in MM to add
     * @return boolean : True if done, False is not done
     */
    public abstract boolean addPoint(Drawing drawing, Rendering2DWindow renderer, PersoPoint pointInMM);

    public abstract void modifyAnchorPoint(Drawing drawing, Rendering2DWindow renderer, PersoPoint pointInMM);

    public abstract boolean areRefsValid();

    /**
     * triggers when cut is over
     *
     * @return
     */
    public abstract Optional<UUID> end();

    /**
     * Updates the cursor for this specific type of DrawCutWrapper
     *
     * @param renderer reference to the renderer
     * @param drawing  reference to the drawing
     */
    public abstract void cursorUpdate(Rendering2DWindow renderer, Drawing drawing);

    //public abstract void movedUpdate(Rendering2DWindow renderer, Drawing drawing);

    /**
     * @return all the {@code PersoPoint} in the wrapper
     */
    public ArrayList<PersoPoint> getPersoPoints() {
        return this.points;
    }

    public void emptyRefs() {
        this.refs = new ArrayList<>();
    }

    protected boolean areRefsNotCircular() {
        for (RefCutDTO ref : refs) {
            if (mainWindow.getController().isRefCircular(ref, cut)) {
                return false;
            }
        }
        return true;
    }

    /**
     * @return cut type of the wrapper
     */
    public CutType getCutType() {
        return this.cut.getCutType();
    }

    /**
     * @return stroke color of the wrapper
     */
    public Color getStrokeColor() {
        return strokeColor;
    }

    public void setStrokeSize(double newSize) {
        this.stroke = new BasicStroke((float) newSize, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
    }

    /**
     * @return the DrawCutState
     */
    public DrawCutState getState() {
        return this.state;
    }

    /**
     * Sets the state and changed the color accordingly
     *
     * @param newState the state
     */
    public void setState(DrawCutState newState, Rendering2DWindow renderer) {
        this.state = newState;
        if (newState == DrawCutState.SELECTED) {
            this.strokeColor = SELECTED_COLOR;
            this.previousState = DrawCutState.SELECTED;
        } else if (newState == DrawCutState.HOVER) {
            this.strokeColor = HOVER_COLOR;
            this.previousState = DrawCutState.HOVER;
        } else if (newState == DrawCutState.REF) {
            this.strokeColor = VALID_COLOR;
            // No setting previous state because a temporary one
        } else if (newState == DrawCutState.INVALID) {
            this.strokeColor = INVALID_COLOR;
        } else {
            this.strokeColor = ANCHOR_COLOR;
            this.previousState = DrawCutState.NOT_SELECTED;
        }

        renderer.repaint();
    }

    public void goBackState(Rendering2DWindow renderer) {
        setState(this.previousState, renderer);
    }

    /**
     * @param strokeColor new stroke color
     */
    public void setStrokeColor(Color strokeColor) {
        this.strokeColor = strokeColor;
    }

    /**
     * @return cursor {@code PersoPoint}
     */
    public PersoPoint getCursorPoint() {
        return this.cursorPoint;
    }

    public List<RefCutDTO> getRefs() {
        return refs;
    }

    /**
     * Create the cursor point
     *
     * @param renderer reference to the renderer
     */
    public void createCursorPoint(Rendering2DWindow renderer) {
        this.cursorPoint = new PersoPoint(renderer.getMmMousePt().getX(), renderer.getMmMousePt().getY(),
                cursorRadius, true, INVALID_COLOR);
    }

    /**
     * Sets the cursor point to null
     */
    public void destroyCursorPoint() {
        this.cursorPoint = null;
    }

    /**
     * @return the CutDTO contained in the wrapper
     */
    public CutDTO getCutDTO() {
        return this.cut;
    }

    /**
     * Update all point position of the cut -- necessary so that mm relative points of the cut are transformed into pixel viewpoint
     *
     * @param renderer reference to the renderer
     */
    protected void update(Rendering2DWindow renderer) {
        this.points = new ArrayList<>();
        for (VertexDTO point : mainWindow.getController().getAbsolutePointsPosition(cut)) {
            PersoPoint p1 = new PersoPoint(point.getX(), point.getY(), 10.0f, true, strokeColor);
            points.add(p1);
        }
    }

    /**
     * Create the cut
     *
     * @return {@code Optional<UUID>} UUID if the cut is valid, null if the cut is invalid
     */
    protected Optional<UUID> createCut() {
        RequestCutDTO rq = new RequestCutDTO(this.cut.getPoints(), this.cut.getCutType(), this.cut.getBitIndex(), mainWindow.getController().getPanelDTO().getDepth(), refs);
        return mainWindow.getController().requestCut(rq);
    }

    /**
     * Validate the selected bit
     *
     * @param selectedBit the selected bit
     * @return the bit information if it is valid
     * @throws BitNotSelectedException when there's no bit selected
     */
    private BitDTO validateSelectedBit(int selectedBit) throws BitNotSelectedException {
        if (selectedBit == -1) {
            throw new BitNotSelectedException("Aucun bit sélectionné"); // Gérer l'erreur de non sélection de bit
        }

        return mainWindow != null ? mainWindow.getController().getBitsDTO()[selectedBit] : new BitDTO("Error", 1);
    }

    public Optional<VertexDTO> changeClosestPointIfMagnetic(double threshold, Optional<VertexDTO> oldClosest, boolean priority) {
        PersoPoint p = this.cursorPoint;
        mainWindow.getController().setIntersectionMagnetic();
        VertexDTO p1 = new VertexDTO(p.getLocationX(), p.getLocationY(), 0.0f);
        Optional<VertexDTO> closestPoint2 = mainWindow.getController().getPointNearIntersections(p1, threshold);
        if (closestPoint2.isPresent()) {
            oldClosest = oldClosest
                    .filter(point -> !priority && p.getDistance(new PersoPoint(closestPoint2.get().getX(), closestPoint2.get().getY(), 0, false)) >= p.getDistance(new PersoPoint(point.getX(), point.getY(), 0, false)))
                    .or(() -> closestPoint2);
        }
        return oldClosest;
    }
}
