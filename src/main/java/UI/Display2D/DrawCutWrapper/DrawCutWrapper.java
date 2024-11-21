package UI.Display2D.DrawCutWrapper;

import Common.DTO.BitDTO;
import Common.DTO.CutDTO;
import Common.Exceptions.BitNotSelectedException;
import Domain.CutType;
import Common.DTO.VertexDTO;
import UI.Display2D.Drawing;
import UI.Display2D.Rendering2DWindow;
import UI.MainWindow;
import UI.Widgets.PersoPoint;

import java.awt.*;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

/**
 * Polymorphic drawing class for cuts
 * @author Louis-Etienne Messier
 */
public abstract class DrawCutWrapper {

    protected PersoPoint cursorPoint;
    protected float cursorRadius = 25;
    protected ArrayList<PersoPoint> points; // Stores the PersoPoint in MM - necessary to keep precision
    protected CutDTO cut;
    protected Color strokeColor = Color.BLACK;
    protected float strokeWidth = 3.0f;
    protected Stroke stroke;
    protected MainWindow mainWindow;
    protected DrawCutState state = DrawCutState.NOT_SELECTED;
    public enum DrawCutState {
        SELECTED,
        NOT_SELECTED,
        HOVER,
    }

    /**
     * Draws the completed cut
     * @param graphics2D reference to grahics
     * @param renderer reference to renderer instance
     */
    public abstract void draw(Graphics2D graphics2D, Rendering2DWindow renderer);

    /**
     * Draw the cut that is still being created
     * @param graphics2D reference to graphics
     * @param renderer reference to renderer instance
     * @param cursor cursor to draw
     */
    public abstract void drawWhileChanging(Graphics2D graphics2D, Rendering2DWindow renderer, PersoPoint cursor);

    /**
     * Add point to the cut being done
     * @param renderer reference to the renderer instance
     * @param pointInMM point in MM to add
     * @return boolean : True if done, False is not done
     */
    public abstract boolean addPoint(Rendering2DWindow renderer, PersoPoint pointInMM);

    /**
     * triggers when cut is over
     * @return
     */
    public abstract Optional<UUID> end();

    /**
     * Updates the cursor for this specific type of DrawCutWrapper
     * @param renderer reference to the renderer
     * @param drawing reference to the drawing
     */
    public abstract void cursorUpdate(Rendering2DWindow renderer, Drawing drawing);

    /**
     * @return all the {@code PersoPoint} in the wrapper
     */
    public ArrayList<PersoPoint> getPersoPoints(){
        return this.points;
    }

    /**
     * Basic constructor with CutDTO
     * @param cut cut
     * @param renderer reference to renderer
     * @param mainWindow reference to mainWindow instance
     */
    public DrawCutWrapper(CutDTO cut, Rendering2DWindow renderer, MainWindow mainWindow){
        this.cut = cut;
        this.strokeWidth = mainWindow.getController().getBitsDTO()[cut.getBitIndex()].getDiameter() * (float) renderer.getZoom();
        this.stroke = new BasicStroke(strokeWidth);
        this.mainWindow = mainWindow;
        cursorPoint  = null;
        this.update(renderer);
    }

    /**
     * Basic constructor with type
     * @param type type
     * @param renderer reference to renderer
     * @param mainWindow reference to mainWindow instance
     */
    public DrawCutWrapper(CutType type, Rendering2DWindow renderer, MainWindow mainWindow){
        if(mainWindow.getMiddleContent() == null)
            return;

        int selectedBit = mainWindow.getMiddleContent().getCutWindow().getBitSelectionPanel().getSelectedBit();
        try{
            BitDTO bit = validateSelectedBit(selectedBit);
            this.cut = new CutDTO(new UUID(1000, 1000), 0.0f, selectedBit, type, new ArrayList<VertexDTO>());
            double zoom = renderer.getZoom();
            this.strokeWidth = mainWindow.getController().getBitsDTO()[cut.getBitIndex()].getDiameter() * (float) renderer.getZoom();
            this.stroke = new BasicStroke(strokeWidth);
        } catch (BitNotSelectedException e) {
            e.printStackTrace(); // Est-ce qu'on veut une barre d'action ou on affiche les commandes a faire?
            this.cut = new CutDTO(new UUID(1000, 1000), 0.0f, -1, type, new ArrayList<VertexDTO>());
            this.stroke = new BasicStroke(strokeWidth);
        }

        this.mainWindow = mainWindow;
        cursorPoint  = null;
        this.update(renderer);
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
    public void setState(DrawCutState newState, Rendering2DWindow renderer)
    {
        this.state = newState;
        if(newState == DrawCutState.SELECTED){
            this.strokeColor = Color.YELLOW;
        }
        else if(newState == DrawCutState.HOVER){
            this.strokeColor = Color.BLUE;
        }
        else{
            this.strokeColor = Color.BLACK;
        }

        renderer.repaint();
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
    public PersoPoint getCursorPoint(){
        return this.cursorPoint;
    }

    /**
     * Create the cursor point
     * @param renderer reference to the renderer
     */
    public void createCursorPoint(Rendering2DWindow renderer){
        this.cursorPoint = new PersoPoint(renderer.getMmMousePt().getX(), renderer.getMmMousePt().getY(),
                cursorRadius, true, Color.RED);
    }

    /**
     * Sets the cursor point to null
     */
    public void destroyCursorPoint(){
        this.cursorPoint = null;
    }

    /**
     * @return the CutDTO contained in the wrapper
     */
    public CutDTO getCutDTO(){
        return this.cut;
    }

    /**
     * Update all point position of the cut -- necessary so that mm relative points of the cut are transformed into pixel viewpoint
     * @param renderer reference to the renderer
     */
    protected void update(Rendering2DWindow renderer){
        this.points = new ArrayList<>();
        for (VertexDTO point : cut.getPoints()){
            PersoPoint p1 = new PersoPoint(point.getX(), point.getY(), 10.0f, true, strokeColor);
            points.add(p1);
        }
    }

    /**
     * Validate the selected bit
     * @param selectedBit the selected bit
     * @return the bit information if it is valid
     * @throws InvalidBitException when there's no bit selected
     */
    private BitDTO validateSelectedBit(int selectedBit) throws BitNotSelectedException {
        if (selectedBit == -1){
            throw new BitNotSelectedException("Aucun bit sélectionné"); // Gérer l'erreur de non sélection de bit
        }

        return mainWindow != null ? mainWindow.getController().getBitsDTO()[selectedBit]: new BitDTO("Error", 1);
        }
}
