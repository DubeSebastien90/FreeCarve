package UI;

import javax.swing.*;
import java.awt.*;

import UI.Display2D.Rendering2DWindow;
import UI.LeftBar.ToolBar.Tool;


/**
 * The {@code MiddleContent} class encapsulates the UI container of the middle
 * section of the UI : it contains all of the sub-windows
 *
 * @author Louis-Etienne Messier & Adam Côté
 * @version 0.1
 * @since 2024-09-21
 */
public class MiddleContent {

    /**
     * Enumeration used to represent one of the different window that can be displayed
     */
    public enum MiddleWindowType {
        CONFIG, CUT, EXPORT
    }

    private final JPanel panel;
    private CutWindow cutWindow;
    private ConfigChoiceWindow configChoiceWindow;
    private ExportWindow exportWindow;
    private final MainWindow mainWindow;
    private MiddleWindowType current;

    public MiddleContent(MainWindow mainWindow) {
        this.panel = new JPanel();
        this.mainWindow = mainWindow;
        init(mainWindow);
        this.panel.setBackground(Color.RED);
    }

    /**
     * @return The configChoiceWindow of the project.
     */
    public ConfigChoiceWindow getConfigChoiceWindow() {
        return this.configChoiceWindow;
    }

    /**
     * @return The cutWindow of the project.
     */
    public CutWindow getCutWindow() {
        return this.cutWindow;
    }

    /**
     * @return The currently displayed window
     */
    public MiddleWindowType getCurrent() {
        return this.current;
    }

    /**
     * @return The panel of the middle window
     */
    public JPanel getPanel() {
        return panel;
    }

    /**
     * Initiates all of the {@code MiddleContent} components
     */
    private void init(MainWindow mainWindow) {
        cutWindow = new CutWindow(mainWindow);
        configChoiceWindow = new ConfigChoiceWindow(mainWindow);
        exportWindow = new ExportWindow(mainWindow);


        this.panel.setLayout(new CardLayout());
        panel.add(configChoiceWindow, "config");
        panel.add(cutWindow.getCutWindow(), "cut");
        panel.add(exportWindow, "export");

        current = MiddleWindowType.CONFIG;
        changePanel(current);
    }

    /**
     * Navigates to the next window and display it on screen
     */
    public void nextWindow() {
        int ordinal = current.ordinal();
        if (ordinal + 1 < MiddleWindowType.values().length) {
            changePanel(MiddleWindowType.values()[ordinal + 1]);
        }
    }

    /**
     * Navigates to the next window and display it on screen
     */
    public void previousWindow() {
        int ordinal = current.ordinal();
        if (ordinal - 1 >= 0) {
            changePanel(MiddleWindowType.values()[ordinal - 1]);
        }
    }

    /**
     * Navigates to the desired window and display it on screen
     *
     * @param type The window that need to be displayed.
     */
    public void changePanel(MiddleWindowType type) {
        DownBar db = mainWindow.getDownBar();
        LeftBar lb = mainWindow.getLeftBar();
        switch (type) {
            case CONFIG -> {
                ((CardLayout) panel.getLayout()).show(panel, "config");
                current = MiddleWindowType.CONFIG;
                configChoiceWindow.getRendering2DWindow().setAll(cutWindow.getRenderer());
                configChoiceWindow.requestFocusInWindow();
                db.setButtonBlueToIndex(0);
                lb.getToolBar().enableTools(new Tool[]{Tool.ZOOMIN, Tool.ZOOMOUT, Tool.SCALE, Tool.MAGNET, Tool.GRID});
                lb.getToolBar().disableTools(new Tool[]{Tool.COUPEL, Tool.FREE_LINE, Tool.VERTICAL, Tool.HORIZONTAL, Tool.RECTANGLE, Tool.TRASH, Tool.RETAILLER, Tool.FORBIDDEN});
            }
            case CUT -> {
                ((CardLayout) panel.getLayout()).show(panel, "cut");
                current = MiddleWindowType.CUT;
                cutWindow.getRenderer().setAll(configChoiceWindow.getRendering2DWindow());
                cutWindow.getScreen(1).requestFocusInWindow();
                db.setButtonBlueToIndex(1);
                lb.getToolBar().enableTools(new Tool[]{Tool.ZOOMIN, Tool.ZOOMOUT, Tool.COUPEL, Tool.GRID, Tool.MAGNET, Tool.FREE_LINE, Tool.VERTICAL, Tool.HORIZONTAL, Tool.RECTANGLE, Tool.RETAILLER, Tool.FORBIDDEN});
                lb.getToolBar().disableTools(new Tool[]{Tool.SCALE, Tool.TRASH});
            }
            case EXPORT -> {
                ((CardLayout) panel.getLayout()).show(panel, "export");
                current = MiddleWindowType.EXPORT;
                mainWindow.getController().setScene();
                exportWindow.calculateGcode();
                exportWindow.getRenderer().requestFocusInWindow();
                db.setButtonBlueToIndex(2);
            }
        }
    }

    /**
     * Zooms on the origin of the Rendering2dWindow if one is currently displayed on this JPanel
     *
     * @param zoomfactor The zooming delta. A negative one will make the board seems bigger.
     */
    public void zoom(int zoomfactor) {
        if (current == MiddleWindowType.CONFIG) {
            configChoiceWindow.getRendering2DWindow().zoomOrigin(zoomfactor);
        } else if (current == MiddleWindowType.CUT) {
            ((Rendering2DWindow) cutWindow.getScreen(1)).zoomOrigin(zoomfactor);
        }
    }
}
