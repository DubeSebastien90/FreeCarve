package UI;

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
    private JPanel panel;
    private CutWindow cutWindow;

    public MiddleContent(){
        this.init();
        panel.setBackground(Color.RED);
    }

    /**
     * @return the panel container of the {@code MiddleContent}
     */
    public JPanel getMiddleContent(){
        return panel;
    }

    /**
     * Initiates all of the {@code MiddleContent} components
     */
    private void init(){
        panel = new JPanel();
        cutWindow = new CutWindow();

        panel.add(cutWindow.getCutWindow(), BorderLayout.CENTER);
    }
}
