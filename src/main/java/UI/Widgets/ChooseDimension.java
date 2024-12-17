package UI.Widgets;

import Common.DTO.VertexDTO;
import UI.Display2D.Rendering2DWindow;
import UI.UIConfig;
import UI.UiUnits;

import javax.swing.*;
import java.awt.*;

/**
 * ChooseDimension provides a UI window for setting and adjusting the dimensions of a 2D rendering panel.
 * It displays input fields for width (x) and height (y), allowing the user to modify dimensions dynamically.
 *
 * @author Adam Côté
 * @version 1.1
 * @since 2024-11-02
 */
public class ChooseDimension extends GenericAttributeBox implements Attributable {

    private final Rendering2DWindow rend;
    private final boolean gridDisplayed;
    private MeasurementInputField xTextField;
    private MeasurementInputField yTextField;
    private MeasurementInputField zTextField;
    private MeasurementInputField gridPrecision;
    private PixelNoUnitInputField magnetPrecision;

    /**
     * Constructs a ChooseDimension panel for modifying the width and height of the specified Rendering2DWindow.
     *
     * @param rend The Rendering2DWindow instance to be resized
     */
    public ChooseDimension(Rendering2DWindow rend, boolean gridDisplayed) {
        super(false, "Panneau");
        this.rend = rend;
        this.gridDisplayed = gridDisplayed;
        init();
        addEventListenerToPointBox();
    }

    /**
     * @return The NumberTextfield for the width input
     */
    public MeasurementInputField getxTextField() {
        return xTextField;
    }

    /**
     * @return The NumberTextfield for the height input
     */
    public MeasurementInputField getyTextField() {
        return yTextField;
    }

    /**
     * @return The NumberTextfield for the height input
     */
    public MeasurementInputField getzTextField() {
        return zTextField;
    }

    /**
     * Initializes the dimension setting UI with labels and input fields for width (x) and height (y),
     * with real-time resizing functionality.
     */
    private void init() {
        VertexDTO panelSize = rend.getMainWindow().getController().getPanelDTO().getPanelDimension();
        xTextField = new MeasurementInputField(rend.getMainWindow(), "Largeur   ", panelSize.getX(), UIConfig.INSTANCE.getDefaultUnit());
        yTextField = new MeasurementInputField(rend.getMainWindow(), "Hauteur   ", panelSize.getY(), UIConfig.INSTANCE.getDefaultUnit());
        zTextField = new MeasurementInputField(rend.getMainWindow(), "Épaisseur", panelSize.getZ(), UIConfig.INSTANCE.getDefaultUnit());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(xTextField, gbc);
        gbc.gridy = 2;
        add(yTextField, gbc);
        gbc.gridy = 3;
        add(zTextField, gbc);

        if (gridDisplayed) {
            gridPrecision = new MeasurementInputField(rend.getMainWindow(), "Taille grille", rend.getMainWindow().getController().getGrid().getSize(), UiUnits.MILLIMETERS);
            magnetPrecision = new PixelNoUnitInputField(rend.getMainWindow(), "Précision aimant", rend.getMainWindow().getController().getGrid().getMagnetPrecision(), "px");
            gbc.gridy = 4;
            add(gridPrecision, gbc);
            gbc.gridy = 5;
            add(magnetPrecision, gbc);
        }
    }

    private void addEventListenerToPointBox() {
        xTextField.getNumericInput().addPropertyChangeListener("value", evt -> {
            rend.clearPoints();
            rend.resizePanneau(xTextField.getMMValue(), rend.getBoard().getHeight());
        });
        yTextField.getNumericInput().addPropertyChangeListener("value", evt -> {
            rend.clearPoints();
            rend.resizePanneau(rend.getBoard().getWidth(), yTextField.getMMValue());
        });
        zTextField.getNumericInput().addPropertyChangeListener("value", evt -> {
            rend.clearPoints();
            rend.getMainWindow().getController().resizePanel(rend.getBoard().getWidth(), rend.getBoard().getHeight(), zTextField.getMMValue());
        });
        if (gridPrecision != null) {
            gridPrecision.getNumericInput().addPropertyChangeListener("value", evt -> {
                // TODO clarify what precision means and why it's an int
                rend.getMainWindow().getController().putGrid(gridPrecision.getMMValue(), rend.getMainWindow().getController().getGrid().getMagnetPrecision());
                rend.repaint();
            });
        }
        if (magnetPrecision != null) {
            magnetPrecision.getNumericInput().addPropertyChangeListener("value", evt -> {
                rend.getMainWindow().getController().putGrid(rend.getMainWindow().getController().getGrid().getSize(), ((Number) magnetPrecision.getNumericInput().getValue()).intValue());
                rend.repaint();
            });
        }
    }

    /**
     * @return JLabel representing the name of this component.
     */
    @Override
    public JLabel showName() {
        return new JLabel("");
    }

    @Override
    public JPanel showErrors() {
        return new JPanel();
    }

    /**
     * @return JPanel representation of this component's attribute view.
     */
    @Override
    public JPanel showAttribute() {
        return this;
    }

}

