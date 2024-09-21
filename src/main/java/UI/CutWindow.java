package UI;

import javax.swing.*;
import java.awt.*;

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

    /**
     * Constructs a {@code CutWindow} instance initializing all of it's sub-panels
     * and sub-components
     */
    public CutWindow(){
        this.init();
    }

    /**
     * @return the {@code JSplitPane} container of the {@code CutWindow}
     */
    public JSplitPane getCutWindow(){
        return mainSplitPane;
    }

    /**
     * Initiates all of the {@code CutWindow} components
     */
    private void init(){
        panel1 = new JPanel();
        panel1.setBackground(Color.BLUE);
        panel2 = new JPanel();
        panel2.setBackground(Color.GREEN);
        panel3 = new JPanel();
        panel3.setBackground(Color.YELLOW);
        panel4 = new JPanel();
        panel4.setBackground(Color.CYAN);
        splitPane1 = new JSplitPane(JSplitPane.VERTICAL_SPLIT, panel1, panel2);
        splitPane2 = new JSplitPane(JSplitPane.VERTICAL_SPLIT, panel3, panel4);
        mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, splitPane1, splitPane2);
    }
}
