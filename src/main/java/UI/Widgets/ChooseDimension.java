package UI.Widgets;

import Common.Interfaces.IUnitConverter;
import UI.Display2D.Rendering2DWindow;


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
    private CustomNumericInputField xTextField;
    private CustomNumericInputField yTextField;
    private CustomNumericInputField gridPrecision;
    private CustomNumericInputField magnetPrecision;

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
    public CustomNumericInputField getxTextField() {
        return xTextField;
    }

    /**
     * @return The NumberTextfield for the height input
     */
    public CustomNumericInputField getyTextField() {
        return yTextField;
    }

    /**
     * Initializes the dimension setting UI with labels and input fields for width (x) and height (y),
     * with real-time resizing functionality.
     */
    private void init() {
        IUnitConverter unitConverter = rend.getMainWindow().getController();
        double displayWidth = Math.round(rend.getBoard().getWidth() * 100);
        displayWidth = displayWidth / 100;
        double displayHeight = Math.round(rend.getBoard().getHeight() * 100);
        displayHeight = displayHeight / 100;
        xTextField = new CustomNumericInputField(unitConverter, "Width", displayWidth, 0, rend.getMainWindow().getController().getPanelDTO().getMaxMMWidth());
        yTextField = new CustomNumericInputField(unitConverter, "Height", displayHeight, 0, rend.getMainWindow().getController().getPanelDTO().getMaxMMHeight());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(xTextField, gbc);
        gbc.gridy = 2;
        add(yTextField, gbc);

        if (gridDisplayed) {
            gridPrecision = new CustomNumericInputField(unitConverter, "Grid size", rend.getMainWindow().getController().getGrid().getSize(), 0, Double.POSITIVE_INFINITY);
            magnetPrecision = new CustomNumericInputField(unitConverter, "Magnet Precision", rend.getMainWindow().getController().getGrid().getMagnetPrecision(), 0, Double.POSITIVE_INFINITY);
            gbc.gridy = 3;
            add(gridPrecision, gbc);
            gbc.gridy = 4;
            add(magnetPrecision, gbc);
        }
    }

    private void addEventListenerToPointBox() {
        xTextField.getNumericInput().addPropertyChangeListener("value", evt -> {
            rend.resizePanneau(xTextField.getMMValue(), rend.getBoard().getHeight());
        });
        yTextField.getNumericInput().addPropertyChangeListener("value", evt -> {
            rend.resizePanneau(yTextField.getMMValue(), rend.getBoard().getHeight());
        });
        if (gridPrecision != null) {
            gridPrecision.getNumericInput().addPropertyChangeListener("value", evt -> {
                // TODO clarify what precision means and why it's an int
                rend.getMainWindow().getController().putGrid((int)gridPrecision.getMMValue(), rend.getMainWindow().getController().getGrid().getMagnetPrecision());
                rend.repaint();
            });
        }
        if (magnetPrecision != null) {
            magnetPrecision.getNumericInput().addPropertyChangeListener("value", evt -> {
                rend.getMainWindow().getController().putGrid(rend.getMainWindow().getController().getGrid().getSize(), (int)magnetPrecision.getMMValue());
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

    /**
     * @return JPanel representation of this component's attribute view.
     */
    @Override
    public JPanel showAttribute() {
        return this;
    }
}

