package UI;

import UI.Events.ChangeAttributeEvent;
import UI.Widgets.MeasurementInputField;
import UI.Widgets.PixelNoUnitInputField;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.*;


public class OptionWindow extends JPanel {

    OptionWrapper optionWrapper;

    public OptionWindow(MainWindow mainWindow) {
        try {
            this.optionWrapper = loadOptions(mainWindow);

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        init(mainWindow);
    }

    private void init(MainWindow mainWindow) {
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
        unit.addItemListener(event -> {
            UIConfig.INSTANCE.setDefaultUnit((UiUnits) event.getItem());
            optionWrapper.setUnits((UiUnits) event.getItem());
        });
        unit.setMaximumSize(new Dimension(400, 40));
        unit.setFont(new Font("Arial", Font.PLAIN, 14));
        unitPanel.add(unit);

        add(Box.createRigidArea(new Dimension(0, 20)));


        JPanel cncPanel = createGroupPanel("Paramètres CNC");
        add(cncPanel);

        PixelNoUnitInputField gcodeRotationRate = new PixelNoUnitInputField(mainWindow, "Rotation outil CNC", mainWindow.getController().getCNCrotationSpeed(), "rotation per second");
        configurePixelField(gcodeRotationRate, evt -> {
            mainWindow.getController().setCNCrotationSpeed(((Number) evt.getNewValue()).intValue());
            optionWrapper.setRotation(((Number) evt.getNewValue()).intValue());
            mainWindow.getMiddleContent().getExportWindow().refreshGcodeParam();
        });
        cncPanel.add(gcodeRotationRate);

        PixelNoUnitInputField gcodeFeedRate = new PixelNoUnitInputField(mainWindow, "Vitesse coupe CNC", mainWindow.getController().getCNCCuttingSpeed(), "m/min");
        configurePixelField(gcodeFeedRate, evt -> {
            mainWindow.getController().setCNCCuttingSpeed(((Number) evt.getNewValue()).intValue());
            optionWrapper.setVitesse(((Number) evt.getNewValue()).intValue());
            mainWindow.getMiddleContent().getExportWindow().refreshGcodeParam();
        });
        cncPanel.add(gcodeFeedRate);

        add(Box.createRigidArea(new Dimension(0, 20)));


        JButton returnButton = new JButton("Sauvegarder et revenir à l'application principale");
        returnButton.setFont(new Font("Arial", Font.BOLD, 14));
        returnButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        returnButton.addActionListener(e -> {
            try {
                sauvegarderOptions();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            mainWindow.showTrueMode();
        });
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

    public static OptionWrapper loadOptions(MainWindow mainWindow) throws IOException, ClassNotFoundException {
        String filePath = "./.settingsOptions.fcv";
        File f = new File(filePath);
        if (f.exists()) {
            try {
                ObjectInputStream in = new ObjectInputStream(new FileInputStream(f));
                Object object = in.readObject();
                in.close();
                setOptionWrapper((OptionWrapper) object, mainWindow);
                return (OptionWrapper) object;
            } catch (Exception ex) {
                return new OptionWrapper();
            }
        } else {
            return new OptionWrapper();
        }
    }

    private void sauvegarderOptions() throws IOException {
        String filePath = "./.settingsOptions.fcv";
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filePath));
        out.writeObject(this.optionWrapper);
        out.close();
    }

    private static void setOptionWrapper(OptionWrapper optionWrapper, MainWindow mainWindow) {
        UIConfig.INSTANCE.setDefaultUnit(optionWrapper.getUnits());
        mainWindow.getController().setCNCrotationSpeed(optionWrapper.getRotation());
        mainWindow.getController().setCNCCuttingSpeed(optionWrapper.getVitesse());
        mainWindow.getMiddleContent().getExportWindow().refreshGcodeParam();

        mainWindow.getMiddleContent().getConfigChoiceWindow().getAttributePanel().update();
    }
}


