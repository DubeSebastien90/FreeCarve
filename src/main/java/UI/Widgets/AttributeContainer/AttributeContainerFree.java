package UI.Widgets.AttributeContainer;


import Common.DTO.CutDTO;
import Common.DTO.VertexDTO;
import UI.Events.ChangeAttributeEvent;
import UI.MainWindow;
import UI.SubWindows.CutListPanel;
import UI.UIConfig;
import UI.Widgets.CutBox;
import UI.Widgets.PointsBox;
import UI.Widgets.SingleValueBox;
import UI.Widgets.SingleValueBoxNotEditable;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

public class AttributeContainerFree extends AttributeContainer {
    PointsBox offsetOfFirstPoint;
    PointsBox offsetOfSecondPoint;


    public AttributeContainerFree(MainWindow mainWindow, CutListPanel cutListPanel, CutDTO cutDTO, CutBox cutBox) {
        super(mainWindow, cutListPanel, cutDTO, cutBox);
        init_attribute(mainWindow, cutDTO);
        init_layout();
    }

    /**
     * Initializes all the components of the Attribute Container
     */
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
        add(offsetOfFirstPoint, gc);

        gc.gridx = 0;
        gc.gridy = 1;
        gc.insets = new Insets(0, 0, UIConfig.INSTANCE.getDefaultPadding() / 3, 0);
        add(offsetOfSecondPoint, gc);


        gc.gridx = 0;
        gc.gridy = 2;
        gc.insets = new Insets(0, 0, UIConfig.INSTANCE.getDefaultPadding() / 3, 0);
        add(depthBox, gc);

        gc.gridx = 0;
        gc.gridy = 3;
        gc.insets = new Insets(0, 0, UIConfig.INSTANCE.getDefaultPadding() / 3, 0);
        add(bitChoiceBox, gc);

        gc.gridx = 0;
        gc.gridy = 4;
        gc.insets = new Insets(0, 0, UIConfig.INSTANCE.getDefaultPadding() / 3, 0);
        add(modifyAnchorBox, gc);

    }

    private VertexDTO edgeEdgeFirstPoint(){
        return this.cutDTO.getPoints().getFirst();
    }

    private VertexDTO edgeEdgeSecondPoint(){
        return this.cutDTO.getPoints().get(1);
    }


    /**
     * Initialize variables relevant to the Attribute Panel
     */
    private void init_attribute(MainWindow mainWindow, CutDTO cutDTO) {
        super.init_attribute();
        offsetOfFirstPoint = new PointsBox(mainWindow, true, "Position relative du 1er point", edgeEdgeFirstPoint());
        offsetOfSecondPoint = new PointsBox(mainWindow, true, "Position relative du 2e point", edgeEdgeSecondPoint());
    }

    @Override
    public void setupEventListeners() {
        addEventListenerToMovePoint(offsetOfFirstPoint, 0);
        addEventListenerToMovePoint(offsetOfSecondPoint, 1);
        addEventListenerToDepth(depthBox);
        addEventListenerToBitChoiceBox(bitChoiceBox);
        addEventListenerModifyAnchor(modifyAnchorBox);
    }

    @Override
    public void updatePanel(CutDTO newCutDTO) {
        cutDTO = newCutDTO;
        offsetOfFirstPoint.getxInput().setValueInMMWithoutTrigerringListeners(edgeEdgeFirstPoint().getX());
        offsetOfFirstPoint.getyInput().setValueInMMWithoutTrigerringListeners(edgeEdgeFirstPoint().getY());
        offsetOfSecondPoint.getxInput().setValueInMMWithoutTrigerringListeners(edgeEdgeSecondPoint().getX());
        offsetOfSecondPoint.getyInput().setValueInMMWithoutTrigerringListeners(edgeEdgeSecondPoint().getY());
        depthBox.getInput().setValueInMMWithoutTrigerringListeners(cutDTO.getDepth());
        revalidate();
        repaint();
    }

    private void addEventListenerToMovePoint(PointsBox pb, int index) {
        pb.getxInput().getNumericInput().addPropertyChangeListener("value", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                CutDTO c = new CutDTO(cutDTO);
                VertexDTO oldVertex = c.getPoints().get(index);
                VertexDTO newPoint = new VertexDTO(pb.getxInput().getMMValue(), oldVertex.getY(), oldVertex.getZ());
                c.getPoints().set(index, newPoint);
                mainWindow.getController().modifyCut(c);
                cutListPanel.modifiedAttributeEventOccured(new ChangeAttributeEvent(cutBox, cutBox));
            }
        });

        pb.getyInput().getNumericInput().addPropertyChangeListener("value", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                CutDTO c = new CutDTO(cutDTO);
                VertexDTO oldVertex = c.getPoints().get(index);
                VertexDTO newPoint = new VertexDTO(oldVertex.getX(), pb.getyInput().getMMValue(), oldVertex.getZ());
                c.getPoints().set(index, newPoint);
                mainWindow.getController().modifyCut(c);
                cutListPanel.modifiedAttributeEventOccured(new ChangeAttributeEvent(cutBox, cutBox));
            }
        });
    }

}

