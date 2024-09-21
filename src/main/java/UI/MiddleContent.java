package UI;

import javax.swing.*;
import java.awt.*;

/**
 * The {@code MiddleContent} class encapsulates the UI container of the middle
 * section of the UI : it contains all of the sub-windows
 */
public class MiddleContent {
    private JPanel panel;

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

    }



}
