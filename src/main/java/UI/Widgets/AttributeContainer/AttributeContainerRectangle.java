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

public class AttributeContainerRectangle extends AttributeContainer{

    PointsBox offsetOfRectangle;
    SingleValueBox widthOfRectangle;
    SingleValueBox widthOfRectangleCenterCenter;
    SingleValueBox heightOfRectangle;
    SingleValueBox heightOfRectangleCenterCenter;

    public AttributeContainerRectangle(MainWindow mainWindow, CutListPanel cutListPanel, CutDTO cutDTO, CutBox cutBox) {
        super(mainWindow, cutListPanel, cutDTO, cutBox);
        init_attribute(mainWindow, cutDTO);
        init_layout();
    }

    private void init_attribute(MainWindow mainWindow, CutDTO cutDTO){
        super.init_attribute();
        offsetOfRectangle = new PointsBox(mainWindow, true, "Distance relative du rectangle", getAnchorCenterPoint());
        widthOfRectangle = new SingleValueBox(mainWindow, true, "Largeur interne", "X", getWidthEdgeEdge(), UIConfig.INSTANCE.getDefaultUnit());
        heightOfRectangle = new SingleValueBox(mainWindow, true, "Hauteur interne", "Y", getHeightEdgeEdge(), UIConfig.INSTANCE.getDefaultUnit());
        widthOfRectangleCenterCenter = new SingleValueBoxNotEditable(mainWindow, true, "Largeur centrale (GCODE)", "X", getWidthCenterCenter(), UIConfig.INSTANCE.getDefaultUnit());
        heightOfRectangleCenterCenter = new SingleValueBoxNotEditable(mainWindow, true, "Hauteur centrale (GCODE)", "Y", getHeightCenterCenter(), UIConfig.INSTANCE.getDefaultUnit());
    }
    private VertexDTO getAnchorCenterPoint(){
        VertexDTO p1 = cutDTO.getPoints().getFirst();
        VertexDTO p3 = cutDTO.getPoints().get(2);
        VertexDTO centerAnchorPoint = p3.sub(p1).mul(0.5).add(p1);
        return centerAnchorPoint;
    }

    private double getWidthEdgeEdge(){
        VertexDTO p1 = cutDTO.getPoints().getFirst();
        VertexDTO p3 = cutDTO.getPoints().get(2);
        VertexDTO diff = p3.sub(p1);
        return diff.getX();
    }

    private double getHeightEdgeEdge(){
        VertexDTO p1 = cutDTO.getPoints().getFirst();
        VertexDTO p3 = cutDTO.getPoints().get(2);
        VertexDTO diff = p3.sub(p1);
        return diff.getY();
    }

    private double getWidthCenterCenter(){
        List<VertexDTO> absPoints = mainWindow.getController().getAbsolutePointsPosition(cutDTO);
        VertexDTO p1 = absPoints.getFirst();
        VertexDTO p3 = absPoints.get(2);
        VertexDTO diff = p3.sub(p1);
        return diff.getX();
    }

    private double getHeightCenterCenter(){
        List<VertexDTO> absPoints = mainWindow.getController().getAbsolutePointsPosition(cutDTO);
        VertexDTO p1 = absPoints.getFirst();
        VertexDTO p3 = absPoints.get(2);
        VertexDTO diff = p3.sub(p1);
        return diff.getY();
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
        add(offsetOfRectangle, gc);

        gc.gridx = 0;
        gc.gridy = 1;
        gc.insets = new Insets(0, 0, UIConfig.INSTANCE.getDefaultPadding() / 3, 0);
        add(widthOfRectangle, gc);

        gc.gridx = 0;
        gc.gridy = 2;
        gc.insets = new Insets(0, 0, UIConfig.INSTANCE.getDefaultPadding() / 3, 0);
        add(heightOfRectangle, gc);

        gc.gridx = 0;
        gc.gridy = 3;
        gc.insets = new Insets(0, 0, UIConfig.INSTANCE.getDefaultPadding() / 3, 0);
        add(widthOfRectangleCenterCenter, gc);

        gc.gridx = 0;
        gc.gridy = 4;
        gc.insets = new Insets(0, 0, UIConfig.INSTANCE.getDefaultPadding() / 3, 0);
        add(heightOfRectangleCenterCenter, gc);

        gc.gridx = 0;
        gc.gridy = 5;
        gc.insets = new Insets(0, 0, UIConfig.INSTANCE.getDefaultPadding() / 3, 0);
        add(depthBox, gc);

        gc.gridx = 0;
        gc.gridy = 6;
        gc.insets = new Insets(0, 0, UIConfig.INSTANCE.getDefaultPadding() / 3, 0);
        add(bitChoiceBox, gc);

        gc.gridx = 0;
        gc.gridy = 7;
        gc.insets = new Insets(0, 0, UIConfig.INSTANCE.getDefaultPadding() / 3, 0);
        add(modifyAnchorBox, gc);
    }

    private CutDTO pointsWidthResizeEdgeEdge(double newWidthEdgeEdge){
        CutDTO c = new CutDTO(cutDTO);
        double height = getHeightEdgeEdge();
        List<VertexDTO> newPoints = mainWindow.getController().generateRectanglePoints(getAnchorCenterPoint(), newWidthEdgeEdge, height);
        for(int i=0; i < c.getPoints().size(); i++){
            c.getPoints().set(i, newPoints.get(i));
        }
        return c;
    }

    private CutDTO pointsHeightResizeEdgeEdge(double newHeightEdgeEdge){
        CutDTO c = new CutDTO(cutDTO);
        double width = getWidthEdgeEdge();
        List<VertexDTO> newPoints = mainWindow.getController().generateRectanglePoints(getAnchorCenterPoint(), width, newHeightEdgeEdge);
        for(int i=0; i < c.getPoints().size(); i++){
            c.getPoints().set(i, newPoints.get(i));
        }
        return c;
    }

    private CutDTO moveAllPointsCenterCenter(double newValue, VertexDTO.AXIS axis){
        CutDTO c = new CutDTO(cutDTO);

        VertexDTO p1 = c.getPoints().getFirst();
        VertexDTO p3 = c.getPoints().get(2);

        VertexDTO centerAnchorPoint = getAnchorCenterPoint();
        double width = p3.sub(p1).getX();;
        double height = p3.sub(p1).getY();

        VertexDTO anchor = VertexDTO.zero();
        if(axis == VertexDTO.AXIS.X){
            anchor = new VertexDTO(newValue, centerAnchorPoint.getY(), centerAnchorPoint.getZ());
        }
        else if(axis == VertexDTO.AXIS.Y){
            anchor = new VertexDTO(centerAnchorPoint.getX(), newValue, centerAnchorPoint.getZ());
        }
        else if(axis == VertexDTO.AXIS.Z){
            anchor = new VertexDTO(centerAnchorPoint.getX(), centerAnchorPoint.getY(), newValue);
        }
        List<VertexDTO> newPoints = mainWindow.getController().generateRectanglePoints(anchor, width, height);

        for(int i=0; i < c.getPoints().size(); i++){
            c.getPoints().set(i, newPoints.get(i));
        }

        return c;
    }

    @Override
    public void setupEventListeners() {
        addEventListenerToResizeInternalWidth(widthOfRectangle);
        addEventListenerToResizeInternalHeight(heightOfRectangle);
        addEventListenerToMoveOffset(offsetOfRectangle);

        addEventListenerToBitChoiceBox(bitChoiceBox);
        addEventListenerToDepth(depthBox);
        addEventListenerModifyAnchor(modifyAnchorBox);
    }

    @Override
    public void updatePanel(CutDTO newCutDTO) {
        cutDTO = newCutDTO;
        offsetOfRectangle.getxInput().setValueInMMWithoutTrigerringListeners(getAnchorCenterPoint().getX());
        offsetOfRectangle.getyInput().setValueInMMWithoutTrigerringListeners(getAnchorCenterPoint().getY());
        offsetOfRectangle.getzInput().setValueInMMWithoutTrigerringListeners(getAnchorCenterPoint().getZ());
        widthOfRectangle.getInput().setValueInMMWithoutTrigerringListeners(getWidthEdgeEdge());
        heightOfRectangle.getInput().setValueInMMWithoutTrigerringListeners(getHeightEdgeEdge());
        widthOfRectangleCenterCenter.getInput().setValueInMMWithoutTrigerringListeners(getWidthCenterCenter());
        heightOfRectangleCenterCenter.getInput().setValueInMMWithoutTrigerringListeners(getHeightCenterCenter());
    }

    private void addEventListenerToResizeInternalWidth(SingleValueBox sb){
        sb.getInput().getNumericInput().addPropertyChangeListener("value", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                double n = sb.getInput().getMMValue();
                CutDTO c = pointsWidthResizeEdgeEdge(n);
                mainWindow.getController().modifyCut(c);
                cutListPanel.modifiedAttributeEventOccured(new ChangeAttributeEvent(cutBox, cutBox));
            }
        });
    }

    private void addEventListenerToResizeInternalHeight(SingleValueBox sb){
        sb.getInput().getNumericInput().addPropertyChangeListener("value", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                double n = sb.getInput().getMMValue();
                CutDTO c = pointsHeightResizeEdgeEdge(n);
                mainWindow.getController().modifyCut(c);
                cutListPanel.modifiedAttributeEventOccured(new ChangeAttributeEvent(cutBox, cutBox));
            }
        });
    }

    private void addEventListenerToMoveOffset(PointsBox pb){
        pb.getxInput().getNumericInput().addPropertyChangeListener("value", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                CutDTO c = moveAllPointsCenterCenter(pb.getxInput().getMMValue(), VertexDTO.AXIS.X);
                mainWindow.getController().modifyCut(c);
                cutListPanel.modifiedAttributeEventOccured(new ChangeAttributeEvent(cutBox, cutBox));
            }
        });

        pb.getyInput().getNumericInput().addPropertyChangeListener("value", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                CutDTO c = moveAllPointsCenterCenter(pb.getyInput().getMMValue(), VertexDTO.AXIS.Y);
                mainWindow.getController().modifyCut(c);
                cutListPanel.modifiedAttributeEventOccured(new ChangeAttributeEvent(cutBox, cutBox));
            }
        });

        pb.getzInput().getNumericInput().addPropertyChangeListener("value", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                CutDTO c = moveAllPointsCenterCenter(pb.getzInput().getMMValue(), VertexDTO.AXIS.Z);
                mainWindow.getController().modifyCut(c);
                cutListPanel.modifiedAttributeEventOccured(new ChangeAttributeEvent(cutBox, cutBox));
            }
        });
    }
}
