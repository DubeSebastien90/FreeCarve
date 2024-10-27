package UI.Widgets;

import Domain.DTO.VertexDTO;
import Domain.ThirdDimension.Vertex;
import UI.SubWindows.BasicWindow;
import UI.UIConfig;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * The {@code PointsBox} class is a UI class that encapsulate the Points editor
 *
 * @author Louis-Etienne Messier
 * @version 0.1
 * @since 2024-10-23
 */
public class PointsBox extends BasicWindow {

    private GridBagLayout layout;
    private JLabel name;
    private CustomNumericInputField xInput;
    private CustomNumericInputField yInput;

    public PointsBox(boolean hasBackground, String name, VertexDTO vertexDTO){
        super(hasBackground);
        this.init(name, vertexDTO);
        this.updatePoints();
    }

    private void init(String name, VertexDTO vertexDTO){
        layout = new GridBagLayout();
        GridBagConstraints gc = new GridBagConstraints();
        this.setLayout(layout);
        this.setBorder(new EmptyBorder(UIConfig.INSTANCE.getDefaultPadding(), UIConfig.INSTANCE.getDefaultPadding(),
                UIConfig.INSTANCE.getDefaultPadding(), UIConfig.INSTANCE.getDefaultPadding()));
        this.setBackground(UIManager.getColor("SubWindow.lightBackground1"));
        this.name = new JLabel(name);
        this.xInput = new CustomNumericInputField("X", vertexDTO.getX());
        this.yInput = new CustomNumericInputField("Y", vertexDTO.getY());

        gc.gridx = 0; gc.gridy = 0;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.weightx = 1;
        this.add(this.name, gc);
        gc.gridx = 0; gc.gridy = 1;
        gc.fill = GridBagConstraints.NONE;
        gc.weightx = 1;
        this.add(xInput, gc);
        gc.gridx = 0; gc.gridy = 2;
        gc.fill = GridBagConstraints.NONE;
        gc.weightx = 1;
        this.add(yInput, gc);
    }

    public void updatePoints(){

    }
}
