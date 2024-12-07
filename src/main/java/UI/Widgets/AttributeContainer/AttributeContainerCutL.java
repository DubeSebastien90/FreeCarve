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
import java.util.List;
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
        return this.cutDTO.getPoints().getFirst().getX();
    }

    private double lEdgeEdgeY(){
        return this.cutDTO.getPoints().getFirst().getY();
    }

    private double lCenterCenterX(){
        List<VertexDTO> points = mainWindow.getController().getAbsolutePointsPosition(cutDTO);
        return points.get(1).getX();
    }

    private double lCenterCenterY(){
        List<VertexDTO> points = mainWindow.getController().getAbsolutePointsPosition(cutDTO);
        return points.get(1).getY();
    }

    private void init_attribute(MainWindow mainWindow, CutDTO cutDTO){
        super.init_attribute();
        offsetX = new SingleValueBox(mainWindow, true, "Largeur relative", "X",lEdgeEdgeX() );
        offsetY = new SingleValueBox(mainWindow, true, "Hauteur relative", "Y",lEdgeEdgeY() );
        absoluteOffsetX = new SingleValueBoxNotEditable(mainWindow, true, "Largeur absolue", "X",Math.abs(lEdgeEdgeX()));
        absoluteOffsetY = new SingleValueBoxNotEditable(mainWindow, true, "Hauteur absolute", "Y",Math.abs(lEdgeEdgeY()));
        offsetXCenterCenter = new SingleValueBoxNotEditable(mainWindow, true, "Position absolue (GCODE)", "X",lCenterCenterX());
        offsetYCenterCenter = new SingleValueBoxNotEditable(mainWindow, true, "Position absolue (GCODE)", "Y",lCenterCenterY());
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
        add(offsetY, gc);

        gc.gridx = 0;
        gc.gridy = 2;
        gc.insets = new Insets(0, 0, UIConfig.INSTANCE.getDefaultPadding() / 3, 0);
        add(absoluteOffsetX, gc);

        gc.gridx = 0;
        gc.gridy = 3;
        gc.insets = new Insets(0, 0, UIConfig.INSTANCE.getDefaultPadding() / 3, 0);
        add(absoluteOffsetY, gc);

        gc.gridx = 0;
        gc.gridy = 4;
        gc.insets = new Insets(0, 0, UIConfig.INSTANCE.getDefaultPadding() / 3, 0);
        add(offsetXCenterCenter, gc);

        gc.gridx = 0;
        gc.gridy = 5;
        gc.insets = new Insets(0, 0, UIConfig.INSTANCE.getDefaultPadding() / 3, 0);
        add(offsetYCenterCenter, gc);

        gc.gridx = 0;
        gc.gridy = 6;
        gc.insets = new Insets(0, 0, UIConfig.INSTANCE.getDefaultPadding() / 3, 0);
        add(depthBox, gc);

        gc.gridx = 0;
        gc.gridy = 7;
        gc.insets = new Insets(0, 0, UIConfig.INSTANCE.getDefaultPadding() / 3, 0);
        add(bitChoiceBox, gc);

        gc.gridx = 0;
        gc.gridy = 8;
        gc.insets = new Insets(0, 0, UIConfig.INSTANCE.getDefaultPadding() / 3, 0);
        add(modifyAnchorBox, gc);
    }

    @Override
    public void setupEventListeners() {
        addEventListenerWidthToEdgeEdge(offsetX);
        addEventListenerHeightToEdgeEdge(offsetY);

        addEventListenerToBitChoiceBox(bitChoiceBox);
        addEventListenerToDepth(depthBox);
        addEventListenerModifyAnchor(modifyAnchorBox);
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
        bitChoiceBox.refresh(mainWindow.getController().getConfiguredBitsMap(), cutDTO.getBitIndex());
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
                double edgeEdgeX = sb.getInput().getMMValue();
                for(int i =0; i < c.getPoints().size(); i++){
                    VertexDTO oldVertex = c.getPoints().get(i);
                    VertexDTO newVertex = new VertexDTO(edgeEdgeX, oldVertex.getY(), oldVertex.getZ());
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
                double edgeEdgeY = sb.getInput().getMMValue();
                for(int i =0; i < c.getPoints().size(); i++){
                    VertexDTO oldVertex = c.getPoints().get(i);
                    VertexDTO newVertex = new VertexDTO(oldVertex.getX(), edgeEdgeY, oldVertex.getZ());
                    c.getPoints().set(i, newVertex);
                }
                mainWindow.getController().modifyCut(c);
                cutListPanel.modifiedAttributeEventOccured(new ChangeAttributeEvent(cutBox, cutBox));
            }
        });
    }
}
