package UI;

import UI.Widgets.MeasurementInputField;
import UI.Widgets.PixelNoUnitInputField;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class OptionWindow extends JPanel {

    public OptionWindow(MainWindow mainWindow) {
        init(mainWindow);
    }

    private void init(MainWindow mainWindow) {
        // TODO implement undo redo for settings
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(20, 20, 20, 20));
        setBackground(null);

        JLabel titleLabel = new JLabel("Panneau d'Options");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(titleLabel);

        add(Box.createRigidArea(new Dimension(0, 20)));
//
//        JPanel dimensionsPanel = createGroupPanel("Dimensions par défaut");
//        add(dimensionsPanel);
//
//        MeasurementInputField defaultPanelWidth = new MeasurementInputField(mainWindow, "Largeur par défaut", UIConfig.INSTANCE.getDefaultBoardWidthMM(), UiUnits.MILLIMETERS);
//        configureMeasurementField(defaultPanelWidth, evt -> UIConfig.INSTANCE.setDefaultBoardWidthMM(defaultPanelWidth.getMMValue()));
//        dimensionsPanel.add(defaultPanelWidth);
//
//        MeasurementInputField defaultPanelHeight = new MeasurementInputField(mainWindow, "Hauteur par défaut", UIConfig.INSTANCE.getDefaultBoardHeightMM(), UiUnits.MILLIMETERS);
//        configureMeasurementField(defaultPanelHeight, evt -> UIConfig.INSTANCE.setDefaultBoardHeightMM(defaultPanelHeight.getMMValue()));
//        dimensionsPanel.add(defaultPanelHeight);
//
//        add(Box.createRigidArea(new Dimension(0, 20)));

        JPanel unitPanel = createGroupPanel("Unités");
        add(unitPanel);

        JComboBox<UiUnits> unit = new JComboBox<>(UiUnits.values());
        unit.setSelectedItem(UIConfig.INSTANCE.getDefaultUnit());
        unit.addItemListener(event -> UIConfig.INSTANCE.setDefaultUnit((UiUnits) event.getItem()));
        unit.setMaximumSize(new Dimension(400, 40));
        unit.setFont(new Font("Arial", Font.PLAIN, 14));
        unitPanel.add(unit);

        add(Box.createRigidArea(new Dimension(0, 20)));


        JPanel cncPanel = createGroupPanel("Paramètres CNC");
        add(cncPanel);

        PixelNoUnitInputField gcodeRotationRate = new PixelNoUnitInputField(mainWindow, "Rotation outil CNC", mainWindow.getController().getCNCrotationSpeed(), "rotation per second");
        configurePixelField(gcodeRotationRate, evt -> {
            mainWindow.getController().setCNCrotationSpeed(((Number) evt.getNewValue()).intValue());
            mainWindow.getMiddleContent().getExportWindow().refreshGcodeParam();
        });
        cncPanel.add(gcodeRotationRate);

        PixelNoUnitInputField gcodeFeedRate = new PixelNoUnitInputField(mainWindow, "Vitesse coupe CNC", mainWindow.getController().getCNCCuttingSpeed(), "m/min");
        configurePixelField(gcodeFeedRate, evt -> {
            mainWindow.getController().setCNCCuttingSpeed(((Number) evt.getNewValue()).intValue());
            mainWindow.getMiddleContent().getExportWindow().refreshGcodeParam();
        });
        cncPanel.add(gcodeFeedRate);

        add(Box.createRigidArea(new Dimension(0, 20)));


        JButton returnButton = new JButton("Revenir à l'application principale");
        returnButton.setFont(new Font("Arial", Font.BOLD, 14));
        returnButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        returnButton.addActionListener(e -> mainWindow.showTrueMode());
        add(returnButton);
    }

    private JPanel createGroupPanel(String title) {
        JPanel groupPanel = new JPanel();
        groupPanel.setLayout(new BoxLayout(groupPanel, BoxLayout.Y_AXIS));
        groupPanel.setBorder(BorderFactory.createTitledBorder(title));
        groupPanel.setBackground(null);
        groupPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        return groupPanel;
    }

    private void configureMeasurementField(MeasurementInputField field, java.beans.PropertyChangeListener listener) {
        field.setCurrentUnit(UiUnits.FEET);
        field.getNumericInput().addPropertyChangeListener("value", listener);
        field.setMaximumSize(new Dimension(400, 50));
    }

    private void configurePixelField(PixelNoUnitInputField field, java.beans.PropertyChangeListener listener) {
        field.getNumericInput().addPropertyChangeListener("value", listener);
        field.setMaximumSize(new Dimension(400, 50));
    }
}
