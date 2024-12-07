package UI;

import UI.Display2D.Rendering2DWindow;
import UI.Listeners.ExportGcodeActionListener;
import UI.SubWindows.*;
import UI.Widgets.BigButton;

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
    private JSplitPane cutInformationSplitPane;
    private JPanel panel2;
    private JPanel panel3;
    private JPanel bitPanel;
    private CutListPanel cutListPanel;
    private AttributePanel attributePanel;
    private Rendering2DWindow rendering2DWindow;
    private BitSelectionPanel bitSelectionPanel;


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

//        attributePanel = new AttributePanel(true, mainWindow);
//        panel2 = attributePanel;

//        cutListPanel = new CutListPanel(true, this, this, mainWindow);
//        panel3 = cutListPanel;

        splitPane1 = new JSplitPane(JSplitPane.VERTICAL_SPLIT, gcodeWindow, nextButton);
        mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, rendering3DWindow, splitPane1);

        //cutInformationSplitPane.setDividerLocation(UIConfig.INSTANCE.getDefaultWindowHeight() / 8);
        splitPane1.setDividerLocation(UIConfig.INSTANCE.getDefaultWindowHeight() / 10 * 7);
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


}
