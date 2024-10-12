package UI;

import Buisness.Project;
import Util.Cut;

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

    private final Project.ProjectMiddleWindow thisState = Project.INSTANCE.getProjectMiddleWindow();
    private JPanel panel;
    private CutWindow cutWindow;
    private FolderWindow projectWindow;
    private ConfigChoiceWindow configChoiceWindow;
    private SimulationWindow simulationWindow;
    private ExportWindow exportWindow;

    private MiddleWindowType current;

    public MiddleContent() {
        this.panel = new JPanel();
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
        switch (type) {
            case FOLDER -> {
                ((CardLayout) panel.getLayout()).show(panel, "folder");
                current = MiddleWindowType.FOLDER;
                projectWindow.requestFocusInWindow();
                break;
            }
            case CONFIG -> {
                ((CardLayout) panel.getLayout()).show(panel, "config");
                current = MiddleWindowType.CONFIG;
                configChoiceWindow.requestFocusInWindow();
                break;
            }
            case CUT -> {
                ((CardLayout) panel.getLayout()).show(panel, "cut");
                current = MiddleWindowType.CUT;
                cutWindow.getScreen(1).requestFocusInWindow();
                break;
            }
            case SIMULATION -> {
                ((CardLayout) panel.getLayout()).show(panel, "simulation");
                current = MiddleWindowType.SIMULATION;
                simulationWindow.getRenderer().requestFocusInWindow();
                break;
            }
            case EXPORT -> {
                ((CardLayout) panel.getLayout()).show(panel, "export");
                current = MiddleWindowType.EXPORT;
                exportWindow.getRenderer().requestFocusInWindow();
                current = MiddleWindowType.EXPORT;
                break;
            }
        }
    }
}
