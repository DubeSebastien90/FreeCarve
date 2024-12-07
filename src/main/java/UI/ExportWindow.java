package UI;

import UI.Listeners.ExportGcodeActionListener;
import UI.SubWindows.BasicWindow;
import UI.SubWindows.Rendering3DWindow;
import UI.Widgets.Attributable;
import UI.Widgets.BigButton;
import UI.Widgets.GenericAttributeBox;
import UI.Widgets.PixelNoUnitInputField;

import javax.swing.*;
import java.awt.*;

/**
 * Represents an export window that displays a 3D renderer for visualizing
 * the final board, and provides an export button to convert the board into GCode instructions.
 *
 * @author Adam Côté
 * @version 1.0
 * @since 2024-10-25
 */
public class ExportWindow {

    private JSplitPane mainSplitPane;
    private JSplitPane splitPane1;
    private JSplitPane splitPane2;

    private CNCCutSpecChooser cncCutSpecChooser;
    private final BigButton nextButton = new BigButton("Export");
    private Rendering3DWindow rendering3DWindow;
    private final MainWindow mainWindow;
    private final JScrollPane gcodeWindow = new JScrollPane();
    private final BasicWindow realGcodeContainer = new BasicWindow(true);
    private final JTextArea gcodeDisplay = new JTextArea();


    /**
     * Constructs an ExportWindow and initializes its layout and components.
     */
    public ExportWindow(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        init();
        calculateGcode();
        setButtonAction();
    }

    public JSplitPane getMainSplitPane() {
        return mainSplitPane;
    }

    /**
     * Returns the renderer used in this export window.
     *
     * @return The Renderer instance responsible for displaying 3D shapes.
     */
    public Rendering3DWindow getRenderer() {
        return rendering3DWindow;
    }

    /**
     * Initializes the layout and adds components to the export window.
     * This method creates various 3D meshes and sets up the renderer with
     * those meshes.
     */
    public void init() {
        gcodeDisplay.setEditable(false);
        gcodeDisplay.setFont(new Font("Consolas", Font.PLAIN, 15));
        gcodeDisplay.setLineWrap(true);
        gcodeDisplay.setWrapStyleWord(true);
        gcodeDisplay.setMargin(new Insets(5, 5, 5, 5));
        gcodeDisplay.setBackground(null);
        gcodeDisplay.setBorder(null);

        realGcodeContainer.setLayout(new BorderLayout());
        realGcodeContainer.add(gcodeDisplay, BorderLayout.CENTER);

        gcodeWindow.add(realGcodeContainer);
        gcodeWindow.setViewportView(realGcodeContainer);
        gcodeWindow.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        gcodeWindow.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        gcodeWindow.setBorder(null);
        gcodeWindow.getViewport().setViewPosition(new Point(0, 0));

        rendering3DWindow = new Rendering3DWindow(mainWindow.getController().getCameraId(), mainWindow);

        cncCutSpecChooser = new CNCCutSpecChooser(mainWindow);

        splitPane1 = new JSplitPane(JSplitPane.VERTICAL_SPLIT, gcodeWindow, nextButton);
        splitPane2 = new JSplitPane(JSplitPane.VERTICAL_SPLIT, cncCutSpecChooser, splitPane1);
        mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, rendering3DWindow, splitPane2);
        splitPane2.setDividerLocation(UIConfig.INSTANCE.getDefaultWindowHeight() / 10*2);
        splitPane1.setDividerLocation(UIConfig.INSTANCE.getDefaultWindowHeight() / 2);
        mainSplitPane.setDividerLocation(UIConfig.INSTANCE.getDefaultWindowWidth() / 3 * 2);
        mainSplitPane.setResizeWeight(1);

    }

    public void calculateGcode() {
        String gcode = mainWindow.getController().convertToGCode();
        gcodeDisplay.setText(gcode);
        gcodeWindow.repaint();
        mainSplitPane.revalidate();
        mainSplitPane.repaint();
    }

    private void setButtonAction() {
        nextButton.getButton().addActionListener(new ExportGcodeActionListener(mainWindow));
    }


    public static class CNCCutSpecChooser extends GenericAttributeBox implements Attributable {

        private MainWindow mainWindow;
        private PixelNoUnitInputField rotationSpeed;
        private PixelNoUnitInputField feedRate;

        public CNCCutSpecChooser(MainWindow mainWindow) {
            super(false, "Spécifications CNC");
            this.mainWindow = mainWindow;
            init();
        }

        /**
         * Initializes the dimension setting UI with labels and input fields for width (x) and height (y),
         * with real-time resizing functionality.
         */
        private void init() {
            rotationSpeed = new PixelNoUnitInputField(mainWindow, "Vitesse de rotation", 1);
            feedRate = new PixelNoUnitInputField(mainWindow, "Vitesse de coupe", 1);
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.weightx = 1.0;
            gbc.weighty = 1.0;
            gbc.fill = GridBagConstraints.NONE;
            gbc.gridx = 0;
            gbc.gridy = 1;
            add(rotationSpeed, gbc);
            gbc.gridy = 2;
            add(feedRate, gbc);
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
}
