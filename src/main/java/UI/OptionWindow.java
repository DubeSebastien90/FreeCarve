package UI;

import UI.Widgets.MeasurementInputField;
import UI.Widgets.PixelNoUnitInputField;

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
        defaultPanelWidth.setMaximumSize(new Dimension(1200, 50));
        add(defaultPanelWidth);

        MeasurementInputField defaultPanelHeight = new MeasurementInputField(mainWindow, "Hauteur par défaut", UIConfig.INSTANCE.getDefaultBoardHeightMM(), UiUnits.MILLIMETERS);
        defaultPanelHeight.setCurrentUnit(UiUnits.FEET);
        defaultPanelHeight.getNumericInput().addPropertyChangeListener("value", evt -> {
            UIConfig.INSTANCE.setDefaultBoardHeightMM(defaultPanelHeight.getMMValue());
        });
        defaultPanelHeight.setMaximumSize(new Dimension(1200, 50));
        add(defaultPanelHeight);

        JComboBox<UiUnits> unit = new JComboBox<>(UiUnits.values());
        unit.setSelectedItem(UIConfig.INSTANCE.getDefaultUnit());
        unit.addItemListener(event -> {
            UIConfig.INSTANCE.setDefaultUnit((UiUnits) event.getItem());
            System.out.println(UIConfig.INSTANCE.getDefaultUnit());
        });
        unit.setMaximumSize(new Dimension(1200, 50));
        add(unit);


        PixelNoUnitInputField gcodeRotationRate = new PixelNoUnitInputField(mainWindow, "Rotation outil CNC", mainWindow.getController().getCNCrotationSpeed(), "rotation per second");
        gcodeRotationRate.getNumericInput().addPropertyChangeListener("value", evt -> {
            mainWindow.getController().setCNCrotationSpeed(((Number) evt.getNewValue()).intValue());

        });
        gcodeRotationRate.setMaximumSize(new Dimension(1200, 50));
        add(gcodeRotationRate);

        PixelNoUnitInputField gcodeFeedRate = new PixelNoUnitInputField(mainWindow, "Vitesse coupe CNC", mainWindow.getController().getCNCCuttingSpeed(), "mm per second");
        gcodeFeedRate.getNumericInput().addPropertyChangeListener("value", evt -> {
            mainWindow.getController().setCNCCuttingSpeed(((Number) evt.getNewValue()).intValue());
        });
        gcodeFeedRate.setMaximumSize(new Dimension(1200, 50));
        add(gcodeFeedRate);


        JButton returnButton = new JButton("Revenir à l'application principale");
        returnButton.addActionListener(e -> mainWindow.showTrueMode());
        add(returnButton);
    }

}
