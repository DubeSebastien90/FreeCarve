package UI.Widgets.AttributeContainer;

import Common.DTO.CutDTO;
import Common.DTO.VertexDTO;
import UI.Events.ChangeAttributeEvent;
import UI.MainWindow;
import UI.SubWindows.CutListPanel;
import UI.UIConfig;
import UI.Widgets.CutBox;
import UI.Widgets.SingleValueBox;
import UI.Widgets.SingleValueBoxNotEditable;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class AttributeContainerCutL extends AttributeContainer{

    SingleValueBox offsetX;
    SingleValueBox absoluteOffsetX;
    SingleValueBox offsetXCenterCenter;
    SingleValueBox offsetY;
    SingleValueBox absoluteOffsetY;
    SingleValueBox offsetYCenterCenter;

    public AttributeContainerCutL(MainWindow mainWindow, CutListPanel cutListPanel, CutDTO cutDTO, CutBox cutBox) {
        super(mainWindow, cutListPanel, cutDTO, cutBox);
        init_attribute(mainWindow, cutDTO);
        init_layout();
    }

    private double lEdgeEdgeX(){
        return centerCenterToEdgeEdge(lCenterCenterX());
    }

    private double lEdgeEdgeY(){
        return centerCenterToEdgeEdge(lCenterCenterY());
    }

    private double lCenterCenterX(){
        return this.cutDTO.getPoints().getFirst().getX();
    }

    private double lCenterCenterY(){
        return this.cutDTO.getPoints().getFirst().getY();
    }

    private void init_attribute(MainWindow mainWindow, CutDTO cutDTO){
        super.init_attribute();
        offsetX = new SingleValueBox(mainWindow, true, "Largeur relative", "X",lEdgeEdgeX(), UIConfig.INSTANCE.getDefaultUnit());
        offsetY = new SingleValueBox(mainWindow, true, "Hauteur relative", "Y",lEdgeEdgeY(), UIConfig.INSTANCE.getDefaultUnit());
        absoluteOffsetX = new SingleValueBoxNotEditable(mainWindow, true, "Largeur absolue", "X",Math.abs(lEdgeEdgeX()), UIConfig.INSTANCE.getDefaultUnit());
        absoluteOffsetY = new SingleValueBoxNotEditable(mainWindow, true, "Hauteur absolute", "Y",Math.abs(lEdgeEdgeY()), UIConfig.INSTANCE.getDefaultUnit());
        offsetXCenterCenter = new SingleValueBoxNotEditable(mainWindow, true, "Distances centrales (GCODE)", "X",lCenterCenterX(), UIConfig.INSTANCE.getDefaultUnit());
        offsetYCenterCenter = new SingleValueBoxNotEditable(mainWindow, true, "Distances centrales (GCODE)", "Y",lCenterCenterY(), UIConfig.INSTANCE.getDefaultUnit());
    }

    private void init_layout(){
        setBackground(null);
        setOpaque(false);
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints gc = new GridBagConstraints();
        setLayout(layout);
        gc.gridx = 0;
        gc.gridy = 0;
        gc.weightx = 1;
        gc.weighty = 1;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.insets = new Insets(0, 0, UIConfig.INSTANCE.getDefaultPadding() / 3, 0);
        add(offsetX, gc);

        gc.gridx = 0;
        gc.gridy = 1;
        gc.insets = new Insets(0, 0, UIConfig.INSTANCE.getDefaultPadding() / 3, 0);
        add(absoluteOffsetX, gc);

        gc.gridx = 0;
        gc.gridy = 2;
        gc.insets = new Insets(0, 0, UIConfig.INSTANCE.getDefaultPadding() / 3, 0);
        add(offsetXCenterCenter, gc);

        gc.gridx = 0;
        gc.gridy = 3;
        gc.insets = new Insets(0, 0, UIConfig.INSTANCE.getDefaultPadding() / 3, 0);
        add(offsetY, gc);

        gc.gridx = 0;
        gc.gridy = 4;
        gc.insets = new Insets(0, 0, UIConfig.INSTANCE.getDefaultPadding() / 3, 0);
        add(absoluteOffsetY, gc);

        gc.gridx = 0;
        gc.gridy = 5;
        gc.insets = new Insets(0, 0, UIConfig.INSTANCE.getDefaultPadding() / 3, 0);
        add(offsetYCenterCenter, gc);
    }

    @Override
    public void setupEventListeners() {
        addEventListenerWidthToEdgeEdge(offsetX);
        addEventListenerHeightToEdgeEdge(offsetY);

        addEventListenerToBitChoiceBox(bitChoiceBox);
        addEventListenerToDepth(depthBox);
    }

    @Override
    public void updatePanel(CutDTO newCutDTO) {
        cutDTO = newCutDTO;

        offsetX.getInput().setValueInMMWithoutTrigerringListeners(lEdgeEdgeX());
        absoluteOffsetX.getInput().setValueInMMWithoutTrigerringListeners(Math.abs(lEdgeEdgeX()));
        offsetXCenterCenter.getInput().setValueInMMWithoutTrigerringListeners( lCenterCenterX());

        offsetY.getInput().setValueInMMWithoutTrigerringListeners(lEdgeEdgeY());
        absoluteOffsetY.getInput().setValueInMMWithoutTrigerringListeners(Math.abs(lEdgeEdgeY()));
        offsetYCenterCenter.getInput().setValueInMMWithoutTrigerringListeners( lCenterCenterY());

        depthBox.getInput().setValueInMMWithoutTrigerringListeners(cutDTO.getDepth());
        revalidate();
        repaint();
    }

    /**
     * Adding the custom event listeners to SingleValueBox objects. The goal is to make
     * the Value attribute react to change events
     *
     * Changes the width
     *
     * @param sb {@code SingleValueBox object}
     */
    private void addEventListenerWidthToEdgeEdge(SingleValueBox sb) {
        sb.getInput().getNumericInput().addPropertyChangeListener("value", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                CutDTO c = new CutDTO(cutDTO);
                double centerCenterN = edgeEdgeToCenterCenter(sb.getInput().getMMValue());
                for(int i =0; i < c.getPoints().size(); i++){
                    VertexDTO oldVertex = c.getPoints().get(i);
                    VertexDTO newVertex = new VertexDTO(centerCenterN, oldVertex.getY(), oldVertex.getZ());
                    c.getPoints().set(i, newVertex);
                }
                mainWindow.getController().modifyCut(c);
                cutListPanel.modifiedAttributeEventOccured(new ChangeAttributeEvent(cutBox, cutBox));
            }
        });
    }

    /**
     * Adding the custom event listeners to SingleValueBox objects. The goal is to make
     * the Value attribute react to change events
     *
     * Changes the height
     *
     * @param sb {@code SingleValueBox object}
     */
    private void addEventListenerHeightToEdgeEdge(SingleValueBox sb) {
        sb.getInput().getNumericInput().addPropertyChangeListener("value", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                CutDTO c = new CutDTO(cutDTO);
                double centerCenterN = edgeEdgeToCenterCenter(sb.getInput().getMMValue());
                for(int i =0; i < c.getPoints().size(); i++){
                    VertexDTO oldVertex = c.getPoints().get(i);
                    VertexDTO newVertex = new VertexDTO(oldVertex.getX(), centerCenterN, oldVertex.getZ());
                    c.getPoints().set(i, newVertex);
                }
                mainWindow.getController().modifyCut(c);
                cutListPanel.modifiedAttributeEventOccured(new ChangeAttributeEvent(cutBox, cutBox));
            }
        });
    }
}
