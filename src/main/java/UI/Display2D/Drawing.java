package UI.Display2D;

import Domain.CutType;
import UI.Display2D.DrawCutWrapper.DrawCutFactory;
import UI.Events.ChangeCutEvent;
import UI.MainWindow;
import UI.Display2D.DrawCutWrapper.DrawCutWrapper;
import UI.Widgets.PersoPoint;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
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
    private List<DrawCutWrapper> cutWrappers; //todo change to map so that you can retrieve them with ID - for faster usage
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
        currentDrawingCut = DrawCutFactory.createEmptyWrapper(CutType.LINE_VERTICAL, renderer, mainWindow);
        initCutMouseListener();
    }

    /**
     * Initiate a specific cut
     * @param type type of the cut
     */
    public void initCut(CutType type){
        deactivateCutListener();
        currentDrawingCut = DrawCutFactory.createEmptyWrapper(type, renderer, mainWindow);
        activateCutListener();
    }

    /**
     * Refresh all the cuts : i.e recreate the all the DrawCutWrappers and repaint
     */
    public void updateCuts(){
        this.cutWrappers = DrawCutFactory.createListDrawCutWrapper(mainWindow.getController().getCutListDTO(), renderer, mainWindow);
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
                        boolean isOver = currentDrawingCut.addPoint(Drawing.this, renderer, new PersoPoint(currentDrawingCut.getCursorPoint()));
                        if(isOver){
                            Optional<UUID> id = currentDrawingCut.end();
                            if(id.isPresent()){
                                updateCuts();
                                renderer.getChangeCutListener().addCutEventOccured(new ChangeCutEvent(renderer, id.get()));
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

    public void changeSelectedWrapperById(UUID id){

        for(DrawCutWrapper wrapper : cutWrappers){
            if(wrapper.getState() == DrawCutWrapper.DrawCutState.SELECTED){
                wrapper.setState(DrawCutWrapper.DrawCutState.NOT_SELECTED, renderer);
            }
        }

        for(DrawCutWrapper wrapper : cutWrappers){
            if(wrapper.getCutDTO().getId() == id){
                wrapper.setState(DrawCutWrapper.DrawCutState.SELECTED, renderer);
            }
        }
    }

    public void changeHoverWrapperById(UUID id){
        for(DrawCutWrapper wrapper : cutWrappers){
            if(wrapper.getCutDTO().getId() == id){
                wrapper.setState(DrawCutWrapper.DrawCutState.HOVER, renderer);
            }
        }
    }

    public void changeNotSelectedWrapperById(UUID id){
        for(DrawCutWrapper wrapper : cutWrappers){
            if(wrapper.getCutDTO().getId() == id){
                wrapper.setState(DrawCutWrapper.DrawCutState.NOT_SELECTED, renderer);
            }
        }
    }

    public void changeRefWrapperById(UUID id){
        for(DrawCutWrapper wrapper : cutWrappers){
            if(wrapper.getCutDTO().getId() == id){
                wrapper.setState(DrawCutWrapper.DrawCutState.REF, renderer);
            }
        }
    }

    public void changeGoBackWrapperById(UUID id){
        for(DrawCutWrapper wrapper : cutWrappers){
            if(wrapper.getCutDTO().getId() == id){
                wrapper.goBackState(renderer);
            }
        }
    }

    public Optional<DrawCutWrapper> getWrapperById(UUID id){
        for(DrawCutWrapper wrapper : cutWrappers){
            if(wrapper.getCutDTO().getId() == id){
                return Optional.of(wrapper);
            }
        }
        return Optional.empty();
    }
}

