package UI;

import javax.swing.*;
import java.awt.*;

/**
 * The {@code UIConfig} enum encapsulates a singleton instance of the configurations
 * and parameters of the UI
 *
 * @author Louis-Etienne Messier
 * @version 0.1
 * @since 2024-09-21
 */
public enum UIConfig {
    INSTANCE;

    private final String windowTitle = "FreeCarve";
    private final int defaultWindowWidth = 800;
    private final int defaultWindowHeight = 600;
    private final int toolIconSize = 20;
    private final int projectSelectionMenuButtonSize = 50;

    private Color colorGreen1 = new Color(78, 167, 46);
    private Color colorBlue1 = new Color(21, 96, 130);
    private Color colorButtonBackground = UIManager.getColor("Button.background");

    private UIConfig() {
    }

    /**
     * @return the title of the application
     */
    public String getWindowTitle() {
        return windowTitle;
    }

    /**
     * @return the default width of the application on launch
     */
    public int getDefaultWindowWidth() {
        return defaultWindowWidth;
    }

    /**
     * @return the default height of the application on launch
     */
    public int getDefaultWindowHeight() {
        return defaultWindowHeight;
    }

    /**
     * @return the size which the icons on the toolbar should have
     */
    public int getToolIconSize() {
        return toolIconSize;
    }

    /**
     * @return The size which the button in the project selection window should be
     */
    public int getProjectSelectionMenuButtonSize() {
        return projectSelectionMenuButtonSize;
    }

    /**
     * @return the colorGreen1 of the UI
     */
    public Color getColorGreen1() {
        return colorGreen1;
    }

    /**
     * @return the colorBlue1 of the UI
     */
    public Color getColorBlue1() {
        return colorBlue1;
    }

    /**
     * @return the color of the button background of the UI
     */
    public Color getColorButtonBackground() {
        return colorButtonBackground;
    }
}