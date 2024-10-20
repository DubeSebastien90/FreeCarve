package UI;

import Domain.ThirdDimension.Mesh;
import Domain.ThirdDimension.Plane;
import Domain.ThirdDimension.Pyramid;
import Domain.ThirdDimension.Vertex;
import UI.SubWindows.BasicWindow;
import UI.SubWindows.CutList;
import UI.SubWindows.Rendering2DWindow;
import UI.Widgets.ChooseDimension;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The {@code CutWindow} class encapsulates the sub-windows necessary for using the
 * CutEdition Menu
 *
 * @author Louis-Etienne Messier
 * @version 0.1
 * @since 2024-09-21
 */
public class CutWindow {
    private JSplitPane mainSplitPane;
    private JSplitPane splitPane1;
    private JSplitPane splitPane2;
    private JPanel panel1;
    private JPanel panel2;
    private JPanel panel3;
    private JPanel panel4;
    private CutList cutList;

    /**
     * Constructs a {@code CutWindow} instance initializing all of it's sub-panels
     * and sub-components
     */
    public CutWindow() {
        this.init();
    }

    /**
     * @return the {@code JSplitPane} container of the {@code CutWindow}
     */
    public JSplitPane getCutWindow() {
        return mainSplitPane;
    }

    public JPanel getScreen(int i) {
        switch (i) {
            case 1 -> {
                return panel1;
            }
        }
        return null;
    }

    /**
     * Initiates all of the {@code CutWindow} components
     */
    private void init() {
        panel1 = new Rendering2DWindow();


        BasicWindow attribute = new BasicWindow(true);
        attribute.add(new ChooseDimension());
        panel2 = attribute;

        panel3 = new JPanel();

        CutList cutList = new CutList(true);
        panel4 = cutList;

        splitPane1 = new JSplitPane(JSplitPane.VERTICAL_SPLIT, panel1, panel2);
        splitPane2 = new JSplitPane(JSplitPane.VERTICAL_SPLIT, panel3, panel4);
        mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, splitPane1, splitPane2);

        splitPane1.setDividerLocation(UIConfig.INSTANCE.getDefaultWindowHeight() / 2);
        splitPane2.setDividerLocation(UIConfig.INSTANCE.getDefaultWindowHeight() / 2);
        mainSplitPane.setDividerLocation(UIConfig.INSTANCE.getDefaultWindowWidth() / 2);
    }
}
