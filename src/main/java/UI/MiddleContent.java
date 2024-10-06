package UI;

import Buisness.Project;

import javax.swing.*;
import java.awt.*;

/**
 * The {@code MiddleContent} class encapsulates the UI container of the middle
 * section of the UI : it contains all of the sub-windows
 *
 * @author Louis-Etienne Messier
 * @version 0.1
 * @since 2024-09-21
 */
public class MiddleContent {
    private final Project.ProjectMiddleWindow thisState = Project.INSTANCE.getProjectMiddleWindow();
    private JPanel panel;
    private BorderLayout borderLayout;
    private CutWindow cutWindow;
    private FolderWindow projectWindow;

    public MiddleContent() {
        this.panel = new JPanel();
        this.borderLayout = new BorderLayout();
        this.init();
        this.panel.setBackground(Color.RED);
    }

    public JPanel getPanel() {
        return panel;
    }

    public void setPanel(JPanel panel) {
        this.panel = panel;
    }

    /**
     * Initiates all of the {@code MiddleContent} components
     */
    private void init() {
        cutWindow = new CutWindow();
        projectWindow = new FolderWindow();

        this.panel.setLayout(borderLayout);
        //panel.add(cutWindow.getCutWindow(), BorderLayout.CENTER);
        //panel.add(new Rendering2DWindow());
        //panel.add(projectWindow);
        panel.add(new ParamWindow());
        //panel.add(new SimulationWindow());
    }
}
