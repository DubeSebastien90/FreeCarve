package UI;

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

    private String windowTitle = "FreeCarve";
    private int defaultWindowWidth = 800;
    private int defaultWindowHeight = 600;

    private UIConfig(){}

    /**
     * @return the title of the application
     */
    public String getWindowTitle(){
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
}
