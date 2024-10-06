package UI.SubWindows;

import UI.Widgets.CutBox;
import Util.UiUtil;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * The {@code CutList} class is a UI class that encapsulates the list of the cuts sub-window
 *
 * @author Louis-Etienne Messier
 * @version 0.1
 * @since 2024-09-21
 */
public class CutList {
    private JPanel panel;
    private BorderLayout borderLayout;
    private ArrayList<CutBox> cutBoxes;

    /**
     * Constructs a {@code CutList} by initializing all of it's attributes
     */
    public CutList() {
        this.init();
    }

    /**
     * @return the {@code JPanel} container of the {@code CutList}
     */
    public JPanel getCutList() {
        return this.panel;
    }

    /**
     * Initiates all of the {@code CutList} components
     */
    private void init() {
        panel = new JPanel();
        borderLayout = new BorderLayout();
        cutBoxes = new ArrayList<CutBox>();
    }
}
