package UI.Display2D;

import Common.CutState;
import Common.DTO.CutDTO;
import Common.DTO.RefCutDTO;
import Common.DTO.VertexDTO;
import Domain.CutType;
import UI.Display2D.DrawCutWrapper.DrawCutFactory;
import UI.Display2D.DrawCutWrapper.DrawCutWrapper;
import UI.Events.ChangeAttributeEvent;
import UI.Events.ChangeCutEvent;
import UI.MainWindow;
import UI.SubWindows.CutListPanel;
import UI.Widgets.CutBox;
import UI.Widgets.PersoPoint;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Utility class to draw the cuts on the board
 *
 * @author Louis-Etienne Messier
 */
public class Drawing {
    private MouseMotionListener createCutMoveListener;
    private MouseListener createCutActionListener;
    private MouseMotionListener modifyAnchorMoveListener;
    private MouseListener modifyAnchorActionListener;
    private MouseMotionListener pointMoveListener;
    private MouseMotionListener cutMoveListener;
    private List<DrawCutWrapper> cutWrappers; //todo change to map so that you can retrieve them with ID - for faster usage
    private DrawCutWrapper currentDrawingCut;
    private DrawCutWrapper currentModifiedCut;
    private PersoPoint currentModifiedPoint;
    private final Rendering2DWindow renderer;
    private final MainWindow mainWindow;
    private DrawingState state;
    private Drawing drawing;
    private CutDTO prevCut;
    private int indexPoint = 0;
    private List<VertexDTO> prevPts;

    public enum DrawingState {
        CREATE_CUT,
        IDLE,
        MODIFY_ANCHOR,
        MODIFY_POINT,
        MODIFY_CUT,
    }

    public void setCurrentDrawingCut(DrawCutWrapper currentDrawingCut) {
        this.currentDrawingCut = currentDrawingCut;
    }

    /**
     * Create the {@code Drawing} utility class
     *
     * @param renderer   reference to the renderer
     * @param mainWindow reference to the mainWindow
     */
    public Drawing(Rendering2DWindow renderer, MainWindow mainWindow) {
        this.renderer = renderer;
        this.mainWindow = mainWindow;
        currentDrawingCut = DrawCutFactory.createEmptyWrapper(CutType.LINE_VERTICAL, renderer, mainWindow);
        initCutMouseListener();
    }

    /**
     * Initiate a specific cut
     *
     * @param type type of the cut
     */
    public void initCut(CutType type) {
        deactivateCreateCutListener();
        setState(DrawingState.CREATE_CUT);
        currentDrawingCut = DrawCutFactory.createEmptyWrapper(type, renderer, mainWindow);
        activateCreateCutListener();
    }

    /**
     * Refresh all the cuts : i.e recreate the all the DrawCutWrappers and repaint
     */
    public void updateCuts() {
        this.cutWrappers = DrawCutFactory.createListDrawCutWrapper(mainWindow.getController().getCutListDTO(), renderer, mainWindow);
        this.renderer.repaint();
    }

    /**
     * @return all the DrawCutWrappers instances
     */
    public List<DrawCutWrapper> getCutWrappers() {
        return this.cutWrappers;
    }

    /**
     * @return the cursor {@code PersoPoint}
     */
    public PersoPoint getCreateCursorPoint() {
        return this.currentDrawingCut.getCursorPoint();
    }

    public PersoPoint getModifyingAnchorCursorPoint() {
        return this.currentModifiedCut.getCursorPoint();
    }

    public DrawingState getState() {
        return this.state;
    }

    /**
     * @return the {@code DrawCutWrapper} that represents the cut being done, can be null
     */
    public DrawCutWrapper getCurrentDrawingCut() {
        return this.currentDrawingCut;
    }

    public DrawCutWrapper getCurrentModifiedCut() {
        return this.currentModifiedCut;
    }

    /**
     * Initialize all of the mouse listeners : both MouseAdapter(s)
     */
    private void initCutMouseListener() {
        createCutMoveListener = new MouseAdapter() {

            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
                if (currentDrawingCut.getCursorPoint() != null) {
                    currentDrawingCut.cursorUpdate(renderer, Drawing.this);
                    renderer.repaint();
                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {
            }
        };

        createCutActionListener = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (currentDrawingCut.getCursorPoint() != null && getState() == DrawingState.CREATE_CUT) {
                    if (currentDrawingCut.getCursorPoint().getValid() == PersoPoint.Valid.NOT_VALID)  // Cut invalid
                    {
                        deactivateCreateCutListener();
                    } else // Cut valid
                    {
                        boolean isOver = currentDrawingCut.addPoint(Drawing.this, renderer, new PersoPoint(currentDrawingCut.getCursorPoint()));
                        if (isOver) {
                            Optional<UUID> id = currentDrawingCut.end();
                            if (id.isPresent()) {
                                updateCuts();
                                mainWindow.getMiddleContent().getCutWindow().notifyObservers();
                                renderer.getChangeCutListener().addCutEventOccured(new ChangeCutEvent(renderer, id.get()));
                                setState(DrawingState.IDLE);
                            }
                        }
                    }
                }
            }
        };

        modifyAnchorMoveListener = new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
                if (currentModifiedCut.getCursorPoint() != null) {
                    currentModifiedCut.cursorUpdate(renderer, Drawing.this);
                    renderer.repaint();
                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {
            }
        };

        modifyAnchorActionListener = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (currentModifiedCut.getCursorPoint() != null) {
                    if (currentModifiedCut.getCursorPoint().getValid() == PersoPoint.Valid.NOT_VALID)  // Cut invalid
                    {
                        deactivateModifyAnchorCutListener();
                    } else // Cut valid
                    {
                        currentModifiedCut.addPoint(Drawing.this, renderer, new PersoPoint(currentModifiedCut.getCursorPoint()));
                        if (currentModifiedCut.areRefsValid()) {
                            CutDTO c = currentModifiedCut.getCutDTO();
                            List<RefCutDTO> newRefs = currentModifiedCut.getRefs();
                            c = new CutDTO(c.getId(), c.getDepth(), c.getBitIndex(), c.getCutType(), c.getPoints(), newRefs, c.getState());
                            CutListPanel cutListPanel = mainWindow.getMiddleContent().getCutWindow().getCutListPanel();
                            mainWindow.getController().modifyCut(c);

                            Optional<CutBox> cutBox = cutListPanel.getCutBoxWithId(c.getId());
                            if (cutBox.isPresent()) {
                                cutListPanel.modifiedAttributeEventOccured(new ChangeAttributeEvent(c, cutBox.get()));
                            }

                            deactivateModifyAnchorCutListener();
                        } else {
                            deactivateModifyAnchorCutListener();
                        }
                    }
                }
            }
        };

        pointMoveListener = new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                //super.mouseMoved(e);
                currentModifiedCut.movePoint(e.getPoint(), renderer, mainWindow, indexPoint);
            }
        };

        cutMoveListener = new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                //super.mouseMoved(e);
                currentModifiedCut.moveUpdate(e.getPoint(), renderer, mainWindow);
            }
        };

    }

    public void initModifyAnchor(CutDTO cutToChangeAnchor) {
        Optional<DrawCutWrapper> modifiedCut = getWrapperById(cutToChangeAnchor.getId());

        if (modifiedCut.isPresent()) {
            currentModifiedCut = modifiedCut.get();
            deactivateModifyAnchorCutListener();
            ;
            currentModifiedCut.emptyRefs();
            setState(DrawingState.MODIFY_ANCHOR);
            activateModifyAnchorCutListener();
        }

    }

    public void initModifyPoint(DrawCutWrapper cutToChangePoint, PersoPoint pointToChange) {
        currentModifiedCut = cutToChangePoint;
        currentModifiedCut.setPointDepart(renderer.getMmMousePt());
        prevCut = cutToChangePoint.getCutDTO();
        prevPts = mainWindow.getController().getAbsolutePointsPosition(prevCut);
        currentModifiedPoint = pointToChange;
        for (int i = 0; i<currentModifiedCut.getPersoPoints().size(); i++) {
            if (currentModifiedCut.getPersoPoints().get(i) == pointToChange) {
                indexPoint = i;
                //System.out.println("lol" + i);
            }
        }
        setState(Drawing.DrawingState.MODIFY_POINT);
        activateModifyPointCutListener();
    }


    public void closeModifyPoint() {
        setState(DrawingState.IDLE);
        currentModifiedCut.emptyRefs();
        deactivateModifyPointCutListener();
    }

    public void initModifyCut(DrawCutWrapper cutToChangePoint) {
        currentModifiedCut = cutToChangePoint;
        currentModifiedCut.setPointDepart(renderer.getMmMousePt());
        setState(DrawingState.MODIFY_CUT);
        activateModifyCutListener();
        prevCut = cutToChangePoint.getCutDTO();
        prevPts = mainWindow.getController().getAbsolutePointsPosition(prevCut);
    }

    public void closeModifyCut() {
        setState(DrawingState.IDLE);
        //mainWindow.getController().executeAndMemorize(()->mainWindow.getController().modifyCut(currentModifiedCut.getCutDTO()), ()->mainWindow.getController().modifyCut(prevCut));
        currentModifiedCut.emptyRefs();
        deactivateModifyCutListener();
    }

    private void activateModifyPointCutListener() {
        renderer.addMouseMotionListener(pointMoveListener);
        System.out.println("activateModifyPointCutListener");
    }

    private void deactivateModifyPointCutListener() {
        renderer.removeMouseMotionListener(pointMoveListener);
        System.out.println("deactivateModifyPointCutListener");
    }

    private void activateModifyCutListener() {
        renderer.addMouseMotionListener(cutMoveListener);
        System.out.println("activateModifyCutListener");
    }

    private void deactivateModifyCutListener() {
        renderer.removeMouseMotionListener(cutMoveListener);
        System.out.println("deactivateModifyCutListener");
    }

    public void setState(DrawingState state) {
        if (state != DrawingState.CREATE_CUT) {
            mainWindow.getLeftBar().deactivateAllCuts();
        }
        this.state = state;
    }


    /**
     * Activates the cutListener so that the board reacts when a cut is being made
     */
    private void activateCreateCutListener() {
        renderer.addMouseMotionListener(createCutMoveListener);
        renderer.addMouseListener(createCutActionListener);
        currentDrawingCut.createCursorPoint(this.renderer);
    }

    private void activateModifyAnchorCutListener() {
        renderer.addMouseMotionListener(modifyAnchorMoveListener);
        renderer.addMouseListener(modifyAnchorActionListener);
        currentModifiedCut.createCursorPoint(this.renderer);
    }

    /**
     * Deactivate the cutListener
     */
    public void deactivateCreateCutListener() {
        renderer.removeMouseMotionListener(createCutMoveListener);
        renderer.removeMouseListener(createCutActionListener);
        currentDrawingCut.destroyCursorPoint();
        setState(DrawingState.IDLE);
        renderer.repaint();
    }

    private void deactivateModifyAnchorCutListener() {
        renderer.removeMouseMotionListener(modifyAnchorMoveListener);
        renderer.removeMouseListener(modifyAnchorActionListener);
        currentModifiedCut.destroyCursorPoint();
        setState(DrawingState.IDLE);
        renderer.repaint();
    }

    public void changeSelectedWrapperById(UUID id) {

        for (DrawCutWrapper wrapper : cutWrappers) {
            if (wrapper.getState() == DrawCutWrapper.DrawCutState.SELECTED) {
                wrapper.setState(DrawCutWrapper.DrawCutState.NOT_SELECTED, renderer);
            }
        }

        for (DrawCutWrapper wrapper : cutWrappers) {
            if (wrapper.getCutDTO().getId() == id) {
                wrapper.setState(DrawCutWrapper.DrawCutState.SELECTED, renderer);
            }
        }
    }

    public void changeHoverWrapperById(UUID id) {
        for (DrawCutWrapper wrapper : cutWrappers) {
            if (wrapper.getCutDTO().getId() == id) {
                wrapper.setState(DrawCutWrapper.DrawCutState.HOVER, renderer);
            }
        }
    }

    public void changeNotSelectedWrapperById(UUID id) {
        for (DrawCutWrapper wrapper : cutWrappers) {
            if (wrapper.getCutDTO().getId() == id) {
                wrapper.setState(DrawCutWrapper.DrawCutState.NOT_SELECTED, renderer);
            }
        }
    }

    public void changeRefWrapperById(UUID id) {
        for (DrawCutWrapper wrapper : cutWrappers) {
            if (wrapper.getCutDTO().getId() == id) {
                wrapper.setState(DrawCutWrapper.DrawCutState.REF, renderer);
            }
        }
    }

    public void changeInvalidWrapperById(UUID id) {
        for (DrawCutWrapper wrapper : cutWrappers) {
            if (wrapper.getCutDTO().getId() == id) {
                wrapper.setState(DrawCutWrapper.DrawCutState.INVALID, renderer);
            }
        }
    }

    public void changeAllInvalid() {
        for (DrawCutWrapper wrapper : cutWrappers) {
            if (wrapper.getCutDTO().getState() == CutState.NOT_VALID) {
                wrapper.setState(DrawCutWrapper.DrawCutState.INVALID, renderer);
            }
        }
    }

    public void changeGoBackWrapperById(UUID id) {
        for (DrawCutWrapper wrapper : cutWrappers) {
            if (wrapper.getCutDTO().getId() == id) {
                wrapper.goBackState(renderer);
            }
        }
    }

    public Optional<DrawCutWrapper> getWrapperById(UUID id) {
        for (DrawCutWrapper wrapper : cutWrappers) {
            if (wrapper.getCutDTO().getId() == id) {
                return Optional.of(wrapper);
            }
        }
        return Optional.empty();
    }

    public List<VertexDTO> getPrevPts() {
        return prevPts;
    }
}

