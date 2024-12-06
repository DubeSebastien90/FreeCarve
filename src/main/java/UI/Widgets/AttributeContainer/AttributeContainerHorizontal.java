package UI.Widgets.AttributeContainer;

import Common.DTO.CutDTO;
import Common.DTO.VertexDTO;
import UI.Events.ChangeAttributeEvent;
import UI.MainWindow;
import UI.SubWindows.CutListPanel;
import UI.UIConfig;
import UI.Widgets.*;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

public class AttributeContainerHorizontal extends AttributeContainer {
    SingleValueBox distanceFromEdgeToEdge;
    SingleValueBox absoluteDistanceFromEdgeToEdge;
    SingleValueBox distanceCenterToCenter;


    public AttributeContainerHorizontal(MainWindow mainWindow, CutListPanel cutListPanel, CutDTO cutDTO, CutBox cutBox) {
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
        add(distanceFromEdgeToEdge, gc);

        gc.gridx = 0;
        gc.gridy = 1;
        gc.insets = new Insets(0, 0, UIConfig.INSTANCE.getDefaultPadding() / 3, 0);
        add(absoluteDistanceFromEdgeToEdge, gc);

        gc.gridx = 0;
        gc.gridy = 2;
        gc.insets = new Insets(0, 0, UIConfig.INSTANCE.getDefaultPadding() / 3, 0);
        add(distanceCenterToCenter, gc);

        gc.gridx = 0;
        gc.gridy = 3;
        gc.insets = new Insets(0, 0, UIConfig.INSTANCE.getDefaultPadding() / 3, 0);
        add(depthBox, gc);

        gc.gridx = 0;
        gc.gridy = 4;
        gc.insets = new Insets(0, 0, UIConfig.INSTANCE.getDefaultPadding() / 3, 0);
        add(bitChoiceBox, gc);

        gc.gridx = 0;
        gc.gridy = 5;
        gc.insets = new Insets(0, 0, UIConfig.INSTANCE.getDefaultPadding() / 3, 0);
        add(modifyAnchorBox, gc);

    }

    private double yEdgeEdge(){
        return cutDTO.getPoints().getFirst().getY();
    }
    private double yCenterCenter(){
        List<VertexDTO> absPoints = mainWindow.getController().getAbsolutePointsPosition(cutDTO);
        return absPoints.getFirst().getY();
    }

    /**
     * Initialize variables relevant to the Attribute Panel
     */
    private void init_attribute(MainWindow mainWindow, CutDTO cutDTO) {
        super.init_attribute();
        distanceFromEdgeToEdge = new SingleValueBox(mainWindow,true, "Distance relative de la sous-pièce", "Y", yEdgeEdge() * UIConfig.INSTANCE.getDefaultUnit().getUnit().getInverseRatio(), UIConfig.INSTANCE.getDefaultUnit());
        absoluteDistanceFromEdgeToEdge = new SingleValueBoxNotEditable(mainWindow,true, "Taille de la sous-pièce", "Y", Math.abs(yEdgeEdge()) * UIConfig.INSTANCE.getDefaultUnit().getUnit().getInverseRatio(), UIConfig.INSTANCE.getDefaultUnit());
        distanceCenterToCenter = new SingleValueBoxNotEditable(mainWindow, true, "Position absolue (GCODE)", "Y", yCenterCenter() * UIConfig.INSTANCE.getDefaultUnit().getUnit().getInverseRatio(), UIConfig.INSTANCE.getDefaultUnit());
    }

    @Override
    public void setupEventListeners() {
        addEventListenerToEdgeEdge(distanceFromEdgeToEdge);
        addEventListenerToDepth(depthBox);
        addEventListenerToBitChoiceBox(bitChoiceBox);
        addEventListenerModifyAnchor(modifyAnchorBox);
    }

    @Override
    public void updatePanel(CutDTO newCutDTO) {
        cutDTO = newCutDTO;
        distanceFromEdgeToEdge.getInput().setValueInMMWithoutTrigerringListeners(yEdgeEdge());
        absoluteDistanceFromEdgeToEdge.getInput().setValueInMMWithoutTrigerringListeners(Math.abs(yEdgeEdge()));
        distanceCenterToCenter.getInput().setValueInMMWithoutTrigerringListeners( yCenterCenter());
        depthBox.getInput().setValueInMMWithoutTrigerringListeners(cutDTO.getDepth());
        revalidate();
        repaint();
    }

    /**
     * Adding the custom event listeners to SingleValueBox objects. The goal is to make
     * the Value attribute react to change events
     *
     * Changes the Y offset of the horizontal cut
     *
     * @param sb {@code SingleValueBox object}
     */
    private void addEventListenerToEdgeEdge(SingleValueBox sb) {
        sb.getInput().getNumericInput().addPropertyChangeListener("value", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                CutDTO c = new CutDTO(cutDTO);
                double newEdgeEdge = sb.getInput().getMMValue();
                for(int i =0; i < c.getPoints().size(); i++){
                    VertexDTO oldVertex = c.getPoints().get(i);
                    VertexDTO newVertex = new VertexDTO(oldVertex.getX(), newEdgeEdge, oldVertex.getZ());
                    c.getPoints().set(i, newVertex);
                }
                mainWindow.getController().modifyCut(c);
                cutListPanel.modifiedAttributeEventOccured(new ChangeAttributeEvent(cutBox, cutBox));
            }
        });
    }

}

