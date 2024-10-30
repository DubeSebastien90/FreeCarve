package UI.Widgets;

import Domain.ThirdDimension.VertexDTO;
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
public class PointsBox extends GenericAttributeBox {

    private CustomNumericInputField xInput;
    private CustomNumericInputField yInput;


    public PointsBox(boolean hasBackground, String name, VertexDTO vertexDTO){
        super(hasBackground, name);
        this.init(vertexDTO);
    }

    private void init(VertexDTO vertexDTO){
        GridBagConstraints gc = new GridBagConstraints();

        this.xInput = new CustomNumericInputField("X", vertexDTO.getX());
        this.yInput = new CustomNumericInputField("Y", vertexDTO.getY());

        gc.gridx = 0; gc.gridy = 1;
        gc.fill = GridBagConstraints.NONE;
        gc.weightx = 1;
        this.add(xInput, gc);
        gc.gridx = 0; gc.gridy = 2;
        gc.fill = GridBagConstraints.NONE;
        gc.weightx = 1;
        this.add(yInput, gc);
    }

    public CustomNumericInputField getxInput(){return this.xInput;}
    public CustomNumericInputField getyInput(){return this.yInput;}
}
