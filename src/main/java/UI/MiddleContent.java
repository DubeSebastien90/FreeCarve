package UI;

import Common.DTO.BitDTO;
import UI.Display2D.Rendering2DWindow;
import UI.Events.ChangeCutEvent;
import UI.LeftBar.ToolBar.Tool;
import UI.Listeners.PanelObservers;
import UI.SubWindows.CutListPanel;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

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
         CONFIG, CUT, SIMULATION, EXPORT
    }

    private final JPanel panel;
    private CutWindow cutWindow;
    private FolderWindow projectWindow;
    private ConfigChoiceWindow configChoiceWindow;
    private SimulationWindow simulationWindow;
    private ExportWindow exportWindow;
    private final MainWindow mainWindow;
    private MiddleWindowType current;
    private Map<Integer, BitDTO> configuredBitsMap;

    public MiddleContent(MainWindow mainWindow) {
        this.panel = new JPanel();
        this.mainWindow = mainWindow;
        configuredBitsMap = mainWindow.getController().refreshConfiguredBitMaps();
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
        cutWindow = new CutWindow(mainWindow, configuredBitsMap);
        projectWindow = new FolderWindow(mainWindow);
        simulationWindow = new SimulationWindow(mainWindow);
        configChoiceWindow = new ConfigChoiceWindow(mainWindow, configuredBitsMap);
        exportWindow = new ExportWindow(mainWindow);


        this.panel.setLayout(new CardLayout());
        //panel.add(projectWindow, "folder");
        panel.add(configChoiceWindow, "config");
        panel.add(cutWindow.getCutWindow(), "cut");
        panel.add(simulationWindow, "simulation");
        panel.add(exportWindow, "export");

        current = MiddleWindowType.CONFIG;
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
//            case FOLDER -> {
//                ((CardLayout) panel.getLayout()).show(panel, "folder");
//                current = MiddleWindowType.FOLDER;
//                projectWindow.requestFocusInWindow();
//                db.setButtonBlueToIndex(0);
//            }
            case CONFIG -> {
                ((CardLayout) panel.getLayout()).show(panel, "config");
                current = MiddleWindowType.CONFIG;
                configChoiceWindow.getRendering2DWindow().setAll(cutWindow.getRenderer());
                configChoiceWindow.requestFocusInWindow();
                db.setButtonBlueToIndex(0);
                lb.getToolBar().enableTools(new Tool[]{Tool.ZOOMIN, Tool.ZOOMOUT, Tool.SCALE, Tool.FORBIDDEN, Tool.MAGNET, Tool.GRID});
                lb.getToolBar().disableTools(new Tool[]{Tool.COUPEL, Tool.FREE_LINE, Tool.VERTICAL, Tool.HORIZONTAL, Tool.RECTANGLE, Tool.TRASH, Tool.RETAILLER});
            }
            case CUT -> {
                ((CardLayout) panel.getLayout()).show(panel, "cut");
                current = MiddleWindowType.CUT;
                cutWindow.getRenderer().setAll(configChoiceWindow.getRendering2DWindow());
                cutWindow.getScreen(1).requestFocusInWindow();
                db.setButtonBlueToIndex(1);
                lb.getToolBar().enableTools(new Tool[]{Tool.ZOOMIN, Tool.ZOOMOUT, Tool.COUPEL, Tool.GRID, Tool.MAGNET, Tool.FREE_LINE, Tool.VERTICAL, Tool.HORIZONTAL, Tool.RECTANGLE, Tool.RETAILLER});
                lb.getToolBar().disableTools(new Tool[]{Tool.SCALE, Tool.FORBIDDEN, Tool.TRASH});
            }
            case SIMULATION -> {
                ((CardLayout) panel.getLayout()).show(panel, "simulation");
                current = MiddleWindowType.SIMULATION;
                mainWindow.getController().setScene();
                simulationWindow.getRenderer().requestFocusInWindow();
                db.setButtonBlueToIndex(2);
            }
            case EXPORT -> {
                ((CardLayout) panel.getLayout()).show(panel, "export");
                current = MiddleWindowType.EXPORT;
                mainWindow.getController().setScene();
                exportWindow.calculateGcode();
                exportWindow.getRenderer().requestFocusInWindow();
                db.setButtonBlueToIndex(3);
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

    /**
     * Listener for the configured bits
     * Called when the user configures a bit in the ConfigChoiceWindow
     *
     * @param index         The index of the bit in the list of bits
     * @param configuredBit The BitDTO of the configured bit
     */
    public void configuredBitsListener(int index, BitDTO configuredBit) {
        configuredBitsMap = mainWindow.getController().refreshConfiguredBitMaps();
    }

    /**
     * The configuredBitsMap is a map containing the index of the in the list of bits and it's corresponding BitDTO
     *
     * @return The configuredBitsMap of the project.
     */
    public Map<Integer, Common.DTO.BitDTO> getConfiguredBitsMap() {
        return configuredBitsMap;
    }
}
