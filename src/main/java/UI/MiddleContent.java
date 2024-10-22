package UI;
import Domain.Controller;
import Domain.Cut;
import Domain.DTO.BitDTO;


import javax.swing.*;
import java.awt.*;

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
        FOLDER, CONFIG, CUT, SIMULATION, EXPORT
    }

    private final JPanel panel;
    private CutWindow cutWindow;
    private FolderWindow projectWindow;
    private ConfigChoiceWindow configChoiceWindow;
    private SimulationWindow simulationWindow;
    private ExportWindow exportWindow;
    private MainWindow mainWindow;
    private MiddleWindowType current;

    public MiddleContent(MainWindow mainWindow) {
        this.panel = new JPanel();
        this.mainWindow = mainWindow;
        init();
        this.panel.setBackground(Color.RED);
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
    private void init() {
        cutWindow = new CutWindow();
        projectWindow = new FolderWindow();
        simulationWindow = new SimulationWindow();
        configChoiceWindow = new ConfigChoiceWindow();
        exportWindow = new ExportWindow();


        this.panel.setLayout(new CardLayout());
        panel.add(projectWindow, "folder");
        panel.add(configChoiceWindow, "config");
        panel.add(cutWindow.getCutWindow(), "cut");
        panel.add(simulationWindow, "simulation");
        panel.add(exportWindow, "export");

        current = MiddleWindowType.FOLDER;
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
        DownBar db = MainWindow.INSTANCE.getDownBar();
        switch (type) {
            case FOLDER -> {
                ((CardLayout) panel.getLayout()).show(panel, "folder");
                current = MiddleWindowType.FOLDER;
                projectWindow.requestFocusInWindow();
                db.setButtonBlueToIndex(0);
            }
            case CONFIG -> {
                ((CardLayout) panel.getLayout()).show(panel, "config");
                current = MiddleWindowType.CONFIG;
                configChoiceWindow.requestFocusInWindow();
                db.setButtonBlueToIndex(1);
            }
            case CUT -> {
                ((CardLayout) panel.getLayout()).show(panel, "cut");
                current = MiddleWindowType.CUT;
                cutWindow.getScreen(1).requestFocusInWindow();
                db.setButtonBlueToIndex(2);
            }
            case SIMULATION -> {
                ((CardLayout) panel.getLayout()).show(panel, "simulation");
                current = MiddleWindowType.SIMULATION;
                simulationWindow.getRenderer().requestFocusInWindow();
                db.setButtonBlueToIndex(3);
            }
            case EXPORT -> {
                ((CardLayout) panel.getLayout()).show(panel, "export");
                current = MiddleWindowType.EXPORT;
                exportWindow.getRenderer().requestFocusInWindow();
                db.setButtonBlueToIndex(4);
            }
        }
    }
}
