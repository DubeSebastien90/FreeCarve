package UI;

import Common.DTO.DimensionDTO;
import Common.Units;

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
    private final int defaultWindowWidth = 1200;
    private final int defaultWindowHeight = 800;
    private double defaultBoardWidthMM = 1219.2;
    private double defaultBoardHeightMM = 914.4;
    private final int toolIconSize = 20;
    private final int projectSelectionMenuButtonSize = 50;
    private final int cutBoxIconSize = 20;
    private final int scrollbarSpeed = 16;
    private final int defaultPadding = 10;
    private final int MAX_NB_BITS = 12;

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
     * @return The size which the icons of the cutBoxes in the CutList should be
     */
    public int getCutBoxIconSize() {
        return cutBoxIconSize;
    }

    /**
     * @return The speed of what the scrollbar should be
     */
    public int getScrollbarSpeed() {
        return scrollbarSpeed;
    }

    /**
     * @return the default padding of the element of this application
     */
    public int getDefaultPadding() {return defaultPadding;}

    public int getMAX_NB_BITS() { return MAX_NB_BITS; }

    public double getDefaultBoardWidthMM() {
        return defaultBoardWidthMM;
    }

    public void setDefaultBoardWidthMM(double defaultBoardWidthMM) {
        this.defaultBoardWidthMM = defaultBoardWidthMM;
    }

    public double getDefaultBoardHeightMM() {
        return defaultBoardHeightMM;
    }

    public void setDefaultBoardHeightMM(double defaultBoardHeightMM) {
        this.defaultBoardHeightMM = defaultBoardHeightMM;
    }
}
