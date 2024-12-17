package UI;

import Common.DTO.DimensionDTO;
import Common.DTO.VertexDTO;
import Common.Units;
import Domain.Controller;

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
    private final int defaultWindowWidth = 1200;
    private final int defaultWindowHeight = 800;

    private double defaultBoardWidthMM = Controller.getDefaultPanelDimension().getX();
    private double defaultBoardHeightMM = Controller.getDefaultPanelDimension().getY();
    private final double defaultPanelOffsetX = 100;
    private final double defaultPanelOffsetY = 100;
    private final double defaultGridSize = 76.2;
    private final int defaultMagnetPrecision = 10;

    private UiUnits defaultUnit = UiUnits.MILLIMETERS;
    private final int toolIconSize = 20;
    private final int projectSelectionMenuButtonSize = 50;
    private final int cutBoxIconSize = 20;
    private final int scrollbarSpeed = 16;
    private final int defaultPadding = 10;
    private final int MAX_NB_BITS = 12;
    private final double MAGIC_ICON_NUMBER = 1.89;
    private final int NB_CLICKS_BEFORE_MUSIC = 5;
    private final double cursorRadius = 25;
    private final double defaultSnapThreshold = 10;

    private final Color INVALID_COLOR = Color.RED;
    private final Color ANCHOR_COLOR = Color.BLACK;
    private final Color SNAP_COLOR = Color.YELLOW;
    private final Color SELECTED_COLOR = Color.GREEN;
    private final Color VALID_COLOR = Color.GREEN;
    private final Color HOVER_COLOR = Color.BLUE;
    private final Color ARROW_COLOR = Color.white;
    private final Color DIMENSION_COLOR = Color.BLACK;
    private final Color CLAMP_COLOR = new Color(255, 0, 0, 200);
    private final Color HOVER_VIEW_COLOR = Color.MAGENTA;
    private Color PANEL_COLOR = new Color(222, 184, 135);

    private final int ARROW_DIMENSION = 2;
    private final double CLAMP_STROKE_WIDTH = 10.0;
    private final Color strokeColor = Color.BLACK;
    private final double defaultStrokeWidth = 3.0f;

    private final double scalingPanelPointRadius = 30;


    private final int CREATE_CUT_ICON_SIZE = 15;
    private final VertexDTO CREATE_CUT_ICON_OFFSET = new VertexDTO(12, 12, 0);

    private UIConfig() {
    }

    public double getScalingPanelPointRadius() {
        return scalingPanelPointRadius;
    }

    public double getDefaultGridSize() {
        return defaultGridSize;
    }

    public int getDefaultMagnetPrecision() {
        return defaultMagnetPrecision;
    }

    public double getDefaultPanelOffsetX() {
        return defaultPanelOffsetX;
    }

    public double getDefaultPanelOffsetY() {
        return defaultPanelOffsetY;
    }

    public Color getPANEL_COLOR() {
        return PANEL_COLOR;
    }

    public double getDefaultStrokeWidth() {
        return defaultStrokeWidth;
    }

    public Color getStrokeColor() {
        return strokeColor;
    }

    public double getCLAMP_STROKE_WIDTH() {
        return CLAMP_STROKE_WIDTH;
    }

    public Color getINVALID_COLOR() {
        return INVALID_COLOR;
    }

    public Color getANCHOR_COLOR() {
        return ANCHOR_COLOR;
    }

    public Color getSNAP_COLOR() {
        return SNAP_COLOR;
    }

    public Color getSELECTED_COLOR() {
        return SELECTED_COLOR;
    }

    public Color getVALID_COLOR() {
        return VALID_COLOR;
    }

    public Color getHOVER_COLOR() {
        return HOVER_COLOR;
    }

    public Color getARROW_COLOR() {
        return ARROW_COLOR;
    }

    public Color getDIMENSION_COLOR() {
        return DIMENSION_COLOR;
    }

    public Color getCLAMP_COLOR() {
        return CLAMP_COLOR;
    }

    public Color getHOVER_VIEW_COLOR() {
        return HOVER_VIEW_COLOR;
    }

    public int getARROW_DIMENSION() {
        return ARROW_DIMENSION;
    }

    public double getDefaultSnapThreshold() {
        return defaultSnapThreshold;
    }

    public double getMAGIC_ICON_NUMBER() {
        return MAGIC_ICON_NUMBER;
    }

    public int getNB_CLICKS_BEFORE_MUSIC() {
        return NB_CLICKS_BEFORE_MUSIC;
    }

    public double getCursorRadius() {
        return cursorRadius;
    }

    /**
     * @return the magic icon number
     */
    public double getNbClicksBeforeMusic() {
        return NB_CLICKS_BEFORE_MUSIC;
    }

    /**
     * @return the magic icon number
     */
    public double getMagicIconNumber() {
        return MAGIC_ICON_NUMBER;
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
    public int getDefaultPadding() {
        return defaultPadding;
    }

    public int getMAX_NB_BITS() {
        return MAX_NB_BITS;
    }

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

    public void setPANEL_COLOR(Color PANEL_COLOR) {
        this.PANEL_COLOR = PANEL_COLOR;
    }

    public UiUnits getDefaultUnit() {
        return defaultUnit;
    }

    public void setDefaultUnit(UiUnits defaultUnit) {
        this.defaultUnit = defaultUnit;
    }

    public int getCREATE_CUT_ICON_SIZE() {
        return CREATE_CUT_ICON_SIZE;
    }

    public VertexDTO getCREATE_CUT_ICON_OFFSET(){return CREATE_CUT_ICON_OFFSET;}

}
