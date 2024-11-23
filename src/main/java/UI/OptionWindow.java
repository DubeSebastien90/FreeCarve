package UI;

import UI.Widgets.MeasurementInputField;

import javax.swing.*;
import java.awt.*;

public class OptionWindow extends JPanel {
    public OptionWindow(MainWindow mainWindow) {
        init(mainWindow);
    }

    private void init(MainWindow mainWindow) {
        //TODO implement undo redo for settings
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(new JLabel("Ceci est le panneau à options"));

        MeasurementInputField defaultPanelWidth = new MeasurementInputField(mainWindow, "Largeur par défaut", UIConfig.INSTANCE.getDefaultBoardWidthMM(), UiUnits.MILLIMETERS);
        defaultPanelWidth.setCurrentUnit(UiUnits.FEET);
        defaultPanelWidth.getNumericInput().addPropertyChangeListener("value", evt -> {
            UIConfig.INSTANCE.setDefaultBoardWidthMM(defaultPanelWidth.getMMValue());
        });
        defaultPanelWidth.setMaximumSize(new Dimension(800, 50));
        add(defaultPanelWidth);

        MeasurementInputField defaultPanelHeight = new MeasurementInputField(mainWindow, "Hauteur par défaut", UIConfig.INSTANCE.getDefaultBoardHeightMM(), UiUnits.MILLIMETERS);
        defaultPanelHeight.setCurrentUnit(UiUnits.FEET);
        defaultPanelHeight.getNumericInput().addPropertyChangeListener("value", evt -> {
            UIConfig.INSTANCE.setDefaultBoardHeightMM(defaultPanelHeight.getMMValue());
        });
        defaultPanelHeight.setMaximumSize(new Dimension(800, 50));
        add(defaultPanelHeight);

        JButton returnButton = new JButton("Revenir à l'application principale");
        returnButton.addActionListener(e -> mainWindow.showTrueMode());
        add(returnButton);
    }

}
