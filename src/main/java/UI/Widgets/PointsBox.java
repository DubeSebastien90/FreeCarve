package UI.Widgets;

import UI.SubWindows.BasicWindow;

import javax.swing.*;
import java.awt.*;

/**
 * The {@code PointsBox} class is a UI class that encapsulate the Points editor
 *
 * @author Louis-Etienne Messier
 * @version 0.1
 * @since 2024-10-23
 */
public class PointsBox extends BasicWindow {

    private BoxLayout layout;
    private JLabel name;
    private JLabel X;
    private JLabel Y;
    private JLabel Z;

    public PointsBox(boolean hasBackground){
        super(hasBackground);
        this.init();
        this.updatePoints();
    }

    private void init(){
        layout = new BoxLayout(this, BoxLayout.PAGE_AXIS);
        this.setLayout(layout);
        name = new JLabel("Points");
        this.add(name);
    }

    public void updatePoints(){

    }
}
